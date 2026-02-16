import { toByteArray } from 'base64-js';
import workletUrl from './mld-worklet.ts?worker&url';

type MldInfoMsg = {
	type: 'info';
	title: string;
	version: string;
	date: string;
	copyright: string;
	durationLooping: number;
	durationNoLoop: number;
};

type WorkletToMainMsg = MldInfoMsg | { type: 'error'; message: string };

export async function playMldInWorklet(data: Uint8Array) {
	const audioContext = new AudioContext();
	await audioContext.audioWorklet.addModule(workletUrl);

	const node = new AudioWorkletNode(audioContext, 'mld-player', {
		numberOfInputs: 0,
		numberOfOutputs: 1,
		outputChannelCount: [2]
	});

	node.connect(audioContext.destination);

	node.port.onmessage = (e: MessageEvent<WorkletToMainMsg>) => {
		const msg = e.data;
		if (msg.type === 'info') {
			parseInfoMessage(msg);
		} else if (msg.type === 'error') {
			console.error(msg.message);
		}
	};

	// Send MLD to worklet
	const buffer = data.buffer;
	node.port.postMessage({ type: 'load', buffer }, [buffer]);

	await audioContext.resume();

	return { audioCtx: audioContext, node };
}

function parseInfoMessage(info: MldInfoMsg) {
	const parsed = {
		title: b64toShiftJisString(info.title),
		version: b64toShiftJisString(info.version),
		date: b64toShiftJisString(info.date),
		copyright: b64toShiftJisString(info.copyright),
		durationLooping: info.durationLooping,
		durationNoLoop: info.durationNoLoop
	};
	console.log(parsed);
}

const encoder = new TextDecoder('Shift_JIS');
function b64toShiftJisString(b64: string): string {
	// eslint-disable-next-line @typescript-eslint/no-unsafe-call
	const bytes = toByteArray(b64) as Uint8Array;
	return encoder.decode(bytes);
}
