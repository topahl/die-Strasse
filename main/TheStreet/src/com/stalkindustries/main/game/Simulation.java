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
	//private int[] location_raster; nicht mehr ben�tigt
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
		//misstrauen[] ist eine Hilfvariable, die sp�ter die sp�ter die neuen Werte enth�lt und sie wird ben�tigt, dass nicht die echten Werte ver�ndert werden, bevor alle berechnet wurden
		double[] misstrauen = new double[people.size()];
		
		double faktor = 0.01275; //Faktor, der regelt, wie stark sich Personen bei einem Methodenaufruf beeinflussen --> abh�ngig von H�ufigkeit des Methodenaufrufs
		
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
							
							//sorgt daf�r, dass sich das Misstrauen zwischen -100 und 100 bewegt
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
		
		//Risiko einer Beschwichtigenaktion steigt, je h�ufiger man sie ausf�hrt
		risiko = this.people.get(personen_id).get_durchgefuehrteBeschwichtigungen(action_id);
		
		//Fehlschlagen der Aktionen ist zudem abh�ngig vom Misstrauensstatus
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
		//wie sich das Misstrauen ver�ndern soll
		if(zufall < -5)
			misstrauen -= 20;
		else if(zufall < 0)
			misstrauen -= 10;
		else if(zufall < 5)
			misstrauen -= 5;
		else if(zufall > 8)
			misstrauen += 10;
		
		
		//sorgt daf�r, dass sich das Misstrauen zwischen -100 und 100 bewegt
		if(misstrauen>100)
			misstrauen = 100;
		if(misstrauen<-100)
			misstrauen = -100;
		
		//Misstrauen der Person setzen
		this.people.get(personen_id).set_misstrauen(misstrauen);
		
		//Bei Person die Anzahl der Beschwichtigenaktionen erh�hen
		this.people.get(personen_id).set_durchgefuehrteBeschwichtigungen(action_id);
	}
	
	
	//TODO
	//Beschwerden an Miri
	public void calc_misstrauen_after_ueberwachungs_action(int action_id, int house_location){
		//Risiko berechnen
		//Risiko ist abh�ngig von der Uhrzeit, d.h.tags�ber ist das Risiko h�her als nachts
		int risiko=0;
		if(this.spiel_stunde>1 && this.spiel_stunde<7){	//Nachtmodus
			risiko = (int)(Math.random()*3); 
		}
		else{	//Tagmodus
			risiko = (int)(Math.random()*7); 
		}
		
		int mittelpunktX = this.houses.get(house_location-1).getPosX()+3*Ressources.RASTERHEIGHT/2;
		int mittelpunktY = this.houses.get(house_location-1).getPosY()+3*Ressources.RASTERHEIGHT/2;
		int epsilon = 200;	
		
		for(int i=0;i<this.people.size();i++){
//			//Checken, ob sich noch jemand in dem Haus befindet
//			//f�r alle Personen, die noch im Haus sind, das Misstrauen neu berechnen
//			if(this.people.get(i).get_location_id() == house_location){
//				if(risiko>2)	//wenn das risiko kleiner ist, hat man Gl�ck und man wird nicht erwicht
//					this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+50); //TODO: den Wert 50 testen ... eventuell erh�hen
//			}
//			//Checken, ob sich jemand in einer epsilon-Umgebung um das Haus befindet, in das eingebrochen werden soll
//			//--> 1. Epsilon-Umgebung aufspannen (ist eine relative eckige :-D)
//			//-->Mittelpunkt vom Haus bestimmen
//			else{
				// wenn sich eine Person in der Epsilon-Umgebung befindet
				if(this.people.get(i).getPosX() >= mittelpunktX-epsilon && this.people.get(i).getPosX() <= mittelpunktX+epsilon && this.people.get(i).getPosY() >= mittelpunktY-epsilon && this.people.get(i).getPosY() <= mittelpunktY+epsilon){
					//wenn das per Zufall eine Person ist, die in dem Haus wohnt und z.B. auf dem Heimweg ist
					//hier ist das Misstrauen nat�rlich gr��er
					if(this.people.get(i).get_haus_id()+1 == house_location){
						if(risiko>2)
							this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+50);
					}
					else{
						if(risiko>2)
							this.people.get(i).set_misstrauen(this.people.get(i).get_misstrauen()+10);
					}
				}
//			}
			//sorgt daf�r, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.people.get(i).get_misstrauen()>100)
				this.people.get(i).set_misstrauen(100);
			if(this.people.get(i).get_misstrauen()<-100)
				this.people.get(i).set_misstrauen(-100);
		}
		
	}
	
	//Beschwerden Miri
	//Allen H�usern den �berwachungsstatus updaten
	public void calc_ueberwachungsstatus(){
		for(int i=0;i<this.houses.size();i++){
			this.houses.get(i).setUeberwachungsstatus(this.houses.get(i).getUeberwachungsmodule().size()/Haus.MAXUBERWACHUNGSMODULE);
		}
	}
	
	//Beschwerden Miri
	//Mittelwert der �berwachungsstati der einzelnen H�user
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
				
				//Zuerst wird der Tagesablauf der Kinder �berpr�ft, da dieser von den Erwachsenen unterschiedlich ist
				if (this.people.get(i) instanceof Kinder){
					if ((this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){
						
						if (this.spiel_stunde==7){ //zur Schule gehen
							berechne_weg(this.people.get(i), 'E');
						}	
						
						//Nur wenn der Mensch nicht zuHause ist, kann er nach Hause gehen
						if ((this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){
							
							if (this.spiel_stunde==14){ //nach Hause gehen
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));				
							}
							if (this.spiel_stunde==20){ //nach Hause gehen
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
							}	
						} 
					} else {

						//nach Hause gehen, notwendig, falls die Kinder noch eine runde im Park drehen & 20 Uhr �berschritten wird
						if (this.spiel_stunde>=20 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ 
							berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
						}
						
						// randomisiert in den Park gehen
						if (this.spiel_stunde >= 15 && this.spiel_stunde <=19 && locid !='P'){ 
							if ((int)(Math.random()*200) == 3){
								berechne_weg(this.people.get(i), 'P');
							}
						}
						
						// randomisiert den Park verlassen oder noch eine Runde drehen
						if (locid =='P' && this.spiel_stunde<=19){ 
							if ((int)(Math.random()*5) == 3){
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									berechne_rundlauf_park(this.people.get(i));
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
								berechne_weg(this.people.get(i), 'E');
							}
							if (this.spiel_stunde==16 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
							}				
							if ((this.spiel_stunde >= 17 || this.spiel_stunde <=0)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), 'P');
								}
							}
						} else {
							if (this.spiel_stunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) == 60){ //zum Einkaufen gehen
								if ((int)(Math.random()*3)+1 == 1){
									berechne_weg(this.people.get(i), 'E');
								}
							}	
							if (this.spiel_stunde >= 9 && this.spiel_stunde <=14 && hausid!='E'){ //zum Einkaufen gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), 'P');
								}
							}
							if (this.spiel_stunde == 12 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
							}
							if ((this.spiel_stunde >=14 || this.spiel_stunde <=1)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									berechne_weg(this.people.get(i), 'P');
								}
							}
						}
						if (this.spiel_stunde==1 && (this.people.get(i).getZeitverzogerung() + this.spiel_minute) >= 60 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
						}
						if (this.spiel_stunde>=2 && this.spiel_stunde<4 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0));
						}
						if (locid =='P' && (this.spiel_stunde < 2 || this.spiel_stunde >= 9)){ //nach Hause gehen
							if ((int)(Math.random()*5) == 3){
								berechne_weg(this.people.get(i), String.valueOf(hausid).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									berechne_rundlauf_park(this.people.get(i));
								}
							}
						}
					}
				}
			
			}
		}
	} 	
	
	//Support Tiki
	public void berechne_rundlauf_park (Person person){
		//Wenn die Person im Park ist, soll er eine Runde spazieren gehen
		
		int counter;
		String ziellocation = "P";
		int xPos_current, yPos_current;
		char locid = (char)((int)(person.get_location_id()));
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		Point parkeingang = new Point();
		
		counter = 1;
		xPos_current = 0;
		yPos_current = 0;
		location_ids = Ressources.getLocation_ids();
		
		parkeingang= berechne_Parkeingang(location_ids);
		
		
		if ((person.getPosY()-Ressources.ZEROPOS.height)/45 == (int)(parkeingang.getX()) && (person.getPosX()-Ressources.ZEROPOS.width)/45 == (int)(parkeingang.getY())){
			
			
			location_ids = wegberechnung_parkrundlauf_rasterkarte_initialisierung(location_ids, ziellocation, locid);
			location_ids.get((int)(parkeingang.getX())).set((int)(parkeingang.getY()),"0");
			
	goalp:	for (int i=0; i<100; i++){
				for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
					for (int k=0; k<25; k++){
						// Es werden Zahlen auf der Map gesucht
						if (location_ids.get(j).get(k).equals(String.valueOf(i))){
							// Es wird �berpr�ft, ob das Ziel in direkter N�he liegt
							if (i>2){
								if (j < 15){ //15 -> Rasterh�he
									if (location_ids.get(j+1).get(k).equals("0")) {
										counter = i;
										yPos_current = j+1;
										xPos_current = k;
										break goalp;
									}
								}
								if (k<24){ //24 -> Rasterbreite
									if (location_ids.get(j).get(k+1).equals("0")) {
										counter = i;
										yPos_current = j;
										xPos_current = k+1;
										break goalp;
									}
								}
								if (j>0){
									if (location_ids.get(j-1).get(k).equals("0")) {
										counter = i;
										yPos_current = j-1;
										xPos_current = k;
										break goalp;
									}
								}
								if (k>0){
									if (location_ids.get(j).get(k-1).equals("0")) {
										counter = i;
										yPos_current = j;
										xPos_current = k-1;
										break goalp;
									}
								}
							}
							// Es wird �berpr�ft, ob ein Feld dr�ber/drunter/links oder rechts ebenfalls begehbar ist -> das wird markiert
							if (i>0){
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
							} else {
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
			}
			if ((int)(Math.random()*2) == 1){
				neuer_weg= wegberechnung_parklinks_fuelle_stack(location_ids, counter, xPos_current, yPos_current);
			} else {
				neuer_weg= wegberechnung_parkrechts_fuelle_stack(location_ids, counter, xPos_current, yPos_current);

			}
			
			person.setMoves(neuer_weg);
			return;
		}
	}
	
	//Support Tiki
	private Stack<Character> wegberechnung_parkrechts_fuelle_stack(ArrayList<ArrayList<String>> location_ids, int counter, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
		
		for (int i = 1; i<=counter+1; i++){
			if (i==counter+1){
				if (yPos_current<15){
					if (location_ids.get(yPos_current+1).get(xPos_current).equals("0")) {			//unten gehts weiter
					yPos_current++;
					neuer_weg.push('o');
					}
				}
				if (xPos_current<24){
					if (location_ids.get(yPos_current).get(xPos_current+1).equals("0")) {			//rechts gehts weiter
					xPos_current++;
					neuer_weg.push('l');
					}
				}
				if (yPos_current>0){
					if (location_ids.get(yPos_current-1).get(xPos_current).equals("0")) {			//oben gehts weiter
					yPos_current--;
					neuer_weg.push('u');
					}
				}
				if (xPos_current>0){
					if (location_ids.get(yPos_current).get(xPos_current-1).equals("0")) {			//links gehts weiter
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
		return neuer_weg;
	}

	//Support Tiki
	private Stack<Character> wegberechnung_parklinks_fuelle_stack(ArrayList<ArrayList<String>> location_ids, int counter, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
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
		
		return neuer_weg;
	}	
		
	//Support Tiki
	private ArrayList<ArrayList<String>> wegberechnung_parkrundlauf_rasterkarte_initialisierung(ArrayList<ArrayList<String>> location_ids, String ziellocation, char locid) {
		for (int i=0; i<location_ids.size(); i++){
			for (int j=0; j<location_ids.get(i).size(); j++){
				if (location_ids.get(i).get(j).charAt(0) == 'X' || (location_ids.get(i).get(j).charAt(0) != ziellocation.charAt(0) && location_ids.get(i).get(j).charAt(0) != 'P' && location_ids.get(i).get(j).charAt(0) != locid )){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == 'P'){
					location_ids.get(i).set(j,"X") ;
				}
			}
		}
		return location_ids;
		
	}
	
	
	//Support Tiki
	private Point berechne_Parkeingang(ArrayList<ArrayList<String>> location_ids){
		int parkcounter=0;
		
		// Der Park darf sich niemals ganz am Rand befinden (sieht auch doof aus)
		for (int i = 1; i<14; i++){
			for (int j = 1; j<23; j++){
				if (location_ids.get(i).get(j).equals("P")){
					if (location_ids.get(i+1).get(j).equals("P") || location_ids.get(i+1).get(j).equals("X")){
						parkcounter++;
					}
					if (location_ids.get(i-1).get(j).equals("P") || location_ids.get(i-1).get(j).equals("X")){
						parkcounter++;
					}
					if (location_ids.get(i).get(j+1).equals("P") || location_ids.get(i).get(j+1).equals("X")){
						parkcounter++;
					}
					if (location_ids.get(i).get(j-1).equals("P") || location_ids.get(i).get(j-1).equals("X")){
						parkcounter++;
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
	public void berechne_weg(Person person, char zielloc){
		
		int counter;
		String ziellocation = String.valueOf(zielloc);
		int xPos_current, yPos_current;
		char locid = (char)((int)(person.get_location_id()));
		ArrayList<ArrayList<String>> location_ids;
		Stack<Character> neuer_weg = new Stack<Character>();
		
		counter = 1;
		xPos_current = 0;
		yPos_current = 0;
		location_ids = Ressources.getLocation_ids();
		
		
		//Rasterkarte initialisieren 
		location_ids = wegberechnung_rasterkarte_initialisierung(location_ids, ziellocation, locid);
		
		ziellocation = "Z";
		
		//Aktuelle Position des M�nnchens wird auf 0 gesetzt
		location_ids.get((person.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((person.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");
				
		
		
		
goal:	for (int i=0; i<100; i++){
			for (int j=0; j<16; j++){  	// J entspricht y-wert, K entspricht x-wert
				for (int k=0; k<25; k++){
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))){
						// Es wird �berpr�ft, ob das Ziel in direkter N�he liegt
						if (j < 15){ //15 -> Rasterh�he
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
						
						
						// Es wird �berpr�ft, ob ein Feld dr�ber/drunter/links oder rechts ebenfalls begehbar ist -> das wird markiert
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
		
		// Der Stack f�r die Bewegung wird mit den richtigen Werten gef�llt. Daf�r hangelt man sich absteigend an der zahlenreihe entlang
		neuer_weg = wegberechnung_fuelle_stack(zielloc, person, location_ids, counter, xPos_current, yPos_current);
		
		//Stack zur Bewegung freigeben
		person.setMoves(neuer_weg);
	}
	
	
	//Support Tiki
	private Stack<Character> wegberechnung_fuelle_stack(Character zielloc, Person person, ArrayList<ArrayList<String>> location_ids, int counter, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
		
		//Falls das Ziel ein Haus ist, soll die Person auf ihren startpunkt laufen.
		if ((int)(zielloc)-48 <=9 && (int)(zielloc)-48 > 0){ //-48 f�r char umwandlung zu int
			neuer_weg = wegberechnung_homeposition(person, xPos_current, yPos_current);
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
		return neuer_weg;
	}

	//Support Tiki
	private Stack<Character> wegberechnung_homeposition(Person person, int xPos_current, int yPos_current) {
		Stack<Character> neuer_weg = new Stack<Character>();
		
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
		return neuer_weg;
	}

	
	//Support Tiki
	private ArrayList<ArrayList<String>> wegberechnung_rasterkarte_initialisierung(ArrayList<ArrayList<String>> location_ids, String ziellocation, char locid) {
		for (int i=0; i<location_ids.size(); i++){
			for (int j=0; j<location_ids.get(i).size(); j++){
				if (location_ids.get(i).get(j).charAt(0) != 'X' && location_ids.get(i).get(j).charAt(0) != ziellocation.charAt(0) && location_ids.get(i).get(j).charAt(0) != 'P' && location_ids.get(i).get(j).charAt(0) != locid ){
					location_ids.get(i).set(j,"You shall not pass!") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == ziellocation.charAt(0)){
					location_ids.get(i).set(j,"Z") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == 'P'){
					location_ids.get(i).set(j,"X") ;
				}
				if (location_ids.get(i).get(j).charAt(0) == locid ){
					location_ids.get(i).set(j,"X") ;
				}
			}
		}
		return location_ids;
	}
	
	//Support Tiki
	public void bewegungAgentWanze (int zielhaus){
		
	}
	
	

	//Support Tiki
	//TODO Gameover implementieren -> Das Event daf�r
	//TODO Gameover Werte berechnen
	public void calc_gamoeover(){
		if (calc_misstrauen_in_street()>=90.0){
			//GAMEOVER!!!
		}
		for (int i = 0; i < this.people.size(); i++){
			if (this.people.get(i) instanceof Terrorist){
				if (this.people.get(i).get_misstrauen() >= 90.00){
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
