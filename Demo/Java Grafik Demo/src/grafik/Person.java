package grafik;

public class Person extends Thread {

	private int posX,posY, wpX, wpY;
	public Person(int x, int y) {
		this.posX=x;
		this.wpX=x;
		this.posY=y;
		this.wpY=y;
	}
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public int getWpX() {
		return wpX;
	}
	public void setWpX(int wpX) {
		this.wpX = wpX;
	}
	public int getWpY() {
		return wpY;
	}
	public void setWpY(int wpY) {
		this.wpY = wpY;
	}
	public void run(){
		while(true){
			if(posX > wpX){
				posX--;
			} 
			else{
				if(posX < wpX){
					posX++;
				}
				else{
					if(posY >wpY){
						posY--;
					}
					else{
						if (posY<wpY){
							posY++;
						}
					}
				}
			}
			try {
				sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
