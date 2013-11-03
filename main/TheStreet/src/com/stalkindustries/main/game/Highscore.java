package com.stalkindustries.main.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Highscore {
	private double highscore = 0d;
	private double wissenswert = 0d;
	private int events = 0;
	private double misstrauen_max = 0d;
	private Simulation simulation;
	private Quiz quiz;
	private Agent agent;
	private int spielzeit;					//in Minuten
	private boolean festgenommen = false;
	
	
	//Beschwerden Miri
	public Highscore(Simulation simulation, Quiz quiz, Agent agent){
		this.simulation = simulation;		//gebraucht für Misstrauen, Spielzeit und Überwachungsstatus
		this.quiz = quiz;					//gebraucht für Beantwortungen der Fragen
		this.agent = agent;
		
		//set Spielzeit
		int min = this.simulation.getSpiel_minute();
		int h = this.simulation.getSpiel_stunde();
		int tag = this.simulation.getSpiel_tag();
		this.spielzeit = min + h*60 + (tag-1)*24*60 - 420;//-420, weil wir um 7 Uhr morgens anfangen

		this.misstrauen_max = this.simulation.getMisstrauenMax();
		
		//Events
		for(int i=0;i<this.simulation.get_people().size();i++){
			if(this.simulation.get_people().get(i).getEvent().size() == 4)
				this.events++;
		}
	}
	
	//Beschwerden Miri
	public void calcHighscoreOfAgent(){
		double zeit = 0;
		if(this.spielzeit != 0)
			zeit = 10000000/this.spielzeit;
		
		//calc Wissenswert
		double tmp = 0;
		ArrayList<Integer> fragen = this.quiz.getBeantwortete();
		for(int i=0;i<fragen.size();i++){
			tmp = tmp + fragen.get(i);
		}
		if(fragen.size() != 0){
			this.wissenswert = tmp/fragen.size();		//zwischen 0 und 100
		}
		
		double misstrauen=0;
		if(this.misstrauen_max!=0)
			misstrauen = 1000/this.misstrauen_max;
		
		//this.highscore = misstrauen + this.wissenswert + zeit + this.events;
		this.highscore = this.wissenswert - this.misstrauen_max - zeit/8640 + this.events/this.simulation.get_people().size();
		
		if(this.festgenommen)
			this.highscore *= 2;
		
		if(this.highscore < 0)
			this.highscore = 0;
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
		Writer fw = null;
	     try
	     {
	    	 fw = new FileWriter("res\\user\\scores.bnd");
	    	 for(int i=0;i<file.size();i++){
	  	       fw.write(file.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 fw.write(this.agent.getName()+": "+this.highscore);
	    	 fw.append( System.getProperty("line.separator") ); 
	    	 fw.close();
	     }
	     catch ( IOException e ) {
	       System.err.println( "Konnte Datei nicht erstellen" );
	     }
	     finally {
	       if ( fw != null )
	         try { fw.close(); } catch ( IOException e ) { e.printStackTrace(); }
	     }
	}
	
	//Beschwerden Miri
	public void exportIntoUser(){
		ArrayList<String> file = this.getFile("res\\user\\"+this.agent.getName()+".usr");
		  
	     Writer fw = null;
	     try
	     {
	    	 fw = new FileWriter("res\\user\\"+this.agent.getName()+".usr");
	    	 for(int i=0;i<file.size();i++){
	  	       fw.write(file.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 fw.write("Highscore: "+this.highscore);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Maximales Misstrauen: "+this.misstrauen_max);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Spielzeit in Minuten: "+this.spielzeit);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Wissenswert: "+this.wissenswert);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Anzahl entdeckter Events: "+this.events+" von "+this.simulation.get_people().size());
	    	 fw.append( System.getProperty("line.separator") ); 
	    	 fw.append( System.getProperty("line.separator") ); 
	    	 fw.close();
	     }
	     catch ( IOException e ) {
	       System.err.println( "Konnte Datei nicht erstellen" );
	     }
	     finally {
	       if ( fw != null )
	         try { fw.close(); } catch ( IOException e ) { e.printStackTrace(); }
	     }
	}

	//Beschwerden Miri
	public void setHighscore(double highscore){
		this.highscore = highscore;
	}
	
	//Beschwerden Miri
	public double getHighscore(){
		return this.highscore;
	}
	
	
	//Beschwerden Miri
	public void setFestgenommen(boolean b){
		this.festgenommen = b;
	}
}
