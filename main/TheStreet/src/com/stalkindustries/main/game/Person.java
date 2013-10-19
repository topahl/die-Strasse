package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Person extends Mensch {
	private int id;
	private int location_id;
	protected int[] aussehen;
	private String name;
	// private Position position;
	private float misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	//private float bewegungsgeschwindigkeit;
	protected int geschlecht; //1=male, 2=female
	//private float zeitverzogerung;
	private int haus_id;
	protected static BufferedImage adults; //adults slice to save RAM

	
	static{
		 try {
			adults = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\adult.png"));
		} catch (IOException e) {
			System.err.println("Could not find adult.png");
			e.printStackTrace();
		}
	}
	
	private void initialize_misstrauen(){
		this.misstrauen = 0;
	}
	
	public float get_misstrauen(){
		return this.misstrauen;
	}
	
	public void set_misstrauen(float misstrauen){
		this.misstrauen = misstrauen;
	}
	
	public int get_location_id(){
		return this.location_id;
	}
	
	public int get_haus_id(){
		return this.haus_id;
	}
	
	public void set_haus_id(int haus_id){
		this.haus_id = haus_id;
	}

}
