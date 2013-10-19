package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {
	private int width;
	private int height;
	BufferedImage karte;
	
	
	public Map(String name){
		try {
			karte= ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\"+name+".png"));
		} catch (IOException e) {
			System.err.println("Could not find Map file:"+name+".png");
			e.printStackTrace();
		}
		height=karte.getHeight();
		width=karte.getWidth();
		//TODO Hausnummern zeichnen
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public BufferedImage getImage(){
		return karte;
	}
	
}
