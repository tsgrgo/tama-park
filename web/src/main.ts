import { Canvas } from './com/nttdocomo/ui/Canvas';
import { IApplication } from './com/nttdocomo/ui/IApplication';
import { GameApp } from './GameApp';
import { startIconAnimation } from './utils/startIconAnimation';

IApplication.setSourceURL('/data/');
// Canvas.setTftFilterEnabled(true);

const game = new GameApp();

void startIconAnimation(['icons/image_36.png', 'icons/image_35.png'], 1000);
