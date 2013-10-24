package com.stalkindustries.main.game;
import java.util.ArrayList;

public class Haus {
	private float ueberwachungsstatus;
	private int posX;
	private int posY;
	private int hausnummer;
	private ArrayList<String> ueberwachungsmodule = new ArrayList();
	private ArrayList<String> boese_events = new ArrayList();
	private boolean agentenhaus;
	public static final int MAXUBERWACHUNGSMODULE = 2;
	
	
	public Haus(int hausnr, boolean agentenhaus, int posX, int posY){
		this.hausnummer = hausnr;
		this.ueberwachungsstatus = 0;
		this.posX = posX;
		this.posY = posY;
		this.agentenhaus = agentenhaus;
		//TODO böse Events
	}
	
	public float getUeberwachungsstatus(){
		return this.ueberwachungsstatus;
	}
	
	public void setUeberwachungsstatus(float u){
		this.ueberwachungsstatus = u;
	}
	
	public ArrayList<String> getUeberwachungsmodule(){
		return this.ueberwachungsmodule;
	}

}
