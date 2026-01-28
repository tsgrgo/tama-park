export class IrRemoteControlFrame {
	public static readonly COUNT_INFINITE = 0;

	private dataBytes: Uint8Array | null = null;
	private dataBitLength = -1;

	private frameDuration = -1; // 0.1 ms units
	private repeatCount = -1;
	private startHighUs = -1;
	private startLowUs = -1;
	private stopHighUs = -1;

	/**
	 * Data is transmitted from the first byte, MSB -> LSB order.
	 */
	public setFrameData(data: Uint8Array, bitLength: number): void {
		if (data == null) throw new Error('data');

		const maxBits = data.length * 8;
		if (bitLength < 0 || bitLength > maxBits) {
			throw new Error(`bitLength out of range: ${bitLength}`);
		}

		// Store only the needed bytes (ceil(bitLength/8))
		const neededBytes = Math.floor((bitLength + 7) / 8);

		this.dataBytes =
			neededBytes === 0 ? new Uint8Array(0) : data.slice(0, neededBytes);
		this.dataBitLength = bitLength;
	}

	/**
	 * Sets the frame data portion using up to 128 bits.
	 * Data is transmitted data1 MSB->LSB then data2 MSB->LSB.
	 */
	public setFrameDataParts(
		data1: bigint,
		bitLength1: number,
		data2: bigint,
		bitLength2: number
	): void {
		if (bitLength1 < 0 || bitLength1 > 64) {
			throw new Error(`bitLength1 out of range: ${bitLength1}`);
		}
		if (bitLength2 < 0 || bitLength2 > 64) {
			throw new Error(`bitLength2 out of range: ${bitLength2}`);
		}

		const totalBits = bitLength1 + bitLength2;
		if (totalBits === 0) {
			this.dataBytes = new Uint8Array(0);
			this.dataBitLength = 0;
			return;
		}

		const packed = new Uint8Array(Math.floor((totalBits + 7) / 8));
		let bitPos = IrRemoteControlFrame.writeBitsToPacked(
			packed,
			0,
			data1,
			bitLength1
		);
		IrRemoteControlFrame.writeBitsToPacked(
			packed,
			bitPos,
			data2,
			bitLength2
		);

		this.dataBytes = packed;
		this.dataBitLength = totalBits;
	}

	/**
	 * Sets the frame repeat interval in 0.1 ms units.
	 */
	public setFrameDuration(duration: number): void {
		if (duration <= 0) throw new Error('duration must be > 0');
		this.frameDuration = duration | 0;
	}

	/**
	 * Sets how many times this frame is transmitted repeatedly.
	 * 0 = infinite
	 */
	public setRepeatCount(count: number): void {
		if (count < 0) throw new Error('count must be >= 0');
		this.repeatCount = count | 0;
	}

	/**
	 * Sets start section High time in microseconds.
	 * If both startHigh and startLow are 0, start section is not sent.
	 */
	public setStartHighDuration(duration: number): void {
		if (duration < 0) throw new Error('duration must be >= 0');
		this.startHighUs = duration | 0;
	}

	/**
	 * Sets start section Low time in microseconds.
	 * If both startHigh and startLow are 0, start section is not sent.
	 */
	public setStartLowDuration(duration: number): void {
		if (duration < 0) throw new Error('duration must be >= 0');
		this.startLowUs = duration | 0;
	}

	/**
	 * Sets stop section High time in microseconds.
	 * If 0, stop section is not sent.
	 */
	public setStopHighDuration(duration: number): void {
		if (duration < 0) throw new Error('duration must be >= 0');
		this.stopHighUs = duration | 0;
	}

	/** Validation (mirrors DoJa-4.0+ rule: everything must be explicitly set) */
	public validate(): void {
		if (this.dataBytes == null || this.dataBitLength < 0)
			throw new Error('Frame data not set');
		if (this.frameDuration <= 0) throw new Error('Frame duration not set');
		if (this.repeatCount < 0) throw new Error('Repeat count not set');
		if (this.startHighUs < 0) throw new Error('StartHigh not set');
		if (this.startLowUs < 0) throw new Error('StartLow not set');
		if (this.stopHighUs < 0) throw new Error('StopHigh not set');
	}

	public getDataBytes(): Uint8Array {
		if (!this.dataBytes) return new Uint8Array(0);
		return this.dataBytes;
	}

	public getDataBitLength(): number {
		return this.dataBitLength;
	}

	public getFrameDuration(): number {
		return this.frameDuration;
	}

	public getRepeatCount(): number {
		return this.repeatCount;
	}

	public getStartHighUs(): number {
		return this.startHighUs;
	}

	public getStartLowUs(): number {
		return this.startLowUs;
	}

	public getStopHighUs(): number {
		return this.stopHighUs;
	}

	private static writeBitsToPacked(
		packed: Uint8Array,
		startingPos: number,
		value: bigint,
		bitLength: number
	): number {
		if (bitLength <= 0) return startingPos;

		// Java logic: startShift = 64 - bitLength; bit = (value >>> (63 - (startShift + i))) & 1
		// In bigint:
		const startShift = 64 - bitLength;

		for (let i = 0; i < bitLength; i++) {
			const shift = BigInt(63 - (startShift + i));
			const bit = Number((value >> shift) & 1n);
			IrRemoteControlFrame.setPackedBit(packed, startingPos++, bit);
		}

		return startingPos;
	}

	private static setPackedBit(
		packed: Uint8Array,
		bitPos: number,
		bit: number
	): void {
		const byteIndex = Math.floor(bitPos / 8);
		const bitInByte = bitPos % 8;
		const mask = 0x80 >>> bitInByte; // MSB first

		if (bit !== 0) packed[byteIndex] = packed[byteIndex] | mask;
		else packed[byteIndex] = packed[byteIndex] & (~mask & 0xff);
	}
}
