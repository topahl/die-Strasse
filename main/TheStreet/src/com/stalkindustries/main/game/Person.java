package com.stalkindustries.main.game;

public class Person extends Mensch {
	private int id;
	private int location_id;
	//private Aussehen aussehen;
	private String name;
	// private Position position;
	private float misstrauen; 	//-100=nicht misstrauisch, 100=ultra misstrauisch, 200=initial
	//private float bewegungsgeschwindigkeit;
	private char geschlecht; //m=male, f=female
	//private float zeitverzogerung;
	private int haus_id;

	
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
