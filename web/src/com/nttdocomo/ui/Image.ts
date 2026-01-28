export class Image {
	protected source: CanvasImageSource | null;

	protected constructor(source: CanvasImageSource | null) {
		this.source = source;
	}

	public unwrap(): CanvasImageSource | null {
		return this.source;
	}

	public getWidth(): number {
		const s = this.source;
		if (!s) return 0;

		if (s instanceof HTMLImageElement) return s.naturalWidth || 0;
		if (s instanceof HTMLCanvasElement) return s.width || 0;
		if (
			typeof OffscreenCanvas !== 'undefined' &&
			s instanceof OffscreenCanvas
		)
			return s.width || 0;
		if (typeof ImageBitmap !== 'undefined' && s instanceof ImageBitmap)
			return s.width || 0;
		if (s instanceof SVGImageElement)
			return s.width?.baseVal?.value
				? Math.round(s.width.baseVal.value)
				: 0;

		return 0;
	}

	public getHeight(): number {
		const s = this.source;
		if (!s) return 0;

		if (s instanceof HTMLImageElement) return s.naturalHeight || 0;
		if (s instanceof HTMLCanvasElement) return s.height || 0;
		if (
			typeof OffscreenCanvas !== 'undefined' &&
			s instanceof OffscreenCanvas
		)
			return s.height || 0;
		if (typeof ImageBitmap !== 'undefined' && s instanceof ImageBitmap)
			return s.height || 0;
		if (s instanceof SVGImageElement)
			return s.height?.baseVal?.value
				? Math.round(s.height.baseVal.value)
				: 0;

		return 0;
	}

	public dispose(): void {
		this.source = null;
	}
}
