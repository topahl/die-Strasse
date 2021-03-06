package com.stalkindustries.main.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Miriam
 */

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
	 * Konstruktor von Highscore
	 * @author Miriam
	 */
	public Highscore(Simulation simulation, Quiz quiz, Agent agent, String levelname){
		this.simulation = simulation;		//gebraucht f�r Misstrauen, Spielzeit und �berwachungsstatus
		this.quiz = quiz;					//gebraucht f�r Beantwortungen der Fragen
		this.agent = agent;
		this.levelname = levelname;
	}
	
	/**
	 * den Highscrore des aktuellen Benutzers berechnen
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
        
        //Anzahl der entdeckten Events berechnen
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
        
        //wenn man den Schwerverbrecher festgenommen hat, dann bekommt man einen Bonus
        //dieser Bonus wird aber mit der Zeit immer geringer
        if(this.festgenommen)
        	if(this.spielzeit/8640 <= 1)
        		this.highscore += 300;
        	else
        		this.highscore += 300-70*Math.log(this.spielzeit/8640);
        
        //Werte auf 2 Nachkommastellen runden
        this.highscore = Double.valueOf(Math.round(this.highscore*100)/100.00);
        this.misstrauenMax = Double.valueOf(Math.round(this.misstrauenMax*100)/100.00);
        this.wissenswert = Double.valueOf(Math.round(this.wissenswert*100)/100.00);
        
        //wenn total schlecht war und einen negativen Highscore erreicht hat, dann wird er 
        //automatisch auf 0 gesetzt, da 0 unser kleinster Highscore ist
        if(this.highscore < 0)
            this.highscore = 0;
    }
	
    /**
     * zum Einlesen des angegebenen Files
     * entweder die Score-Dateien, oder die User-Dateien
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
	 * Export der Highscoredaten in die Scoredateien der einzelnen Level
	 * @author Miriam
	 */
	public void exportIntoScores(){
		//f�r den Fall, dass der Dateipfad und die Datei noch nicht existieren, werden sie hier neu erstellt
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
		ArrayList<String> filearray = this.getFile(Ressources.HOMEDIR+"res\\user\\"+this.levelname+".bnd"); //Daten aus Datei holen, falls sie schon existiert
		Writer fw = null;
	     try
	     {
	    	 fw = new FileWriter(Ressources.HOMEDIR+"res\\user\\"+this.levelname+".bnd");
	    	 //alte Daten in Datei schreiben
	    	 for(int i=0;i<filearray.size();i++){
	  	       fw.write(filearray.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 
	    	 //neue Daten anh�ngen
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
	 * Benutzerspezifische Auswertung erstellen oder erweitern
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
	    	 //alte Daten in Datei schreiben
	    	 for(int i=0;i<file.size();i++){
	  	       fw.write(file.get(i));
		       fw.append( System.getProperty("line.separator") ); 
	    	 }
	    	 
	    	 //neue Daten anh�ngen
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
