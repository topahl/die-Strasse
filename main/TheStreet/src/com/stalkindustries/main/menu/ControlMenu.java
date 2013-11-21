package com.stalkindustries.main.menu;

import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.stalkindustries.main.IControl;
import com.stalkindustries.main.TheStreet;
import com.stalkindustries.main.game.Ressources;

/**
 * Diese Klasse  reagiert auf alle Benutzereingaben im Hauptmenü und leitet entsprechende Reaktionen ein.
 * 
 * @author Tobias
 */
public class ControlMenu implements IControl, ListSelectionListener {
	
	private Menu mainmenu;
	
	
	public ControlMenu(Menu mainmenu) {
		this.mainmenu=mainmenu;
	}

	/**
	 * @author Tobias
	 * @see IControl#call(String)
	 */
	public void call(String funktion) {
		if(funktion.startsWith("level:"))		//level:*
			beginGame(funktion);
		if(funktion.equals("beenden"))			//beenden
			exitMenu();
		if(funktion.equals("start"))			//start
			pressedStart();
		if(funktion.equals("back"))				//back
			mainmenu.showLayer(Menu.LAYERMENU);
		if(funktion.equals("profil"))			//profil
			openProfil();
		if(funktion.equals("highscore"))		//highscore
			openHighscore();
		if(funktion.equals("tutorial"))			//tutorial
			mainmenu.showLayer(Menu.LAYERTUTORIAL);
		if(funktion.equals("credits"))			//credits
			mainmenu.showLayer(Menu.LAYERCREDITS);
		if(funktion.equals("create"))			//create
			this.createUser();
		if(funktion.equals("use")) 				//use
			this.changeCurrentUser();
		if(funktion.equals("alleScores")) 		//allScores
			mainmenu.showLayer(Menu.LAYERHIGHSCORE);
		if(funktion.equals("meineScores")) 		//mainScores
			mainmenu.showLayer(Menu.LAYERPERSHIGHSCORE);
		if(funktion.equals("tutorialBack"))		//tutorialBack
			mainmenu.tutorialBack();
		if(funktion.equals("tutorialNext"))		//tutorialNext
			mainmenu.tutorialNext();
	}
	
	/**
	 * @author Tobias
	 * Sicherheitsabfrage, ob sich der Benutzer schon angemeldet hat. 
	 * Zugriff auf Levelauswahl nur als angemeldeter Benutzer
	 */
	private void pressedStart() {
		if(mainmenu.getCurrentUser()!= "")
			mainmenu.showLayer(Menu.LAYERLEVEL);
		else
			openProfil();
	}
	
	/**
	 * @author Stephan
	 * Sicherheitsabfrage, ob sich der Benutzer schon angemeldet hat. 
	 * Zugriff auf Highscore nur als angemeldeter Benutzer
	 */
	private void openHighscore() {
		if(mainmenu.getCurrentUser()!= "")
			mainmenu.showLayer(Menu.LAYERHIGHSCORE);
		else
			openProfil();
	}
	
	/**
	 * @author Tobias
	 * Wechselt den aktuellen Benutzer und resettet ggf eine vorhandene Auswahl in den Highscore-details.
	 */
	private void changeCurrentUser() {
		Object listselection = mainmenu.getBenutzerliste().getSelectedValue();
		if(listselection != null){			
			mainmenu.setCurrentUser(listselection.toString());
			mainmenu.showLayer(Menu.LAYERMENU);
			mainmenu.readUserHighscores(listselection.toString());
		}
		mainmenu.resetPersHighscore();
		
	}

	/**
	 * Startet ein Spiel mit em angegebenen Level
	 * @param levelname Name des Levels, welches geladen werden soll.
	 * @author Tobias
	 */
	private void beginGame(String levelname){
		TheStreet.loadLeve(levelname.substring(6), mainmenu.getCurrentUser());
		exitMenu();
	}
	
	/**
	 * Beendet das Menü
	 * @author Tobias
	 */
	private void exitMenu(){
		mainmenu.dispose();
	}

	
	/**
	 * @author Tobias
	 * @see IControl#mousePresent(String, boolean)
	 */
	public void mousePresent(String funktion, boolean isPresent) {
		// do nothing
	}
	
	
	/**
	 * Kümmert sich um das anlegen eines Neuen Benutzers, inc. der Benutzerdatei
	 *@author Tobias
	 */
	private void createUser(){
		String user = this.mainmenu.popInputUsername().trim();
		if(!user.equals("")){ //leere Eingabe anfangen
			File folder = new File("res\\user\\");
			File file = new File("res\\user\\"+user+".usr");
			
			//ggf Ordner anlegen, dann Datei anlegen
	    	if(!folder.canWrite())
	    		System.err.println("Can't write to user files");
	    	try {
	    		folder.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Error while creating Userfile");
				e.printStackTrace();
			}
	    	mainmenu.showLayer(Menu.LAYERMENU);
	    	mainmenu.setCurrentUser(user);
		}
		mainmenu.resetPersHighscore();
	}
	
	/**
	 * Ließt alle vorhandenen Benutzerdaten ein und erstellt eine Liste zur Anzeige-
	 * @author Tobias
	 * @return ListModel für die Anzeige auf dem Bildschirm
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) //Notwendig um eine Kompatibilität für JDK 1.6 & 1.7 zu ermöglichen
	private DefaultListModel getPlayernames(){
		DefaultListModel output = new DefaultListModel();
		File folder = new File(Ressources.HOMEDIR+"res\\user\\");
    	for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()&&fileEntry.getName().endsWith(".usr")) {
                output.addElement(fileEntry.getName().substring(0, fileEntry.getName().length()-4));
            } 
    	}
    	return output;
    }
	
	
	/**
	 * Öffnet das Profil nachdem die Userdaten neu eingelesen wurden.
	 * @author Tobias
	 */
	@SuppressWarnings("unchecked") //Notwendig um eine Kompatibilität für JDK 1.6 & 1.7 zu ermöglichen
	public void openProfil(){
		mainmenu.getBenutzerliste().setModel(getPlayernames());
		mainmenu.showLayer(Menu.LAYERPROFIL);
	}

	/**
	 * @author Tobias
	 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent arg0) {
		if(!arg0.getValueIsAdjusting())
			mainmenu.updatePersHighscore();
		
		
	}
	
}
