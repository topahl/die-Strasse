package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;

public class Agent extends Mensch { 

	public Agent(int house_id){
		this.haus_id = house_id;
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		sprite=adults.getSubimage(0, 0, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2);
		step();
	}
}
