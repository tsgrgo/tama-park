export class MediaSound {
	private data: Uint8Array;

	constructor(data: Uint8Array /*| ArrayBuffer*/) {
		this.data = data;
	}

	public unwrap() {
		return this.data;
	}

	public use() {}
}
