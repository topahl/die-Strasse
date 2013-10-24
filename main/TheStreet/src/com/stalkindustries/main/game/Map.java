package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.stalkindustries.main.TheStreet;

public class Map extends JLabel{
	private int width;
	private int height;
	BufferedImage karte;
	private ArrayList<ArrayList<String>> location_ids;
	
	
	public Map(String name){
		try {
			karte= ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\"+name+"_map.png"));
		} catch (IOException e) {
			System.err.println("Could not find Map file:"+name+".png");
			e.printStackTrace();
		}
		
		//Initialisierung der location_ids
		//TODO richtigen Dateipfad später angeben
		location_ids=TheStreet.read_from_csv("C:/Users/Martika/Desktop/Dropbox/Software Engineering/Grafikdesign/Fertig/russland_map.csv");
		height=karte.getHeight();
		width=karte.getWidth();
		setIcon(new ImageIcon(karte));
		setSize(height,width);
		//TODO Hausnummern zeichnen
	}
	
	
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public BufferedImage getImage(){
		return karte;
	}



	public ArrayList<ArrayList<String>> getLocation_ids() {
		return location_ids;
	}



	public void setLocation_ids(ArrayList<ArrayList<String>> location_ids) {
		this.location_ids = location_ids;
	}
	
}
