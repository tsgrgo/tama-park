$inputPath  = Join-Path -Path $PSScriptRoot -ChildPath "ENGLISH_PATCH__Tamagotchi_Park.sp"
$bytes = [System.IO.File]::ReadAllBytes($inputPath)

# Strip the 64 byte DoJa scratchpad header + 128 game data header
$payload = $bytes[192..85341]

$chunkSize = 10240
$i = 0

for ($offset = 0; $offset -lt $payload.Length; $offset += $chunkSize) {
    $end = [Math]::Min($offset + $chunkSize - 1, $payload.Length - 1)
    $chunk = $payload[$offset..$end]

    $outPath = Join-Path -Path $PSScriptRoot -ChildPath ("{0}.bin" -f $i)

    [System.IO.File]::WriteAllBytes($outPath, $chunk)
    $i++
}
