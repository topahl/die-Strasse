package com.stalkindustries.main.menu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

import com.stalkindustries.main.IControl;
import com.stalkindustries.main.TheStreet;

/**
 * 
 * @author Tobias
 *
 */
public class ControlMenu implements IControl{
	
	private Menu mainmenu;
	
	public ControlMenu(Menu mainmenu){
		this.mainmenu=mainmenu;
	}
	
	public void call(String funktion){
		System.out.println("You pressed: "+funktion);
		if(funktion.startsWith("level:"))
			beginGame(funktion);
		if(funktion.equals("beenden"))
			exitMenu();
		if(funktion.equals("start"))
			mainmenu.showLayer(Menu.LAYERLEVEL);
		if(funktion.equals("back"))
			mainmenu.showLayer(Menu.LAYERMENU);
		if(funktion.equals("profil"))
			mainmenu.showLayer(Menu.LAYERPROFIL);
		if(funktion.equals("highscore"))
			mainmenu.showLayer(Menu.LAYERHIGHSCORE);
		if(funktion.equals("tutorial"))
			mainmenu.showLayer(Menu.LAYERTUTORIAL);
		if(funktion.equals("create"))
			this.createUser();
			
		
			
	}
	
	private void beginGame(String levelname){
		TheStreet.loadLeve(levelname.substring(6));
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
		String user = this.mainmenu.getInputUsername();
		
		File folder = new File("res\\user\\"+user+".usr");
			
    	if(!folder.canWrite())
    		System.err.println("Can't write to user files");
    	try {
    		folder.mkdirs();
			folder.createNewFile();
		} catch (IOException e) {
			System.err.println("Error while creating Userfile");
			e.printStackTrace();
		}
	}
	
	
	
	
}
