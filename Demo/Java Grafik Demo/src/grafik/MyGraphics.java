package grafik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MyGraphics extends JFrame {
	
	private static final long serialVersionUID = 1L;
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
    g.drawImage(karte, 0, 0, 1000, 700, Color.white,null);
    for(int i=0;i<persons.length;i++){
    	g.setColor(Color.red);
        g.fillOval(persons[i].getPosX(), persons[i].getPosY(), 10, 10);
    } 
  }
  

  
}
