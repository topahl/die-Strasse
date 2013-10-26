package com.stalkindustries.main.game;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
//Kreiert vom unglaublichen Stephan
//auf Basis von Tobias unglaublicher Arbeit
import com.stalkindustries.main.IControl;

public class Control implements IControl {
	
	//Laden des Spielfenster-Objektes, um auf Funktionen davon zugreifen zu können
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
		if(funktion == "pause")
			clickPause();
		if(funktion == "close")
			clickExit();
		if(funktion == "closeSpionage")
			closeSpionageMenu();
		if(funktion == "aktionenSpionage")
			clickAktionSpionage();
		if(funktion == "closeBeschwichtigen")
			closeBeschwichtigenMenu();
		if(funktion == "aktionenBeschwichtigen")
			clickAktionBeschwichtigen();
		if (funktion.equals("aktionKuchen")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*6, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionUnterhalten")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*7, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionFlirten")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*8, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionHand")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*9, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionParkBeschwichtigen")){
//			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
//			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionWanze")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionKamera")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 39, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionHacken")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*2, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionFernglas")){
			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, (39)*3, 39, 39)));
			guilayer.getMousefollower().setVisible(true);
		}
		if (funktion.equals("aktionParkSpionage")){
//			guilayer.getMousefollower().setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
//			guilayer.getMousefollower().setVisible(true);
		}
		
		if (funktion.equals("pause") || funktion.equals("close") || funktion.equals("aktionenBeschwichtigen") || funktion.equals("aktionenSpionage")){
			guilayer.getMousefollower().setVisible(false);
		}
		
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
		// von Pause unabhängig?
		
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
		
		//TODO: weitere Aktionen (buttons disablen, "Pause"-Fenster anzeigen etc.) bei Pause einfügen
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
		
	}
	
	private void clickKamera() {
		
	}
	
	private void clickHacken() {
		
	}
	
	private void clickFernglas() {
		
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
		
	}
	
	private void clickUnterhalten() {
		
	}
	
	private void clickFlirten() {
		
	}
	
	private void clickNachhause() {
		
	}
	
	private void clickHand() {
		
	}
	
	private void clickParkBeschwichtigen() {
		
	}
	
	private void clickRazzia() {
		
	}

}
