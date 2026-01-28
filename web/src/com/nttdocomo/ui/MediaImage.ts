import { Image } from './Image';

export class MediaImage extends Image {
	public readonly ready: Promise<void>;
	private objectUrl: string | null = null;

	constructor(data: Uint8Array | ArrayBuffer) {
		super(null); // don't have the decoded data yet

		const bytes = data
			? data instanceof Uint8Array
				? data
				: new Uint8Array(data)
			: new Uint8Array(0);

		this.ready = this.decode(bytes);
	}

	public use(): void {
		/* no-op */
	}

	public getImage(): Image {
		return this;
	}

	public override dispose(): void {
		super.dispose();

		if (this.objectUrl) {
			URL.revokeObjectURL(this.objectUrl);
			this.objectUrl = null;
		}
	}

	private async decode(bytes: Uint8Array): Promise<void> {
		try {
			const blob = new Blob([new Uint8Array(bytes).buffer]);
			const url = URL.createObjectURL(blob);
			this.objectUrl = url;

			const img = new window.Image();
			img.crossOrigin = 'anonymous';

			await new Promise<void>((resolve, reject) => {
				img.onload = () => resolve();
				img.onerror = () =>
					reject(new Error('Failed to decode image bytes.'));
				img.src = url;
			});

			this.source = img;
		} catch (e) {
			console.log('Image decode error:', e);
			this.source = null;
		}
	}
}
