package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Dimension screen;  //Screen resolution
	private Dimension zeropos; //Koordinatenverschiebung auf Bildschirms
	private Map karte;
	

	public GUI(){
		screen = Toolkit.getDefaultToolkit().getScreenSize();	//Screen resolution
		karte = new Map();										//initialisiere Karte
		
		zeropos = new Dimension();								//zeropos berechnen
		zeropos.setSize((screen.getWidth()/2)-(karte.getWidth()/2),(screen.getHeight()/2)-(karte.getWidth()/2)); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//Close Window in Menu bar possible
        setTitle("The Street");									//Window Title in menu bar
        setUndecorated(true);									//Pseudo Full Screen
        setSize((int)screen.getWidth(),(int)screen.getHeight());
        setBackground(Color.black);
        setVisible(true);
	}	
}
