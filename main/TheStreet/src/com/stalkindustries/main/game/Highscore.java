package com.stalkindustries.main.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Highscore {
	private double highscore = 0;
	private double wissenswert = 0;
	private double events = 0;
	private double misstrauen_max = 0;
	private Simulation simulation;
	private Quiz quiz;
	private Agent agent;
	private int spielzeit;					//in Minuten
	
	
	//Beschwerden Miri
	public Highscore(Simulation simulation, Quiz quiz, Agent agent){
		this.simulation = simulation;		//gebraucht für Misstrauen, Spielzeit und Überwachungsstatus
		this.quiz = quiz;					//gebraucht für Beantwortungen der Fragen
		this.agent = agent;
		
		//set Spielzeit
		int min = this.simulation.getSpiel_minute();
		int h = this.simulation.getSpiel_stunde();
		int tag = this.simulation.getSpiel_tag();
		this.spielzeit = min + h*60 + tag*24*60;

		this.misstrauen_max = this.simulation.getMisstrauenMax();
	}
	
	//Beschwerden Miri
	public void calcHighscoreOfAgent(){
		double zeit = 0;
		
		//calc Wissenswert
		double tmp = 0;
		ArrayList<Integer> fragen = this.quiz.getBeantwortete();
		for(int i=0;i<fragen.size();i++){
			tmp = tmp + fragen.get(i);
		}
		this.wissenswert = tmp/fragen.size();		//zwischen 0 und 100
		
		this.highscore = 1000/this.misstrauen_max + this.wissenswert + zeit + this.events;
	}
	
	//Beschwerden Miri
	public ArrayList<String> getFile(String dateiName){
		ArrayList<String> file = new ArrayList<String>(); 
		try {
            BufferedReader in = new BufferedReader(new FileReader(dateiName));
            String zeile = null;
            while ((zeile = in.readLine()) != null) {
            	file.add(zeile);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		return file;
	}
	
	//Beschwerden Miri
	public void exportIntoScores(){
		ArrayList<String> file = this.getFile("res\\user\\scores.bnd");
		
	}
	
	//Beschwerden Miri
	public void exportIntoUser(){
		ArrayList<String> file = this.getFile("res\\user\\"+this.agent.getName()+".usr");
		
	}

	//Beschwerden Miri
	public void setHighscore(double highscore){
		this.highscore = highscore;
	}
	
	//Beschwerden Miri
	public double getHighscore(){
		return this.highscore;
	}
}
