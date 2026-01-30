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

	private readonly domCanvas: HTMLCanvasElement;
	private readonly bufferCanvas: OffscreenCanvas | HTMLCanvasElement;
	protected readonly bufferG: Graphics;

	protected constructor() {
		// Visible canvas
		this.domCanvas = document.createElement('canvas');
		this.domCanvas.width = CANVAS_WIDTH;
		this.domCanvas.height = CANVAS_HEIGHT;
		this.domCanvas.tabIndex = 0; // make focusable
		this.domCanvas.style.background = 'black';
		this.domCanvas.style.outline = 'none';
		this.domCanvas.style.imageRendering = 'pixelated';

		// Offscreen buffer
		this.bufferCanvas = new OffscreenCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		const ctx = this.bufferCanvas.getContext('2d');
		if (!ctx) throw new Error('Failed to get 2D context (offscreen).');
		this.bufferG = new Graphics(ctx);

		this.domCanvas.addEventListener('keydown', e => {
			const dojaKeyCode = Canvas.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, true);
			this.processEvent(KEY_PRESSED_EVENT, dojaKeyCode);
			e.preventDefault();
		});

		this.domCanvas.addEventListener('keyup', e => {
			const dojaKeyCode = Canvas.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, false);
			this.processEvent(KEY_RELEASED_EVENT, dojaKeyCode);
			e.preventDefault();
		});
	}

	protected abstract paint(g: Graphics): void;

	protected abstract processEvent(type: number, param: number): void;

	public getWidth(): number {
		return CANVAS_WIDTH;
	}

	public getHeight(): number {
		return CANVAS_HEIGHT;
	}

	public repaint(): void {
		this.paint(this.bufferG);
		const g = this.domCanvas.getContext('2d');
		if (!g) return;

		// g.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		g.drawImage(this.bufferCanvas, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		// this.paint(new Graphics(g));
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

	public unwrap(): HTMLCanvasElement {
		return this.domCanvas;
	}

	public focus(): void {
		this.domCanvas.focus();
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
}
