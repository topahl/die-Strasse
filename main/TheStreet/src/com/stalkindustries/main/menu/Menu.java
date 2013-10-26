package com.stalkindustries.main.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.game.Ressources;

public class Menu extends JFrame implements MouseMotionListener{
	private JLayeredPane window;
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
		window = new JLayeredPane();
		this.addMouseMotionListener(this);
		
		       
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //initLevelSelect();
        initMainMenu();
        
        
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(window, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.width, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(window, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.height, Short.MAX_VALUE)
        );
        
        setUndecorated(true);
        setBackground(Color.black);
        
		
        
        
        
        pack();
        
      
    }
	
	private void initMainMenu(){
		mainmenu = new javax.swing.JLayeredPane();
		JLabel currentscreentext = new JLabel();
        currentscreentext.setText("Hauptmenü");
        currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
        currentscreentext.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
        currentscreentext.setBounds(45,29, 200, 37);
        mainmenu.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
        Button button;
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 0, 370, 80), Ressources.menubutton.getSubimage(370, 0, 370, 80), Ressources.menubutton.getSubimage(740, 0, 370, 80), Ressources.menubutton.getSubimage(1110, 0, 370, 80), "start", 70,140, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("start", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 80, 370, 80), Ressources.menubutton.getSubimage(370, 80, 370, 80), Ressources.menubutton.getSubimage(740, 80, 370, 80), Ressources.menubutton.getSubimage(1110, 80, 370, 80), "tutorial",70, 230, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("tutorial", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 160, 370, 80), Ressources.menubutton.getSubimage(370, 160, 370, 80), Ressources.menubutton.getSubimage(740, 160, 370, 80), Ressources.menubutton.getSubimage(1110, 160, 370, 80), "profil", 70, 320, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("profil", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 240, 370, 80), Ressources.menubutton.getSubimage(370, 240, 370, 80), Ressources.menubutton.getSubimage(740, 240, 370, 80), Ressources.menubutton.getSubimage(1110, 240, 370, 80), "highscore", 70, 410, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("Highscore", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 320, 370, 80), Ressources.menubutton.getSubimage(370, 320, 370, 80), Ressources.menubutton.getSubimage(740, 320, 370, 80), Ressources.menubutton.getSubimage(1110, 320, 370, 80), "beenden", 70, 500, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("Beenden", button);
        
        JLabel main = new JLabel();
        main.setIcon(new ImageIcon(Ressources.mainmenu));
        main.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        mainmenu.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        mainmenu.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(mainmenu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
	}
	
	private void initLevelSelect(){
		mapselect = new JLayeredPane();
		JLabel currentscreentext = new JLabel();
        currentscreentext.setText("Levelauswahl");
        currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
        currentscreentext.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
        currentscreentext.setBounds(45,29, 200, 37);
        mapselect.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        JTextArea beschreibung = new JTextArea();
        beschreibung.setColumns(20);
        beschreibung.setLineWrap(true);
        beschreibung.setRows(5);
        beschreibung.setText("Wählen Sie das Land, welches in diesem Spiel simuliert werden soll.");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(850,270, 200, 200);
        mapselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(Ressources.mainmenusub));
        background.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        mapselect.add(background, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        mapselect.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(mapselect, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	
	//Support Tiki
	public void mouseMoved(MouseEvent e) {	
		//mousefollower.setLocation(e.getX()-15, e.getY()-15);
	}
    public void mouseDragged(MouseEvent e) {} //do nothing, notwendig für implements MouseMotion

}
