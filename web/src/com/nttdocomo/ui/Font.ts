interface FontMetrics {
	ascent: number;
	descent: number;
	height: number;
}

export class Font {
	//
	// |<- 4 ->|<- 4 ->|<----- 8 ----->|<----- 8 ----->|<----- 8 ----->|
	// +-------+-------+-------+-------+-------+-------+-------+-------+
	// | 0111  | face  |         style |         size  |      -        |
	// +-------+-------+---------------+---------------+-------+-------+
	// Or last is type and everything else is 0
	//
	public static readonly TYPE_DEFAULT = 0x00000000;
	public static readonly TYPE_HEADING = 0x00000001;

	public static readonly FACE_SYSTEM = 0x71000000;
	public static readonly FACE_MONOSPACE = 0x72000000;
	public static readonly FACE_PROPORTIONAL = 0x73000000;

	public static readonly STYLE_PLAIN = 0x70100000;
	public static readonly STYLE_BOLD = 0x70110000;
	public static readonly STYLE_ITALIC = 0x70120000;
	public static readonly STYLE_BOLDITALIC = 0x70130000;

	public static readonly SIZE_SMALL = 0x70000100;
	public static readonly SIZE_MEDIUM = 0x70000200;
	public static readonly SIZE_LARGE = 0x70000300;
	public static readonly SIZE_TINY = 0x70000400;

	public static readonly DEFAULT_FONT = Font.FACE_SYSTEM | Font.SIZE_TINY | Font.STYLE_PLAIN;

	private static readonly FACE_MASK = 0xff000000;
	private static readonly STYLE_MASK = 0xf0ff0000;
	private static readonly SIZE_MASK = 0xf000ff00;

	private static readonly CACHE = new Map<number, Font>();

	private static readonly METRICS_CTX: CanvasRenderingContext2D = (() => {
		const c = document.createElement('canvas');
		c.width = 1;
		c.height = 1;
		const ctx = c.getContext('2d');
		if (!ctx) throw new Error('Failed to create 2D context for Font metrics.');
		ctx.textBaseline = 'alphabetic';
		ctx.textAlign = 'left';
		return ctx;
	})();

	private readonly cssFont: string;
	private metricsCache?: FontMetrics;

	private constructor(cssFont: string) {
		this.cssFont = cssFont;
	}

	public static getFont(spec: number): Font {
		const existing = Font.CACHE.get(spec);
		if (existing) return existing;

		const created = Font.createFont(spec);
		Font.CACHE.set(spec, created);
		return created;
	}

	public unwrap(): string {
		return this.cssFont;
	}

	public getHeight(): number {
		return this.getMetrics().height;
	}

	public getDescent(): number {
		return this.getMetrics().descent;
	}

	public stringWidth(str: string | null | undefined): number {
		if (!str) return 0;
		const ctx = Font.METRICS_CTX;
		ctx.font = this.cssFont;
		return Math.round(ctx.measureText(str).width);
	}

	private getMetrics(): FontMetrics {
		if (this.metricsCache) return this.metricsCache;

		const ctx = Font.METRICS_CTX;
		ctx.font = this.cssFont;

		const textMetrics = ctx.measureText('Mg');

		const ascent = Math.ceil(textMetrics.actualBoundingBoxAscent);
		const descent = Math.ceil(textMetrics.actualBoundingBoxDescent);
		const height = ascent + descent;

		this.metricsCache = { ascent, descent, height };
		return this.metricsCache;
	}

	private static createFont(spec: number): Font {
		// TYPE form: low 8 bits only (everything else 0)
		if ((spec & 0xffffff00) === 0) {
			const type = spec & 0xff;
			return Font.createTypeFont(type);
		}

		// FACE|STYLE|SIZE form
		const face = spec & Font.FACE_MASK;
		const style = spec & Font.STYLE_MASK;
		const size = spec & Font.SIZE_MASK;

		const family = Font.mapFace(face);
		const { weight, fontStyle } = Font.mapStyle(style);
		const px = Font.mapSize(size);

		const css = `${fontStyle} ${weight} ${px}px ${family}`.trim().replace(/\s+/g, ' ');

		return new Font(css);
	}

	private static createTypeFont(type: number): Font {
		switch (type) {
			case Font.TYPE_HEADING:
				return new Font(`normal 700 18px system-ui`);
			case Font.TYPE_DEFAULT:
			default:
				return new Font(`normal 400 10px system-ui`);
		}
	}

	private static mapFace(face: number): string {
		switch (face) {
			case Font.FACE_SYSTEM:
				return `MS Gothic, system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif`;
			case Font.FACE_MONOSPACE:
				return `ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace`;
			case Font.FACE_PROPORTIONAL:
				return `Arial, Helvetica, sans-serif`;
			default:
				return `system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif`;
		}
	}

	private static mapStyle(style: number): {
		weight: string;
		fontStyle: string;
	} {
		switch (style) {
			case Font.STYLE_PLAIN:
				return { weight: '400', fontStyle: 'normal' };
			case Font.STYLE_BOLD:
				return { weight: '700', fontStyle: 'normal' };
			case Font.STYLE_ITALIC:
				return { weight: '400', fontStyle: 'italic' };
			case Font.STYLE_BOLDITALIC:
				return { weight: '700', fontStyle: 'italic' };
			default:
				return { weight: '400', fontStyle: 'normal' };
		}
	}

	private static mapSize(size: number): number {
		switch (size) {
			case Font.SIZE_TINY:
				return 12;
			case Font.SIZE_SMALL:
				return 18;
			case Font.SIZE_MEDIUM:
				return 24;
			case Font.SIZE_LARGE:
				return 26;
			default:
				return 12;
		}
	}

	private static guessPxSize(cssFont: string): number {
		const m = cssFont.match(/(\d+(?:\.\d+)?)px\b/);
		if (!m) return 12;
		const v = Number(m[1]);
		return Number.isFinite(v) ? v : 12;
	}
}
