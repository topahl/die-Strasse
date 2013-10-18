package grafik;

import java.awt.image.BufferedImage;


public abstract class Entity extends Thread {
	public abstract BufferedImage getGraphic();
	public abstract int getX();
	public abstract int getY();
	public int height;
	public int width;
}
