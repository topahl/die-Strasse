package grafik;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TestMain {
	
	public Map karte;
	private Window fenster;
	public ArrayList<Entity> entitys = new ArrayList<Entity>();
	private PaintJob painter;
	
	public static void main(String[] args) {
		TestMain prog = new TestMain();
		
		prog.run();
	}
	public void run(){
		karte = new Map("src\\grafik\\data\\map_demo_GUI.png");
		fenster=new Window(this);
		//TP Fenster update Thread
		painter = new PaintJob(fenster);
		painter.start();
		//TP optimierung  des Speicherverbrauchs erforderlich
		
		BufferedImage tempsprite = new  BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempsprite.createGraphics();
		g2d.setColor(new Color(1f,0f,0f,0.2f));
		g2d.fillOval(0, 0, 45, 45);
		g2d.fillOval(0, 45, 45, 45);
		g2d.fillOval(45, 0, 45, 45);
		g2d.fillOval(45, 45, 45, 45);
		entitys.add(new Person(200,200,tempsprite));
		entitys.add(new Person(100,100,"src\\grafik\\data\\demo_person.png"));
		entitys.get(0).start();
	}
}
