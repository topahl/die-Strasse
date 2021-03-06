package com.stalkindustries.main.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.IControl;

/**
 * 
 * @author Tobias, Stephan, Martika
 *
 */
public class Control implements IControl {
	
	private String lastFunktioncode = "";
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu k�nnen
	private GUILayer guilayer;
	private Quiz quiz;
//	private int house_id = -1;

	
	
	/**
	 * Konstruktor l�dt Ingamefenster-Objekt
	 * @param guilayer Objekt des Ingamefensters
	 */
	public Control(GUILayer guilayer, Quiz quiz) {
		this.guilayer = guilayer;
		this.quiz = quiz;
	}

	
	
	/**
	 * Call wird von Button bei Klick aufgerufen
	 * Anhand des "Namens" entsprechende Funktion aufrufen
	 * @author Tobias & Martika
	 */
	public void call(String funktion) {
	
		//GUI Ingame Buttons
		if(funktion.equals("pause"))
			clickPause();
		if(funktion.equals("close"))
			clickExit();
		if(funktion.equals("dialogAccept"))
			clickDialogAccept();
		if(funktion.equals("dialogDecline"))
			clickDialogDecline();
		
		//Buttons Men�leiste
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
		
		//Buttons H�user (invisible)
		if(funktion.substring(0,4).equals("Haus"))
			//Die Methode clickHaus wird mit der Nummer des Hauses das gedr�ckt wurde aufgerufen 
			clickHaus((int)((funktion.substring(4,5)).charAt(0)-48));		

		//Mousefollower abschalten bei bestimmten Buttons
		if(funktion.equals("pause") || funktion.equals("close")
				|| funktion.equals("beschwichtigen") || funktion.equals("spionage") ||
				funktion.equals("nachHause")) {
			guilayer.getMousefollower().setVisible(false);
		}
		
		lastFunktioncode = funktion;
	}

