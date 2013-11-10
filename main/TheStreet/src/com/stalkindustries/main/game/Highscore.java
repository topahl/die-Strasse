package com.stalkindustries.main.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.stalkindustries.main.menu.Menu;

public class Highscore {
	private double highscore = 0d;
	private double wissenswert = 0d;
	private int events = 0;
	private double misstrauenMax = 0d;
	private Simulation simulation;
	private Quiz quiz;
	private Agent agent;
	private int spielzeit;					//in Minuten
	private boolean festgenommen = false;
	private String levelname;
	
	
	/**
	 * @author Miriam
	 */
	public Highscore(Simulation simulation, Quiz quiz, Agent agent, String levelname){
		this.simulation = simulation;		//gebraucht für Misstrauen, Spielzeit und Überwachungsstatus
		this.quiz = quiz;					//gebraucht für Beantwortungen der Fragen
		this.agent = agent;
		this.levelname = levelname;
	}
	
	/**
	 * @author Miriam
	 */
    public void calcHighscoreOfAgent(){
        //set Spielzeit
                int min = this.simulation.getSpielMinute();
                int h = this.simulation.getSpielStunde();
                int tag = this.simulation.getSpielTag();
                this.spielzeit = min + h*60 + (tag-1)*24*60 - 420;//-420, weil wir um 7 Uhr morgens anfangen
        
        //Misstrauen Max
        this.misstrauenMax = this.simulation.getMisstrauenMax();
        
        //Events
                for(int i=0;i<this.simulation.getPeople().size();i++){
                    if(this.simulation.getPeople().get(i).getEvent().size() == 4)
                        this.events++;
                }
        
        //calc Wissenswert
        double tmp = 0;
        ArrayList<Integer> fragen = this.quiz.getBeantwortete();
        for(int i=0;i<fragen.size();i++){
            tmp = tmp + fragen.get(i);
        }
        if(fragen.size() != 0){
            this.wissenswert = tmp/fragen.size();        //zwischen 0 und 100
        }
        
        this.highscore = this.wissenswert - this.misstrauenMax - this.spielzeit/8640 + 100*this.events/this.simulation.getPeople().size();
        
        if(this.festgenommen)
            this.highscore += 300-70*Math.log(this.spielzeit/8640);
        
        if(this.highscore < 0)
            this.highscore = 0;
    }
	
    /**
	 * @author Miriam
	 */
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
	
	/**
	 * @author Miriam
	 */
	public void exportIntoScores(){
		//für den Fall, dass der Dateipfad und die Datei noch nicht existieren, werden sie hier neu erstellt
			File folder = new File(Ressources.HOMEDIR+"res\\user\\");
			File file = new File(Ressources.HOMEDIR+"res\\user\\"+this.levelname+".bnd");
				
	    	if(!folder.canWrite())
	    		System.err.println("Can't write to user files");
	    	try {
	    		folder.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Error while creating Userfile");
				e.printStackTrace();
			}		
		
	    //Beschreiben der Datei levelname.bnd
		ArrayList<String> filearray = this.getFile(Ressources.HOMEDIR+"res\\user\\"+this.levelname+".bnd");
		Writer fw = null;
	     try
	     {
	    	 fw = new FileWriter(Ressources.HOMEDIR+"res\\user\\"+this.levelname+".bnd");
	    	 for(int i=0;i<filearray.size();i++){
	  	       fw.write(filearray.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 
	    	 fw.write(this.agent.getName()+": "+this.highscore +":");
	    	 String now = new SimpleDateFormat(" dd.MM.yyyy : HH.mm.ss").format(new Date());
	    	 fw.write(now);
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
	
	/**
	 * @author Miriam
	 */
	public void exportIntoUser(){
		ArrayList<String> file = this.getFile(Ressources.HOMEDIR+"res\\user\\"+this.agent.getName()+".usr");
		
		String festnahme="";
		if(this.festgenommen)
			festnahme = "ja";
		else
			festnahme = "nein";
		
	     Writer fw = null;
	     try
	     {
	    	 fw = new FileWriter(Ressources.HOMEDIR+"res\\user\\"+this.agent.getName()+".usr");
	    	 for(int i=0;i<file.size();i++){
	  	       fw.write(file.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 String now = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
	    	 fw.write(now);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Highscore: "+this.highscore);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Terroristen festgenommen: "+festnahme);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Maximales Misstrauen: "+this.misstrauenMax);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Spielzeit in Minuten: "+this.spielzeit);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Wissenswert: "+this.wissenswert);
	    	 fw.append( System.getProperty("line.separator") );
	    	 fw.write("Anzahl entdeckter Events: "+this.events+" von "+this.simulation.getPeople().size());
	    	 fw.append( System.getProperty("line.separator") ); 
	    	 fw.write("Level: "+this.levelname);
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

	/**
	 * @author Miriam
	 */
	public void setHighscore(double highscore){
		this.highscore = highscore;
	}
	
	/**
	 * @author Miriam
	 */
	public double getHighscore(){
		return this.highscore;
	}
	
	
	/**
	 * @author Miriam
	 */
	public void setFestgenommen(boolean b){
		this.festgenommen = b;
	}
}
