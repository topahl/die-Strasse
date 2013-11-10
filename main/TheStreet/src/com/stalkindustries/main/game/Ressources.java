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
	private static ArrayList<ArrayList<String>> names;
	private static ArrayList<ArrayList<String>> quizfragen;
	private static ArrayList<ArrayList<String>> livetickergags;
	private static ArrayList<ArrayList<String>> evilevents;
	private static ArrayList<ArrayList<String>> normalevents;
	
	public static final int RASTERHEIGHT = 45; //Map Raster
	public static final int TORSOCNT =4; //Anzahl verschiedener Torsos in Sprite Grafik
	public static final int GAMESPEED = 40; // Zeitspanne in der eine Berechnung durchgefürt wird
	public static final Dimension SCREEN;
	public static final Dimension ZEROPOS;
	public static final int MAPHEIGHT = 720;
	public static final int MAPWIDTH = 1125;
	public static BufferedImage menubars;
	public static BufferedImage zahlen;
	public static BufferedImage ingamebutton;
	public static BufferedImage mainMenu;
	public static BufferedImage mainMenuSub;
	public static BufferedImage menuButton;
	public static BufferedImage ingameFrame;
	public static BufferedImage tutorialBg;
	public static BufferedImage[] tutorial;
	
	public static final String HOMEDIR = defaultDirectory();
	public static int NUMBERHOUSES;
	public static final int NUMBERBESCHWICHTIGENACTIONS = 4; // Zahl ist grad nur Dummywert
	public static int AUSGEWAEHLTESLAND = 1; //TODO dynamisch ausgewähltes Land reinschreiben
	
	 
	public static void loadLevelInfomration(String levelname){
		location_ids=readFromCsv(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_map.csv");
		names = readFromCsv(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_namen.csv");
		quizfragen = readFromCsv(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_quizfragen.csv");
		
		livetickergags = readFromCsv(Ressources.HOMEDIR+"res\\game\\"+"livetickergags.csv");
		//livetickergags = randomizeGags();
		livetickergags = randomizeLists(copyCsv(livetickergags));
		
		evilevents = readFromCsv(Ressources.HOMEDIR+"res\\game\\"+"EvilEvents.csv");
		
		normalevents = readFromCsv(Ressources.HOMEDIR+"res\\game\\"+"NormaleEvents.csv");
		normalevents = randomizeLists(copyCsv(normalevents));
		
		setNumberOfHouses();
		
	}
	
	
	static{
		//Initialisierung der location_ids
		
//		russian_names = read_from_csv("src\\com\\stalkindustries\\data\\russland_namen.csv");
//		arabian_names = read_from_csv("src\\com\\stalkindustries\\data\\saudiarabien_quizfragen.csv");
//		russian_quiz = read_from_csv("src\\com\\stalkindustries\\data\\russland_quizfragen.csv");
//		arabian_quiz = read_from_csv("src\\com\\stalkindustries\\data\\saudiarabien_quizfragen.csv");
		
		SCREEN=Toolkit.getDefaultToolkit().getScreenSize();
		ZEROPOS = new Dimension();	//zeropos berechnen -> Koordinatenverschiebung
		ZEROPOS.setSize((SCREEN.getWidth()/2)-(MAPWIDTH/2),(SCREEN.getHeight()/2)-(MAPHEIGHT/2));
		
		try {
			menubars = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\gui_ingame_bars.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_ingame_bars.png");
			e.printStackTrace();
		}
		
		try {
			zahlen = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\slice_digits.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_digits.png");  
			e.printStackTrace();
		}
		
		try {
			ingamebutton = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\slice_buttons_ingame.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_buttons_ingame.png");
			e.printStackTrace();
		}
		
		try {
			mainMenu = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\gui_menu_main.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_menu_main.png");
			e.printStackTrace();
		}
		
		try {
			menuButton = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\slice_buttons_menu.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_buttons_menu.png");
			e.printStackTrace();
		}
		
		try {
			ingameFrame = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\slice_fenster.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image slice_fenster.png");
			e.printStackTrace();
		}
		
		try {
			mainMenuSub = ImageIO.read(new File(Ressources.HOMEDIR+"res\\game\\gui_menu_sub.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image gui_menu_sub.png");
			e.printStackTrace();
		}
		
		
		try {
			tutorialBg = ImageIO.read(new File(Ressources.HOMEDIR+"res\\tutorial\\tutorial_bg.png"));
		} catch (IOException e) {
			System.err.println("Could not find Image tutorial_bg.png");
			e.printStackTrace();
		}
		
		tutorial = new BufferedImage[5];
		for(int i = 1; i < tutorial.length; i++) {
			try {
				tutorial[i] = ImageIO.read(new File(Ressources.HOMEDIR+"res\\tutorial\\tutorial_overlay" + i + ".png"));
			} catch (IOException e) {
				System.err.println("Could not find Image tutorial_overlay" + i + ".png");
				e.printStackTrace();
			}
		}
	}
	
	
	//Beschwerden an Sven und Miri
		public static ArrayList<ArrayList<String>> readFromCsv(String dateiName){
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
		
		
	private static void setNumberOfHouses(){
		int houses=0;
		
		for(int i=0;i<location_ids.size();i++){
			for(int j=0;j<location_ids.get(i).size();j++){
				//maximum der Zahlen aus location_ids bestimmen und kontrollieren, dass keine Buchstaben genommen werden
				if((int)location_ids.get(i).get(j).charAt(0)-48 > houses && (int)location_ids.get(i).get(j).charAt(0)<58){
					houses = (int)location_ids.get(i).get(j).charAt(0)-48;
				}
			}
		}
		
		NUMBERHOUSES = houses;
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
		loc_ids = copyCsv(location_ids);
		return loc_ids;
	}
	
	
	public static ArrayList<ArrayList<String>> getNames() {
		ArrayList<ArrayList<String>> namen;
		namen = copyCsv(names);
		return namen;
	}
	
	
	public static ArrayList<ArrayList<String>> getQuiz() {
		ArrayList<ArrayList<String>> quiz;
		quiz = copyCsv(quizfragen);
		return quiz;
	}
	
	public static ArrayList<ArrayList<String>> getEvilEvents() {
		ArrayList<ArrayList<String>> evil;
		evil = copyCsv(evilevents);
		return evil;
	}
	
	public static ArrayList<ArrayList<String>> getNormalEvents() {
		ArrayList<ArrayList<String>> normal;
		normal = copyCsv(normalevents);
		return normal;
	}	
	
	public static ArrayList<ArrayList<String>> copyCsv(ArrayList<ArrayList<String>> input){
		ArrayList<ArrayList<String>> new_csv = new ArrayList<ArrayList<String>>();
		
		for(int i=0;i<input.size();i++){
			ArrayList<String> tmp = new ArrayList<String>();
			for(int j=0;j<input.get(i).size();j++){
				tmp.add(input.get(i).get(j));
			}
			new_csv.add(tmp);
		}	
		return new_csv;
	}
	
	//TODO: entfernen
	public static ArrayList<ArrayList<String>> randomizeGags(){
		ArrayList<ArrayList<String>> gags = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> gags_input = copyCsv(livetickergags);
		ArrayList<String> tmp;
		int start = (int)(Math.random()*livetickergags.size());
		int step = (int)(Math.random()*(livetickergags.size()-2)+1);
		
		for(int i=0;i<gags_input.size();i++){
			tmp = new ArrayList<String>();
			tmp.add(gags_input.get(start).get(0));
			gags_input.remove(start);
			start = (start+step)%gags_input.size();
			gags.add(tmp);
		}
		
		return gags;
	}
	
	public static ArrayList<ArrayList<String>> randomizeLists(ArrayList<ArrayList<String>> input){
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		ArrayList<String> tmp;
		int listsize = input.size();
		int start = (int)(Math.random()*listsize);
		int step = (int)(Math.random()*(listsize-2)+1);
		
		for(int i=0;i<listsize;i++){
			//tmp = new ArrayList<String>();
			//tmp.add(input.get(start).get(0));
			tmp = input.get(start);
			input.remove(start);
			if(input.size() != 0)
				start = (start+step)%input.size();
			output.add(tmp);
		}
		
		return output;
	}
	
	
	public static ArrayList<ArrayList<String>> getLiveTickerGags(){
		return livetickergags;
	}
	
	private static String defaultDirectory()
	{
		if(System.getenv("DevelopmentEnvironment")== null){
		    String OS = System.getProperty("os.name").toUpperCase();
		    if (OS.contains("WIN"))
		        return System.getenv("APPDATA")+"\\The Street\\";
		    else if (OS.contains("MAC"))
		        return System.getProperty("user.home") + "/Library/Application/The Street/ "
		                + "Support";
		    else if (OS.contains("NUX"))
		        return System.getProperty("user.home")+"\\The Street\\";
		    return System.getProperty("user.dir")+"\\The Street\\";
		}
		return "";
	}
	
}
