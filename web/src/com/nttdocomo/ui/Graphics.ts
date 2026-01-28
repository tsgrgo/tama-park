import type { Font } from './Font';
import type { Image } from './Image';

export class Graphics {
	private readonly g:
		| CanvasRenderingContext2D
		| OffscreenCanvasRenderingContext2D;

	constructor(
		g: CanvasRenderingContext2D | OffscreenCanvasRenderingContext2D
	) {
		this.g = g;
	}

	public static getColorOfName(name: number): string {
		// prettier-ignore
		switch (name) {
			case 0: return '#000000'; // BLACK
			case 1: return '#0000ff'; // BLUE
			case 2: return '#00ff00'; // LIME
			case 3: return '#00ffff'; // CYAN / AQUA
			case 4: return '#ff0000'; // RED
			case 5: return '#ff00ff'; // MAGENTA / FUCHSIA
			case 6: return '#ffff00'; // YELLOW
			case 7: return '#ffffff'; // WHITE
			case 8: return '#808080'; // GRAY
			case 9: return '#000080'; // NAVY
			case 10: return '#008000'; // GREEN (dark)
			case 11: return '#008080'; // TEAL
			case 12: return '#800000'; // MAROON
			case 13: return '#800080'; // PURPLE
			case 14: return '#808000'; // OLIVE
			case 15: return '#c0c0c0'; // SILVER
			default: return '#000000';
		}
	}

	public static getColorOfRGB(r: number, g: number, b: number): number {
		r = Graphics.clamp8(r);
		g = Graphics.clamp8(g);
		b = Graphics.clamp8(b);
		return (r << 16) | (g << 8) | b;
	}

	private static clamp8(v: number): number {
		if (v < 0) return 0;
		if (v > 255) return 255;
		return v | 0;
	}

	public lock(): void {
		// no-op: double buffering is already done in Canvas
	}

	public unlock(_present: boolean): void {
		// no-op: double buffering is already done in Canvas
	}

	public setFont(font: Font | null): void {
		if (!font) return;
		const css = font.unwrap();
		if (css) this.g.font = css;
	}

	public setColor(rgb: number): void {
		const r = (rgb >> 16) & 0xff;
		const g = (rgb >> 8) & 0xff;
		const b = rgb & 0xff;
		const css = `rgb(${r},${g},${b})`;

		this.g.fillStyle = css;
		this.g.strokeStyle = css;
	}

	public fillRect(x: number, y: number, w: number, h: number): void {
		this.g.fillRect(x, y, w, h);
	}

	public drawRect(x: number, y: number, w: number, h: number): void {
		this.g.strokeRect(x, y, w, h);
	}

	public fillArc(
		x: number,
		y: number,
		w: number,
		h: number,
		start: number,
		arc: number
	): void {
		const cx = x + w / 2;
		const cy = y + h / 2;
		const rx = w / 2;
		const ry = h / 2;

		const startRad = (start * Math.PI) / 180;
		const endRad = ((start + arc) * Math.PI) / 180;

		this.g.beginPath();
		this.g.ellipse(cx, cy, rx, ry, 0, startRad, endRad, arc < 0);
		this.g.lineTo(cx, cy);
		this.g.closePath();
		this.g.fill();
	}

	public drawString(str: string, x: number, y: number): void {
		this.g.fillText(str, x, y);
	}

	public setClip(x: number, y: number, w: number, h: number): void {
		this.g.resetTransform();
		this.g.beginPath();
		this.g.rect(x, y, w, h);
		this.g.clip();
	}

	public drawImage(img: Image | null, x: number, y: number): void {
		if (!img) return;
		const src = img.unwrap();
		if (!src) return;
		this.g.drawImage(src, x, y);
	}
}
