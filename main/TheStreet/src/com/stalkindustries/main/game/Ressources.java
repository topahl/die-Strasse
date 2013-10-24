package com.stalkindustries.main.game;

import java.util.ArrayList;

import com.stalkindustries.main.TheStreet;

public class Ressources {
	
	private static ArrayList<ArrayList<String>> location_ids;
	
	static{
		//Initialisierung der location_ids
		//TODO richtigen Dateipfad später angeben
		location_ids=TheStreet.read_from_csv("C:/Users/Martika/Desktop/Dropbox/Software Engineering/Grafikdesign/Fertig/russland_map.csv");
		
		
	}
	
	
	
	public static ArrayList<ArrayList<String>> getLocation_ids() {
		return location_ids;
	}


//
//	public void setLocation_ids(ArrayList<ArrayList<String>> location_ids) {
//		this.location_ids = location_ids;
//	}
}
