package com.stalkindustries.main.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OSTimer implements ActionListener {
	
	GUILayer guilayer;
	
	public OSTimer(GUILayer guilayer){
		this.guilayer = guilayer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		this.guilayer.step();
		
	}

}
