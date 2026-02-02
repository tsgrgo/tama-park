export function stringConstructor(data: AllowSharedBufferSource): string {
	return new TextDecoder('Shift_JIS').decode(data);
}
