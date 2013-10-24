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
	private ArrayList<Person> people = new ArrayList<Person>(); 
	private Agent agent = new Agent();
	private int spiel_tag=1;
	private int spiel_stunde=7;
	private int spiel_minute=0;
	private ArrayList<Haus> houses = new ArrayList<Haus>();

	
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
	
	public void calc_ueberwachungsstatus(){
		for(int i=0;i<this.houses.size();i++){
			this.houses.get(i).setUeberwachungsstatus(this.houses.get(i).getUeberwachungsmodule().size()/Haus.MAXUBERWACHUNGSMODULE);
		}
	}
	
	public float calc_ueberwachung_in_street(){
		float ueberwachung = 0;
		for(int i=0;i<this.houses.size();i++){
			ueberwachung += this.houses.get(i).getUeberwachungsstatus();
		}
		ueberwachung = ueberwachung/this.people.size();
		
		return ueberwachung;
	}
	

	
	
	// Support Tiki
	void calc_spielzeit(){
		this.spiel_minute++;
		if (this.spiel_minute==60){
			this.spiel_minute = 0;
			this.spiel_stunde++;
		}
		if (this.spiel_stunde==24){
			this.spiel_stunde=0;
			this.spiel_tag++;
		}
	}
	
	
	//Support Tiki
	//TODO Die Häufigkeit des "in den Park gehen" testen, evtl zu häufig
	void tagesablauf(){
		for	(int i=0; i<this.people.size(); i++){
			if (this.people.get(i) instanceof Kinder){
				if (this.spiel_stunde==7 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //zur Schule gehen
					berechne_weg(this.people.get(i), 'E');
				}	
				if (this.spiel_stunde==14  && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
					berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));				
				}	
				if (this.spiel_stunde==20  && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
					berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
				}
				if (this.spiel_stunde >= 15 && this.spiel_stunde <=20 ){ // in den Park gehen
					if ((int)(Math.random()*200) == 3){
						berechne_weg(this.people.get(i), 'P');
					}
				}
				if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
					if ((int)(Math.random()*300) == 3){
						berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id())); 
					}
				}
			}else{
				if (this.people.get(i) instanceof Erwachsene){
					if (((Erwachsene)people.get(i)).isHat_arbeit()){
						if (this.spiel_stunde==8 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ // Zur Arbeit gehen
							berechne_weg(this.people.get(i), 'E');
						}
						if (this.spiel_stunde==16 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}		
						if (this.spiel_stunde==1 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}		
						if (this.spiel_stunde >= 17 || this.spiel_stunde <=1){  // in den Park gehen
							if ((int)(Math.random()*300) == 3){
								System.out.println("In Park gehen");
								berechne_weg(this.people.get(i), 'P');
							}
						}
					} else {
						if (this.spiel_stunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //zum Einkaufen gehen
							berechne_weg(this.people.get(i), 'E');
						}	
						if (this.spiel_stunde == 11 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}
						if (this.spiel_stunde == 1 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //nach Hause gehen
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id()));
						}
						if (this.spiel_stunde >=12 || this.spiel_stunde <=1){  // in den Park gehen
							if ((int)(Math.random()*300) == 3){
								berechne_weg(this.people.get(i), 'P');
							}
						}
					}
					if (this.people.get(i).get_location_id()=='P'){ //nach Hause gehen
						if ((int)(Math.random()*300) == 3){
							berechne_weg(this.people.get(i), (char)(this.people.get(i).get_haus_id())); 
						}
					}
				}
			}
		}
	} 	
	
	//Support Tiki
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
		
		//Wenn die Person im Park ist, soll er eine Runde spazieren gehen
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
				if (location_ids.get(i).get(j).charAt(0) != 'X' && location_ids.get(i).get(j).charAt(0) != ziellocation.charAt(0) && location_ids.get(i).get(j).charAt(0) != 'P' && location_ids.get(i).get(j).charAt(0) != person.get_location_id()){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == ziellocation.charAt(0)){
					location_ids.get(i).set(j,"Z") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == 'P'){
					location_ids.get(i).set(j,"X") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == person.get_location_id()){
					location_ids.get(i).set(j,"X") ;
				}
			}
		}
		ziellocation = "Z";
		location_ids.get((person.getPosY()-Ressources.ZEROPOS.height)/45).set((person.getPosX()-Ressources.ZEROPOS.width)/45,"0") ;		
		
