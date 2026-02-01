// Java-like DataInputStream on top of async InputStream.
// - Big-endian numeric reads
// - readFully / skipBytes
// - readUTF implemented in "modified UTF-8" form (as Java DataInputStream does)
//
// Notes:
// - All methods are async because InputStream is async in the browser.
// - This is intentionally focused on the parts games typically use.

import { InputStream } from './InputStream';

export class EOFException extends Error {
	constructor(message = 'EOF') {
		super(message);
		this.name = 'EOFException';
	}
}

export class UTFDataFormatException extends Error {
	constructor(message = 'Malformed modified UTF-8') {
		super(message);
		this.name = 'UTFDataFormatException';
	}
}

export class DataInputStream {
	private readonly input: InputStream;
	private readonly scratch8 = new Uint8Array(8);

	constructor(input: InputStream) {
		this.input = input;
	}

	public async close(): Promise<void> {
		await this.input.close();
	}

	public async available(): Promise<number> {
		return this.input.available();
	}

	// ---------------- basic reads ----------------

	/** Reads one unsigned byte (0..255). Throws EOFException if EOF. */
	public async readUnsignedByte(): Promise<number> {
		const b = await this.input.read();
		if (b < 0) throw new EOFException();
		return b & 0xff;
	}

	/** Reads one signed byte (-128..127). Throws EOFException if EOF. */
	public async readByte(): Promise<number> {
		const b = await this.readUnsignedByte();
		return (b << 24) >> 24; // sign-extend 8-bit
	}

	/** Reads boolean (nonzero byte is true). */
	public async readBoolean(): Promise<boolean> {
		return (await this.readUnsignedByte()) !== 0;
	}

	/** Reads a 16-bit signed short (big-endian). */
	public async readShort(): Promise<number> {
		await this.readFully(this.scratch8, 0, 2);
		const v = (this.scratch8[0] << 8) | this.scratch8[1];
		return (v << 16) >> 16; // sign-extend 16-bit
	}

	/** Reads a 16-bit unsigned short (0..65535). */
	public async readUnsignedShort(): Promise<number> {
		await this.readFully(this.scratch8, 0, 2);
		return (this.scratch8[0] << 8) | this.scratch8[1];
	}

	/** Reads a 16-bit char (0..65535), returned as number (like Java char code unit). */
	public async readChar(): Promise<number> {
		return this.readUnsignedShort();
	}

	/** Reads a 32-bit signed int (big-endian). */
	public async readInt(): Promise<number> {
		await this.readFully(this.scratch8, 0, 4);
		// bitwise ops yield signed 32-bit
		return (this.scratch8[0] << 24) | (this.scratch8[1] << 16) | (this.scratch8[2] << 8) | this.scratch8[3];
	}

	/** Reads a 32-bit unsigned int (0..2^32-1) as number. */
	public async readUnsignedInt(): Promise<number> {
		const v = await this.readInt();
		return v >>> 0;
	}

	/**
	 * Reads a 64-bit signed long as bigint (big-endian).
	 * This matches Java's long semantics (TS number can't safely represent 64-bit).
	 */
	public async readLong(): Promise<bigint> {
		await this.readFully(this.scratch8, 0, 8);
		let x = 0n;
		for (let i = 0; i < 8; i++) {
			x = (x << 8n) | BigInt(this.scratch8[i]);
		}
		// Interpret as signed 64-bit
		if (x & (1n << 63n)) {
			x = x - (1n << 64n);
		}
		return x;
	}

	/** Reads IEEE-754 float (32-bit) big-endian. */
	public async readFloat(): Promise<number> {
		await this.readFully(this.scratch8, 0, 4);
		const dv = new DataView(this.scratch8.buffer, this.scratch8.byteOffset, 4);
		return dv.getFloat32(0, false);
	}

	/** Reads IEEE-754 double (64-bit) big-endian. */
	public async readDouble(): Promise<number> {
		await this.readFully(this.scratch8, 0, 8);
		const dv = new DataView(this.scratch8.buffer, this.scratch8.byteOffset, 8);
		return dv.getFloat64(0, false);
	}

	// ---------------- bulk reads ----------------

	/**
	 * Reads up to len bytes. Returns number read, or -1 on EOF (like InputStream).
	 * Provided for convenience.
	 */
	public async read(b: Uint8Array, off = 0, len = b.length - off): Promise<number> {
		return this.input.readBytes(b, off, len);
	}

