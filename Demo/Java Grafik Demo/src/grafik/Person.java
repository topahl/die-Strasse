package grafik;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Person extends Entity {

	private int posX,posY, wpX, wpY;
	private BufferedImage sprite;
	
	
	public Person(int x, int y, String src) {
		this.posX=x;
		this.wpX=x;
		this.posY=y; 
		this.wpY=y;
		//TP Preload Images
		try {
			sprite = ImageIO.read(new File(src));
		} catch (IOException e) {
			System.err.println("Could not find sprite in location: "+src);
			e.printStackTrace();
		}
		height=45;
		width=45;
	}
	
	public Person(int x, int y, BufferedImage sprite) {
		this.posX=x;
		this.wpX=x;
		this.posY=y; 
		this.wpY=y;
		this.sprite = sprite;
		height=45;
		width=45;
	}
	
	
	public int getX() {
		return posX;
	}
	public int getY() {
		return posY;
	}
	public int getWpX() {
		return wpX;
	}
	public void setWpX(int wpX) {
		this.wpX = wpX;
	}
	public int getWpY() {
		return wpY;
	}
	public void setWpY(int wpY) {
		this.wpY = wpY;
	}
	@Override
	public BufferedImage getGraphic() {
		if(posX > wpX){
			return sprite.getSubimage(45, 0, 45, 45);
		} 
		else{
			if(posX < wpX){
				return sprite.getSubimage(45, 45, 45, 45);
			}
			else{
				if(posY >wpY){
					return sprite.getSubimage(0, 45, 45, 45);
				}
				return sprite.getSubimage(0, 0, 45, 45);
			}
		}
		
		
	}
	public void run(){
		while(true){
			if(posX > wpX){
				posX--;
			} 
			else{
				if(posX < wpX){
					posX++;
				}
				else{
					if(posY >wpY){
						posY--;
					}
					else{
						if (posY<wpY){
							posY++;
						}
					}
				}
			}
			try {
				sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
