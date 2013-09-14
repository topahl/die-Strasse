package grafik;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {
	private BufferedImage karte;
	
	public BufferedImage getImage() {
		return karte;
	}

	public Map(String src){
		try {
			karte = ImageIO.read(new File(src));
		} catch (IOException e) {
			System.err.println("Could not find Map in location: "+src);
			e.printStackTrace();
		}
	}
}
