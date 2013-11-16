package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Person extends Mensch {
	private int id;
	protected int[] aussehen;
	protected BufferedImage tempSprite; //Sprite ohne Schatten
	private double misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	protected int geschlecht; //1=male, 2=female
	private int zeitverzoegerung; // in Minuten
	private int durchgefuehrteBeschwichtigungen[] = new int[Ressources.NUMBERBESCHWICHTIGENACTIONS];//zum Mitzählen, weil 20 Kuchen am Tag, doch wieder misstrauisch machen
	protected ArrayList<String> event;
	protected Boolean istFarbig;
	
	static private int lastId=-1;
		
	
	/**
	 * Menschen werden in ihrer Hausfarbe angezeigt
	 * @author Martika
	 */
	public abstract void farbeZeigen();
	
	
	public void updateSchatten(){
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
		g2d.drawImage(tempSprite, 0, 0,null);
	}
	
	public Person(int house_id, ArrayList<String> event){
		lastId++;
		
		this.id = lastId;
		this.setLocationId('0');
		this.setLocation(0, 0);
		this.misstrauen = 0;
		
		//Bewegunsggeschwindigkeit
		int zufall = (int)(Math.random()*(10))+1;
		if(zufall > 7)
			this.bewegungsgeschwindigkeit = 5;
		else
			this.bewegungsgeschwindigkeit = 3;
		
		this.geschlecht = (int)(Math.random()*(2)+1);
		this.zeitverzoegerung = (int)(Math.random()*(59))+1;
		this.hausId = house_id;
		this.event = event;
		this.istFarbig=false;
				

		//Person einen Namen geben
		//this.name = Ressources.getNames().get((int)(Math.random()*Ressources.getNames().size())).get(this.geschlecht-1);	
		this.setName(Ressources.getNames().get((int)(Math.random()*Ressources.getNames().size())).get(this.geschlecht-1));
		//System.out.println(this.name);
	}
	
	public double getMisstrauen(){
		return this.misstrauen;
	}
	
	public void setMisstrauen(double misstrauen){
		this.misstrauen = misstrauen;
	}
 

	public int getZeitverzogerung() {
		return zeitverzoegerung;
	}


	public void setZeitverzogerung(int zeitverzogerung) {
		this.zeitverzoegerung = zeitverzogerung;
	}
	
	public int getDurchgefuehrteBeschwichtigungen(int index){
		return this.durchgefuehrteBeschwichtigungen[index];
	}
	
	public void setDurchgefuehrteBeschwichtigungen(int index, int wert){
		this.durchgefuehrteBeschwichtigungen[index] = wert;
	}
	
	public void erhoeheDurchgefuehrteBeschwichtigungen(int index){
		this.durchgefuehrteBeschwichtigungen[index]++;
	}
	
	
	public int getGeschlecht(){
		return this.geschlecht;
	}
	
	public void addStringToEvent(String s){
		this.event.add(s);
	}
	
	public ArrayList<String> getEvent(){
		return this.event;
	}


	public Boolean getIstFarbig() {
		return istFarbig;
	}


	public void setIstFarbig(Boolean istFarbig) {
		this.istFarbig = istFarbig;
	}

}
