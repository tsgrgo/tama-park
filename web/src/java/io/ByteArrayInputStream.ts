import { InputStream } from './InputStream';

export class ByteArrayInputStream extends InputStream {
	private pos = 0;
	private readonly buf: Uint8Array;

	constructor(data: Uint8Array) {
		super();
		this.buf = data ?? new Uint8Array(0);
	}

	public override async readBytes(b: Uint8Array, off: number, len: number): Promise<number> {
		this.ensureOpen();
		if (len <= 0) return 0;
		if (this.pos >= this.buf.length) return -1;

		const n = Math.min(len, this.buf.length - this.pos);
		b.set(this.buf.subarray(this.pos, this.pos + n), off);
		this.pos += n;
		return n;
	}

	public override async skip(n: number): Promise<number> {
		this.ensureOpen();
		if (n <= 0) return 0;
		const k = Math.min(n, this.buf.length - this.pos);
		this.pos += k;
		return k;
	}

	public override async available(): Promise<number> {
		this.ensureOpen();
		return Math.max(0, this.buf.length - this.pos);
	}

	public override async close(): Promise<void> {
		await super.close();
	}
}
