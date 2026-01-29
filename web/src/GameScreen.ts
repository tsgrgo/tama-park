import { Canvas } from './com/nttdocomo/ui/Canvas';
import type { Graphics } from './com/nttdocomo/ui/Graphics';
import { GameApp } from './GameApp';

export class GameScreen extends Canvas {
	constructor(_app: GameApp) {
		super();
	}

	protected override processEvent(type: number, param: number): void {
		GameApp.processEvent(type, param);
	}

	protected override paint(g: Graphics): void {
		if (GameApp.drawState === 2) {
			GameApp.drawState = 3;

			if (GameApp.drawOnNextPaint) {
				g.lock();

				GameApp.fullDraw = GameApp.fullDrawOnNextPaint;
				GameApp.fullDrawOnNextPaint = false;

				if (GameApp.resumedDraw) {
					GameApp.resumedDraw = false;
				}

				g.setFont(GameApp.currentFont);
				GameApp.draw(g);
				g.unlock(true);
			} else {
				GameApp.drawOnNextPaint = true;
			}

			GameApp.drawState = 0;
		}
	}
}
