package com.stalkindustries.main.game;

public class Highscore {
	private double highscore;
	private double wissenswert;
	private Simulation simulation;
	private Quiz quiz;
	private Agent agent;
	
	
	//Beschwerden Miri
	public Highscore(Simulation simulation, Quiz quiz, Agent agent){
		this.simulation = simulation;		//gebraucht f�r Misstrauen, Spielzeit und �berwachungsstatus
		this.quiz = quiz;					//gebraucht f�r Beantwortungen der Fragen
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
