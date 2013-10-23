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
	private char location_id;
	protected char currentMove = 'n'; //links, rechts, oben , unten, nichts
	private Stack<Character> moves = new Stack<Character>();
	protected static BufferedImage adults; //slice PNG to save RAM
	protected static BufferedImage infants; //slice PNG to save RAM
	
	static{
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
		
		if(posX%45==0&&posY%45==0){
			if(!moves.empty()){
				currentMove=moves.pop();
			}
			else{
				currentMove = 'n';
			}
		}
		switch(currentMove){
		case 'l':
			posX--;
			break;
		case 'r':
			posX++;
			break;
		case 'o':
			posY++;
			break;
		case 'u':
			posX--;
			break;
		}

		switch(currentMove){
		case 'l':
			setIcon(new ImageIcon(sprite.getSubimage(45, 45, 45, 45)));
			break;
		case 'r':
			setIcon(new ImageIcon(sprite.getSubimage(0, 45, 45, 45)));
			break;
		case 'o':
			setIcon(new ImageIcon(sprite.getSubimage(45, 0, 45, 45)));
			break;
		case 'u':
		default:
			setIcon(new ImageIcon(sprite.getSubimage(0, 0, 45, 45)));
			break;
		
		}
		
		
		
		
		setLocation(posX,posY); //no repaint needed
		
		
	}
	
	
	//Support: Tobi
	public int getPosX() {
		return posX;
	}
	//Support: Tobi
	public int getPosY() {
		return posY;
	}
	
	
	public void set_location_id(char id){
		this.location_id = id;
	}
	
	public char get_location_id(){
		return this.location_id;
	}
}