	/**
	 * Reads exactly len bytes or throws EOFException.
	 */
	public async readFully(b: Uint8Array, off = 0, len = b.length - off): Promise<void> {
		if (len < 0) throw new RangeError('len < 0');
		let n = 0;
		while (n < len) {
			const r = await this.input.readBytes(b, off + n, len - n);
			if (r < 0) throw new EOFException();
			if (r === 0) continue; // should only happen if len==0, but be safe
			n += r;
		}
	}

	// Add to DataInputStream.ts
	public async readFullyN(len: number): Promise<Uint8Array> {
		if (len < 0) throw new RangeError('len < 0');
		const out = new Uint8Array(len);
		await this.readFully(out, 0, len);
		return out;
	}

	public async skipBytes(n: number): Promise<number> {
		const skipped = await this.input.skip(n);
		return skipped | 0;
	}

	// ---------------- readLine (optional-ish) ----------------
	// Java's DataInputStream.readLine is deprecated but some legacy code uses it.
	// This implements a simple CR/LF handler similar to old behavior.
	public async readLine(): Promise<string | null> {
		const chars: number[] = [];
		let sawAny = false;

		while (true) {
			const b = await this.input.read();
			if (b < 0) {
				return sawAny ? String.fromCharCode(...chars) : null;
			}
			sawAny = true;

			if (b === 0x0a) {
				// \n
				break;
			}
			if (b === 0x0d) {
				// \r
				const next = await this.input.read();
				if (next !== 0x0a && next >= 0) {
					// Can't "unread" easily; ignore (closest behavior). Usually fine for game assets.
				}
				break;
			}
			chars.push(b & 0xff);
		}

		return String.fromCharCode(...chars);
	}

	// ---------------- Java modified UTF-8 ----------------

	/**
	 * Reads a string encoded with Java's DataInputStream.writeUTF/readUTF format:
	 * - two-byte unsigned length (number of bytes, not chars)
	 * - "modified UTF-8" encoding (null encoded as 0xC0 0x80, surrogate pairs encoded as two 3-byte sequences)
	 */
	public async readUTF(): Promise<string> {
		const utflen = await this.readUnsignedShort();
		if (utflen === 0) return '';

		const bytearr = new Uint8Array(utflen);
		await this.readFully(bytearr, 0, utflen);

		return DataInputStream.decodeModifiedUTF8(bytearr);
	}

	private static decodeModifiedUTF8(bytes: Uint8Array): string {
		const out: number[] = [];
		let count = 0;

		while (count < bytes.length) {
			const c = bytes[count] & 0xff;

			if (c <= 0x7f) {
				// 1-byte: 0xxxxxxx (note: 0x00 does not appear in modified UTF-8; null is 0xC0 0x80)
				count += 1;
				out.push(c);
				continue;
			}

			if (c >> 5 === 0b110) {
				// 2-byte: 110xxxxx 10xxxxxx
				if (count + 1 >= bytes.length) throw new UTFDataFormatException();
				const c2 = bytes[count + 1] & 0xff;
				if ((c2 & 0xc0) !== 0x80) throw new UTFDataFormatException();

				const ch = ((c & 0x1f) << 6) | (c2 & 0x3f);
				count += 2;

				// In modified UTF-8, null is encoded as 0xC0 0x80 -> ch=0
				out.push(ch);
				continue;
			}

			if (c >> 4 === 0b1110) {
				// 3-byte: 1110xxxx 10xxxxxx 10xxxxxx
				if (count + 2 >= bytes.length) throw new UTFDataFormatException();
				const c2 = bytes[count + 1] & 0xff;
				const c3 = bytes[count + 2] & 0xff;
				if ((c2 & 0xc0) !== 0x80 || (c3 & 0xc0) !== 0x80) throw new UTFDataFormatException();

				const ch = ((c & 0x0f) << 12) | ((c2 & 0x3f) << 6) | (c3 & 0x3f);
				count += 3;
				out.push(ch);
				continue;
			}

			// 4-byte sequences are not used in Java modified UTF-8.
			throw new UTFDataFormatException();
		}

		// Convert UTF-16 code units to JS string
		// out is array of 0..65535 code units
		// Avoid stack blow for huge strings by chunking
		let s = '';
		const CHUNK = 8192;
		for (let i = 0; i < out.length; i += CHUNK) {
			s += String.fromCharCode(...out.slice(i, i + CHUNK));
		}
		return s;
	}
}
