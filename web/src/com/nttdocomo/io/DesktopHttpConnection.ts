import { ByteArrayInputStream } from '../../../java/io/ByteArrayInputStream';
import { ByteArrayOutputStream } from '../../../java/io/ByteArrayOutputStream';
import { DataInputStream } from '../../../java/io/DataInputStream';
import type { InputStream } from '../../../java/io/InputStream';
import type { OutputStream } from '../../../java/io/OutputStream';
import type { HttpConnection } from './HttpConnection';

export class DesktopHttpConnection implements HttpConnection {
	private readonly urlString: string;

	private method = 'GET';
	private connected = false;

	private inputStream: InputStream | null = null;
	private outputStream: ByteArrayOutputStream | null = null;

	private response: Response | null = null;
	private responseBytes: Uint8Array | null = null;

	private headers = new Headers();

	constructor(urlString: string) {
		this.urlString = urlString;
	}

	public setRequestMethod(method: string): void {
		if (method != null && method.trim() !== '') {
			this.method = method;
		}
	}

	public async connect(): Promise<void> {
		if (this.connected) return;
		this.connected = true;
	}

	public async openInputStream(): Promise<InputStream> {
		await this.connect();
		if (this.inputStream) return this.inputStream;

		await this.ensureFetched();

		const bytes = this.responseBytes ?? new Uint8Array(0);
		this.inputStream = new ByteArrayInputStream(bytes);
		return this.inputStream;
	}

	public async openDataInputStream(): Promise<DataInputStream> {
		return new DataInputStream(await this.openInputStream());
	}

	public async openOutputStream(): Promise<OutputStream> {
		await this.connect();
		if (this.outputStream) return this.outputStream;

		this.outputStream = new ByteArrayOutputStream();
		return this.outputStream;
	}

	public async getLength(): Promise<number> {
		await this.connect();
		await this.ensureFetched();

		if (this.responseBytes) return this.responseBytes.length;

		const lenHeader = this.response?.headers.get('content-length');
		if (!lenHeader) return -1;
		const n = Number(lenHeader);
		return Number.isFinite(n) ? n : -1;
	}

	public async close(): Promise<void> {
		try {
			if (this.inputStream) await this.inputStream.close();
		} catch {}
		try {
			if (this.outputStream) await this.outputStream.close();
		} catch {}

		this.inputStream = null;
		this.outputStream = null;
		this.response = null;
		this.responseBytes = null;

		this.connected = false;
		this.headers = new Headers();
	}

	private async ensureFetched(): Promise<void> {
		if (this.response) return;

		const m = this.method.toUpperCase();

		let body: Uint8Array | undefined;

		const canHaveBody = m !== 'GET' && m !== 'HEAD';

		if (canHaveBody && this.outputStream) {
			body = this.outputStream.toByteArray();
		}

		let resp: Response;
		try {
			resp = await fetch(this.urlString, {
				method: m,
				headers: this.headers,
				// @ts-ignore
				body: body ? body : undefined,
				cache: 'no-store',
				redirect: 'follow'
			});
		} catch (e) {
			throw new Error(`HttpConnection.connect failed for: ${this.urlString}`);
		}

		this.response = resp;

		try {
			const ab = await resp.arrayBuffer();
			this.responseBytes = new Uint8Array(ab);
		} catch {
			this.responseBytes = new Uint8Array(0);
		}
	}

	public setRequestProperty(key: string, value: string): void {
		this.headers.set(key, value);
	}

	public getResponseCode(): number {
		return this.response ? this.response.status : 0;
	}

	public getResponseMessage(): string {
		return this.response ? this.response.statusText : '';
	}
}
