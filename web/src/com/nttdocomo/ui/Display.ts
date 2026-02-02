import type { Canvas } from './Canvas';

/**
 * Defines the view of a device. The display class is an abstraction of the device's screen and keypad,
 * and is used to get information about the screen and keypad.
 */
export class Display {
	public static readonly KEY_PRESSED_EVENT = 0;
	public static readonly KEY_RELEASED_EVENT = 1;

	// Key constants
	public static readonly KEY_0 = 0x00;
	public static readonly KEY_ASTERISK = 0x0a; // '*'
	public static readonly KEY_POUND = 0x0b; // '#'

	// Directions / select
	public static readonly KEY_LEFT = 0x10;
	public static readonly KEY_UP = 0x11;
	public static readonly KEY_RIGHT = 0x12;
	public static readonly KEY_DOWN = 0x13;
	public static readonly KEY_SELECT = 0x14;

	// Soft keys
	public static readonly KEY_SOFT1 = 0x15;
	public static readonly KEY_SOFT2 = 0x16;

	private static canvas?: Canvas | null;
	private static hostElement?: HTMLDivElement;
	private static keypadStateBits: number;

	public static setCurrent(canvas: Canvas | null) {
		if (this.canvas === canvas) return;

		if (!this.hostElement) this.createHostElement();

		if (this.canvas) {
			const oldEl = this.canvas.unwrap();
			if (oldEl.parentElement === this.hostElement) {
				this.hostElement.removeChild(oldEl);
			}
		}

		this.canvas = canvas;

		if (canvas) {
			const el = canvas.unwrap();
			this.hostElement?.appendChild(el);
			canvas.focus();
		}
	}

	private static createHostElement(): void {
		if (this.hostElement) return;

		const frame = document.createElement('div');
		frame.style.width = '240px';
		frame.style.height = '240px';
		frame.style.border = '1px solid black';
		frame.style.background = 'black';
		frame.style.position = 'relative';
		frame.style.margin = '0 auto';
		frame.style.overflow = 'hidden';
		frame.style.display = 'flex';
		frame.style.alignItems = 'center';
		frame.style.justifyContent = 'center';

		const title = document.createElement('div');
		title.textContent = 'DoJa Emulator';
		title.style.position = 'absolute';
		title.style.top = '-1.5em';
		title.style.left = '0';
		title.style.color = 'black';
		title.style.fontFamily = 'sans-serif';
		title.style.fontSize = '14px';

		document.body.appendChild(title);
		document.body.appendChild(frame);

		this.hostElement = frame;

		document.addEventListener('keydown', e => {
			const dojaKeyCode = this.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, true);
			this.processEvent(this.KEY_PRESSED_EVENT, dojaKeyCode);
			e.preventDefault();
		});

		document.addEventListener('keyup', e => {
			const dojaKeyCode = this.mapDomToDojaKey(e);
			this.updateKeypadState(dojaKeyCode, false);
			this.processEvent(this.KEY_RELEASED_EVENT, dojaKeyCode);
			e.preventDefault();
		});
	}

	private static processEvent(type: number, param: number) {
		this.canvas?.dispatchEvent(type, param);
	}

	private static mapDomToDojaKey(e: KeyboardEvent): number {
		const code = e.code;
		const key = e.key;

		// Directions / select
		switch (code) {
			case 'ArrowLeft':
				return this.KEY_LEFT;
			case 'ArrowRight':
				return this.KEY_RIGHT;
			case 'ArrowUp':
				return this.KEY_UP;
			case 'ArrowDown':
				return this.KEY_DOWN;
			case 'Enter':
			case 'NumpadEnter':
				return this.KEY_SELECT;

			// Soft keys
			case 'F1':
				return this.KEY_SOFT1;
			case 'F2':
				return this.KEY_SOFT2;
		}

		// Digits
		if (code.startsWith('Digit')) {
			const d = Number(code.slice('Digit'.length));
			if (Number.isInteger(d) && d >= 0 && d <= 9) return this.KEY_0 + d;
		}
		if (code.startsWith('Numpad')) {
			const maybe = code.slice('Numpad'.length);
			const d = Number(maybe);
			if (Number.isInteger(d) && d >= 0 && d <= 9) return this.KEY_0 + d;
		}

		// Symbols
		if (key === '*') return this.KEY_ASTERISK;
		if (key === '#') return this.KEY_POUND;

		// Not mapped
		return -1;
	}

	private static updateKeypadState(dojaKeyCode: number, pressed: boolean): void {
		if (dojaKeyCode < 0 || dojaKeyCode >= 32) return;

		const bit = 1 << dojaKeyCode;

		if (pressed) this.keypadStateBits |= bit;
		else this.keypadStateBits &= ~bit;

		this.canvas?.setKeypadSate(this.keypadStateBits);
	}
}
