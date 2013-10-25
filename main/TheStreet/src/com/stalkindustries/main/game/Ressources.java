package com.stalkindustries.main.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Ressources {
	
	private static ArrayList<ArrayList<String>> location_ids;
	
	public static final int RASTERHEIGHT = 45; //Map Raster
	public static final int TORSOCNT =4; //Anzahl verschiedener Torsos in Sprite Grafik
	public static final int GAMESPEED = 40; // Zeitspanne in der eine Berechnung durchgefürt wird
	public static final Dimension SCREEN;
	public static final Dimension ZEROPOS;
	public static final int MAPHEIGHT = 720;
	public static final int MAPWIDTH = 1125;
	public static BufferedImage menubars;
	
	 
	 
	static{
		//Initialisierung der location_ids
		location_ids=read_from_csv("src\\com\\stalkindustries\\data\\russland_map.csv");
		SCREEN=Toolkit.getDefaultToolkit().getScreenSize();
		ZEROPOS = new Dimension();	//zeropos berechnen -> Koordinatenverschiebung
		ZEROPOS.setSize((SCREEN.getWidth()/2)-(MAPWIDTH/2),(SCREEN.getHeight()/2)-(MAPHEIGHT/2));
		
		try {
			menubars = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\gui_ingame_bars.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_ingame_bars.png");
			e.printStackTrace();
		}
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
	
		
	public static ArrayList<ArrayList<String>> getLocation_ids() {
		ArrayList<ArrayList<String>> loc_ids = new ArrayList<ArrayList<String>>();
		ArrayList<String>tmp;
		
		for(int i=0;i<location_ids.size();i++){
			tmp = new ArrayList<String>();
			for(int j=0;j<location_ids.get(i).size();j++){
				tmp.add(location_ids.get(i).get(j));
			}
			loc_ids.add(tmp);
		}
		
		return loc_ids;
	}
	
}
