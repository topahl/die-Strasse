package com.stalkindustries.main.menu;

import com.stalkindustries.main.IControl;
import com.stalkindustries.main.TheStreet;
	
public class Control implements IControl{
	
	private Menu mainmenu;
	
	public Control(Menu mainmenu){
		this.mainmenu=mainmenu;
	}
	
	public void call(String funktion){
		if(funktion == "start")
			beginGame();
		if(funktion == "beenden")
			exitMenu();
			
	}
	
	private void beginGame(){
		TheStreet.loadLeve("russland");
		exitMenu();
	}
	
	private void exitMenu(){
		mainmenu.dispose();
	}
	
}
