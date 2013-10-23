package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.util.Stack;

public abstract class Mensch {
	protected BufferedImage sprite;
	private int posX;
	private int posY;
	private char location_id;
	protected char currentMove = 'n'; //links, rechts, oben , unten, nichts
	private Stack<Character> moves = new Stack<Character>();
	
	
	abstract public BufferedImage paint();
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
		
		
	}
	
	
	public void setPosX(int x){
		this.posX = x;
	}
	
	public void setPosY(int y){
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
	//Support: Tobi
	protected void teleport(int x, int y){
		posX=x;
		posY=y;
	}
	
	
	public void set_location_id(char id){
		this.location_id = id;
	}
	
	public char get_location_id(){
		return this.location_id;
	}
}
