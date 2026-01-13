import { CanvasBase } from './CanvasBase';
import { Graphics } from './Graphics';
import { Application } from './Application';

export class Screen extends CanvasBase {
	constructor(_app: Application, canvasEl: HTMLCanvasElement) {
		super(canvasEl);
	}

	processEvent(var1: number, var2: number): void {
		Application.Code_input(var1, var2);
	}

	paint(g: Graphics): void {
		if (Application.Code === 2) {
			Application.Code = 3;

			if (Application.Exceptions) {
				g.lock();

				Application.I = Application.StackMap;
				Application.StackMap = false;

				if (Application.Z) {
					Application.Z = false;
				}

				g.setFont(Application.J);

				Application.Exceptions_draw(g);

				g.unlock(true);
			} else {
				Application.Exceptions = true;
			}

			Application.Code = 0;
		}
	}
}