	/**
	 * @author Martika
	 */
	private void clickRemoveFernglas() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(guilayer.getSimulation().getAgent().getHausId()+48+1));
		guilayer.getSimulation().setWieschteAktion(true);
		guilayer.getSimulation().getAgent().setMussWuseln(currentHouse+"Fernglasr");
	}


	/**
	 * @author Martika
	 */
	private void clickRemoveHacken() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().getAgent().setMussWuseln("Hackenr");
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieschteAktion(true);
	}


	/**
	 * @author Martika
	 */
	private void clickRemoveKamera() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().getAgent().setMussWuseln("Kamerar");
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieschteAktion(true);
	}


	/**
	 * @author Martika
	 */
	private void clickRemoveWanze() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().getAgent().setMussWuseln("Wanzer");
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(currentHouse+1+48));
		guilayer.getSimulation().setWieschteAktion(true);
	}



	private void quizAntwort(String antwort){ //antwort=A || B || C
		this.quiz.analyzeAntwort(antwort);
		this.quiz.calcMisstrauenAfterQuiz();
		closeWindow("quizfenster");
	}
	
	/**
	 * Klicks auf H�user abfangen
	 * @author Martika & Miriam(Razzia)
	 */
	private void clickHaus(int hausid) {
		Stack<Character> stehenBleiben = new Stack<Character>();
		boolean istVorhanden = false;
		boolean soAtHome = false;
		
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		
		// hausid von 1-9, get_haus_id 0-8 => Deswegen plus 1
		if (hausid != guilayer.getSimulation().getAgent().getHausId()+1){
		
		if(lastFunktioncode.equals("aktionKuchen")){
			for (int i=0; i<guilayer.getSimulation().getPeople().size(); i++){
				if (guilayer.getSimulation().getPeople().get(i).getLocationId()== (char)(hausid+48) && 
						guilayer.getSimulation().getPeople().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						//ausgew�hlte Person bekommt einen Stack zugewiesen, sodass er sich nicht bewegen darf
						guilayer.getSimulation().getPeople().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().getAgent().setMussWuseln(i+"Kuchen");
						if (guilayer.getSimulation().getAgent().getLocationId() != (char)(hausid+48)){
							guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieschteAktion(false);
					}	
				}
			}
			if (!soAtHome){
				guilayer.showDialogMessage("Das klappt nicht!", "In Haus "+ hausid + " ist keiner zu Hause", false, true);
			}
		}
			
		if(lastFunktioncode.equals("aktionUnterhalten")){
			for (int i=0; i<guilayer.getSimulation().getPeople().size(); i++){
				if (guilayer.getSimulation().getPeople().get(i).getLocationId()== (char)(hausid+48) &&
						guilayer.getSimulation().getPeople().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						//ausgew�hlte Person bekommt einen Stack zugewiesen, sodass er sich nicht bewegen darf
						guilayer.getSimulation().getPeople().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().getAgent().setMussWuseln(i+"Unterhalten");
						if (guilayer.getSimulation().getAgent().getLocationId() != (char)(hausid+48)){
							guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieschteAktion(false);
					}	
				}
			}
			if (!soAtHome){
				guilayer.showDialogMessage("Das klappt nicht!", "In Haus "+ hausid + " ist keiner zu Hause", false, true);
			}
		}
			
		if(lastFunktioncode.equals("aktionFlirten")){
			for (int i=0; i<guilayer.getSimulation().getPeople().size(); i++){
				if (guilayer.getSimulation().getPeople().get(i).getLocationId()== (char)(hausid+48) &&
						guilayer.getSimulation().getPeople().get(i).getCurrentMove()=='n' &&
						guilayer.getSimulation().getPeople().get(i) instanceof Erwachsene){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						//ausgew�hlte Person bekommt einen Stack zugewiesen, sodass er sich nicht bewegen darf
						guilayer.getSimulation().getPeople().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().getAgent().setMussWuseln(i+"Flirten");
						if (guilayer.getSimulation().getAgent().getLocationId() != (char)(hausid+48)){
							guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieschteAktion(false);
					}	
				}
			}
			if (!soAtHome){
				guilayer.showDialogMessage("Das klappt nicht!", "In Haus "+ hausid + " ist kein Erwachsener zu Hause", false, true);
			}
		}
			
		if(lastFunktioncode.equals("aktionHand")){
			for (int i=0; i<guilayer.getSimulation().getPeople().size(); i++){
				if (guilayer.getSimulation().getPeople().get(i).getLocationId()== (char)(hausid+48) &&
						guilayer.getSimulation().getPeople().get(i).getCurrentMove()=='n'){
					if (!soAtHome){
						soAtHome = true;
						stehenBleiben.add('s');
						//ausgew�hlte Person bekommt einen Stack zugewiesen, sodass er sich nicht bewegen darf
						guilayer.getSimulation().getPeople().get(i).setMoves(stehenBleiben);
						guilayer.getSimulation().getAgent().setMussWuseln(i+"Hand");
						if (guilayer.getSimulation().getAgent().getLocationId() != (char)(hausid+48)){
							guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
						}
						guilayer.getButtonsMap().get("nachHause").setEnabled(true);
						guilayer.getSimulation().setWieschteAktion(false);
					}	
				}
			}
			if (!soAtHome){
				guilayer.showDialogMessage("Das klappt nicht!", "In Haus "+ hausid + " ist keiner zu Hause", false, true);
			}
		}

		if(lastFunktioncode.equals("aktionWanze")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Wanze")){
					istVorhanden=true;
					guilayer.showDialogMessage("Das klappt nicht!", "Dieses Spionagetool wurde in Haus "+ hausid + " schon installiert", false, true);
					break;
				}	
			}
			if (!istVorhanden){
				guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
				guilayer.getSimulation().getAgent().setMussWuseln("Wanze");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieschteAktion(true);
			}
			istVorhanden = false;
		}
			
		
		if(lastFunktioncode.equals("aktionKamera")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Kamera")){
					istVorhanden=true;
					guilayer.showDialogMessage("Das klappt nicht!", "Dieses Spionagetool wurde in Haus "+ hausid + " schon installiert", false, true);
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
				guilayer.getSimulation().getAgent().setMussWuseln("Kamera");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieschteAktion(true);
			}
			istVorhanden = false;
		}

			
		if(lastFunktioncode.equals("aktionHacken")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Hacken")){
					istVorhanden=true;
					guilayer.showDialogMessage("Das klappt nicht!", "Dieses Spionagetool wurde in Haus "+ hausid + " schon installiert", false, true);
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(hausid+48));
				guilayer.getSimulation().getAgent().setMussWuseln("Hacken");
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
				guilayer.getSimulation().setWieschteAktion(true);
			}
			istVorhanden = false;
		}
			
		if(lastFunktioncode.equals("aktionFernglas")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Fernglas")){
					istVorhanden=true;
					guilayer.showDialogMessage("Das klappt nicht!", "Dieses Spionagetool wurde in Haus "+ hausid + " schon installiert", false, true);
					break;
				}
			}
			if (!istVorhanden){
				guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(guilayer.getSimulation().getAgent().getHausId()+48+1));
				guilayer.getSimulation().setWieschteAktion(true);
				guilayer.getSimulation().getAgent().setMussWuseln(hausid+"Fernglas");
			}
			istVorhanden = false;
		}
		
		
		if(lastFunktioncode.equals("aktionrazzia")){
			boolean festgenommen = false;
			if (this.guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsstatus() > 80){
				for(int i=0;i<this.guilayer.getSimulation().getPeople().size();i++){
					//checken, wer in dem ausgew�hlten Haus wohnt
					if(this.guilayer.getSimulation().getPeople().get(i).getHausId() == hausid-1){
						//checken, ob der Terrorist in dem ausgew�hlten Haus wohnt
						if(this.guilayer.getSimulation().getPeople().get(i) instanceof Terrorist){
							festgenommen = true;
						}
					}
				}
				this.guilayer.stopGame();
				if(festgenommen){
					this.guilayer.getHighscore().setFestgenommen(true);
					this.guilayer.showDialogMessage("Gewonnen", "Gl�ckwunsch, Sie haben den Schwerverbrecher festgenommen!", false, false);
				}
				else{
					this.guilayer.showDialogMessage("Verloren", "Schade! Das n�chste Mal sollten Sie mehr Beweise sammeln.", false, false);
				}
				
			}
			else{
				this.guilayer.showDialogMessage("Abgelehnt", "Das Hauptquartier fordert mehr Indizien.", false, true);
			}
		}
	
		if(!lastFunktioncode.startsWith("aktion")){
			showHouseInfo(hausid);			
		}
		
		}
		guilayer.getMousefollower().setVisible(false);
	}

	
	
	/**
	 * Mousefollower updaten
	 * @author Martika
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
			spionagelabelBeschr(isPresent, "Fernglas anbringen");

		
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
	 * @author Martika, Tobias
	 */
	public void clickPause() {
		//Pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
		
		if (guilayer.getButtonsMap().get("beschwichtigen").isEnabled()){
			guilayer.getButtonsMap().get("beschwichtigen").setEnabled(false);
		} else {
			if (guilayer.getSimulation().getSpielStunde()<2 || guilayer.getSimulation().getSpielStunde()>6){
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
			if ((char)(guilayer.getSimulation().getAgent().getHausId()+1+48) != guilayer.getSimulation().getAgent().getLocationId()){
				guilayer.getButtonsMap().get("nachHause").setEnabled(true);
			}
		}
		
		if (guilayer.getButtonsMap().get("aktionrazzia").isEnabled()){
			guilayer.getButtonsMap().get("aktionrazzia").setEnabled(false);
		} else {
			guilayer.getButtonsMap().get("aktionrazzia").setEnabled(true);
		}
		
		if (guilayer.getButtonsMap().get("Haus1").isEnabled()){
			for (int i=1; i<=Ressources.NUMBERHOUSES; i++){
				guilayer.getButtonsMap().get("Haus"+i).setEnabled(false);
				
			}
		} else{
			for (int i=1; i<=Ressources.NUMBERHOUSES; i++){
				guilayer.getButtonsMap().get("Haus"+i).setEnabled(true);
			}
		}
				
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		
	}

	
	
	/**
	 * Ingame-Fenster schlie�en
	 * @param fensterName Bezeichnung des Fensters
	 * @author Tobias
	 */
	public void closeWindow(String fensterName) {
		JLayeredPane frame = guilayer.getWindow(fensterName);
		frame.setVisible(false);
		frame.setEnabled(false);
	}
	
	
	
	/**
	 * Bestimmte Fenster �ffnen
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
	 * Klicks auf Buttons in Men�leiste
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

	/**
	 * Agent geht nach Hause
	 * @author Martika
	 */
	private void clickNachhause() {
		closeWindow("fensterhaus"); 
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		guilayer.getSimulation().getAgent().setMussWuseln("");
		guilayer.getButtonsMap().get("nachHause").setEnabled(false);
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), (char)(guilayer.getSimulation().getAgent().getHausId()+1+48));
		if (!guilayer.getSimulation().isWieschteAktion()){
			for (int i=0; i<guilayer.getSimulation().getPeople().size(); i++){
				if (guilayer.getSimulation().getPeople().get(i).getCurrentMove()=='s'){
					//Person darf sich wieder bewegen, sollte man einen Kuchen vorbeibringen wollen
					guilayer.getSimulation().getPeople().get(i).setMoves(new Stack());
				}
			}
		}
	}
	
	/**
	 * Agent m�chte einen Haushalt festnehmen
	 * @author Miriam 
	 */
	private void clickRazzia() {
		closeWindow("fensterhaus"); 
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*13, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}

	
	
	/**
	 * @author Martika
	 */
	private void clickWanzen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	/**
	 * @author Martika
	 */
	private void clickKamera() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 39, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	/**
	 * @author Martika
	 */
	private void clickHacken() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*2, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	/**
	 * @author Martika
	 */
	private void clickFernglas() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*3, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("spionage");
	}
	
	/**
	 * @author Martika
	 */
	private void clickKuchen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*6, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieschteAktion(false);
	}
	
	/**
	 * @author Martika
	 */
	private void clickUnterhalten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*7, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieschteAktion(false);
	}
	
	/**
	 * @author Martika
	 */
	private void clickFlirten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*8, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieschteAktion(false);
	}

	/**
	 * @author Martika
	 */
	private void clickHand() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*9, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		guilayer.getSimulation().setWieschteAktion(false);
	}

	/**
	 * @author Martika
	 */
	private void clickParkBeschwichtigen() {
		//kein Icon, da Einsatzort (Park) vorgegeben
		closeWindow("beschwichtigen");
		guilayer.getSimulation().calcWeg(guilayer.getSimulation().getAgent(), 'P');
		guilayer.getSimulation().getAgent().setMussWuseln("Park");
		guilayer.getButtonsMap().get("nachHause").setEnabled(true);
		guilayer.getSimulation().setWieschteAktion(false);
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
	 * Zeigt die Hausinformation f�r ein Haus an
	 * @param hausnr Hausnummer des Hases welches angezeigt werden soll
	 * @author Tobias, Martika(ifabfrage, ob Personen schon �berwacht werden)
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
			if(person.getHausId()==hausnr && ((Person)person).getIstFarbig()==true){
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

	
	public String getLastFunktioncode(){
		return this.lastFunktioncode;
	}
	
	public void setLastFunktioncode(String lastFunktioncode){
		this.lastFunktioncode = lastFunktioncode;
	}
}
