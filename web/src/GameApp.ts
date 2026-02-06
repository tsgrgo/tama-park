import { AudioPresenter } from './com/nttdocomo/ui/AudioPresenter';
import type { Canvas } from './com/nttdocomo/ui/Canvas';
import { Display } from './com/nttdocomo/ui/Display';
import { Font } from './com/nttdocomo/ui/Font';
import { Graphics } from './com/nttdocomo/ui/Graphics';
import { IApplication } from './com/nttdocomo/ui/IApplication';
import type { MediaListener } from './com/nttdocomo/ui/MediaListener';
import type { MediaPresenter } from './com/nttdocomo/ui/MediaPresenter';
import type { MediaSound } from './com/nttdocomo/ui/MediaSound';
import type { Image } from './com/nttdocomo/ui/Image';
import { Timer } from './com/nttdocomo/util/Timer';
import type { TimerListener } from './com/nttdocomo/util/TimerListener';
import { GameScreen } from './GameScreen';
import { IrRemoteControlFrame } from './com/nttdocomo/device/IrRemoteControlFrame';
import { IrRemoteControl } from './com/nttdocomo/device/IrRemoteControl';
import { PhoneSystem } from './com/nttdocomo/ui/PhoneSystem';
import { MediaImage } from './com/nttdocomo/ui/MediaImage';
import { MediaManager } from './com/nttdocomo/ui/MediaManager';
import { DataInputStream } from './java/io/DataInputStream';
import { DataOutputStream } from './java/io/DataOutputStream';
import { Connector } from './javax/microedition/io/Connector';
import type { HttpConnection } from './com/nttdocomo/io/HttpConnection';
import { Thread } from './java/lang/Thread';
import { stringConstructor } from './java/lang/String';

const PAGE_AUTH_ERROR = -3;
const PAGE_COM_ERROR = -2;
const PAGE_PREP_ERROR = -1;
const PAGE_NONE_0 = 0;
const PAGE_NONE_1 = 1;
const PAGE_DOWNLOADING = 2;
const PAGE_LOADING = 3;
const PAGE_TITLE = 4;
const PAGE_MAILBOX_MODE = 5;
const PAGE_TRAVEL_MODE = 6;
const PAGE_SHOPPING_CENTER = 7;
const PAGE_PARENT_CALL = 8;
const PAGE_GOTCHI_KING = 9;
const PAGE_TRAVEL_MEMORY = 10;
const PAGE_EXCHANGE_PLAZA = 11;

const SOFT_LABEL_START = 0;
const SOFT_LABEL_MENU = 1;
const SOFT_LABEL_CLOSE = 2;
const SOFT_LABEL_BACK = 3;
const SOFT_LABEL_TITLE = 4;
const SOFT_LABEL_HELP = 5;
const SOFT_LABEL_EMPTY = 6;

const ALIGN_LEFT = 0;
const ALIGN_RIGHT = 1;
const ALIGN_CENTER = 2;

const KEY_0 = 1n;
const KEY_1 = 2n;
const KEY_2 = 4n;
const KEY_3 = 8n;
const KEY_4 = 16n;
const KEY_5 = 32n;
const KEY_6 = 64n;
const KEY_7 = 128n;
const KEY_8 = 256n;
const KEY_9 = 512n;
const KEY_ASTERISK = 1024n; // '*'
const KEY_POUND = 2048n; // '#'
const KEY_LEFT = 65536n;
const KEY_UP = 131072n;
const KEY_RIGHT = 262144n;
const KEY_DOWN = 524288n;
const KEY_SELECT = 1048576n;
const KEY_SOFT1 = 2097152n;
const KEY_SOFT2 = 4194304n;
const KEY_UP_LEFT = 196608n;
const KEY_DOWN_RIGHT = 786432n;
const KEY_DOWN_RIGHT_SELECT = 1835008n;

export class GameApp extends IApplication implements TimerListener, MediaListener {
	public static drawState = 0; // 0: Idle, 2: Request pending, 3: Currently drawing
	public static running: boolean;
	public static resumedDraw: boolean;
	public static fps: number;
	public static drawOnNextPaint: boolean;
	public static fullDraw: boolean;
	public static fullDrawOnNextPaint: boolean;
	public static loadGameSaveDelay: number;
	public static timer: Timer;
	public static mediaListener: GameApp;
	public static canvas: Canvas;
	public static canvasWidth: number;
	public static canvasHeight: number;
	public static rootX: number;
	public static rootY: number;
	public static currentFont: Font;
	public static currentFontHeight: number;
	public static garbageCollectTimer: number;
	public static gameDataVersion: number;
	public static inputStateFlag: boolean;
	public static currentPage: number;
	public static travelMemoryDebug: boolean;
	public static totalMemory: bigint;
	public static audioPresenters: AudioPresenter[];
	public static mediaSounds: MediaSound[];
	public static loopedSoundId: number;
	public static previousMusicId: number;
	public static shouldRestartMusic: boolean;
	public static previousMusicParam: boolean;
	public static inputState: bigint[];
	public static keyHeldTime: number;
	public static timeSinceLastInput: number;
	public static systemAttributeState: number[];
	public static gameSave: number[]; // [?, resDownloaded, gameDataVersion, isSoundEnabled, ? ..]
	public static currentFontIdx: number;
	public static rngState: number;
	public static previousSoftLabelIdx: number;
	public static currentSoftLabelIdx: number;
	public static softLabels: string[];
	public static loadingProgress: number;
	public static images: Array<Image | null>;
	public static imageSizes: number[];
	public static titleScreenState: number[];
	public static titleScreenLayout: number[];
	public static mailboxModeState: number[];
	public static mailboxModeLayout: number[];
	public static travelModeState: number[];
	public static travelModeLayout: number[];
	public static shoppingCenterState: number[];
	public static shoppingCenterLayoutTable: number[];
	public static shoppingCenterItemColors: number[];
	public static itemTicketLayout: number[];
	public static parentCallState: number[];
	public static allowanceTicketLayout: number[];
	public static parentCallImages: Array<Image | null>;
	public static parentCallText: string | null;
	public static parentCallQuote: string | null;
	public static gotchiKingState: number[];
	public static gotchiKingInviteTicketLayout: number[];
	public static gotchiKingImages: Array<Image | null>;
	public static imagesToTemporarilyDispose: number[];
	public static travelMemoryState: number[]; // [?, flowStep, ...]
	public static memoryPhotoLayout: number[];
	public static travelMemoryTexts: Array<string | null>;
	public static travelMemoryPhoto: Image | null;
	public static exchangePlazaState: number[]; // [?, ?, colorIdx?, ...]
	public static exchangeTicketLayout: number[];
	public static exchangePlazaImages: Array<Image | null>;
	public static exchangePlazaTexts: Array<string | null>;
	public static regionSelectLayout: number[];
	public static explanationState: number[]; // [index, numberOfPages, current, isOpen]
	public static menuState: number[]; // [isMenuOpen, menuPage, explanationIndex, numberOfExplanationPages...]
	public static errorState: number[]; // [pageToGoBack, flowStepToGoBack, flowStepToGoBack, showErrorPage] ? = some action id?
	public static errorPageText: string | null;
	public static imageReadInfo: number;
	public static errorPageUnusedToggle: boolean;
	public static errorPageUnusedCounter: number;
	public static texts: string[];
	public static buttonState: number[]; // [0: selectedButtonIdx, 1: numberOfButtons, 2: canLoopAround, 3: selectedOutlineColor, 4: selectedColor, 5, 6: selectedTextColor, 7: selectedShadowColor, 8: outlineColor, 9: color, 10, 11: textColor, 12: shadowColor, 13: isPressed]
	public static codeInputState: number[];
	public static digitShuffleTable: number[];
	public static rainbowColors: number[];
	public static bytesSentToServer: Uint8Array;
	public static bytesToSendViaIr: Uint8Array;
	public static irFrames: IrRemoteControlFrame[];
	public static irRemoteControl: IrRemoteControl;
	public static irState: number[]; // [transmissionState, ?, currentPage, ...]
	public static irSendTimestamp: number;
	public static generalIrSendLayout: number[];
	public static shoppingCenterIrSendLayout: number[];
	public executingTimerExpired = false;

	static {
		this.running = true;
		this.resumedDraw = false;
		this.drawOnNextPaint = false;
		this.fullDraw = false;
		this.loadGameSaveDelay = 8;
		this.garbageCollectTimer = 0;
		this.gameDataVersion = 0;
		this.inputStateFlag = false;
		this.currentPage = 0;
		this.totalMemory = 0n;
		this.audioPresenters = new Array<AudioPresenter>(2);
		this.loopedSoundId = -1;
		this.previousMusicId = -1;
		this.inputState = new Array<bigint>(7).fill(0n);
		this.keyHeldTime = 0;
		this.timeSinceLastInput = 0;
		this.systemAttributeState = new Array<number>(2).fill(0);
		this.gameSave = new Array<number>(7).fill(0);
		this.rngState = 0;
		this.previousSoftLabelIdx = 6;
		this.currentSoftLabelIdx = 6;
		this.softLabels = ['Start', 'Menu', 'Close', 'Back', 'Title', 'Help', ''];
		this.images = new Array<Image>(93);
		this.titleScreenState = new Array<number>(5).fill(0);
		this.titleScreenLayout = [120, 176, 186, 26, 26, 25, 2, 120, 208, 186, 26, 16, 15, 2];
		this.mailboxModeState = new Array<number>(3).fill(0);
		this.mailboxModeLayout = [120, 132, 230, 26, 20, 19, 2, 120, 168, 230, 26, 18, 17, 2, 120, 204, 230, 26, 11, 10, 2];
		this.travelModeState = new Array<number>(3).fill(0);
		this.travelModeLayout = [120, 146, 220, 26, 22, 21, 2, 120, 186, 220, 26, 14, 13, 2];
		this.shoppingCenterState = new Array<number>(4).fill(0);
		this.shoppingCenterLayoutTable = [120, 202, 220, 24, 14, 2, 120, 168, 220, 24, 13, 2, 120, 134, 220, 24, 12, 2, 120, 100, 220, 24, 11, 2, 120, 66, 220, 24, 10, 2];
		this.shoppingCenterItemColors = [
			11025351, 1648446, 11025351, 1648446, 7053048, 1648446, 7053048, 1648446, 10873427, 2323575, 10873427, 2323575, 16777041, 16734720, 16777041, 16734720, 16021161, 16777215, 16021161,
			16777215
		];
		this.itemTicketLayout = [120, 144, 190, 28, 93, 2, 120, 176, 190, 28, 16, 2, 120, 208, 190, 28, 15, 2];
		this.parentCallState = new Array<number>(7).fill(0);
		this.allowanceTicketLayout = [120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2];
		this.parentCallImages = new Array<Image>(2);
		this.gotchiKingState = new Array<number>(6).fill(0);
		this.gotchiKingInviteTicketLayout = [120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2];
		this.gotchiKingImages = new Array<Image>(2);
		this.imagesToTemporarilyDispose = [58, 59, 60, 71, 89, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 72, 89];
		this.travelMemoryState = new Array<number>(4).fill(0);
		this.memoryPhotoLayout = [120, 165, 170, 28, 15, 2, 120, 198, 170, 28, 35, 2];
		this.travelMemoryTexts = new Array<string>(2);
		this.exchangePlazaState = new Array<number>(5).fill(0);
		this.exchangeTicketLayout = [120, 165, 170, 28, 93, 2, 120, 198, 170, 28, 15, 2];
		this.exchangePlazaImages = new Array<Image>(2);
		this.exchangePlazaTexts = new Array<string>(3);
		this.regionSelectLayout = [
			0, 0, 1, 6, 6, 1, 80, 41, 0, 28, 2, 0, 0, 2, 81, 43, 0, 64, 3, 1, 1, 3, 82, 45, -44, 44, 4, 2, 2, 4, 83, 47, -64, 60, 5, 3, 3, 5, 84, 49, -108, 60, 6, 4, 4, 6, 85, 51, -164, 60, 0, 5, 5,
			0, 86, 53
		];
		this.explanationState = new Array<number>(4).fill(0);
		this.menuState = new Array<number>(9).fill(0);
		this.errorState = new Array<number>(4).fill(0);
		this.texts = new Array<string>(183);
		this.buttonState = new Array<number>(14).fill(0);
		this.codeInputState = new Array<number>(6).fill(0);
		this.digitShuffleTable = [
			3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 3, 8, 4, 0, 1, 5, 6, 7, 2, 9, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 4, 8, 1, 6, 2, 3, 9, 5, 0, 7, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 0, 6, 2, 7, 3, 9, 1, 4, 5, 8, 7,
			1, 6, 3, 0, 2, 9, 4, 5, 8, 7, 1, 6, 3, 0, 2, 9, 4, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1, 6, 8, 5, 3, 0, 9, 7, 2, 4, 1
		];
		this.rainbowColors = [15947864, 16777041, 10873427, 7053048, 16777215, 11025351, 16021161];
		this.bytesSentToServer = new Uint8Array(14);
		this.bytesToSendViaIr = new Uint8Array(16);
		this.irFrames = this.createIrRemoteControlFrames(1);
		this.irRemoteControl = IrRemoteControl.getIrRemoteControl();
		this.irState = new Array<number>(6).fill(0);
		this.generalIrSendLayout = [120, 144, 170, 28, 15, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2];
		this.shoppingCenterIrSendLayout = [120, 144, 170, 28, 16, 2, 120, 176, 170, 28, 94, 2, 120, 208, 170, 28, 95, 2];
	}

	constructor() {
		super();
		GameApp.mediaListener = this;
		const args = this.getArgs();
		this.initCanvas();

		try {
			PhoneSystem.setAttribute(0, 1);
		} catch (e) {}

		GameApp.setCurrentFont(2);
		GameApp.fps = 8;
		GameApp.timer = new Timer();
		GameApp.timer.setRepeat(true);
		GameApp.timer.setTime(1000 / GameApp.fps);
		GameApp.timer.setListener(this);
		GameApp.timer.start();
	}

	public static repaint(): void {
		this.drawState = 2;
		this.canvas.repaint();
	}

	public static async startTimerWithRetry(): Promise<void> {
		for (let i = 0; i < 10; ++i) {
			try {
				this.timer.start();
				return;
			} catch (e) {
				try {
					this.timer.stop();
				} catch (ignored) {}

				try {
					await Thread.sleep(1000);
				} catch (ignored) {}
			}
		}
	}

	public static log(str: string): void {
		console.log(str);
	}

	public static initAudioPresenters(): void {
		this.audioPresenters[0] = AudioPresenter.getAudioPresenter(0);
		this.audioPresenters[0].setAttribute(133, 0);
		this.audioPresenters[0].setMediaListener(this.mediaListener);
		this.audioPresenters[1] = AudioPresenter.getAudioPresenter(1);
		this.audioPresenters[1].setAttribute(133, 1);
	}

	public static playSoundInternal(soundIdx: number, presenterIdx: number, flag: boolean): void {
		this.stopSound(presenterIdx);

		try {
			if (this.gameSave[3] == 0) {
				this.audioPresenters[presenterIdx].setSound(this.mediaSounds[soundIdx]);
				if (flag) {
				}

				this.audioPresenters[presenterIdx].play();
			}
		} catch (e) {
			this.log('playsound:' + soundIdx + ' ' + e);
		}
	}

	public static playSound(soundIdx: number, flag: boolean): void {
		this.playSoundInternal(soundIdx, flag ? 0 : 1, flag);
	}

	public static stopSound(presenterIdx: number): void {
		try {
			// Thread.sleep(100L);
			this.audioPresenters[presenterIdx].stop();
		} catch (ignored) {}
	}

	public static stopAllSounds(): void {
		for (let i = 0; i < this.audioPresenters.length; ++i) {
			this.stopSound(i);
		}
	}

	public static playMusic(musicId: number, musicParam: boolean): void {
		if (this.gameSave[3] == 0 && musicId >= 0) {
			this.stopSound(0);
			this.playSoundInternal(musicId, 0, musicParam);
		}

		this.loopedSoundId = musicId;
		this.previousMusicId = musicId;
		this.previousMusicParam = musicParam;
	}

	public static checkMusic(): void {
		if (this.shouldRestartMusic) {
			this.restartMusic();
			this.shouldRestartMusic = false;
		}
	}

	public static restartMusicOnNext(): void {
		this.shouldRestartMusic = true;
	}

	public static toggleSound(): void {
		this.gameSave[3] = 1 - this.gameSave[3];
		this.stopAllSounds();
		this.restartMusic();
	}

	public static restartMusic(): void {
		if (this.previousMusicParam) {
			this.playMusic(this.previousMusicId, this.previousMusicParam);
		}
	}

	public static loadSounds(): boolean {
		// DataInputStream stream = null;
		// int currentIndex = 0;

		// boolean success;
		// try {
		//     mediaSounds = new MediaSound[7];
		//     int[] sizes = loadShortArray(128);
		//     int pos = 0;

		//     for (int i = 0; i < 93; ++i) {
		//         pos += sizes[i];
		//     }

		//     stream = Connector.openDataInputStream("scratchpad:///0;pos=" + (pos + 128 + 568));

		//     for (int i = 0; i < 7; ++i) {
		//         currentIndex = i;
		//         byte[] data = new byte[sizes[i + 93]];
		//         stream.read(data);

		//         for (int j = 0; j < data.length; ++j) {
		//         }

		//         mediaSounds[i] = MediaManager.getSound("data");
		//         mediaSounds[i].use();
		//         log("loadsound:" + i);
		//         data = null;
		//         System.gc();
		//     }

		//     initAudioPresenters();
		//     success = true;
		// } catch (Exception e) {
		//     log("loadsounderr i:" + currentIndex);
		//     success = false;
		// } finally {
		//     if (stream != null) {
		//         try {
		//             stream.close();
		//         } catch (Exception ignored) {
		//         }
		//     }

		// }

		// return success;

		this.initAudioPresenters();
		return true; // TODO
	}

	public static isKeyPressed(key: bigint): boolean {
		return (this.inputState[1] & key) != 0n;
	}

	public static isKeyDown(key: bigint): boolean {
		return (this.inputState[2] & key) != 0n;
	}

	public static processEvent(type: number, param: number): void {
		try {
			if (type == 0) {
				this.inputState[0] = BigInt(this.canvas.getKeypadState()) & BigInt(0x7fffffff);
				this.inputState[4]++;
			}
		} catch (ignored) {}
	}

	public static updateInputState(): void {
		const var0 = 0n;
		const var2 = 0n;

		if (this.inputState[4] == 0n) {
			this.inputState[0] = 0n;
		}

		this.inputState[3] = this.inputState[6];
		this.inputState[6] = this.inputState[0] | var0 | (var2 << 32n);
		this.inputState[5] = this.inputState[6] & (this.inputState[6] ^ this.inputState[3]);

		if (this.inputStateFlag) {
			if (this.inputState[4] != 0n) {
				this.inputState[5] |= (var2 << 32n) & 844424930131968n;
			}
		} else if (this.inputState[4] != 0n) {
			this.inputState[5] |= var0 & 655360n;
		}

		if ((this.inputState[3] ^ this.inputState[6]) == 0n && this.inputState[6] != 0n) {
			++this.keyHeldTime;
		} else {
			this.keyHeldTime = 0;
		}

		this.inputState[4] = 0n;
		if ((this.inputState[6] & 9851624207876096n) == 0n) {
			++this.timeSinceLastInput;
		} else {
			this.timeSinceLastInput = 0;
		}

		this.inputState[2] = this.inputState[6];
		this.inputState[1] = this.inputState[5];
	}

	public static systemAttributeHelper(): void {
		try {
			this.systemAttributeState[0] = 0;
			this.systemAttributeState[1] = 0;
			PhoneSystem.setAttribute(1, 0);
		} catch (ignored) {}
	}

	public static setSomeSystemAttribute(): void {
		if (this.systemAttributeState[0] > 0) {
			if (this.gameSave[5] == 0 && this.systemAttributeState[1] == 0) {
				try {
					this.systemAttributeState[1] = 1;
					PhoneSystem.setAttribute(1, 1);
				} catch (ignored) {}
			}

			if (--this.systemAttributeState[0] <= 0) {
				this.systemAttributeHelper();
			}
		}
	}

	public static async loadGameSave(): Promise<void> {
		console.log('loadGameSave');

		try {
			const inputStream = await Connector.openDataInputStream('scratchpad:///0;pos=0');

			for (let i = 0; i < this.gameSave.length; ++i) {
				this.gameSave[i] = await inputStream.readInt();
			}

			console.log('loaded game save: ', this.gameSave);

			await inputStream.close();
			// inputStream = null;
			// System.gc();
		} catch (ignored) {}
	}

	public static async saveGame(): Promise<void> {
		try {
			const outputStream = await Connector.openDataOutputStream('scratchpad:///0;pos=0');

			for (let i = 0; i < this.gameSave.length; ++i) {
				await outputStream.writeInt(this.gameSave[i]);
			}

			await outputStream.close();
			// outputStream = null;
			// System.gc();
		} catch (ignored) {
			console.log(ignored);
		}
	}

