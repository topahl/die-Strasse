package com.stalkindustries.main.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.Scrollbar;
import com.stalkindustries.main.game.Ressources;

public class Menu extends JFrame implements MouseMotionListener {
	private JLayeredPane window;
	private JLayeredPane mainmenu;
	private JLayeredPane mapselect;
	private JLayeredPane profilselect;
	private JLayeredPane tutorial;
	private JLayeredPane highscore;
	private JLayeredPane credits;
	private JLayeredPane pershighscore;
	private ControlMenu control;
	
	private JTextField username;
	private JList benutzerliste; 
	private JLabel currentUser;
	private HashMap<String,BufferedImage> levelicons = new HashMap<String,BufferedImage>();
	
	public static final int LAYERMENU = 1;
	public static final int LAYERLEVEL = 2;
	public static final int LAYERPROFIL = 3;
	public static final int LAYERHIGHSCORE = 4;
	public static final int LAYERTUTORIAL = 5;
	public static final int LAYERCREDITS = 6;
	public static final int LAYERPERSHIGHSCORE = 7;
	
	private HashMap<String,Button> buttons = new HashMap<String,Button>();
	private String[][] tutorialText = new String[10][2];
	private JLabel tutorialTitel = new JLabel();
	private JTextArea tutorialBeschreibung = new JTextArea();
	private JLabel tutorialOverlay = new JLabel();
	private JLabel[] gameDetails = new JLabel[14];
	private JList persHighscoreList;
	private int tutorialPage = 0;
	
	
	
	/**
	 * Konstruktor des Menus ohne Spielername
	 * @author Tobias
	 */
	public Menu() {
		control = new ControlMenu(this);
        initComponents();
        setVisible(true);
    }
	
	
	
	/**
	 * Konstruktor des Menus mit Spielername
	 * @param playername Angemeldeter Benutzer
	 * @author Tobias
	 */
	public Menu(String playername) {
		this(); //Call main constructor
		this.setCurrentUser(playername);
		this.showLayer(LAYERMENU);
	}
	

	
	/**
	 * Setzt aktuell angemeldeten Benutzer neu
	 * @param user benutzername
	 */
	public void setCurrentUser(String user) {
		currentUser.setText(user);
	}

	
	
	//TODO was zum Geier macht diese Methode?
	/**
	 * Setzt das JLabel mit angemeldetem Benutzer neu
	 * @param currentUser Label mit angemeldetem Benutzer
	 */
	public void setCurrentUser(JLabel currentUser) {
		this.currentUser = currentUser;
	}

	
	
	/**
	 * Init Components (auf jeder Menüseite)
	 * @author Tobias
	 */
	private void initComponents() {
		window = new JLayeredPane();
		this.addMouseMotionListener(this);
		
		 JLabel label = new JLabel();
	     label.setText("Angemeldet als");
	     label.setFont(new Font("Corbel",Font.BOLD,20));
	     label.setForeground(new Color(0x1f, 0x1f, 0x1f));
	     label.setHorizontalAlignment(SwingConstants.RIGHT);
	     label.setBounds(Ressources.ZEROPOS.width+855,Ressources.ZEROPOS.height+10,200,45);
	     window.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);

	     currentUser = new JLabel();
	     currentUser.setText("");
	     currentUser.setFont(new Font("Corbel",Font.BOLD,30));
	     currentUser.setForeground(new Color(0x1f, 0x1f, 0x1f));
	     currentUser.setHorizontalAlignment(SwingConstants.RIGHT);
	     currentUser.setBounds(Ressources.ZEROPOS.width+855,Ressources.ZEROPOS.height+35,200,45);
	     window.add(currentUser, javax.swing.JLayeredPane.DEFAULT_LAYER);
	     
