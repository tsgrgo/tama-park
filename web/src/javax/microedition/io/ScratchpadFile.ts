type IDBMode = 'readonly' | 'readwrite';

function reqToPromise<T>(req: IDBRequest<T>): Promise<T> {
	return new Promise((resolve, reject) => {
		req.onsuccess = () => resolve(req.result);
		req.onerror = () => reject(req.error);
	});
}

function openDb(dbName: string, storeName: string): Promise<IDBDatabase> {
	return new Promise((resolve, reject) => {
		const req = indexedDB.open(dbName, 1);
		req.onupgradeneeded = () => {
			const db = req.result;
			if (!db.objectStoreNames.contains(storeName)) {
				db.createObjectStore(storeName);
			}
		};
		req.onsuccess = () => resolve(req.result);
		req.onerror = () => reject(req.error);
	});
}

class AsyncMutex {
	private p = Promise.resolve();
	run<T>(fn: () => Promise<T>): Promise<T> {
		const next = this.p.then(fn, fn);
		this.p = next.then(
			() => undefined,
			() => undefined
		);
		return next;
	}
}

export class ScratchpadFile {
	public static readonly HEADER_SIZE = 64;
	public static readonly PAGE_SIZE = 16 * 1024;

	private readonly dbPromise: Promise<IDBDatabase>;
	private readonly storeName: string;
	private readonly mutex = new AsyncMutex();

	constructor(dbName = 'game_scratchpad', storeName = 'scratchpad') {
		this.storeName = storeName;
		this.dbPromise = openDb(dbName, storeName);
	}

	private pageKey(i: number) {
		return `p:${i}`;
	}
	private metaKey(k: string) {
		return `m:${k}`;
	}

	private async store(mode: IDBMode): Promise<IDBObjectStore> {
		const db = await this.dbPromise;
		const tx = db.transaction(this.storeName, mode);
		return tx.objectStore(this.storeName);
	}

	/** Logical length in "user space" (excluding header). */
	public async getLength(): Promise<number> {
		return this.mutex.run(async () => {
			const st = await this.store('readonly');
			const v = await reqToPromise<any>(st.get(this.metaKey('len')));
			return typeof v === 'number' ? v : 0;
		});
	}

	public async readAt(pos: number, len: number): Promise<Uint8Array> {
		if (pos < 0) pos = 0;
		if (len <= 0) return new Uint8Array(0);

		const absPos = pos + ScratchpadFile.HEADER_SIZE;

		return this.mutex.run(async () => {
			const st = await this.store('readonly');
			const out = new Uint8Array(len);

			let remaining = len;
			let outOff = 0;
			let cur = absPos;

			while (remaining > 0) {
				const pageIndex = Math.floor(cur / ScratchpadFile.PAGE_SIZE);
				const pageOff = cur % ScratchpadFile.PAGE_SIZE;
				const take = Math.min(remaining, ScratchpadFile.PAGE_SIZE - pageOff);

				const pageVal = await reqToPromise<any>(st.get(this.pageKey(pageIndex)));
				const page = pageVal instanceof Uint8Array ? pageVal : pageVal ? new Uint8Array(pageVal) : null;

				if (page) {
					out.set(page.subarray(pageOff, pageOff + take), outOff);
				}
				// missing pages read as zeros

				cur += take;
				outOff += take;
				remaining -= take;
			}

			return out;
		});
	}

	public async writeAt(pos: number, data: Uint8Array): Promise<void> {
		if (pos < 0) pos = 0;
		if (!data || data.length === 0) return;

		const absPos = pos + ScratchpadFile.HEADER_SIZE;

		return this.mutex.run(async () => {
			const st = await this.store('readwrite');

			let cur = absPos;
			let inOff = 0;
			let remaining = data.length;

			while (remaining > 0) {
				const pageIndex = Math.floor(cur / ScratchpadFile.PAGE_SIZE);
				const pageOff = cur % ScratchpadFile.PAGE_SIZE;
				const take = Math.min(remaining, ScratchpadFile.PAGE_SIZE - pageOff);

				const pageVal = await reqToPromise<any>(st.get(this.pageKey(pageIndex)));
				let page: Uint8Array;

				if (pageVal instanceof Uint8Array) page = pageVal;
				else if (pageVal) page = new Uint8Array(pageVal);
				else page = new Uint8Array(ScratchpadFile.PAGE_SIZE);

				page.set(data.subarray(inOff, inOff + take), pageOff);
				await reqToPromise(st.put(page, this.pageKey(pageIndex)));

				cur += take;
				inOff += take;
				remaining -= take;
			}

			const newEnd = pos + data.length;
			const oldLenVal = await reqToPromise<any>(st.get(this.metaKey('len')));
			const oldLen = typeof oldLenVal === 'number' ? oldLenVal : 0;
			if (newEnd > oldLen) {
				await reqToPromise(st.put(newEnd, this.metaKey('len')));
			}
		});
	}

	public async clear(): Promise<void> {
		return this.mutex.run(async () => {
			const st = await this.store('readwrite');
			await reqToPromise(st.clear());
			await reqToPromise(st.put(0, this.metaKey('len')));
		});
	}
}