	public static async downloadGameData(path: string, size: number, pos: number): Promise<void> {
		const buffer = new Uint8Array(10240);
		this.repaint();
		pos += this.gameSave[1] * 10240;

		for (let i = this.gameSave[1]; i < Math.ceil(size / 10240); ++i) {
			const httpConnection = Connector.open(this.mediaListener.getSourceURL() + path + i + '.bin', 1, true);
			httpConnection.setRequestMethod('GET');
			await httpConnection.connect();
			// System.gc();
			const inputStream = new DataInputStream(await httpConnection.openInputStream());
			const length = await httpConnection.getLength();

			const bytesRead = await inputStream.read(buffer, 0, length);

			await inputStream.close();
			await httpConnection.close();

			if (bytesRead != length) {
				throw new Error('http load error!');
			}

			const outputStream = await Connector.openDataOutputStream('scratchpad:///0;pos=' + pos);
			await outputStream.write(buffer, 0, length);
			await outputStream.close();

			pos += length;
			this.gameSave[1]++;
			await this.saveGame();
			++this.loadingProgress;
			this.repaint();
		}

		// buffer = null;
		// System.gc();
	}

	public static async loadShortArray(pos: number): Promise<number[]> {
		return await this.loadArray(pos, 2);
	}

	public static async loadArray(pos: number, elementSizeInBytes: number): Promise<number[]> {
		const stream = new DataInputStream(await Connector.openInputStream('scratchpad:///0;pos=' + pos));
		const arraySize = await stream.readShort();
		const res = new Array<number>(arraySize);

		if (elementSizeInBytes == 2) {
			for (let i = 0; i < arraySize; ++i) {
				res[i] = await stream.readShort();
			}
		} else if (elementSizeInBytes == 4) {
			for (let i = 0; i < arraySize; ++i) {
				res[i] = await stream.readInt();
			}
		}

		await stream.close();
		return res;
	}

	public static drawSprite(g: Graphics, idx: number, x: number, y: number, anchor: number): void {
		if (this.images[idx]) this.drawImage(g, this.images[idx], x, y, anchor);
	}

	public static drawImage(g: Graphics, img: Image | null, x: number, y: number, anchor: number): void {
		if (!img) return;

		if (anchor == 2) {
			x -= img.getWidth() / 2;
		} else if (anchor == 1) {
			x -= img.getWidth();
		} else if (anchor == 3) {
			x -= img.getWidth() / 2;
			y -= img.getHeight() / 2;
		}

		g.drawImage(img, x, y);
	}

	public static getSpriteWidth(idx: number): number {
		return this.images[idx]?.getWidth() || 0;
	}

	public static getSpriteHeight(idx: number): number {
		return this.images[idx]?.getHeight() || 0;
	}

	public static setColorOfRGB(graphics: Graphics, r: number, g: number, b: number): void {
		graphics.setColor(Graphics.getColorOfRGB(r, g, b));
	}

