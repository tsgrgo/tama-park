import { Graphics } from './Graphics';

export abstract class CanvasBase {
	protected readonly canvasEl: HTMLCanvasElement;
	protected readonly ctx: CanvasRenderingContext2D;
	protected readonly g: Graphics;

	constructor(canvasEl: HTMLCanvasElement) {
		const ctx = canvasEl.getContext('2d');
		if (!ctx) throw new Error('2D context not available');

		this.canvasEl = canvasEl;
		this.ctx = ctx;
		this.g = new Graphics(ctx);

		window.addEventListener('keydown', e =>
			this.processEvent(1, e.keyCode)
		);
		window.addEventListener('keyup', e => this.processEvent(2, e.keyCode));

		this.canvasEl.tabIndex = 0;
		this.canvasEl.addEventListener('click', () => this.canvasEl.focus());
	}

	getWidth(): number {
		return this.canvasEl.width;
	}

	getHeight(): number {
		return this.canvasEl.height;
	}

	repaint(): void {
		this.paint(this.g);
	}

	abstract processEvent(type: number, param: number): void;

	abstract paint(g: Graphics): void;
}
