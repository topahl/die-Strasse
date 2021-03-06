package com.stalkindustries.main.game;
import java.util.ArrayList;

public class Haus {
	private float ueberwachungsstatus;
	private int posX;
	private int posY;
	private int hausnummer;
	private float ueberwachungsWerte[] = new float[6];
	private ArrayList<String> ueberwachungsmodule = new ArrayList();
	private ArrayList<String> boeseEvents = new ArrayList();	//TODO: entfernen
	private boolean agentenhaus;
	public static final int MAXUBERWACHUNGSMODULE = 6;
	
	
	public Haus(int hausnr, boolean agentenhaus, int posX, int posY){
		this.hausnummer = hausnr;
		this.ueberwachungsstatus = 0;
		this.posX = posX;
		this.posY = posY;
		this.agentenhaus = agentenhaus;
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
	
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}

	public float getUeberwachungsWert(int index) {
		return ueberwachungsWerte[index];
	}

	public void setUeberwachungsWert(float ueberwachungsWert, int index) {
		this.ueberwachungsWerte[index] = ueberwachungsWert;
	}

}
