package com.stalkindustries.main.game;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;

import com.stalkindustries.main.TheStreet;


/**
 * @author Hauptsächlich Miriam & Martika
 */
public class Simulation {
	
	private int[][] beziehungsmatrix;
	private ArrayList<Person> people = new ArrayList<Person>(); 
	private Agent agent = new Agent(0,"");
	private int spielTag=1;
	private int spielStunde=7;
	private int spielMinute=0;
	private ArrayList<Haus> houses = new ArrayList<Haus>();
	private boolean wieschteAktion=true;  //wiescht = boese
	private double misstrauenMax=-100;

	
	public Simulation(){
		
	}
	
	
	/**
	 * @author Miriam
	 */
	void initialisiereBeziehungsmatrix(){
		int tmp;
		this.beziehungsmatrix = new int[this.people.size()][this.people.size()];
		for(int i=0;i<this.people.size();i++)
			for(int j=0;j<this.people.size();j++)
				this.beziehungsmatrix[i][j] = 0;
		
		//asymmetrisch
		for(int i=0;i<this.people.size();i++){
			for(int j=0;j<this.people.size();j++){
				if(i!=j){
					tmp = (int)(Math.random()*(10))+1;
					this.beziehungsmatrix[i][j] = tmp;
					if(this.people.get(i).getHausId() == this.people.get(j).getHausId()){ //Person in einem Haushalt sind besser miteinander befreundet
						tmp = (10-tmp)/2;
						this.beziehungsmatrix[i][j] += tmp;
						this.beziehungsmatrix[j][i] += tmp;
					}
				}
			}
		}
	}
	
	
	
