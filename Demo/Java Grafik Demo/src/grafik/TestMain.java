package grafik;

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
		karte = new Map("src\\grafik\\data\\demo_map.png");
		fenster=new Window(this);
		//TP Fenster update Thread
		painter = new PaintJob(fenster);
		painter.start();
		//TP optimierung  des Speicherverbrauchs erforderlich
		entitys.add(new Person(100,100,"src\\grafik\\data\\demo_person.png"));
		entitys.get(0).start();
	}
}
