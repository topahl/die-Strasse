package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.util.Stack;

public class Agent extends Mensch { 
	private String mussWuseln;

	public Agent(int house_id, String name){
		this.hausId = house_id;
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		sprite=adults.getSubimage(0, 0, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2);
		step();
		bewegungsgeschwindigkeit=5;
		setMussWuseln("");
		this.setName(name);
	}

	public String getMussWuseln() {
		return mussWuseln;
	}

	public void setMussWuseln(String mussWuseln) {
		this.mussWuseln = mussWuseln;
	}
}
