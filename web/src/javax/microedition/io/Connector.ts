import { DesktopHttpConnection } from '../../../com/nttdocomo/io/DesktopHttpConnection';
import type { HttpConnection } from '../../../com/nttdocomo/io/HttpConnection';
import { DataInputStream } from '../../../java/io/DataInputStream';
import { DataOutputStream } from '../../../java/io/DataOutputStream';
import type { InputStream } from '../../../java/io/InputStream';
import type { OutputStream } from '../../../java/io/OutputStream';
import { ScratchpadFile } from './ScratchpadFile';
import { ScratchpadInputStream } from './ScratchpadInputStream';
import { ScratchpadOutputStream } from './ScratchpadOutputStream';

type Params = Record<string, string>;

export class Connector {
	private static scratchpad: ScratchpadFile = new ScratchpadFile('game_scratchpad', 'scratchpad');

	public static setScratchpadStore(dbName: string, storeName: string): void {
		Connector.scratchpad = new ScratchpadFile(dbName, storeName);
	}

	public static open(url: string, _mode: number, _timeouts: boolean): HttpConnection {
		console.log('http open:', url);
		return new DesktopHttpConnection(url);
	}

	public static async openInputStream(location: string): Promise<InputStream> {
		console.log('openInputStream:', location);
		const u = Connector.parse(location);

		if (u.scheme !== 'scratchpad') {
			throw new Error(`Unsupported scheme for openInputStream: ${u.scheme}`);
		}

		const pos = u.getLongParam('pos', 0);
		return new ScratchpadInputStream(Connector.scratchpad, pos);
	}

	public static async openOutputStream(location: string): Promise<OutputStream> {
		console.log('openOutputStream:', location);
		const u = Connector.parse(location);

		if (u.scheme !== 'scratchpad') {
			throw new Error(`Unsupported scheme for openOutputStream: ${u.scheme}`);
		}

		const pos = u.getLongParam('pos', 0);
		return new ScratchpadOutputStream(Connector.scratchpad, pos);
	}

	public static async openDataInputStream(location: string): Promise<DataInputStream> {
		return new DataInputStream(await Connector.openInputStream(location));
	}

	public static async openDataOutputStream(location: string): Promise<DataOutputStream> {
		return new DataOutputStream(await Connector.openOutputStream(location));
	}

	private static parse(raw: string): ParsedUrl {
		// Examples:
		//  scratchpad:///0;pos=128
		//  https://example.com/a.bin
		//  file path
		const schemeIdx = raw.indexOf(':');
		if (schemeIdx <= 0) return new ParsedUrl('file', raw, {});

		const scheme = raw.substring(0, schemeIdx).toLowerCase();
		if (scheme !== 'scratchpad') {
			return new ParsedUrl(scheme, raw, {});
		}

		// scratchpad:///0;pos=123;foo=bar
		// Split parameters after the first ';'
		const semi = raw.indexOf(';');
		const params: Params = {};
		if (semi >= 0 && semi + 1 < raw.length) {
			const paramPart = raw.substring(semi + 1);
			const pairs = paramPart.split(';');
			for (const p of pairs) {
				const eq = p.indexOf('=');
				if (eq > 0 && eq + 1 < p.length) {
					params[p.substring(0, eq)] = p.substring(eq + 1);
				} else if (p.length > 0) {
					params[p] = '';
				}
			}
		}

		return new ParsedUrl('scratchpad', raw, params);
	}
}

class ParsedUrl {
	public readonly scheme: string;
	public readonly raw: string;
	private readonly params: Params;

	constructor(scheme: string, raw: string, params: Params) {
		this.scheme = scheme;
		this.raw = raw;
		this.params = params;
	}

	public getLongParam(key: string, def: number): number {
		try {
			const v = this.params[key];
			if (v == null || v === '') return def;
			const n = Number(v);
			return Number.isFinite(n) ? Math.floor(n) : def;
		} catch {
			return def;
		}
	}
}
