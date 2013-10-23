package com.stalkindustries.main;

import com.stalkindustries.main.game.GUILayer;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TheStreet {
	public static void main(String[] args) {
		GUILayer game = new GUILayer();
		
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
	
	
	//Beschwerden an Sven und Miri
	public static ArrayList<ArrayList<String>> read_from_csv(String dateiName){
		ArrayList<ArrayList<String>> list_of_lists = new ArrayList<ArrayList<String>>();
		
		//File vorhanden?
		File file = new File(dateiName);
        if (!file.canRead() || !file.isFile()){
        	System.out.print("No file found.");
            System.exit(0);
        }
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(dateiName));
            String zeile = null;
            ArrayList<String> tmp;
            while ((zeile = in.readLine()) != null) {
            	tmp = new ArrayList<String>();
            	for(int i=0;i<zeile.split(";").length;i++){
            		tmp.add(zeile.split(";")[i]);	//Spalte hinzufügen
            	}
            	list_of_lists.add(tmp);	//Zeile hinzufügen
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		return list_of_lists;
	}
}
