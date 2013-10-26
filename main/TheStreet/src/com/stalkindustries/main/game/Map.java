package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Map extends JLabel{
	private int width;
	private int height;
	BufferedImage karte;	
	
	public Map(String name, int agentenhaus){
		try {
			karte= ImageIO.read(new File("res\\level\\"+name+"\\"+name+"_map.png"));
		} catch (IOException e) {
			System.err.println("Could not find Map file:"+name+".png");
			e.printStackTrace();
		}
		
		height=karte.getHeight();
		width=karte.getWidth();
		
		//Beschwerden Miri
		//Hausnummern zeichnen
		Graphics2D g2d = this.karte.createGraphics();
		for(int i=0;i<Ressources.NUMBERHOUSES;i++){
			for(int j=1;j<Ressources.getLocation_ids().size()-1;j++){
				for(int k=1;k<Ressources.getLocation_ids().get(j).size()-1;k++){
					if(Ressources.getLocation_ids().get(j).get(k).charAt(0) ==("" + (i+1)).charAt(0)){	
						if(Ressources.getLocation_ids().get(j+1).get(k).charAt(0) == ("" + (i+1)).charAt(0) && Ressources.getLocation_ids().get(j-1).get(k).charAt(0) == ("" + (i+1)).charAt(0) && Ressources.getLocation_ids().get(j).get(k+1).charAt(0) == ("" + (i+1)).charAt(0) && Ressources.getLocation_ids().get(j).get(k-1).charAt(0) == ("" + (i+1)).charAt(0)) {
							if(i == agentenhaus){
								g2d.drawImage(Ressources.zahlen.getSubimage(10*Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),k*Ressources.RASTERHEIGHT,j*Ressources.RASTERHEIGHT, null);
							}
							else
								g2d.drawImage(Ressources.zahlen.getSubimage((i+1)*Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),k*Ressources.RASTERHEIGHT,j*Ressources.RASTERHEIGHT, null);
						}
					}
				}
			}
		}
		
		setIcon(new ImageIcon(karte));
		setSize(height,width);
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
