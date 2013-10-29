package com.stalkindustries.main.game;
import java.awt.Point;
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
	private Agent agent = new Agent(0);
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
		//misstrauen[] ist eine Hilfvariable, die später die später die neuen Werte enthält und sie wird benötigt, dass nicht die echten Werte verändert werden, bevor alle berechnet wurden
		double[] misstrauen = new double[people.size()];
		
		double faktor = 0.01275; //Faktor, der regelt, wie stark sich Personen bei einem Methodenaufruf beeinflussen --> abhängig von Häufigkeit des Methodenaufrufs
		
		//misstrauen[] mit den alten Misstrauenswerten initialisieren
		for(int i=0;i<this.people.size();i++)
			misstrauen[i] = this.people.get(i).get_misstrauen();
		
		//jede Person kann theoretisch wieder von jeder anderen beeinflusst werden
		for(int i=0;i<this.people.size();i++){
			//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
			if((int)(this.people.get(i).get_location_id())-48+1 != 0 && (int)(this.people.get(i).get_location_id())-48+1 != 'X' && (int)(this.people.get(i).get_location_id())-48+1 != 'E'){
				for(int j=0;j<this.people.size();j++){
					//eine Person kann sich nicht selbst beeinflussen
					if(i!=j){
						//wenn sich die beiden Personen am selben Ort befinden
						if((int)(this.people.get(i).get_location_id())-48+1 == (int)(this.people.get(j).get_location_id())-48+1){
							//Ausnahme in der Berechnung: Kinder beeinflussen Erwachsene weniger
							if(this.people.get(i) instanceof Erwachsene && this.people.get(j) instanceof Kinder){	//Kind beeinflusst Erwachsenen weniger
								misstrauen[i] = misstrauen[i] - faktor/2*this.beziehungsmatrix[i][j]*(this.people.get(i).get_misstrauen()-this.people.get(j).get_misstrauen());
							}
							else{
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]*(this.people.get(i).get_misstrauen()-this.people.get(j).get_misstrauen());
							}
							
							//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
							if(misstrauen[i]>100)
								misstrauen[i] = 100;
							if(misstrauen[i]<-100)
								misstrauen[i] = -100;
						}
					}
				}
			}
		}
		
		//Mapping der neuen Misstrauenswerte auf Person
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
	//Mittelwert des Misstrauens der einzelnen Personen
	float calc_misstrauen_in_street(){
		float misstrauen = 0;
		for(int i=0;i<this.people.size();i++){
			misstrauen += this.people.get(i).get_misstrauen();
		}
		misstrauen = misstrauen/this.people.size();
		
		return misstrauen;
	}
	
	
	//Beschwerden an Miri
	public void calc_misstrauen_after_beschwichtigen_action(int action_id, int personen_id){
		int zufall=0;
		int risiko;
		double misstrauen = this.people.get(personen_id).get_misstrauen();
		
		//Risiko einer Beschwichtigenaktion steigt, je häufiger man sie ausführt
		risiko = this.people.get(personen_id).get_durchgefuehrteBeschwichtigungen(action_id);
		
		//Fehlschlagen der Aktionen ist zudem abhängig vom Misstrauensstatus
		//Personen sind misstrauisch
		if(misstrauen >= 0)
			zufall = (int)(Math.random()*(risiko+misstrauen/10));
		//Personen sind weniger misstrauisch
		else{
			if(risiko+misstrauen/10 < 0)
				zufall = -(int)(Math.random()*(-(risiko+misstrauen/10)));
			else
				zufall = (int)(Math.random()*(risiko+misstrauen/10));
		}
		
		//TODO Testen, ob die Werte passen
		//wie sich das Misstrauen verändern soll
		if(zufall < -5)
			misstrauen -= 20;
		else if(zufall < 0)
			misstrauen -= 10;
		else if(zufall < 5)
			misstrauen -= 5;
		else if(zufall > 8)
			misstrauen += 10;
		
		
		//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
		if(misstrauen>100)
			misstrauen = 100;
		if(misstrauen<-100)
			misstrauen = -100;
		
		//Misstrauen der Person setzen
		this.people.get(personen_id).set_misstrauen(misstrauen);
		
		//Bei Person die Anzahl der Beschwichtigenaktionen erhöhen
		this.people.get(personen_id).set_durchgefuehrteBeschwichtigungen(action_id);
	}
	
	
	//TODO
	//Beschwerden an Miri
	public void calc_misstrauen_after_ueberwachungs_action(String action_id, int house_location){
		//Risiko berechnen
		//Risiko ist abhängig von der Uhrzeit, d.h.tagsüber ist das Risiko höher als nachts
		int risiko=0;
		if(this.spiel_stunde>1 && this.spiel_stunde<7){	//Nachtmodus
			risiko = (int)(Math.random()*3); 
		}
		else{	//Tagmodus
			risiko = (int)(Math.random()*15); 
		}
		
		int mittelpunktX = this.houses.get(house_location-1).getPosX()+3*Ressources.RASTERHEIGHT/2;
		int mittelpunktY = this.houses.get(house_location-1).getPosY()+3*Ressources.RASTERHEIGHT/2;
		int epsilon = 200;	
		
		for(int i=0;i<this.people.size();i++){
			//Checken, ob sich noch jemand in dem Haus befindet
			//für alle Personen, die noch im Haus sind, das Misstrauen neu berechnen
			if((int)(this.people.get(i).get_location_id())-48 == house_location){
				if(risiko>2)	//wenn das risiko kleiner ist, hat man Glück und man wird nicht erwicht
					this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+6); //TODO: den Wert 50 testen ... eventuell erhöhen
			}
			//Checken, ob sich jemand in einer epsilon-Umgebung um das Haus befindet, in das eingebrochen werden soll
			//--> 1. Epsilon-Umgebung aufspannen (ist eine relative eckige :-D)
			//-->Mittelpunkt vom Haus bestimmen
			else{
				// wenn sich eine Person in der Epsilon-Umgebung befindet
				if(this.people.get(i).getPosX() >= mittelpunktX-epsilon && this.people.get(i).getPosX() <= mittelpunktX+epsilon && this.people.get(i).getPosY() >= mittelpunktY-epsilon && this.people.get(i).getPosY() <= mittelpunktY+epsilon && this.people.get(i).get_location_id()!='E'){
					//wenn das per Zufall eine Person ist, die in dem Haus wohnt und z.B. auf dem Heimweg ist
					//hier ist das Misstrauen natürlich größer
					if(this.people.get(i).get_haus_id()+1 == house_location){
						if(risiko>2)
							this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+5);
					}
					else{
						if(risiko>2)
							this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+2);
					}
				}
			}
			//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.people.get(i).get_misstrauen()>100)
				this.people.get(i).set_misstrauen(100);
			if(this.people.get(i).get_misstrauen()<-100)
				this.people.get(i).set_misstrauen(-100);
		}
		
	}
	
	
	//Beschwerden Miri
	//während ein Haus überwacht wird, macht sich bei den Bewohnern ein Unwohlsein breit und sie werden mehr misstrauisch
	public void calc_misstrauen_during_ueberwachung(){
		for(int i=0;i<this.people.size();i++){
			// wenn Überwachungsmodule in dem Haus, in dem die Person lebt, installiert wurden
			if(this.houses.get(this.people.get(i).get_haus_id()).getUeberwachungsmodule().size() > 0){
				this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+2);
			}
		}
	}
	
	
	//Beschwerden Miri
	//Allen Häusern den Überwachungsstatus updaten
	public void calc_ueberwachungsstatus(){
		for(int i=0;i<this.houses.size();i++){
			this.houses.get(i).setUeberwachungsstatus(this.houses.get(i).getUeberwachungsmodule().size()/Haus.MAXUBERWACHUNGSMODULE);
		}
	}
	
	//Beschwerden Miri
	//Mittelwert der Überwachungsstati der einzelnen Häuser
	public float calc_ueberwachung_in_street(){
		float ueberwachung = 0;
		for(int i=0;i<this.houses.size();i++){
			ueberwachung += this.houses.get(i).getUeberwachungsstatus();
		}
		ueberwachung = ueberwachung/this.houses.size();
		
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
	void tagesablauf(){
		char locid ='0';
		int hausid = 0;
		for	(int i=0; i<this.people.size(); i++){
			locid = (char)((int)(this.people.get(i).get_location_id()));
			hausid = this.people.get(i).get_haus_id()+1;
			
			// Es bekommen nur die Menschen einen neuen Weg zugewiesen, wenn sie sich gerade nicht bewegen
			if (this.people.get(i).getCurrentMove() == 'n'){
				
				//Zuerst wird der Tagesablauf der Kinder überprüft, da dieser von den Erwachsenen unterschiedlich ist
				if (this.people.get(i) instanceof Kinder){
					if ((this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){
						
						if (this.spiel_stunde==7){ //zur Schule gehen
							berechne_weg(this.people.get(i), null, 'E');
						}	
						
						//Nur wenn der Mensch nicht zuHause ist, kann er nach Hause gehen
						if ((this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){
							
							if (this.spiel_stunde==14){ //nach Hause gehen
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));				
							}
							if (this.spiel_stunde==20){ //nach Hause gehen
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
							}	
						} 
					} else {

						//nach Hause gehen, notwendig, falls die Kinder noch eine runde im Park drehen & 20 Uhr überschritten wird
						if (this.spiel_stunde>=20 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ 
							berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
						}
						
						// randomisiert in den Park gehen
						if (this.spiel_stunde >= 15 && this.spiel_stunde <=19 && locid !='P'){ 
							if ((int)(Math.random()*200) == 3){
								berechne_weg(this.people.get(i), null, 'P');
							}
						}
						
						// randomisiert den Park verlassen oder noch eine Runde drehen
						if (locid =='P' && this.spiel_stunde<=19){ 
							if ((int)(Math.random()*5) == 3){
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									berechne_weg(this.people.get(i), null, 'P');
								}
							}
						}
					}
					
				}else{
					//Nun wir der Tagesablauf der Erwachsenen untersucht
					if (this.people.get(i) instanceof Erwachsene){
						
						//Zuerst werden die Erwachsenen untersucht, die Arbeit haben
						if (((Erwachsene)people.get(i)).isHat_arbeit()){
							if (this.spiel_stunde==8 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ // Zur Arbeit gehen
								berechne_weg(this.people.get(i), null, 'E');
							}
							if (this.spiel_stunde==16 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
							}				
							if ((this.spiel_stunde >= 17 || this.spiel_stunde <=0)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), null, 'P');
								}
							}
						} else {
							if (this.spiel_stunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //zum Einkaufen gehen
								if ((int)(Math.random()*3)+1 == 1){
									berechne_weg(this.people.get(i), null, 'E');
								}
							}	
							if (this.spiel_stunde >= 9 && this.spiel_stunde <=14 && hausid!='E'){ //zum Einkaufen gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), null, 'P');
								}
							}
							if (this.spiel_stunde == 12 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
							}
							if ((this.spiel_stunde >=14 || this.spiel_stunde <=1)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), null, 'P');
								}
							}
						}
						if (this.spiel_stunde==1 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
						}
						if (this.spiel_stunde>=2 && this.spiel_stunde<4 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0));
						}
						if (locid =='P' && (this.spiel_stunde < 2 || this.spiel_stunde >= 9)){ //nach Hause gehen
							if ((int)(Math.random()*5) == 3){
								berechne_weg(this.people.get(i), null, String.valueOf(hausid).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									berechne_weg(this.people.get(i), null, 'P');
								}
							}
						}
					}
				}
			
			}
		}
	} 	
	
	
	//Support Tiki
	private Point berechne_Parkeingang(ArrayList<ArrayList<String>> location_ids){
		int parkcounter=0;
		
		// Der Park darf sich niemals ganz am Rand befinden (sieht auch doof aus)
		for (int i = 1; i<14; i++){
			for (int j = 1; j<23; j++){
				if (location_ids.get(i).get(j).equals("P")){
					if (i<13){
						if (location_ids.get(i+1).get(j).equals("P") || location_ids.get(i+1).get(j).equals("X")){
							parkcounter++;
						}
					}
					if (i>0){
						if (location_ids.get(i-1).get(j).equals("P") || location_ids.get(i-1).get(j).equals("X")){
							parkcounter++;
						}
					}
					if (j<22){
						if (location_ids.get(i).get(j+1).equals("P") || location_ids.get(i).get(j+1).equals("X")){
							parkcounter++;
						}
					}
					if (j>0){
						if (location_ids.get(i).get(j-1).equals("P") || location_ids.get(i).get(j-1).equals("X")){
							parkcounter++;
						}
					}
					
				}
				if (parkcounter == 3){
					return new Point (i,j);
				} else{
					parkcounter=0;
				}
			}
		}		
		return null;
	}
	
	
	
	//Support Tiki
	public void berechne_weg(Person person, Agent agent, char zielloc){
		
		char locid = (char)((int)(person.get_location_id()));
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		Point parkeingang = new Point();
		
		location_ids = Ressources.getLocation_ids();
		
		if (zielloc == 'P'){
			parkeingang = berechne_Parkeingang(location_ids);
		}
		
		//Rasterkarte initialisieren 
		
		
		//Aktuelle Position des Männchens wird auf 0 gesetzt
		if (agent == null && !(zielloc == 'P' && person.get_location_id()=='P')){
			location_ids = wegberechnung_rasterkarte_initialisierung(location_ids, String.valueOf(zielloc), locid);
			location_ids.get((person.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((person.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");
		} else {
			location_ids = wegberechnung_rasterkarte_initialisierung(location_ids, "P", locid);
			location_ids.get((int)(parkeingang.getX())).set((int)(parkeingang.getY()),"0");
		}
		
		//Bewegungsstack nach und nach füllen
		neuer_weg = fuelle_bewegungs_stack(location_ids, person, agent, zielloc);
		
		//Stack zur Bewegung freigeben
		person.setMoves(neuer_weg);
	}
	
	
	
	//Support Tiki
	private Stack<Character> fuelle_bewegungs_stack(ArrayList<ArrayList<String>> location_ids, Person person, Agent agent, char zielloc){
		
		Stack<Character> neuer_weg = new Stack<Character>();
		int counter = 1;
		int xPos_current = 0;
		int yPos_current = 0;
		String ziellocation="Z";
		
		goal:	for (int i=0; i<100; i++){
					for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
						for (int k=0; k<25; k++){
							// Es werden Zahlen auf der Map gesucht
							if (j==7){
								System.out.print("testetstetstett");
							}
							if (location_ids.get(j).get(k).equals(String.valueOf(i))){
								// Es wird überprüft, ob das Ziel in direkter Nähe liegt
								if (j < 15){ //15 -> Rasterhöhe
									if (location_ids.get(j+1).get(k).equals(ziellocation)) {
										location_ids.get(j+1).set(k,String.valueOf(i+1));
										counter = i;
										yPos_current = j+1;
										xPos_current = k;
										break goal;
									}
								}
								if (k<24){ //24 -> Rasterbreite
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
//								(zielloc=='P' && ziellocation.equals("P") && i>0) || zielloc != person.get_location_id()
								if ((i>0 && zielloc == person.get_location_id()) || zielloc != person.get_location_id()){
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
								} else { //TODO anpassen!!!
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
					//Wenn eine Person in den Park gehen möchte  und schon im Park ist, braucht man eine andere Ziellocation
					if (agent == null && i == 2 && zielloc=='P'){
						if (person.get_location_id()=='P'){
							ziellocation="0";
						}
					}
				}
		
		// Der Stack für die Bewegung wird mit den richtigen Werten gefüllt. Dafür hangelt man sich absteigend an der zahlenreihe entlang
			neuer_weg = fuelle_stack_weg(zielloc, person, agent, location_ids, counter, xPos_current, yPos_current);
		
		
		return neuer_weg;
	}
	
	//Support Tiki
	private Stack<Character> fuelle_stack_weg(Character zielloc, Person person, Agent agent, ArrayList<ArrayList<String>> location_ids, int counter, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
		
		//Falls das Ziel ein Haus ist, soll die Person auf ihren startpunkt laufen.
		if ((int)(zielloc)-48 <=9 && (int)(zielloc)-48 > 0){ //-48 für char umwandlung zu int
			neuer_weg = fuelle_stack_homeposition(person, agent, xPos_current, yPos_current);
		}
		
		if(person.get_location_id()!=zielloc || (int)(Math.random()*2) == 1){   //|| 
			for (int i = counter; i>=0; i--){
				if (person.get_location_id()==zielloc && i == 0){
					if (yPos_current<15){
						if (location_ids.get(yPos_current+1).get(xPos_current).equals(String.valueOf(counter+1))) {			//unten gehts weiter
							yPos_current++;
							neuer_weg.push('o');
						}
					}
					if (xPos_current<24){
						if (location_ids.get(yPos_current).get(xPos_current+1).equals(String.valueOf(counter+1))) {			//rechts gehts weiter
							xPos_current++;
							neuer_weg.push('l');
						}
					}
					if (yPos_current>0){
						if (location_ids.get(yPos_current-1).get(xPos_current).equals(String.valueOf(counter+1))) {			//oben gehts weiter
							yPos_current--;
							neuer_weg.push('u');
						}
					}
					if (xPos_current>0){
						if (location_ids.get(yPos_current).get(xPos_current-1).equals(String.valueOf(counter+1))) {			//links gehts weiter
							xPos_current--;
							neuer_weg.push('r');
						}
					}
				} else{
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
			}
		} else {
			for (int i = 1; i<=counter+1; i++){
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
		}
		
		
		return neuer_weg;
	}

	//Support Tiki
	private Stack<Character>fuelle_stack_homeposition(Person person, Agent agent, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
		//TODO generisch machen
		
		if (agent == null){
			if (((person.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+1 == xPos_current){
				neuer_weg.push('l');
			}
			if (((person.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+2 == xPos_current){
				neuer_weg.push('l');
				neuer_weg.push('l');
			}
			if (((person.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-1 == xPos_current){
				neuer_weg.push('r');
			}
			if (((person.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-2 == xPos_current){
				neuer_weg.push('r');
				neuer_weg.push('r');
			}
			if (((person.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-1 == yPos_current){
				neuer_weg.push('u');
			}
			if (((person.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-2 == yPos_current){
				neuer_weg.push('u');
				neuer_weg.push('u');
			}
			if (((person.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+1 == yPos_current){
				neuer_weg.push('o');
			}
			if (((person.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+2 == yPos_current){
				neuer_weg.push('o');
				neuer_weg.push('o');
			}
		} else {
			if (((agent.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+1 == xPos_current){
				neuer_weg.push('l');
			}
			if (((agent.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+2 == xPos_current){
				neuer_weg.push('l');
				neuer_weg.push('l');
			}
			if (((agent.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-1 == xPos_current){
				neuer_weg.push('r');
			}
			if (((agent.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-2 == xPos_current){
				neuer_weg.push('r');
				neuer_weg.push('r');
			}
			if (((agent.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-1 == yPos_current){
				neuer_weg.push('u');
			}
			if (((agent.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-2 == yPos_current){
				neuer_weg.push('u');
				neuer_weg.push('u');
			}
			if (((agent.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+1 == yPos_current){
				neuer_weg.push('o');
			}
			if (((agent.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+2 == yPos_current){
				neuer_weg.push('o');
				neuer_weg.push('o');
			}
		}
		
		
		return neuer_weg;
	}

	
	//Support Tiki
	private ArrayList<ArrayList<String>> wegberechnung_rasterkarte_initialisierung(ArrayList<ArrayList<String>> location_ids, String ziellocation, char locid) {
		for (int i=0; i<location_ids.size(); i++){
			for (int j=0; j<location_ids.get(i).size(); j++){
				if (location_ids.get(i).get(j).charAt(0) != 'X' && location_ids.get(i).get(j).charAt(0) != ziellocation.charAt(0) && location_ids.get(i).get(j).charAt(0) != 'P' && location_ids.get(i).get(j).charAt(0) != locid ){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
				if (!(ziellocation.equals("P") && locid == 'P')){
					if (location_ids.get(i).get(j).charAt(0) == ziellocation.charAt(0)){
						location_ids.get(i).set(j,"Z") ;
					}
					if (location_ids.get(i).get(j).charAt(0) == locid ){
						location_ids.get(i).set(j,"X") ;
					}
				} else{
					if (location_ids.get(i).get(j).charAt(0) == 'X'){
						location_ids.get(i).set(j,"You shall not pass!") ;
					}
				}
				if (location_ids.get(i).get(j).charAt(0) == 'P'){
					location_ids.get(i).set(j,"X") ;
				}
				
			}
		}
		return location_ids;
	}
	
	//Support Tiki
	public void bewegungAgentWanze (int zielhaus){
		int counter;
		String ziellocation = String.valueOf(zielhaus);
		int xPos_current, yPos_current;
		char locid = (char)((int)(agent.get_location_id()));
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
//		
		counter = 1;
		xPos_current = 0;
		yPos_current = 0;
		location_ids = Ressources.getLocation_ids();
		
		
		//Rasterkarte initialisieren 
		location_ids = wegberechnung_rasterkarte_initialisierung(location_ids, ziellocation, locid);
		
		//Aktuelle Position des Männchens wird auf 0 gesetzt
		location_ids.get((this.agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((this.agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");

		ziellocation = "Z";
		
		neuer_weg = agentRumwuseln(location_ids);
		
		
goala:	for (int i=0; i<100; i++){
			for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
				for (int k=0; k<25; k++){
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))){
						// Es wird überprüft, ob das Ziel in direkter Nähe liegt
						if (j < 15){ //15 -> Rasterhöhe
							if (location_ids.get(j+1).get(k).equals(ziellocation)) {
								location_ids.get(j+1).set(k,String.valueOf(i+1));
								counter = i;
								yPos_current = j+1;
								xPos_current = k;
								break goala;
							}
						}
						if (k<24){ //24 -> Rasterbreite
							if (location_ids.get(j).get(k+1).equals(ziellocation)) {
								location_ids.get(j).set(k+1,String.valueOf(i+1));
								counter = i;
								yPos_current = j;
								xPos_current = k+1;
								break goala;
							}
						}
						if (j>0){
							if (location_ids.get(j-1).get(k).equals(ziellocation)) {
								location_ids.get(j-1).set(k,String.valueOf(i+1));
								counter = i;
								yPos_current = j-1;
								xPos_current = k;
								break goala;
							}
						}
						if (k>0){
							if (location_ids.get(j).get(k-1).equals(ziellocation)) {
								location_ids.get(j).set(k-1,String.valueOf(i+1));
								counter = i;
								yPos_current = j;
								xPos_current = k-1;
								break goala;
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
		//Stack zur Bewegung freigeben
		this.agent.setMoves(neuer_weg);
	}
	
	
	//Support Tiki
	private Stack<Character> agentRumwuseln(ArrayList<ArrayList<String>> location_ids) {
		Stack<Character> neuer_weg = new Stack<Character>();
		
		for (int i=0; i<15; i++){
			for (int j=0; j<24; j++){
				if ((location_ids.get(i).get(j)=="Z" || location_ids.get(i).get(j)=="0") && location_ids.get(i+1).get(j)=="Z" && location_ids.get(i-1).get(j)=="Z" && location_ids.get(i).get(j+1)=="Z" && location_ids.get(i).get(j-1)=="Z"){
					
					//Nun läuft der Agent lustig hin und her
					neuer_weg.push('r');
					neuer_weg.push('o');
					neuer_weg.push('l');
					neuer_weg.push('u');
					neuer_weg.push('r');
					neuer_weg.push('u');
					neuer_weg.push('l');
					neuer_weg.push('o');
					neuer_weg.push('o');
					neuer_weg.push('l');
					neuer_weg.push('u');
					neuer_weg.push('u');
					neuer_weg.push('r');
					neuer_weg.push('o');
					
					if ((this.agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT!=j || (this.agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT!=i){
						//Der Agent wird zum Mittelpunkt des Hauses geleitet
						if (location_ids.get(i-2).get(j-1).equals("X") || location_ids.get(i-1).get(j-2).equals("X")){
							neuer_weg.push('u');
							neuer_weg.push('r');
						}
						if (location_ids.get(i-2).get(j).equals("X")){
							neuer_weg.push('u');
						}
						if (location_ids.get(i-2).get(j+1).equals("X") || location_ids.get(i-1).get(j+2).equals("X")){
							neuer_weg.push('u');
							neuer_weg.push('l');
						}
						if (location_ids.get(i).get(j+2).equals("X")){
							neuer_weg.push('l');
						}
						if (location_ids.get(i+1).get(j+2).equals("X") || location_ids.get(i+2).get(j+1).equals("X")){
							neuer_weg.push('o');
							neuer_weg.push('l');
						}
						if (location_ids.get(i+2).get(j).equals("X")){
							neuer_weg.push('o');
						}
						if (location_ids.get(i+2).get(j-1).equals("X") || location_ids.get(i+1).get(j-2).equals("X") ){
							neuer_weg.push('o');
							neuer_weg.push('r');
						}
						if (location_ids.get(i).get(j-2).equals("X")){
							neuer_weg.push('r');
						}
					} else{
						neuer_weg.push('o');
					}
					
				}
			}
		}	
		return neuer_weg;
	}


	//Support Tiki
	//TODO Gameover implementieren -> Das Event dafür
	//TODO Gameover Werte berechnen
	public void calc_gamoeover(){
		if (calc_misstrauen_in_street()>=90.0){
			//GAMEOVER!!!
		}
		for (int i = 0; i < this.people.size(); i++){
			if (this.people.get(i) instanceof Terrorist){
				if (this.people.get(i).get_misstrauen() >= 85.00){
					// GAMEOVER!!!
				}
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
