import type { MediaListener } from './MediaListener';

export interface MediaPresenter {
	play(): void;

	stop(): void;

	setAttribute(attrib: number, value: number): void;

	setMediaListener(listener: MediaListener): void;
}
