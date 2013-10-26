
package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Kinder extends Person {
	
	public Kinder(int house_id){
		super(house_id);
		this.haus_id=house_id;
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);			//Sven weis jetzt wie das funktioniert
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*Ressources.TORSOCNT+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		temp_sprite = new BufferedImage(Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, BufferedImage.TYPE_INT_ARGB);

		farbeZeigen(0);
	}
	private void farbeZeigen (int farbeZeigen){		//wenn farbeZeigen==1 dann wird Sie gezeigt sonst muss farbeZeigen ==0 sein. 
													//Fragen an Sven
		Graphics2D g2d = temp_sprite.createGraphics();
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0], (this.haus_id+1)*Ressources.RASTERHEIGHT*2*farbeZeigen, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2) ,0,0 , null); //Körperbau
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0]+360, (aussehen[1]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Hautfarbe
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*Ressources.TORSOCNT*4+geschlecht*Ressources.RASTERHEIGHT*2, (aussehen[2]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Frisur
		update_schatten();
		step();
			
	}
}