	/**
	 * @author Miriam
	 */
	void calcMisstrauen(){
		if(this.spielStunde > 5 || this.spielStunde < 2){
			//misstrauen[] ist eine Hilfvariable, die später die später die neuen Werte enthält und sie wird benötigt, dass nicht die echten Werte verändert werden, bevor alle berechnet wurden
			double[] misstrauen = new double[people.size()];
			
			double faktor = 0.01275; //Faktor, der regelt, wie stark sich Personen bei einem Methodenaufruf beeinflussen --> abhängig von Häufigkeit des Methodenaufrufs
			
			//misstrauen[] mit den alten Misstrauenswerten initialisieren
			for(int i=0;i<this.people.size();i++)
				misstrauen[i] = this.people.get(i).getMisstrauen();
			
			//jede Person kann theoretisch wieder von jeder anderen beeinflusst werden
			for(int i=0;i<this.people.size();i++){
				//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
				if((int)(this.people.get(i).getLocationId())-48+1 != 0 && (int)(this.people.get(i).getLocationId())-48+1 != 'X' && (int)(this.people.get(i).getLocationId())-48+1 != 'E'){
					for(int j=0;j<this.people.size();j++){
						//eine Person kann sich nicht selbst beeinflussen
						if(i!=j){
							//wenn sich die beiden Personen am selben Ort befinden
							if((int)(this.people.get(i).getLocationId())-48+1 == (int)(this.people.get(j).getLocationId())-48+1){
								//Ausnahme in der Berechnung: Kinder beeinflussen Erwachsene weniger
								if(this.people.get(i) instanceof Erwachsene && this.people.get(j) instanceof Kinder){	//Kind beeinflusst Erwachsenen weniger
									misstrauen[i] = misstrauen[i] - faktor/2*this.beziehungsmatrix[i][j]*(this.people.get(i).getMisstrauen()-this.people.get(j).getMisstrauen());
								}
								else{
									misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]*(this.people.get(i).getMisstrauen()-this.people.get(j).getMisstrauen());
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
				this.people.get(i).setMisstrauen(misstrauen[i]);
			}
		}
	}
	
	/**
	 * @author Tobias
	 */
		public void updatePosition(){
			for(int i=0;i<people.size();i++){
				people.get(i).step();
			}
		}
	
		
		/**
		 * Mittelwert des Misstrauens der einzelnen Personen
		 * @author Miriam
		 */
	public double calcMisstrauenInStreet(){
		float misstrauen = 0;
		for(int i=0;i<this.people.size();i++){
			misstrauen += this.people.get(i).getMisstrauen();
		}
		misstrauen = misstrauen/this.people.size();
		
		return misstrauen;
	}
	
	
	/**
	 * @author Miriam
	 */
	public void calcMisstrauenNachBeschwichtigenInPark(){
		for(int i=0;i<this.people.size();i++){
			//Checken, ob sich noch jemand im Park befindet
			if(this.people.get(i).getLocationId() == 'P'){						
				this.people.get(i).setMisstrauen(this.people.get(i).getMisstrauen()-1); 
			}
			
			//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.people.get(i).getMisstrauen()>100)
				this.people.get(i).setMisstrauen(100);
			if(this.people.get(i).getMisstrauen()<-100)
				this.people.get(i).setMisstrauen(-100);
		}
	}
	
	
	
	/**
	 * @author Miriam
	 */
    public void calcMisstrauenNachBeschwichtigen(int actionId, Person person){
        int zufall=0;
        int risiko;
        double misstrauen = person.getMisstrauen();
        
        //Fehlschlagen der Aktionen ist unter anderem abhängig vom Misstrauensstatus
                if(misstrauen >= 0){
                    zufall = (int)(Math.random()*(misstrauen/10));
                    if(zufall == 0)
                        zufall = 1;
                }
                //Personen sind weniger misstrauisch
                else{
                    if(misstrauen/10 < 0){
                        zufall = -(int)(Math.random()*(-(misstrauen/10)));
                        if(zufall == 0)
                            zufall = -1;
                    }
//                    else
//                        zufall = (int)(Math.random()*(misstrauen/10));
                }
        
        //Risiko einer Beschwichtigenaktion steigt, je häufiger man sie ausführt
        risiko = person.getDurchgefuehrteBeschwichtigungen(actionId);
        if(actionId == 0 || actionId == 2 || actionId == 3){    //Kuchen, Flirten, Helfen
            if(risiko == 0){
                if(zufall > 5)
                    zufall *= (-1);
                else if(zufall >= 0)
                    zufall *= (-10);
                else
                    zufall *= 10;
            }
            else{
                if(zufall > 5)
                    zufall *= 6;
                else if(zufall >= 0)
                    zufall *= 6;
                else
                    zufall *= (-3);
            }
        }
        else if(actionId == 1){    //bei Haus vorbeigehen, um zu reden
            if(risiko <= 3)
                if(zufall >= 0)
                    zufall *= (-3);
                else
                    zufall *= 4;
            else
                if(zufall >= 0)
                    zufall *= 3;
                else
                    zufall *= (-1); 
        }
        else{                        //Im Park unterhalten
        	if(zufall >= 0)
        		zufall *= (-2);
            else
                zufall *= 3;
        }
        
        misstrauen += zufall;
        
        
        //sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
        if(misstrauen>100)
            misstrauen = 100;
        if(misstrauen<-100)
            misstrauen = -100;
        
        //Misstrauen der Person setzen
        person.setMisstrauen(misstrauen);
    }
	
	
    /**
	 * @author Miriam
	 */
	public void calcMisstrauenNachUeberwachung(int hausId){
		//Risiko berechnen
		//Risiko ist abhängig von der Uhrzeit, d.h.tagsüber ist das Risiko höher als nachts
		int risiko=0;
		if(this.spielStunde>1 && this.spielStunde<6){	//Nachtmodus
			risiko = (int)(Math.random()*3); 
		}
		else{	//Tagmodus
			risiko = (int)(Math.random()*15); 
		}
		
		int mittelpunktX = this.houses.get(hausId-1).getPosX()+3*Ressources.RASTERHEIGHT/2;
		int mittelpunktY = this.houses.get(hausId-1).getPosY()+3*Ressources.RASTERHEIGHT/2;
		int epsilon = 200;	
		
		for(int i=0;i<this.people.size();i++){
			//Checken, ob sich noch jemand in dem Haus befindet
			//für alle Personen, die noch im Haus sind, das Misstrauen neu berechnen
			if((int)(this.people.get(i).getLocationId())-48 == hausId){
				if(risiko>2)	//wenn das risiko kleiner ist, hat man Glück und man wird nicht erwicht
					this.people.get(i).setMisstrauen(this.people.get(i).getMisstrauen()+6); 
			}
			//Checken, ob sich jemand in einer epsilon-Umgebung um das Haus befindet, in das eingebrochen werden soll
			//--> 1. Epsilon-Umgebung aufspannen (ist eine relative eckige :-D)
			//-->Mittelpunkt vom Haus bestimmen
			else{
				// wenn sich eine Person in der Epsilon-Umgebung befindet
				if(this.people.get(i).getPosX() >= mittelpunktX-epsilon && this.people.get(i).getPosX() <= mittelpunktX+epsilon && this.people.get(i).getPosY() >= mittelpunktY-epsilon && this.people.get(i).getPosY() <= mittelpunktY+epsilon && this.people.get(i).getLocationId()!='E'){
					//wenn das per Zufall eine Person ist, die in dem Haus wohnt und z.B. auf dem Heimweg ist
					//hier ist das Misstrauen natürlich größer
					if(this.people.get(i).getHausId()+1 == hausId){
						if(risiko>2)
							this.people.get(i).setMisstrauen(this.people.get(i).getMisstrauen()+2);
					}
					else{
						if(risiko>2)
							this.people.get(i).setMisstrauen(this.people.get(i).getMisstrauen()+1);
					}
				}
			}
			//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.people.get(i).getMisstrauen()>100)
				this.people.get(i).setMisstrauen(100);
			if(this.people.get(i).getMisstrauen()<-100)
				this.people.get(i).setMisstrauen(-100);
		}
		
	}
	
	
	/**
	 * @author Miriam
	 */
	public void agentBetrittFremdesHaus(){
		//wenn sich der Agent in irgendeinem Haus befindet
		if((int)(getAgent().getLocationId())-48 >= 1 && (int)(getAgent().getLocationId())-48 <= 9){
			//wenn sich der Agent in dem Haus befindet, das ausspioniert werden soll
			if(getAgent().getLocationId() != (char)getAgent().getHausId()+48+1){
				if (wieschteAktion){
					calcMisstrauenNachUeberwachung((int)(getAgent().getLocationId()-48));
				}
					
			}
		}
	}	
	
	
	/**
	 * während ein Haus überwacht wird, macht sich bei den Bewohnern ein Unwohlsein breit und sie werden mehr misstrauisch
	 * @author Miriam
	 */
	public void calcMisstrauenDuringUeberwachung(){
		for(int i=0;i<this.people.size();i++){
			// wenn Überwachungsmodule in dem Haus, in dem die Person lebt, installiert wurden
			if(this.houses.get(this.people.get(i).getHausId()).getUeberwachungsmodule().size() > 0){
				this.people.get(i).setMisstrauen(this.people.get(i).getMisstrauen()+2);
			}
			//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.people.get(i).getMisstrauen()>100)
				this.people.get(i).setMisstrauen(100);
			if(this.people.get(i).getMisstrauen()<-100)
				this.people.get(i).setMisstrauen(-100);
		}
	}
	
	
	/**
	 * Allen Häusern den Überwachungsstatus updaten
	 * @author Miriam
	 */
	public void updateUeberwachungsstatus(){
		float ueberwachungswert=0;
		
		
		for (int i=0; i<getHouses().size(); i++){
			getHouses().get(i).setUeberwachungsstatus(0);
			for (int j=0; j<getHouses().get(i).getUeberwachungsmodule().size(); j++){
				if (getHouses().get(i).getUeberwachungsmodule().get(j).equals("Wanze")){
					ueberwachungswert = ueberwachungswert + getHouses().get(i).getUeberwachungsWert(0);
				}
				if (getHouses().get(i).getUeberwachungsmodule().get(j).equals("Kamera")){
					ueberwachungswert = ueberwachungswert + getHouses().get(i).getUeberwachungsWert(1);
				}
				if (getHouses().get(i).getUeberwachungsmodule().get(j).equals("Hacken")){
					ueberwachungswert = ueberwachungswert + getHouses().get(i).getUeberwachungsWert(2);
				}
				if (getHouses().get(i).getUeberwachungsmodule().get(j).equals("Fernglas")){
					ueberwachungswert = ueberwachungswert + getHouses().get(i).getUeberwachungsWert(3);
				}
				if (ueberwachungswert > 100){
					ueberwachungswert = 100;
				}
				getHouses().get(i).setUeberwachungsstatus(ueberwachungswert);
				if (ueberwachungswert >=0){
					calcColouredPeople(i);
				}
			} 
			ueberwachungswert=0;
		}
	}
	
	/**
	 * Mittelwert der Überwachungsstati der einzelnen Häuser
	 * @author Miriam
	 */
	public float calcUeberwachungsstatusInStreet(){
		float ueberwachung = 0;
		for(int i=0;i<this.houses.size();i++){
			ueberwachung += this.houses.get(i).getUeberwachungsstatus();
		}
		ueberwachung = ueberwachung/(this.houses.size()-1);
		
		return ueberwachung;
	}
	

	
	
	/**
	 * Durch den GUILayer wird regelmäßig diese Methode aufgerufen
	 * Es wird die Spielzeit um eine Minute erhöht
	 * @author Martika
	 */
	void calcSpielzeit(){
		this.spielMinute++;
		if (this.spielMinute==60){
			this.spielMinute = 0;
			this.spielStunde++;
		}
		if (this.spielStunde==24){
			this.spielStunde=0;
			this.spielTag++;
		}
	}
	
	
	/**
	 * Kinder gehen ab 7 Uhr zur Schule
	 * Erwachsene ab 8 Uhr zur Arbeit (wenn sie welche haben)
	 * Arbeitslose können um 9 Uhr Einkaufen gehen
	 * ab 12 Uhr kommen Arbeitslose vom Einkaufen zurück
	 * Ab 14 Uhr kommen die Kinder aus der Schule
	 * Ab 16 Uhr die Erwachsenen vom Arbeiten
	 * Um 20 Uhr müssen die Kinder nach Hause gehen
	 * Um 2 Uhr müssen die Erwachsenen nach Hause gehen
	 * @author Martika
	 */
	public void tagesablauf(){
		char locationId ='0';
		int hausId = 0;
		for	(int i=0; i<this.people.size(); i++){
			locationId = this.people.get(i).getLocationId();
			hausId = this.people.get(i).getHausId()+1;
			
			// Es bekommen nur die Menschen einen neuen Weg zugewiesen, wenn sie sich gerade nicht bewegen
			if (this.people.get(i).getCurrentMove() == 'n'){
				
				//Zuerst wird der Tagesablauf der Kinder überprüft, da dieser von den Erwachsenen unterschiedlich ist
				if (this.people.get(i) instanceof Kinder){
					if ((this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){
						
						if (this.spielStunde==7 && this.people.get(i).getLocationId()!='E'){ //zur Schule gehen
							calcWeg(this.people.get(i), 'E');
						}	
						
						//Nur wenn der Mensch nicht zuHause ist, kann er nach Hause gehen
						if ((this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){
							
							if (this.spielStunde==14){ //nach Hause gehen
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));				
							}
							if (this.spielStunde==20){ //nach Hause gehen
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
							}	
						} 
					} else {

						//nach Hause gehen, notwendig, falls die Kinder noch eine runde im Park drehen & 20 Uhr überschritten wird
						if (this.spielStunde>=20 && (this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ 
							calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
						}
						
						// randomisiert in den Park gehen
						if (this.spielStunde >= 15 && this.spielStunde <=19 && locationId !='P'){ 
							if ((int)(Math.random()*200) == 3){
								calcWeg(this.people.get(i), 'P');
							}
						}
						
						// randomisiert den Park verlassen oder noch eine Runde drehen
						if (locationId =='P' && this.spielStunde<=19){ 
							if ((int)(Math.random()*5) == 3){
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									calcWeg(this.people.get(i), 'P');
								}
							}
						}
					}
					
				}else{
					//Nun wir der Tagesablauf der Erwachsenen untersucht
					if (this.people.get(i) instanceof Erwachsene){
						
						//Zuerst werden die Erwachsenen untersucht, die Arbeit haben
						if (((Erwachsene)people.get(i)).isHatArbeit()){
							if ((this.spielStunde==8 || this.spielStunde==9) && this.people.get(i).getLocationId()!='E' && (this.people.get(i).getZeitverzogerung() + this.spielMinute) >= 60){ // Zur Arbeit gehen
								calcWeg(this.people.get(i), 'E');
							}
							if (this.spielStunde==16 && 
									(this.people.get(i).getZeitverzogerung() + this.spielMinute) >= 60 && 
									(this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || 
									this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
							}				
							if ((this.spielStunde >= 17 || this.spielStunde <=0)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									calcWeg(this.people.get(i), 'P');
								}
							}
						} else {
							if (this.spielStunde == 9 && (this.people.get(i).getZeitverzogerung() + this.spielMinute) == 60){ //zum Einkaufen gehen
								if ((int)(Math.random()*3)+1 == 1){
									calcWeg(this.people.get(i), 'E');
								}
							}	
							if (this.spielStunde >= 9 && this.spielStunde <=14 && hausId!='E'){ //in den Park gehen
								if ((int)(Math.random()*300) == 3){
									calcWeg(this.people.get(i), 'P');
								}
							}
							if (this.spielStunde == 12 && 
									(this.people.get(i).getZeitverzogerung() + this.spielMinute) >= 60 && 
									(this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || 
									this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
							}
							if ((this.spielStunde >=14 || this.spielStunde <=1)){  // in den Park gehen
								if ((int)(Math.random()*300) == 3){
									calcWeg(this.people.get(i), 'P');
								}
							}
						}
						if (this.spielStunde==1 && 
								(this.people.get(i).getZeitverzogerung() + this.spielMinute) >= 60 && 
								(this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || 
								this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
						}
						if (this.spielStunde>=2 && this.spielStunde<5 && 
								(this.people.get(i).getHomePosX()!=this.people.get(i).getPosX() || 
								this.people.get(i).getHomePosY()!=this.people.get(i).getPosY())){ //nach Hause gehen
							calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0));
						}
						if (locationId =='P' && (this.spielStunde < 2 || this.spielStunde >= 9)){ //nach Hause gehen
							if ((int)(Math.random()*5) == 3){
								calcWeg(this.people.get(i), String.valueOf(hausId).charAt(0)); 
							} else{
								if (this.people.get(i).getCurrentMove() == 'n'){
									calcWeg(this.people.get(i), 'P');
								}
							}
						}
					}
				}
			}
		}
		//Der Agent soll nach 21 Uhr nach Hause gehen, sollte er sich in einem fremden Haus befinden
		if (getAgent().getLocationId() != (char)(getAgent().getHausId()+48+1) && (this.spielStunde>=21 || 
			this.spielStunde<6) && !wieschteAktion && getAgent().getCurrentMove()=='n' &&
			getAgent().getLocationId() != 'P'){
			calcWeg(getAgent(), (char)(getAgent().getHausId()+48+1));
		}
		//Der Agent soll nach 2 Uhr nach Hause gehen, sollte er sich im Park befinden,
		//Das dient zur Agententarnung, da jeder um 2 Uhr nach Hause geht
		if (getAgent().getLocationId() != (char)(getAgent().getHausId()+48+1) && this.spielStunde>=2 && 
				this.spielStunde<6 && !wieschteAktion && getAgent().getCurrentMove()=='n'){
				calcWeg(getAgent(), (char)(getAgent().getHausId()+48+1));
			}
	} 	
	
	
	
	/**
	 * Durch diese Methode wird der Weg zur zielloc berechnet
	 * Es wird der Stack der jeweiligen Person für die Bewegung gefüllt
	 * @param mensch Es wird eine Person oder ein Agent in Mensch gecasted
	 * @param zielloc gibt das Ziel der Person an (1-9, P, E)
	 * @author Martika
	 */
	public void calcWeg(Mensch mensch, char zielLoc){
		
		//Bewegung wird durch folgende Taktik ermittelt:
		//Eine Arraylist in einer Arraylist bildet die Karte des jeweiligen Landes ab
		//Alle begehbaren Flächern werden als 'X' markiert. Die Häuser mit den Zahlen 1-9, 
		//Kartenausgänge mit 'E' und der Park mit 'P'
		// die aktuelle Position des Menschen wird auf 0 gesetzt
		//Nun wird immer für jede Himmelsrichtung geguckt, ob die Fläche begehbar ist. Wenn ja, schreibe eine 1 rein
		//Danach werden alle 1er untersucht, ob die Nachbarflächen begehbar sind -> schreibe 2 rein
		//Bis man am Ziel ist wird der Weg so hochgezählt.
		//Ist man am Ziel , hangelt man sich rückwärtz zur aktuellen Position runter und 
		//schreibt entsprechenede Bewegungen in den Stack
		
		
		char locationId; //Aktuelle Position des Menschen				
		ArrayList<ArrayList<String>> locationIds; //Rasterkarte der jeweiligen Map
		Stack<Character> neuerWeg = new Stack<Character>(); //Stack für die Bewegung
		
		locationId = mensch.getLocationId();			
		locationIds = Ressources.getLocation_ids();
		
		//Die Karte wird initialisiert
		if (zielLoc != locationId || zielLoc!='P'){
			locationIds = rasterkarteInitialisieren(locationIds, String.valueOf(zielLoc), locationId);
		} else {
			locationIds = rasterkarteInitialisieren(locationIds, "P", locationId);
		}

		//Die aktuelle Position des Menschen wird auf '0' gesetzt. Das hängt von der aktuellen Bewegung des
		//Menschen ab. Wenn der Mensch gerade in einer bewegung nach untern oder nach rechts ist kann das zu
		//unschönen Ergebnissen führen - Mensch läuft um ein Feld verschoben
			switch(mensch.getCurrentMove()){
	        case 'r': locationIds.get((mensch.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set(((mensch.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+1,"0");
	                  break;
	        case 'u': locationIds.get(((mensch.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+1).set((mensch.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");
	                  break;
	        case 'o':
	        case 'l':
	        default: locationIds.get((mensch.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((mensch.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");
	        }
		
			
		//Bewegungsstack wird gefüllt
		neuerWeg = calcWegInRasterkarte(locationIds, mensch, zielLoc);
			
		//Stack zur Bewegung wird gesetzt
		mensch.setMoves(neuerWeg);
	}
	
	
	/**
	 * Es werden die Zahlen in die Karte geschrieben, so wie sich der Mensch bewegen darf
	 * @param location_ids Die Rasterkarte mit dem Zahlen-Weg
	 * @param mensch Person oder Agent in Mensch gecasted
	 * @param zielloc ziel '1-9', 'P' oder 'E'
	 * @return Bewegungsstack
	 * @author Martika
	 */
	private Stack<Character> calcWegInRasterkarte(ArrayList<ArrayList<String>> location_ids, Mensch mensch, char zielloc){
		
		Stack<Character> neuer_weg = new Stack<Character>();
		int counter = 1;		//Wird auf die Zahl gesetzt, die am Ende in der Rasterkarte im ziel steht
								//so weiß man wieviele Schritte der Mensch gehen muss
		int xPos_current = 0; 	//Wird gesetzt auf die Zielposition, wenn diese Ermittelt wurde
		int yPos_current = 0; 	//Wird gesetzt auf die Zielposition, wenn diese Ermittelt wurde
		boolean firstStep = true; //Ist für den Kreisweg im Park wichtig
		String ziellocation="Z";
		
		goal:	for (int i=0; i<100; i++){
					for (int j=0; j<Ressources.MAPHEIGHT/Ressources.RASTERHEIGHT; j++){  	// J entspricht y-wert, K entspricht x-wert
						for (int k=0; k<Ressources.MAPWIDTH/Ressources.RASTERHEIGHT; k++){
							// Es werden Zahlen auf der Map gesucht
							if (location_ids.get(j).get(k).equals(String.valueOf(i))){
								// Es wird überprüft, ob das Ziel in direkter Nähe liegt
								if (valuesInRange(k, j+1)){ 
									if (location_ids.get(j+1).get(k).equals(ziellocation)) {
										//Ziel wurde erreicht
										location_ids.get(j+1).set(k,String.valueOf(i+1));
										counter = i;
										yPos_current = j+1;
										xPos_current = k;
										break goal;    	//Alternative für die hässliche break-marke wäre 
														//eine eigene Methode mit eigenem Rückgabeobjekt,
														//also eine neue Klasse
									}
								}
								if (valuesInRange(k+1, j)){ 
									if (location_ids.get(j).get(k+1).equals(ziellocation)) {
										//Ziel wurde erreicht
										location_ids.get(j).set(k+1,String.valueOf(i+1));
										counter = i;
										yPos_current = j;
										xPos_current = k+1;
										break goal;
									}
								}
								if (valuesInRange(k, j-1)){
									if (location_ids.get(j-1).get(k).equals(ziellocation)) {
										//Ziel wurde erreicht
										location_ids.get(j-1).set(k,String.valueOf(i+1));
										counter = i;
										yPos_current = j-1;
										xPos_current = k;
										break goal;
									}
								}
								if (valuesInRange(k-1, j)){
									if (location_ids.get(j).get(k-1).equals(ziellocation)) {
										//Ziel wurde erreicht
										location_ids.get(j).set(k-1,String.valueOf(i+1));
										counter = i;
										yPos_current = j;
										xPos_current = k-1;
										break goal;
									}
								}
							
								if ((i>0 && (zielloc == mensch.getLocationId())) ||
										(zielloc != mensch.getLocationId())){
									// ist ein Feld drüber/drunter/links oder rechts ebenfalls begehbar?
									//-> das wird mit einer um eins höheren Zahl markiert
									if (valuesInRange(k, j+1)){
										if (location_ids.get(j+1).get(k).equals("X")) {	
											//Weg nach unten ist begehbar
											location_ids.get(j+1).set(k,String.valueOf(i+1));
										}
									}
									if (valuesInRange(k+1, j)){
										if (location_ids.get(j).get(k+1).equals("X")) {		
											//Weg nach rechts ist begehbar
											location_ids.get(j).set(k+1,String.valueOf(i+1));
										}
									}
									if (valuesInRange(k, j-1)){
										if (location_ids.get(j-1).get(k).equals("X")) {			
											// Weg nach oben ist begehbar
											location_ids.get(j-1).set(k,String.valueOf(i+1));
										}
									}
									if (valuesInRange(k-1, j)){
										if (location_ids.get(j).get(k-1).equals("X")) {			
											//Weg nach links ist begehbar
											location_ids.get(j).set(k-1,String.valueOf(i+1));
										}
									}
								} else { 
									//Im Park darf man sich nur in eine Richtung ausbreiten
									if (valuesInRange(k, j)){
										if (location_ids.get(j-1).get(k).equals("X") && firstStep) {
											// Weg nach oben ist begehbar
											location_ids.get(j-1).set(k,String.valueOf(i+1));
											firstStep = false;
										}
									}
									if (valuesInRange(k-1, j)){
										if (location_ids.get(j).get(k-1).equals("X") && firstStep) {
											//Weg nach links ist begehbar
											location_ids.get(j).set(k-1,String.valueOf(i+1));
											firstStep = false;
										}
									}
									if (valuesInRange(k+1, j)){
										if (location_ids.get(j).get(k+1).equals("X") && firstStep) {
											//Weg nach rechts ist begehbar
											location_ids.get(j).set(k+1,String.valueOf(i+1));
											firstStep = false;
										}
									}
									if (valuesInRange(k, j+1)){
										if (location_ids.get(j+1).get(k).equals("X") && firstStep) {
											//Weg nach unten ist begehbar
											location_ids.get(j+1).set(k,String.valueOf(i+1));
											firstStep = false;
										}
									}
								}
							}
						}
					}
					//Wenn ein Mensch in den Park gehen möchte und schon im Park ist,
					//Ist die Zieellocation natürlich sein Anfangspunkt und damit die 0

					if (i == 2 && zielloc=='P' && mensch.getLocationId()=='P'){
						ziellocation="0";
					}
				}
		
		// Der Stack für die Bewegung wird mit den richtigen Werten gefüllt
			neuer_weg = fuelleStackWeg(zielloc, mensch, location_ids, counter, xPos_current, yPos_current);
		
		
		return neuer_weg;
	}
	

	/**
	 * Der gesamte Bewegungsstack wird gefüllt
	 * @param zielloc ziel '1-9', 'E' oder 'P'
	 * @param Mensch person oder Agent in Mensch gecasted
	 * @param locationIds Karte mit vorgefertigten Zahlen-Weg
	 * @param counter gibt die Anzahl der Schritte für den Menschen an 
	 * @param xPosCurrent gibt die aktuelle Position des Ziels an
	 * @param yPosCurrent gibt die aktuelle Position des Ziels an
	 * @return Bewegungsstack
	 * @author Martika
	 */
	private Stack<Character> fuelleStackWeg(Character zielLoc, Mensch mensch, ArrayList<ArrayList<String>> locationIds, int counter, int xPosCurrent, int yPosCurrent) {
		Stack<Character> neuerWeg = new Stack<Character>();
		
		//Falls das Ziel das eigene Haus ist, soll der Mensch auf die Homeposition laufen
		if (mensch.getHausId() == (int)(zielLoc-48-1)){
			if ((int)(zielLoc)-48 <=9 && (int)(zielLoc)-48 > 0){ //-48 für char umwandlung zu int
				neuerWeg = fuelleStackFuerHomeposition(mensch, xPosCurrent, yPosCurrent);
			}
		}
		
		//Hier wird vom Ziel rückwärtz runtergezählt bis zur Startposition
		//Wird bei jeder normalen Bewegung verwendet, oder mit einer 50% Chance beim Parkrundlauf
		if(mensch.getLocationId()!=zielLoc || (int)(Math.random()*2) == 1){ 
			for (int i = counter; i>=0; i--){
				if (mensch.getLocationId()==zielLoc && i == 0 && zielLoc=='P'){
					if (valuesInRange(xPosCurrent, yPosCurrent+1)){
						if (locationIds.get(yPosCurrent+1).get(xPosCurrent).equals(String.valueOf(counter+1))) {			//unten gehts weiter
							yPosCurrent++;
							neuerWeg.push('o');
						}
					}
					if (valuesInRange(xPosCurrent+1, yPosCurrent)){
						if (locationIds.get(yPosCurrent).get(xPosCurrent+1).equals(String.valueOf(counter+1))) {			//rechts gehts weiter
							xPosCurrent++;
							neuerWeg.push('l');
						}
					}
					if (valuesInRange(xPosCurrent, yPosCurrent-1)){
						if (locationIds.get(yPosCurrent-1).get(xPosCurrent).equals(String.valueOf(counter+1))) {			//oben gehts weiter
							yPosCurrent--;
							neuerWeg.push('u');
						}
					}
					if (valuesInRange(xPosCurrent-1, yPosCurrent)){
						if (locationIds.get(yPosCurrent).get(xPosCurrent-1).equals(String.valueOf(counter+1))) {			//links gehts weiter
							xPosCurrent--;
							neuerWeg.push('r');
						}
					}
				} else{
					if (valuesInRange(xPosCurrent, yPosCurrent+1)){
						if (locationIds.get(yPosCurrent+1).get(xPosCurrent).equals(String.valueOf(i))) {			//unten gehts weiter
						yPosCurrent++;
						neuerWeg.push('o');
						}
					}
					if (valuesInRange(xPosCurrent+1, yPosCurrent)){
						if (locationIds.get(yPosCurrent).get(xPosCurrent+1).equals(String.valueOf(i))) {			//rechts gehts weiter
						xPosCurrent++;
						neuerWeg.push('l');
						}
					}
					if (valuesInRange(xPosCurrent, yPosCurrent-1)){
						if (locationIds.get(yPosCurrent-1).get(xPosCurrent).equals(String.valueOf(i))) {			//oben gehts weiter
						yPosCurrent--;
						neuerWeg.push('u');
						}
					}
					if (valuesInRange(xPosCurrent-1, yPosCurrent)){
						if (locationIds.get(yPosCurrent).get(xPosCurrent-1).equals(String.valueOf(i))) {			//links gehts weiter
						xPosCurrent--;
						neuerWeg.push('r');
						}
					}
				}
			}
		} else {
			for (int i = 1; i<=counter+1; i++){
				if (zielLoc!='P'){
					i--;
				}
				if (valuesInRange(xPosCurrent, yPosCurrent+1)){
					if (locationIds.get(yPosCurrent+1).get(xPosCurrent).equals(String.valueOf(i))) {
						//unten gehts weiter
						yPosCurrent++;
						neuerWeg.push('o');
					}
				}
				if (valuesInRange(xPosCurrent+1, yPosCurrent)){
					if (locationIds.get(yPosCurrent).get(xPosCurrent+1).equals(String.valueOf(i))) {
						//rechts gehts weiter
						xPosCurrent++;
						neuerWeg.push('l');
					}
				}
				if (valuesInRange(xPosCurrent, yPosCurrent-1)){
					if (locationIds.get(yPosCurrent-1).get(xPosCurrent).equals(String.valueOf(i))) {
						//oben gehts weiter
						yPosCurrent--;
						neuerWeg.push('u');
					}
				}
				if (valuesInRange(xPosCurrent-1, yPosCurrent)){
					if (locationIds.get(yPosCurrent).get(xPosCurrent-1).equals(String.valueOf(i))) {
						//links gehts weiter
						xPosCurrent--;
						neuerWeg.push('r');
					}
				} 
				if (zielLoc!='P'){
					i++;
				}
			}
		}
		return neuerWeg;
	}


	/**
	 * Füllt den Stack mit der Bewegung vom Hauseingang zur Homeposition
	 * @param mensch Person oder Agent in Mensch gecasted
	 * @param xPosCurrent Hauseingang
	 * @param yPosCurrent Hauseingang
	 * @return Bewegungsstack
	 * @author Martika
	 */
	private Stack<Character>fuelleStackFuerHomeposition(Mensch mensch, int xPosCurrent, int yPosCurrent) {
		Stack<Character> neuerWeg = new Stack<Character>();

		if (((mensch.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+1 == xPosCurrent){
			neuerWeg.push('l');
		}
		if (((mensch.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)+2 == xPosCurrent){
			neuerWeg.push('l');
			neuerWeg.push('l');
		}
		if (((mensch.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-1 == xPosCurrent){
			neuerWeg.push('r');
		}
		if (((mensch.getHomePosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)-2 == xPosCurrent){
			neuerWeg.push('r');
			neuerWeg.push('r');
		}
		if (((mensch.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-1 == yPosCurrent){
			neuerWeg.push('u');
		}
		if (((mensch.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)-2 == yPosCurrent){
			neuerWeg.push('u');
			neuerWeg.push('u');
		}
		if (((mensch.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+1 == yPosCurrent){
			neuerWeg.push('o');
		}
		if (((mensch.getHomePosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT)+2 == yPosCurrent){
			neuerWeg.push('o');
			neuerWeg.push('o');
		}
				
		return neuerWeg;
	}

	
	/**
	 * Unnötige Flächen werden als nicht begehbar gekennzeichnet
	 * Das eigene Haus bekommt 'begehbar' zugewiesen
	 * @param locationIds Karte des jeweiligen Landes
	 * @param zielLocation Das Ziel 1-9, E oder P
	 * @param locationId aktuelle Position des Menschen
	 * @return initialisierte Karte
	 * @author Martika
	 */
	private ArrayList<ArrayList<String>> rasterkarteInitialisieren(ArrayList<ArrayList<String>> locationIds, String zielLocation, char locationId) {
		for (int i=0; i<locationIds.size(); i++){
			for (int j=0; j<locationIds.get(i).size(); j++){
				if (locationIds.get(i).get(j).charAt(0) != 'X' && locationIds.get(i).get(j).charAt(0) != zielLocation.charAt(0) && locationIds.get(i).get(j).charAt(0) != 'P' && locationIds.get(i).get(j).charAt(0) != locationId ){
					//nicht begehbar
					locationIds.get(i).set(j,"You shall not pass!") ;
				}
				if (!(zielLocation.equals("P") && locationId == 'P')){
					if (locationIds.get(i).get(j).charAt(0) == zielLocation.charAt(0)){
						//Hier ist das Ziel
						locationIds.get(i).set(j,"Z") ;
					}
					if (locationIds.get(i).get(j).charAt(0) == locationId ){
						//begehbar
						locationIds.get(i).set(j,"X") ;
					}
				} else{
					if (locationIds.get(i).get(j).charAt(0) == 'X'){
						//nicht begehbar, wenn man im Park im Kreisläuft
						locationIds.get(i).set(j,"You shall not pass!") ;
					}
				}
				if (locationIds.get(i).get(j).charAt(0) == 'P'){
					//begehbar
					locationIds.get(i).set(j,"X") ;
				}
				
			}
		}
		return locationIds;
	}
	
	
	/**
	 * @param doTimes die Anzahl, wieviele Runden der Agent drehen soll
	 * @author Martika
	 */
	private void fuelleStackRumwuseln(int doTimes) {
		Stack<Character> neuerWeg = new Stack<Character>();
		ArrayList<ArrayList<String>> locationIds;
		locationIds = Ressources.getLocation_ids();
		
		//Aktuelle Agentenposition wird auf 0 gesetzt
		locationIds.get((agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"0");
		
		//Karte wird initialisiert
		locationIds = rasterkarteInitialisieren(locationIds, String.valueOf(getAgent().getLocationId()), getAgent().getLocationId());
		
		locationIds.get((agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT).set((agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT,"Z");
		
		for (int i=0; i<15; i++){
			for (int j=0; j<24; j++){
				if ((locationIds.get(i).get(j)=="Z" || locationIds.get(i).get(j)=="0") && locationIds.get(i+1).get(j)=="Z" && locationIds.get(i-1).get(j)=="Z" && locationIds.get(i).get(j+1)=="Z" && locationIds.get(i).get(j-1)=="Z"){
					
					//Nun läuft der Agent lustig hin und her
					for (int k=0; k<doTimes; k++){
						neuerWeg.push('r');
						neuerWeg.push('o');
						neuerWeg.push('l');
						neuerWeg.push('u');
						neuerWeg.push('r');
						neuerWeg.push('u');
						neuerWeg.push('l');
						neuerWeg.push('o');
						neuerWeg.push('o');
						neuerWeg.push('l');
						neuerWeg.push('u');
						neuerWeg.push('u');
						neuerWeg.push('r');
						neuerWeg.push('o');
					}
					
					//Hat der agent vorher nicht gewuselt, soll er zur Hausmitte laufen
					if (this.agent.getPosX() == this.agent.getHomePosX() && this.agent.getPosY() ==this.agent.getHomePosY()){
						neuerWeg.push('u');
						neuerWeg.push('r');
					} else{
						if ((this.agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT==j && (this.agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT==i+1){
							neuerWeg.push('o');
						} else {
							if ((this.agent.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT!=j || (this.agent.getPosY()-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT!=i){
								//Der Agent wird zum Mittelpunkt des Hauses geleitet
								if (locationIds.get(i-2).get(j-1).equals("X") || locationIds.get(i-1).get(j-2).equals("X")){
									neuerWeg.push('u');
									neuerWeg.push('r');
								}
								if (locationIds.get(i-2).get(j).equals("X")){
									neuerWeg.push('u');
								}
								if (locationIds.get(i-2).get(j+1).equals("X") || locationIds.get(i-1).get(j+2).equals("X")){
									neuerWeg.push('u');
									neuerWeg.push('l');
								}
								if (locationIds.get(i).get(j+2).equals("X")){
									neuerWeg.push('l');
								}
								if (locationIds.get(i+1).get(j+2).equals("X") || locationIds.get(i+2).get(j+1).equals("X")){
									neuerWeg.push('o');
									neuerWeg.push('l');
								}
								if (locationIds.get(i+2).get(j).equals("X")){
									neuerWeg.push('o');
								}
								if (locationIds.get(i+2).get(j-1).equals("X") || locationIds.get(i+1).get(j-2).equals("X") ){
									neuerWeg.push('o');
									neuerWeg.push('r');
								}
								if (locationIds.get(i).get(j-2).equals("X")){
									neuerWeg.push('r');
								}
							} 
						}	
					}
				}
			}
		}	
		getAgent().setMoves(neuerWeg);
	}
	
	
	/**
	 * Hier wird das wuseln gestartet, die Spionagetools letztendlich nach dem Wuseln installiert
	 * entfernt ect. 
	 * @author Martika
	 */
	public void doSomethingAfterAgentAction(){
		int personId =0;
		
		//Das Atribut mussWuseln im agent beinhaltet z.B. "Wanze"
		//-> es muss eine Wanze installiert werden
		
		//Wenn beispielsweise "Wanze+" in mussWuseln steht
		//-> Agent hat schon gewuselt und nun kann das Spionagetool hinzugefügt werden
		
		//Wird ein Spionagetool entfernt, steht in mussWuseln "Wanzer" bzw "Wanzer+"
		//r für remove
		
		//Wenn man Kuchen vorbeibringen möchte, steht in mussWuseln zuerst die ID der Person,
		//an die der Kuchen vorbeigebracht werden soll z.B. "11Kuchen"
		
		if (!wieschteAktion && getAgent().getMussWuseln() != "Park" && getAgent().getMussWuseln() != "Park+"){ 
			//Es wird die PersonenID aus mussWuseln identifiziert
			if (getAgent().getMussWuseln().charAt(1) <='9' && getAgent().getMussWuseln().charAt(1)>='0'){
				personId = Integer.parseInt(getAgent().getMussWuseln().substring(0,2));
			} else{
				personId = Integer.parseInt(getAgent().getMussWuseln().substring(0,1));
			}
		}
		
		
		//Spionage
		if(getAgent().getMussWuseln().equals("Wanze+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().add("Wanze");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert((float)(Math.random()*17.5f+1)+22.5f,0);
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Wanze")){
			fuelleStackRumwuseln(2);
			getAgent().setMussWuseln("Wanze+");
		} 
		
		if(getAgent().getMussWuseln().equals("Kamera+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().add("Kamera");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert((float)(Math.random()*10+1)+35,1);
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Kamera")){
			fuelleStackRumwuseln(3);
			getAgent().setMussWuseln("Kamera+");
		}
		
		if(getAgent().getMussWuseln().equals("Hacken+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().add("Hacken");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert((float)(Math.random()*20+1)+15,2);
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Hacken")){
			fuelleStackRumwuseln(2);
			getAgent().setMussWuseln("Hacken+");
		}
		
		
		if(getAgent().getMussWuseln().length()==10 && getAgent().getMussWuseln().substring(1,10).equals("Fernglas+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getMussWuseln().charAt(0)-48-1)).getUeberwachungsmodule().add("Fernglas");
			getHouses().get((int)(getAgent().getMussWuseln().charAt(0)-48-1)).setUeberwachungsWert((float)(Math.random()*7+1)+8,3);
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().length()==9 && getAgent().getMussWuseln().substring(1,9).equals("Fernglas")){
			fuelleStackRumwuseln(1);
			getAgent().setMussWuseln(getAgent().getMussWuseln()+"+");
		}		
		
		
		//Spionage entfernen
		
		if(getAgent().getMussWuseln().equals("Wanzer+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().remove("Wanze");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert(0,0);			
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Wanzer")){
			fuelleStackRumwuseln(2);
			getAgent().setMussWuseln("Wanzer+");
		}
		
		if(getAgent().getMussWuseln().equals("Kamerar+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().remove("Kamera");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert(0,1);			
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Kamerar")){
			fuelleStackRumwuseln(3);
			getAgent().setMussWuseln("Kamerar+");
		}
		
		if(getAgent().getMussWuseln().equals("Hackenr+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getLocationId()-48-1)).getUeberwachungsmodule().remove("Hacken");
			getHouses().get((int)(getAgent().getLocationId()-48-1)).setUeberwachungsWert(0,2);			
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().equals("Hackenr")){
			fuelleStackRumwuseln(2);
			getAgent().setMussWuseln("Hackenr+");
		}
		
		if(getAgent().getMussWuseln().length()==11 && getAgent().getMussWuseln().substring(1,11).equals("Fernglasr+") && getAgent().getCurrentMove()=='n'){
			getHouses().get((int)(getAgent().getMussWuseln().charAt(0)-48)).getUeberwachungsmodule().remove("Fernglas");
			getHouses().get((int)(getAgent().getMussWuseln().charAt(0)-48)).setUeberwachungsWert(0,3);
			getAgent().setMussWuseln("");
		}
		if(getAgent().getMussWuseln().length()==10 && getAgent().getMussWuseln().substring(1,10).equals("Fernglasr")){
			fuelleStackRumwuseln(1);
			getAgent().setMussWuseln(getAgent().getMussWuseln()+"+");
		}
			
		
		
		
		//Soziales
		if(getAgent().getMussWuseln().length()>=8 && personId <=9 &&  getAgent().getMussWuseln().substring(1,8).equals("Kuchen+") ||
				getAgent().getMussWuseln().length()>=9 && personId >9 &&  getAgent().getMussWuseln().substring(2,9).equals("Kuchen+")){
			calcMisstrauenNachBeschwichtigen(0, getPeople().get(personId));
			getPeople().get(personId).erhoeheDurchgefuehrteBeschwichtigungen(0);
			getPeople().get(personId).setMoves(new Stack());
			getAgent().setMussWuseln("");
		}	
		if(getAgent().getMussWuseln().length()>=7 && personId <=9 && getAgent().getMussWuseln().substring(1,7).equals("Kuchen") ||
				getAgent().getMussWuseln().length()>=8 && personId >9 && getAgent().getMussWuseln().substring(2,8).equals("Kuchen") ){
			getAgent().setMussWuseln(personId+"Kuchen+");
		}	
		
		if(getAgent().getMussWuseln().length()>=13 && personId <=9 &&  getAgent().getMussWuseln().substring(1,13).equals("Unterhalten+") ||
				getAgent().getMussWuseln().length()>=14 && personId >9 &&  getAgent().getMussWuseln().substring(2,14).equals("Unterhalten+")){
			calcMisstrauenNachBeschwichtigen(1, getPeople().get(personId));
			getPeople().get(personId).erhoeheDurchgefuehrteBeschwichtigungen(1);
			getPeople().get(personId).setMoves(new Stack());
			getAgent().setMussWuseln("");
		}	
		if(getAgent().getMussWuseln().length()>=12 && personId <=9 && getAgent().getMussWuseln().substring(1,12).equals("Unterhalten") ||
				getAgent().getMussWuseln().length()>=13 && personId >9 && getAgent().getMussWuseln().substring(2,13).equals("Unterhalten") ){
			getAgent().setMussWuseln(personId+"Unterhalten+");
		}
		
		if(getAgent().getMussWuseln().length()>=9 && personId <=9 &&  getAgent().getMussWuseln().substring(1,9).equals("Flirten+") ||
				getAgent().getMussWuseln().length()>=10 && personId >9 &&  getAgent().getMussWuseln().substring(2,10).equals("Flirten+")){
			calcMisstrauenNachBeschwichtigen(2, getPeople().get(personId));
			getPeople().get(personId).erhoeheDurchgefuehrteBeschwichtigungen(2);
			getPeople().get(personId).setMoves(new Stack());
			getAgent().setMussWuseln("");
		}	
		if(getAgent().getMussWuseln().length()>=8 && personId <=9 && getAgent().getMussWuseln().substring(1,8).equals("Flirten") ||
				getAgent().getMussWuseln().length()>=9 && personId >9 && getAgent().getMussWuseln().substring(2,9).equals("Flirten") ){
			getAgent().setMussWuseln(personId+"Flirten+");
		}
		
		if(getAgent().getMussWuseln().length()>=6 && personId <=9 &&  getAgent().getMussWuseln().substring(1,6).equals("Hand+") ||
				getAgent().getMussWuseln().length()>=7 && personId >9 &&  getAgent().getMussWuseln().substring(2,7).equals("Hand+")){
			calcMisstrauenNachBeschwichtigen(3, getPeople().get(personId));
			getPeople().get(personId).erhoeheDurchgefuehrteBeschwichtigungen(3);
			getPeople().get(personId).setMoves(new Stack());
			getAgent().setMussWuseln("");
		}	
		if(getAgent().getMussWuseln().length()>=5 && personId <=9 && getAgent().getMussWuseln().substring(1,5).equals("Hand") ||
				getAgent().getMussWuseln().length()>=6 && personId >9 && getAgent().getMussWuseln().substring(2,6).equals("Hand") ){
			getAgent().setMussWuseln(personId+"Hand+");
		}
		
		if(getAgent().getMussWuseln().equals("Park")){
			calcWeg(agent, 'P');
			calcMisstrauenNachBeschwichtigenInPark();
		} 	
				
	}
	
	
	/**
	 * Es wird überprüft, ob die Bewohner des Hauses farbig angezeigt werden sollen
	 * Sind die Menschen einmal farbig, werden sie nicht wieder grau
	 * @param hausid das zu betrachtende Haus
	 * @author Martika
	 */
	private void calcColouredPeople (int hausid){
		for (int i = 0; i<getPeople().size(); i++){
			if (getPeople().get(i).getHausId() == hausid && !getPeople().get(i).getIstFarbig()){
				getPeople().get(i).setIstFarbig(true);
				getPeople().get(i).farbeZeigen();
			}
		}
	}
	
	
	/**
	 * Es wird überprüft ob der X und Y Wert sich im Wertebereich der Karte befindet
	 * @param x zu untersuchender X Wert
	 * @param y zu untersuchender Y Wert
	 * @return true, wenn die Werte im Raster sind - false, wenn die Werte aus dem Raster gehen
	 * @author Martika
	 */
	private boolean valuesInRange(int x, int y){
		if (x>=0 && x<Ressources.MAPWIDTH/Ressources.RASTERHEIGHT && 
				y>=0 && y<Ressources.MAPHEIGHT/Ressources.RASTERHEIGHT){
			return true;
		} else{
			return false;
		}
	}
	
	

	/**
	 * analysiert ob das Spiel verloren ist
	 * @return true, wenn das Spiel Gameover ist - false, wenn noch nicht gameover ist
	 * @author Martika
	 */
	public boolean calcGameOver(){
		//Wenn das Misstrauen der Straße größer 90 ist oder
		//Der Agent ein Misstrauen von größer 85
		
		if (calcMisstrauenInStreet()>=90.0){
			return true;
		}
		for (int i = 0; i < this.people.size(); i++){
			if (this.people.get(i).getBorder()!= null && this.people.get(i).getCurrentMove()=='n' && 
					this.people.get(i).getLocationId()=='E'){
				return true;
			}
			if (this.people.get(i) instanceof Terrorist && this.people.get(i).getMisstrauen() >= 85.00){
				//Der Terrorist muss auffällig die Karte verlassen, danach hat man verloren
				if (this.people.get(i).getCurrentMove()=='n'){
					calcWeg(this.people.get(i), 'E');
					this.people.get(i).setBorder(BorderFactory.createLineBorder(Color.red));
					return false;
				}
			} 
			
		}	
		return false;
	}
	
	
	
	public ArrayList<Person> getPeople(){
		return people;
	}
	
	public void setPeople(Person p){
		people.add(p);
	}
	
	
	public Agent getAgent(){
		return agent;
	}
	
	public void setAgent(Agent agent){
		this.agent = agent;
	}


	public int getSpielTag() {
		return spielTag;
	} 


	public int getSpielStunde() {
		return spielStunde;
	}


	public int getSpielMinute() {
		return spielMinute;
	}

	
	/**
	 * @return Zeit im format: ss:mm
	 * @author Martika
	 */
	public String getSpielzeitAsString() {
		String zeit="";
		zeit = String.valueOf(this.spielStunde);
		if (this.spielStunde <=9){
			zeit = "0" + zeit;
		}
		zeit = zeit + ":";
		if (this.spielMinute <=9){
			zeit = zeit + "0";
		}
		zeit = zeit + String.valueOf(this.spielMinute);
		
		return zeit;
	}
	
	public ArrayList<Haus> getHouses(){
		return this.houses;
	}
	
	public void setHouses(Haus haus){
		this.houses.add(haus);
	}

	public void setWieschteAktion(boolean wieeeeschteAktion) { 
		this.wieschteAktion = wieeeeschteAktion;
	}

	public boolean isWieschteAktion(){
		return this.wieschteAktion;
	}
	
	public void calcMisstrauenMax(){
		double misstrauen = this.calcMisstrauenInStreet();
		if(this.misstrauenMax < misstrauen){
			this.misstrauenMax = misstrauen;
		}
	}
	
	public double getMisstrauenMax(){
		return this.misstrauenMax;
	}
}
