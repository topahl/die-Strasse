package grafik;

public class PaintJob extends Thread{
	private MyGraphics fenster;

	public PaintJob(MyGraphics fenster){
		this.fenster = fenster;
	}
	public void run(){
		while(true){
			fenster.repaint();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
