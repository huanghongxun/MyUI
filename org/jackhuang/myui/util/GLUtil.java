/**
 * MyUI Mod is under MIT License.
 * READ THE LICENSE FIRST.
 * @author huangyuhui
 */
package org.jackhuang.myui.util;

public class GLUtil {

	public static int loadTexture(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0,
				image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()
				* image.getHeight() * 4); // 4 for RGBA, 3 for RGB

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component.
															// Only for RGBA
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

		// You now have a ByteBuffer filled with the color data of each pixel.
		// Now just create a texture ID and bind it. Then you can load it using
		// whatever OpenGL method you want, for example:

		int textureID = glGenTextures(); // Generate texture ID
		glBindTexture(GL_TEXTURE_2D, textureID); // Bind texture ID

		// Setup wrap mode
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		// Setup texture scaling filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Send texel data to OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(),
				image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		// Return the texture ID so we can bind it later again
		return textureID;
	}

	public static BufferedImage loadImage(String loc) {

		try {
			File f = new File(loc).getAbsoluteFile();
			//System.out.println("F: " + f.getPath());
			return ImageIO.read(f);
		} catch (IOException e) {
		}
		return null;
	}
	
	public static void drawTexture(int x1, int y1, int x2, int y2) {
    	GL11.glColor3f(255, 255, 255);
    	GL11.glBegin(GL11.GL_QUADS);
    	GL11.glTexCoord2d(0, 0);
    	GL11.glVertex2i(x1, y1); 
    	GL11.glTexCoord2d(1, 0);  
    	GL11.glVertex2i(x1, y2); 
    	GL11.glTexCoord2d(1, 1);  
    	GL11.glVertex2i(x2, y2);
    	GL11.glTexCoord2d(0, 1);  
    	GL11.glVertex2i(x2, y1);
    	GL11.glEnd();
	}

}
