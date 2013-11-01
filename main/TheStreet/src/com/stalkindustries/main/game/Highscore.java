package com.stalkindustries.main.game;

public class Highscore {
	private double highscore;
	private double wissenswert;
	private Simulation simulation;
	private Quiz quiz;
	private Agent agent;
	
	
	//Beschwerden Miri
	public Highscore(Simulation simulation, Quiz quiz, Agent agent){
		this.simulation = simulation;		//gebraucht für Misstrauen, Spielzeit und Überwachungsstatus
		this.quiz = quiz;					//gebraucht für Beantwortungen der Fragen
		this.agent = agent;
	}
	
	//Beschwerden Miri
	public void calcHighscoreOfAgent(){
		
	}
	
	//Beschwerden Miri
	public void exportIntoScores(){
		
	}
	
	//Beschwerden Miri
	public void exportIntoUser(){
		
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
