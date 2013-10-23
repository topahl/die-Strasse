package com.stalkindustries.main.game;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import com.stalkindustries.main.TheStreet;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	//private int[] location_raster; nicht mehr ben�tigt
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
		double faktor = 0.01275; //kommt noch darauf an, wie h�ufig die Methode aufgerufen wird
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
		// Vorausgesetzt, dass die Spilzeit initialisiert wurde, abfragen auf "null" nicht m�glich da primitiver Datentyp
		
		//alle 500 ms wird eine Spielminute hochgez�hlt, dh 12 Minuten f�r einen Tagesablauf
		
		long current_milis;
		current_milis = System.currentTimeMillis();
		
		//TODO -> es muss immer genau 500 oder 0 sein, das werden wir in unserer Abfrage selten hinbekommen... 
		// ausbauf�hig, vorl�ufig steht was
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
	//TODO Die H�ufigkeit des "in den Park gehen" testen, evtl zu h�ufig
	void tagesablauf(){
		for	(int i=0; i<this.people.size(); i++){
			if (this.people.get(i) instanceof Kinder){
				if (this.spielStunde==7 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //zur Schule gehen
					berechne_weg(this.people.get(i), 'E');
				}	
				if (this.spielStunde==14  && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
					berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));				
				}	
				if (this.spielStunde==20  && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
					berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
				}
				if (this.spielStunde >= 15 && this.spielStunde <=20 ){ // in den Park gehen
					if ((int)Math.random()*20 == 3){
						berechne_weg(this.people.get(i), 'P');
					}
				}
				if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
					if ((int)Math.random()*30 == 3){
						berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id())); 
					}
				}
			}else{
				if (this.people.get(i) instanceof Erwachsene){
					if (((Erwachsene)people.get(i)).isHat_arbeit()){
						if (this.spielStunde==8 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ // Zur Arbeit gehen
							berechne_weg(this.people.get(i), 'E');
						}
						if (this.spielStunde==16 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}		
						if (this.spielStunde==1 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}		
						if (this.spielStunde >= 17 || this.spielStunde <=1){  // in den Park gehen
							if ((int)Math.random()*30 == 3){
								berechne_weg(this.people.get(i), 'P');
							}
						}
					} else {
						if (this.spielStunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //zum Einkaufen gehen
							berechne_weg(this.people.get(i), 'E');
						}	
						if (this.spielStunde == 11 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}
						if (this.spielStunde == 1 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}
						if (this.spielStunde >=12 || this.spielStunde <=1){  // in den Park gehen
							if ((int)Math.random()*30 == 3){
								berechne_weg(this.people.get(i), 'P');
							}
						}
					}
					if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
						if ((int)Math.random()*30 == 3){
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id())); 
						}
					}
				}
			}
		}
	} 	
	
	//Support Tiki
	//TODO richtigen Dateipfad verwenden
	//TODO Castingprobleme untersuchen
	public void berechne_weg(Person person, char ziellocation){
		
		int counter;
		int xPos_current, yPos_current;
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		
		counter = 1;
		xPos_current = person.getPosX();
		yPos_current = person.getPosY();
		location_ids=TheStreet.read_from_csv("C:/Users/Martika/Desktop/Dropbox/Software Engineering/Grafikdesign/Fertig/russland_map.csv");
		
		for (int i=0; i<location_ids.size(); i++){
			for (int j=0; j<location_ids.get(i).size(); j++){
				if (location_ids.get(i).get(j).charAt(0) != 'X' && location_ids.get(i).get(j).charAt(0) != ziellocation){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
			}
		}
		
		location_ids.get(person.getPosX()).set(person.getPosY(),"0") ;
		
		for (int i=0; i<100; i++){
			for (int j=0; j<location_ids.size(); j++){
				for (int k=0; k<location_ids.get(i).size(); k++){
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))){
						// Es wird �berpr�ft, ob das Ziel in direkter N�he liegt
						if (location_ids.get(j+1).get(k).equals(ziellocation)) {
							location_ids.get(j+1).set(k,String.valueOf(i));
							counter = i;
							xPos_current = j;
							yPos_current = k;
							break;
						}
						if (location_ids.get(j).get(k+1).equals(ziellocation)) {
							location_ids.get(j).set(k+1,String.valueOf(i));
							counter = i;
							break;
						}
						if (location_ids.get(j-1).get(k).equals(ziellocation)) {
							location_ids.get(j-1).set(k,String.valueOf(i));
							counter = i;
							break;
						}
						if (location_ids.get(j).get(k-1).equals(ziellocation)) {
							location_ids.get(j).set(k-1,String.valueOf(i));
							counter = i;
							break;
						}
						
						// Es wird �berpr�ft, ob ein Feld dr�ber/drunter/links oder rechts ebenfalls begehbar ist -> das wird markiert
						if (location_ids.get(j+1).get(k).equals("X")) {			//Weg nach unten ist begehbar
							location_ids.get(j+1).set(k,String.valueOf(i));
						}
						if (location_ids.get(j).get(k+1).equals("X")) {			//Weg nach rechts ist begehbar
							location_ids.get(j).set(k+1,String.valueOf(i));
						}
						if (location_ids.get(j-1).get(k).equals("X")) {			// Weg nach oben ist begehbar
							location_ids.get(j-1).set(k,String.valueOf(i));
						}
						if (location_ids.get(j).get(k-1).equals("X")) {			//Weg nach links ist begehbar
							location_ids.get(j).set(k-1,String.valueOf(i));
						}
					}
				}
			}
		}
		
		// Der Stack f�r die Bewegung wird mit den richtigen Werten gef�llt. Daf�r hangelt man sich absteigend an der zahlenreihe entlang
		for (int i = (counter-1); i>=0; i++){
			if (location_ids.get(xPos_current+1).get(yPos_current).equals(counter)) {			//unten gehts weiter
				location_ids.get(xPos_current+1).set(yPos_current,String.valueOf(i));
				xPos_current++;
				neuer_weg.push('o');
			}
			if (location_ids.get(xPos_current).get(yPos_current+1).equals(counter)) {			//rechts gehts weiter
				location_ids.get(xPos_current).set(yPos_current+1,String.valueOf(i));
				yPos_current++;
				neuer_weg.push('l');
			}
			if (location_ids.get(xPos_current-1).get(yPos_current).equals(counter)) {			//oben gehts weiter
				location_ids.get(xPos_current-1).set(yPos_current,String.valueOf(i));
				xPos_current--;
				neuer_weg.push('u');
			}
			if (location_ids.get(xPos_current).get(yPos_current-1).equals(counter)) {			//links gehts weiter
				location_ids.get(xPos_current).set(yPos_current-1,String.valueOf(i));
				yPos_current--;
				neuer_weg.push('r');
			}
		}
		person.setMoves(neuer_weg);
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