	public static setColorOfRGBInt(g: Graphics, rgb: number): void {
		g.setColor(Graphics.getColorOfRGB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255));
	}

	public static drawString(g: Graphics, str: string, x: number, y: number, align: number): void {
		this.drawMultilineString(g, str, x, y, this.currentFont.getHeight() + 1, align);
	}

	public static drawMultilineString(g: Graphics, str: string, x: number, y: number, lineHeight: number, align: number): void {
		let fromIndex = 0;
		let hasNewLine = true;

		for (y += this.currentFont.getHeight(); hasNewLine; y += lineHeight) {
			let toIndex = str.indexOf('\n', fromIndex);
			if (toIndex == -1) {
				toIndex = str.length;
				hasNewLine = false;
			}

			let newX = x;
			if (align == ALIGN_CENTER) {
				newX = x - this.currentFont.stringWidth(str.substring(fromIndex, toIndex)) / 2;
			} else if (align == ALIGN_RIGHT) {
				newX = x - this.currentFont.stringWidth(str.substring(fromIndex, toIndex));
			}

			g.drawString(str.substring(fromIndex, toIndex), newX, y - this.currentFont.getDescent());
			fromIndex = toIndex + 1;
		}
	}

	public static drawBeveledRect(g: Graphics, x: number, y: number, width: number, height: number, borderColor: number, innerColor: number): void {
		if (width >= 2 && height >= 2) {
			this.setColorOfRGBInt(g, borderColor);
			g.fillRect(x + 1, y, width - 2, 2);
			g.fillRect(x + 1, y + height - 2, width - 2, 2);
			g.fillRect(x, y + 1, 2, height - 2);
			g.fillRect(x + width - 2, y + 1, 2, height - 2);
			if (width >= 4 && height >= 4) {
				this.setColorOfRGBInt(g, innerColor);
				g.fillRect(x + 1, y + 2, 1, height - 4);
				g.fillRect(x + width - 2, y + 2, 1, height - 4);
				g.fillRect(x + 2, y + 1, width - 4, height - 2);
			}
		}
	}

	public static drawShadedRect(g: Graphics, x: number, y: number, width: number, height: number, color1: number, color2: number, color3: number): void {
		const colors = [color1, color2, color1, color3];

		for (let i = 0; i < 4; ++i) {
			g.setColor(colors[i]);
			g.fillRect(x + i, y + i, width - i * 2, height - i * 2);
		}
	}

	public static setCurrentFont(fontIndex: number): void {
		switch (fontIndex) {
			case 0:
				//                                                   face style    size     no type
				this.currentFont = Font.getFont(1895826432); // 0111 0001 00000000 00000100 00000000
				break;
			case 1:
				this.currentFont = Font.getFont(1895825664); // 0111 0001 00000000 00000001 00000000
				break;
			case 2:
				this.currentFont = Font.getFont(1895825920); // 0111 0001 00000000 00000010 00000000
				break;
			case 3:
				this.currentFont = Font.getFont(1896940032); // 0111 0001 00010001 00000010 00000000
		}

		this.currentFontHeight = this.currentFont.getHeight();
		this.currentFontIdx = fontIndex;
	}

	public static rand(max: number): number {
		this.rngState = this.rngState * 1103515245 + 12345;
		this.rngState &= 32767;
		return Math.floor((this.rngState * max) / 32768);
	}

	public static abs(n: number): number {
		if (n < 0) {
			n *= -1;
		}

		return n;
	}

	public static splitCount(text: string, delimiter: string): number {
		let startIndex = 0;
		let hasMoreDelimiters = true;

		let count;
		let delimiterIndex;
		for (count = 0; hasMoreDelimiters; startIndex = delimiterIndex + delimiter.length) {
			delimiterIndex = text.indexOf(delimiter, startIndex);
			if (delimiterIndex == -1) {
				delimiterIndex = text.length;
				hasMoreDelimiters = false;
			}

			++count;
		}

		return count;
	}

	public static substringBetweenDelimiters(str: string, startDelimiterCount: number, endDelimiterCount: number, delimiter: string): string {
		let currentIndex = 0;

		for (let i = 0; i < startDelimiterCount; ++i) {
			currentIndex = str.indexOf(delimiter, currentIndex);
			if (currentIndex == -1) {
				this.log('subStringLine:Invalid line selection');
				return '';
			}

			currentIndex += delimiter.length;
		}

		const startIndex = currentIndex;

		for (let i = 0; i < endDelimiterCount; ++i) {
			currentIndex = str.indexOf(delimiter, currentIndex);
			if (currentIndex == -1) {
				return str.substring(startIndex);
			}

			currentIndex += delimiter.length;
		}

		if (0 < endDelimiterCount) {
			currentIndex -= delimiter.length;
		}

		return str.substring(startIndex, currentIndex);
	}

	public static launchCurrentApp(arg: string): boolean {
		let success = true;

		try {
			const args = [arg];
			IApplication.getCurrentApp()?.launch(1, args);
		} catch (e) {
			success = false;
		}

		return success;
	}

	public static setSoftLabel(which: number, str: string): void {
		if (which == 0) {
			this.canvas.setSoftLabel(0, str);
		} else if (which == 1) {
			this.canvas.setSoftLabel(1, str);
		}
	}

	public static selectSoftLabel(index: number): void {
		if (this.previousSoftLabelIdx != index) {
			try {
				this.currentSoftLabelIdx = this.previousSoftLabelIdx;
				this.setSoftLabel(0, this.softLabels[index]);
				this.previousSoftLabelIdx = index;
			} catch (ignored) {}
		}
	}

	public static async downloadGameDataIfNeeded(): Promise<boolean> {
		try {
			if (this.gameDataVersion != this.gameSave[2]) {
				this.gameSave[1] = 0;
				this.gameSave[2] = this.gameDataVersion;
				await this.saveGame();
			}

			if (this.gameSave[1] != 255) {
				this.loadingProgress = this.gameSave[1];
				await this.downloadGameData('', 85149, 128); // 72483
				this.gameSave[1] = 255;
				await this.saveGame();
			}

			return true;
		} catch (e) {
			return false;
		}
	}

	public static async checkGameData(): Promise<void> {
		console.log('checkGameData');

		if (await this.downloadGameDataIfNeeded()) {
			this.goToPage(PAGE_LOADING);
		} else {
			this.goToPage(PAGE_COM_ERROR);
		}
	}

	public static async loadResources(): Promise<void> {
		console.log('loadResources');

		const result = await this.loadImages(128, 0, 93);
		if (result == -1) {
			this.goToPage(PAGE_PREP_ERROR);
		} else if (this.loadSounds() && (await this.loadTexts())) {
			this.goToPage(PAGE_TITLE);
		} else {
			this.goToPage(PAGE_PREP_ERROR);
		}
	}

	public static async loadImages(pos: number, startIndex: number, count: number): Promise<number> {
		try {
			await Thread.sleep(200);
		} catch (e) {}

		try {
			const sizes = await this.loadShortArray(128);
			this.imageSizes = sizes;
			pos += (sizes.length + 1) * 2;

			for (let i = 0; i < startIndex; ++i) {
				pos += sizes[i];
			}

			const stream = new DataInputStream(await Connector.openInputStream('scratchpad:///0;pos=' + pos));

			for (let i = startIndex; i < count; ++i) {
				const imageData = new Uint8Array(sizes[i]);
				await stream.read(imageData);
				const mediaImage = MediaManager.getImage(imageData);
				mediaImage.use();
				this.images[i] = mediaImage.getImage();
				++this.loadingProgress;
				pos += sizes[i];
				this.repaint();

				try {
					await Thread.sleep(10); // 50
				} catch (e) {}
			}

			await stream.close();
			// System.gc();
			return pos;
		} catch (e) {
			console.log(e);
			return -1;
		}
	}

	public static loadImage(index: number): void {
		// TODO
	}

	public static disposeImage(index: number): void {
		if (this.images[index] != null) {
			this.images[index].dispose();
			this.images[index] = null;
		}
	}

	public static downloadingPage(g: Graphics, x: number, y: number): void {
		const barX = (this.canvasWidth - 200) / 2;
		const barY = y + 168;
		this.setColorOfRGBInt(g, 16777215);
		g.fillRect(x, y, 240, 240);
		this.setColorOfRGBInt(g, 16763955);
		g.drawRect(barX, barY, 200, 40);
		this.drawString(g, 'Downloading', this.canvasWidth / 2, barY - this.currentFontHeight - 4, ALIGN_CENTER);
		const progressBarWidth = (200 * this.loadingProgress) / 9; // 8
		g.fillRect(barX, barY, progressBarWidth, 40);
	}

	public static async loadingPage(g: Graphics, x: number, y: number): Promise<void> {
		if (3 < this.loadingProgress) {
			try {
				await Thread.sleep(300);
			} catch (ignored) {}

			this.loadingAnimation(g, x, y, (this.loadingProgress * 8) / 93, this.loadingProgress);
		}
	}

	public static exitGameOnSelect(): void {
		if (this.isKeyPressed(KEY_SELECT)) {
			this.running = false;
		}
	}

	public static showError(g: Graphics, str: string, x: number, y: number): void {
		this.setColorOfRGBInt(g, 0);
		g.fillRect(x, y, 240, 240);
		this.setColorOfRGBInt(g, 16777215);
		this.drawString(g, str, this.canvasWidth / 2, y + 30, ALIGN_CENTER);
		this.drawString(g, 'An error has occured', this.canvasWidth / 2, y + 31 + this.currentFontHeight, ALIGN_CENTER);
		this.drawString(g, 'Confirm:Exit', this.canvasWidth / 2, y + 240 - 10 - this.currentFontHeight, ALIGN_CENTER);
	}

	public static resetTitleScreenState(): void {
		this.setButtonConfig(2, true);
		this.setSelectedButtonIndex(0);
		this.setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
		this.playMusic(1, true);
		this.titleScreenState[2] = 0;
		this.titleScreenState[3] = 0;
		this.titleScreenState[4] = 0;
		this.nextTitleScreenState(0);
	}

	public static nextTitleScreenState(state: number): void {
		switch (state) {
			case 0:
			case 1:
			case 2:
			default:
				this.fullDrawOnNextPaint = true;
				this.titleScreenState[2] = 0;
				this.titleScreenState[1] = state;
		}
	}

	public static titleScreenFlow(): void {
		this.titleScreenState[2]++;
		switch (this.titleScreenState[1]) {
			case 0:
				this.nextTitleScreenState(1);
				return;
			case 1:
				this.titleScreenAnimatedFlow();
				break;
			case 2:
				this.titleScreenFinishedFlow();
		}
	}

	public static titleScreenAnimatedFlow(): void {
		const ready = -48 + this.titleScreenState[2] * 8;
		if (this.isKeyPressed(KEY_SELECT) || 0 <= ready) {
			this.nextTitleScreenState(2);
		}
	}

	public static titleScreenFinishedFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(100, 5);
		} else {
			if (this.titleScreenState[3] <= 32) {
				this.titleScreenState[4] += 2;
			} else {
				this.titleScreenState[4] -= 2;
			}
			this.titleScreenState[3] += this.titleScreenState[4];
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
				case 0:
					this.goToPage(PAGE_MAILBOX_MODE);
					return;
				case 1:
					this.goToPage(PAGE_TRAVEL_MODE);
					return;
				default:
					this.titleScreenState[0] = this.getSelectedButtonIndex();
			}
		}
	}

	public static titleScreen(g: Graphics, x: number, y: number): void {
		switch (this.titleScreenState[1]) {
			case 1:
				this.titleScreenAnimated(g, x, y);
				break;
			case 2:
				this.titleScreenFinished(g, x, y);
		}
	}

	public static titleScreenAnimated(g: Graphics, x: number, y: number): void {
		const planetX = (this.canvasWidth - this.getSpriteWidth(72)) / 2;
		const planetY = y + 54;
		const logoOffsetY = -48 + this.titleScreenState[2] * 8;
		this.setColorOfRGBInt(g, 6728679);
		g.fillRect(x, y, 240, 240);

		// Tamagotchi Park
		this.drawSprite(g, 23, x, y + logoOffsetY, 0);

		// Eggs
		this.drawSprite(g, 6, x, y + 116, 0);
		this.drawSprite(g, 6, x + 120, y + 116, 0);

		this.setColorOfRGBInt(g, 7456538);
		g.fillRect(x, y + 158, 240, 82);

		// Planet
		this.drawSprite(g, 72, planetX, planetY, 0);
		this.drawSprite(g, 73 + ((this.titleScreenState[2] >> 4) & 1), planetX + 23, planetY + 66, 0);
	}

	public static titleScreenFinished(g: Graphics, x: number, y: number): void {
		const planetX = (this.canvasWidth - this.getSpriteWidth(72)) / 2;
		const planetY = y + 54;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 6728679);
			g.fillRect(x, y, 240, 240);

			// Tamagotchi Planet
			this.drawSprite(g, 23, x, y, 0);
			this.drawBackgroundCity(g, 6, this.titleScreenState[3], x, y + 116, 240);
			this.setColorOfRGBInt(g, 7456538);
			g.fillRect(x, y + 158, 240, 82);

			// Bandai
			this.drawSprite(g, 9, this.canvasWidth / 2, y + 158, 2);

			// Planet
			this.drawSprite(g, 72, planetX, planetY, 0);
			this.drawSprite(g, 73 + ((this.titleScreenState[2] >> 4) & 1), planetX + 23, planetY + 66, 0);

			for (let i = 0; i < 2; ++i) {
				this.drawLayoutSpriteButton(g, i, this.titleScreenLayout, 0);
			}
		} else {
			this.setColorOfRGBInt(g, 7456538);
			g.fillRect(x, y + 158 + this.getSpriteHeight(9), 240, 240 - (158 + this.getSpriteHeight(9)));
			this.setColorOfRGBInt(g, 6728679);
			g.fillRect(x, y + 116, 240, this.getSpriteHeight(6));

			this.drawBackgroundCity(g, 6, this.titleScreenState[3], x, y + 116, 240);

			// Planet
			this.drawSprite(g, 72, planetX, planetY, 0);
			this.drawSprite(g, 73 + ((this.titleScreenState[2] >> 4) & 1), planetX + 23, planetY + 66, 0);

			for (let i = 0; i < 2; ++i) {
				this.drawLayoutSpriteButton(g, i, this.titleScreenLayout, 0);
			}
		}

		let eggsY = this.getValueFrom7Table(this.titleScreenLayout, this.getSelectedButtonIndex(), 1);
		eggsY += this.getValueFrom7Table(this.titleScreenLayout, this.getSelectedButtonIndex(), 3) / 2;
		eggsY -= 10;
		this.drawSprite(g, 75, x + 2, y + eggsY, 0);
		this.drawSprite(g, 75, x + 240 - (this.getSpriteWidth(75) - 10), y + eggsY, 0);
	}

	public static drawBackgroundCity(g: Graphics, spriteIndex: number, offset: number, x: number, y: number, screenWidth: number): void {
		while (0 < offset) {
			offset -= this.getSpriteWidth(spriteIndex) || 50;
		}

		while (offset < screenWidth) {
			this.drawSprite(g, spriteIndex, x + offset, y, 0);
			offset += this.getSpriteWidth(spriteIndex) || 50;
		}
	}

	public static resetMailboxModeState(): void {
		this.setButtonConfig(3, true);
		this.setSelectedButtonIndex(0);
		this.setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
		this.playMusic(2, true);
		this.nextMailboxModeState(0);
	}

	public static nextMailboxModeState(state: number): void {
		this.mailboxModeState[2] = 0;
		this.mailboxModeState[1] = state;
	}

	public static mailboxModeFlow(): void {
		this.mailboxModeState[2]++;
		switch (this.mailboxModeState[1]) {
			case 0:
				this.nextMailboxModeState(1);
				break;
			case 1:
				this.mailboxModeSelectFlow();
		}
	}

	public static mailboxModeSelectFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(105, 3);
		} else {
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
				case 0:
					this.goToPage(PAGE_SHOPPING_CENTER);
					return;
				case 1:
					this.goToPage(PAGE_PARENT_CALL);
					return;
				case 2:
					this.goToPage(PAGE_GOTCHI_KING);
					return;
				default:
					this.mailboxModeState[0] = this.getSelectedButtonIndex();
			}
		}
	}

	public static mailboxModePage(g: Graphics, x: number, y: number): void {
		const chatBubbleHeight = (this.currentFontHeight + 1) * 2 + this.currentFontHeight;
		const chatBubbleWidth = 184;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			this.setColorOfRGBInt(g, 6728679);
			g.fillRect(x, y + 78, 240, this.getSpriteHeight(6));

			// City background
			this.drawSprite(g, 6, x, y + 78, 0);
			this.drawSprite(g, 6, x + 120, y + 78, 0);

			this.drawBeveledRect(g, x + 3, y + 3, chatBubbleWidth, chatBubbleHeight + 4, 0, 16056665);
			this.setColorOfRGBInt(g, 16777215);
			// 29: Connect to Tama Planet by phone!
			this.drawString(g, this.getText(29), x + 3 + 2, y + 3 + 2, ALIGN_LEFT);

			for (let i = 0; i < 3; ++i) {
				this.drawLayoutSpriteButton(g, i, this.mailboxModeLayout, 0);
			}
		} else {
			for (let i = 0; i < 3; ++i) {
				this.drawLayoutSpriteButton(g, i, this.mailboxModeLayout, 0);
			}
		}

		let pairDistance = this.getValueFrom7Table(this.mailboxModeLayout, this.getSelectedButtonIndex(), 2) / 2;
		pairDistance -= 10;

		let pairPositionY = this.getValueFrom7Table(this.mailboxModeLayout, this.getSelectedButtonIndex(), 1) + y;
		pairPositionY += this.getValueFrom7Table(this.mailboxModeLayout, this.getSelectedButtonIndex(), 3) / 2;

		this.drawMirroredTamagotchiPair(g, 64, this.canvasWidth / 2, pairPositionY, (pairDistance - this.getSpriteWidth(64)) * 2, this.mailboxModeState[2]);

		if (this.fullDraw) {
			// Bubble tip
			this.drawSprite(g, 90, x + 3 + chatBubbleWidth - 1, y + 3 + chatBubbleHeight / 2 - 5, 0);
			// Planet
			this.drawSprite(g, 89, x + 240 + 2 - this.getSpriteWidth(89), y + 54, 0);
			this.clearOutsideGameArea(g);
		}
	}

	public static resetTravelModeSate(): void {
		this.setButtonConfig(2, true);
		this.setSelectedButtonIndex(0);
		this.setButtonTheme(15947864, 15947864, 16353930, 16777215, 12138328, 16777215, 16777215, 16353930, 16777215, 13816530);
		this.playMusic(0, true);
		this.nextTravelModeState(0);
	}

	public static nextTravelModeState(state: number): void {
		this.travelModeState[1] = state;
		this.travelModeState[2] = 0;
	}

	public static travelModeFlow(): void {
		this.travelModeState[2]++;
		switch (this.travelModeState[1]) {
			case 0:
				this.nextTravelModeState(1);
				break;
			case 1:
				this.travelModeSelectFlow();
		}
	}

	public static travelModeSelectFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(108, 2);
		} else {
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
				case 0:
					this.goToPage(PAGE_TRAVEL_MEMORY);
					return;
				case 1:
					this.goToPage(PAGE_EXCHANGE_PLAZA);
					return;
				default:
					this.travelModeState[0] = this.getSelectedButtonIndex();
			}
		}
	}

	public static travelModePage(g: Graphics, x: number, y: number): void {
		const chatBubbleHeight = (this.currentFontHeight + 1) * 2 + this.currentFontHeight;
		const chatBubbleWidth = 192;

		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			this.setColorOfRGBInt(g, 6728679);
			g.fillRect(x, y + 78, 240, this.getSpriteHeight(6));

			// Background city
			this.drawSprite(g, 6, x, y + 78, 0);
			this.drawSprite(g, 6, x + 120, y + 78, 0);

			this.drawBeveledRect(g, x + 3, y + 3, chatBubbleWidth, chatBubbleHeight + 8, 0, 16056665);
			this.setColorOfRGBInt(g, 16777215);
			// 40: Send your Tama on a trip with your phone!
			this.drawString(g, this.getText(40), x + 3 + 6, y + 3 + 4, ALIGN_LEFT);

			for (let i = 0; i < 2; ++i) {
				this.drawLayoutSpriteButton(g, i, this.travelModeLayout, 0);
			}
		} else {
			for (let i = 0; i < 2; ++i) {
				this.drawLayoutSpriteButton(g, i, this.travelModeLayout, 0);
			}
		}

		let pairDistance = this.getValueFrom7Table(this.travelModeLayout, this.getSelectedButtonIndex(), 2) / 2;
		pairDistance -= 10;

		let pairPositionY = this.getValueFrom7Table(this.travelModeLayout, this.getSelectedButtonIndex(), 1) + y;
		pairPositionY += this.getValueFrom7Table(this.travelModeLayout, this.getSelectedButtonIndex(), 3) / 2;

		this.drawMirroredTamagotchiPair(g, 64, this.canvasWidth / 2, pairPositionY, (pairDistance - this.getSpriteWidth(64)) * 2, this.travelModeState[2]);

		if (this.fullDraw) {
			this.drawSprite(g, 90, x + 3 + chatBubbleWidth - 1, y + 3 + chatBubbleHeight / 2 - 5, 0);
			this.drawSprite(g, 57, x + 240 - this.getSpriteWidth(57) - 2, y + 68, 0);
		}
	}

	public static resetShoppingCenterState(): void {
		this.nextShoppingCenterState(0);
	}

	public static nextShoppingCenterState(state: number): void {
		switch (state) {
			case 0:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 1:
				this.setButtonConfig(5, true);
				this.setSelectedButtonIndex(4);
				this.setButtonTheme2(16750848, 16750848, 16763955, 16777215, 16750848, 16777164, 16750848, 16750848);
				this.selectSoftLabel(SOFT_LABEL_MENU);
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 3:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(3, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
				break;
			case 4:
				this.startSendingViaIr(this.currentPage, 175, 2, 61);
		}

		this.fullDrawOnNextPaint = true;
		this.shoppingCenterState[3] = 0;
		this.shoppingCenterState[2] = 0;
		this.shoppingCenterState[1] = state;
	}

	public static async shoppingCenterFlow(): Promise<void> {
		this.shoppingCenterState[2]++;
		switch (this.shoppingCenterState[1]) {
			case 0:
				this.nextShoppingCenterState(1);
				break;
			case 1:
				this.shoppingCenterItemTypeSelectFlow();
				break;
			case 2:
				await this.downloadShoppingCenterPassword();
				break;
			case 3:
				this.shoppingCenterItemTicketFlow();
				break;
			case 4:
				this.shoppingCenterSendViaIrFlow();
		}
	}

	public static shoppingCenterItemTypeSelectFlow(): void {
		this.shoppingCenterState[3]++;
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(110, 5);
		} else {
			const selectedButton = this.getSelectedButtonIndex();
			const pressedButton = this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_DOWN), this.isKeyPressed(KEY_UP));
			this.shoppingCenterState[0] = this.getSelectedButtonIndex();
			if (selectedButton != this.shoppingCenterState[0]) {
				this.shoppingCenterState[3] = 0;
			}

			if (pressedButton != -1) {
				this.nextShoppingCenterState(2);
			}
		}
	}

	public static downloadShoppingCenterPassword(): void {
		// TODO
	}

	public static prepareShoppingCenterSentData(): void {
		this.setFirstByteSentToServer();
		this.setByteSentToServer(1, 0);
		this.setByteSentToServer(2, 3);

		// shoppingCenterState[0] + 1 --> selected item category
		// 1 - Elite Items
		// 2 - Luxury Items
		// 3 - Fancy Items
		// 4 - Market Items
		// 5 - Common Items
		this.setByteSentToServer(3, this.shoppingCenterState[0] + 1);
	}

	public static shoppingCenterItemTicketFlow(): void {
		this.shoppingCenterState[3]++;
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(115, 6);
		} else {
			if (6 < this.shoppingCenterState[2]) {
				switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
					case 0:
						this.nextShoppingCenterState(4);
						break;
					case 1:
						this.nextShoppingCenterState(0);
						break;
					case 2:
						this.goToPage(PAGE_TITLE);
				}
			}
		}
	}

	public static shoppingCenterSendViaIrFlow(): void {
		if (!this.hasStartedSendingViaIr()) {
			this.nextShoppingCenterState(3);
		} else {
			this.sendViaIrFlow();
		}
	}

	public static shoppingCenterPage(g: Graphics, x: number, y: number): void {
		switch (this.shoppingCenterState[1]) {
			case 0:
			default:
				break;
			case 1:
				this.shoppingCenterItemTypeSelect(g, x, y);
				break;
			case 2:
				this.shoppingCenterIssuingItemTicket(g, x, y);
				break;
			case 3:
				this.shoppingCenterItemTicket(g, x, y);
				break;
			case 4:
				this.shoppingCenterSendViaIR(g, x, y);
		}
	}

	public static shoppingCenterItemTypeSelect(g: Graphics, x: number, y: number): void {
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 2: Shopping Center
			this.drawTextWithBackground(g, this.getText(2), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(2)) + 8, 2);
		}

		for (let i = 0; i < 5; ++i) {
			const colorBaseIndex = i * 4;
			this.setButtonTheme(
				this.shoppingCenterItemColors[colorBaseIndex + 0],
				this.shoppingCenterItemColors[colorBaseIndex + 0],
				this.shoppingCenterItemColors[colorBaseIndex + 0],
				this.shoppingCenterItemColors[colorBaseIndex + 1],
				this.shoppingCenterItemColors[colorBaseIndex + 0],
				this.shoppingCenterItemColors[colorBaseIndex + 2],
				this.shoppingCenterItemColors[colorBaseIndex + 2],
				this.shoppingCenterItemColors[colorBaseIndex + 2],
				this.shoppingCenterItemColors[colorBaseIndex + 3],
				this.shoppingCenterItemColors[colorBaseIndex + 2]
			);
			this.drawLayoutTextButton(g, i, this.shoppingCenterLayoutTable, 0);
		}

		// 26: Use your Gotchi Points from your Keitama to buy Tamagotchi goods! Choose the rank of the item you want and press OK!
		this.drawFullWidthScrollingText(g, x, this.getText(26), y + 2 + 34 + 1, this.shoppingCenterState[2], 12, 16056665, 16777215);

		for (let i = 0; i < 5; ++i) {
			let distance = this.getValueFrom6Table(this.shoppingCenterLayoutTable, i, 2) - 66;
			let baseSpriteId;
			if (i == this.getSelectedButtonIndex()) {
				baseSpriteId = 61;
				distance -= this.abs((this.shoppingCenterState[3] & 31) - 16);
			} else {
				baseSpriteId = 62;
			}

			const spriteId = baseSpriteId + ((this.shoppingCenterState[3] >> 3) & 1);
			let centerY = y + this.getValueFrom6Table(this.shoppingCenterLayoutTable, i, 1);
			centerY += this.getValueFrom6Table(this.shoppingCenterLayoutTable, i, 3) / 2 + 2;
			this.drawTamagotchiPair(g, spriteId, x + this.getValueFrom6Table(this.shoppingCenterLayoutTable, i, 0), centerY, distance, this.shoppingCenterState[2], 0);
		}
	}

	public static shoppingCenterIssuingItemTicket(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		this.setColorOfRGBInt(g, 6728679);
		g.fillRect(x, y + 110, 240, this.getSpriteHeight(6));

		// Background city
		this.drawSprite(g, 6, x, y + 110, 0);
		this.drawSprite(g, 6, x + 120, y + 110, 0);

		// 76: Issuing Item Ticket
		this.drawTextWithBackground(g, this.getText(76), this.canvasWidth / 2, y + 2, 200, 2);
		this.drawSprite(g, 76, this.canvasWidth / 2, y + 100, 2);
	}

	public static shoppingCenterItemTicket(g: Graphics, x: number, y: number): void {
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 79: Item Ticket
			this.drawTextWithBackground(g, this.getText(79), this.canvasWidth / 2, y + 3, this.currentFont.stringWidth(this.getText(79)) + 8, 2);
		} else {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y + this.getValueFrom6Table(this.itemTicketLayout, 0, 1), 240, 240 - (this.getValueFrom6Table(this.itemTicketLayout, 0, 1) + this.getSpriteHeight(0)));
		}
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);

		// 64: Enter the Ticket No. in your Keitama
		this.drawFullWidthScrollingText(g, x, this.getText(64), y + 3 + this.currentFontHeight + 12, this.shoppingCenterState[2], 12, 16056665, 16777215);
		const codeInputY = y + 3 + this.currentFontHeight + 12 + this.currentFontHeight + 4;
		if (this.fullDraw) {
			this.drawCodeInputBackground(g, this.canvasWidth / 2, codeInputY, 2, 0, 16770972, 16750748, 16770972);
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, codeInputY + 10, 56, this.shoppingCenterState[2] >> 1, false);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, codeInputY + 10, 56, this.shoppingCenterState[2] >> 1, false, 16770972);
		}

		this.drawCodeInput(g, this.canvasWidth / 2, codeInputY + 4, 62, false, this.fullDraw);

		for (let i = 0; i < 3; ++i) {
			this.drawLayoutTextButton(g, i, this.itemTicketLayout, 0);
		}

		this.drawMirroredTamagotchiPair(
			g,
			61,
			x + this.getValueFrom6Table(this.itemTicketLayout, this.getSelectedButtonIndex(), 0),
			y + this.getValueFrom6Table(this.itemTicketLayout, this.getSelectedButtonIndex(), 1) + this.getValueFrom6Table(this.itemTicketLayout, this.getSelectedButtonIndex(), 3) / 2,
			this.getValueFrom6Table(this.itemTicketLayout, this.getSelectedButtonIndex(), 2) - 10,
			this.shoppingCenterState[2]
		);
	}

	public static shoppingCenterSendViaIR(g: Graphics, x: number, y: number): void {
		this.sendViaIR(g, x, y);
	}

	public static drawFullWidthScrollingText(g: Graphics, x: number, text: string, y: number, time: number, speed: number, textColor: number, backgroundColor: number): void {
		this.drawScrollingText(g, text, x, y, 240, time, speed, textColor, backgroundColor);
	}

	public static drawScrollingText(g: Graphics, text: string, x: number, y: number, width: number, time: number, speed: number, textColor: number, backgroundColor: number): void {
		this.setColorOfRGBInt(g, backgroundColor);
		g.fillRect(x, y, width, this.currentFontHeight);
		this.setColorOfRGBInt(g, textColor);
		const stringWidth = this.currentFont.stringWidth(text);
		this.drawString(g, text, x + width - ((time * speed) % (width + stringWidth)), y, ALIGN_LEFT);
	}

	public static drawDownloadUploadAnimations(g: Graphics, x: number, y: number, distance: number, time: number, dir: boolean): void {
		this.drawSprite(g, 32, x - (distance + 6), y + 18, 0);
		this.drawSprite(g, (!dir ? 77 : 78) + (time % 3) * 2, x - (distance + 5), y, 0);
		this.drawSprite(g, 33, x + (distance - 3), y + 20, 0);
		this.drawSprite(g, (dir ? 83 : 84) + (time % 3) * 2, x + (distance - 3), y, 0);
	}

	public static drawDownloadUploadAnimationsWithBackground(g: Graphics, x: number, y: number, distance: number, time: number, dir: boolean, backgroundColor: number): void {
		this.setColorOfRGBInt(g, backgroundColor);
		g.fillRect(x - (distance + 6), y, this.getSpriteWidth(32), 18 + this.getSpriteHeight(32));
		g.fillRect(x + (distance - 3), y, this.getSpriteWidth(33), 20 + this.getSpriteHeight(33));
		this.drawDownloadUploadAnimations(g, x, y, distance, time, dir);
	}

	public static drawTextWithBackground(g: Graphics, text: string, x: number, y: number, width: number, align: number): void {
		const height = this.calculateTextHeight(text);
		const borderColor = 0;
		const color = 16056665;
		const textColor = 16777215;
		if (align == 2) {
			x -= width / 2;
		} else if (align == 1) {
			x -= width;
		}

		this.drawBeveledRect(g, x, y, width, height, borderColor, color);
		this.setColorOfRGBInt(g, textColor);
		this.drawString(g, text, x + width / 2, y + 2, ALIGN_CENTER);
	}

	public static calculateTextHeight(text: string): number {
		return (this.currentFontHeight + 1) * (this.splitCount(text, '\n') - 1) + this.currentFontHeight + 4;
	}

	public static drawCodeInputBackground(g: Graphics, x: number, y: number, align: number, outerBorderColor: number, outerFillColor: number, innerBorderColor: number, innerFillColor: number): void {
		if (align == 2) {
			x -= 88;
		} else if (align == 1) {
			x -= 176;
		}

		this.drawBeveledRect(g, x, y, 176, 70, outerBorderColor, outerFillColor);
		this.drawBeveledRect(g, x + 3, y + 3, 170, 64, innerBorderColor, innerFillColor);
	}

	public static drawCodeInputBackgroundWithHorizontalLine(
		g: Graphics,
		x: number,
		y: number,
		outerBorderColor: number,
		outerFillColor: number,
		innerBorderColor: number,
		innerFillColor: number,
		lineColor: number
	): void {
		const width = 32;
		const newY = y + 24;

		this.setColorOfRGBInt(g, lineColor);
		g.fillRect(x, newY, width, 2);
		g.fillRect(x, newY + 2 + 1, width, 16);
		g.fillRect(x, newY + 2 + 1 + 16 + 1, width, 2);
		g.fillRect(x + width + 176, newY, width, 2);
		g.fillRect(x + width + 176, newY + 2 + 1, width, 16);
		g.fillRect(x + width + 176, newY + 2 + 1 + 16 + 1, width, 2);

		this.drawCodeInputBackground(g, this.canvasWidth / 2, y, 2, outerBorderColor, outerFillColor, innerBorderColor, innerFillColor);
	}

	public static drawMirroredTamagotchiPair(g: Graphics, baseSpriteId: number, centerX: number, centerY: number, distance: number, time: number): void {
		this.drawTamagotchiPair(g, baseSpriteId, centerX, centerY, distance, time, 1);
	}

	public static drawTamagotchiPair(g: Graphics, baseSpriteId: number, centerX: number, centerY: number, distance: number, time: number, offsetAnimation: number): void {
		this.drawSprite(
			g,
			baseSpriteId + (offsetAnimation & -((time >> 2) & 1)),
			centerX - distance / 2 - this.getSpriteWidth(baseSpriteId) - 2 - 2,
			centerY - this.getSpriteHeight(baseSpriteId) / 2,
			0
		);
		this.drawSprite(g, baseSpriteId + (offsetAnimation & -(((time >> 2) + 1) & 1)), centerX + distance / 2 + 2 + 2, centerY - this.getSpriteHeight(baseSpriteId) / 2, 0);
	}

	public static drawCodeInput(g: Graphics, x: number, y: number, height: number, showCursor: boolean, redraw: boolean): void {
		const rightX = x - 35;
		const topY = y + (height - 49) / 2;

		const cursorIndex = this.getCursorIndex();
		const previousCursorIndex = this.getPreviousCursorIndex();

		this.setColorOfRGBInt(g, 16777215);

		if (redraw) {
			g.fillRect(rightX - 2, y, 75, height);
		} else {
			g.fillRect(rightX + (previousCursorIndex % 5) * 15, topY + Math.floor(previousCursorIndex / 5) * 26, 11, 24);
			g.fillRect(rightX + (cursorIndex % 5) * 15, topY + Math.floor(cursorIndex / 5) * 26, 11, 24);
		}

		if (showCursor) {
			this.setColorOfRGBInt(g, 13619071);
			g.fillRect(rightX + (cursorIndex % 5) * 15, topY + Math.floor(cursorIndex / 5) * 26, 11, 24);
		}

		this.setColorOfRGBInt(g, 0);

		if (redraw) {
			this.drawAllDigits(g, x, topY, 2);
		} else {
			this.drawDigit(g, x, topY, 2, previousCursorIndex);
			this.drawDigit(g, x, topY, 2, cursorIndex);
		}
	}

	public static resetParentCallState(): void {
		this.clearCodeInput();
		this.setCursorIndex(0);
		this.nextParentCallState(0);
	}

	public static nextParentCallState(state: number): void {
		switch (state) {
			case 0:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 1:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				this.parentCallState[0] = 0;
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 3:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 4:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 5:
				this.startSendingViaIr(this.currentPage, 177, 2, 55);
		}

		this.fullDrawOnNextPaint = true;
		this.parentCallState[6] = 0;
		this.parentCallState[1] = state;
	}

	public static clearDownloadedParentCallData(): void {
		for (let i = 0; i < 2; ++i) {
			if (this.parentCallImages[i] != null) {
				this.parentCallImages[i]?.dispose();
				this.parentCallImages[i] = null;
			}
		}

		this.parentCallText = null;
		this.parentCallQuote = null;
		// System.gc();
	}

	public static async parentCallFlow(): Promise<void> {
		this.parentCallState[6]++;
		switch (this.parentCallState[1]) {
			case 0:
				this.nextParentCallState(1);
				break;
			case 1:
				this.parentCallCodeInputFlow();
				break;
			case 2:
				await this.downloadParentCallData();
				break;
			case 3:
				this.parentCallExplanationFlow();
				break;
			case 4:
				this.parentCallAllowanceTicketFlow();
				break;
			case 5:
				this.parentCallSendViaIrFlow();
		}
	}

	public static parentCallCodeInputFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(121, 6);
		} else {
			if (this.parentCallState[0] != 0) {
				if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
					this.nextParentCallState(2);
				} else if (!this.isKeyPressed(KEY_SELECT)) {
					if (this.isKeyPressed(KEY_UP_LEFT)) {
						this.setCursorIndex(9);
						this.parentCallState[0] = 0;
					} else if (this.isKeyPressed(KEY_DOWN_RIGHT)) {
						this.setCursorIndex(0);
						this.parentCallState[0] = 0;
					}
				}
			} else if (
				this.handleCodeInput(
					this.isKeyPressed(KEY_LEFT),
					this.isKeyPressed(KEY_RIGHT),
					this.isKeyPressed(KEY_UP),
					this.isKeyPressed(KEY_DOWN),
					this.isKeyPressed(KEY_SELECT),
					this.getPressedNumber()
				)
			) {
				this.parentCallState[0] = 1;
			}

			this.setSelectedButtonIndex(this.parentCallState[0]);
		}
	}

	public static async downloadParentCallData(): Promise<void> {
		this.clearDownloadedParentCallData();
		this.prepareParentCallSentData();
		let inputStream: DataInputStream | null = null;

		try {
			inputStream = await this.sendPreparedDataToServer(13);
			this.unknownOperationOnServerResponse(inputStream);

			// Java: int errorMessageLength = inputStream.read();
			// In TS DataInputStream, readUnsignedByte() is used for the same 0..255 behavior.
			const errorMessageLength = await inputStream.readUnsignedByte();

			if (errorMessageLength > 0) {
				const errorMessage = await this.readString(inputStream, errorMessageLength);
				this.showErrorPage(this.currentPage, 2, 1, errorMessage);
			} else {
				const passwordData = new Uint8Array(10);
				await inputStream.read(passwordData);

				for (let i = 0; i < 2; ++i) {
					const imageSize = await inputStream.readUnsignedShort();
					this.parentCallImages[i] = await this.readImage(inputStream, imageSize);
				}

				const textSize = await inputStream.readUnsignedShort();
				this.parentCallText = await this.readString(inputStream, textSize);

				this.parentCallState[2] = 0;
				this.parentCallState[4] = 0;
				this.parentCallState[5] = this.countQuotedSegments(this.parentCallText);
				this.parentCallQuote = this.findNthQuote(this.parentCallText, this.parentCallState[4]);
				this.parseAndStoreDownloadedPassword(passwordData);
				this.nextParentCallState(3);
			}
		} catch (e) {
			this.log('e:' + e);
			// 92: Communication has failed. Would you like to try again?
			this.showErrorPage(this.currentPage, 2, 1, this.getText(92));
		} finally {
			if (inputStream != null) {
				try {
					await inputStream.close();
				} catch (ignored) {}
			}

			this.playSound(6, false);
		}
	}

	public static prepareParentCallSentData(): void {
		this.setFirstByteSentToServer();
		this.setByteSentToServer(1, 1);
		this.setByteSentToServer(2, 3);

		for (let i = 0; i < 10; ++i) {
			this.setByteSentToServer(3 + i, this.getDigitBankA(i));
		}
	}

	public static parentCallExplanationFlow(): void {
		this.parentCallState[3]++;
		if (6 < this.parentCallState[6]) {
			if (-1 != this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT))) {
				this.parentCallState[4]++;
				this.parentCallState[3] = 0;
				if (this.parentCallState[5] <= this.parentCallState[4]) {
					this.generateDerivedCodeInBankB();
					if (3 < this.getDigitBankB(2)) {
						this.goToPage(PAGE_TITLE);
					} else {
						this.nextParentCallState(4);
					}
				} else {
					this.parentCallQuote = this.findNthQuote(this.parentCallText!, this.parentCallState[4]);
				}
			}
		} else {
			const quoteWidth = this.currentFont.stringWidth(this.parentCallQuote);
			if (quoteWidth + 232 <= this.parentCallState[3] * 12) {
				this.parentCallState[3] = 0;
			}
		}
	}

	public static parentCallAllowanceTicketFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(127, 6);
		} else {
			if (6 < this.parentCallState[6]) {
				switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
					case 0:
						this.nextParentCallState(5);
						break;
					case 1:
						this.goToPage(PAGE_TITLE);
				}
			}
		}
	}

	public static parentCallSendViaIrFlow(): void {
		if (!this.hasStartedSendingViaIr()) {
			this.nextParentCallState(4); // Go back
		} else {
			this.sendViaIrFlow();
		}
	}

	public static parentCallPage(g: Graphics, x: number, y: number): void {
		switch (this.parentCallState[1]) {
			case 0:
			default:
				break;
			case 1:
				this.parentCallCodeInput(g, x, y);
				break;
			case 2:
				this.connectingToTamaPlanet(g, x, y);
				break;
			case 3:
				this.parentCallExplanation(g, x, y);
				break;
			case 4:
				this.parentCallAllowanceTicket(g, x, y);
				break;
			case 5:
				this.parentCallSendViaIR(g, x, y);
		}
	}

	public static parentCallCodeInput(g: Graphics, x: number, y: number): void {
		// 17: Connect with your parent on Tamagotchi Planet!
		const textHeight = this.calculateTextHeight(this.getText(17));
		const buttonY = y + 240 - 42;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240 - this.getSpriteHeight(0));
			// 17: Connect with your parent on Tamagotchi Planet!
			this.drawTextWithBackground(g, this.getText(17), this.canvasWidth / 2, y + 3, 232, 2);
		}

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 65: Enter the Address No. shown on your Keitama
		this.drawFullWidthScrollingText(g, x, this.getText(65), y + 3 + textHeight + 3, this.parentCallState[6], 12, 16056665, 16777215);
		const inputY = y + 3 + textHeight + 3 + this.currentFontHeight + 4 + 20;
		const showCursor = 0 == this.getSelectedButtonIndex() && (this.parentCallState[6] & 4) != 0;
		this.drawCodeInputWithSimpleBackground(g, this.canvasWidth / 2, inputY, showCursor);
		// 18: OK
		this.drawTextButton(g, 1, this.getText(18), this.canvasWidth / 2, buttonY, 100, 28, 2, 0);
		if (this.fullDraw) {
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, inputY + 5, 56, this.parentCallState[6] >> 1, true);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, inputY + 5, 56, this.parentCallState[6] >> 1, true, 16777076);
		}

		if (1 == this.getSelectedButtonIndex()) {
			this.drawMirroredTamagotchiPair(g, 55, this.canvasWidth / 2, y + 240 - 42 + 14, 120, this.parentCallState[6]);
		}
	}

	public static connectingToTamaPlanet(g: Graphics, x: number, y: number): void {
		this.loadingAnimation(g, x, y, 8, 0);
		// Connecting to Tama Planet
		this.drawSprite(g, 24, x + 126, y + 100, 0);
		const spriteWidth = this.getSpriteWidth(24);

		for (let i = 0; i < 3; ++i) {
			this.drawSprite(g, 7, x + 126 - 6 * i, y + 118, 0);
			this.drawSprite(g, 8, x + 126 + spriteWidth + 6 * i, y + 118, 0);
		}
	}

	public static parentCallExplanation(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		const titleY = y + 4;
		// 23: Parent Call
		this.drawTextWithBackground(g, this.getText(23), this.canvasWidth / 2, titleY, this.currentFont.stringWidth(this.getText(23)) + 8, 2);
		const imageY = titleY + this.currentFontHeight + 8 + 6;
		this.drawSprite(g, 58, x, imageY, 0); // House
		this.drawImage(g, this.parentCallImages[(this.parentCallState[6] >> 3) & 1], x + 144, imageY, 0);
		const scrollingTextY = imageY + 130 + 1;
		const scrollingTextX = (this.canvasWidth - 232) / 2;
		this.drawScrollingText(g, this.parentCallQuote ?? '', scrollingTextX, scrollingTextY, 232, this.parentCallState[3], 12, 16777215, 16056665);
		this.setColorOfRGBInt(g, 0);
		g.drawRect(scrollingTextX, scrollingTextY - 1, 231, this.currentFontHeight + 1);
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, scrollingTextY, 4, this.currentFontHeight);
		g.fillRect(scrollingTextX, scrollingTextY - 1, 1, 1);
		g.fillRect(scrollingTextX, scrollingTextY + this.currentFontHeight, 1, 1);
		g.fillRect(scrollingTextX + 232, scrollingTextY, 4, this.currentFontHeight);
		g.fillRect(scrollingTextX - 1, scrollingTextY - 1, 1, 1);
		g.fillRect(scrollingTextX + 232 - 1, scrollingTextY + this.currentFontHeight, 1, 1);

		let textIndex;
		if (this.parentCallState[5] - 1 <= this.parentCallState[4]) {
			textIndex = 22;
		} else {
			textIndex = 87;
		}

		// 7: Explanation
		this.drawTextButton(g, 0, this.getText(textIndex), this.canvasWidth / 2, y + 240 - 36, 160, 28, 2, 0);
		this.drawSprite(g, 27, x + 1, y + 1, 0);
		this.drawSprite(g, 28, x + 240 - 1 - this.getSpriteWidth(28), y + 1, 0);
		this.drawSprite(g, 37 + ((this.parentCallState[6] >> 1) & 1), x + 240 - 44, y + imageY + 1, 0);
		this.drawSprite(g, 12, x + 240 - 38, imageY + 9, 0);
		this.drawSprite(g, 92, x + 140, scrollingTextY - 16, 0);
		this.drawMirroredTamagotchiPair(g, 55, this.canvasWidth / 2, y + 240 - 36 + this.getSpriteHeight(55) / 2, 160, this.parentCallState[6]);
	}

	public static parentCallAllowanceTicket(g: Graphics, x: number, y: number): void {
		// 67: Allowance Ticket
		const textHeight = this.calculateTextHeight(this.getText(67));
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 67: Allowance Ticket
			this.drawTextWithBackground(g, this.getText(67), this.canvasWidth / 2, y + 3, this.currentFont.stringWidth(this.getText(67)) + 8, 2);
		} else {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y + this.getValueFrom6Table(this.allowanceTicketLayout, 0, 1) - 5, 240, 240 - (this.getValueFrom6Table(this.allowanceTicketLayout, 0, 1) - 5 + this.getSpriteHeight(0)));
		}

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);

		// 66: Enter the Allowance Ticket No. in your Keitama!
		this.drawFullWidthScrollingText(g, x, this.getText(66), y + 3 + textHeight + 3, this.parentCallState[6], 12, 16056665, 16777215);
		const inputY = y + 3 + textHeight + 3 + this.currentFontHeight + 12;
		this.drawCodeInputWithSimpleBackground(g, this.canvasWidth / 2, inputY, false);

		for (let i = 0; i < 2; ++i) {
			this.drawLayoutTextButton(g, i, this.allowanceTicketLayout, 0);
		}

		this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, inputY + 5, 56, this.parentCallState[6] >> 1, false, 16777076);
		this.drawMirroredTamagotchiPair(
			g,
			55,
			x + this.getValueFrom6Table(this.allowanceTicketLayout, this.getSelectedButtonIndex(), 0),
			y + this.getValueFrom6Table(this.allowanceTicketLayout, this.getSelectedButtonIndex(), 1) + this.getValueFrom6Table(this.allowanceTicketLayout, this.getSelectedButtonIndex(), 3) / 2,
			this.getValueFrom6Table(this.allowanceTicketLayout, this.getSelectedButtonIndex(), 2),
			this.parentCallState[6]
		);
	}

	public static parentCallSendViaIR(g: Graphics, x: number, y: number): void {
		this.sendViaIR(g, x, y);
	}

	public static drawCodeInputWithSimpleBackground(g: Graphics, x: number, y: number, showCursor: boolean): void {
		const newX = x - 88;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16777076);
			g.drawRect(newX, y, 176, 70);
			g.fillRect(newX + 2, y + 2, 173, 67);
		}

		this.drawCodeInput(g, x, y + 2, 67, showCursor, this.fullDraw);
	}

	public static loadingAnimation(g: Graphics, x: number, y: number, circleCount: number, colorOffset: number): void {
		this.setColorOfRGBInt(g, 11367);
		g.fillRect(x, y, 240, 240);
		this.drawSprite(g, 1, x, y + 240 - this.getSpriteHeight(1), 0);
		const xOffset = 0;
		this.drawSprite(g, 3, x + 240 + xOffset, y + 10, 1);
		this.drawRainbowCircles(g, x + 50, y + 108, 12, -8, 5, colorOffset, circleCount);
	}

	public static resetGotchiKingState(): void {
		for (let i = 0; i < this.imagesToTemporarilyDispose.length; ++i) {
			this.disposeImage(this.imagesToTemporarilyDispose[i]);
		}

		this.clearCodeInput();
		this.setCursorIndex(0);
		this.nextGotchiKingState(0);
	}

	public static nextGotchiKingState(nextStateId: number): void {
		switch (nextStateId) {
			case 0:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 1:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.gotchiKingState[0] = 0;
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 3:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 4:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 5:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 6:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 7:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 8:
				this.startSendingViaIr(this.currentPage, 179, 2, 39);
		}

		this.fullDrawOnNextPaint = true;
		this.gotchiKingState[3] = 0;
		this.gotchiKingState[1] = nextStateId;
	}

	public static clearDownloadedGotchiKingData(): void {
		for (let i = 0; i < 2; ++i) {
			if (this.gotchiKingImages[i] != null) {
				this.gotchiKingImages[i]?.dispose();
				this.gotchiKingImages[i] = null;
			}
		}

		// System.gc();

		for (let i = 0; i < this.imagesToTemporarilyDispose.length; ++i) {
			try {
				this.loadImage(this.imagesToTemporarilyDispose[i]);
			} catch (ignored) {}
		}

		// System.gc();
	}

	public static async gotchiKingFlow(): Promise<void> {
		this.gotchiKingState[3]++;
		switch (this.gotchiKingState[1]) {
			case 0:
				this.nextGotchiKingState(1);
				break;
			case 1:
				this.gotchiKingCodeInputFlow();
				break;
			case 2:
				await this.downloadGotchiKingData();
				break;
			case 3:
				this.gotchiKingBroadcastFlow1();
				break;
			case 4:
				this.gotchiKingBroadcastFlow2();
				break;
			case 5:
				this.gotchiKingInviteFlow();
				break;
			case 6:
				this.gotchiKingIssuingInvitationFlow();
				break;
			case 7:
				this.gotchiKingInviteTicketFlow();
				break;
			case 8:
				this.gotchiKingSendViaIrFlow();
		}
	}

	public static gotchiKingCodeInputFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(133, 6);
		} else {
			if (this.gotchiKingState[0] != 0) {
				if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
					this.nextGotchiKingState(2);
				} else if (!this.isKeyPressed(KEY_SELECT)) {
					if (this.isKeyPressed(KEY_UP_LEFT)) {
						this.setCursorIndex(9);
						this.gotchiKingState[0] = 0;
					} else if (this.isKeyPressed(KEY_DOWN_RIGHT)) {
						this.setCursorIndex(0);
						this.gotchiKingState[0] = 0;
					}
				}
			} else if (
				this.handleCodeInput(
					this.isKeyPressed(KEY_LEFT),
					this.isKeyPressed(KEY_RIGHT),
					this.isKeyPressed(KEY_UP),
					this.isKeyPressed(KEY_DOWN),
					this.isKeyPressed(KEY_SELECT),
					this.getPressedNumber()
				)
			) {
				this.gotchiKingState[0] = 1;
			}

			this.setSelectedButtonIndex(this.gotchiKingState[0]);
		}
	}

	public static downloadGotchiKingData(): void {
		// TODO
	}

	public static prepareGotchiKingSentData(): void {
		this.setFirstByteSentToServer();
		this.setByteSentToServer(1, 2);
		this.setByteSentToServer(2, 3);

		for (let i = 0; i < 10; ++i) {
			// Copy input digits
			this.setByteSentToServer(3 + i, this.getDigitBankA(i));
		}
	}

	public static gotchiKingBroadcastFlow1(): void {
		if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT)) != -1) {
			this.nextGotchiKingState(4);
		}
	}

	public static gotchiKingBroadcastFlow2(): void {
		if (45 <= this.gotchiKingState[3] || this.isKeyPressed(KEY_SELECT)) {
			this.nextGotchiKingState(5);
		}
	}

	public static gotchiKingInviteFlow(): void {
		if (6 <= this.gotchiKingState[5]) {
			this.gotchiKingState[5] = 0;
			this.gotchiKingState[4]++;
			this.gotchiKingState[4] %= 2;
		} else {
			this.gotchiKingState[5]++;
		}

		if (6 < this.gotchiKingState[3] && this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT)) != -1) {
			this.nextGotchiKingState(6);
		}
	}

	public static gotchiKingIssuingInvitationFlow(): void {
		if (this.gotchiKingState[3] > 30) {
			this.playSound(6, false);
			this.nextGotchiKingState(7);
		}
	}

	public static gotchiKingInviteTicketFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(139, 6);
		} else {
			if (6 < this.gotchiKingState[3]) {
				switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
					case 0:
						this.nextGotchiKingState(8);
						break;
					case 1:
						this.goToPage(PAGE_TITLE);
				}
			}
		}
	}

	public static gotchiKingSendViaIrFlow(): void {
		if (!this.hasStartedSendingViaIr()) {
			this.nextGotchiKingState(7); // Go back
		} else {
			this.sendViaIrFlow();
		}
	}

	public static gotchiKingPage(g: Graphics, x: number, y: number): void {
		switch (this.gotchiKingState[1]) {
			case 0:
			default:
				break;
			case 1:
				this.gotchiKingCodeInput(g, x, y);
				break;
			case 2:
				this.gotchiKingConnecting(g, x, y);
				break;
			case 3:
			case 4:
				this.gotchiKingBroadcast(g, x, y);
				break;
			case 5:
				this.gotchiKingInvite(g, x, y);
				break;
			case 6:
				this.gotchiKingIssuingInvitation(g, x, y);
				break;
			case 7:
				this.gotchiKingInviteTicket(g, x, y);
				break;
			case 8:
				this.gotchiKingSendViaIR(g, x, y);
		}
	}

	public static gotchiKingCodeInput(g: Graphics, x: number, y: number): void {
		// 30: Connect with the Gotchi King on Tamagotchi Planet!
		const textHeight = this.calculateTextHeight(this.getText(30));
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240 - this.getSpriteHeight(0));
			// 30: Connect with the Gotchi King on Tamagotchi Planet!
			this.drawTextWithBackground(g, this.getText(30), this.canvasWidth / 2, y + 2, 230, 2);
		}

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);

		// 68: Enter the Gotchi King Address No. shown on your Keitama
		this.drawFullWidthScrollingText(g, x, this.getText(68), y + 2 + textHeight + 2, this.gotchiKingState[3], 12, 16056665, 16777215);
		const showCursor = 0 == this.getSelectedButtonIndex() && (this.gotchiKingState[3] & 4) != 0;
		const inputY = y + 2 + textHeight + 2 + this.currentFontHeight + 3;
		this.drawCodeInputWithSimpleBackground(g, this.canvasWidth / 2, inputY, showCursor);
		// 18: OK
		this.drawTextButton(g, 1, this.getText(18), this.canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
		if (this.fullDraw) {
		}

		if (this.fullDraw) {
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, inputY + 5, 56, this.gotchiKingState[3] >> 1, true);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, inputY + 5, 56, this.gotchiKingState[3] >> 1, true, 16777076);
		}

		if (1 == this.getSelectedButtonIndex()) {
			this.drawMirroredTamagotchiPair(g, 39, this.canvasWidth / 2, y + 240 - 42 + this.getSpriteHeight(39) / 2, 100, this.gotchiKingState[3]);
		}
	}

	public static gotchiKingConnecting(g: Graphics, x: number, y: number): void {
		this.connectingToTamaPlanet(g, x, y);
	}

	public static gotchiKingBroadcast(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		const var3 = y + 68;
		this.setColorOfRGBInt(g, 6532583);
		g.fillRect(x, var3, 240, 118);
		const titleY = y + 4;
		// 31: Gotchi King Broadcast
		this.drawTextWithBackground(g, this.getText(31), this.canvasWidth / 2, titleY, 200, 2);
		let var5 = var3 + 15;
		if (this.gotchiKingState[1] == 4) {
			let var6;
			if (20 < this.gotchiKingState[3]) {
				var6 = 20;
			} else {
				var6 = this.gotchiKingState[3];
			}

			var5 -= (var6 * 6) / 20;
		}

		this.drawSprite(g, 6, x, var3 + 76, 0);
		this.drawSprite(g, 6, x + 120, var3 + 76, 0);
		this.drawSprite(g, 66, this.canvasWidth / 2, var3, 2);
		this.drawBeveledRect(g, (this.canvasWidth - 36) / 2, var3 + 21, 36, 8, 3805255, 16315136);
		this.drawBeveledRect(g, (this.canvasWidth - 36) / 2, var5, 36, 8, 3805255, 16315136);
		if (this.gotchiKingState[1] == 3) {
			// 75: Enter
			this.drawTextButton(g, 0, this.getText(75), this.canvasWidth / 2, y + 240 - 44, 100, 28, 2, 0);
			this.drawMirroredTamagotchiPair(g, 39, this.canvasWidth / 2, y + 240 - 44 + this.getSpriteHeight(39) / 2, 100, this.gotchiKingState[3]);
		}

		this.drawSprite(g, 27, x + 1, y + 1, 0);
		this.drawSprite(g, 28, x + 240 - 1 - this.getSpriteWidth(28), y + 1, 0);
		this.drawSprite(g, 37 + ((this.gotchiKingState[3] >> 1) & 1), x + 240 - 44, var3 + 2, 0);
		this.drawSprite(g, 12, x + 240 - 38, var3 + 10, 0);
	}

	public static gotchiKingInvite(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		const titleY = y + 4;
		// 31: Gotchi King Broadcast
		this.drawTextWithBackground(g, this.getText(31), this.canvasWidth / 2, titleY, 200, 2);
		const imageY = y + 68;
		const imageIndex = this.gotchiKingState[4];
		this.drawImage(g, this.gotchiKingImages[imageIndex], x + 0, imageY, 0);
		// 32: Invite
		this.drawTextButton(g, 0, this.getText(32), this.canvasWidth / 2, y + 240 - 44, 160, 28, 2, 0);
		// Antennas
		this.drawSprite(g, 27, x + 1, y + 1, 0);
		this.drawSprite(g, 28, x + 240 - 1 - this.getSpriteWidth(28), y + 1, 0);
		// Blue Z
		this.drawSprite(g, 37 + ((this.gotchiKingState[3] >> 1) & 1), x + 240 - 44, imageY + 2, 0);
		// LIVE
		this.drawSprite(g, 12, x + 240 - 38, imageY + 10, 0);
		this.drawMirroredTamagotchiPair(g, 39, this.canvasWidth / 2, y + 240 - 44 + this.getSpriteHeight(39) / 2, 160, this.gotchiKingState[3]);
	}

	public static gotchiKingIssuingInvitation(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 77: Issuing Invitation
		this.drawTextWithBackground(g, this.getText(77), this.canvasWidth / 2, y + 2, 200, 2);
		const stepX = 16;
		const radius = 6;
		const circleCount = 11;
		const width = radius + stepX * (circleCount - 1);
		this.drawRainbowCircles(g, (this.canvasWidth - width) / 2, y + 68, stepX, 0, radius, this.gotchiKingState[3], circleCount);
		this.drawSprite(g, 34, this.canvasWidth / 2, y + 90, 2);
	}

	public static gotchiKingInviteTicket(g: Graphics, x: number, y: number): void {
		// 69: Invite Ticket
		const textHeight = this.calculateTextHeight(this.getText(69));
		let newY;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 69: Invite Ticket
			this.drawTextWithBackground(g, this.getText(69), this.canvasWidth / 2, y + 3, this.currentFont.stringWidth(this.getText(69)) + 8, 2);
		} else {
			newY = y + this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, 0, 1);
			newY += this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, 0, 3) / 2;
			newY -= this.getSpriteHeight(39) / 2;
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, newY, 240, 240 - (newY + this.getSpriteHeight(39) / 2));
		}

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 70: Enter the Invitation Ticket No. in your Keitama!
		this.drawFullWidthScrollingText(g, x, this.getText(70), y + 3 + textHeight + 3, this.gotchiKingState[3], 12, 16056665, 16777215);

		for (let i = 0; i < 2; ++i) {
			this.drawLayoutTextButton(g, i, this.gotchiKingInviteTicketLayout, 0);
		}

		const codeInputY = y + 3 + textHeight + 3 + this.currentFontHeight + 12;
		if (this.fullDraw) {
			this.drawCodeInputBackground(g, this.canvasWidth / 2, codeInputY, 2, 0, 16756418, 13722050, 16756418);
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, codeInputY + 10, 56, this.gotchiKingState[3] >> 1, false);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, codeInputY + 10, 56, this.gotchiKingState[3] >> 1, false, 16756418);
		}

		this.drawCodeInput(g, this.canvasWidth / 2, codeInputY + 4, 62, false, this.fullDraw);
		this.drawMirroredTamagotchiPair(
			g,
			39,
			x + this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, this.getSelectedButtonIndex(), 0),
			y +
				this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, this.getSelectedButtonIndex(), 1) +
				this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, this.getSelectedButtonIndex(), 3) / 2 +
				1,
			this.getValueFrom6Table(this.gotchiKingInviteTicketLayout, this.getSelectedButtonIndex(), 2),
			this.gotchiKingState[3]
		);
	}

	public static gotchiKingSendViaIR(g: Graphics, x: number, y: number): void {
		this.sendViaIR(g, x, y);
	}

	public static resetTravelMemoryState(): void {
		this.clearCodeInput();
		this.setCursorIndex(0);
		this.nextTravelMemoryState(0);
	}

	public static nextTravelMemoryState(nextStateId: number): void {
		switch (nextStateId) {
			case 0:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 1:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.travelMemoryState[0] = 0;
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 3:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 4:
				this.selectSoftLabel(SOFT_LABEL_MENU);
		}

		this.fullDrawOnNextPaint = true;
		this.travelMemoryState[2] = 0;
		this.travelMemoryState[1] = nextStateId;
	}

	public static clearDownloadedTravelMemoryData(): void {
		if (this.travelMemoryPhoto != null) {
			this.travelMemoryPhoto.dispose();
			this.travelMemoryPhoto = null;
		}

		for (let i = 0; i < 2; ++i) {
			this.travelMemoryTexts[i] = null;
		}

		// System.gc();
	}

	public static async travelMemoryFlow(): Promise<void> {
		this.travelMemoryState[2]++;
		switch (this.travelMemoryState[1]) {
			case 0:
				this.nextTravelMemoryState(1);
				break;
			case 1:
				this.travelMemoryCodeInputFlow();
				break;
			case 2:
				await this.downloadTravelMemoryData();
				break;
			case 3:
				this.displayMemoryPhotoFlow();
				break;
			case 4:
				this.travelMemoryNoopFlow();
		}
	}

	public static travelMemoryCodeInputFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(145, 6);
		} else {
			if (this.travelMemoryState[0] != 0) {
				if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
					this.nextTravelMemoryState(2);
				} else if (!this.isKeyPressed(KEY_SELECT)) {
					if (this.isKeyPressed(KEY_UP_LEFT)) {
						this.setCursorIndex(9);
						this.travelMemoryState[0] = 0;
					} else if (this.isKeyPressed(KEY_DOWN_RIGHT)) {
						this.setCursorIndex(0);
						this.travelMemoryState[0] = 0;
					}
				}
			} else if (
				this.handleCodeInput(
					this.isKeyPressed(KEY_LEFT),
					this.isKeyPressed(KEY_RIGHT),
					this.isKeyPressed(KEY_UP),
					this.isKeyPressed(KEY_DOWN),
					this.isKeyPressed(KEY_SELECT),
					this.getPressedNumber()
				)
			) {
				this.travelMemoryState[0] = 1;
			}

			this.setSelectedButtonIndex(this.travelMemoryState[0]);
		}
	}

	public static downloadTravelMemoryData(): void {
		// TODO
	}

	public static prepareTravelMemorySentData(): void {
		this.setFirstByteSentToServer();
		let digit = 4;
		if (this.travelMemoryDebug) {
			digit = 9;
		}

		this.setByteSentToServer(1, digit);
		this.setByteSentToServer(2, 4);

		for (let i = 0; i < 10; ++i) {
			// Copy input digits
			this.setByteSentToServer(3 + i, this.getDigitBankA(i));
		}
	}

	public static displayMemoryPhotoFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(151, 7);
		} else {
			if (6 < this.travelMemoryState[2]) {
				switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
					case 0:
						this.goToPage(PAGE_TITLE);
						break;
					case 1:
						this.launchCurrentApp('http://tamapark.gs.keitaiarchive.org/cgi-bin/album.cgi?uid=NULLGWDOCOMO&op=latest');
				}
			}
		}
	}

	public static travelMemoryNoopFlow(): void {}

	public static travelMemoryPage(g: Graphics, x: number, y: number): void {
		switch (this.travelMemoryState[1]) {
			case 0:
			default:
				break;
			case 1:
				this.travelMemoryCodeInput(g, x, y);
				break;
			case 2:
				this.printingPhoto(g, x, y);
				break;
			case 3:
				this.drawMemoryPhoto(g, x, y);
				break;
			case 4:
				this.travelMemoryNoop(g, x, y);
		}
	}

	public static travelMemoryCodeInput(g: Graphics, x: number, y: number): void {
		// 36: Let's look at travel memories from your trips!
		const textHeight = this.calculateTextHeight(this.getText(36));
		const codeInputY = y + 2 + textHeight + 2 + this.currentFontHeight + 3;

		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 36: Let's look at travel memories from your trips!
			this.drawTextWithBackground(g, this.getText(36), this.canvasWidth / 2, y + 2, 230, 2);
			if (this.travelMemoryDebug) {
				this.setColorOfRGBInt(g, 16777215);
				g.fillRect(x, y + 240 - 4, 4, 4);
			}
		} else {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y + 240 - 42, 240, 193);
		}

		if (this.fullDraw) {
			this.drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 2210832, 10873360, 7786961);
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, codeInputY + 4 + 2, 56, this.travelMemoryState[2] >> 1, true);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, codeInputY + 4 + 2, 56, this.travelMemoryState[2] >> 1, true, 10873360);
		}

		const showCursor = 0 == this.getSelectedButtonIndex() && (this.travelMemoryState[2] & 4) != 0;
		this.drawCodeInput(g, this.canvasWidth / 2, codeInputY + 4, 62, showCursor, this.fullDraw);
		// 71: Enter the Travel No. shown on your Keitama
		this.drawFullWidthScrollingText(g, x, this.getText(71), y + 2 + textHeight + 2, this.travelMemoryState[2], 12, 16056665, 16777215);
		// 18: OK
		this.drawTextButton(g, 1, this.getText(18), this.canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
		if (1 == this.getSelectedButtonIndex()) {
			this.drawMirroredTamagotchiPair(g, 35, this.canvasWidth / 2, y + 240 - 42 + 14 + 1, 100, this.travelMemoryState[2]);
		}
	}

	public static printingPhoto(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		const newY = y + 4;
		// 37: Printing Photo
		this.drawTextWithBackground(g, this.getText(37), this.canvasWidth / 2, y + 2, 200, 2);
		this.setColorOfRGBInt(g, 16770939);
		g.fillRect(x, y + 46, 240, 108);
		// Photo house
		this.drawSprite(g, 60, this.canvasWidth / 2, y + 46, 2);
		const stepX = 16;
		const radius = 6;
		const circleCount = 11;
		const width = radius + stepX * (circleCount - 1);
		this.drawRainbowCircles(g, (this.canvasWidth - width) / 2, y + 46 + 108 + 8, stepX, 0, radius, 0, circleCount);
	}

	public static drawMemoryPhoto(g: Graphics, x: number, y: number): void {
		const fontHeight = this.currentFontHeight;
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);

		let stringWidth = this.currentFont.stringWidth(this.travelMemoryTexts[0]) + 8;
		if (stringWidth < this.travelMemoryPhoto!.getWidth()) {
			stringWidth = this.travelMemoryPhoto!.getWidth();
		}

		this.drawTextWithBackground(g, this.travelMemoryTexts[0]!, this.canvasWidth / 2, y + 2, stringWidth, 2);
		stringWidth = this.currentFontHeight;
		this.drawImage(g, this.travelMemoryPhoto, this.canvasWidth / 2, y + 2 + fontHeight + 4 + 4, 2);
		this.drawFullWidthScrollingText(g, x, this.travelMemoryTexts[1]!, y + 2 + fontHeight + 4 + 4 + this.travelMemoryPhoto!.getHeight() + 7, this.travelMemoryState[2], 12, 16056665, 16777215);

		for (let i = 0; i < 2; ++i) {
			this.drawLayoutTextButton(g, i, this.memoryPhotoLayout, 0);
		}

		this.drawMirroredTamagotchiPair(
			g,
			35,
			x + this.getValueFrom6Table(this.memoryPhotoLayout, this.getSelectedButtonIndex(), 0),
			y + this.getValueFrom6Table(this.memoryPhotoLayout, this.getSelectedButtonIndex(), 1) + this.getValueFrom6Table(this.memoryPhotoLayout, this.getSelectedButtonIndex(), 3) / 2 + 1,
			this.getValueFrom6Table(this.memoryPhotoLayout, this.getSelectedButtonIndex(), 2),
			this.travelMemoryState[2]
		);
	}

	public static travelMemoryNoop(g: Graphics, x: number, y: number): void {}

	public static getRegionSelectLayout(column: number, row: number): number {
		return this.regionSelectLayout[column * 8 + row];
	}

	public static resetExchangePlazaState(): void {
		this.clearCodeInput();
		this.setButtonConfig(2, true);
		this.setSelectedButtonIndex(this.exchangePlazaState[0]);
		this.nextExchangePlazaState(0);
	}

	public static nextExchangePlazaState(stateId: number): void {
		switch (stateId) {
			case 0:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				break;
			case 1:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.exchangePlazaState[0] = 0;
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 3:
				this.exchangePlazaState[3] = 0;
				this.selectSoftLabel(SOFT_LABEL_MENU);
				break;
			case 4:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 5:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 6:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 7:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
				break;
			case 8:
				this.startSendingViaIr(this.currentPage, 181, 2, 30);
		}

		this.fullDrawOnNextPaint = true;
		this.exchangePlazaState[2] = 0;
		this.exchangePlazaState[1] = stateId;
	}

	public static clearDownloadedExchangePlazaData(): void {
		for (let i = 0; i < 2; ++i) {
			if (this.exchangePlazaImages[i] != null) {
				this.exchangePlazaImages[i]?.dispose();
				this.exchangePlazaImages[i] = null;
			}
		}

		for (let i = 0; i < 3; ++i) {
			this.exchangePlazaTexts[i] = null;
		}

		// System.gc();
	}

	public static async exchangePlazaFlow(): Promise<void> {
		this.exchangePlazaState[2]++;
		switch (this.exchangePlazaState[1]) {
			case 0:
				this.nextExchangePlazaState(1);
				break;
			case 1:
				this.exchangePlazaCodeInputFlow();
				break;
			case 2:
				this.exchangePlazaLoadingScreenFlow();
				break;
			case 3:
				this.exchangePlazaRegionSelectFlow();
				break;
			case 4:
				await this.downloadExchangePlazaData();
				break;
			case 5:
				this.exchangePlazaExchangeFailedFlow();
				break;
			case 6:
				this.exchangePlazaExchangeSuccessFlow();
				break;
			case 7:
				this.exchangePlazaExchangeTicketFlow();
				break;
			case 8:
				this.exchangePlazaSendViaIrFlow();
		}
	}

	public static exchangePlazaCodeInputFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(158, 6);
		} else {
			if (this.exchangePlazaState[0] != 0) {
				if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
					this.nextExchangePlazaState(2);
				} else if (!this.isKeyPressed(KEY_SELECT)) {
					if (this.isKeyPressed(KEY_UP_LEFT)) {
						this.setCursorIndex(9);
						this.exchangePlazaState[0] = 0;
					} else if (this.isKeyPressed(KEY_DOWN_RIGHT)) {
						this.setCursorIndex(0);
						this.exchangePlazaState[0] = 0;
					}
				}
			} else if (
				this.handleCodeInput(
					this.isKeyPressed(KEY_LEFT),
					this.isKeyPressed(KEY_RIGHT),
					this.isKeyPressed(KEY_UP),
					this.isKeyPressed(KEY_DOWN),
					this.isKeyPressed(KEY_SELECT),
					this.getPressedNumber()
				)
			) {
				this.exchangePlazaState[0] = 1;
			}

			this.setSelectedButtonIndex(this.exchangePlazaState[0]);
		}
	}

	public static exchangePlazaLoadingScreenFlow(): void {
		if (this.exchangePlazaState[2] > 10) {
			this.playSound(6, false);
			this.nextExchangePlazaState(3);
		}
	}

	public static exchangePlazaRegionSelectFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(164, 2);
		} else {
			this.exchangePlazaState[4] = this.exchangePlazaState[3];
			if (this.isKeyPressed(KEY_LEFT)) {
				this.playSound(4, false);
				this.exchangePlazaState[3] = this.getRegionSelectLayout(this.exchangePlazaState[3], 2);
			} else if (this.isKeyPressed(KEY_UP)) {
				this.playSound(4, false);
				this.exchangePlazaState[3] = this.getRegionSelectLayout(this.exchangePlazaState[3], 3);
			} else if (this.isKeyPressed(KEY_RIGHT)) {
				this.playSound(4, false);
				this.exchangePlazaState[3] = this.getRegionSelectLayout(this.exchangePlazaState[3], 4);
			} else if (this.isKeyPressed(KEY_DOWN)) {
				this.playSound(4, false);
				this.exchangePlazaState[3] = this.getRegionSelectLayout(this.exchangePlazaState[3], 5);
			} else if (this.isKeyPressed(KEY_SELECT)) {
				this.playSound(5, false);
				this.nextExchangePlazaState(4);
			}
		}
	}

	public static async downloadExchangePlazaData(): Promise<void> {
		// TODO
	}

	public static prepareExchangePlazaSentData(): void {
		this.setFirstByteSentToServer();
		this.setByteSentToServer(1, 5);
		this.setByteSentToServer(2, 3);

		for (let i = 0; i < 10; ++i) {
			this.setByteSentToServer(3 + i, this.getDigitBankA(i));
		}

		this.setByteSentToServer(13, this.exchangePlazaState[3] + 1);
	}

	public static exchangePlazaExchangeFailedFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(167, 1);
		} else {
			if (6 < this.exchangePlazaState[2] && this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT)) != -1) {
				this.nextExchangePlazaState(3);
			}
		}
	}

	public static exchangePlazaExchangeSuccessFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(166, 1);
		} else {
			if (6 < this.exchangePlazaState[2] && this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT)) != -1) {
				this.nextExchangePlazaState(7);
			}
		}
	}

	public static exchangePlazaExchangeTicketFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(168, 7);
		} else {
			if (6 < this.exchangePlazaState[2]) {
				switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
					case 0:
						this.nextExchangePlazaState(8);
						break;
					case 1:
						this.goToPage(PAGE_TITLE);
				}
			}
		}
	}

	public static exchangePlazaSendViaIrFlow(): void {
		if (!this.hasStartedSendingViaIr()) {
			this.nextExchangePlazaState(7); // Go back
		} else {
			this.sendViaIrFlow();
		}
	}

	public static exchangePlazaPage(g: Graphics, x: number, y: number): void {
		switch (this.exchangePlazaState[1]) {
			case 0:
			default:
				break;
			case 1:
				this.exchangePlazaCodeInput(g, x, y);
				break;
			case 2:
				this.exchangePlazaLoadingScreen(g, x, y);
				break;
			case 3:
				this.exchangePlazaRegionSelect(g, x, y);
				break;
			case 4:
				this.exchangePlazaLoadingShip(g, x, y);
				break;
			case 5:
				this.exchangePlazaExchangeFailed(g, x, y);
				break;
			case 6:
				this.exchangePlazaExchangeSuccess(g, x, y);
				break;
			case 7:
				this.exchangePlazaExchangeTicket(g, x, y);
				break;
			case 8:
				this.exchangePlazaSendViaIR(g, x, y);
		}
	}

	public static exchangePlazaCodeInput(g: Graphics, x: number, y: number): void {
		// 41: Trade the regional specialty items you've collected!
		const textHeight = this.calculateTextHeight(this.getText(41));
		const codeInputY = y + 2 + textHeight + 2 + this.currentFontHeight + 3;

		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// 41: Trade the regional specialty items you've collected!
			this.drawTextWithBackground(g, this.getText(41), this.canvasWidth / 2, y + 2, 230, 2);
		} else {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y + 240 - 52, 240, 188);
		}

		if (this.fullDraw) {
			this.drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 3429838, 6728678, 7786961);
			this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, codeInputY + 4 + 2, 56, this.exchangePlazaState[2] >> 1, true);
		} else {
			this.drawDownloadUploadAnimationsWithBackground(g, this.canvasWidth / 2, codeInputY + 4 + 2, 56, this.exchangePlazaState[2] >> 1, true, 6728678);
		}

		// 72: Enter the Exchange No. shown on your Keitama
		this.drawFullWidthScrollingText(g, x, this.getText(72), y + 2 + textHeight + 2, this.exchangePlazaState[2], 12, 16056665, 16777215);
		const showCursor = 0 == this.getSelectedButtonIndex() && (this.exchangePlazaState[2] & 4) != 0;
		this.drawCodeInput(g, this.canvasWidth / 2, codeInputY + 4, 62, showCursor, this.fullDraw);
		// 18: OK
		this.drawTextButton(g, 1, this.getText(18), this.canvasWidth / 2, y + 240 - 42, 100, 28, 2, 0);
		if (1 == this.getSelectedButtonIndex()) {
			this.drawMirroredTamagotchiPair(g, 30, this.canvasWidth / 2, y + 240 - 42 + 7 + 1, 100, this.exchangePlazaState[2]);
		}
	}

	public static exchangePlazaLoadingScreen(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 73: To Exchange Plaza!
		this.drawTextWithBackground(g, this.getText(73), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(73)) + 8, 2);
		// Tama park card
		this.drawSprite(g, 59, this.canvasWidth / 2, y + 50, 2);
		const width = 176;
		this.drawRainbowCircles(g, (this.canvasWidth - width) / 2, y + 160, 16, 0, 6, this.exchangePlazaState[2], 11);
	}

	public static exchangePlazaRegionSelect(g: Graphics, x: number, y: number): void {
		const selectorX = x + 168;
		const selectorY = y + 76;
		if (this.fullDraw) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// Tama park banner
			this.drawSprite(g, 71, x, y + 240 - this.getSpriteHeight(71), 0);
			// 6: Exchange Plaza
			this.drawTextWithBackground(g, this.getText(6), this.canvasWidth / 2, y + 3, this.currentFont.stringWidth(this.getText(6)) + 8, 2);
			// 74: Choose a region to trade with and press OK!
			this.drawFullWidthScrollingText(g, x, this.getText(74), y + 3 + this.calculateTextHeight(this.getText(6)) + 3, this.exchangePlazaState[2], 12, 16056665, 16777215);

			this.drawTextWithBackground(g, this.getText(this.getRegionSelectLayout(this.exchangePlazaState[3], 6)), x + 3, selectorY, 110, 0);

			for (let column = 0; column < 7; ++column) {
				let spriteIndex = this.getRegionSelectLayout(column, 7);
				if (this.exchangePlazaState[3] == column && (this.exchangePlazaState[2] & 4) != 0) {
					++spriteIndex;
				}

				this.drawSprite(g, spriteIndex, selectorX + this.getRegionSelectLayout(column, 0), selectorY + this.getRegionSelectLayout(column, 1), 0);
			}
		} else {
			this.drawFullWidthScrollingText(g, x, this.getText(74), y + 3 + this.calculateTextHeight(this.getText(6)) + 3, this.exchangePlazaState[2], 12, 16056665, 16777215);
			if (this.exchangePlazaState[4] != this.exchangePlazaState[3]) {
				this.drawTextWithBackground(g, this.getText(this.getRegionSelectLayout(this.exchangePlazaState[3], 6)), x + 3, selectorY, 110, 0);
				this.drawSprite(
					g,
					this.getRegionSelectLayout(this.exchangePlazaState[4], 7),
					selectorX + this.getRegionSelectLayout(this.exchangePlazaState[4], 0),
					selectorY + this.getRegionSelectLayout(this.exchangePlazaState[4], 1),
					0
				);
				let spriteIndex = this.getRegionSelectLayout(this.exchangePlazaState[3], 7);
				if ((this.exchangePlazaState[2] & 4) != 0) {
					++spriteIndex;
				}

				this.drawSprite(g, spriteIndex, selectorX + this.getRegionSelectLayout(this.exchangePlazaState[3], 0), selectorY + this.getRegionSelectLayout(this.exchangePlazaState[3], 1), 0);
			} else if ((this.exchangePlazaState[2] & 3) == 0) {
				let spriteIndex = this.getRegionSelectLayout(this.exchangePlazaState[3], 7);
				if ((this.exchangePlazaState[2] & 4) != 0) {
					++spriteIndex;
				}

				this.drawSprite(g, spriteIndex, selectorX + this.getRegionSelectLayout(this.exchangePlazaState[3], 0), selectorY + this.getRegionSelectLayout(this.exchangePlazaState[3], 1), 0);
			}
		}
	}

	public static exchangePlazaLoadingShip(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		this.setColorOfRGBInt(g, 6728679);
		g.fillRect(x, y + 114, 240, this.getSpriteHeight(6));
		// Background city
		this.drawSprite(g, 6, x, y + 114, 0);
		this.drawSprite(g, 6, x + 120, y + 114, 0);
		// 43: Landing ship...
		this.drawTextWithBackground(g, this.getText(43), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(43)) + 8, 2);
		// Ufo
		this.drawSprite(g, 76, this.canvasWidth / 2, y + 102, 2);
	}

	public static exchangePlazaExchangeFailed(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 89: Exchanged Failed
		this.drawTextWithBackground(g, this.getText(89), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(89)) + 8, 2);
		const backgroundY = y + 62;
		// Dotted background
		this.drawSprite(g, 4, x + 0, backgroundY, 0);
		this.drawSprite(g, 4, x + 120, backgroundY, 0);
		this.drawImage(g, this.exchangePlazaImages[0], this.canvasWidth / 2, backgroundY - 12, 2);
		const messageBoxHeight = (this.currentFontHeight + 1) * 2 + this.currentFontHeight;
		const messageBoxY = y + 240 - (messageBoxHeight + 4) - 2;
		// 88: Retry
		this.drawTextButton(g, 0, this.getText(88), this.canvasWidth / 2, messageBoxY - 44, 100, 28, 2, 0);
		this.drawBeveledRect(g, (this.canvasWidth - 232) / 2, messageBoxY, 232, messageBoxHeight + 4, 16056665, 16056665);
		this.setColorOfRGBInt(g, 16777215);
		// 90: No trading partners were found in that region.
		this.drawString(g, this.getText(90), this.canvasWidth / 2, messageBoxY + 2, ALIGN_CENTER);
		this.drawMirroredTamagotchiPair(g, 30, this.canvasWidth / 2, messageBoxY - 44 + this.getSpriteHeight(30) / 2, 100, this.exchangePlazaState[2]);
	}

	public static exchangePlazaExchangeSuccess(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);
		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
		// 48: Exchange Success!
		this.drawTextWithBackground(g, this.getText(48), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(48)) + 8, 2);
		this.drawImage(g, this.exchangePlazaImages[0], x + 0, y + 42, 0);
		this.drawImage(g, this.exchangePlazaImages[1], x + 120, y + 42, 0);
		const messageBoxHeight = (this.currentFontHeight + 1) * 2 + this.currentFontHeight;
		const messageBoxY = y + 240 - (messageBoxHeight + 4) - 2;
		// 18: OK
		this.drawTextButton(g, 0, this.getText(18), this.canvasWidth / 2, messageBoxY - 38, 100, 28, 2, 0);
		this.drawBeveledRect(g, (this.canvasWidth - 232) / 2, messageBoxY, 232, messageBoxHeight + 4, 16056665, 16056665);
		this.setColorOfRGBInt(g, 16777215);
		this.drawString(g, this.exchangePlazaTexts[0]!, this.canvasWidth / 2, messageBoxY + 2, ALIGN_CENTER);
		this.drawString(g, this.exchangePlazaTexts[1]!, this.canvasWidth / 2, messageBoxY + 2 + this.currentFontHeight + 1, ALIGN_CENTER);
		this.drawString(g, this.exchangePlazaTexts[2]!, this.canvasWidth / 2, messageBoxY + 2 + (this.currentFontHeight + 1) * 2, ALIGN_CENTER);
		this.drawMirroredTamagotchiPair(g, 30, this.canvasWidth / 2, messageBoxY - 38 + this.getSpriteHeight(30) / 2, 100, this.exchangePlazaState[2]);
	}

	public static exchangePlazaExchangeTicket(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);

		// 51: Exchange Ticket
		const textHeight = this.calculateTextHeight(this.getText(51));
		this.drawTextWithBackground(g, this.getText(51), this.canvasWidth / 2, y + 2, this.currentFont.stringWidth(this.getText(51)) + 8, 2);

		// 91: Enter the Exchange Ticket No. in your Keitama!
		this.drawFullWidthScrollingText(g, x, this.getText(91), y + 2 + textHeight + 2, this.exchangePlazaState[2], 12, 16056665, 16777215);

		for (let i = 0; i < 2; ++i) {
			this.drawLayoutTextButton(g, i, this.exchangeTicketLayout, 0);
		}

		const codeInputY = y + 3 + textHeight + 3 + this.currentFontHeight + 12;
		this.drawCodeInputBackgroundWithHorizontalLine(g, x, codeInputY, 0, 16777215, 16730112, 16751616, 7786961);
		this.drawCodeInput(g, this.canvasWidth / 2, codeInputY + 4, 62, false, true);
		this.drawDownloadUploadAnimations(g, this.canvasWidth / 2, codeInputY + 10, 56, this.exchangePlazaState[2] >> 1, false);
		this.drawMirroredTamagotchiPair(
			g,
			30,
			x + this.getValueFrom6Table(this.exchangeTicketLayout, this.getSelectedButtonIndex(), 0),
			y + this.getValueFrom6Table(this.exchangeTicketLayout, this.getSelectedButtonIndex(), 1) + this.getValueFrom6Table(this.exchangeTicketLayout, this.getSelectedButtonIndex(), 3) / 2 + 1,
			this.getValueFrom6Table(this.exchangeTicketLayout, this.getSelectedButtonIndex(), 2),
			this.exchangePlazaState[2]
		);
	}

	public static exchangePlazaSendViaIR(g: Graphics, x: number, y: number): void {
		this.sendViaIR(g, x, y);
	}

	public static openExplanation(index: number, numberOfPages: number): void {
		this.explanationState[0] = index;
		this.explanationState[1] = numberOfPages;
		this.explanationState[2] = 0;
		this.explanationState[3] = 1;
		this.selectSoftLabel(SOFT_LABEL_BACK);
	}

	public static closeExplanation(): void {
		this.explanationState[3] = 0;
	}

	public static scrollExplanation(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.closeExplanation();
		} else {
			if (this.isKeyPressed(KEY_DOWN_RIGHT_SELECT)) {
				this.explanationState[2]++;
				if (this.explanationState[1] <= this.explanationState[2]) {
					this.closeExplanation();
				}
			} else if (this.isKeyPressed(KEY_UP_LEFT)) {
				this.explanationState[2]--;
				if (this.explanationState[2] < 0) {
					this.explanationState[2] = 0;
				}
			}
		}
	}

	public static isExplanationOpen(): boolean {
		return this.explanationState[3] != 0;
	}

	public static explanationPage(g: Graphics, x: number, y: number): void {
		if (!this.isExplanationOpen()) return;

		this.setColorOfRGBInt(g, 16763955);
		g.fillRect(x, y, 240, 240);

		// Bottom decoration
		this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);

		const lineX = x + 4;
		let lineY = y + (240 - (this.currentFontHeight + 1) * 7) / 2;

		this.drawBeveledRect(g, x + 2, lineY - 1, 236, (this.currentFontHeight + 1) * 7 + 2, 16056665, 16056665);

		const text = this.getText(this.explanationState[0] + this.explanationState[2]);
		const lineCount = this.splitCount(text, '\n');

		this.setColorOfRGBInt(g, 16777215);

		for (let i = 0; i < lineCount; ++i) {
			let line = this.substringBetweenDelimiters(text, i, 1, '\n');
			let colorIndex = line.indexOf('$');

			if (colorIndex == -1) {
				this.drawString(g, line, lineX, lineY, ALIGN_LEFT);
			} else {
				let currentLineX = lineX;

				do {
					const linePart = line.substring(0, colorIndex);
					this.drawString(g, linePart, currentLineX, lineY, ALIGN_LEFT);

					currentLineX += this.currentFont.stringWidth(linePart);
					if (line.length - 1 <= colorIndex) {
						break;
					}

					const baseColor = 0;
					let parsedColor = baseColor | (parseInt(line.substring(colorIndex + 1, colorIndex + 1 + 3)) << 16);
					parsedColor |= parseInt(line.substring(colorIndex + 4, colorIndex + 4 + 3)) << 8;
					parsedColor |= parseInt(line.substring(colorIndex + 7, colorIndex + 7 + 3));

					this.setColorOfRGBInt(g, parsedColor);

					line = line.substring(colorIndex + 10);
					colorIndex = line.indexOf('$');
				} while (colorIndex != -1);

				this.drawString(g, line, currentLineX, lineY, ALIGN_LEFT);
			}

			lineY += this.currentFontHeight + 1;
		}
	}

	public static openMenu(): void {
		this.menuState[6] = this.getSelectedButtonIndex();
		this.menuState[7] = this.getCurrentNumberOfButtons();
		this.menuState[8] = this.getCanButtonsLoopAround();
		this.setButtonConfig(4, true);
		this.setSelectedButtonIndex(0);
		this.setCurrentExplanation(0, -1);
		this.menuState[4] = this.getSelectedButtonIndex();
		this.menuState[0] = 1; // isMenuOpen
		this.menuState[5] = 0;
		this.selectSoftLabel(SOFT_LABEL_BACK);
		this.nextMenuState(0);
	}

	public static setCurrentExplanation(index: number, numberOfPages: number): void {
		this.menuState[2] = index;
		this.menuState[3] = numberOfPages;
	}

	public static closeMenu(): void {
		this.fullDrawOnNextPaint = true;
		this.menuState[0] = 0; // isMenuOpen
	}

	public static nextMenuState(nextState: number): void {
		switch (nextState) {
			case 0:
			default:
				break;
			case 1:
				this.setButtonConfig(4, true);
				this.setSelectedButtonIndex(this.menuState[4]);
				break;
			case 2:
				this.openExplanation(this.menuState[2], this.menuState[3]);
				break;
			case 3:
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(1);
				break;
			case 4:
				this.setButtonConfig(2, true);
				this.setSelectedButtonIndex(1);
		}

		this.menuState[5] = 0;
		this.menuState[1] = nextState;
	}

	public static isMenuOpen(): boolean {
		return this.menuState[0] != 0;
	}

	public static async menuFlow(): Promise<void> {
		if (this.isMenuOpen()) {
			this.menuState[5]++;
			switch (this.menuState[1]) {
				case 0:
					this.nextMenuState(1);
					break;
				case 1:
					await this.mainMenuFlow();
					break;
				case 2:
					if (this.isExplanationOpen()) {
						this.scrollExplanation();
					} else {
						this.nextMenuState(1);
					}
					break;
				case 3:
					this.returnToTitlePageFlow();
					break;
				case 4:
					this.closeAppPageFlow();
			}
		}
	}

	public static async mainMenuFlow(): Promise<void> {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.setButtonConfig(this.menuState[7], this.menuState[8] != 0);
			this.setSelectedButtonIndex(this.menuState[6]);
			this.selectSoftLabel(this.currentSoftLabelIdx);
			this.closeMenu();
		} else {
			this.menuState[4] = this.getSelectedButtonIndex();
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
				case 0:
					if (0 < this.menuState[3]) {
						this.nextMenuState(2);
					}
					break;
				case 1:
					this.goToPage(PAGE_TITLE);
					break;
				case 2:
					this.nextMenuState(4);
					break;
				case 3:
					this.toggleSound();
					await this.saveGame();
			}
		}
	}

	public static returnToTitlePageFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.nextMenuState(1);
		} else {
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP_LEFT), this.isKeyPressed(KEY_DOWN_RIGHT))) {
				case 0:
					this.closeMenu();
					this.goToPage(PAGE_TITLE);
					break;
				case 1:
					this.nextMenuState(1);
			}
		}
	}

	public static closeAppPageFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.nextMenuState(1);
		} else {
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP_LEFT), this.isKeyPressed(KEY_DOWN_RIGHT))) {
				case 0:
					this.exitGame();
					break;
				case 1:
					this.nextMenuState(1);
			}
		}
	}

	public static drawMenuPages(g: Graphics, x: number, y: number): void {
		if (this.isMenuOpen()) {
			switch (this.menuState[1]) {
				case 0:
				default:
					break;
				case 1:
					this.mainMenu(g, x, y);
					break;
				case 2:
					if (this.isExplanationOpen()) {
						this.explanationPage(g, x, y);
					}
					break;
				case 3:
					this.returnToTitlePage(g, x, y);
					break;
				case 4:
					this.closeAppPage(g, x, y);
			}
		}
	}

	public static mainMenu(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		const newX = x + 120;
		const newY = y + 5;
		g.fillRect(x, y, 240, 240);
		// 54: Menu
		this.drawTextWithBackground(g, this.getText(54), newX, newY, this.currentFont.stringWidth(this.getText(54)) + 8, 2);
		// 55: Read Explanation
		this.drawButton(g, this.getText(55), 0, newY + 2 + (this.currentFontHeight + 6) * 2);
		// 56: Return to Title
		this.drawButton(g, this.getText(56), 1, newY + 2 + (this.currentFontHeight + 6) * 3);
		// 57: Close App
		this.drawButton(g, this.getText(57), 2, newY + 2 + (this.currentFontHeight + 6) * 4);
		// 58: Sound  ON
		// 59: Sound  OFF
		this.drawButton(g, this.getText(58 + this.gameSave[3]), 3, newY + 2 + (this.currentFontHeight + 6) * 5);

		this.drawMenuEggs(g, this.canvasWidth / 2, newY + 2 + this.currentFontHeight + 6 + 2, 86, this.menuState[5]);
	}

	public static returnToTitlePage(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		const newX = x + 120;
		const newY = y + 5;
		g.fillRect(x, y, 240, 240);
		// 60: Return to Title?
		this.drawTextWithBackground(g, this.getText(60), newX, newY, this.currentFont.stringWidth(this.getText(60)) + 4, 2);
		// 62: Yes
		this.drawButton(g, this.getText(62), 0, newY + 2 + (this.currentFontHeight + 6) * 2);
		// 63: No
		this.drawButton(g, this.getText(63), 1, newY + 2 + (this.currentFontHeight + 6) * 3);
		this.drawMenuEggs(g, this.canvasWidth / 2, newY + 2 + this.currentFontHeight + 6 + 2, 86, this.menuState[5]);
	}

	public static closeAppPage(g: Graphics, x: number, y: number): void {
		this.setColorOfRGBInt(g, 16763955);
		const newX = x + 120;
		const newY = y + 5;
		g.fillRect(x, y, 240, 240);
		// 61: Close App?
		this.drawTextWithBackground(g, this.getText(61), newX, newY, this.currentFont.stringWidth(this.getText(61)) + 8, 2);
		// 62: Yes
		this.drawButton(g, this.getText(62), 0, newY + 2 + (this.currentFontHeight + 6) * 2);
		// 63: No
		this.drawButton(g, this.getText(63), 1, newY + 2 + (this.currentFontHeight + 6) * 3);
		this.drawMenuEggs(g, this.canvasWidth / 2, newY + 2 + this.currentFontHeight + 6 + 2, 86, this.menuState[5]);
	}

	public static drawButton(g: Graphics, text: string, buttonIdx: number, y: number): void {
		let textColor;
		let backgroundColor;
		if (buttonIdx == this.getSelectedButtonIndex()) {
			backgroundColor = 16056665;
			textColor = 16777215;
		} else {
			backgroundColor = 7786961;
			textColor = 3096512;
		}

		this.drawBeveledRect(g, (this.canvasWidth - 220) / 2, y, 220, this.currentFontHeight + 4, backgroundColor, backgroundColor);
		this.setColorOfRGBInt(g, textColor);
		this.drawString(g, text, this.canvasWidth / 2, y + 2, ALIGN_CENTER);
	}

	public static drawMenuEggs(g: Graphics, x: number, y: number, gap: number, time: number): void {
		let eggX = x - gap;

		for (let i = 0; i < 3; eggX += gap) {
			const spriteIndex = 69 + ((i + (time >> 2)) & 1);
			this.drawSprite(g, spriteIndex, eggX - this.getSpriteWidth(spriteIndex) / 2, y, 0);
			++i;
		}
	}

	public static showErrorPage(pageId: number, actionId1: number, actionId2: number, errorMessage: string): void {
		this.errorState[0] = pageId;
		this.errorState[1] = actionId1;
		this.errorState[2] = actionId2;
		this.errorPageText = errorMessage;
		this.setButtonConfig(2, true);
		this.setSelectedButtonIndex(1);
		this.setButtonTheme2(16777215, 7786961, 16777215, 16777215, 16777215, 6594720, 13158600, 13158600);
		this.selectSoftLabel(SOFT_LABEL_EMPTY);
		this.errorState[3] = 1; // show error page
	}

	public static closeErrorPage(): void {
		this.errorState[3] = 0; // hide error page
		this.errorPageText = null;
	}

	public static openPreviousPageWithFlowStep(flowStep: number): void {
		this.currentPage = this.errorState[0];
		this.closeErrorPage();
		switch (this.errorState[0]) {
			case 7:
				this.nextShoppingCenterState(flowStep);
				break;
			case 8:
				this.nextParentCallState(flowStep);
				break;
			case 9:
				this.nextGotchiKingState(flowStep);
				break;
			case 10:
				this.nextTravelMemoryState(flowStep);
				break;
			case 11:
				this.nextExchangePlazaState(flowStep);
		}
	}

	public static shouldShowErrorPage(): boolean {
		return this.errorState[3] != 0;
	}

	public static errorPageFlow(): void {
		if (this.shouldShowErrorPage()) {
			if (this.isKeyPressed(KEY_DOWN)) {
				this.errorPageUnusedCounter -= (this.currentFontHeight + 1) * 7;
			} else if (this.isKeyPressed(KEY_UP)) {
				this.errorPageUnusedCounter += (this.currentFontHeight + 1) * 7;
			}

			if (this.isKeyPressed(KEY_0)) {
				this.errorPageUnusedToggle = !this.errorPageUnusedToggle;
			}

			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_LEFT), this.isKeyPressed(KEY_RIGHT))) {
				case 0:
					this.openPreviousPageWithFlowStep(this.errorState[1]);
					break;
				case 1:
					this.openPreviousPageWithFlowStep(this.errorState[2]);
			}
		}
	}

	public static errorPage(g: Graphics, x: number, y: number): void {
		if (this.shouldShowErrorPage()) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// Bottom decoration
			this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
			const lineCount = this.splitCount(this.errorPageText!, '\n');
			const textHeight = (lineCount - 1) * (this.currentFontHeight + 1) + this.currentFontHeight + 4;
			const newY = y + (240 - (textHeight + 4 + 8)) / 2;
			this.drawBeveledRect(g, (this.canvasWidth - 232) / 2, newY, 232, textHeight, 16056665, 16056665);
			this.setColorOfRGBInt(g, 16777215);
			this.drawString(g, this.errorPageText!, this.canvasWidth / 2, newY + 2, ALIGN_CENTER);
			// 88: Retry
			this.drawTextButton(g, 0, this.getText(88), this.canvasWidth / 2 - 8, newY + textHeight + 4, 100, 28, 1, 0);
			// 9: Back
			this.drawTextButton(g, 1, this.getText(9), this.canvasWidth / 2 + 8, newY + textHeight + 4, 100, 28, 0, 0);
		}
	}

	public static getText(idx: number): string {
		return this.texts[idx];
	}

	public static async loadTexts(): Promise<boolean> {
		let stream: DataInputStream | null = null;
		let success = true;
		const indexOffset = 100;

		try {
			const lengths = await this.loadShortArray(128);
			let pos = 128 + (lengths.length + 1) * 2;

			for (let i = 0; i < indexOffset; ++i) {
				pos += lengths[i];
			}

			stream = await Connector.openDataInputStream('scratchpad:///0;pos=' + pos);

			for (let i = 0; i < 183; ++i) {
				const data = new Uint8Array(lengths[indexOffset + i]);
				await stream.read(data);
				this.texts[i] = stringConstructor(data);
				// data = null;
				// System.gc();
			}
		} catch (e) {
			success = false;
		} finally {
			if (stream != null) {
				try {
					await stream.close();
					stream = null;
					// System.gc();
				} catch (ignored) {}
			}
		}

		return success;
	}

	public static setButtonConfig(numberOfButtons: number, canLoopAround: boolean): void {
		this.buttonState[1] = numberOfButtons;
		this.buttonState[2] = canLoopAround ? 1 : 0;
		this.buttonState[13] = 0; // not pressed
	}

	public static setSelectedButtonIndex(index: number): void {
		this.buttonState[0] = index;
	}

	public static setButtonTheme(
		selectedOutlineColor: number,
		selectedColor: number,
		unused1: number,
		selectedTextColor: number,
		selectedShadowColor: number,
		outlineColor: number,
		color: number,
		unused2: number,
		textColor: number,
		shadowColor: number
	): void {
		this.buttonState[3] = selectedOutlineColor;
		this.buttonState[4] = selectedColor;
		this.buttonState[5] = unused1;
		this.buttonState[6] = selectedTextColor;
		this.buttonState[7] = selectedShadowColor;
		this.buttonState[8] = outlineColor;
		this.buttonState[9] = color;
		this.buttonState[10] = unused2;
		this.buttonState[11] = textColor;
		this.buttonState[12] = shadowColor;
	}

	public static setButtonTheme2(
		selectedOutlineColor: number,
		selectedColor: number,
		unused1: number,
		selectedTextColor: number,
		outlineColor: number,
		color: number,
		unused2: number,
		textColor: number
	): void {
		this.setButtonTheme(selectedOutlineColor, selectedColor, unused1, selectedTextColor, 0, outlineColor, color, unused2, textColor, 0);
	}

	public static getPressedButtonIndex(pressButton: boolean, decrementButtonIndex: boolean, incrementButtonIndex: boolean): number {
		let selected = -1;
		if (this.buttonState[13] != 0) {
			selected = this.getSelectedButtonIndex();
			this.buttonState[13] = 0; // not pressed
			this.playSound(5, false);
		} else if (pressButton) {
			this.buttonState[13] = 1; // pressed
			this.fullDrawOnNextPaint = true;
		} else {
			let buttonIndexChanged = false;

			if (decrementButtonIndex) {
				buttonIndexChanged = true;
				this.buttonState[0]--;
				if (this.buttonState[0] < 0) {
					if (this.buttonState[2] != 0) {
						// selectedButtonIdx = numberOfButtons - 1
						this.buttonState[0] = this.buttonState[1] - 1;
					} else {
						buttonIndexChanged = false;
						this.buttonState[0] = 0;
					}
				}
			}

			if (incrementButtonIndex) {
				buttonIndexChanged = true;
				this.buttonState[0]++;
				if (this.buttonState[1] <= this.buttonState[0]) {
					if (this.buttonState[2] != 0) {
						this.buttonState[0] = 0;
					} else {
						buttonIndexChanged = false;
						this.buttonState[0] = this.buttonState[1] - 1;
					}
				}
			}

			if (buttonIndexChanged) {
				this.playSound(4, false);
			}

			this.buttonState[13] = 0; // not pressed
		}

		return selected;
	}

	public static getSelectedButtonIndex(): number {
		return this.buttonState[0];
	}

	public static getCurrentNumberOfButtons(): number {
		return this.buttonState[1];
	}

	public static getCanButtonsLoopAround(): number {
		return this.buttonState[2];
	}

	public static drawLayoutTextButton(g: Graphics, buttonIndex: number, layout: number[], rounding: number): void {
		this.drawTextButton(
			g,
			buttonIndex,
			this.getText(this.getValueFrom6Table(layout, buttonIndex, 4)),
			this.rootX + this.getValueFrom6Table(layout, buttonIndex, 0),
			this.rootY + this.getValueFrom6Table(layout, buttonIndex, 1),
			this.getValueFrom6Table(layout, buttonIndex, 2),
			this.getValueFrom6Table(layout, buttonIndex, 3),
			this.getValueFrom6Table(layout, buttonIndex, 5),
			rounding
		);
	}

	public static drawLayoutSpriteButton(g: Graphics, buttonIndex: number, layout: number[], rounding: number): void {
		this.drawSpriteButton(
			g,
			buttonIndex,
			this.getValueFrom7Table(layout, buttonIndex, 4),
			this.getValueFrom7Table(layout, buttonIndex, 5),
			this.rootX + this.getValueFrom7Table(layout, buttonIndex, 0),
			this.rootY + this.getValueFrom7Table(layout, buttonIndex, 1),
			this.getValueFrom7Table(layout, buttonIndex, 2),
			this.getValueFrom7Table(layout, buttonIndex, 3),
			this.getValueFrom7Table(layout, buttonIndex, 6),
			rounding
		);
	}

	public static getValueFrom6Table(table: number[], row: number, column: number): number {
		return table[row * 6 + column];
	}

	public static getValueFrom7Table(table: number[], row: number, column: number): number {
		return table[row * 7 + column];
	}

	public static drawTextButton(g: Graphics, buttonIndex: number, text: string, x: number, y: number, width: number, height: number, align: number, rounding: number): void {
		let newX;
		if (align == 2) {
			newX = x - width / 2;
		} else if (align == 0) {
			newX = x;
		} else {
			newX = x - width;
		}

		let isPressed = false;
		let outlineColor;
		let color;
		let unused;
		let textColor;
		let shadowColor;
		if (buttonIndex == this.getSelectedButtonIndex()) {
			outlineColor = this.buttonState[3];
			color = this.buttonState[4];
			unused = this.buttonState[5];
			textColor = this.buttonState[6];
			shadowColor = this.buttonState[7];
			isPressed = this.buttonState[13] != 0;
		} else {
			outlineColor = this.buttonState[8];
			color = this.buttonState[9];
			unused = this.buttonState[10];
			textColor = this.buttonState[11];
			shadowColor = this.buttonState[12];
		}

		switch (rounding) {
			case 0:
				this.drawRoundedTextButton(g, text, newX, y, width, height, outlineColor, color, unused, textColor, shadowColor, isPressed);
				break;
			case 1:
				this.drawRectangularTextButton(g, text, newX, y, width, height, outlineColor, color, unused, textColor, shadowColor, isPressed);
		}
	}

	public static drawSpriteButton(
		g: Graphics,
		buttonIndex: number,
		pressedSprite: number,
		sprite: number,
		x: number,
		y: number,
		width: number,
		height: number,
		align: number,
		rounding: number
	): void {
		let newX;
		if (align == 2) {
			newX = x - width / 2;
		} else if (align == 0) {
			newX = x;
		} else {
			newX = x - width;
		}

		let isHighlighted = false;
		let outlineColor;
		let innerColor;
		let unstyledColor;
		let spriteIndex;
		if (buttonIndex == this.getSelectedButtonIndex()) {
			outlineColor = this.buttonState[3];
			innerColor = this.buttonState[4];
			unstyledColor = this.buttonState[7];
			isHighlighted = this.buttonState[13] != 0;
			spriteIndex = pressedSprite;
		} else {
			outlineColor = this.buttonState[8];
			innerColor = this.buttonState[9];
			unstyledColor = this.buttonState[12];
			spriteIndex = sprite;
		}

		switch (rounding) {
			case 0:
				this.drawRoundedSpriteButton(g, spriteIndex, newX, y, width, height, outlineColor, innerColor, unstyledColor, isHighlighted);
				break;
			case 1:
				this.drawRectangularSpriteButton(g, spriteIndex, newX, y, width, height, outlineColor, innerColor, unstyledColor, isHighlighted);
		}
	}

	public static drawRoundedTextButton(
		g: Graphics,
		text: string,
		x: number,
		y: number,
		width: number,
		height: number,
		outlineColor: number,
		color: number,
		unused: number,
		textColor: number,
		shadowColor: number,
		isPressed: boolean
	): void {
		this.drawRoundedButtonBackground(g, x, y, width, height, outlineColor, color, shadowColor, isPressed);
		if (isPressed) {
			y += 2;
		}

		let textHeight = this.splitCount(text, '\n');
		textHeight = this.currentFontHeight + (textHeight - 1) * (this.currentFontHeight + 1);
		this.setColorOfRGBInt(g, textColor);
		this.drawString(g, text, x + width / 2, y + (height - textHeight) / 2, ALIGN_CENTER);
	}

	public static drawRoundedSpriteButton(
		g: Graphics,
		spriteIndex: number,
		x: number,
		y: number,
		width: number,
		height: number,
		outlineColor: number,
		color: number,
		shadowColor: number,
		isPressed: boolean
	): void {
		this.drawRoundedButtonBackground(g, x, y, width, height, outlineColor, color, shadowColor, isPressed);
		if (isPressed) {
			y += 2;
		}

		this.drawSprite(g, spriteIndex, x + width / 2, y + (height - this.getSpriteHeight(spriteIndex)) / 2, 2);
	}

	public static drawRoundedButtonBackground(g: Graphics, x: number, y: number, width: number, height: number, outlineColor: number, color: number, shadowColor: number, isPressed: boolean): void {
		if (!isPressed) {
			this.setColorOfRGBInt(g, shadowColor);
			g.fillArc(x - 1, y + 2 - 1, height + 2, height + 2, 180, 90);
			g.fillArc(x + width - height - 2, y + 2 - 1, height + 2, height + 2, -90, 90);
			g.fillRect(x + height / 2, y + height + 1, width - height, 2);
		} else {
			y += 2;
		}

		this.setColorOfRGBInt(g, outlineColor);
		g.fillArc(x - 1, y - 1, height + 2, height + 2, 90, 180);
		g.fillArc(x + width - height - 2, y - 1, height + 2, height + 2, -90, 180);
		g.fillRect(x + height / 2, y - 1, width - height, 1);
		g.fillRect(x + height / 2, y + height, width - height, 1);

		this.setColorOfRGBInt(g, color);
		g.fillArc(x, y, height, height, 90, 180);
		g.fillArc(x + width - height - 1, y, height, height, -90, 180);
		g.fillRect(x + height / 2, y, width - height, height);
	}

	public static drawRectangularTextButton(
		g: Graphics,
		text: string,
		x: number,
		y: number,
		width: number,
		height: number,
		borderColor: number,
		color: number,
		unused: number,
		textColor: number,
		shadowColor: number,
		isPressed: boolean
	): void {
		this.drawRectangularButtonBackground(g, x, y, width, height, borderColor, color, shadowColor, isPressed);
		if (isPressed) {
			y += 2;
		}

		let textHeight = this.splitCount(text, '\n');
		textHeight = this.currentFontHeight + (textHeight - 1) * (this.currentFontHeight + 1);
		this.setColorOfRGBInt(g, textColor);
		this.drawString(g, text, x + width / 2, y + (height - textHeight) / 2, ALIGN_CENTER);
	}

	public static drawRectangularSpriteButton(
		g: Graphics,
		spriteIndex: number,
		x: number,
		y: number,
		width: number,
		height: number,
		borderColor: number,
		color: number,
		shadowColor: number,
		isPressed: boolean
	): void {
		this.drawRectangularButtonBackground(g, x, y, width, height, borderColor, color, shadowColor, isPressed);
		if (isPressed) {
			y += 2;
		}

		this.drawSprite(g, spriteIndex, x + width / 2, y + (height - this.getSpriteHeight(spriteIndex)) / 2, 2);
	}

	public static drawRectangularButtonBackground(g: Graphics, x: number, y: number, width: number, height: number, borderColor: number, color: number, shadowColor: number, isPressed: boolean): void {
		if (!isPressed) {
			this.drawBeveledRect(g, x, y + height - 2, width, 4, shadowColor, shadowColor);
		} else {
			y += 2;
		}

		this.drawBeveledRect(g, x, y, width, height, borderColor, color);
	}

	public static clearCodeInput(): void {
		for (let i = 0; i < 4; ++i) {
			this.codeInputState[i] = 0;
		}

		this.setCursorIndex(0);
	}

	public static setCursorIndex(index: number): void {
		this.codeInputState[5] = this.codeInputState[4];
		this.codeInputState[4] = index;
	}

	public static parseAndStoreDownloadedPassword(passwordData: Uint8Array): void {
		for (let i = 0; i < 10; ++i) {
			this.setDigitBankA(i, passwordData[i]);
		}
	}

	public static setDigitBankA(digitIndex: number, digit: number): void {
		digit %= 10;
		const intIndex = 0 + Math.floor(digitIndex / 8);
		let packedInt = this.codeInputState[intIndex];
		const shift = 4 * (7 - (digitIndex % 8));
		packedInt &= ~(15 << shift);
		packedInt |= digit << shift;
		this.codeInputState[intIndex] = packedInt;
	}

	public static setDigitBankB(digitIndex: number, digit: number): void {
		digit %= 10;
		const intIndex = 2 + Math.floor(digitIndex / 8);
		let packedInt = this.codeInputState[intIndex];
		const shift = 4 * (7 - (digitIndex % 8));
		packedInt &= ~(15 << shift);
		packedInt |= digit << shift;
		this.codeInputState[intIndex] = packedInt;
	}

	public static generateDerivedCodeInBankB(): void {
		const keyDigit = this.getDigitBankA(1);

		for (let i = 0; i < 10; ++i) {
			this.setDigitBankB(this.digitShuffleTable[keyDigit * 10 + i], this.getDigitBankA(i));
		}
	}

	public static getDigitBankA(digitIndex: number): number {
		const intIndex = 0 + Math.floor(digitIndex / 8);
		const digit = (this.codeInputState[intIndex] >> (4 * (7 - (digitIndex % 8)))) & 15;
		return digit;
	}

	public static getDigitBankB(digitIndex: number): number {
		const intIndex = 2 + Math.floor(digitIndex / 8);
		const digit = (this.codeInputState[intIndex] >> (4 * (7 - (digitIndex % 8)))) & 15;
		return digit;
	}

	public static getCursorIndex(): number {
		return this.codeInputState[4];
	}

	public static getPreviousCursorIndex(): number {
		return this.codeInputState[5];
	}

	public static handleCodeInput(moveLeft: boolean, moveRight: boolean, moveUp: boolean, moveDown: boolean, incrementDigit: boolean, directDigit: number): boolean {
		let cursorIndex = this.codeInputState[4];
		let allFilled = false;

		if (incrementDigit) {
			this.playSound(5, false);
			this.setDigitBankA(cursorIndex, (this.getDigitBankA(cursorIndex) + 1) % 10);
		} else if (moveRight) {
			++cursorIndex;
			if (10 <= cursorIndex) {
				cursorIndex = 0;
				allFilled = true;
			}

			this.setCursorIndex(cursorIndex);
		} else if (moveLeft) {
			--cursorIndex;
			if (cursorIndex < 0) {
				cursorIndex = 9;
				allFilled = true;
			}

			this.setCursorIndex(cursorIndex);
		} else if (moveUp) {
			cursorIndex -= 5;
			if (cursorIndex < 0) {
				allFilled = true;
				cursorIndex += 10;
			}

			this.setCursorIndex(cursorIndex);
		} else if (moveDown) {
			cursorIndex += 5;
			if (10 <= cursorIndex) {
				allFilled = true;
				cursorIndex -= 10;
			}

			this.setCursorIndex(cursorIndex);
		} else if (0 <= directDigit) {
			this.playSound(5, false);
			this.setDigitBankA(cursorIndex, directDigit);
			++cursorIndex;
			if (10 <= cursorIndex) {
				cursorIndex = 0;
				allFilled = true;
			}

			this.setCursorIndex(cursorIndex);
		}

		return allFilled;
	}

	public static getPressedNumber(): number {
		let number = -1;
		if (this.isKeyPressed(KEY_0)) {
			number = 0;
		} else if (this.isKeyPressed(KEY_1)) {
			number = 1;
		} else if (this.isKeyPressed(KEY_2)) {
			number = 2;
		} else if (this.isKeyPressed(KEY_3)) {
			number = 3;
		} else if (this.isKeyPressed(KEY_4)) {
			number = 4;
		} else if (this.isKeyPressed(KEY_5)) {
			number = 5;
		} else if (this.isKeyPressed(KEY_6)) {
			number = 6;
		} else if (this.isKeyPressed(KEY_7)) {
			number = 7;
		} else if (this.isKeyPressed(KEY_8)) {
			number = 8;
		} else if (this.isKeyPressed(KEY_9)) {
			number = 9;
		}

		return number;
	}

	public static drawAllDigits(g: Graphics, x: number, y: number, align: number): void {
		for (let i = 0; i < 10; ++i) {
			this.drawDigit(g, x, y, align, i);
		}
	}

	public static drawDigit(g: Graphics, x: number, y: number, align: number, index: number): void {
		if (align == 2) {
			x -= 35;
		} else if (align == 1) {
			x -= 71;
		}

		x += index * 15;
		if (5 <= index) {
			y += 26;
			x -= 75;
		}

		this.drawString(g, '' + this.getDigitBankA(index), x, y, ALIGN_LEFT);
	}

	public static drawRainbowCircles(g: Graphics, x: number, y: number, stepX: number, stepY: number, radius: number, startColorOffset: number, circleCount: number): void {
		let colorIndex = startColorOffset % 7;
		if (colorIndex < 0) {
			colorIndex += 7;
		}

		for (let i = 0; i < circleCount; ++i) {
			this.setColorOfRGBInt(g, this.rainbowColors[colorIndex]);
			g.fillArc(x, y, radius * 2, radius * 2, 0, 360);
			++colorIndex;
			colorIndex %= 7;
			x += stepX;
			y += stepY;
		}
	}

	public static drawPixelBitmap(g: Graphics, bitmapData: Uint8Array, headerOffset: number, pixelScale: number, color: number, x: number, y: number, flipHorizontally: boolean): void {
		const pixelWidth = ((bitmapData[headerOffset + 0] & 255) + 7) >>> 3;
		const pixelHeight = bitmapData[headerOffset + 1] & 255;
		this.setColorOfRGB(g, (color >>> 16) & 255, (color >>> 8) & 255, color & 255);
		g.setClip(0, 0, this.canvasWidth + 16, this.canvasHeight + 16);
		g.fillRect(this.canvasWidth, this.canvasHeight, pixelScale, pixelScale);
		let dataIndex = headerOffset + 2;
		let xStep;
		if (flipHorizontally) {
			xStep = -pixelScale;
			x += pixelScale * ((bitmapData[headerOffset + 0] & 255) - 1);
		} else {
			xStep = pixelScale;
		}

		for (let row = 0; row < pixelHeight; ++row) {
			let currentX = x;

			for (let byteInRow = 0; byteInRow < pixelWidth; ++byteInRow) {
				for (let bit = 0; bit < 8; ++bit) {
					if (((bitmapData[dataIndex] >>> (7 - bit)) & 1) != 0) {
						g.fillRect(currentX, y, pixelScale, pixelScale);
					}

					currentX += xStep;
				}

				++dataIndex;
			}

			y += pixelScale;
		}
	}

	public static drawPixelBitmap2(g: Graphics, bitmapData: Uint8Array, headerOffset: number, pixelScale: number, color: number, x: number, y: number, flipHorizontally: boolean): void {
		const pixelWidth = ((bitmapData[headerOffset + 0] & 255) + 7) >>> 3;
		const pixelHeight = bitmapData[headerOffset + 1] & 255;
		this.setColorOfRGB(g, (color >>> 16) & 255, (color >>> 8) & 255, color & 255);
		let dataIndex = headerOffset + 2;
		let xStep;
		if (flipHorizontally) {
			xStep = -pixelScale;
			x += pixelScale * ((bitmapData[headerOffset + 0] & 255) - 1);
		} else {
			xStep = pixelScale;
		}

		for (let row = 0; row < pixelHeight; ++row) {
			let currentX = x;

			for (let byteInRow = 0; byteInRow < pixelWidth; ++byteInRow) {
				for (let bit = 0; bit < 8; ++bit) {
					if (((bitmapData[dataIndex] >>> (7 - bit)) & 1) != 0) {
						g.fillRect(currentX, y, pixelScale, pixelScale);
					}

					currentX += xStep;
				}

				++dataIndex;
			}

			y += pixelScale;
		}
	}

	public static setByteSentToServer(index: number, data: number): void {
		this.bytesSentToServer[index] = data;
	}

	public static setFirstByteSentToServer(): void {
		this.setByteSentToServer(0, 16);
	}

	public static async sendPreparedDataToServer(length: number): Promise<DataInputStream> {
		return await this.sendDataToServer(this.bytesSentToServer, length);
	}

	public static async sendDataToServer(data: Uint8Array, length: number): Promise<DataInputStream> {
		const encodedData = this.urlEncodeData(data, length);
		const url = 'http://tamapark.gs.keitaiarchive.org/cgi-bin/iaserver.cgi?uid=NULLGWDOCOMO&data=' + encodedData;
		this.log('senddata:' + url);

		let input: DataInputStream | null = null;
		let http: HttpConnection | null = null;
		let output: DataOutputStream | null = null;

		try {
			http = Connector.open(url, 1, true);
			http.setRequestMethod('GET');
			await http.connect();
			input = await http.openDataInputStream();
			const buffer = new Uint8Array(1024);
			output = await Connector.openDataOutputStream('scratchpad:///0;pos=85258');

			let bytesToWrite = 0;

			for (let remainingBytes = await http.getLength(); 0 < remainingBytes; remainingBytes -= bytesToWrite) {
				if (remainingBytes < buffer.length) {
					bytesToWrite = remainingBytes;
				} else {
					bytesToWrite = buffer.length;
				}

				this.log('dlsize:' + remainingBytes + ' writeSize:' + bytesToWrite);
				await input.read(buffer, 0, bytesToWrite);
				await output.write(buffer, 0, bytesToWrite);
			}
		} catch (e) {
			throw e;
		} finally {
			try {
				if (output != null) {
					await output.close();
				}
			} catch (ignored) {}

			try {
				if (input != null) {
					await input.close();
				}
			} catch (ignored) {}

			try {
				if (http != null) {
					await http.close();
				}
			} catch (ignored) {}
		}

		return await Connector.openDataInputStream('scratchpad:///0;pos=85258');
	}

	public static urlEncodeData(data: Uint8Array, length: number): string {
		const uppercase = false;

		let outputLength: number;
		if (uppercase) {
			outputLength = length * 2;
		} else {
			outputLength = length * 3;
		}

		const output = new Uint8Array(outputLength);
		let outputIdx = 0;

		for (let i = 0; i < length; ++i) {
			if (!uppercase) {
				output[outputIdx] = 37; // '%'
				++outputIdx;
			}

			const byteValue = data[i] & 0xff;

			for (let nibbleIndex = 1; nibbleIndex >= 0; --nibbleIndex) {
				let hexDigit = (byteValue >> (4 * nibbleIndex)) & 0x0f;

				if (hexDigit >= 10) {
					hexDigit = hexDigit - 10 + 65; // 'A'
				} else {
					hexDigit = hexDigit + 48; // '0'
				}

				output[outputIdx] = hexDigit;
				++outputIdx;
			}
		}

		return stringConstructor(output);
	}

	public static async readString(inputStream: DataInputStream, length: number): Promise<string> {
		const buffer = new Uint8Array(length);

		await inputStream.read(buffer);
		const result = stringConstructor(buffer);
		// inputStream = null;
		// System.gc();
		return result;
	}

	public static async readImage(inputStream: DataInputStream, expectedLength: number): Promise<Image> {
		this.imageReadInfo = 0;
		this.imageReadInfo |= expectedLength;
		const imageData = new Uint8Array(expectedLength);

		let bytesRead;
		for (bytesRead = 0; bytesRead < expectedLength; ++bytesRead) {
			const readValue = await inputStream.readUnsignedByte();
			if (readValue == -1) {
				break;
			}

			imageData[bytesRead] = readValue;
		}

		this.imageReadInfo |= bytesRead << 32;
		const mediaImage = MediaManager.getImage(imageData);
		mediaImage.use();
		const image = mediaImage.getImage();
		// imageData = null;
		// System.gc();
		return image;
	}

	public static countQuotedSegments(text: string): number {
		let insideQuote = false;
		let searchIndex = 0;
		let segmentCount = 0;

		while (true) {
			searchIndex = text.indexOf("'", searchIndex);
			if (searchIndex == -1) {
				if (insideQuote) {
					this.log('Dialogue error: missing closing tag');
				}

				this.log('wordsCnt:' + segmentCount);
				return segmentCount;
			}

			this.log('numindex:' + searchIndex);
			if (insideQuote) {
				++segmentCount;
				insideQuote = false;
			} else {
				insideQuote = true;
			}

			++searchIndex;
		}
	}

	public static findNthQuote(text: string, n: number): string {
		let currentIndex = 0;
		let insideQuote = false;
		let wordStart = 0;
		let searchIndex = 0;

		let result;
		while (true) {
			searchIndex = text.indexOf("'", searchIndex);
			if (searchIndex == -1) {
				result = '';
				break;
			}

			if (insideQuote) {
				if (n == currentIndex) {
					result = text.substring(wordStart, searchIndex);
					break;
				}

				++currentIndex;
				++searchIndex;
				insideQuote = false;
			} else {
				insideQuote = true;
				++searchIndex;
				wordStart = searchIndex;
			}
		}

		return result;
	}

	public static unknownOperationOnServerResponse(inputStream: unknown): void {
		// This is a no-op. Maybe just a decompilation artifact?
		// (public static void aW(DataInputStream var0) throws Exception)
	}

	public static createIrRemoteControlFrames(n: number): IrRemoteControlFrame[] {
		const res = new Array<IrRemoteControlFrame>(n);

		for (let i = 0; i < n; ++i) {
			res[i] = new IrRemoteControlFrame();
		}

		return res;
	}

	public static startSendingViaIr(currentPage: number, var1: number, var2: number, var3: number): void {
		this.irState[2] = currentPage;
		this.irState[3] = var1;
		this.irState[4] = var2;
		this.irState[5] = var3;
		this.nextIrState(1);
	}

	public static nextIrState(state: number): void {
		switch (state) {
			case 0:
				this.irRemoteControl.stop();
				break;
			case 1:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				this.setButtonTheme2(16777215, 7786961, 6594720, 16777215, 16777215, 6594720, 16763955, 13158600);
				break;
			case 2:
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				break;
			case 3:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.setButtonConfig(3, true);
				this.setSelectedButtonIndex(0);
				break;
			case 4:
				this.irRemoteControl.stop();
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
				break;
			case 5:
				this.irRemoteControl.stop();
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
				this.setButtonConfig(1, false);
				this.setSelectedButtonIndex(0);
		}

		this.irState[1] = 0;
		this.irState[0] = state;
	}

	public static prepareDataSentViaIr(): void {
		this.generateDerivedCodeInBankB();
		this.setByteSentViaIr(0, 96);
		this.setByteSentViaIr(1, 8);
		this.setByteSentViaIr(2, 0);
		this.setByteSentViaIr(3, 0);

		for (let i = 0; i < 10; i += 2) {
			const value = (this.reverse4Bits(this.getDigitBankB(i)) << 4) | this.reverse4Bits(this.getDigitBankB(i + 1));
			this.setByteSentViaIr(4 + Math.floor(i / 2), value);
		}

		this.setByteSentViaIr(9, 0);
		this.setByteSentViaIr(10, 0);
		this.setByteSentViaIr(11, 0);
		this.setByteSentViaIr(12, 0);
		this.setByteSentViaIr(13, 0);
		this.setByteSentViaIr(14, 0);

		let checksum = 0;
		for (let i = 0; i < 15; ++i) {
			checksum += this.reverse8Bits(this.bytesToSendViaIr[i]);
		}

		this.setByteSentViaIr(15, this.reverse8Bits(checksum & 255));
	}

	public static setByteSentViaIr(index: number, value: number): void {
		this.bytesToSendViaIr[index] = value;
	}

	public static reverse4Bits(data: number): number {
		return ((data & 1) << 3) | ((data & 2) << 1) | ((data & 4) >> 1) | ((data & 8) >> 3);
	}

	public static reverse8Bits(data: number): number {
		return ((data & 1) << 7) | ((data & 2) << 5) | ((data & 4) << 3) | ((data & 8) << 1) | ((data & 16) >> 1) | ((data & 32) >> 3) | ((data & 64) >> 5) | ((data & 128) >> 7);
	}

	public static hasStartedSendingViaIr(): boolean {
		return this.irState[0] != 0;
	}

	public static sendViaIrFlow(): void {
		if (this.hasStartedSendingViaIr()) {
			this.irState[1]++;
			switch (this.irState[0]) {
				case 1:
					this.sendIrFrames();
					break;
				case 2:
					this.sendViaIrSendingFlow();
					break;
				case 3:
					this.sendViaIrSendingCompleteFlow();
					break;
				case 4:
				case 5:
					this.sendViaIrErrorFlow();
			}
		}
	}

	public static sendIrFrames(): void {
		try {
			this.irRemoteControl.stop();
			this.prepareDataSentViaIr();
			this.irRemoteControl.setCarrier(131, 131);
			this.irRemoteControl.setCode0(0, 470, 730);
			this.irRemoteControl.setCode1(0, 470, 1330);
			this.irFrames[0].setFrameData(this.bytesToSendViaIr, 128);
			this.irFrames[0].setRepeatCount(3);
			this.irFrames[0].setFrameDuration(2500);
			this.irFrames[0].setStartHighDuration(9600);
			this.irFrames[0].setStartLowDuration(2400);
			this.irFrames[0].setStopHighDuration(1200);
			this.irRemoteControl.send(1, this.irFrames);
			this.irSendTimestamp = new Date().getTime();
			this.nextIrState(2);
		} catch (e) {
			this.nextIrState(5);
		}
	}

	public static sendViaIrSendingFlow(): void {
		if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
			this.nextIrState(4);
		} else {
			if (750 < new Date().getTime() - this.irSendTimestamp) {
				this.irRemoteControl.stop();
				this.playSound(6, false);
				this.nextIrState(3);
			}
		}
	}

	public static sendViaIrSendingCompleteFlow(): void {
		if (this.isKeyPressed(KEY_SOFT1)) {
			this.openMenu();
			this.setCurrentExplanation(this.irState[3], this.irState[4]);
		} else {
			switch (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), this.isKeyPressed(KEY_UP), this.isKeyPressed(KEY_DOWN))) {
				case 0:
					if (this.irState[2] == 7) {
						this.nextIrState(0);
						this.nextShoppingCenterState(0);
					} else {
						this.nextIrState(0);
						this.goToPage(PAGE_TITLE);
					}
					break;
				case 1:
					this.nextIrState(0);
					break;
				case 2:
					this.nextIrState(1);
			}
		}
	}

	public static sendViaIrErrorFlow(): void {
		if (this.getPressedButtonIndex(this.isKeyPressed(KEY_SELECT), false, false) != -1) {
			this.nextIrState(0);
		}
	}

	public static sendViaIR(g: Graphics, x: number, y: number): void {
		if (this.hasStartedSendingViaIr()) {
			this.setColorOfRGBInt(g, 16763955);
			g.fillRect(x, y, 240, 240);
			// Bottom decoration
			this.drawSprite(g, 0, x, y + 240 - this.getSpriteHeight(0), 0);
			switch (this.irState[0]) {
				case 2:
					this.sendViaIrSending(g, x, y);
					break;
				case 3:
					this.sendViaIrComplete(g, x, y);
					break;
				case 4:
					this.sendViaIrInterrupted(g, x, y);
					break;
				case 5:
					this.sendViaIrFailed(g, x, y);
			}
		}
	}

	public static sendViaIrSending(g: Graphics, x: number, y: number): void {
		// 98: Sending...
		this.drawTextWithBackground(g, this.getText(98), this.canvasWidth / 2, y + 5, this.currentFont.stringWidth(this.getText(98)) + 8, 2);
		this.drawSprite(g, 67, this.canvasWidth / 2 + (this.irState[1] * 8 - 60), y + 50, 2);
		// 38: End
		this.drawTextButton(g, 0, this.getText(38), this.canvasWidth / 2, y + 50 + this.getSpriteHeight(67) + 10, this.currentFont.stringWidth(this.getText(38)) + 8, this.currentFontHeight + 4, 2, 0);
		// 18: OK
		this.drawMirroredTamagotchiPair(
			g,
			this.irState[5],
			this.canvasWidth / 2,
			y + 50 + this.getSpriteHeight(67) + 10 + (this.currentFontHeight + 4) / 2,
			this.currentFont.stringWidth(this.getText(18)) + 20,
			this.irState[1]
		);
	}

	public static sendViaIrComplete(g: Graphics, x: number, y: number): void {
		// 99: Sending complete!
		this.drawTextWithBackground(g, this.getText(99), this.canvasWidth / 2, y + 5, this.currentFont.stringWidth(this.getText(99)) + 8, 2);
		this.drawSprite(g, 68, this.canvasWidth / 2, y + 50, 2);
		let layout;

		if (this.irState[2] == 7) {
			layout = this.shoppingCenterIrSendLayout;
		} else {
			layout = this.generalIrSendLayout;
		}

		for (let i = 0; i < 3; ++i) {
			this.drawLayoutTextButton(g, i, layout, 0);
		}

		this.drawMirroredTamagotchiPair(
			g,
			this.irState[5],
			x + this.getValueFrom6Table(layout, this.getSelectedButtonIndex(), 0),
			y + this.getValueFrom6Table(layout, this.getSelectedButtonIndex(), 1) + this.getValueFrom6Table(layout, this.getSelectedButtonIndex(), 3) / 2,
			this.getValueFrom6Table(layout, this.getSelectedButtonIndex(), 2) - 10,
			this.irState[1]
		);
	}

	public static sendViaIrInterrupted(g: Graphics, x: number, y: number): void {
		// 96: Interrupted...
		this.drawTextWithBackground(g, this.getText(96), this.canvasWidth / 2, y + 5, this.currentFont.stringWidth(this.getText(96)) + 8, 2);
		this.transmissionErrorPage(g, x, y);
	}

	public static sendViaIrFailed(g: Graphics, x: number, y: number): void {
		// 97: Transmission failed
		this.drawTextWithBackground(g, this.getText(97), this.canvasWidth / 2, y + 5, this.currentFont.stringWidth(this.getText(97)) + 8, 2);
		this.transmissionErrorPage(g, x, y);
	}

	public static transmissionErrorPage(g: Graphics, x: number, y: number): void {
		this.drawSprite(g, 29, this.canvasWidth / 2, y + 50, 2);
		// 18: OK
		this.drawTextButton(g, 0, this.getText(18), this.canvasWidth / 2, y + 50 + this.getSpriteHeight(29) + 10, this.currentFont.stringWidth(this.getText(18)) + 8, this.currentFontHeight + 4, 2, 0);
		this.drawMirroredTamagotchiPair(
			g,
			this.irState[5],
			this.canvasWidth / 2,
			y + 50 + this.getSpriteHeight(29) + 10 + (this.currentFontHeight + 4) / 2,
			this.currentFont.stringWidth(this.getText(18)) + 20,
			this.irState[1]
		);
	}

	public static exitGame(): void {
		this.running = false;
	}

	public static goToPage(nextPage: number): void {
		switch (this.currentPage) {
			case PAGE_PARENT_CALL:
				this.clearDownloadedParentCallData();
				break;
			case PAGE_GOTCHI_KING:
				this.clearDownloadedGotchiKingData();
				break;
			case PAGE_TRAVEL_MEMORY:
				this.clearDownloadedTravelMemoryData();
				break;
			case PAGE_EXCHANGE_PLAZA:
				this.clearDownloadedExchangePlazaData();
		}

		switch (nextPage) {
			case PAGE_DOWNLOADING:
				this.loadingProgress = 0;
				break;
			case PAGE_LOADING:
				this.loadingProgress = 0;
				break;
			case PAGE_TITLE:
				// Resources loaded successfully
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetTitleScreenState();
				break;
			case PAGE_MAILBOX_MODE:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetMailboxModeState();
				break;
			case PAGE_TRAVEL_MODE:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetTravelModeSate();
				break;
			case PAGE_SHOPPING_CENTER:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetShoppingCenterState();
				break;
			case PAGE_PARENT_CALL:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetParentCallState();
				break;
			case PAGE_GOTCHI_KING:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetGotchiKingState();
				break;
			case PAGE_TRAVEL_MEMORY:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetTravelMemoryState();
				break;
			case PAGE_EXCHANGE_PLAZA:
				this.selectSoftLabel(SOFT_LABEL_MENU);
				this.resetExchangePlazaState();
				break;
			default:
				// Failed to load resources
				this.selectSoftLabel(SOFT_LABEL_EMPTY);
		}

		this.closeMenu();
		this.closeExplanation();
		this.closeErrorPage();
		this.currentPage = nextPage;
		this.drawOnNextPaint = false;
		this.fullDrawOnNextPaint = true;
	}

	public static async controlFlow(): Promise<void> {
		this.checkMusic();
		if (this.isKeyPressed(KEY_ASTERISK)) {
			this.toggleSound();
			await this.saveGame();
		}

		if (this.isMenuOpen()) {
			await this.menuFlow();
		} else if (this.shouldShowErrorPage()) {
			this.errorPageFlow();
		} else {
			switch (this.currentPage) {
				case PAGE_AUTH_ERROR:
				case PAGE_COM_ERROR:
				case PAGE_PREP_ERROR:
					this.exitGameOnSelect();
					break;
				case PAGE_NONE_0:
					if (--this.loadGameSaveDelay <= 0) {
						this.gameSave[4] = 3;
						await this.loadGameSave();
						this.goToPage(PAGE_NONE_1);
					}
					break;
				case PAGE_NONE_1:
					this.goToPage(PAGE_DOWNLOADING);
					break;
				case PAGE_DOWNLOADING:
					await this.checkGameData();
					break;
				case PAGE_LOADING:
					await this.loadResources();
					break;
				case PAGE_TITLE:
					this.titleScreenFlow();
					break;
				case PAGE_MAILBOX_MODE:
					this.mailboxModeFlow();
					break;
				case PAGE_TRAVEL_MODE:
					this.travelModeFlow();
					break;
				case PAGE_SHOPPING_CENTER:
					await this.shoppingCenterFlow();
					break;
				case PAGE_PARENT_CALL:
					await this.parentCallFlow();
					break;
				case PAGE_GOTCHI_KING:
					await this.gotchiKingFlow();
					break;
				case PAGE_TRAVEL_MEMORY:
					await this.travelMemoryFlow();
					break;
				case PAGE_EXCHANGE_PLAZA:
					await this.exchangePlazaFlow();
					break;
				default:
					if (this.isKeyPressed(KEY_SELECT)) {
						this.running = false;
					}
			}
		}
	}

	public static async draw(g: Graphics): Promise<void> {
		try {
			if (this.isMenuOpen()) {
				this.drawMenuPages(g, this.rootX, this.rootY);
				return;
			}

			if (this.shouldShowErrorPage()) {
				this.errorPage(g, this.rootX, this.rootY);
				return;
			}

			switch (this.currentPage) {
				case PAGE_AUTH_ERROR:
					this.showError(g, 'Authenticating', this.rootX, this.rootY);
					break;
				case PAGE_COM_ERROR:
					this.showError(g, 'Communicating', this.rootX, this.rootY);
					break;
				case PAGE_PREP_ERROR:
					this.showError(g, 'Preparing', this.rootX, this.rootY);
					break;
				case PAGE_NONE_0:
				case PAGE_NONE_1:
				default:
					break;
				case PAGE_DOWNLOADING:
					this.downloadingPage(g, this.rootX, this.rootY);
					break;
				case PAGE_LOADING:
					await this.loadingPage(g, this.rootX, this.rootY);
					break;
				case PAGE_TITLE:
					this.titleScreen(g, this.rootX, this.rootY);
					break;
				case PAGE_MAILBOX_MODE:
					this.mailboxModePage(g, this.rootX, this.rootY);
					break;
				case PAGE_TRAVEL_MODE:
					this.travelModePage(g, this.rootX, this.rootY);
					break;
				case PAGE_SHOPPING_CENTER:
					this.shoppingCenterPage(g, this.rootX, this.rootY);
					break;
				case PAGE_PARENT_CALL:
					this.parentCallPage(g, this.rootX, this.rootY);
					break;
				case PAGE_GOTCHI_KING:
					this.gotchiKingPage(g, this.rootX, this.rootY);
					break;
				case PAGE_TRAVEL_MEMORY:
					this.travelMemoryPage(g, this.rootX, this.rootY);
					break;
				case PAGE_EXCHANGE_PLAZA:
					this.exchangePlazaPage(g, this.rootX, this.rootY);
			}
		} catch (e) {
			this.log('disp error' + e);
		}
	}

	public static clearOutsideGameArea(g: Graphics): void {
		this.setColorOfRGB(g, 255, 255, 255);
		if (this.rootX > 0) {
			g.fillRect(0, 0, this.rootX, this.canvasHeight);
			g.fillRect(this.rootX + 240, 0, this.rootX, this.canvasHeight);
		}

		if (this.rootY > 0) {
			g.fillRect(0, 0, this.canvasWidth, this.rootY);
			g.fillRect(0, this.rootY + 240, this.canvasWidth, this.rootY);
		}
	}

	////////////////////////////////

	public mediaAction(source: MediaPresenter, type: number, param: number): void {
		if (type == 3) {
			GameApp.playMusic(GameApp.loopedSoundId, true);
		}
	}

	public resume(): void {
		try {
			GameApp.timer.stop();
			void GameApp.startTimerWithRetry();
		} catch (ignored) {}

		try {
			// Thread.sleep(50L);
		} catch (ignored) {}

		GameApp.restartMusicOnNext();
		GameApp.resumedDraw = true;
		GameApp.fullDrawOnNextPaint = true;
	}

	async timerExpired(timer: Timer): Promise<void> {
		try {
			GameApp.timer.stop();
			if (!this.executingTimerExpired) {
				this.executingTimerExpired = true;
				if (GameApp.drawState == 0) {
					GameApp.updateInputState();
					GameApp.setSomeSystemAttribute();
					await GameApp.controlFlow();
					if ((GameApp.garbageCollectTimer & 10) == 0) {
						// System.gc();
					}

					GameApp.rand(2);
					++GameApp.garbageCollectTimer;
					GameApp.drawState = 1;
				}

				if (GameApp.drawState == 1) {
					GameApp.repaint();
				}

				if (!GameApp.running) {
					IApplication.getCurrentApp()?.terminate();
				}

				this.executingTimerExpired = false;
			}

			await GameApp.startTimerWithRetry();
		} catch (ignored) {}
	}

	public initCanvas(): void {
		GameApp.canvas = new GameScreen(this);
		GameApp.canvas.setBackground(Graphics.getColorOfName(0));
		GameApp.canvasWidth = GameApp.canvas.getWidth();
		GameApp.canvasHeight = GameApp.canvas.getHeight();
		GameApp.rootX = (GameApp.canvasWidth - 240) / 2;
		GameApp.rootY = (GameApp.canvasHeight - 240) / 2;
		GameApp.inputStateFlag = true;
		Display.setCurrent(GameApp.canvas);
	}

	public start(): void {}
}