goal:	for (int i=0; i<100; i++){
			for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
				for (int k=0; k<25; k++){
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))){
						// Es wird überprüft, ob das Ziel in direkter Nähe liegt
						if (j < 15){
							if (location_ids.get(j+1).get(k).equals(ziellocation)) {
							location_ids.get(j+1).set(k,String.valueOf(i+1));
							counter = i;
							yPos_current = j+1;
							xPos_current = k;
							break goal;
							}
						}
						if (k<24){
							if (location_ids.get(j).get(k+1).equals(ziellocation)) {
							location_ids.get(j).set(k+1,String.valueOf(i+1));
							counter = i;
							yPos_current = j;
							xPos_current = k+1;
							break goal;
							}
						}
						if (j>0){
							if (location_ids.get(j-1).get(k).equals(ziellocation)) {
							location_ids.get(j-1).set(k,String.valueOf(i+1));
							counter = i;
							yPos_current = j-1;
							xPos_current = k;
							break goal;
							}
						}
						if (k>0){
							if (location_ids.get(j).get(k-1).equals(ziellocation)) {
							location_ids.get(j).set(k-1,String.valueOf(i+1));
							counter = i;
							yPos_current = j;
							xPos_current = k-1;
							break goal;
							}
						}
						
						
						// Es wird überprüft, ob ein Feld drüber/drunter/links oder rechts ebenfalls begehbar ist -> das wird markiert
						if (j<15){
							if (location_ids.get(j+1).get(k).equals("X")) {			//Weg nach unten ist begehbar
							location_ids.get(j+1).set(k,String.valueOf(i+1));
							}
						}
						if (k<24){
							if (location_ids.get(j).get(k+1).equals("X")) {			//Weg nach rechts ist begehbar
							location_ids.get(j).set(k+1,String.valueOf(i+1));
							}
						}
						if (j>0){
							if (location_ids.get(j-1).get(k).equals("X")) {			// Weg nach oben ist begehbar
							location_ids.get(j-1).set(k,String.valueOf(i+1));
							}
						}
						if (k>0){
							if (location_ids.get(j).get(k-1).equals("X")) {			//Weg nach links ist begehbar
							location_ids.get(j).set(k-1,String.valueOf(i+1));
							}
						}
					}
				}
			}
		}
//		if (xPos_current==0 || yPos_current==0){
//			System.out.print("Fehler!");
//		}
		// Der Stack für die Bewegung wird mit den richtigen Werten gefüllt. Dafür hangelt man sich absteigend an der zahlenreihe entlang
		for (int i = counter; i>=0; i--){
			if (yPos_current<15){
				if (location_ids.get(yPos_current+1).get(xPos_current).equals(String.valueOf(i))) {			//unten gehts weiter
				yPos_current++;
				neuer_weg.push('o');
				}
			}
			if (xPos_current<24){
				if (location_ids.get(yPos_current).get(xPos_current+1).equals(String.valueOf(i))) {			//rechts gehts weiter
				xPos_current++;
				neuer_weg.push('l');
				}
			}
			if (yPos_current>0){
				if (location_ids.get(yPos_current-1).get(xPos_current).equals(String.valueOf(i))) {			//oben gehts weiter
				yPos_current--;
				neuer_weg.push('u');
				}
			}
			if (xPos_current>0){
				if (location_ids.get(yPos_current).get(xPos_current-1).equals(String.valueOf(i))) {			//links gehts weiter
				xPos_current--;
				neuer_weg.push('r');
				}
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


	public int getSpiel_tag() {
		return spiel_tag;
	} 


	public int getSpiel_stunde() {
		return spiel_stunde;
	}


	public int getSpiel_minute() {
		return spiel_minute;
	}

	
	public String getSpielzeit_as_string() {
		String zeit="";
		zeit = String.valueOf(this.spiel_stunde);
		if (this.spiel_stunde <=9){
			zeit = "0" + zeit;
		}
		zeit = zeit + ":";
		if (this.spiel_minute <=9){
			zeit = zeit + "0";
		}
		zeit = zeit + String.valueOf(this.spiel_minute);
		
		return zeit;
	}
	
	public ArrayList<Haus> getHouses(){
		return this.houses;
	}
	
	public void setHouses(Haus haus){
		this.houses.add(haus);
	}

}
