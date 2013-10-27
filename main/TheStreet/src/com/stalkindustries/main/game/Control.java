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
	
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu k�nnen
	private GUILayer guilayer;

	
	
	/**
	 * Konstruktor l�dt Ingamefenster-Objekt
	 * @param guilayer Objekt des Ingamefensters
	 */
	public Control(GUILayer guilayer) {
		this.guilayer = guilayer;
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

		//Buttons H�user (invisible)
		if(funktion.equals("Haus1"))
			clickHaus1();		
		if(funktion.equals("Haus2"))
			clickHaus2();
		if(funktion.equals("Haus3"))
			clickHaus3();
		if(funktion.equals("Haus4"))
			clickHaus4();
		if(funktion.equals("Haus5"))
			clickHaus5();
		if(funktion.equals("Haus6"))
			clickHaus6();
		if(funktion.equals("Haus7"))
			clickHaus7();
		if(funktion.equals("Haus8"))
			clickHaus8();
		if(funktion.equals("Haus9"))
			clickHaus9();

		//Mousefollower abschalten bei bestimmten Buttons
		if(funktion.equals("pause") || funktion.equals("close")
				|| funktion.equals("aktionenBeschwichtigen") || funktion.equals("aktionenSpionage")) {
			guilayer.getMousefollower().setVisible(false);
		}
		
	}

	
	
	/**
	 * Klicks auf H�user abfangen
	 */
	private void clickHaus9() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus8() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus7() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus6() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus5() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus4() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus3() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus2() {
		guilayer.getMousefollower().setVisible(false);
	}

	private void clickHaus1() {
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
	 */
	private void closeWindow(String fensterName) {
		JLayeredPane frame = guilayer.getWindow(fensterName);
		frame.setVisible(false);
		frame.setEnabled(false);
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
