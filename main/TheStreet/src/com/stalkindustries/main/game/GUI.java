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
	
	
	//Test
	private Erwachsene person[] = new Erwachsene[9];
	//Test ende
	
	
	//Support: Tobi
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
	
	//Support: Tobi
	public void paint(Graphics g){
		//TP Double Frame Buffering to remove flickering
		BufferedImage bufferedImage = new BufferedImage((int)screen.getWidth(), (int)screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0,(int)screen.getWidth(), (int)screen.getHeight());
		g2d.drawImage(karte.getImage(),(int)zeropos.getWidth(), (int)zeropos.getHeight(), karte.getWidth(), karte.getHeight(), Color.black,null);
		
		
		//Test
		for(int i=0;i<9;i++){
			person[i]=new Erwachsene();
		}
		g2d.drawImage(person[0].paint(),zeropos.width+0, zeropos.height+270, null);
		g2d.drawImage(person[1].paint(),zeropos.width+90, zeropos.height+270, null);
		g2d.drawImage(person[2].paint(),zeropos.width+180, zeropos.height+270, null);
		g2d.drawImage(person[3].paint(),zeropos.width+270, zeropos.height+270, null);
		g2d.drawImage(person[4].paint(),zeropos.width+360, zeropos.height+270, null);
		g2d.drawImage(person[5].paint(),zeropos.width+450, zeropos.height+270, null);
		g2d.drawImage(person[6].paint(),zeropos.width+540, zeropos.height+270, null);
		g2d.drawImage(person[7].paint(),zeropos.width+630, zeropos.height+270, null);
		g2d.drawImage(person[8].paint(),zeropos.width+720, zeropos.height+270, null);
	    //TEst ende
		
		Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.drawImage(bufferedImage, null, 0, 0); 
	  }
}
