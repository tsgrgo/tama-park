import type { TimerListener } from './TimerListener';

export class Timer {
	private listener: TimerListener | null = null;
	private delayMillis = 0;
	private repeat = false;

	private handle:
		| ReturnType<typeof setTimeout>
		| ReturnType<typeof setInterval>
		| null = null;

	private running = false;

	public setTime(millis: number): void {
		this.delayMillis = millis;
	}

	public setRepeat(repeat: boolean): void {
		this.repeat = repeat;
	}

	public setListener(listener: TimerListener): void {
		this.listener = listener;
	}

	public start(): void {
		if (this.running) {
			throw new Error('Timer already running');
		}
		if (!this.listener) {
			throw new Error('TimerListener not set');
		}

		this.running = true;

		if (this.repeat) {
			this.handle = setInterval(() => {
				this.listener?.timerExpired(this);
			}, this.delayMillis);
		} else {
			this.handle = setTimeout(() => {
				this.listener?.timerExpired(this);
				this.stop();
			}, this.delayMillis);
		}
	}

	public stop(): void {
		this.running = false;

		if (this.handle != null) {
			if (this.repeat) {
				clearInterval(this.handle as ReturnType<typeof setInterval>);
			} else {
				clearTimeout(this.handle as ReturnType<typeof setTimeout>);
			}
			this.handle = null;
		}
	}
}
