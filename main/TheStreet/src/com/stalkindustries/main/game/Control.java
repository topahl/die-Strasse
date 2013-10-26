package com.stalkindustries.main.game;

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
	
	
	//Handling für Mousefollower Icons 
	
	if (funktion.equals("aktionKuchen")){
		
	}
	if (funktion.equals("aktionUnterhalten")){
		
	}
	if (funktion.equals("aktionFlirten")){
		
	}
	if (funktion.equals("aktionHand")){
		
	}
	if (funktion.equals("aktionParkBeschwichtigen")){
		
	}
	
	}

	
	
	private void clickPause() {
		//pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
		guilayer.getButtonsMap().get("aktionenBeschwichtigen").setEnabled(false);
		guilayer.getButtonsMap().get("aktionenSpionage").setEnabled(false);
		guilayer.getButtonsMap().get("aktionNachhause").setEnabled(false);
		guilayer.getButtonsMap().get("aktionRazzia").setEnabled(false);
		//TODO: weitere Aktionen (buttons disablen, "Pause"-Fenster anzeigen etc.) bei Pause einfügen
	}
	
	private void clickExit() {
		guilayer.endGame();
	}
	
	private void clickAktionSpionage() {
		
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
