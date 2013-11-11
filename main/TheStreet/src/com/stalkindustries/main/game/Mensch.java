package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Mensch extends JLabel {
	protected BufferedImage sprite;
	private int posX;
	private int posY;
	private int homeposX;
	private int homeposY;
	private String name;
	private char locationId;
	protected char currentMove = 'n'; //links, rechts, oben , unten, nichts -> Kleinbuchstaben
	private Stack<Character> moves = new Stack<Character>();
	protected static BufferedImage adults; //slice PNG to save RAM
	protected static BufferedImage infants; //slice PNG to save RAM
	protected int bewegungsgeschwindigkeit;
	protected int hausId;
	private boolean invisibleLeftOrUp=false; //Wenn eine Person nach links oder oben rausläuft, muss sie noch einen Schritt länger gezeichnet werden
	
	
	public static void loadImages(String levelname){
		 try {
				adults = ImageIO.read(new File(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_slice_person_adult.png"));
			} catch (IOException e) {
				System.err.println("Could not find adult.png");
				e.printStackTrace();
			}
			 try {
				infants = ImageIO.read(new File(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_slice_person_child.png"));
			} catch (IOException e) {
				System.err.println("Could not find child.png");
				e.printStackTrace();
			}
		
	}
		

	/**
	 * @author Tobias & Martika
	 */
	public void step(){
		
		if((posX-Ressources.ZEROPOS.width)%45==0&&(posY-Ressources.ZEROPOS.height)%45==0){
			if(!moves.empty()){
				if (moves.peek()!='s'){
					currentMove=moves.pop();
				}else{
					currentMove = 's';
				}
			}
			else{
				currentMove = 'n';
			}
		}
		switch(currentMove){
		case 'l':
			posX-=this.bewegungsgeschwindigkeit;
			invisibleLeftOrUp = true;
			break;
		case 'r':
			posX+=this.bewegungsgeschwindigkeit;
			invisibleLeftOrUp = false;
			break;
		case 'o':
			posY-=this.bewegungsgeschwindigkeit;
			invisibleLeftOrUp = true;
			break;
		case 'u':
			posY+=this.bewegungsgeschwindigkeit;
			invisibleLeftOrUp = false;
			break;
		default: break;
		}

		switch(currentMove){
		case 'l':
			setIcon(new ImageIcon(sprite.getSubimage(Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT)));
			break;
		case 'r':
			setIcon(new ImageIcon(sprite.getSubimage(Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT)));
			break;
		case 'o':
			setIcon(new ImageIcon(sprite.getSubimage(0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT)));
			break;
		case 'u':
		default:
			setIcon(new ImageIcon(sprite.getSubimage(0, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT)));
			break;
		
		}
		
		
		
		
		setLocation(posX,posY); //no repaint needed
		
		
		if(moves.empty() && locationId=='E'){
			if (invisibleLeftOrUp==false){
				this.setVisible(false);
			} else{
				invisibleLeftOrUp=false;
			}
		} else{
			this.setVisible(true);
		}
	}
	
	
	//TODO entfernen?!
	private void updateLocationRaster(){
		//weißt Bewohnern eine Location-ID zu
		//--> man weiß nun, wo sie sich grob befinden, d.h.
		//wenn sich Person in bestimmtem Haus befindet, dann ist Location-Id = Haus-Id
		//Schule --> Location-ID = Schule-ID
		//Park --> Location-Id = Park-ID
		//einkaufen, arbeiten, ... --> Location-Id = 42
	}
	
	public void teleport(int x, int y){
		this.posX = x;
		this.posY = y;
	}
	
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public int getHomePosX(){
		return this.homeposX;
	}
	
	public int getHomePosY(){
		return this.homeposY;
	}
	
	public void setHomePosX(int x){
		this.homeposX = x;
	}
	
	public void setHomePosY(int y){
		this.homeposY = y;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setLocationId(char id){
		this.locationId = id;
	}
	
	public char getLocationId(){
		return this.locationId;
	}
	
	
	public void setMoves(Stack<Character> moves){
		this.moves = moves;
	}
	
	public char getCurrentMove(){
		return currentMove;
	} 
	
	public int getHausId(){
		return this.hausId;
	}
	
	public void setHausId(int hausId){
		this.hausId = hausId;
	}
	
	public BufferedImage getSprite(){
		return sprite;
	}
	
	public Stack<Character> getMoves(){
		return this.moves;
	}
}
