package com.stalkindustries.main;

import com.stalkindustries.main.game.GUILayer;
import com.stalkindustries.main.menu.Menu;

public class TheStreet {
	public static void main(String[] args) {
		
		Menu menu = new Menu();
		
	}
	public static void loadLeve(String levelname, String agentname){
		GUILayer game = new GUILayer(levelname, agentname);
	}
	
	public static void loadMenu(String playername){
		Menu menu = new Menu(playername);
	}
}
