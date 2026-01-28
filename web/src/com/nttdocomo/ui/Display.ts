import type { Canvas } from './Canvas';
import { IApplication } from './IApplication';

export class Display {
	public static setCurrent(canvas: Canvas) {
		IApplication.getCurrentApp()?.setCanvas(canvas);
	}
}
