package com.stalkindustries.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import com.stalkindustries.main.game.Control;

public class Button extends JButton implements ActionListener{
	
	private String ok_code;
	private Control controlunit;
	
	public Button(Control controlunit, BufferedImage normal,BufferedImage hover, BufferedImage clicked, String funktionsname, int x, int y){
		this.ok_code = funktionsname;
		addActionListener(this);
		
		setIcon(new javax.swing.ImageIcon(normal));
		setRolloverIcon(new javax.swing.ImageIcon(hover));
        setPressedIcon(new javax.swing.ImageIcon(clicked));
        
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setPreferredSize(new Dimension(normal.getWidth(),normal.getHeight()));
        
        setBounds(x, y, normal.getWidth(),normal.getHeight());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controlunit.call(ok_code);
		
	}
	
}
