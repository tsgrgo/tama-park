import { Graphics } from './Graphics';

export const KEY_PRESSED_EVENT = 0;
export const KEY_RELEASED_EVENT = 1;

// Key constants
export const KEY_0 = 0x00;
export const KEY_ASTERISK = 0x0a; // '*'
export const KEY_POUND = 0x0b; // '#'

// Directions / select
export const KEY_LEFT = 0x10;
export const KEY_UP = 0x11;
export const KEY_RIGHT = 0x12;
export const KEY_DOWN = 0x13;
export const KEY_SELECT = 0x14;

// Soft keys
export const KEY_SOFT1 = 0x15;
export const KEY_SOFT2 = 0x16;

const CANVAS_WIDTH = 240;
const CANVAS_HEIGHT = 240;

export abstract class Canvas {
	private keypadStateBits = 0;
	private isPainting = false;

	private readonly domCanvas: HTMLCanvasElement;
	private readonly domCtx: CanvasRenderingContext2D;

	private readonly bufferCanvas: OffscreenCanvas | HTMLCanvasElement;
	private readonly bufferG: Graphics;

	private tftFilter = true;
	private tftOverlayCanvas?: OffscreenCanvas | HTMLCanvasElement;
	private noiseFrames: (OffscreenCanvas | HTMLCanvasElement)[] = [];
	private noiseIndex = 0;

	protected constructor() {
		// Visible canvas
		this.domCanvas = document.createElement('canvas');
		this.domCanvas.width = CANVAS_WIDTH;
		this.domCanvas.height = CANVAS_HEIGHT;
		this.domCanvas.style.background = 'black';
		this.domCanvas.style.outline = 'none';
		this.domCanvas.style.imageRendering = 'pixelated';
		this.domCtx = this.getContext(this.domCanvas) as CanvasRenderingContext2D;

		document.addEventListener('keydown', e => {
			const dojaKeyCode = Canvas.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, true);
			this.processEvent(KEY_PRESSED_EVENT, dojaKeyCode);
			e.preventDefault();
		});

