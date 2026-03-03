import { Canvas } from './com/nttdocomo/ui/Canvas';
import { IApplication } from './com/nttdocomo/ui/IApplication';
import { GameApp } from './GameApp';

IApplication.setSourceURL('/data/');
// Canvas.setTftFilterEnabled(true);

const game = new GameApp();

startIconAnimation(['icons/image_36.png', 'icons/image_35.png'], 1000);

function startIconAnimation(icons: string[], speed: number) {
	let iconIndex = 0;

	setInterval(() => {
		[...document.querySelectorAll('[rel="icon"]')].forEach(e => e.remove());

		if (iconIndex >= icons.length) iconIndex = 0;
		const link = document.createElement('link');
		link.rel = 'icon';
		link.href = icons[iconIndex++];
		link.type = 'image/png';

		document.head.append(link);
	}, speed);
}
