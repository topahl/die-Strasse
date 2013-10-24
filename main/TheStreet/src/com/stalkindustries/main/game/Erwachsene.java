package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Erwachsene extends Person{
	
	private boolean hat_arbeit;	
	public Erwachsene(int house_id){
		
		super(house_id);
		setSize(45, 45);
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*4+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		temp_sprite = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = temp_sprite.createGraphics();
		g2d.drawImage(adults.getSubimage(90*aussehen[0], 0, 90, 90) ,0,0 , null); //Körperbau
		g2d.drawImage(adults.getSubimage(90*aussehen[0]+360, (aussehen[1]-1)*90, 90, 90),0,0, null); //Hautfarbe
		g2d.drawImage(adults.getSubimage(900, (aussehen[2]-1)*90, 90, 90),0,0, null); //Frisur
		//TODO Geschlecht einbauen
		//TODO arbeit oder nicht
		
		update_schatten();
		step();
	}


	public boolean isHat_arbeit() {
		return hat_arbeit;
	}

	public void setHat_arbeit(boolean hat_arbeit) {
		this.hat_arbeit = hat_arbeit;
	}
}
