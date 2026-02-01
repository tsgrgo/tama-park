import { OutputStream } from './OutputStream';

export class ByteArrayOutputStream extends OutputStream {
	private chunks: Uint8Array[] = [];
	private size = 0;

	public override async writeBytes(b: Uint8Array, off: number, len: number): Promise<void> {
		this.ensureOpen();
		if (len <= 0) return;

		const chunk = b.subarray(off, off + len);
		// Copy to avoid holding onto a larger backing buffer
		const copy = new Uint8Array(chunk.length);
		copy.set(chunk);
		this.chunks.push(copy);
		this.size += copy.length;
	}

	public toByteArray(): Uint8Array {
		const out = new Uint8Array(this.size);
		let p = 0;
		for (const c of this.chunks) {
			out.set(c, p);
			p += c.length;
		}
		return out;
	}

	public reset(): void {
		this.chunks = [];
		this.size = 0;
	}

	public override async close(): Promise<void> {
		await super.close();
	}
}
