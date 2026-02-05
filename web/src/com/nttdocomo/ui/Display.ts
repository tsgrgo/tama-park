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
	private static pointerIdToDojaKey = new Map<number, number>();
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
		frame.style.position = 'relative';
		frame.style.margin = '0 auto';
		frame.style.overflow = 'hidden';
		frame.style.display = 'flex';
		frame.style.alignItems = 'center';
		frame.style.justifyContent = 'center';

		const parent = document.querySelector('#game');
		if (!parent) throw new Error('#game root element not found');
		parent.appendChild(frame);

		this.hostElement = frame;

		document.addEventListener('keydown', e => {
			const dojaKeyCode = this.mapDomToDojaKey(e);
			this.pressKey(dojaKeyCode);
		});

		document.addEventListener('keyup', e => {
			const dojaKeyCode = this.mapDomToDojaKey(e);
			this.releaseKey(dojaKeyCode);
		});

		document.addEventListener('pointerdown', e => {
			const buttonId = this.getButtonIdFromEventTarget(e.target);
			const dojaKey = this.mapButtonIdToDojaKey(buttonId);
			if (dojaKey < 0) return;

			if (e.target instanceof Element) {
				e.target.setPointerCapture(e.pointerId);
			}

			this.pointerIdToDojaKey.set(e.pointerId, dojaKey);
			this.pressKey(dojaKey);
		});

		const releasePointer = (pe: PointerEvent) => {
			const dojaKey = this.pointerIdToDojaKey.get(pe.pointerId);
			if (dojaKey === undefined) return;

			this.pointerIdToDojaKey.delete(pe.pointerId);
			this.releaseKey(dojaKey);
		};

		document.addEventListener('pointerup', e => releasePointer(e as PointerEvent));
		document.addEventListener('pointercancel', e => releasePointer(e as PointerEvent));

		window.addEventListener('blur', () => {
			for (const dojaKey of this.pointerIdToDojaKey.values()) {
				this.releaseKey(dojaKey);
			}
			this.pointerIdToDojaKey.clear();
		});
	}

	private static pressKey(dojaKey: number) {
		this.updateKeypadState(dojaKey, true);
		this.processEvent(this.KEY_PRESSED_EVENT, dojaKey);
	}

	private static releaseKey(dojaKey: number) {
		this.updateKeypadState(dojaKey, false);
		this.processEvent(this.KEY_RELEASED_EVENT, dojaKey);
	}

	private static getButtonIdFromEventTarget(target: EventTarget | null): string {
		if (!(target instanceof Element)) return '';
		const el = target.closest('[id^="key-"]');
		return el?.id || '';
	}

	private static processEvent(type: number, param: number) {
		this.canvas?.dispatchEvent(type, param);
	}

	private static mapDomToDojaKey(e: KeyboardEvent): number {
		const code = e.code;
		const key = e.key;

		// Directions / select
		switch (code) {
			case 'KeyA':
			case 'ArrowLeft':
				return this.KEY_LEFT;
			case 'KeyD':
			case 'ArrowRight':
				return this.KEY_RIGHT;
			case 'KeyW':
			case 'ArrowUp':
				return this.KEY_UP;
			case 'KeyS':
			case 'ArrowDown':
				return this.KEY_DOWN;
			case 'Enter':
			case 'NumpadEnter':
			case 'Space':
				return this.KEY_SELECT;

			// Soft keys
			case 'F1':
			case 'KeyQ':
			case 'ShiftLeft':
				return this.KEY_SOFT1;
			case 'F2':
			case 'KeyE':
			case 'ControlLeft':
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

	private static mapButtonIdToDojaKey(id: string): number {
		switch (id) {
			case 'key-left':
				return this.KEY_LEFT;
			case 'key-right':
				return this.KEY_RIGHT;
			case 'key-up':
				return this.KEY_UP;
			case 'key-down':
				return this.KEY_DOWN;
			case 'key-select':
				return this.KEY_SELECT;

			case 'key-soft-1':
				return this.KEY_SOFT1;
			case 'key-soft-2':
				return this.KEY_SOFT2;

			case 'key-asterisk':
				return this.KEY_ASTERISK;
			case 'key-pound':
				return this.KEY_POUND;
		}

		if (id.startsWith('key-')) {
			const n = Number(id.slice(4));
			if (Number.isInteger(n) && n >= 0 && n <= 9) {
				return this.KEY_0 + n;
			}
		}

		return -1;
	}

	private static updateKeypadState(dojaKeyCode: number, pressed: boolean): void {
		if (dojaKeyCode < 0 || dojaKeyCode >= 32) return;

		const bit = 1 << dojaKeyCode;

		if (pressed) this.keypadStateBits |= bit;
		else this.keypadStateBits &= ~bit;

		this.canvas?.setKeypadSate(this.keypadStateBits);
		console.log(this.keypadStateBits);
	}
}
