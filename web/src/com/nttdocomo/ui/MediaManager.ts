import { MediaImage } from './MediaImage';
import { MediaSound } from './MediaSound';

export class MediaManager {
	public static getImage(data: Uint8Array | ArrayBuffer | string): MediaImage {
		if (typeof data === 'string') {
			// TODO
			return new MediaImage(new ArrayBuffer(0));
		}

		return new MediaImage(data);
	}

	public static getSound(data: Uint8Array | ArrayBuffer) {
		return new MediaSound(data);
	}
}
