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
	private static ArrayList<ArrayList<String>> russian_names;
	private static ArrayList<ArrayList<String>> arabian_names;
	private static ArrayList<ArrayList<String>> russian_quiz;
	private static ArrayList<ArrayList<String>> arabian_quiz;
	
	public static final int RASTERHEIGHT = 45; //Map Raster
	public static final int TORSOCNT =4; //Anzahl verschiedener Torsos in Sprite Grafik
	public static final int GAMESPEED = 40; // Zeitspanne in der eine Berechnung durchgefürt wird
	public static final Dimension SCREEN;
	public static final Dimension ZEROPOS;
	public static final int MAPHEIGHT = 720;
	public static final int MAPWIDTH = 1125;
	public static String LEVELNAME;
	public static BufferedImage menubars;
	public static BufferedImage zahlen;
	public static BufferedImage ingamebutton;
	public static BufferedImage mainmenu;
	public static BufferedImage menubutton;
	public static BufferedImage ingameframe;
	
	public static final int NUMBERHOUSES = 9;
	public static final int NUMBERBESCHWICHTIGENACTIONS = 4; // Zahl ist grad nur Dummywert
	public static int AUSGEWAEHLTESLAND = 1; //TODO dynamisch ausgewähltes Land reinschreiben
	
	 
	 
	static{
		//Initialisierung der location_ids
		location_ids=read_from_csv("src\\com\\stalkindustries\\data\\russland_map.csv");
		russian_names = read_from_csv("src\\com\\stalkindustries\\data\\russland_namen.csv");
		arabian_names = read_from_csv("src\\com\\stalkindustries\\data\\saudiarabien_quizfragen.csv");
		russian_quiz = read_from_csv("src\\com\\stalkindustries\\data\\russland_quizfragen.csv");
		arabian_quiz = read_from_csv("src\\com\\stalkindustries\\data\\saudiarabien_quizfragen.csv");
		
		SCREEN=Toolkit.getDefaultToolkit().getScreenSize();
		ZEROPOS = new Dimension();	//zeropos berechnen -> Koordinatenverschiebung
		ZEROPOS.setSize((SCREEN.getWidth()/2)-(MAPWIDTH/2),(SCREEN.getHeight()/2)-(MAPHEIGHT/2));
		
		try {
			menubars = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\gui_ingame_bars.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_ingame_bars.png");
			e.printStackTrace();
		}
		
		try {
			zahlen = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\slice_digits.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_digits.png");  
			e.printStackTrace();
		}
		
		try {
			ingamebutton = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\slice_buttons_ingame.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_buttons_ingame.png");
			e.printStackTrace();
		}
		
		try {
			mainmenu = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\gui_menu_main.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_menu_main.png");
			e.printStackTrace();
		}
		
		try {
			menubutton = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\slice_buttons_menu.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_buttons_menu.png");
			e.printStackTrace();
		}
		
		try {
			ingameframe = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\slice_fenster.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_fenster.png,");
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
//		ArrayList<ArrayList<String>> loc_ids = new ArrayList<ArrayList<String>>();
//		ArrayList<String>tmp;
//		
//		for(int i=0;i<location_ids.size();i++){
//			tmp = new ArrayList<String>();
//			for(int j=0;j<location_ids.get(i).size();j++){
//				tmp.add(location_ids.get(i).get(j));
//			}
//			loc_ids.add(tmp);
//		}
		ArrayList<ArrayList<String>> loc_ids;
		loc_ids = copy_csv(location_ids);
		return loc_ids;
	}
	
	
	public static ArrayList<ArrayList<String>> getRussianNames() {
		ArrayList<ArrayList<String>> rus_names;
		rus_names = copy_csv(russian_names);
		return rus_names;
	}
	
	
	public static ArrayList<ArrayList<String>> getArabianNames() {
		ArrayList<ArrayList<String>> arab_names;
		arab_names = copy_csv(arabian_names);
		return arab_names;
	}
	
	
	public static ArrayList<ArrayList<String>> getRussianQuiz() {
		ArrayList<ArrayList<String>> rus_quiz;
		rus_quiz = copy_csv(russian_quiz);
		return rus_quiz;
	}
	
	
	public static ArrayList<ArrayList<String>> getArabianQuiz() {
		ArrayList<ArrayList<String>> arab_quiz;
		arab_quiz = copy_csv(arabian_quiz);
		return arab_quiz;
	}
	
	
	public static ArrayList<ArrayList<String>> copy_csv(ArrayList<ArrayList<String>> location_ids){
		ArrayList<ArrayList<String>> new_csv = new ArrayList<ArrayList<String>>();
		ArrayList<String>tmp;
		
		for(int i=0;i<location_ids.size();i++){
			tmp = new ArrayList<String>();
			for(int j=0;j<location_ids.get(i).size();j++){
				tmp.add(location_ids.get(i).get(j));
			}
			new_csv.add(tmp);
		}	
		return new_csv;
	}
	
	public void loadLevelFiles(String levelname){
		LEVELNAME = levelname;
	}
	
}