	     initLevelIcons();
		 setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
         setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
         setResizable(false);
         GroupLayout layout = new GroupLayout(getContentPane());
         getContentPane().setLayout(layout);
        
       
         //Alle Menüseiten initialisieren
         initProfilMenu();
         initHighscore();
         initPersHighscore();
         initLevelSelect();
         initTutorial();
         initCredits();
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
        
		
		//call Login screen
        control.openProfil();
        

        
        pack();
    }
	
	
	
	/**
	 * Init Tutorial
	 * @author Stephan
	 */
	private void initTutorial() {
		tutorial = new JLayeredPane();
		
		//Texte auf den Tutorialseiten
		tutorialText[0][0] = "Willkommen in The Street!";
		tutorialText[0][1] = "Sie sind ein Agent in geheimer Mission. Überwachen Sie Ihre Nachbarschaft und stellen Sie so den Schwerverbrecher, aber seien Sie achtsam, damit die Bewohner nicht zu misstrauisch werden und sie deshalb auffliegen...";
		tutorialText[1][0] = "Aufbau des Spielfensters";
		tutorialText[1][1] = "Im Spielfenster sehen Sie Ihre Nachbarschaft aus der Vogelperspektive. Oben sehen Sie die Liveticker-Leiste, den Pause- und Beenden-Button. Unten ist die Menüleiste, über die Sie das Spiel steuern.";
		tutorialText[2][0] = "Die Straße";
		tutorialText[2][1] = "Zu Beginn sehen Sie die Bewohner der Straße in ihren Häusern. Ihr eigenes Haus ist für Sie mit dem Bundesadler gekennzeichnet. Die Verteilung der Bewohner und Häuser erfolgt pro Spiel zufällig.";
		tutorialText[3][0] = "Tagesablauf";
		tutorialText[3][1] = "Die Bewohner folgen ihren jeweiligen Tagesabläufen: morgens verlassen Kinder die Straße zur Schule und Erwachsene gehen zur Arbeit, oder sie gehen Einkaufen oder in den Park. Die aktuelle Uhrzeit wird Ihnen in der Menüleiste angezeigt.";
		tutorialText[4][0] = "Aktionen";
		tutorialText[4][1] = "Über die Aktionsbuttons können Sie als Agent spionieren oder sozial interagieren. Ihre Aktionen wirken sich positiv oder negativ auf den Überwachungswert und das Misstrauen in der Nachbarschaft aus.";
		//tutorialText[5][0] = "";
		//tutorialText[5][1] = "";

		tutorialTitel.setText(tutorialText[0][0]);
		tutorialTitel.setFont(new Font("Corbel", Font.BOLD, 24));
		tutorialTitel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		tutorialTitel.setBounds(2*Ressources.RASTERHEIGHT, 12*Ressources.RASTERHEIGHT+5, 14*Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT-5);
        tutorial.add(tutorialTitel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        tutorialBeschreibung.setLineWrap(true);
        tutorialBeschreibung.setText(tutorialText[0][1]);
        tutorialBeschreibung.setWrapStyleWord(true);
        tutorialBeschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tutorialBeschreibung.setFocusable(false);
        tutorialBeschreibung.setOpaque(false);
        tutorialBeschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        tutorialBeschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        tutorialBeschreibung.setBounds(2*Ressources.RASTERHEIGHT, 13*Ressources.RASTERHEIGHT, 14*Ressources.RASTERHEIGHT, 3*Ressources.RASTERHEIGHT);
        tutorial.add(tutorialBeschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);

        //Zurück-Button
        Button button = new Button(this.control,
				Ressources.menuButton.getSubimage(795   , 0, 30, 30),
				Ressources.menuButton.getSubimage(795+30, 0, 30, 30),
				Ressources.menuButton.getSubimage(795+60, 0, 30, 30),
				Ressources.menuButton.getSubimage(795+90, 0, 30, 30),
				"tutorialBack", Ressources.RASTERHEIGHT + 7, 7 * Ressources.RASTERHEIGHT + 7, this);
	    button.setEnabled(false);
	    tutorial.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
	    buttons.put("tutorialBack", button);

	    //Vor-Button
	    button = new Button(this.control,
				Ressources.menuButton.getSubimage(795   , 30, 30, 30),
				Ressources.menuButton.getSubimage(795+30, 30, 30, 30),
				Ressources.menuButton.getSubimage(795+60, 30, 30, 30),
				Ressources.menuButton.getSubimage(795+90, 30, 30, 30),
				"tutorialNext", 16 * Ressources.RASTERHEIGHT + 7, 7 * Ressources.RASTERHEIGHT + 7, this);
	    tutorial.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
	    buttons.put("tutorialNext", button);

	    //aktueller Overlay der Tutorial Seite
        tutorialOverlay.setIcon(tutorialGetCurrentOverlay());
        tutorialOverlay.setBounds(2*Ressources.RASTERHEIGHT, 3*Ressources.RASTERHEIGHT, 14*Ressources.RASTERHEIGHT, 9*Ressources.RASTERHEIGHT);
        tutorial.add(tutorialOverlay, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Hintergrundbild des Tutorials
		JLabel bg = new JLabel();
        bg.setIcon(new ImageIcon(Ressources.tutorialBg));
        bg.setBounds(2*Ressources.RASTERHEIGHT-3, 3*Ressources.RASTERHEIGHT-3, 14*Ressources.RASTERHEIGHT+6, 9*Ressources.RASTERHEIGHT+6);
        tutorial.add(bg, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
		generateStandardSubPageElements(tutorial, "Tutorial", "Lernen Sie das Spielprinzip und die Funktionen von The Street kennen.");
	}


	
	/**
	 * Gibt aktuelle OverlayGrafik als Icon zurück
	 * @author Stephan
	 */
	private ImageIcon tutorialGetCurrentOverlay() {
		if(tutorialPage == 0)
			return null;
		if(tutorialPage < Ressources.tutorial.length)
			return new ImageIcon(Ressources.tutorial[tutorialPage]);
		return null;
	}
	
	
	
	/**
	 * Im Tutorial eine Seite zurück gehen
	 * @author Stephan
	 */
	public void tutorialBack() {
		//nur Blättern wenn nicht erste Seite
		if(tutorialPage != 0) {
			tutorialPage--;
			tutorialOverlay.setIcon(tutorialGetCurrentOverlay());
			tutorialTitel.setText(tutorialText[tutorialPage][0]);
			tutorialBeschreibung.setText(tutorialText[tutorialPage][1]);
			
			//Wenn nötig, Buttons enablen/disablen
			if(tutorialPage == 0)
				this.getButtonsMap().get("tutorialBack").setEnabled(false);
			if(tutorialPage == tutorialText.length / 2 - 2)
				this.getButtonsMap().get("tutorialNext").setEnabled(true);
		}
	}
	
	
	
	/**
	 * Im Tutorial eine Seite vor gehen
	 * @author Stephan
	 */
	public void tutorialNext() {
		//nur Blättern wenn nicht letzte Seite
		if(tutorialPage < tutorialText.length / 2 - 1) {
			tutorialPage++;
			tutorialOverlay.setIcon(tutorialGetCurrentOverlay());
			tutorialTitel.setText(tutorialText[tutorialPage][0]);
			tutorialBeschreibung.setText(tutorialText[tutorialPage][1]);
			
			//Wenn nötig, Buttons enablen/disablen
			if(tutorialPage == 1)
				this.getButtonsMap().get("tutorialBack").setEnabled(true);
			if(tutorialPage == tutorialText.length / 2 - 1)
				this.getButtonsMap().get("tutorialNext").setEnabled(false);
		}
	}

	
	
	/**
	 * Init Credits
	 * @author Stephan
	 */
	private void initCredits() {
		credits = new JLayeredPane();
		
		JLabel label = new JLabel();
		label.setText("Team");
        label.setFont(new Font("Corbel",Font.BOLD,32));
        label.setForeground(new Color(0xf9, 0xf9, 0xf9));
        label.setBounds(Ressources.RASTERHEIGHT, 150, 250, 50);
        credits.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        JTextArea beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Teamleitung\n\nProjektmanagement\n\nSimulation &\nAlgorithmus\n\nFrontend & GUI\n\n\nStoryline\n\nDesign");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(Ressources.RASTERHEIGHT, 200, Ressources.RASTERHEIGHT*5, 9*Ressources.RASTERHEIGHT);
        credits.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);

        beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Tobias Pahlings\n\nSven Wessiepe\n\nMiriam Marx\nMartika Schwan\n\nTobias Pahlings\nStephan Giesau\n\nCarolina Maetzing\n\nStephan Giesau\n");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(Ressources.RASTERHEIGHT*6, 200, Ressources.RASTERHEIGHT*4, 9*Ressources.RASTERHEIGHT);
        credits.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);

        label = new JLabel();
		label.setText("Projekt");
        label.setFont(new Font("Corbel",Font.BOLD,32));
        label.setForeground(new Color(0xf9, 0xf9, 0xf9));
        label.setBounds(Ressources.RASTERHEIGHT*11, 150, 250, 50);
        credits.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("The Street ist entstanden als Projekt im Modul Software Engineering bei Eckard Kruse an der DHBW Mannheim.\n(TINF12AI-BC, WS 2013/2014)");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(Ressources.RASTERHEIGHT*11, 200, Ressources.RASTERHEIGHT*6, Ressources.RASTERHEIGHT*3);
        credits.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);

        label = new JLabel();
		label.setText("Disclaimer");
        label.setFont(new Font("Corbel",Font.BOLD,32));
        label.setForeground(new Color(0xf9, 0xf9, 0xf9));
        label.setBounds(Ressources.RASTERHEIGHT*11, Ressources.RASTERHEIGHT*8, 250, 50);
        credits.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Die Handlung und alle handelnden Personen sowie deren Namen sind frei erfunden. Jegliche Ähnlichkeiten mit lebenden oder realen Personen wären rein zufällig.");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(Ressources.RASTERHEIGHT*11, Ressources.RASTERHEIGHT*8+50, Ressources.RASTERHEIGHT*6, Ressources.RASTERHEIGHT*4);
        credits.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);

		generateStandardSubPageElements(credits, "Credits", "Informationen zur Entwicklung von The Street und Disclaimer.");
	}
	
	
	
	/**
	 * Init Profil Menu
	 * @author Tobias
	 */
	@SuppressWarnings("rawtypes")
	private void initProfilMenu() {
		profilselect = new JLayeredPane();
		
		this.username = new JTextField();
		this.username.setBounds(460, 335, 300, 66);
		this.username.setFont(new Font("Corbel",Font.BOLD,30));
		this.username.setForeground(new Color(0xf9, 0xf9, 0xf9));
		this.username.setOpaque(false);
		this.username.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		this.username.setCaretColor(new Color(0xf9, 0xf9, 0xf9));
		this.username.setBorder(BorderFactory.createEmptyBorder());
		profilselect.add(this.username, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(Ressources.menuButton.getSubimage(360, 0, 315, 66)));
		label.setBounds(450, 330, 315, 66);
		profilselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		Button button = new Button(this.control,
				Ressources.menuButton.getSubimage(0,603, 315,70),
				Ressources.menuButton.getSubimage(315, 603, 313,70),
				Ressources.menuButton.getSubimage(2 * 315, 603, 315,70),
				Ressources.menuButton.getSubimage(3 * 315, 603, 315,70),
				"create", 450, 400, this);
	    profilselect.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			
		label = new JLabel();
		label.setText("Benutzer anlegen");
        label.setFont(new Font("Corbel",Font.BOLD,32));
        label.setForeground(new Color(0xf9, 0xf9, 0xf9));
        label.setBounds(450, 150, 250, 50);
        profilselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        JTextArea beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Sie haben noch keinen Nutzernamen? Legen Sie hier einen neuen an. \n \n \nGewünschter Nutzername");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(450,200, 350, 150);
        profilselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Benutzerauswahl
		benutzerliste = new JList();
        benutzerliste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        benutzerliste.setOpaque(false);
        benutzerliste.setBackground(new Color(0,0,0,0));
        benutzerliste.setFont(new Font("Corbel",Font.BOLD,20));
        benutzerliste.setForeground(new Color(0xf9, 0xf9, 0xf9));
        JScrollPane scrollpane = new Scrollbar(this.control); 
        scrollpane.setViewportView(benutzerliste);
        scrollpane.getViewport().setOpaque(false);
        scrollpane.setOpaque(false);
        scrollpane.setBackground(new Color(0,0,0,0));
        scrollpane.setBorder(null);
        JScrollBar sb = scrollpane.getVerticalScrollBar();
        sb.setPreferredSize(new Dimension(30,0));
        sb.setBackground(new Color(0,0,0,0));
        scrollpane.setBounds(50, 260, 300, 300);
        profilselect.add(scrollpane, javax.swing.JLayeredPane.DEFAULT_LAYER);
                
        
        label = new JLabel();
		label.setText("Benutzer wählen");
        label.setFont(new Font("Corbel",Font.BOLD,32));
        label.setForeground(new Color(0xf9, 0xf9, 0xf9));
        label.setBounds(45, 150, 250, 50);
        profilselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Sie haben noch keinen Nutzernamen? Legen Sie hier einen neuen an.");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(450,200, 350, 50);
        profilselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
		
		beschreibung = new JTextArea();
        beschreibung.setLineWrap(true);
        beschreibung.setText("Melden Sie sich mit Ihrem Nutzernamen an.");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(45,200, 300, 50);
        profilselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        button = new Button(this.control,
				Ressources.menuButton.getSubimage(0,671, 315,70),
				Ressources.menuButton.getSubimage(315, 671, 313,70),
				Ressources.menuButton.getSubimage(2 * 315, 671, 315,70),
				Ressources.menuButton.getSubimage(3 * 315, 671, 315,70),
				"use", 45, 600, this);
	    profilselect.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		generateStandardSubPageElements(profilselect, "Benutzer", "Wählen Sie hier den Benutzer, als der Sie spielen, oder legen Sie einen neuen Benutzer an.");
        
	}
	
	
	
	/**
	 * Init Main Menu
	 * @author Tobias
	 */
	private void initMainMenu() {
		mainmenu = new javax.swing.JLayeredPane();
		JLabel currentscreentext = new JLabel();
        currentscreentext.setText("Hauptmenü");
        currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
        currentscreentext.setForeground(new Color(0x1f, 0x1f, 0x1f));
        currentscreentext.setBounds(45,29, 200, 37);
        mainmenu.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
        Button button;
        
        button=new Button(control, Ressources.menuButton.getSubimage(0, 136, 370, 80), Ressources.menuButton.getSubimage(370, 136, 370, 80), Ressources.menuButton.getSubimage(740, 136, 370, 80), Ressources.menuButton.getSubimage(1110, 136, 370, 80), "start", 70,140, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("start", button);
        
        button=new Button(control, Ressources.menuButton.getSubimage(0, 136+80, 370, 80), Ressources.menuButton.getSubimage(370, 136+80, 370, 80), Ressources.menuButton.getSubimage(740, 136+80, 370, 80), Ressources.menuButton.getSubimage(1110, 136+80, 370, 80), "tutorial",70, 230, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("tutorial", button);
        
        button=new Button(control, Ressources.menuButton.getSubimage(0, 136+160, 370, 80), Ressources.menuButton.getSubimage(370, 136+160, 370, 80), Ressources.menuButton.getSubimage(740, 136+160, 370, 80), Ressources.menuButton.getSubimage(1110, 136+160, 370, 80), "profil", 70, 320, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("profil", button);
        
        button=new Button(control, Ressources.menuButton.getSubimage(0, 136+240, 370, 80), Ressources.menuButton.getSubimage(370, 136+240, 370, 80), Ressources.menuButton.getSubimage(740, 136+240, 370, 80), Ressources.menuButton.getSubimage(1110, 136+240, 370, 80), "highscore", 70, 410, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("highscore", button);
        
        button=new Button(control, Ressources.menuButton.getSubimage(0, 136+320, 370, 80), Ressources.menuButton.getSubimage(370, 136+320, 370, 80), Ressources.menuButton.getSubimage(740, 136+320, 370, 80), Ressources.menuButton.getSubimage(1110, 136+320, 370, 80), "beenden", 70, 500, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("beenden", button);
        
		button = new Button(this.control,
				Ressources.menuButton.getSubimage(0, 881, 315, 70),
				Ressources.menuButton.getSubimage(315, 881, 313, 70),
				Ressources.menuButton.getSubimage(2 * 315, 881, 315, 70),
				Ressources.menuButton.getSubimage(3 * 315, 881, 315, 70),
				"credits", 17 * Ressources.RASTERHEIGHT, 9 * Ressources.RASTERHEIGHT - 10, this);
	    mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
	    buttons.put("credits", button);
        
        JLabel main = new JLabel();
        main.setIcon(new ImageIcon(Ressources.mainMenu));
        main.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        mainmenu.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        mainmenu.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(mainmenu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
	}
	
	
	
	/**
	 * Init Highscore
	 * @author Tobias
	 */
	@SuppressWarnings("rawtypes")
	private void initHighscore() {
		highscore = new JLayeredPane();
		
		//Bestenliste
		JList list = new JList();
		//list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setOpaque(false);
        list.setBackground(new Color(0,0,0,0));
        list.setForeground(new Color(0xf9, 0xf9, 0xf9));
        list.setFont(new Font("Corbel",Font.BOLD,20));
        HighscoreTableRenderer renderer = new HighscoreTableRenderer();
        list.setCellRenderer(renderer);
        renderer.addIcons(levelicons);
        JScrollPane scrollpane = new Scrollbar(this.control); 
        scrollpane.setViewportView(list);
        scrollpane.getViewport().setOpaque(false);
        scrollpane.setOpaque(false);
        scrollpane.setBackground(new Color(0,0,0,0));
        scrollpane.setBorder(null);
        JScrollBar sb = scrollpane.getVerticalScrollBar();
        sb.setPreferredSize(new Dimension(30,0));
        sb.setBackground(new Color(0,0,0,0));
        scrollpane.setBounds(50, 300, 700, 340);
        highscore.add(scrollpane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
		//load level icons for Highscore List
        ArrayList<String> levels = readAvaidableLevel();
        ArrayList<ArrayList<String>> scores = new ArrayList<ArrayList<String>>();
        for(String levelname : levels){
        	try {
        		File file= new File(Ressources.HOMEDIR+"res\\user\\"+levelname+".bnd");
        		BufferedReader in = new BufferedReader(new FileReader(file));
        		String nextLine;
    			while((nextLine = in.readLine()) != null){					
    				if(!nextLine.equals("")){
    					String[] split = nextLine.split(":");
    					ArrayList<String> score = new ArrayList<String>();
    					score.add(levelname);
    					score.add(split[1]);
    					score.add(split[0]);
    					score.add(split[2]);
    					scores.add(score);
    				}
    			}
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        	
        Collections.sort(scores, new CompareScore());
		DefaultListModel model = new DefaultListModel();
		for(ArrayList<String> score : scores)
			model.addElement(score);
			
		list.setModel(model);

		
		//Button
		Button alleScoresButton = new Button(this.control,
		Ressources.menuButton.getSubimage(0, 740, 315, 70),
		Ressources.menuButton.getSubimage(315, 740, 313, 70),
		Ressources.menuButton.getSubimage(2 * 315, 740, 315, 70),
		Ressources.menuButton.getSubimage(3 * 315, 740, 315, 70),
		"meineScores", Ressources.RASTERHEIGHT, 3 * Ressources.RASTERHEIGHT - 10, this);
		highscore.add(alleScoresButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		buttons.put("meineScores", alleScoresButton);
		
		
		//Label header
		JLabel headerLabel = new JLabel();
		headerLabel.setText("Bestenliste - Top 100");
		headerLabel.setFont(new Font("Corbel",Font.BOLD,30));
		headerLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		headerLabel.setBounds(60, 220, 400, 50);
		highscore.add(headerLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		
		//Label für Tabellenüberschriften
		
		JLabel levelLabel = new JLabel();
		levelLabel.setText("Level");
		levelLabel.setFont(new Font("Corbel",Font.BOLD,22));
		levelLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		levelLabel.setBounds(98, 255, 100, 50);
		highscore.add(levelLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel scoreLabel = new JLabel();
		scoreLabel.setText("Score");
		scoreLabel.setFont(new Font("Corbel",Font.BOLD,22));
		scoreLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		scoreLabel.setBounds(300, 255, 100, 50);
		highscore.add(scoreLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel benutzerLabel = new JLabel();
		benutzerLabel.setText("Benutzer");
		benutzerLabel.setFont(new Font("Corbel",Font.BOLD,22));
		benutzerLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		benutzerLabel.setBounds(365, 255, 100, 50);
		highscore.add(benutzerLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel datumLabel = new JLabel();
		datumLabel.setText("Datum");
		datumLabel.setFont(new Font("Corbel",Font.BOLD,22));
		datumLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		datumLabel.setBounds(570, 255, 100, 50);
		highscore.add(datumLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		generateStandardSubPageElements(highscore, "Highscores", "Sehen Sie hier die Auswertung Ihrer Spiele und vergleichen Sie Ihr Ergebnis mit dem von anderen Spielern.");
	}
	
	/**
	 * @author Tobias
	 */
	private void initPersHighscore(){
		pershighscore = new JLayeredPane();
		
		JLabel label = new JLabel();
		label.setText("Meine Spiele");
		label.setFont(new Font("Corbel",Font.BOLD,30));
		label.setBounds(50, 250, 200, 45);
		label.setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		label = new JLabel();
		label.setText("Spieldetails");
		label.setFont(new Font("Corbel",Font.BOLD,30));
		label.setBounds(400, 250, 200, 45);
		label.setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[0] = new JLabel();  //Land
		gameDetails[0].setIcon(new ImageIcon(levelicons.get("russland")));
		gameDetails[0].setBounds(405, 290, 200, 45);
		pershighscore.add(gameDetails[0], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[1] = new JLabel(); //Uhrzeit Datum
		gameDetails[1].setText("20.09.2013 - 23:41 Uhr");
		gameDetails[1].setFont(new Font("Corbel",Font.BOLD,20));
		
		
		//Eigene Bestenliste
		persHighscoreList = new JList();
		//list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		persHighscoreList.setOpaque(false);
		persHighscoreList.setBackground(new Color(0,0,0,0));
		persHighscoreList.setForeground(new Color(0xf9, 0xf9, 0xf9));
		persHighscoreList.setFont(new Font("Corbel",Font.BOLD,20));
		PersHighscoreTableRenderer renderer = new PersHighscoreTableRenderer();
		persHighscoreList.setCellRenderer(renderer);
		persHighscoreList.addListSelectionListener(control);
		
		JScrollPane scrollpane = new Scrollbar(this.control); 
		scrollpane.setViewportView(persHighscoreList);
		scrollpane.getViewport().setOpaque(false);
		scrollpane.setOpaque(false);
		scrollpane.setBackground(new Color(0,0,0,0));
		scrollpane.setBorder(null);
		JScrollBar sb = scrollpane.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(30,0));
        sb.setBackground(new Color(0,0,0,0));
        scrollpane.setBounds(50, 300, 300, 300);
        pershighscore.add(scrollpane, javax.swing.JLayeredPane.DEFAULT_LAYER);
    	renderer.addIcons(levelicons);
        
		
		generateStandardSubPageElements(pershighscore, "Meine Scores", "");
	}
	
	
	/**
	 * Init Level Select
	 * @author Tobias
	 */
	private void initLevelSelect(){
		mapselect = new JLayeredPane();      
        
        ArrayList<String> levels=readAvaidableLevel();
        for(int i=0;i<levels.size();i++){
        	if(i>3)
        		break;
        	try {
    			BufferedImage loader = ImageIO.read(new File(Ressources.HOMEDIR+"res\\level\\"+levels.get(i)+"\\"+levels.get(i)+"_slice_menu.png"));
    			BufferedImage iconnormal = new BufferedImage(312,134, BufferedImage.TYPE_INT_ARGB);
    			BufferedImage iconhover = new BufferedImage(312,134, BufferedImage.TYPE_INT_ARGB);
    			Graphics2D g2d = iconhover.createGraphics();
    			g2d.drawImage(loader.getSubimage(0, 0, 312, 134), 0, 0, null);
    			g2d.drawImage(Ressources.menuButton.getSubimage(0, 0, 312, 135), 0, 0, null);
    			g2d.dispose();
    			
    			g2d = iconnormal.createGraphics();
    			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    			g2d.drawImage(loader.getSubimage(0, 0, 312, 134), 0, 0, null);
    			g2d.drawImage(Ressources.menuButton.getSubimage(0, 0, 312, 135), 0, 0, null);
    			g2d.dispose();
    			
    			Button button = new Button(control,iconnormal,iconhover,iconnormal,iconnormal,"level:"+levels.get(i), 45+((i/2)*360),180+((i%2)*200), this);
    			mapselect.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
    			buttons.put(levels.get(i), button);
    			
    			JLabel label= new JLabel();
    			BufferedImage textlabel = loader.getSubimage(315, 9, loader.getWidth()-315, 26);
    			g2d = textlabel.createGraphics();
    			g2d.drawImage(Ressources.menuButton.getSubimage(315, 9, 37, 26), 0, 0, null);
    			label.setIcon(new ImageIcon(textlabel));
    			label.setBounds(45+((i/2)*360),145+((i%2)*200), loader.getWidth()-315, 25);
    			mapselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);	
    		} catch (IOException e) {
    			System.err.println("Could not find Image "+levels.get(i)+"_slice_menu.png");
    			e.printStackTrace();
    		}
		}
        generateStandardSubPageElements(mapselect, "Levelauswahl", "Wählen Sie das Land, welches in diesem Spiel simuliert werden soll.");
    }
	
	/**
	 * @author Tobias 
	 */
	private void initLevelIcons(){
		ArrayList<String> levels = readAvaidableLevel();
		for(String levelname : levels){
        	try {
				BufferedImage loader = ImageIO.read(new File(Ressources.HOMEDIR+"res\\level\\"+levelname+"\\"+levelname+"_slice_menu.png"));
				Graphics2D g2d = loader.createGraphics();
    			g2d.drawImage(Ressources.menuButton.getSubimage(315, 9, 37, 26), 315, 9, null);
				this.levelicons.put(levelname, loader.getSubimage(315, 9, loader.getWidth()-315, 26));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
	
	
    /**
     * Blendet den ausgewälten layer ein und alle anderen aus
     * @param layernummer Nummer des layers der angezeigt werden soll
     */
    public void showLayer(int layernummer) {
    	mainmenu.setVisible(layernummer==LAYERMENU?true:false);
    	mainmenu.setEnabled(layernummer==LAYERMENU?true:false);
    	mapselect.setVisible(layernummer==LAYERLEVEL?true:false);
    	mapselect.setEnabled(layernummer==LAYERLEVEL?true:false);
    	profilselect.setVisible(layernummer==LAYERPROFIL?true:false);
    	profilselect.setEnabled(layernummer==LAYERPROFIL?true:false);
    	highscore.setVisible(layernummer==LAYERHIGHSCORE?true:false);
    	highscore.setEnabled(layernummer==LAYERHIGHSCORE?true:false);
    	tutorial.setVisible(layernummer==LAYERTUTORIAL?true:false);
    	tutorial.setEnabled(layernummer==LAYERTUTORIAL?true:false);
    	credits.setVisible(layernummer==LAYERCREDITS?true:false);
    	credits.setEnabled(layernummer==LAYERCREDITS?true:false);
    	pershighscore.setVisible(layernummer==LAYERPERSHIGHSCORE?true:false);
    	pershighscore.setEnabled(layernummer==LAYERPERSHIGHSCORE?true:false);
    }
    
    
    
    /**
     * Überprüft ob alle daten für ein Level vorhanden sind
     * 
     * @param folder ein Ordner welcher ein level beinhalten soll
     * @return true = Level ist ok 
     */
    private boolean isValidLevel(File folder) {
    	int found=0;
    	String foldername=folder.getName().trim();
    	for (File fileEntry : folder.listFiles()) {
    		if(fileEntry.isFile()){
    			String name = fileEntry.getName().trim();
    			if(name.equals(foldername+"_map.csv"))
    				found++;
    			else if(name.equals(foldername+"_map.png"))
    				found++;
    			else if(name.equals(foldername+"_namen.csv"))
    				found++;
    			else if(name.equals(foldername+"_quizfragen.csv"))
    				found++;
    			else if(name.equals(foldername+"_slice_menu.png"))
    				found++;
    			else if(name.equals(foldername+"_slice_person_adult.png"))
    				found++;
    			else if(name.equals(foldername+"_slice_person_child.png"))
    				found++;
    		}
    	}
    	if(found==7) 
    		return true;
    	System.err.println("levelfolder "+foldername+" is not valid!");
    	return false;
    }
    
    
    
    /**
     * 
     * @param subpage Referenz auf JLayeredPane
     * @param titel Titel der Menuseite
     * @param description Beschreibung der Menuseite
     */
    private void generateStandardSubPageElements(JLayeredPane subpage, String titel, String description) {
    	JLabel currentscreentext = new JLabel();
		currentscreentext.setText(titel);
	    currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	    currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
	    currentscreentext.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
	    currentscreentext.setBounds(45,29, 200, 37);
	    subpage.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
	    Button back = new Button(this.control,
				Ressources.ingamebutton.getSubimage(948, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
				Ressources.ingamebutton.getSubimage(948 + Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
				Ressources.ingamebutton.getSubimage(948 + 2 * Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
				Ressources.ingamebutton.getSubimage(948 + 3 * Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
				"back", Ressources.MAPWIDTH-Ressources.RASTERHEIGHT, 0, this);
	    subpage.add(back, javax.swing.JLayeredPane.DEFAULT_LAYER);
	    
	    JTextArea beschreibung = new JTextArea();
        beschreibung.setColumns(20);
        beschreibung.setLineWrap(true);
        beschreibung.setRows(5);
        beschreibung.setText(description);
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(850,270, 250, 200);
        subpage.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
		JLabel main = new JLabel();
		main.setIcon(new ImageIcon(Ressources.mainMenuSub));
        main.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        subpage.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        subpage.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(subpage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
    }
    
    
    
    /**
     * Liest alle verfügbaren Levels ein und überprüft sie auf Richtigkeit
     * 
     * @author Tobias
     * @return Liste aller validen Levelfiles
     */
    private ArrayList<String> readAvaidableLevel(){
    	File folder = new File(Ressources.HOMEDIR+"res\\level");
    	ArrayList<String> levels = new ArrayList<String>();
    	for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() && isValidLevel(fileEntry)) {
                levels.add(fileEntry.getName().trim());
            } 
    	}
    	return levels;
    }
    
    
    public ArrayList<ArrayList<String>> readUserHighscores(String username){
    	ArrayList<String> levels = readAvaidableLevel();
        ArrayList<ArrayList<String>> scores = new ArrayList<ArrayList<String>>();
    	try {
    		File file= new File(Ressources.HOMEDIR+"res\\user\\"+username+".usr");
    		BufferedReader in = new BufferedReader(new FileReader(file));
    		String nextLine;
			while((nextLine = in.readLine()) != null){					
				if(!nextLine.equals("")){
					ArrayList<String> score = new ArrayList<String>();
					String[] split;
					//Datum + Uhrzeit
					split = nextLine.split(" ");
					score.add(split[0]);
					score.add(split[1]);
					
					//Highscore
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim());
					
					//Terrorist
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim().toUpperCase());
					
					//Misstrauen
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim());
					
					//Spielzeit
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim());
					
					//Wissenswert
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim());
					
					//Events
					nextLine = in.readLine();
					split = nextLine.split(":");
					split = split[1].split("von");
					score.add(split[0].trim());
					score.add(split[1].trim());
					
					//Level
					nextLine = in.readLine();
					split = nextLine.split(":");
					score.add(split[1].trim());
					
					
					scores.add(score);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DefaultListModel model = new DefaultListModel();
		
    	for(int i=scores.size();i>0;i--)
    		model.addElement(scores.get(i-1));
    	
    	persHighscoreList.setModel(model);
    	return scores;
    }
    
    /**
     * Returns the current input username and resets the field.
     * 
     * @author Tobias
     * @return Username input String
     */
    public String popInputUsername() {
    	String input = username.getText();
    	username.setText("");
    	return input;
    	
    }


    
	/**
	 * Gibt die HashMap der Buttons zurück
	 * @return Hashmap der Menu-Buttons
	 * @author Stephan
	 */
	public HashMap<String, Button> getButtonsMap() {
		return this.buttons;
	}
    
    
	public JList getBenutzerliste() {
		return benutzerliste;
	}
	
	public String getCurrentUser() {
		return currentUser.getText();
	}
	
	public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}
