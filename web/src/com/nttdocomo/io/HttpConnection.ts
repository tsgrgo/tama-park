import type { DataInputStream } from '../../../java/io/DataInputStream';
import type { InputStream } from '../../../java/io/InputStream';
import type { OutputStream } from '../../../java/io/OutputStream';

export interface HttpConnection {
	setRequestMethod(method: string): void;
	connect(): Promise<void>;
	openInputStream(): Promise<InputStream>;
	openDataInputStream(): Promise<DataInputStream>;
	openOutputStream(): Promise<OutputStream>;
	getLength(): Promise<number>;
	close(): Promise<void>;
}
