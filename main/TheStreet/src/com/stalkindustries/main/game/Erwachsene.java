package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Erwachsene extends Person{
	
	public Erwachsene(){
		
	}
	
	public Erwachsene(int house_id){
		super(house_id);
		
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*4+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		sprite = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		g2d.drawImage(adults.getSubimage(90*aussehen[0], 0, 90, 90) ,0,0 , null); //Körperbau
		g2d.drawImage(adults.getSubimage(90*aussehen[0]+360, (aussehen[1]-1)*90, 90, 90),0,0, null); //Hautfarbe
		g2d.drawImage(adults.getSubimage(900, (aussehen[2]-1)*90, 90, 90),0,0, null); //Frisur
		//TODO Geschlecht einbauen
		//TODO arbeit oder nicht
	}
	
	
	@Override
	public BufferedImage paint() {
		switch(currentMove){
		case 'l':
			return sprite.getSubimage(45, 45, 45, 45);
		case 'r':
			return sprite.getSubimage(0, 45, 45, 45);
		case 'o':
			return sprite.getSubimage(45, 0, 45, 45);
		case 'u':
			return sprite.getSubimage(0, 0, 45, 45);
		}
		return sprite.getSubimage(0, 0, 45, 45);
	}
}
