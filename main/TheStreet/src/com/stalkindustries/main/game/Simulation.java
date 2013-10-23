package com.stalkindustries.main.game;
import java.util.ArrayList;
import java.util.Stack;

import com.stalkindustries.main.TheStreet;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	//private int[] location_raster; nicht mehr benötigt
	private ArrayList<Person> people = new ArrayList(); 
	private Agent agent;
	private int spielTag;
	private int spielStunde;
	private int spielMinute;
	
	
	//Beschwerden an Miri
	void initialize_beziehungsmatrix(){
		int tmp;
		for(int i=0;i<this.people.size();i++)
			for(int j=0;j<this.people.size();i++)
				this.beziehungsmatrix[i][j] = 0;
			
		for(int i=0;i<this.people.size();i++){
			for(int j=i+1;j<this.people.size();j++){
				tmp = (int)Math.random()*(10)+1;
				this.beziehungsmatrix[i][j] = tmp;
				this.beziehungsmatrix[j][i] = tmp;
				if(this.people.get(i).get_haus_id() == this.people.get(j).get_haus_id()){ //Person in einem Haushalt sind besser miteinander befreundet
					tmp = (10-tmp)/2;
					this.beziehungsmatrix[i][j] += tmp;
					this.beziehungsmatrix[j][i] += tmp;
				}
			}
		}
	}
	
	
	/*//Beschwerden an Miri
	void initialize_location_raster(){
		this.location_raster = new int[this.people.size()];
		for(int i=0;i<this.people.size();i++){
			this.location_raster[i] = 42;
		}
	}*/
	
	
	//Beschwerden an Sven und Miri
	void calculate_misstrauen(){
		int z=0;
		boolean b;
		if(this.people.get(z) instanceof Erwachsene){b = ((Erwachsene)people.get(z)).isHat_arbeit();}
		
		
		double[] misstrauen = new double[people.size()];
		double faktor = 0.01275; //kommt noch darauf an, wie häufig die Methode aufgerufen wird
		for(int i=0;i<this.people.size();i++)
			misstrauen[i] = this.people.get(i).get_misstrauen();
		
		for(int i=0;i<this.people.size();i++){
			if(this.people.get(i).get_location_id() != 0 && this.people.get(i).get_location_id() != 'X' && this.people.get(i).get_location_id() != 'E'){	//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
				for(int j=0;j<this.people.size();j++){
					if(i!=j){
						if(this.people.get(i).get_location_id() == this.people.get(j).get_location_id()){
							//zwischenspeichern in misstrauen[] erforderlich, sonst wird immer mit aktualisiertem Wert gerechnet
							if(this.people.get(i) instanceof Erwachsene && this.people.get(j) instanceof Kinder){	//Kind beeinflusst Erwachsenen weniger
								misstrauen[i] = misstrauen[i] - faktor/2*this.beziehungsmatrix[i][j]*(this.people.get(i).get_misstrauen()-this.people.get(j).get_misstrauen());
							}
							else{
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]*(this.people.get(i).get_misstrauen()-this.people.get(j).get_misstrauen());
							}

							if(misstrauen[i]>100)//Rangecheck
								misstrauen[i] = 100;
							if(misstrauen[i]<-100)
								misstrauen[i] = -100;
						}
					}
				}
			}
		}
		
		//mapping der neuen Misstrauenswerte auf Person
		for(int i=0;i<this.people.size();i++){
			this.people.get(i).set_misstrauen(misstrauen[i]);
		}
	}
	
	//Support: Tobi
		public void update_position(){
			for(int i=0;i<people.size();i++){
				people.get(i).step();
			}
			
		}
	
	//Beschwerden an Miri
	float calc_misstrauen_in_street(){
		float misstrauen = 0;
		for(int i=0;i<this.people.size();i++){
			misstrauen += this.people.get(i).get_misstrauen();
		}
		misstrauen = misstrauen/this.people.size();
		
		return misstrauen;
	}
	
	//TODO
	void calc_misstrauen_after_action(){
		
	}
	
	
	//Support Tiki
	void initialize_spielzeit(){
		//Tagesbeginn variabel
		//TODO mit Gruppe besprechen wann wir anfangen sollen
		this.spielMinute=0;
		this.spielStunde=7;
		this.spielTag=1;
	}
	
	
	// Support Tiki
	void calc_spielzeit(){
		// Vorausgesetzt, dass die Spilzeit initialisiert wurde, abfragen auf "null" nicht möglich da primitiver Datentyp
		
		//alle 500 ms wird eine Spielminute hochgezählt, dh 12 Minuten für einen Tagesablauf
		
		long current_milis;
		current_milis = System.currentTimeMillis();
		
		//TODO -> es muss immer genau 500 oder 0 sein, das werden wir in unserer Abfrage selten hinbekommen... 
		// ausbaufähig, vorläufig steht was
		if (current_milis == 500 || current_milis == 0){
			this.spielMinute++;
			if (this.spielMinute == 60){
				this.spielMinute = 0;
				this.spielStunde++;
				if (this.spielStunde==24){
					this.spielStunde=0;
					this.spielStunde++;
				}
			}
		}
	}
	
	
	//Support Tiki
	//TODO Weg auf den Stack schreiben
	//TODO Die Häufigkeit des "in den Park gehen" testen, evtl zu häufig
	void tagesablauf(){
		for	(int i=0; i<this.people.size(); i++){
			if (this.people.get(i) instanceof Kinder){
				if (this.spielStunde==7 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //
				
				//gehe zur Schule
				}	
				if (this.spielStunde==14  && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ 
				
				//gehe nach Hause				
				}	
				if (this.spielStunde==20  && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
					
				//gehe nach Hause, falls location nicht zuhause ist
				}
				if (this.spielStunde >= 15 && this.spielStunde <=20 ){
					if ((int)Math.random()*20 == 3){
					//gehe in den Park
					}
				}
				if (this.people.get(i).get_location_id()=='P'){
					if ((int)Math.random()*30 == 3){
					//gehe nach Hause
					}
				}
			}else{
				if (this.people.get(i) instanceof Erwachsene){
					if (((Erwachsene)people.get(i)).isHat_arbeit()){
						if (this.spielStunde==8 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
							
							//gehe zur Arbeit
						}
						if (this.spielStunde==16 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){	
						
							//gehe nach Hause
						}		
						if (this.spielStunde==1 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
								
							//gehe nach Hause
						}		
						if (this.spielStunde >= 17 || this.spielStunde <=1){
							if ((int)Math.random()*30 == 3){
							//gehe in den Park
							}
						}
					} else {
						if (this.spielStunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
							
							//gehe Einkaufen
						}	
						if (this.spielStunde == 11 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
								
							//gehe nach Hause
						}
						if (this.spielStunde == 1 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
								
							//gehe nach Hause
						}
						if (this.spielStunde >=12 || this.spielStunde <=1){
							if ((int)Math.random()*30 == 3){
								//gehe in den Park
							}
						}
					}
					if (this.people.get(i).get_location_id()=='P'){
						if ((int)Math.random()*30 == 3){
							//gehe nach Hause
						}
					}
				}
			}
		}
	} 	
	
	//Support Tiki
	public void berechne_weg(Person person, char ziellocation){
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		location_ids=TheStreet.read_from_csv("C:/Users/Martika/Desktop/Dropbox/Software Engineering/Grafikdesign/Fertig/russland_map.csv");
		
	
	
	}
	
	
	public ArrayList<Person> get_people(){
		return people;
	}
	
	public void set_people(Person p){
		people.add(p);
	}
	
	
	public Agent get_agent(){
		return agent;
	}


	public int getSpielTag() {
		return spielTag;
	}


	public void setSpielTag(int spielTag) {
		this.spielTag = spielTag;
	}


	public int getSpielStunde() {
		return spielStunde;
	}


	public void setSpielStunde(int spielStunde) {
		this.spielStunde = spielStunde;
	}


	public int getSpielMinute() {
		return spielMinute;
	}


	public void setSpielMinute(int spielMinute) {
		this.spielMinute = spielMinute;
	}

}
