import { IrRemoteControlFrame } from './IrRemoteControlFrame';

export class IrRemoteControl {
	/**
	 * Indicates the code output pattern is High-first (= 0).
	 */
	public static readonly PATTERN_HL = 0;
	/**
	 * Indicates the code output pattern is Low-first (= 1).
	 */
	public static readonly PATTERN_LH = 1;

	private static readonly INSTANCE = new IrRemoteControl();

	public static getIrRemoteControl(): IrRemoteControl {
		return IrRemoteControl.INSTANCE;
	}

	private carrierHighUs = -1;
	private carrierLowUs = -1;

	private code0: Pulse | null = null;
	private code1: Pulse | null = null;

	// "thread" replacement
	private sending = false;
	private sendToken = 0; // increment to cancel any in-flight loop

	protected constructor() {}

	/**
	 * @param highDuration carrier High duration (0.1 microsecond units).
	 * @param lowDuration  carrier Low duration (0.1 microsecond units).
	 */
	public setCarrier(highDuration: number, lowDuration: number): void {
		if (highDuration <= 0 || lowDuration <= 0) {
			throw new Error('Carrier durations must be > 0');
		}
		this.carrierHighUs = highDuration | 0;
		this.carrierLowUs = lowDuration | 0;

		IrRemoteControl.log(
			'Carrier set: HIGH=%dus LOW=%dus',
			highDuration,
			lowDuration
		);
	}

	/**
	 * Sets the logical "0" pulse information.
	 */
	public setCode0(
		pattern: number,
		highDuration: number,
		lowDuration: number
	): void {
		this.code0 = new Pulse(pattern, highDuration, lowDuration);
		IrRemoteControl.log('Code0 set: %s', this.code0.toString());
	}

	/**
	 * Sets the logical "1" pulse information.
	 */
	public setCode1(
		pattern: number,
		highDuration: number,
		lowDuration: number
	): void {
		this.code1 = new Pulse(pattern, highDuration, lowDuration);
		IrRemoteControl.log('Code1 set: %s', this.code1.toString());
	}

	/**
	 * @param numFrames number of frames to send (must be >= 1).
	 * @param frames    array of frames to send.
	 */
	public send(numFrames: number, frames: IrRemoteControlFrame[]): void;
	/**
	 * @param numFrames number of frames to send (must be >= 1).
	 * @param frames    array of frames to send.
	 * @param timeout   timeout value (seconds).
	 */
	public send(
		numFrames: number,
		frames: IrRemoteControlFrame[],
		timeout: number
	): void;
	/**
	 * @param numFrames number of frames to send (must be >= 1).
	 * @param frames    array of frames to send.
	 * @param timeout   timeout value (seconds).
	 * @param count     number of times to transmit the signal.
	 */
	public send(
		numFrames: number,
		frames: IrRemoteControlFrame[],
		timeout?: number,
		count?: number
	): void {
		const t = timeout ?? 10;
		const c = count ?? Number.MAX_SAFE_INTEGER;
		this.sendInternal(numFrames, frames, t, c);
	}

	private sendInternal(
		numFrames: number,
		frames: IrRemoteControlFrame[],
		timeoutSec: number,
		count: number
	): void {
		this.validateSendArgs(numFrames, frames, timeoutSec, count);

		this.stop();
		this.sending = true;

		const myToken = ++this.sendToken;

		// Fire-and-forget async loop (closest to daemon thread)
		void this.runSendLoop(myToken, numFrames, frames, timeoutSec, count);
	}

	private async runSendLoop(
		token: number,
		numFrames: number,
		frames: IrRemoteControlFrame[],
		timeoutSec: number,
		count: number
	): Promise<void> {
		IrRemoteControl.log('=== IR SEND START ===');

		const endTimeMs = Date.now() + timeoutSec * 1000;

		try {
			for (let cycle = 1; cycle <= count; cycle++) {
				if (this.shouldStop(token, endTimeMs)) break;

				IrRemoteControl.log('Send cycle %d', cycle);

				const completed = await this.sendOneCycle(
					token,
					numFrames,
					frames,
					endTimeMs
				);
				if (!completed) break;
			}
		} finally {
			// Only the latest send loop should turn off sending
			if (this.sendToken === token) this.sending = false;
			IrRemoteControl.log('=== IR SEND END ===');
		}
	}

	private async sendOneCycle(
		token: number,
		numFrames: number,
		frames: IrRemoteControlFrame[],
		endTimeMs: number
	): Promise<boolean> {
		for (let i = 0; i < numFrames; i++) {
			if (this.shouldStop(token, endTimeMs)) return false;

			const f = frames[i];
			this.logFrame(i, f);

			const shouldContinue = await this.sendFrameWithRepeats(
				token,
				i,
				f,
				endTimeMs
			);
			if (!shouldContinue) return false;
		}
		return true;
	}

