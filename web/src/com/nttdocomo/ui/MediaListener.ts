import type { MediaPresenter } from './MediaPresenter';

export interface MediaListener {
	mediaAction(source: MediaPresenter, type: number, param: number): void;
}
