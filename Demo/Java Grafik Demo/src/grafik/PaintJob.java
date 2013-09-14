package grafik;

public class PaintJob extends Thread{
	private Window fenster;

	public PaintJob(Window fenster){
		this.fenster = fenster;
	}
	public void run(){ 
		long time;
		while(true){
			time = System.currentTimeMillis();
			fenster.repaint();
			while(System.currentTimeMillis()-time<5);
		}
	}

}
