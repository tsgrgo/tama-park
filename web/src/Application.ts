import { Graphics } from './Graphics';

export class Application {
	static Code = 0;
	static Exceptions = false;
	static I = false;
	static StackMap = false;
	static Z = false;

	// should be a Font object
	static J = '16px sans-serif';

	static Code_input(type: number, param: number): void {}

	static Exceptions_draw(g: Graphics): void {
		g.setColor('#000');
		g.fillRect(0, 0, 240, 240);
		g.setColor('#fff');
		g.drawString('Hello from b.Exceptions_draw()', 10, 20);
	}
}
