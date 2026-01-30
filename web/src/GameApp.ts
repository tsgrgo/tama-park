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
import type { IrRemoteControlFrame } from './com/nttdocomo/device/IrRemoteControlFrame';
import { IrRemoteControl } from './com/nttdocomo/device/IrRemoteControl';
import { PhoneSystem } from './com/nttdocomo/ui/PhoneSystem';

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
	public static drawState: number; // 0: Idle, 2: Request pending, 3: Currently drawing
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
	public static parentCallImages: Image[];
	public static parentCallText: string;
	public static parentCallQuote: string;
	public static gotchiKingState: number[];
	public static gotchiKingInviteTicketLayout: number[];
	public static gotchiKingImages: Image[];
	public static imagesToTemporarilyDispose: number[];
	public static travelMemoryState: number[]; // [?, flowStep, ...]
	public static memoryPhotoLayout: number[];
	public static travelMemoryTexts: string[];
	public static travelMemoryPhoto: Image;
	public static exchangePlazaState: number[]; // [?, ?, colorIdx?, ...]
	public static exchangeTicketLayout: number[];
	public static exchangePlazaImages: Image[];
	public static exchangePlazaTexts: string[];
	public static regionSelectLayout: number[];
	public static explanationState: number[]; // [index, numberOfPages, current, isOpen]
	public static menuState: number[]; // [isMenuOpen, menuPage, explanationIndex, numberOfExplanationPages...]
	public static errorState: number[]; // [pageToGoBack, flowStepToGoBack, flowStepToGoBack, showErrorPage] ? = some action id?
	public static errorPageText: string;
	public static imageReadInfo: bigint;
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
	public static irSendTimestamp: bigint;
	public static generalIrSendLayout: number[];
	public static shoppingCenterIrSendLayout: number[];
	public executingTimerExpired: boolean;

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

	public static startTimerWithRetry(): void {
		for (let i = 0; i < 10; ++i) {
			try {
				this.timer.start();
				return;
			} catch (e) {
				try {
					this.timer.stop();
				} catch (ignored) {}

				try {
					// Thread.sleep(1000L);
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
		let var0 = 0n;
		let var2 = 0n;

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

	public static loadGameSave() {}

	public static saveGame() {}

	public static downloadGameData(path: string, size: number, pos: number) {}

	public static loadShortArray(pos: number): number[] {
		return this.loadArray(pos, 2);
	}

	public static loadArray(pos: number, elementSizeInBytes: number): number[] {
		return []; // TODO
	}

	public static drawSprite(g: Graphics, idx: number, x: number, y: number, anchor: number): void {
		if (this.images[idx]) this.drawImage(g, this.images[idx], x, y, anchor);
	}

	public static drawImage(g: Graphics, img: Image, x: number, y: number, anchor: number): void {
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

		let startIndex = currentIndex;

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

	public static downloadGameDataIfNeeded(): boolean {
		try {
			if (this.gameDataVersion != this.gameSave[2]) {
				this.gameSave[1] = 0;
				this.gameSave[2] = this.gameDataVersion;
				this.saveGame();
			}

			if (this.gameSave[1] != 255) {
				this.loadingProgress = this.gameSave[1];
				this.downloadGameData('', 72483, 128);
				this.gameSave[1] = 255;
				this.saveGame();
			}

			return true;
		} catch (e) {
			return false;
		}
	}

	public static checkGameData(): void {
		if (this.downloadGameDataIfNeeded()) {
			this.goToPage(PAGE_LOADING);
		} else {
			this.goToPage(PAGE_COM_ERROR);
		}
	}

	public static loadResources(): void {
		const result = this.loadImages(128, 0, 93);
		if (result == -1) {
			this.goToPage(PAGE_PREP_ERROR);
		} else if (this.loadSounds() && this.loadTexts()) {
			this.goToPage(PAGE_TITLE);
		} else {
			this.goToPage(PAGE_PREP_ERROR);
		}
	}

	public static loadImages(pos: number, startIndex: number, count: number): number {
		return 1; // TODO
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
		const progressBarWidth = (200 * this.loadingProgress) / 8;
		g.fillRect(barX, barY, progressBarWidth, 40);
	}

	public static loadingPage(g: Graphics, x: number, y: number): void {
		if (3 < this.loadingProgress) {
			try {
				// Thread.sleep(300L);
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
			offset -= this.getSpriteWidth(spriteIndex);
		}

		while (offset < screenWidth) {
			this.drawSprite(g, spriteIndex, x + offset, y, 0);
			offset += this.getSpriteWidth(spriteIndex);
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
				drawLayoutSpriteButton(g, i, this.mailboxModeLayout, 0);
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

	public static shoppingCenterFlow(): void {
		this.shoppingCenterState[2]++;
		switch (this.shoppingCenterState[1]) {
			case 0:
				this.nextShoppingCenterState(1);
				break;
			case 1:
				this.shoppingCenterItemTypeSelectFlow();
				break;
			case 2:
				this.downloadShoppingCenterPassword();
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
		let codeInputY = y + 3 + this.currentFontHeight + 12 + this.currentFontHeight + 4;
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

	////////////////////////////////

	public resume(): void {
		throw new Error('Method not implemented.');
	}
	mediaAction(source: MediaPresenter, type: number, param: number): void {
		throw new Error('Method not implemented.');
	}
	timerExpired(timer: Timer): void {
		throw new Error('Method not implemented.');
	}

	static a: number = 0;
	public static draw(g: Graphics): void {
		console.log('paint');
		this.drawBeveledRect(g, 50 + Math.sin(this.a++ * 0.01) * 30, 50 + Math.cos(this.a++ * 0.01) * 30, 40, 40, 0x34345, 0x45583);

		this.drawRoundedButtonBackground(g, 100, 100, 100, 25, 0x568756, 0xfffff, 0xaaaa, false);
	}

	public static drawRoundedButtonBackground(g: Graphics, x: number, y: number, width: number, height: number, outlineColor: number, color: number, shadowColor: number, isPressed: boolean) {
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

	public initCanvas(): void {
		GameApp.canvas = new GameScreen(this);
		GameApp.canvas.setBackground(Graphics.getColorOfName(0));
		GameApp.canvasWidth = GameApp.canvas.getWidth();
		GameApp.canvasHeight = GameApp.canvas.getHeight();
		GameApp.rootX = (GameApp.canvasWidth - 240) / 2;
		GameApp.rootY = (GameApp.canvasHeight - 240) / 2;
		GameApp.inputStateFlag = true;
		Display.setCurrent(GameApp.canvas);

		setInterval(() => {
			GameApp.drawState = 2;
			GameApp.canvas?.repaint();
		}, 10);
	}

	public static goToPage(n: number) {}

	public start(): void {}
}
