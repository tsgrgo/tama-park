import { OutputStream } from '../../../java/io/OutputStream';
import type { ScratchpadFile } from './ScratchpadFile';

export class ScratchpadOutputStream extends OutputStream {
	private cursor: number;
	private readonly sp: ScratchpadFile;

	constructor(sp: ScratchpadFile, pos: number) {
		super();
		this.sp = sp;
		this.cursor = Math.max(0, Math.floor(pos));
	}

	public override async writeBytes(b: Uint8Array, off: number, len: number): Promise<void> {
		this.ensureOpen();
		if (len <= 0) return;

		const chunk = b.subarray(off, off + len);
		await this.sp.writeAt(this.cursor, chunk);
		this.cursor += len;
	}
}
