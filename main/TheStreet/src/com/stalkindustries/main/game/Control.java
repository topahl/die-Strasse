package com.stalkindustries.main.game;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
//Kreiert vom unglaublichen Stephan
//auf Basis von Tobias unglaublicher Arbeit
import com.stalkindustries.main.IControl;

/**
 * 
 * @author Tobias
 *
 */
public class Control implements IControl {
	
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu k�nnen
	private GUILayer guilayer;
	private String currentButton;
	
	public Control(GUILayer guilayer){
		this.guilayer = guilayer;
	}
	
	//Call wird von Button bei Klick aufgerufen
	//Anhand des "Namens" entsprechende Funktion aufrufen
	public void call(String funktion) { //TODO implement
		System.out.println("You pressed:"+funktion);
		currentButton = funktion;
	
		//aus irgendeinem schwachsinnigen Grund haben wir Java 1.6 (steht auch im Pflichtenheft)
		//Switch-case mit Strings erst ab 1.7, schade.
		if(funktion.equals("pause"))
			clickPause();
		if(funktion.equals("close"))
			clickExit();
		if(funktion.equals("closeSpionage"))
			closeSpionageMenu();
		if(funktion.equals("aktionenSpionage"))
			clickAktionSpionage();
		if(funktion.equals("aktionNachhause"))
			clickNachhause();
		if(funktion.equals("aktionRazzia"))
			clickRazzia();
		if(funktion.equals("closeBeschwichtigen"))
			closeBeschwichtigenMenu();
		if(funktion.equals("aktionenBeschwichtigen"))
			clickAktionBeschwichtigen();
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
		if(funktion.equals("pause") || funktion.equals("close") || funktion.equals("aktionenBeschwichtigen") || funktion.equals("aktionenSpionage")){
			guilayer.getMousefollower().setVisible(false);
		}
		
	}
	
	@Override
	public void mousePresent(String funktion, boolean isPresent) {
//		if(isPresent)
//			System.out.println("You entered:"+funktion);
//		else
//			System.out.println("You leved:"+funktion);
		
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
		if(funktion.equals("aktionKuchen"))
			beschwichtigenlabelBeschr(isPresent, "Kuchen vorbeibringen");
		if(funktion.equals("aktionUnterhalten"))
			beschwichtigenlabelBeschr(isPresent, "sich Unterhalten");
		if(funktion.equals("aktionFlirten"))
			beschwichtigenlabelBeschr(isPresent, "Flirten");
		if(funktion.equals("aktionHand"))
			beschwichtigenlabelBeschr(isPresent, "Helfen");
		if(funktion.equals("aktionParkBeschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "im Park Unterhalten");
		if(funktion.equals("aktion6Beschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "Aktion 6");
	}
	
	
	private void closeBeschwichtigenMenu() {
		JLayeredPane frame = guilayer.getWindow("beschwichtigen");
		frame.setVisible(false);
		frame.setEnabled(false);
		
	}

	private void clickPause() {
		//pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
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
		closeBeschwichtigenMenu();
		closeSpionageMenu();
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
	
	private void closeSpionageMenu(){
		JLayeredPane frame = guilayer.getWindow("spionage");
		frame.setVisible(false);
		frame.setEnabled(false);
	}
	
	private void clickExit() {
		guilayer.endGame();
	}
	
	private void clickAktionSpionage() {
		closeBeschwichtigenMenu();
		JLayeredPane frame = guilayer.getWindow("spionage");
		if(frame.isVisible())
			closeSpionageMenu();
		else{
			frame.setEnabled(true);
			frame.setVisible(true);
		}
	}
	
	private void clickWanzen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickKamera() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 39, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickHacken() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*2, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickFernglas() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*3, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickParkSpionage() {
		
	}
	
	private void clickAktionBeschwichtigen() {
		closeSpionageMenu();
		JLayeredPane frame = guilayer.getWindow("beschwichtigen");
		if(frame.isVisible())
			closeBeschwichtigenMenu();
		else{
			frame.setEnabled(true);
			frame.setVisible(true);
		}
	}
	
	private void clickKuchen() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*6, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickUnterhalten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*7, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickFlirten() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*8, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickNachhause() {
		
	}
	
	private void clickHand() {
		guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*9, 39, 39)));
		guilayer.getMousefollower().setVisible(true);
	}
	
	private void clickParkBeschwichtigen() {
		
	}
	
	private void clickRazzia() {
		
	}
	
	private void spionagelabelBeschr(boolean isPresent, String Text){
		if(isPresent){
			System.out.println(Text);
			
		}
	}
	private void beschwichtigenlabelBeschr(boolean isPresent, String Text){
		if(isPresent){
			System.out.println(Text);
		}
	}
	

}
