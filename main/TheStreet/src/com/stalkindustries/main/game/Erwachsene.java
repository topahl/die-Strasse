package com.stalkindustries.main.game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Erwachsene extends Person{
	
	private boolean hatArbeit;	
	public Erwachsene(int house_id,ArrayList<String> event){
		super(house_id,event);
		this.hausId=house_id;
		
		int tmp = (int)(Math.random()*3+1);
		if(tmp == 3)
			this.hatArbeit = false;
		else
			this.hatArbeit = true;
		
		setSize(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);	
		aussehen = new int[3];
		aussehen[0]= (int)(Math.random()*Ressources.TORSOCNT+1); //Körperbau
		aussehen[1]= (int)(Math.random()*10+1); //Hautfarbe
		aussehen[2]= (int)(Math.random()*10+1); //Frisur
		
		farbeZeigen();
		}

	
	/**
	 * @author Sven
	 */
	public void farbeZeigen (){
		tempSprite = new BufferedImage(Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempSprite.createGraphics();
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0], (this.hausId+1)*Ressources.RASTERHEIGHT*2*(istFarbig?1:0), Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2) ,0,0 , null); //Körperbau
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*aussehen[0]+Ressources.RASTERHEIGHT*Ressources.TORSOCNT*2, (aussehen[1]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Hautfarbe
		g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*Ressources.TORSOCNT*4+geschlecht*Ressources.RASTERHEIGHT*2, (aussehen[2]-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Frisur
		if(hatArbeit && istFarbig==true)
			g2d.drawImage(adults.getSubimage(Ressources.RASTERHEIGHT*2*11, (geschlecht-1)*Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2, Ressources.RASTERHEIGHT*2),0,0, null); //Krawate
		
		updateSchatten();
		step();
	}
	
	
	public boolean isHatArbeit() {
		return hatArbeit;
	}
}
