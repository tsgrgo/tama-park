// Java-like DataOutputStream on top of our async OutputStream.
// - Big-endian numeric writes
// - writeUTF uses Java "modified UTF-8" with 2-byte unsigned length prefix
//
// Notes:
// - All methods are async because OutputStream is async in the browser.

import { OutputStream } from './OutputStream';
import { UTFDataFormatException } from './DataInputStream'; // reuse the same error type

export class DataOutputStream {
	private readonly out: OutputStream;
	private readonly scratch8 = new Uint8Array(8);

	constructor(out: OutputStream) {
		this.out = out;
	}

	public async flush(): Promise<void> {
		await this.out.flush();
	}

	public async close(): Promise<void> {
		await this.out.close();
	}

	// ---------------- basic writes ----------------

	public async writeBoolean(v: boolean): Promise<void> {
		await this.out.write(v ? 1 : 0);
	}

	/** Writes the low 8 bits */
	public async writeByte(v: number): Promise<void> {
		await this.out.write(v);
	}

	/** Writes bytes from a buffer */
	public async write(b: Uint8Array, off = 0, len = b.length - off): Promise<void> {
		if (len <= 0) return;
		await this.out.writeBytes(b, off, len);
	}

	/** Writes a 16-bit signed short (big-endian) */
	public async writeShort(v: number): Promise<void> {
		this.scratch8[0] = (v >>> 8) & 0xff;
		this.scratch8[1] = v & 0xff;
		await this.out.writeBytes(this.scratch8, 0, 2);
	}

	/** Writes a 16-bit char (0..65535) big-endian */
	public async writeChar(v: number): Promise<void> {
		await this.writeShort(v & 0xffff);
	}

	/** Writes a 32-bit int (big-endian) */
	public async writeInt(v: number): Promise<void> {
		this.scratch8[0] = (v >>> 24) & 0xff;
		this.scratch8[1] = (v >>> 16) & 0xff;
		this.scratch8[2] = (v >>> 8) & 0xff;
		this.scratch8[3] = v & 0xff;
		await this.out.writeBytes(this.scratch8, 0, 4);
	}

	/**
	 * Writes a 64-bit long (big-endian).
	 * Use bigint for correctness (JS number cannot safely represent 64-bit ints).
	 */
	public async writeLong(v: bigint): Promise<void> {
		// Convert to unsigned 64-bit representation
		let x = v;
		if (x < 0n) x = x + (1n << 64n);

		for (let i = 7; i >= 0; i--) {
			this.scratch8[i] = Number(x & 0xffn);
			x >>= 8n;
		}
		await this.out.writeBytes(this.scratch8, 0, 8);
	}

	/** Writes IEEE-754 float (32-bit) big-endian */
	public async writeFloat(v: number): Promise<void> {
		const dv = new DataView(this.scratch8.buffer, this.scratch8.byteOffset, 4);
		dv.setFloat32(0, v, false);
		await this.out.writeBytes(this.scratch8, 0, 4);
	}

	/** Writes IEEE-754 double (64-bit) big-endian */
	public async writeDouble(v: number): Promise<void> {
		const dv = new DataView(this.scratch8.buffer, this.scratch8.byteOffset, 8);
		dv.setFloat64(0, v, false);
		await this.out.writeBytes(this.scratch8, 0, 8);
	}

	// ---------------- string-ish helpers ----------------

	/**
	 * Java DataOutputStream.writeBytes(String):
	 * writes the low 8 bits of each char (ISO-8859-1-ish truncation).
	 */
	public async writeBytes(str: string): Promise<void> {
		if (!str) return;
		const buf = new Uint8Array(str.length);
		for (let i = 0; i < str.length; i++) {
			buf[i] = str.charCodeAt(i) & 0xff;
		}
		await this.out.writeBytes(buf, 0, buf.length);
	}

	/**
	 * Java DataOutputStream.writeChars(String):
	 * writes each char as 2 bytes (big-endian UTF-16 code unit).
	 */
	public async writeChars(str: string): Promise<void> {
		if (!str) return;
		// Chunk to avoid huge allocations for very large strings
		const CHUNK = 4096;
		for (let i = 0; i < str.length; i += CHUNK) {
			const end = Math.min(str.length, i + CHUNK);
			const buf = new Uint8Array((end - i) * 2);
			let p = 0;
			for (let j = i; j < end; j++) {
				const ch = str.charCodeAt(j) & 0xffff;
				buf[p++] = (ch >>> 8) & 0xff;
				buf[p++] = ch & 0xff;
			}
			await this.out.writeBytes(buf, 0, buf.length);
		}
	}

	/**
	 * Java DataOutputStream.writeUTF(String):
	 * - 2-byte unsigned length (number of encoded bytes)
	 * - modified UTF-8 bytes
	 */
	public async writeUTF(str: string): Promise<void> {
		const encoded = DataOutputStream.encodeModifiedUTF8(str ?? '');
		if (encoded.length > 65535) {
			throw new UTFDataFormatException(`encoded string too long: ${encoded.length} bytes`);
		}

		await this.writeShort(encoded.length); // unsigned short length
		if (encoded.length > 0) {
			await this.out.writeBytes(encoded, 0, encoded.length);
		}
	}

	// ---------------- modified UTF-8 encoding ----------------

	/**
	 * Java "modified UTF-8":
	 * - U+0000 encoded as 0xC0 0x80 (never as 0x00)
	 * - U+0001..U+007F -> 1 byte
	 * - U+0080..U+07FF -> 2 bytes
	 * - otherwise -> 3 bytes
	 * - surrogate pairs are *not* combined; each UTF-16 code unit is encoded separately (3 bytes each)
	 */
	private static encodeModifiedUTF8(str: string): Uint8Array {
		// First pass: compute byte length
		let utflen = 0;
		for (let i = 0; i < str.length; i++) {
			const c = str.charCodeAt(i);

			if (c >= 0x0001 && c <= 0x007f) {
				utflen += 1;
			} else if (c === 0x0000 || (c >= 0x0080 && c <= 0x07ff)) {
				utflen += 2;
			} else {
				utflen += 3;
			}
		}

		const out = new Uint8Array(utflen);
		let p = 0;

		for (let i = 0; i < str.length; i++) {
			const c = str.charCodeAt(i);

			if (c >= 0x0001 && c <= 0x007f) {
				out[p++] = c & 0x7f;
			} else if (c === 0x0000 || (c >= 0x0080 && c <= 0x07ff)) {
				out[p++] = 0xc0 | ((c >> 6) & 0x1f);
				out[p++] = 0x80 | (c & 0x3f);
			} else {
				out[p++] = 0xe0 | ((c >> 12) & 0x0f);
				out[p++] = 0x80 | ((c >> 6) & 0x3f);
				out[p++] = 0x80 | (c & 0x3f);
			}
		}

		return out;
	}
}
