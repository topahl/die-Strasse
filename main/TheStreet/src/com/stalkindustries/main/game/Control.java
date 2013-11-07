package com.stalkindustries.main.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.IControl;

//Kreiert vom unglaublichen Stephan
//auf Basis von Tobias unglaublicher Arbeit
/**
 * 
 * @author Tobias, Stephan, Tiki
 *
 */
public class Control implements IControl {
	
	private String lastFunktioncode = "";
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu können
	private GUILayer guilayer;
	private Quiz quiz;
//	private int house_id = -1;

	
	
	/**
	 * Konstruktor lädt Ingamefenster-Objekt
	 * @param guilayer Objekt des Ingamefensters
	 */
	public Control(GUILayer guilayer, Quiz quiz) {
		this.guilayer = guilayer;
		this.quiz = quiz;
	}

	
	
	/**
	 * Call wird von Button bei Klick aufgerufen
	 * Anhand des "Namens" entsprechende Funktion aufrufen
	 * TODO Funktionalität komplett testen
	 */
	public void call(String funktion) {
		System.out.println("You pressed:"+funktion);
	
		//GUI Ingame Buttons
		if(funktion.equals("pause"))
			clickPause();
		if(funktion.equals("close"))
			clickExit();
		if(funktion.equals("dialogAccept"))
			clickDialogAccept();
		if(funktion.equals("dialogDecline"))
			clickDialogDecline();
		
		//Buttons Menüleiste
		if(funktion.equals("spionage"))
			clickAktionenSpionage();
		if(funktion.equals("beschwichtigen"))
			clickAktionenBeschwichtigen();
		if(funktion.equals("nachHause"))
			clickNachhause();
		if(funktion.equals("aktionrazzia"))
			clickRazzia();
		
		//Buttons Aktionsfenster Spionage
		if(funktion.equals("closeSpionage"))
			closeWindow("spionage");
		if(funktion.equals("aktionKuchen"))
			clickKuchen();
		if(funktion.equals("aktionUnterhalten"))
			clickUnterhalten();
		if(funktion.equals("aktionFlirten"))
			clickFlirten();
		if(funktion.equals("aktionHand"))
			clickHand();
		if(funktion.equals("parkBeschwichtigen"))
			clickParkBeschwichtigen();
		
		//Buttons Aktionsfenster Beschwichtigen 
		if(funktion.equals("closeBeschwichtigen"))
			closeWindow("beschwichtigen");
		if(funktion.equals("aktionWanze"))
			clickWanzen();
		if(funktion.equals("aktionKamera"))
			clickKamera();
		if(funktion.equals("aktionHacken"))
			clickHacken();
		if(funktion.equals("aktionFernglas"))
			clickFernglas();
		
		//Buttons small WerkzeugeSpionage im Houselayer
		if(funktion.equals("werkzeugWanze"))
			clickRemoveWanze();
		if(funktion.equals("werkzeugKamera"))
			clickRemoveKamera();
		if(funktion.equals("werkzeugHacken"))
			clickRemoveHacken();
		if(funktion.equals("werkzeugFernglas"))
			clickRemoveFernglas();
		
		
		if(funktion.equals("closeHaus"))
			closeWindow("fensterhaus");
		
		if(funktion.startsWith("Quiz"))
			quizAntwort(funktion.substring(4));
		
		//Buttons Häuser (invisible)
		if(funktion.substring(0,4).equals("Haus"))
			//Die Methode clickHaus wird mit der Nummer des Hauses das gedrückt wurde aufgerufen 
			clickHaus((int)((funktion.substring(4,5)).charAt(0)-48));		

		//Mousefollower abschalten bei bestimmten Buttons
		if(funktion.equals("pause") || funktion.equals("close")
				|| funktion.equals("beschwichtigen") || funktion.equals("spionage") ||
				funktion.equals("nachHause")) {
			guilayer.getMousefollower().setVisible(false);
		}
		
		lastFunktioncode = funktion;
	}

	
	private void clickRemoveFernglas() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getSimulation().getHouses().get(currentHouse).getUeberwachungsmodule().remove("Fernglas");
//		guilayer.getSimulation().get_agent().setMussWuseln("Fernglas");
//		guilayer.getSimulation().berechne_weg(null, guilayer.getSimulation().get_agent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().getHouses().get(currentHouse).setUeberwachungsWert(0,3);
		guilayer.getSimulation().setWieeeeschteAktion(true);
		
	}



	private void clickRemoveHacken() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().get_agent().setMussWuseln("Hackenr");
		guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieeeeschteAktion(true);
	}



	private void clickRemoveKamera() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().get_agent().setMussWuseln("Kamerar");
		guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieeeeschteAktion(true);
	}



	private void clickRemoveWanze() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().get_agent().setMussWuseln("Wanzer");
		guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieeeeschteAktion(true);
	}





	private void quizAntwort(String antwort){ //antwort=A || B || C
		guilayer.getButtonsMap().get("pause").setEnabled(true);
		this.quiz.analyzeAntwort(antwort);
		this.quiz.calcMisstrauenAfterQuiz();
		closeWindow("quizfenster");
	}
	
	/**
	 * Klicks auf Häuser abfangen
	 */

	private void clickHaus(int hausid) {
		Stack<Character> stehenBleiben = new Stack<Character>();
		boolean istVorhanden = false;
		boolean soAtHome = false;
		int fernglasCounter=0;
		
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		
		// hausid von 1-9, get_haus_id 0-8 => Deswegen plus 1
		if (hausid != guilayer.getSimulation().get_agent().get_haus_id()+1){
		
		if(lastFunktioncode.equals("aktionKuchen")){
			for (int i=0; i<guilayer.getSimulation().get_people().size(); i++){
				if (guilayer.getSimulation().get_people().get(i).get_location_id()== (char)(hausid+48) && 
						guilayer.getSimulation().get_people().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						guilayer.getSimulation().get_people().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().get_agent().setMussWuseln(i+"Kuchen");
						if (guilayer.getSimulation().get_agent().get_location_id() != (char)(hausid+48)){
							guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
						}
						//guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieeeeschteAktion(false);
					}	
				}
			}
		}
			
		if(lastFunktioncode.equals("aktionUnterhalten")){
			for (int i=0; i<guilayer.getSimulation().get_people().size(); i++){
				if (guilayer.getSimulation().get_people().get(i).get_location_id()== (char)(hausid+48) &&
						guilayer.getSimulation().get_people().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						guilayer.getSimulation().get_people().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().get_agent().setMussWuseln(i+"Unterhalten");
						if (guilayer.getSimulation().get_agent().get_location_id() != (char)(hausid+48)){
							guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieeeeschteAktion(false);
					}	
				}
			}
		}
			
		if(lastFunktioncode.equals("aktionFlirten")){
			for (int i=0; i<guilayer.getSimulation().get_people().size(); i++){
				if (guilayer.getSimulation().get_people().get(i).get_location_id()== (char)(hausid+48) &&
						guilayer.getSimulation().get_people().get(i).getCurrentMove()=='n' &&
						guilayer.getSimulation().get_people().get(i) instanceof Erwachsene){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						guilayer.getSimulation().get_people().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().get_agent().setMussWuseln(i+"Flirten");
						if (guilayer.getSimulation().get_agent().get_location_id() != (char)(hausid+48)){
							guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieeeeschteAktion(false);
					}	
				}
			}
		}
			
		if(lastFunktioncode.equals("aktionHand")){
			for (int i=0; i<guilayer.getSimulation().get_people().size(); i++){
				if (guilayer.getSimulation().get_people().get(i).get_location_id()== (char)(hausid+48) &&
						guilayer.getSimulation().get_people().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						guilayer.getSimulation().get_people().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().get_agent().setMussWuseln(i+"Hand");
						if (guilayer.getSimulation().get_agent().get_location_id() != (char)(hausid+48)){
							guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieeeeschteAktion(false);
					}	
				}
			}
		}
//			
//		if(lastFunktioncode.equals("aktionParkBeschwichtigen"))

		if(lastFunktioncode.equals("aktionWanze")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Wanze")){
					istVorhanden=true;
					break;
				}	
			}
			if (!istVorhanden){
				guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
				guilayer.getSimulation().get_agent().setMussWuseln("Wanze");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieeeeschteAktion(true);
			}
			istVorhanden = false;
		}
			
		
		if(lastFunktioncode.equals("aktionKamera")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Kamera")){
					istVorhanden=true;
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
				guilayer.getSimulation().get_agent().setMussWuseln("Kamera");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieeeeschteAktion(true);
			}
			istVorhanden = false;
		}

			
		if(lastFunktioncode.equals("aktionHacken")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Hacken")){
					istVorhanden=true;
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(hausid+48));
				guilayer.getSimulation().get_agent().setMussWuseln("Hacken");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieeeeschteAktion(true);
			}
			istVorhanden = false;
		}
			
		if(lastFunktioncode.equals("aktionFernglas")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Fernglas")){
					istVorhanden=true;
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().add("Fernglas");
				guilayer.getSimulation().setWieeeeschteAktion(true);
				guilayer.getSimulation().getHouses().get(hausid-1).setUeberwachungsWert((float)(Math.random()*7+1)+8,3);
				
				
			}
			istVorhanden = false;
		}
		
		
		//Beschwerden Miri
		if(lastFunktioncode.equals("aktionrazzia")){
			boolean festgenommen = false;
			if (this.guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsstatus() > 80){
				for(int i=0;i<this.guilayer.getSimulation().get_people().size();i++){
					//checken, wer in dem ausgewählten Haus wohnt
					if(this.guilayer.getSimulation().get_people().get(i).get_haus_id() == hausid-1){
						//checken, ob der Terrorist in dem ausgewählten Haus wohnt
						if(this.guilayer.getSimulation().get_people().get(i) instanceof Terrorist){
							festgenommen = true;
						}
					}
				}
				this.guilayer.stopGame();
				if(festgenommen){
					this.guilayer.getHighscore().setFestgenommen(true);
					this.guilayer.showDialogMessage("Gewonnen", "Herzlichen Glückwunsch, Sie haben den Terroristen festgenommen!", false, false);
				}
				else{
					this.guilayer.showDialogMessage("Verloren", "Schade! Das nächste Mal sollten Sie mehr Beweise sammeln.", false, false);
				}
				
			}
			else{
				this.guilayer.showDialogMessage("Abgelehnt", "Das Hauptquartier fordert mehr Indizien.", false, true);
			}
		}
