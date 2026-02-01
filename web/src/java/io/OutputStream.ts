export abstract class OutputStream {
	private closed = false;

	/** Writes one byte (only low 8 bits used). */
	public async write(b: number): Promise<void> {
		const one = new Uint8Array([b & 0xff]);
		await this.writeBytes(one, 0, 1);
	}

	/** Writes bytes from `b[off..off+len)` */
	public abstract writeBytes(b: Uint8Array, off: number, len: number): Promise<void>;

	/** Convenience: write entire buffer */
	public async writeAll(b: Uint8Array): Promise<void> {
		await this.writeBytes(b, 0, b.length);
	}

	/** Flush (optional). Default no-op. */
	public async flush(): Promise<void> {}

	public async close(): Promise<void> {
		this.closed = true;
	}

	protected ensureOpen(): void {
		if (this.closed) throw new Error('Stream closed');
	}
}
