export class Graphics {
	private ctx: CanvasRenderingContext2D;
	private locked = false;

	constructor(ctx: CanvasRenderingContext2D) {
		this.ctx = ctx;
	}

	lock(): void {
		this.locked = true;
		// this.ctx.save();
	}

	unlock(_present: boolean): void {
		this.locked = false;
		// this.ctx.restore();
	}

	setFont(font: string): void {
		this.ctx.font = font;
	}

	setColor(cssColor: string): void {
		this.ctx.fillStyle = cssColor;
		this.ctx.strokeStyle = cssColor;
	}

	fillRect(x: number, y: number, w: number, h: number): void {
		this.ctx.fillRect(x, y, w, h);
	}

	drawRect(x: number, y: number, w: number, h: number): void {
		this.ctx.strokeRect(x, y, w, h);
	}

	drawString(text: string, x: number, y: number): void {
		this.ctx.fillText(text, x, y);
	}

	getContext(): CanvasRenderingContext2D {
		return this.ctx;
	}
}
