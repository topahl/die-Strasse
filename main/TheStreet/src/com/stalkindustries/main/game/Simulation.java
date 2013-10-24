package com.stalkindustries.main.game;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import com.stalkindustries.main.TheStreet;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	//private int[] location_raster; nicht mehr benötigt
	private ArrayList<Person> people = new ArrayList(); 
	private Agent agent = new Agent();
	private int spielTag;
	private int spielStunde;
	private int spielMinute;
	
	public Simulation(){
		
	}
	
	
	//Beschwerden an Miri
	void initialize_beziehungsmatrix(){
		int tmp;
		this.beziehungsmatrix = new int[this.people.size()][this.people.size()];
		for(int i=0;i<this.people.size();i++)
			for(int j=0;j<this.people.size();j++)
				this.beziehungsmatrix[i][j] = 0;
			
		for(int i=0;i<this.people.size();i++){
			for(int j=i+1;j<this.people.size();j++){
				tmp = (int)(Math.random()*(10))+1;
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
	
	
	//Support Tiki
	//TODO Die Häufigkeit des "in den Park gehen" testen, evtl zu häufig
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
					if ((int)(Math.random()*20) == 3){
						berechne_weg(this.people.get(i), 'P');
					}
				}
				if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
					if ((int)(Math.random()*30) == 3){
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
							if ((int)(Math.random()*30) == 3){
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
							if ((int)(Math.random()*30) == 3){
								berechne_weg(this.people.get(i), 'P');
							}
						}
					}
					if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
						if ((int)(Math.random()*30) == 3){
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id())); 
						}
					}
				}
			}
		}
	} 	
	
	//Support Tiki
	//TODO richtigen Dateipfad verwenden
	public void berechne_weg(Person person, char zielloc){
		
		int counter;
		String ziellocation = String.valueOf(zielloc);
		int xPos_current, yPos_current;
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		
		counter = 1;
		xPos_current = 0;
		yPos_current = 0;
		location_ids = Ressources.getLocation_ids();
		
		if (zielloc == person.get_location_id()){
			neuer_weg.push('l');
			neuer_weg.push('u');
			neuer_weg.push('u');
			neuer_weg.push('r');
			neuer_weg.push('r');
			neuer_weg.push('r');
			neuer_weg.push('r');
			neuer_weg.push('o');
			neuer_weg.push('o');
			neuer_weg.push('l');
			neuer_weg.push('l');
			neuer_weg.push('l');
			person.setMoves(neuer_weg);
			return;
		}
		
		
		for (int i=0; i<location_ids.size(); i++){
			for (int j=0; j<location_ids.get(i).size(); j++){
				if (location_ids.get(i).get(j).charAt(0) != 'X' && location_ids.get(i).get(j).charAt(0) != ziellocation.charAt(0) && location_ids.get(i).get(j).charAt(0) != 'P'){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == ziellocation.charAt(0)){
					location_ids.get(i).set(j,"Z") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == 'P'){
					location_ids.get(i).set(j,"X") ;
				}
			}
		}
		ziellocation = "Z";
		
		location_ids.get(person.getPosX()).set(person.getPosY(),"0") ;
		
goal:	for (int i=0; i<100; i++){
			for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
				for (int k=0; k<25; k++){
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))){
						// Es wird überprüft, ob das Ziel in direkter Nähe liegt
						if (location_ids.get(j+1).get(k).equals(ziellocation)) {
							location_ids.get(j+1).set(k,String.valueOf(i+1));
							counter = i;
							yPos_current = j+1;
							xPos_current = k;
							break goal;
						}
						if (location_ids.get(j).get(k+1).equals(ziellocation)) {
							location_ids.get(j).set(k+1,String.valueOf(i+1));
							counter = i;
							yPos_current = j;
							xPos_current = k+1;
							break goal;
						}
						if (location_ids.get(j-1).get(k).equals(ziellocation)) {
							location_ids.get(j-1).set(k,String.valueOf(i+1));
							counter = i;
							yPos_current = j-1;
							xPos_current = k;
							break goal;
						}
						if (location_ids.get(j).get(k-1).equals(ziellocation)) {
							location_ids.get(j).set(k-1,String.valueOf(i+1));
							counter = i;
							yPos_current = j;
							xPos_current = k-1;
							break goal;
						}
						
						// Es wird überprüft, ob ein Feld drüber/drunter/links oder rechts ebenfalls begehbar ist -> das wird markiert
						if (location_ids.get(j+1).get(k).equals("X")) {			//Weg nach unten ist begehbar
							location_ids.get(j+1).set(k,String.valueOf(i+1));
						}
						if (location_ids.get(j).get(k+1).equals("X")) {			//Weg nach rechts ist begehbar
							location_ids.get(j).set(k+1,String.valueOf(i+1));
						}
						if (location_ids.get(j-1).get(k).equals("X")) {			// Weg nach oben ist begehbar
							location_ids.get(j-1).set(k,String.valueOf(i+1));
						}
						if (location_ids.get(j).get(k-1).equals("X")) {			//Weg nach links ist begehbar
							location_ids.get(j).set(k-1,String.valueOf(i+1));
						}
					}
				}
			}
		}
		
		// Der Stack für die Bewegung wird mit den richtigen Werten gefüllt. Dafür hangelt man sich absteigend an der zahlenreihe entlang
		for (int i = counter; i>=0; i++){
			if (location_ids.get(xPos_current+1).get(yPos_current).equals(String.valueOf(i))) {			//unten gehts weiter
				xPos_current++;
				neuer_weg.push('o');
			}
			if (location_ids.get(xPos_current).get(yPos_current+1).equals(String.valueOf(i))) {			//rechts gehts weiter
				yPos_current++;
				neuer_weg.push('l');
			}
			if (location_ids.get(xPos_current-1).get(yPos_current).equals(String.valueOf(i))) {			//oben gehts weiter
				xPos_current--;
				neuer_weg.push('u');
			}
			if (location_ids.get(xPos_current).get(yPos_current-1).equals(String.valueOf(i))) {			//links gehts weiter
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
	
	public void set_agent(Agent agent){
		this.agent = agent;
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
