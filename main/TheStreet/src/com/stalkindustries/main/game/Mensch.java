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
	private char location_id;
	protected char currentMove = 'n'; //links, rechts, oben , unten, nichts -> Kleinbuchstaben
	private Stack<Character> moves = new Stack<Character>();
	protected static BufferedImage adults; //slice PNG to save RAM
	protected static BufferedImage infants; //slice PNG to save RAM
	protected int bewegungsgeschwindigkeit;
	protected int haus_id;
	private boolean invisible_lefternside=false;
	
	
	public static void loadImages(String levelname){
		 try {
				adults = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\Russland_adult.png"));
			} catch (IOException e) {
				System.err.println("Could not find adult.png");
				e.printStackTrace();
			}
			 try {
				infants = ImageIO.read(new File("src\\com\\stalkindustries\\grafik\\Russland_child.png"));
			} catch (IOException e) {
				System.err.println("Could not find child.png");
				e.printStackTrace();
			}
		
	}
		

	//Support: Tobi
	public void step(){
		
		if((posX-Ressources.ZEROPOS.width)%45==0&&(posY-Ressources.ZEROPOS.height)%45==0){
			if(!moves.empty()){
				currentMove=moves.pop();
			}
			else{
				currentMove = 'n';
			}
		}
		switch(currentMove){
		case 'l':
			posX-=this.bewegungsgeschwindigkeit;
			invisible_lefternside = true;
			break;
		case 'r':
			posX+=this.bewegungsgeschwindigkeit;
			invisible_lefternside = false;
			break;
		case 'o':
			posY-=this.bewegungsgeschwindigkeit;
			invisible_lefternside = true;
			break;
		case 'u':
			posY+=this.bewegungsgeschwindigkeit;
			invisible_lefternside = false;
			break;
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
		
		
		if(moves.empty() && location_id=='E'){
			if (invisible_lefternside==false){
				this.setVisible(false);
			} else{
				invisible_lefternside=false;
			}
		} else{
			this.setVisible(true);
		}
	}
	
	
	
	void update_location_raster(){
		//TODO
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
	
	
	//Support: Tobi
	public int getPosX() {
		return posX;
	}
	//Support: Tobi
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
	
	public void set_location_id(char id){
		this.location_id = id;
	}
	
	public char get_location_id(){
		return this.location_id;
	}
	
	
	public void setMoves(Stack<Character> moves){
		this.moves = moves;
	}
	
	public char getCurrentMove(){
		return currentMove;
	} 
	
	public int get_haus_id(){
		return this.haus_id;
	}
	
	public void set_haus_id(int haus_id){
		this.haus_id = haus_id;
	}
	
//	public Stack<Character> getMoves(){
//		return this.moves;
//	}
}
