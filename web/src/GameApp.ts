import type { AudioPresenter } from './com/nttdocomo/ui/AudioPresenter';
import type { Canvas } from './com/nttdocomo/ui/Canvas';
import { Display } from './com/nttdocomo/ui/Display';
import type { Font } from './com/nttdocomo/ui/Font';
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

const KEY_0 = 1;
const KEY_1 = 2;
const KEY_2 = 4;
const KEY_3 = 8;
const KEY_4 = 16;
const KEY_5 = 32;
const KEY_6 = 64;
const KEY_7 = 128;
const KEY_8 = 256;
const KEY_9 = 512;
const KEY_ASTERISK = 1024; // '*'
const KEY_POUND = 2048; // '#'
const KEY_LEFT = 65536;
const KEY_UP = 131072;
const KEY_RIGHT = 262144;
const KEY_DOWN = 524288;
const KEY_SELECT = 1048576;
const KEY_SOFT1 = 2097152;
const KEY_SOFT2 = 4194304;
const KEY_UP_LEFT = 196608;
const KEY_DOWN_RIGHT = 786432;
const KEY_DOWN_RIGHT_SELECT = 1835008;

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
	public static images: Image[];
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

	// inside GameApp class

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

	////////////////////////////////

	public static processEvent(type: number, param: number): void {}

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

	public static drawBeveledRect(g: Graphics, x: number, y: number, width: number, height: number, borderColor: number, innerColor: number) {
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
		textHeight = currentFontHeight + (textHeight - 1) * (currentFontHeight + 1);
		this.setColorOfRGBInt(g, textColor);
		this.drawString(g, text, x + width / 2, y + (height - textHeight) / 2, ALIGN_CENTER);
	}

	public static setColorOfRGB(graphics: Graphics, r: number, g: number, b: number): void {
		graphics.setColor(Graphics.getColorOfRGB(r, g, b));
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

	public static setColorOfRGBInt(g: Graphics, rgb: number) {
		g.setColor(Graphics.getColorOfRGB((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255));
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

	public start(): void {}
}
