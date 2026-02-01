import { InputStream } from '../../../java/io/InputStream';
import { ScratchpadFile } from './ScratchpadFile';

export class ScratchpadInputStream extends InputStream {
	private cursor: number;
	private readonly sp: ScratchpadFile;

	constructor(sp: ScratchpadFile, pos: number) {
		super();
		this.sp = sp;
		this.cursor = Math.max(0, Math.floor(pos));
	}

	public override async readBytes(b: Uint8Array, off: number, len: number): Promise<number> {
		this.ensureOpen();
		if (len <= 0) return 0;

		const fileLen = await this.sp.getLength();
		if (this.cursor >= fileLen) return -1;

		const canRead = Math.min(len, fileLen - this.cursor);
		const chunk = await this.sp.readAt(this.cursor, canRead);

		b.set(chunk, off);
		this.cursor += canRead;
		return canRead;
	}

	public override async skip(n: number): Promise<number> {
		this.ensureOpen();
		if (n <= 0) return 0;

		const fileLen = await this.sp.getLength();
		const cur = this.cursor;
		const end = Math.min(cur + n, fileLen);
		this.cursor = end;
		return end - cur;
	}

	public override async available(): Promise<number> {
		this.ensureOpen();
		const fileLen = await this.sp.getLength();
		const remaining = fileLen - this.cursor;
		if (remaining <= 0) return 0;
		return remaining > 0x7fffffff ? 0x7fffffff : remaining | 0;
	}
}
