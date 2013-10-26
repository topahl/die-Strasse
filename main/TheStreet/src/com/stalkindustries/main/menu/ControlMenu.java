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
		if(funktion == "russland")
			beginGame();
		if(funktion == "beenden")
			exitMenu();
		if(funktion == "start")
			showLevelSel();
			
	}
	
	private void beginGame(){
		TheStreet.loadLeve("russland");
		exitMenu();
	}
	
	private void exitMenu(){
		mainmenu.dispose();
	}

	@Override
	public void mousePresent(String funktion, boolean isPresent) {
		// TODO do nothing
		
	}
	
	private void showLevelSel(){
		JLayeredPane pane = mainmenu.getMenuLayer("levelsel");
		pane.setVisible(true);
		pane.setEnabled(true);
		closeMenuLayer("mainmenu");
	}
	
	
	private void closeMenuLayer(String layer){
		JLayeredPane pane = mainmenu.getMenuLayer(layer);
		pane.setVisible(false);
		pane.setEnabled(false);
	}
	
	
	
}
