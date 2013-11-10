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
 * 
 * @author Tobias
 *
 */
public class ControlMenu implements IControl, ListSelectionListener {
	
	private Menu mainmenu;
	
	public ControlMenu(Menu mainmenu) {
		this.mainmenu=mainmenu;
	}
	
	public void call(String funktion) {
		System.out.println("You pressed: "+funktion);
		if(funktion.startsWith("level:"))
			beginGame(funktion);
		if(funktion.equals("beenden"))
			exitMenu();
		if(funktion.equals("start"))
			pressedStart();
		if(funktion.equals("back"))
			mainmenu.showLayer(Menu.LAYERMENU);
		if(funktion.equals("profil"))
			openProfil();
		if(funktion.equals("highscore"))
			mainmenu.showLayer(Menu.LAYERHIGHSCORE);
		if(funktion.equals("tutorial"))
			mainmenu.showLayer(Menu.LAYERTUTORIAL);
		if(funktion.equals("credits"))
			mainmenu.showLayer(Menu.LAYERCREDITS);
		if(funktion.equals("create"))
			this.createUser();
		if(funktion.equals("use")) 
			this.changeCurrentUser();
		if(funktion.equals("alleScores")) 
			mainmenu.showLayer(Menu.LAYERHIGHSCORE);
		if(funktion.equals("meineScores")) 
			mainmenu.showLayer(Menu.LAYERPERSHIGHSCORE);
		if(funktion.equals("tutorialBack")) {
			mainmenu.tutorialBack();
		}
		if(funktion.equals("tutorialNext")) {
			mainmenu.tutorialNext();
		}

	}
	
	
	private void pressedStart() {
		if(mainmenu.getCurrentUser()!= "")
			mainmenu.showLayer(Menu.LAYERLEVEL);
		else
			openProfil();
		
	}

	private void changeCurrentUser() {
		Object listselection = mainmenu.getBenutzerliste().getSelectedValue();
		if(listselection != null){			
			mainmenu.setCurrentUser(listselection.toString());
			mainmenu.showLayer(Menu.LAYERMENU);
			mainmenu.readUserHighscores(listselection.toString());
		}
		mainmenu.resetPersHighscore();
		
	}

	private void beginGame(String levelname){
		TheStreet.loadLeve(levelname.substring(6), mainmenu.getCurrentUser());
		exitMenu();
	}
	
	private void exitMenu(){
		mainmenu.dispose();
	}

	@Override
	public void mousePresent(String funktion, boolean isPresent) {
		// TODO do nothing
		
	}
	/**
	 *@author Tobias
	 */
	private void createUser(){
		String user = this.mainmenu.popInputUsername().trim();
		if(!user.equals("")){
			File folder = new File("res\\user\\");
			File file = new File("res\\user\\"+user+".usr");
				
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
	 * Reads Userfiles
	 * @author Tobias
	 * @return Viewpoint list for JList with Usernames
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	 * @author Tobias
	 */
	@SuppressWarnings("unchecked")
	public void openProfil(){
		mainmenu.getBenutzerliste().setModel(getPlayernames());
		mainmenu.showLayer(Menu.LAYERPROFIL);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(!arg0.getValueIsAdjusting())
			mainmenu.updatePersHighscore();
		
		
	}
	
}
