package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Person extends Mensch {
	private int id;
	protected int[] aussehen;
	protected BufferedImage temp_sprite; //Sprite ohne Schatten
	private double misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	protected int geschlecht; //1=male, 2=female
	private int zeitverzogerung; // in Minuten
	private int durchgefuehrteBeschwichtigungen[] = new int[Ressources.NUMBERBESCHWICHTIGENACTIONS];//zum Mitzählen, weil 20 Kuchen am Tag, doch wieder misstrauisch machen
	
	static private int last_id=-1;
		
	
	public abstract void farbeZeigen(boolean farbeZeigen);
	
	
	public void update_schatten(){
		sprite = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		if(misstrauen==0)
			g2d.setColor(new Color(1f,1f,1f,0.2f));
		else if(misstrauen>0)
			g2d.setColor(new Color(1f,(float)(1-(Math.sqrt(misstrauen/100))),(float)(1-(Math.sqrt(misstrauen/100))),0.2f));
		else
			g2d.setColor(new Color((float)(1-(Math.sqrt(-misstrauen/100))),1f,(float)(1-(Math.sqrt(-misstrauen/100))),0.2f));
		
		g2d.fillOval(3, 3, Ressources.RASTERHEIGHT-6, Ressources.RASTERHEIGHT-6);
		g2d.fillOval(3+Ressources.RASTERHEIGHT, 3, Ressources.RASTERHEIGHT-6, Ressources.RASTERHEIGHT-6);
		g2d.fillOval(3, 3+Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT-6, Ressources.RASTERHEIGHT-6);
		g2d.fillOval(3+Ressources.RASTERHEIGHT,3+Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT-6, Ressources.RASTERHEIGHT-6);
		g2d.drawImage(temp_sprite, 0, 0,null);
	}
	
	public Person(int house_id){
		last_id++;
		
		this.id = last_id;
		this.set_location_id('0');
		this.setLocation(0, 0);
		this.misstrauen = 0;
		
		//Bewegunsggeschwindigkeit
		int zufall = (int)(Math.random()*(10))+1;
		if(zufall > 7)
			this.bewegungsgeschwindigkeit = 5;
		else
			this.bewegungsgeschwindigkeit = 3;
		
		this.geschlecht = (int)(Math.random()*(2)+1);
		this.zeitverzogerung = (int)(Math.random()*(60))+1;
		this.haus_id = house_id;
				

		//Person einen Namen geben
		//this.name = Ressources.getNames().get((int)(Math.random()*Ressources.getNames().size())).get(this.geschlecht-1);	
		this.setName(Ressources.getNames().get((int)(Math.random()*Ressources.getNames().size())).get(this.geschlecht-1));
		//System.out.println(this.name);
	}
	
	public double get_misstrauen(){
		return this.misstrauen;
	}
	
	public void set_misstrauen(double misstrauen){
		this.misstrauen = misstrauen;
	}
 

	public int getZeitverzogerung() {
		return zeitverzogerung;
	}


	public void setZeitverzogerung(int zeitverzogerung) {
		this.zeitverzogerung = zeitverzogerung;
	}
	
	public int get_durchgefuehrteBeschwichtigungen(int index){
		return this.durchgefuehrteBeschwichtigungen[index];
	}
	
	public void erhoehe_durchgefuehrteBeschwichtigungen(int index){
		this.durchgefuehrteBeschwichtigungen[index]++;
	}
	
	
	public int getGeschlecht(){
		return this.geschlecht;
	}

}
