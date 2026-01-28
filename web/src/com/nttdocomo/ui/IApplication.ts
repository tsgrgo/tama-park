// IApplication.ts

import { Canvas } from './Canvas';

export abstract class IApplication {
	private static currentApp?: IApplication;

	private args: string[] = [];
	private canvas?: Canvas | null;
	private hostElement?: HTMLDivElement;

	protected constructor() {
		IApplication.currentApp = this;
		this.createHostElement();
	}

	public static getCurrentApp(): IApplication | null {
		if (!this.currentApp) {
			throw new Error(
				'getCurrentApp(): Application has not yet been created'
			);
		}
		return this.currentApp;
	}

	public abstract start(): void;
	public abstract resume(): void;

	protected getSourceURL(): string {
		return '';
	}

	protected getArgs(): string[] {
		return this.args.slice();
	}

	public launch(type: number, args: string[] | null): void {
		if (args) this.args = args.slice();
		console.log('IApplication.launch type=', type, 'args=', this.args);
	}

	public terminate(): void {
		console.log('terminated');
	}

	public setCanvas(canvas: Canvas | null): void {
		if (this.canvas === canvas) return;

		if (!this.hostElement) return;

		if (this.canvas) {
			const oldEl = this.canvas.unwrap();
			if (oldEl.parentElement === this.hostElement) {
				this.hostElement.removeChild(oldEl);
			}
		}

		this.canvas = canvas;

		if (canvas) {
			const el = canvas.unwrap();
			this.hostElement.appendChild(el);
			canvas.focus();
		}
	}

	private createHostElement(): void {
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

		window.addEventListener('beforeunload', () => {
			this.terminate();
		});

		this.hostElement = frame;
	}
}
