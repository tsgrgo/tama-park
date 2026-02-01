export abstract class InputStream {
	private closed = false;

	/** Reads one byte [0..255], or -1 on EOF */
	public async read(): Promise<number> {
		const buf = new Uint8Array(1);
		const n = await this.readBytes(buf, 0, 1);
		if (n <= 0) return -1;
		return buf[0];
	}

	/**
	 * Reads up to `len` bytes into `b` at offset `off`.
	 * Returns number of bytes read, or -1 on EOF.
	 */
	public abstract readBytes(b: Uint8Array, off: number, len: number): Promise<number>;

	/**
	 * Skips up to n bytes. Returns the actual number skipped.
	 * Default implementation reads-and-discards into a temp buffer.
	 */
	public async skip(n: number): Promise<number> {
		if (n <= 0) return 0;

		const tmp = new Uint8Array(Math.min(8192, n));
		let remaining = n;
		let skipped = 0;

		while (remaining > 0) {
			const toRead = Math.min(tmp.length, remaining);
			const r = await this.readBytes(tmp, 0, toRead);
			if (r <= 0) break;
			skipped += r;
			remaining -= r;
		}

		return skipped;
	}

	/**
	 * Returns an estimate of bytes that can be read without blocking.
	 * In the browser this is often 0 unless the stream is fully buffered.
	 */
	public async available(): Promise<number> {
		return 0;
	}

	public async close(): Promise<void> {
		this.closed = true;
	}

	protected ensureOpen(): void {
		if (this.closed) throw new Error('Stream closed');
	}
}
