package com.stalkindustries.main.menu;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.TheStreet;
import com.stalkindustries.main.game.Ressources;

public class Menu extends JFrame{
	private JLayeredPane mainmenu;
	private JLayeredPane mapselect;
	private ControlMenu control;
	//TODO add more screens
	
	private HashMap<String,Button> buttons = new HashMap<String,Button>();
	
	
	public Menu() {
		control = new ControlMenu(this);
        initComponents();
        setVisible(true);

    }

	private void initComponents(){
		mainmenu = new javax.swing.JLayeredPane();
		
		JLabel currentscreentext = new JLabel();
        currentscreentext.setText("Hauptmenü");
        currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
        currentscreentext.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
        currentscreentext.setBounds(Ressources.ZEROPOS.width+45,Ressources.ZEROPOS.height+29, 200, 37);
        mainmenu.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
        Button button;
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 0, 370, 80), Ressources.menubutton.getSubimage(370, 0, 370, 80), Ressources.menubutton.getSubimage(740, 0, 370, 80), Ressources.menubutton.getSubimage(1110, 0, 370, 80), "start", Ressources.ZEROPOS.width+70, Ressources.ZEROPOS.height+140);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("start", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 80, 370, 80), Ressources.menubutton.getSubimage(370, 80, 370, 80), Ressources.menubutton.getSubimage(740, 80, 370, 80), Ressources.menubutton.getSubimage(1110, 80, 370, 80), "tutorial", Ressources.ZEROPOS.width+70, Ressources.ZEROPOS.height+230);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("tutorial", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 160, 370, 80), Ressources.menubutton.getSubimage(370, 160, 370, 80), Ressources.menubutton.getSubimage(740, 160, 370, 80), Ressources.menubutton.getSubimage(1110, 160, 370, 80), "profil", Ressources.ZEROPOS.width+70, Ressources.ZEROPOS.height+320);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("profil", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 240, 370, 80), Ressources.menubutton.getSubimage(370, 240, 370, 80), Ressources.menubutton.getSubimage(740, 240, 370, 80), Ressources.menubutton.getSubimage(1110, 240, 370, 80), "highscore", Ressources.ZEROPOS.width+70, Ressources.ZEROPOS.height+410);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("Highscore", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 320, 370, 80), Ressources.menubutton.getSubimage(370, 320, 370, 80), Ressources.menubutton.getSubimage(740, 320, 370, 80), Ressources.menubutton.getSubimage(1110, 320, 370, 80), "beenden", Ressources.ZEROPOS.width+70, Ressources.ZEROPOS.height+500);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("Beenden", button);
        
        
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        
        JLabel main = new JLabel();
        main.setIcon(new ImageIcon(Ressources.mainmenu));
        main.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        mainmenu.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainmenu, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.width, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainmenu, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.height, Short.MAX_VALUE)
        );
        
        setUndecorated(true);
        setBackground(Color.black);
        
		
        
        
        
        pack();
	}
}
