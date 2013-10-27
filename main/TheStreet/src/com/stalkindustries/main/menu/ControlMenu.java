package com.stalkindustries.main.menu;

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
			mainmenu.showLayer(Menu.LEVELSELECT);
		if(funktion.equals("back"))
			mainmenu.showLayer(Menu.MENULAYER);
			
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
	
	
	
	
}
