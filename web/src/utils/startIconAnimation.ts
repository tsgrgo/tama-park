export async function startIconAnimation(urls: string[], speed: number) {
	const dataUrls = await Promise.all(urls.map(toDataUrl));

	let iconIndex = 0;

	const link = getOrCreateFaviconLink();
	link.type = 'image/png';

	link.href = dataUrls[iconIndex++ % dataUrls.length];

	setInterval(() => {
		link.href = dataUrls[iconIndex++ % dataUrls.length];
	}, speed);
}

function getOrCreateFaviconLink(): HTMLLinkElement {
	const existing = document.querySelector<HTMLLinkElement>('link[rel="icon"]');
	if (existing) return existing;

	const link = document.createElement('link');
	link.rel = 'icon';
	document.head.appendChild(link);
	return link;
}

async function toDataUrl(url: string): Promise<string> {
	const res = await fetch(url, { cache: 'force-cache' });
	if (!res.ok) throw new Error(`Failed to load favicon: ${url}`);

	const blob = await res.blob();
	return await new Promise<string>((resolve, reject) => {
		const reader = new FileReader();
		// eslint-disable-next-line @typescript-eslint/no-base-to-string
		reader.onload = () => resolve(String(reader.result));
		// eslint-disable-next-line @typescript-eslint/prefer-promise-reject-errors
		reader.onerror = () => reject(reader.error);
		reader.readAsDataURL(blob);
	});
}
