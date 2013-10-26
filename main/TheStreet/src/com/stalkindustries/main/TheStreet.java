package com.stalkindustries.main;

import com.stalkindustries.main.game.GUILayer;
import com.stalkindustries.main.menu.Menu;

public class TheStreet {
	public static void main(String[] args) {
		
		Menu menu = new Menu();
		
		
		
		//CSV Test
		/*ArrayList<ArrayList<String>> list_of_lists;
		list_of_lists = read_from_csv("C://Users/Miriam/Documents/Ausbildung/DHBW Mannheim/Semester/3.Semester_WS_2013_2014/Software Engineering I/Github/die-Strasse/main/TheStreet/src/com/stalkindustries/main/Quizfragen.csv");
		System.out.print(list_of_lists.size());
		for(int i=0;i<list_of_lists.size();i++){
			for(int j=0;j<list_of_lists.get(i).size();j++){
				System.out.print(list_of_lists.get(i).get(j));
			}
			System.out.print("\n");
		}*/
	}
	public static void loadLeve(String levelname){
		GUILayer game = new GUILayer(levelname);
	}
	
	public static void loadMenu(){
		Menu menu = new Menu();
	}
}
