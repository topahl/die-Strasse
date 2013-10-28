package com.stalkindustries.main.game;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import com.stalkindustries.main.IControl;

//Kreiert vom unglaublichen Stephan
//auf Basis von Tobias unglaublicher Arbeit
/**
 * 
 * @author Tobias, Stephan, Tiki
 *
 */
public class Control implements IControl {
	
	String lastFunktioncode = "";
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu k�nnen
	private GUILayer guilayer;
	private Quiz quiz;

	
	
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
	 * TODO Funktionalit�t komplett testen
	 */
	public void call(String funktion) {
		System.out.println("You pressed:"+funktion);
	
		//aus irgendeinem schwachsinnigen Grund haben wir Java 1.6 (steht auch im Pflichtenheft)
		//Switch-case mit Strings erst ab 1.7, schade.
		
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
		if(funktion.equals("aktionenSpionage"))
			clickAktionenSpionage();
		if(funktion.equals("aktionenBeschwichtigen"))
			clickAktionenBeschwichtigen();
		if(funktion.equals("aktionNachhause"))
			clickNachhause();
		if(funktion.equals("aktionRazzia"))
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
		if(funktion.equals("aktionParkBeschwichtigen"))
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
		if(funktion.equals("aktionParkSpionage"))
			clickParkSpionage();		
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
				|| funktion.equals("aktionenBeschwichtigen") || funktion.equals("aktionenSpionage")) {
			guilayer.getMousefollower().setVisible(false);
		}
		
		lastFunktioncode = funktion;
	}

	
	private void quizAntwort(String antwort){ //antwort=A || B || C
		this.quiz.analyzeAntwort(antwort);
		this.quiz.calcMisstrauenAfterQuiz();
		closeWindow("quizfenster");
	}
	
	/**
	 * Klicks auf H�user abfangen
	 */

	private void clickHaus(int hausid) {
		boolean istVorhanden = false;
		
		// hausid von 1-9, get_haus_id 0-8 => Deswegen plus 1
		if (hausid != guilayer.getSimulation().get_agent().get_haus_id()+1){
			
		
		
//	 TODO "aktion6Beschwichtigen" && "aktion6Spionage" werden nicht abgefragt
//		if(lastFunktioncode.equals("aktionKuchen"))
//			
//		if(lastFunktioncode.equals("aktionUnterhalten"))
//			
//		if(lastFunktioncode.equals("aktionFlirten"))
//			
//		if(lastFunktioncode.equals("aktionHand"))
//			
//		if(lastFunktioncode.equals("aktionParkBeschwichtigen"))
//			
//		
//
		if(lastFunktioncode.equals("aktionWanze")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Wanze")){
					istVorhanden=true;
					break;
				}	
			}
			if (!istVorhanden){
				guilayer.getSimulation().bewegungAgentWanze(hausid);
				guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().add("Wanze");
				guilayer.getSimulation().calc_misstrauen_after_ueberwachungs_action("Wanze", hausid);
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
				// gleiche Bewegung wie Verwanzen
				guilayer.getSimulation().bewegungAgentWanze(hausid);
				guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().add("Kamera");
				guilayer.getSimulation().calc_misstrauen_after_ueberwachungs_action("Kamera", hausid);
			}
			istVorhanden = false;
		}
			