		document.addEventListener('keyup', e => {
			const dojaKeyCode = Canvas.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, false);
			this.processEvent(KEY_RELEASED_EVENT, dojaKeyCode);
			e.preventDefault();
		});

		// Offscreen buffer
		this.bufferCanvas = new OffscreenCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		const bufferCtx = this.getContext(this.bufferCanvas);
		this.bufferG = new Graphics(bufferCtx);

		this.buildTftOverlays();
	}

	protected abstract paint(g: Graphics): unknown;

	protected abstract processEvent(type: number, param: number): void;

	public getWidth(): number {
		return CANVAS_WIDTH;
	}

	public getHeight(): number {
		return CANVAS_HEIGHT;
	}

	public repaint(): void {
		if (this.isPainting) return;

		this.isPainting = true;
		const res = this.paint(this.bufferG);

		if (res && res instanceof Promise) {
			res.then(() => (this.isPainting = false));
		} else {
			this.isPainting = false;
		}

		this.domCtx.drawImage(this.bufferCanvas, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		if (this.tftFilter) {
			this.applyTftOverlay(this.domCtx);
		}
	}

	public setBackground(color: string): void {
		this.domCanvas.style.background = color;
	}

	public setSoftLabel(which: number, text: string | null): void {
		const t = text ?? '';
		if (which === 0) console.log('left soft label:' + t);
		else if (which === 1) console.log('right soft label:' + t);
	}

	public getKeypadState(): number {
		return this.keypadStateBits;
	}

	public focus(): void {
		this.domCanvas.focus();
	}

	public unwrap(): HTMLCanvasElement {
		return this.domCanvas;
	}

	private static mapDomToDojaKey(e: KeyboardEvent): number {
		const code = e.code;
		const key = e.key;

		// Directions / select
		switch (code) {
			case 'ArrowLeft':
				return KEY_LEFT;
			case 'ArrowRight':
				return KEY_RIGHT;
			case 'ArrowUp':
				return KEY_UP;
			case 'ArrowDown':
				return KEY_DOWN;
			case 'Enter':
			case 'NumpadEnter':
				return KEY_SELECT;

			// Soft keys
			case 'F1':
				return KEY_SOFT1;
			case 'F2':
				return KEY_SOFT2;
		}

		// Digits
		if (code.startsWith('Digit')) {
			const d = Number(code.slice('Digit'.length));
			if (Number.isInteger(d) && d >= 0 && d <= 9) return KEY_0 + d;
		}
		if (code.startsWith('Numpad')) {
			const maybe = code.slice('Numpad'.length);
			const d = Number(maybe);
			if (Number.isInteger(d) && d >= 0 && d <= 9) return KEY_0 + d;
		}

		// Symbols
		if (key === '*') return KEY_ASTERISK;
		if (key === '#') return KEY_POUND;

		// Not mapped
		return -1;
	}

	private updateKeypadState(dojaKeyCode: number, pressed: boolean): void {
		if (dojaKeyCode < 0 || dojaKeyCode >= 32) return;

		const bit = 1 << dojaKeyCode;

		if (pressed) this.keypadStateBits |= bit;
		else this.keypadStateBits &= ~bit;
	}

	private getContext(canvas: OffscreenCanvas | HTMLCanvasElement): CanvasRenderingContext2D | OffscreenCanvasRenderingContext2D {
		const ctx = canvas.getContext('2d') as CanvasRenderingContext2D | OffscreenCanvasRenderingContext2D | null;
		if (!ctx) throw new Error('Failed to get 2D context');

		ctx.imageSmoothingEnabled = false;

		return ctx;
	}

	private makeOffscreen(width: number, height: number): OffscreenCanvas | HTMLCanvasElement {
		if (typeof OffscreenCanvas !== 'undefined') return new OffscreenCanvas(width, height);
		const canvas = document.createElement('canvas');
		canvas.width = width;
		canvas.height = height;
		return canvas;
	}

	private buildTftOverlays(): void {
		const w = CANVAS_WIDTH;
		const h = CANVAS_HEIGHT;

		const overlay = this.makeOffscreen(w, h);
		const overlayCtx = this.getContext(overlay);
		overlayCtx.clearRect(0, 0, w, h);

		// Create a tiny 3x1 RGB stripe tile
		const tile = this.makeOffscreen(3, 1);
		const tileCtx = this.getContext(tile);
		tileCtx.clearRect(0, 0, 3, 1);

		// Base dim (aperture)
		tileCtx.fillStyle = 'rgba(0, 0, 0, 0.08)';
		tileCtx.fillRect(0, 0, 3, 1);

		// Subpixel boosts (tweak alphas)
		tileCtx.fillStyle = 'rgba(255,0,0,0.18)';
		tileCtx.fillRect(0, 0, 1, 1);
		tileCtx.fillStyle = 'rgba(0,255,0,0.14)';
		tileCtx.fillRect(1, 0, 1, 1);
		tileCtx.fillStyle = 'rgba(0,0,255,0.18)';
		tileCtx.fillRect(2, 0, 1, 1);

		const pat = (overlayCtx as CanvasRenderingContext2D).createPattern(tile as any, 'repeat');
		if (!pat) throw new Error('Failed to create TFT pattern.');

		// Paint the overlay once
		overlayCtx.save();
		overlayCtx.globalCompositeOperation = 'source-over';
		overlayCtx.fillStyle = pat as any;
		overlayCtx.fillRect(0, 0, w, h);

		// Scan lines
		overlayCtx.globalCompositeOperation = 'source-over';
		overlayCtx.globalAlpha = 0.05;
		overlayCtx.fillStyle = 'black';
		for (let y = 0; y < h; y += 2) overlayCtx.fillRect(0, y, w, 1);
		overlayCtx.restore();

		this.tftOverlayCanvas = overlay;

		// Precompute noise frames
		this.noiseFrames = [];
		const noiseSize = 64;
		const frames = 8;

		for (let f = 0; f < frames; f++) {
			const noise = this.makeOffscreen(noiseSize, noiseSize);
			const noiseCtx = this.getContext(noise);

			const img = noiseCtx.getImageData(0, 0, noiseSize, noiseSize);
			const data = img.data;
			for (let i = 0; i < data.length; i += 4) {
				const v = (Math.random() * 256) | 0;
				data[i] = v;
				data[i + 1] = v;
				data[i + 2] = v;
				data[i + 3] = 255;
			}
			noiseCtx.putImageData(img, 0, 0);
			this.noiseFrames.push(noise);
		}
	}

	private applyTftOverlay(ctx: CanvasRenderingContext2D): void {
		ctx.save();
		ctx.filter = 'contrast(1.08) saturate(1.05) brightness(0.98)';

		if (this.tftOverlayCanvas) {
			ctx.globalCompositeOperation = 'multiply';
			ctx.globalAlpha = 0.8;
			ctx.drawImage(this.tftOverlayCanvas as any, 0, 0);
		}

		if (this.noiseFrames.length) {
			const n = this.noiseFrames[this.noiseIndex++ % this.noiseFrames.length];
			ctx.globalCompositeOperation = 'overlay';
			ctx.globalAlpha = 0.025;
			const ox = (this.noiseIndex * 7) % 64;
			const oy = (this.noiseIndex * 11) % 64;
			ctx.drawImage(n as any, -ox, -oy, CANVAS_WIDTH, CANVAS_HEIGHT);
			ctx.drawImage(n as any, CANVAS_WIDTH - ox, -oy, CANVAS_WIDTH, CANVAS_HEIGHT);
			ctx.drawImage(n as any, -ox, CANVAS_HEIGHT - oy, CANVAS_WIDTH, CANVAS_HEIGHT);
			ctx.drawImage(n as any, CANVAS_WIDTH - ox, CANVAS_HEIGHT - oy, CANVAS_WIDTH, CANVAS_HEIGHT);
		}

		ctx.restore();
	}
}
