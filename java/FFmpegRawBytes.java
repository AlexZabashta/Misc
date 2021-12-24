import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class FFmpegRawBytes {

	public static void drawFrame(Graphics2D g, int w, int h, int f, int frame) {
		g.setColor(Color.RED);
		g.drawLine(0, 0, 20, frame);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		int w = 1920;
		int h = 1080;
		int f = 60;

		String cmd = String.format("ffmpeg -f rawvideo -pix_fmt rgb24 -s:v %dx%d -r %d -i - -c:v libx264 output.mp4", w, h, f);

		Process process = Runtime.getRuntime().exec(cmd);

		try (OutputStream stream = process.getOutputStream()) {
			for (int frame = 0; frame < f * 10; frame++) {
				BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

				Graphics2D g = (Graphics2D) image.getGraphics();

				g.setBackground(Color.BLACK);

				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

				drawFrame(g, w, h, f, frame);
				
				byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

				for (int i = 0; i < buffer.length; i += 3) {
					buffer[i + 0] ^= buffer[i + 2];
					buffer[i + 2] ^= buffer[i + 0];
					buffer[i + 0] ^= buffer[i + 2];
				}

				stream.write(buffer);
				stream.flush();

				System.out.println(frame);
			}
		}

		System.out.println(process.waitFor());

	}
}
