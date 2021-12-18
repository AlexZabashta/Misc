ffmpeg -y -ss 00:04:36 -to 00:10:32 -i %1 -af "loudnorm" -ab 256k -ar 48000 -c:v copy output.mp4