//			
//		if(lastFunktioncode.equals("parkSpionage"))
			
		if(!lastFunktioncode.startsWith("aktion")){
			showHouseInfo(hausid);			
		}
		
		}
		guilayer.getMousefollower().setVisible(false);
	}

	
	
	/**
	 * Mousefollower updaten
	 * @author Tiki
	 */
	@Override
	public void mousePresent(String funktion, boolean isPresent) {
		
		//Aktionen Spionage
		if(funktion.equals("aktionWanze"))
			spionagelabelBeschr(isPresent, "Wanzen anbringen");
		if(funktion.equals("aktionKamera"))
			spionagelabelBeschr(isPresent, "Kameras anbringen");
		if(funktion.equals("aktionHacken"))
			spionagelabelBeschr(isPresent, "Trojaner installieren");
		if(funktion.equals("aktionFernglas"))
			spionagelabelBeschr(isPresent, "Haus beobachten");

		
		//Aktionen Beschwichtigen
		if(funktion.equals("aktionKuchen"))
			beschwichtigenlabelBeschr(isPresent, "Kuchen vorbeibringen");
		if(funktion.equals("aktionUnterhalten"))
			beschwichtigenlabelBeschr(isPresent, "Sich unterhalten");
		if(funktion.equals("aktionFlirten"))
			beschwichtigenlabelBeschr(isPresent, "Flirten");
		if(funktion.equals("aktionHand"))
			beschwichtigenlabelBeschr(isPresent, "Helfen");
		if(funktion.equals("parkBeschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "Im Park unterhalten");
	}
	
	
	
	/**
	 * Spiel pausieren
	 */
	public void clickPause() {
		//Pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
		
		if (guilayer.getButtonsMap().get("beschwichtigen").isEnabled()){
			guilayer.getButtonsMap().get("beschwichtigen").setEnabled(false);
		} else {
			if (guilayer.getSimulation().getSpiel_stunde()<2 || guilayer.getSimulation().getSpiel_stunde()>6){
				guilayer.getButtonsMap().get("beschwichtigen").setEnabled(true);
			}
		}
		if (guilayer.getButtonsMap().get("spionage").isEnabled()){
			guilayer.getButtonsMap().get("spionage").setEnabled(false);
			guilayer.showDialogMessage("Pause", "Das Spiel wurde pausiert.", false,true);
		} else {
			guilayer.getButtonsMap().get("spionage").setEnabled(true);
			closeWindow("dialog");
		}
		if (guilayer.getButtonsMap().get("nachHause").isEnabled()){
			guilayer.getButtonsMap().get("nachHause").setEnabled(false);
		} else {
			if ((char)(guilayer.getSimulation().get_agent().get_haus_id()+1+48) != guilayer.getSimulation().get_agent().get_location_id()){
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
			}
		}
		
		if (guilayer.getButtonsMap().get("aktionrazzia").isEnabled()){
			guilayer.getButtonsMap().get("aktionrazzia").setEnabled(false);
		} else {
			guilayer.getButtonsMap().get("aktionrazzia").setEnabled(true);
		}
		
		//Pause-Button oben rechts ausblenden, wenn pausiert
//		if (guilayer.getButtonsMap().get("pause").isEnabled()) {
//			guilayer.getButtonsMap().get("pause").setEnabled(false);
//		} else {
//			guilayer.getButtonsMap().get("pause").setEnabled(true);
//		}
		
//		guilayer.showDialogMessage("Pause", "Das Spiel wurde pausiert.", false,true);
//		
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		
	}

	
	
	/**
	 * Ingame-Fenster schließen
	 * @param fensterName Bezeichnung des Fensters
	 */
	public void closeWindow(String fensterName) {
		JLayeredPane frame = guilayer.getWindow(fensterName);
		frame.setVisible(false);
		frame.setEnabled(false);
	}
	
	
	
	/**
	 * Bestimmte Fenster öffnen
	 */
	private void openWindow(String fensterName) {
		JLayeredPane frame = guilayer.getWindow(fensterName);
		frame.setVisible(true);
		frame.setEnabled(true);
	}
	
	
	
	private void clickDialogAccept() {
		if(guilayer.getDialogUeberschrift().equals("Pause"))
			clickPause();
		guilayer.getWindow("dialog").setEnabled(false);
		guilayer.getWindow("dialog").setVisible(false);
	}
	private void clickDialogDecline() {
		guilayer.getWindow("dialog").setEnabled(false);
		guilayer.getWindow("dialog").setVisible(false);
	}
	
	
	/**
	 * Spiel beenden
	 */
	private void clickExit() {
		this.guilayer.callHighscore();
		guilayer.endGame();
	}
	
	
	/**
	 * Klicks auf Buttons in Menüleiste
	 */
	private void clickAktionenSpionage() {
		closeWindow("beschwichtigen");
		closeWindow("fensterhaus"); 
		JLayeredPane frame = guilayer.getWindow("spionage");
		if(frame.isVisible())
			closeWindow("spionage");
		else{
			frame.setEnabled(true);
			frame.setVisible(true);
		}
	}
	
	private void clickAktionenBeschwichtigen() {
		closeWindow("spionage");
		closeWindow("fensterhaus"); 
		JLayeredPane frame = guilayer.getWindow("beschwichtigen");
		if(frame.isVisible())
			closeWindow("beschwichtigen");
		else{
			frame.setEnabled(true);
			frame.setVisible(true);
		}
	}

	private void clickNachhause() {
		closeWindow("fensterhaus"); 
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		guilayer.getSimulation().get_agent().setMussWuseln("");
		guilayer.getButtonsMap().get("nachHause").setEnabled(false);
		guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), (char)(guilayer.getSimulation().get_agent().get_haus_id()+1+48));
	}
	
	private void clickRazzia() {
		closeWindow("fensterhaus"); 
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*13, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}

	
	
	/**
	 * Klicks auf Aktionen
	 */
	private void clickWanzen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	private void clickKamera() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 39, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	private void clickHacken() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*2, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	private void clickFernglas() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*3, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	
	private void clickKuchen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*6, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieeeeschteAktion(false);
	}
	
	private void clickUnterhalten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*7, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieeeeschteAktion(false);
	}
	
	private void clickFlirten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*8, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieeeeschteAktion(false);
	}

	private void clickHand() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*9, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieeeeschteAktion(false);
	}

	private void clickParkBeschwichtigen() {
		//kein Icon, da Einsatzort (Park) vorgegeben
		closeWindow("beschwichtigen");
		guilayer.getSimulation().berechne_weg(guilayer.getSimulation().get_agent(), 'P');
		guilayer.getSimulation().get_agent().setMussWuseln("Park");
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().setWieeeeschteAktion(false);
	}
	

	
	/**
	 * Beschreibungstext im Aktionenfenster setzen
	 * @param isPresent
	 * @param text
	 */
	private void spionagelabelBeschr(boolean isPresent, String text){
		if(isPresent)
			guilayer.getBeschreibung("spionage").setText(text);
		else
			guilayer.getBeschreibung("spionage").setText("");
	}
	private void beschwichtigenlabelBeschr(boolean isPresent, String text){
		if(isPresent)
			guilayer.getBeschreibung("beschwichtigen").setText(text);
		else
			guilayer.getBeschreibung("beschwichtigen").setText("");
	}
	
	/**
	 * Zeigt die Hausinformation für ein Haus an
	 * @param hausnr Hausnummer des Hases welches angezeigt werden soll
	 */
	private void showHouseInfo(int hausnr){
		JLayeredPane hausinfo = guilayer.getWindow("fensterhaus");
		JLabel[] informationen = guilayer.getHausinfoLabels();
		informationen[0].setText("Haus "+hausnr);
		hausinfo.setVisible(true);
		hausinfo.setEnabled(true);
		hausnr--;
		ArrayList<Mensch> personen = guilayer.getHumans();
		HashMap<String,Button> buttons = guilayer.getButtonsMap();
		buttons.get("werkzeugWanze").setEnabled(false);
		buttons.get("werkzeugKamera").setEnabled(false);
		buttons.get("werkzeugHacken").setEnabled(false);
		buttons.get("werkzeugFernglas").setEnabled(false);
		int perscnt = 1;
		for(int i=0;i<4;i++){
				informationen[i+1].setVisible(false);
				informationen[i+5].setVisible(false);
		}
		for(Mensch person:personen){
			if(person.get_haus_id()==hausnr && ((Person)person).getIstFarbig()==true){
				informationen[perscnt].setIcon(new ImageIcon(person.getSprite().getSubimage(0, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT)));
				informationen[perscnt+4].setText(person.getName());
				informationen[perscnt].setVisible(true);
				informationen[perscnt+4].setVisible(true);
				perscnt++;
			}
		}
		for(String modul:guilayer.getSimulation().getHouses().get(hausnr).getUeberwachungsmodule()){
			buttons.get("werkzeug"+modul).setEnabled(true);
		}
		informationen[9].setSize((int)(guilayer.getSimulation().getHouses().get(hausnr).getUeberwachungsstatus()*1.66), 19);
	}

	
//	public int getHouseId(){
//		return this.house_id;
//	}
//	
//	public void setHouseId(int hous_id){
//		this.house_id = hous_id;
//	}
	
	public String getLastFunktioncode(){
		return this.lastFunktioncode;
	}
	
	public void setLastFunktioncode(String lastFunktioncode){
		this.lastFunktioncode = lastFunktioncode;
	}
}
