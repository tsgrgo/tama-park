import { createMldPlayer } from '../../../mld/createMldPlayer';
import type { MediaListener } from './MediaListener';
import type { MediaPresenter } from './MediaPresenter';
import type { MediaSound } from './MediaSound';

export class AudioPresenter implements MediaPresenter {
	private static INSTANCES = new Map<number, AudioPresenter>();

	private mldPlayer?: Awaited<ReturnType<typeof createMldPlayer>>;
	private sound?: MediaSound;
	private listener?: MediaListener;

	public static getAudioPresenter(port: number): AudioPresenter {
		if (this.INSTANCES.has(port)) return this.INSTANCES.get(port)!;
		const presenter = new AudioPresenter(port);
		this.INSTANCES.set(port, presenter);
		return presenter;
	}

	private constructor(_port: number) {}

	public setSound(mediaSound: MediaSound) {
		this.sound = mediaSound;
	}

	public play(): void {
		if (!this.mldPlayer) {
			createMldPlayer()
				.then(res => {
					this.mldPlayer = res;
					this.loadMldToPlayer();
				})
				.catch(e => console.log(e));

			return;
		}
		this.loadMldToPlayer();
	}

	public stop(): void {
		// void this.mldPlayer?.stop();
	}

	public setAttribute(_attrib: number, _value: number): void {}

	public setMediaListener(listener: MediaListener): void {
		this.listener = listener;
	}

	private fireEvent(type: number, param: number): void {
		try {
			this.listener?.mediaAction(this, type, param);
		} catch (error) {
			console.error(error);
		}
	}

	private loadMldToPlayer() {
		if (!this.mldPlayer || !this.sound) return;
		void this.mldPlayer.load(this.sound.unwrap().buffer as ArrayBuffer);
	}
}
