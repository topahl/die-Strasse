package com.stalkindustries.main.game;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	String lastFunktioncode = "";
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
				|| funktion.equals("aktionenBeschwichtigen") || funktion.equals("aktionenSpionage") ||
				funktion.equals("nachHause")) {
			guilayer.getMousefollower().setVisible(false);
		}
		
		lastFunktioncode = funktion;
	}

	
	private void clickRemoveFernglas() {
		// TODO Auto-generated method stub
		
	}



	private void clickRemoveHacken() {
		// TODO Auto-generated method stub
		
	}



	private void clickRemoveKamera() {
		// TODO Auto-generated method stub
		
	}



	private void clickRemoveWanze() {
		int currentHouse = Integer.parseInt(lastFunktioncode.substring(4,5))-1 ;
		closeWindow("fensterhaus"); 
		guilayer.getSimulation().getHouses().get(currentHouse).getUeberwachungsmodule().remove("Wanze");
		
		guilayer.getButtonsMap().get("aktionNachhause").setEnabled(true);
		guilayer.getSimulation().get_agent().setMussWuseln("Wanze");
		guilayer.getSimulation().berechne_weg(null, guilayer.getSimulation().get_agent(), (char)(currentHouse+1+48));
		
		
//		guilayer.getSimulation().getHouses().get(currentHouse).getUeberwachungsmodule().remove("Wanze")
	
			}





	private void quizAntwort(String antwort){ //antwort=A || B || C
		this.quiz.analyzeAntwort(antwort);
		this.quiz.calcMisstrauenAfterQuiz();
		closeWindow("quizfenster");
	}
	
	/**
	 * Klicks auf Häuser abfangen
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

		if(lastFunktioncode.equals("aktionWanze")){
			for (int i=0; i<guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().size(); i++){
				if (guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().get(i).equals("Wanze")){
					istVorhanden=true;
					break;
				}	
			}
			if (!istVorhanden){
				guilayer.getSimulation().berechne_weg(null, guilayer.getSimulation().get_agent(), (char)(hausid+48));
				guilayer.getSimulation().get_agent().setMussWuseln("Wanze");
				guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().add("Wanze");
				guilayer.getButtonsMap().get("aktionNachhause").setEnabled(true);
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
				guilayer.getSimulation().berechne_weg(null, guilayer.getSimulation().get_agent(), (char)(hausid+48));
				guilayer.getSimulation().get_agent().setMussWuseln("Kamera");
				guilayer.getSimulation().getHouses().get(hausid-1).getUeberwachungsmodule().add("Kamera");
				guilayer.getButtonsMap().get("aktionNachhause").setEnabled(true);
			}
			istVorhanden = false;
		}

//			
//		if(lastFunktioncode.equals("aktionHacken"))
//			
//		if(lastFunktioncode.equals("aktionFernglas"))
//			
//		if(lastFunktioncode.equals("aktionParkSpionage"))
			
		if(!lastFunktioncode.startsWith("aktion")){
			schowHausinfo(hausid);			
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
			spionagelabelBeschr(isPresent, "Ausspionieren");
		if(funktion.equals("aktionParkSpionage"))
			spionagelabelBeschr(isPresent, "Im Park spionieren");
		if(funktion.equals("aktion6Spionage"))
			spionagelabelBeschr(isPresent, "Aufgabe 6");
		
		//Aktionen Beschwichtigen
		if(funktion.equals("aktionKuchen"))
			beschwichtigenlabelBeschr(isPresent, "Kuchen vorbeibringen");
		if(funktion.equals("aktionUnterhalten"))
			beschwichtigenlabelBeschr(isPresent, "Sich unterhalten");
		if(funktion.equals("aktionFlirten"))
			beschwichtigenlabelBeschr(isPresent, "Flirten");
		if(funktion.equals("aktionHand"))
			beschwichtigenlabelBeschr(isPresent, "Helfen");
		if(funktion.equals("aktionParkBeschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "Im Park unterhalten");
		if(funktion.equals("aktion6Beschwichtigen"))
			beschwichtigenlabelBeschr(isPresent, "Aktion 6");
	}
	
	
	
	/**
	 * Spiel pausieren
	 */
	private void clickPause() {
		//Pause-Funktion von GUILayer aufrufen
		guilayer.updateTimerStatus();
		
		//TODO: funktioniert das auch später noch im Spiel?
		//z.B. Menübuttons sind nicht immer disabled im spielverlauf ... vg Stephan
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
		if (guilayer.getButtonsMap().get("aktionNachhause").isEnabled()){
			guilayer.getButtonsMap().get("aktionNachhause").setEnabled(false);
		} else {
			if ((char)(guilayer.getSimulation().get_agent().get_haus_id()+1+48) != guilayer.getSimulation().get_agent().get_location_id()){
				guilayer.getButtonsMap().get("aktionNachhause").setEnabled(true);
			}
		}
		
		
		
		
		
		closeWindow("spionage");
		closeWindow("beschwichtigen");
		
		// von Pause unabhängig?
//		
//		if (guilayer.getButtonsMap().get("aktionRazzia").isEnabled()){
//			guilayer.getButtonsMap().get("aktionRazzia").setEnabled(false);
//		} else {
//			guilayer.getButtonsMap().get("aktionRazzia").setEnabled(false);
//		}
		
		//TODO: weitere Aktionen (buttons disablen, "Pause"-Fenster anzeigen etc.) bei Pause einfügen
	}

	
	
	/**
	 * Ingame-Fenster schließen
	 * @param fensterName Bezeichnung des Fensters
	 */
	private void closeWindow(String fensterName) {
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
	 * Klicks auf Buttons in Menüleiste
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
		guilayer.getSimulation().berechne_weg(null, guilayer.getSimulation().get_agent(), (char)(guilayer.getSimulation().get_agent().get_haus_id()+1+48));
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
	
	/**
	 * Zeigt die Hausinformation für ein Haus an
	 * @param hausnr Hausnummer des Hases welches angezeigt werden soll
	 */
	private void schowHausinfo(int hausnr){
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
			if(person.get_haus_id()==hausnr){
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
		
	}
	
	
//	public int getHouseId(){
//		return this.house_id;
//	}
//	
//	public void setHouseId(int hous_id){
//		this.house_id = hous_id;
//	}

}
