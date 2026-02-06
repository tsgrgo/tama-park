import type { Timer } from './Timer';

export interface TimerListener {
	timerExpired(timer: Timer): void | Promise<void>;
}
