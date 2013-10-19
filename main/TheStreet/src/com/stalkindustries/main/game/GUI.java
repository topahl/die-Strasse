package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Dimension screen;  //Screen resolution
	private Dimension zeropos; //Koordinatenverschiebung auf Bildschirms
	private Map karte;
	

	public GUI(){
		screen = Toolkit.getDefaultToolkit().getScreenSize();	//Screen resolution
		karte = new Map("Russland");										//initialisiere Karte
		
		zeropos = new Dimension();								//zeropos berechnen
		zeropos.setSize((screen.getWidth()/2)-(karte.getWidth()/2),(screen.getHeight()/2)-(karte.getHeight()/2)); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//Close Window in Menu bar possible
        setTitle("The Street");									//Window Title in menu bar
        setUndecorated(true);									//Pseudo Full Screen
        setSize((int)screen.getWidth(),(int)screen.getHeight());
        setBackground(Color.black);
        setVisible(true);
	}	
	public void paint(Graphics g){
		//TP Double Frame Buffering to remove flickering
		BufferedImage bufferedImage = new BufferedImage((int)screen.getWidth(), (int)screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0,(int)screen.getWidth(), (int)screen.getHeight());
		g2d.drawImage(karte.getImage(),(int)zeropos.getWidth(), (int)zeropos.getHeight(), karte.getWidth(), karte.getHeight(), Color.black,null);
	    Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.drawImage(bufferedImage, null, 0, 0); 
	  }
}