//			
//		if(lastFunktioncode.equals("aktionHacken"))
//			
//		if(lastFunktioncode.equals("aktionFernglas"))
//			
//		if(lastFunktioncode.equals("aktionParkSpionage"))
			
		}
		guilayer.getMousefollower().setVisible(false);
	}

	
	
	/**
	 * Mousefollower updaten
	 * @author Tiki
	 */
	@Override
	public void mousePresent(String funktion, boolean isPresent) {
//		if(isPresent)//TODO entfernen
//			System.out.println("You entered:"+funktion);
//		else
//			System.out.println("You leaved:"+funktion);
		
		//Aktionen Spionage
		if(funktion.equals("aktionWanze"))
			spionagelabelBeschr(isPresent, "Wanzen anbringen");
		if(funktion.equals("aktionKamera"))
			spionagelabelBeschr(isPresent, "Kameras anbringen");
		if(funktion.equals("aktionHacken"))
			spionagelabelBeschr(isPresent, "Laptop hacken");
		if(funktion.equals("aktionFernglas"))
			spionagelabelBeschr(isPresent, "ausspionieren");
		if(funktion.equals("aktionParkSpionage"))
			spionagelabelBeschr(isPresent, "im Park spionieren");
		if(funktion.equals("aktion6Spionage"))
			spionagelabelBeschr(isPresent, "Aufgabe 6");
		
		//Aktionen Beschwichtigen
		if(funktion.equals("aktionKuchen"))
			beschwichtigenlabelBeschr(isPresent, "Kuchen vorbeibringen");
		if(funktion.equals("aktionUnterhalten"))
			beschwichtigenlabelBeschr(isPresent, "sich Unterhalten");
		if(funktion.equals("aktionFlirten"))
			beschwichtigenlabelBeschr(isPresent, "Flirten");
		if(funktion.equals("aktionHand"))
			beschwichtigenlabelBeschr(isPresent, "helfen");
		if(funktion.equals("aktionParkBeschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "im Park Unterhalten");
		if(funktion.equals("aktion6Beschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "Aktion 6");
	}
	
	
	
	/**
	 * Spiel pausieren
	 */
	private void clickPause() {
		//Pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
		
		//TODO: funktioniert das auch sp�ter noch im Spiel?
		//z.B. Men�buttons sind nicht immer disabled im spielverlauf ... vg Stephan
		if (guilayer.getButtonsMap().get("aktionenBeschwichtigen").isEnabled()){
			guilayer.getButtonsMap().get("aktionenBeschwichtigen").setEnabled(false);
		} else {
			guilayer.getButtonsMap().get("aktionenBeschwichtigen").setEnabled(true);
		}
		if (guilayer.getButtonsMap().get("aktionenSpionage").isEnabled()){
			guilayer.getButtonsMap().get("aktionenSpionage").setEnabled(false);
		} else {
			guilayer.getButtonsMap().get("aktionenSpionage").setEnabled(true);
		}
		// von Pause unabh�ngig?
		closeWindow("spionage");
		closeWindow("beschwichtigen");
//		if (guilayer.getButtonsMap().get("aktionNachhause").isEnabled()){
//			guilayer.getButtonsMap().get("aktionNachhause").setEnabled(false);
//		} else {
//			guilayer.getButtonsMap().get("aktionNachhause").setEnabled(true);
//		}
//		if (guilayer.getButtonsMap().get("aktionRazzia").isEnabled()){
//			guilayer.getButtonsMap().get("aktionRazzia").setEnabled(false);
//		} else {
//			guilayer.getButtonsMap().get("aktionRazzia").setEnabled(false);
//		}
		
		//TODO: weitere Aktionen (buttons disablen, "Pause"-Fenster anzeigen etc.) bei Pause einf�gen
	}

	
	
	/**
	 * Ingame-Fenster schlie�en
	 * @param fensterName Bezeichnung des Fensters
	 */
	private void closeWindow(String fensterName) {
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
		guilayer.endGame();
	}
	
	
	/**
	 * Klicks auf Buttons in Men�leiste
	 */
	private void clickAktionenSpionage() {
		closeWindow("beschwichtigen");
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
		JLayeredPane frame = guilayer.getWindow("beschwichtigen");
		if(frame.isVisible())
			closeWindow("beschwichtigen");
		else{
			frame.setEnabled(true);
			frame.setVisible(true);
		}
	}

	private void clickNachhause() {
		guilayer.getButtonsMap().get("aktionNachhause").setEnabled(false);
	}
	
	private void clickRazzia() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*12, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
		closeWindow("spionage");
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
	
	private void clickParkSpionage() {
		//kein Icon, da Einsatzort (Park) vorgegeben
		closeWindow("spionage");
	}
	
	private void clickKuchen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*6, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
	}
	
	private void clickUnterhalten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*7, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
	}
	
	private void clickFlirten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*8, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
	}

	private void clickHand() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*9, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
		closeWindow("beschwichtigen");
	}

	private void clickParkBeschwichtigen() {
		//kein Icon, da Einsatzort (Park) vorgegeben
		closeWindow("beschwichtigen");
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
	

}
