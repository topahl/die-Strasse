package com.stalkindustries.main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.stalkindustries.main.game.Control;

public class Button extends JButton implements ActionListener{
	
	private String ok_code;
	private IControl controlunit;
	
	
	//invisible Button
	public Button(IControl controlunit, int width, int height,String funktionsname, int x, int y, MouseMotionListener ml){
		addMouseMotionListener(ml);
		this.ok_code = funktionsname;
		addActionListener(this);
		setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setPreferredSize(new Dimension(width,height));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setBounds(x, y, width,height);
        this.controlunit=controlunit;
	}
	
	//visible Button
	public Button(IControl controlunit, BufferedImage normal,BufferedImage hover, BufferedImage clicked,BufferedImage disabled, String funktionsname, int x, int y,MouseMotionListener ml){
		addMouseMotionListener(ml);
		this.ok_code = funktionsname;
		addActionListener(this);
		setIcon(new ImageIcon(normal));
		setRolloverIcon(new ImageIcon(hover));
        setPressedIcon(new ImageIcon(clicked));
        if(disabled != null) // es ist möglich kein disabled icon zu setzen
        	setDisabledIcon(new ImageIcon(disabled));
        
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setPreferredSize(new Dimension(normal.getWidth(),normal.getHeight()));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setBounds(x, y, normal.getWidth(),normal.getHeight());
        this.controlunit=controlunit;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controlunit.call(ok_code);
		
	}
	
}
