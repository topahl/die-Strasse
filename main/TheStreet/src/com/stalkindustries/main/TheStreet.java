package com.stalkindustries.main;

import com.stalkindustries.main.game.GUI;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TheStreet {
	public static void main(String[] args) {
		GUI game = new GUI();
		
		//ArrayList<ArrayList<String>> list_of_lists;
		//list_of_lists = read_from_csv();
	}
	
	
	//Beschwerden an Sven und Miri
	public static ArrayList<ArrayList<String>> read_from_csv(String dateiName){
		ArrayList<ArrayList<String>> list_of_lists = new ArrayList<ArrayList<String>>();
		
		//File vorhanden?
		File file = new File(dateiName);
        if (!file.canRead() || !file.isFile())
            System.exit(0);
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(dateiName));
            String zeile = null;
            while ((zeile = in.readLine()) != null) {
            	list_of_lists.add(null);	//Zeile hinzufügen
            	for(int i=0;i<zeile.split(";").length;i++){
            		list_of_lists.get(i).add(zeile.split(";")[i]);	//Spalte hinzufügen
            	}
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		return list_of_lists;
	}
}
