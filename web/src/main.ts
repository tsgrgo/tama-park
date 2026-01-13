import { Screen } from './Screen';
import { Application } from './Application';

const canvas = document.querySelector<HTMLCanvasElement>('#game');
if (!canvas) throw new Error('Missing #game canvas');

canvas.width = 240;
canvas.height = 240;

const app = new Application();
const screen = new Screen(app, canvas);

function frame() {
	Application.Code = 2;
	screen.repaint();
	requestAnimationFrame(frame);
}

requestAnimationFrame(frame);