	private async sendFrameWithRepeats(
		token: number,
		index: number,
		f: IrRemoteControlFrame,
		endTimeMs: number
	): Promise<boolean> {
		const repeatCount = f.getRepeatCount();

		if (repeatCount === IrRemoteControlFrame.COUNT_INFINITE) {
			IrRemoteControl.log('Frame %d repeats infinitely', index);

			while (!this.shouldStop(token, endTimeMs)) {
				await this.sleepFrame(token, f);
			}
			return false;
		} else {
			for (let i = 0; i < repeatCount; i++) {
				if (this.shouldStop(token, endTimeMs)) return false;
				await this.sleepFrame(token, f);
			}
			return true;
		}
	}

	private shouldStop(token: number, endTimeMs: number): boolean {
		return (
			!this.sending || this.sendToken !== token || Date.now() >= endTimeMs
		);
	}

	private validateSendArgs(
		numFrames: number,
		frames: IrRemoteControlFrame[],
		timeout: number,
		count: number
	): void {
		if (frames == null) throw new Error('frames');
		if (numFrames <= 0) throw new Error('numFrames <= 0');
		if (frames.length < numFrames) throw new Error('frames too short');
		if (timeout <= 0) throw new Error('timeout <= 0');
		if (count <= 0) throw new Error('count <= 0');

		if (this.carrierHighUs <= 0 || this.carrierLowUs <= 0) {
			throw new Error('Carrier not configured');
		}
		if (this.code0 == null || this.code1 == null) {
			throw new Error('Code pulses not configured');
		}

		for (let i = 0; i < numFrames; i++) {
			const f = frames[i];
			if (f == null) throw new Error(`frame[${i}] is null`);
			// validate() is protected in my TS frame conversion; call via (f as any) if needed.
			f.validate?.();
		}
	}

	public stop(): void {
		if (!this.sending) return;

		this.sending = false;
		this.sendToken++; // cancels any in-flight loop immediately

		IrRemoteControl.log('IR transmission stopped');
	}

	private async sleepFrame(
		token: number,
		f: IrRemoteControlFrame
	): Promise<void> {
		// 0.1 ms units --> ms (integer)
		const dur01ms = f.getFrameDuration();
		const ms = Math.floor(dur01ms / 10);

		if (ms <= 0) return;
		await IrRemoteControl.sleep(ms);

		// Check cancel quickly after sleeping
		if (this.sendToken !== token) return;
	}

	private logFrame(index: number, frame: IrRemoteControlFrame): void {
		const bytes = frame.getDataBytes();
		IrRemoteControl.log(
			'Frame[%d]: bits=%d data=%s start(H=%dus L=%dus) stop(H=%dus) repeat=%d dur=%d(0.1ms)',
			index,
			frame.getDataBitLength(),
			IrRemoteControl.bytesToString(bytes),
			frame.getStartHighUs(),
			frame.getStartLowUs(),
			frame.getStopHighUs(),
			frame.getRepeatCount(),
			frame.getFrameDuration()
		);
	}

	private static bytesToString(bytes: Uint8Array): string {
		// Similar vibe to Arrays.toString(byte[])
		return `[${Array.from(bytes).join(', ')}]`;
	}

	private static log(fmt: string, ...args: any[]): void {
		// Basic %d / %s support to resemble String.format
		let i = 0;
		const msg = fmt.replace(/%[ds]/g, m => {
			const v = args[i++];
			return m === '%d' ? String(Number(v)) : String(v);
		});
		console.log('[IR] ' + msg, ...args.slice(i));
	}

	private static sleep(ms: number): Promise<void> {
		return new Promise(resolve => setTimeout(resolve, ms));
	}
}

class Pulse {
	public readonly pattern: number;
	public readonly highDuration: number;
	public readonly lowDuration: number;

	constructor(pattern: number, highDuration: number, lowDuration: number) {
		if (
			pattern !== IrRemoteControl.PATTERN_HL &&
			pattern !== IrRemoteControl.PATTERN_LH
		) {
			throw new Error('Invalid pattern: ' + pattern);
		}
		if (highDuration <= 0 || lowDuration <= 0) {
			throw new Error('Pulse durations must be > 0');
		}
		this.pattern = pattern;
		this.highDuration = highDuration | 0;
		this.lowDuration = lowDuration | 0;
	}

	public toString(): string {
		return `${this.pattern === IrRemoteControl.PATTERN_HL ? 'HL' : 'LH'} HIGH=${this.highDuration}us LOW=${this.lowDuration}us`;
	}
}
