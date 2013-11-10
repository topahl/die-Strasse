
package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Kinder extends Person {
	
	public Kinder(int house_id, ArrayList<String> event){
		super(house_id,event);
		this.hausId=house_id;
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);			//Sven weis jetzt wie das funktioniert
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*Ressources.TORSOCNT+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		tempSprite = new BufferedImage(Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		
		farbeZeigen();
	}
	public void farbeZeigen (){		//wenn farbeZeigen==1 dann wird Sie gezeigt sonst muss farbeZeigen ==0 sein. 
													//Fragen an Sven
		tempSprite = new BufferedImage(Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempSprite.createGraphics();
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0], (this.hausId+1)*Ressources.RASTERHEIGHT*2*(istFarbig?1:0), Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2) ,0,0 , null); //Körperbau
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0]+360, (aussehen[1]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Hautfarbe
		g2d.drawImage(infants.getSubimage(Ressources.RASTERHEIGHT*Ressources.TORSOCNT*4+geschlecht*Ressources.RASTERHEIGHT*2, (aussehen[2]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Frisur
		updateSchatten();
		step();
			
	}
}
