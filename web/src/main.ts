import { Canvas } from './com/nttdocomo/ui/Canvas';
import { IApplication } from './com/nttdocomo/ui/IApplication';
import { GameApp } from './GameApp';

IApplication.setSourceURL('/data/');
Canvas.setTftFilterEnabled(true);

const game = new GameApp();
