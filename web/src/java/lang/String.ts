const SHIFT_JIS = 'Shift_JIS';

export function stringConstructor(
	data: AllowSharedBufferSource,
	encoding = SHIFT_JIS
): string {
	return new TextDecoder(encoding).decode(data);
}
