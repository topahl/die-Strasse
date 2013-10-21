package com.stalkindustries.main.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

public abstract class Mensch {
	protected BufferedImage sprite;
	private int posX;
	private int posY;
	protected char currentMove = 'n'; //links, rechts, oben , unten, nichts
	private Stack<Character> moves = new Stack<Character>();
	
	
	abstract public BufferedImage paint();
	
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

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
	
	protected void teleport(int x, int y){
		posX=x;
		posY=y;
	}
}
