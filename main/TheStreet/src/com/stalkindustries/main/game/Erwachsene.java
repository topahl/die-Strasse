package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Erwachsene extends Person{
	
	private boolean hat_arbeit;	
	public Erwachsene(int house_id,ArrayList<String> event){
		super(house_id,event);
		this.haus_id=house_id;
		
		//hat Arbeit?
		int tmp = (int)(Math.random()*3+1);
		if(tmp == 3)
			this.hat_arbeit = false;
		else
			this.hat_arbeit = true;
		
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);		//Sven weis jetzt wie das funktioniert
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*Ressources.TORSOCNT+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		
		farbeZeigen(false);
		}

	public void farbeZeigen (boolean farbeZeigen){		//wenn farbeZeigen==1 dann wird Sie gezeigt sonst muss farbeZeigen ==0 sein. 
													//Fragen an Sven
		temp_sprite = new BufferedImage(Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = temp_sprite.createGraphics();
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0], (this.haus_id+1)*Ressources.RASTERHEIGHT*2*(farbeZeigen?1:0), Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2) ,0,0 , null); //Körperbau
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0]+Ressources.RASTERHEIGHT*Ressources.TORSOCNT*2, (aussehen[1]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Hautfarbe
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*Ressources.TORSOCNT*4+geschlecht*Ressources.RASTERHEIGHT*2, (aussehen[2]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Frisur
		if(hat_arbeit && farbeZeigen==true)
			g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*11, (geschlecht-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Krawate
		
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
