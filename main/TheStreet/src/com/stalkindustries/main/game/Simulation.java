package com.stalkindustries.main.game;
import java.util.ArrayList;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	private int[] location_raster;
	private ArrayList<Person> people = new ArrayList(); // nicht mit dieser arraylist arbeiten
	private ArrayList<Kinder> kinder = new ArrayList();
	private ArrayList<Erwachsene> erwachsene = new ArrayList();
	private Agent agent;
	private int spielTag;
	private int spielStunde;
	private int spielMinute;
	
	
	//Beschwerden an Miri
	void initialize_beziehungsmatrix(){
		int tmp;
		for(int i=0;i<this.people.size();i++)
			for(int j=0;j<this.people.size();i++)
				this.beziehungsmatrix[i][j] = 42;
			
		for(int i=0;i<this.people.size();i++){
			for(int j=i+1;j<this.people.size();i++){
				tmp = (int)Math.random()*(11);
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
	
	
	//Beschwerden an Miri
	void initialize_location_raster(){
		this.location_raster = new int[this.people.size()];
		for(int i=0;i<this.people.size();i++){
			this.location_raster[i] = 42;
		}
	}
	
	void update_location_raster(){
		//TODO
		//weißt Bewohnern eine Location-ID zu
		//--> man weiß nun, wo sie sich grob befinden, d.h.
		//wenn sich Person in bestimmtem Haus befindet, dann ist Location-Id = Haus-Id
		//Schule --> Location-ID = Schule-ID
		//Park --> Location-Id = Park-ID
		//einkaufen, arbeiten, ... --> Location-Id = 42
	}
	
	
	//Beschwerden an Miri
	void calculate_misstrauen(){
		float[] misstrauen = new float[people.size()];
		float faktor = 1/100; //kommt noch darauf an, wie häufig die Methode aufgerufen wird
		for(int i=0;i<this.people.size();i++)
			misstrauen[i] = this.people.get(i).get_misstrauen();
		
		for(int i=0;i<this.people.size();i++){
			if(this.people.get(i).get_location_id() != 42){	//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
				for(int j=0;j<this.people.size();j++){
					if(i!=j){
						if(this.people.get(i).get_location_id() == this.people.get(j).get_location_id()){
							//zwischenspeichern in misstrauen[] erforderlich, sonst wird immer mit aktualisiertem Wert gerechnet
							if(misstrauen[i]>=misstrauen[j] && misstrauen[j]>=0)
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]>=misstrauen[j] && misstrauen[j]<0)
								misstrauen[i] = misstrauen[i] + faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]<misstrauen[j] && misstrauen[j]>=0)
								misstrauen[i] = misstrauen[i] + faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]<misstrauen[j] && misstrauen[j]<0)
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							
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
		setSpielMinute(0);
		setSpielStunde(7);
		setSpielTag(1);
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
			setSpielMinute(getSpielMinute()+1);
			if (getSpielMinute() == 60){
				setSpielMinute(0);
				setSpielStunde(getSpielStunde()+1);
				if (getSpielStunde()==24){
					setSpielStunde(0);
					setSpielTag(getSpielTag()+1);
				}
			}
		}
		
	}
	
	
	//Support Tiki
	//TODO einiges
	void tagesablauf(){
		// i=0 oder i=1...?
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
					
				// gehe Randommäßig in den Park
				}
			} else {
					//if (this.people.get(i).){ //hat arbeit
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
							
						//gehe randommäßig in den Park
						}
						
					//} else {
						
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
								
							//gehe randommäßig in den Park
							}
						
					//}
			}	
		}
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
