package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Person extends Mensch {
	private int id;
	protected int[] aussehen;
	private String name;
	private double misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	//private float bewegungsgeschwindigkeit;
	protected int geschlecht; //1=male, 2=female
	private int zeitverzogerung; // in Minuten
	private int haus_id;
	
	static private int last_id=-1;
	
	public Person(){
		
	}
	
	
	public Person(int house_id){
		last_id++;
		
		this.id = last_id;
		this.set_location_id('0');
		//TODO: set_name
		this.setLocation(0, 0);
		this.misstrauen = 0;
		//TODO: set Bewegungsgeschwindigkeit
		this.geschlecht = (int)Math.random()*(2)+1;
		this.zeitverzogerung = (int)Math.random()*(60)+1;
		this.haus_id = house_id;
	}
	
	private void initialize_misstrauen(){
		this.misstrauen = 0;
	}
	
	public double get_misstrauen(){
		return this.misstrauen;
	}
	
	public void set_misstrauen(double misstrauen){
		this.misstrauen = misstrauen;
	}
	
	public int get_haus_id(){
		return this.haus_id;
	}
	
	public void set_haus_id(int haus_id){
		this.haus_id = haus_id;
	}


	public int getZeitverzogerung() {
		return zeitverzogerung;
	}


	public void setZeitverzogerung(int zeitverzogerung) {
		this.zeitverzogerung = zeitverzogerung;
	}

}
