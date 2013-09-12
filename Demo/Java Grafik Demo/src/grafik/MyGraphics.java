package grafik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MyGraphics extends JFrame {
	
	static BufferedImage karte;
	static Person persons[] = new Person[2];
	static PaintJob fensterjob;
	//SG hab ich gemacht :) alles ab hier
  public static void main(String[] args) {
	  try {
		
		karte = ImageIO.read(new File("src\\grafik\\data\\Karte.png"));
		persons[0] = new Person(100,100);
		persons[0].start();
		persons[0].setWpX(500);
		persons[0].setWpY(500);
		persons[1] = new Person(500,500);
		persons[1].start();
		persons[1].setWpX(400);
		persons[1].setWpY(900);
		

		
		MyGraphics fenster = new MyGraphics();
		fensterjob= new PaintJob(fenster);
		fensterjob.start();
	
	} catch (IOException e) {
			e.printStackTrace();
		}
    
  }

  public MyGraphics() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Zeichnen mit Java");
        setSize(1000, 700);
        setVisible(true);
  }

  @Override 
  public void paint(Graphics g) {
	
    Insets insets = getInsets();
    int originX = insets.left;
    int originY = insets.top;
    int breite   = getSize().width  - insets.left - insets.right;
    int hoehe   = getSize().height - insets.top  - insets.bottom;
    
   
    g.drawImage(karte, 0, 0, 1000, 700, Color.white,null);
    for(int i=0;i<persons.length;i++){
    	g.setColor(Color.red);
        g.fillOval(persons[i].getPosX(), persons[i].getPosY(), 10, 10);
    } 
  }
  

  
}
