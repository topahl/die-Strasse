
package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Kinder extends Person {
	
	public Kinder(int house_id){
		super(house_id);
		setSize(45, 45);
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*4+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		sprite = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		g2d.drawImage(infants.getSubimage(90*aussehen[0], 0, 90, 90) ,0,0 , null); //Körperbau
		g2d.drawImage(infants.getSubimage(90*aussehen[0]+360, (aussehen[1]-1)*90, 90, 90),0,0, null); //Hautfarbe
		g2d.drawImage(infants.getSubimage(900, (aussehen[2]-1)*90, 90, 90),0,0, null); //Frisur
		//TODO Geschlecht einbauen
		//TODO arbeit oder nicht
		step();
	}
}
