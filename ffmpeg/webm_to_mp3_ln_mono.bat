chcp 65001
SET inf=%1
SET ouf=%inf:webm=mp3%
ffmpeg -i %inf% -filter:a "loudnorm" -ac 1 -ab 192k -ar 48000 %ouf%
