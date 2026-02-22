import type { MediaListener } from './MediaListener';

export interface MediaPresenter {
	play(): unknown;

	stop(): void;

	setAttribute(attrib: number, value: number): void;

	setMediaListener(listener: MediaListener): void;
}
