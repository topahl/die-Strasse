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
	private String name;
	protected BufferedImage temp_sprite; //Sprite ohne Schatten
	private double misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	protected int geschlecht; //1=male, 2=female
	private int zeitverzogerung; // in Minuten
	private int durchgefuehrteBeschwichtigungen[] = new int[Ressources.NUMBERBESCHWICHTIGENACTIONS];//zum Mitzählen, weil 20 Kuchen am Tag, doch wieder misstrauisch machen
	
	static private int last_id=-1;
	
	public Person(){
		
	} //TODO erntfernen
	
	public void update_schatten(){
		misstrauen=Math.random()*200-100; //TODO entfernen
		sprite = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = sprite.createGraphics();
		if(misstrauen==0)
			g2d.setColor(new Color(1f,1f,1f,0.2f));
		else if(misstrauen>0)
			g2d.setColor(new Color((float)(misstrauen/100),0f,0f,0.2f));
		else
			g2d.setColor(new Color(0f,(float)(misstrauen/(-100)),0f,0.2f));
		
		g2d.fillOval(0, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		g2d.fillOval(Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		g2d.fillOval(0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		g2d.fillOval(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
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
		System.out.println(geschlecht);
		this.zeitverzogerung = (int)(Math.random()*(60))+1;
		this.haus_id = house_id;
				
		//TODO
		//Person einen Namen geben
		
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
	
	public void set_durchgefuehrteBeschwichtigungen(int index){
		this.durchgefuehrteBeschwichtigungen[index]++;
	}

}
