package grafik;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TestMain {
	
	public Map karte;
	private Window fenster;
	private Splash fenster1;
	public ArrayList<Entity> entitys = new ArrayList<Entity>();
	private PaintJob painter;
	
	public static void main(String[] args) {
		TestMain prog = new TestMain();
		
		prog.run();
	}
	public void run(){
		karte = new Map("src\\grafik\\data\\map_demo_GUI.png");
		fenster1 = new Splash();
		try {
			for(int i=0; i<1000;i++){
				fenster1.repaint();
				Thread.sleep(30);
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fenster1.dispose();
		fenster=new Window(this);
		//TP Fenster update Thread
		painter = new PaintJob(fenster);
		painter.start();
		//TP optimierung  des Speicherverbrauchs erforderlich
		BufferedImage sprite;
		try {
			sprite = ImageIO.read(new File("src\\grafik\\data\\demo_figur_ohneschein.png"));
		
		BufferedImage tempsprite = new  BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempsprite.createGraphics();
		g2d.setColor(new Color(1f,1f,1f,0.2f));
		g2d.fillOval(2, 2, 41, 41);
		g2d.fillOval(0, 45, 45, 45);
		g2d.fillOval(45, 0, 45, 45);
		g2d.fillOval(45, 45, 45, 45);
		g2d.drawImage(sprite, 0, 0, null);		
		entitys.add(new Person(200,200,tempsprite));
		entitys.add(new Person(100,100,"src\\grafik\\data\\demo_person.png"));
		entitys.get(0).start();
		} catch (IOException e) {
			System.err.println("Could not find sprite in location: "+"src\\grafik\\data\\demo_figur_ohneschein.png");
			e.printStackTrace();
		}
	}
}
