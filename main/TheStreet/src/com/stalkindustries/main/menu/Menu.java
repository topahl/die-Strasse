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
import javax.swing.Icon;
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
import com.stalkindustries.main.TheStreet;
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
	private String[][] tutorialText = new String[12][2];
	private JLabel tutorialTitel = new JLabel();
	private JTextArea tutorialBeschreibung = new JTextArea();
	private JLabel tutorialOverlay = new JLabel();
	private JLabel[] gameDetails = new JLabel[18];
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
		this.showLayer(LAYERPERSHIGHSCORE);
		this.resetPersHighscore();
		this.readUserHighscores(playername);
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
		tutorialText[0][1] = "Sie sind ein Agent in geheimer Mission. Überwachen Sie Ihre Nachbarschaft und stellen Sie so den Schwerverbrecher, aber seien Sie achtsam, damit die Bewohner nicht zu misstrauisch werden und Sie deshalb auffliegen...";
		tutorialText[1][0] = "Aufbau des Spielfensters";
		tutorialText[1][1] = "Im Spielfenster sehen Sie Ihre Nachbarschaft aus der Vogelperspektive. Oben sehen Sie die Liveticker-Leiste, den Pause- und Beenden-Button. Unten ist die Menüleiste, über die Sie das Spiel steuern.";
		tutorialText[2][0] = "Die Straße";
		tutorialText[2][1] = "Zu Beginn sehen Sie die Bewohner der Straße in ihren Häusern. Ihr eigenes Haus ist für Sie mit dem Bundesadler gekennzeichnet. Die Verteilung der Bewohner und Häuser erfolgt pro Spiel zufällig.";
		tutorialText[3][0] = "Tagesablauf";
		tutorialText[3][1] = "Die Bewohner folgen ihren jeweiligen Tagesabläufen: morgens verlassen Kinder die Straße zur Schule. Erwachsene gehen zur Arbeit, Einkaufen oder in den Park. Die aktuelle Uhrzeit wird Ihnen in der Menüleiste angezeigt.";
		tutorialText[4][0] = "Aktionen";
		tutorialText[4][1] = "Über die Aktionsbuttons können Sie als Agent spionieren oder sozial interagieren. Ihre Aktionen wirken sich positiv oder negativ auf den Überwachungswert und das Misstrauen in der Nachbarschaft aus.";
		tutorialText[5][0] = "Spionageaktionen";
		tutorialText[5][1] = "Installieren Sie Spionageinstrumente in den Häusern der Nachbarschaft und beobachten Sie einige Häuser über Fernglas, um den Überwachungswert zu erhöhen. Lassen Sie sich bei der Installation, ob tagsüber oder nachts, nur nicht erwischen - sonst werden die Bewohner sehr misstrauisch... Überwachungswerkzeuge können im Haus wieder deinstalliert werden.";
		tutorialText[6][0] = "Soziale Aktionen";
		tutorialText[6][1] = "Sie können das Misstrauen der Bewohner wieder senken, indem Sie tagsüber an deren Häusern oder im Park sozial aktiv werden. Das Misstrauen einzelner Bewohner können Sie anhand der roten (negativen) oder grünen (positiven) Färbung des Scheins erkennen.";
		tutorialText[7][0] = "Misstrauenswert";
		tutorialText[7][1] = "Der Misstrauenswert bildet das durchschnittliche Misstrauen der Nachbarschaft ab. Jeder Bewohner hat Beziehungen zu anderen Bewohnern, über die sich das Misstrauen verbreitet und angleicht, wenn sich diese im Park oder in der Schule begegnen.";
		tutorialText[8][0] = "Überwachungswert und Liveticker";
		tutorialText[8][1] = "Wenn Sie ein Haus überwachen, können Sie dessen Bewohner sehen und erkennen, ob die Bewohner arbeiten (Krawatte/Halstuch). Durch entsprechend hohe Überwachung können Sie 'Evil Events' aufdecken, die Ihnen Hinweise auf den Schwerverbrecher geben könnten. Evil Events erscheinen rot im Liveticker - seien Sie also wachsam!";
		tutorialText[9][0] = "Quizfragen";
		tutorialText[9][1] = "Während des Spiels wird Ihr Wissen zum entsprechenden Land abgefragt. Antworten Sie schnell in der vorgegebenen Zeit und präzise, denn eine falsche oder keine Antwort wirkt sich negativ auf den Misstrauenswert der Nachbarschaft aus, wogegen eine richtige Antwort den Misstrauenswert senkt.";
		tutorialText[10][0] = "Spielende";
		tutorialText[10][1] = "Glauben Sie den Schwerverbrecher gefunden zu haben, können Sie sein Haus (bei entsprechendem Überwachungswert) hochnehmen lassen. Haben Sie das falsche Haus gewählt oder wird der Schwerverbrecher aus anderen Gründen zu misstrauisch, flieht er und Sie haben verloren. Fliegen Sie durch zu hohes Misstrauen der Nachbarschaft auf, ist das Spiel auch vorbei.";
		tutorialText[11][0] = "Sie sind bereit!";
		tutorialText[11][1] = "Nun sind Sie mit den Grundlagen des Spieles vertraut und können Ihre nächste Mission starten!";
		
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
        tutorialBeschreibung.setFont(new Font("Corbel",Font.BOLD,18));
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
			if(tutorialPage == tutorialText.length - 2)
				this.getButtonsMap().get("tutorialNext").setEnabled(true);
		}
	}
	
	
	
	/**
	 * Im Tutorial eine Seite vor gehen
	 * @author Stephan
	 */
	public void tutorialNext() {
		//nur Blättern wenn nicht letzte Seite
		if(tutorialPage < tutorialText.length - 1) {
			tutorialPage++;
			tutorialOverlay.setIcon(tutorialGetCurrentOverlay());
			tutorialTitel.setText(tutorialText[tutorialPage][0]);
			tutorialBeschreibung.setText(tutorialText[tutorialPage][1]);
			
			//Wenn nötig, Buttons enablen/disablen
			if(tutorialPage == 1)
				this.getButtonsMap().get("tutorialBack").setEnabled(true);
			if(tutorialPage == tutorialText.length - 1)
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
        scrollpane.setBounds(50, 280, 720, 360);
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
		Button meineScoresButton = new Button(this.control,
		Ressources.menuButton.getSubimage(0, 745, 315, 70),
		Ressources.menuButton.getSubimage(315, 745, 313, 70),
		Ressources.menuButton.getSubimage(2 * 315, 745, 315, 70),
		Ressources.menuButton.getSubimage(3 * 315, 745, 315, 70),
		"meineScores", Ressources.RASTERHEIGHT, 3 * Ressources.RASTERHEIGHT - 10, this);
		highscore.add(meineScoresButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		buttons.put("meineScores", meineScoresButton);
		
		
		//Label header
		JLabel headerLabel = new JLabel();
		headerLabel.setText("Bestenliste - Top 100");
		headerLabel.setFont(new Font("Corbel",Font.BOLD,30));
		headerLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		headerLabel.setBounds(60, 200, 400, 50);
		highscore.add(headerLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		
		//Label für Tabellenüberschriften
		
		JLabel levelLabel = new JLabel();
		levelLabel.setText("Level");
		levelLabel.setFont(new Font("Corbel",Font.BOLD,22));
		levelLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		levelLabel.setBounds(98, 235, 100, 50);
		highscore.add(levelLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel scoreLabel = new JLabel();
		scoreLabel.setText("Score");
		scoreLabel.setFont(new Font("Corbel",Font.BOLD,22));
		scoreLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		scoreLabel.setBounds(300, 235, 100, 50);
		highscore.add(scoreLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel benutzerLabel = new JLabel();
		benutzerLabel.setText("Benutzer");
		benutzerLabel.setFont(new Font("Corbel",Font.BOLD,22));
		benutzerLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		benutzerLabel.setBounds(365, 235, 100, 50);
		highscore.add(benutzerLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		JLabel datumLabel = new JLabel();
		datumLabel.setText("Datum");
		datumLabel.setFont(new Font("Corbel",Font.BOLD,22));
		datumLabel.setForeground(new Color(0xf9, 0xf9, 0xf9));
		datumLabel.setBounds(570, 235, 100, 50);
		highscore.add(datumLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		
		JLabel listbackground = new JLabel();
    	listbackground.setIcon(new ImageIcon(Ressources.menuButton.getSubimage(335, 955, 750, 395)));
    	listbackground.setBounds(33, 263, 750, 395);
    	highscore.add(listbackground, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		generateStandardSubPageElements(highscore, "Highscores", "Sehen Sie hier die Auswertung Ihrer Spiele und vergleichen Sie Ihr Ergebnis mit dem von anderen Spielern.");
	}
	
	/**
	 * @author Tobias
	 */
	private void initPersHighscore(){
		pershighscore = new JLayeredPane();
		
		//Button
		Button alleScoresButton = new Button(this.control,
		Ressources.menuButton.getSubimage(0, 815, 315, 70),
		Ressources.menuButton.getSubimage(315, 815, 313, 70),
		Ressources.menuButton.getSubimage(2 * 315, 815, 315, 70),
		Ressources.menuButton.getSubimage(3 * 315, 815, 315, 70),
		"alleScores", Ressources.RASTERHEIGHT, 3 * Ressources.RASTERHEIGHT - 10, this);
		pershighscore.add(alleScoresButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
		buttons.put("alleScores", alleScoresButton);
		
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
		gameDetails[1].setBounds(405, 325, 400, 30);
		gameDetails[1].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[1], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[2] = new JLabel(); //Score
		gameDetails[2].setText("999.99");
		gameDetails[2].setFont(new Font("Corbel",Font.BOLD,40));
		gameDetails[2].setBounds(610, 295, 150, 50);
		gameDetails[2].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[2].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[2], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[3] = new JLabel();
		gameDetails[3].setText("Spielminuten");
		gameDetails[3].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[3].setBounds(405, 375, 200, 30);
		gameDetails[3].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[3], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[4] = new JLabel(); //Spielminuten
		gameDetails[4].setText("1855425556");
		gameDetails[4].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[4].setBounds(640, 375, 120, 30);
		gameDetails[4].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[4].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[4], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[5] = new JLabel(); 
		gameDetails[5].setText("Terrorist festgenommen");
		gameDetails[5].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[5].setBounds(405, 425, 250, 30);
		gameDetails[5].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[5], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[7] = new JLabel(); //Terrorist
		gameDetails[7].setText("Nein");
		gameDetails[7].setFont(new Font("Corbel",Font.BOLD,14));
		gameDetails[7].setBounds(710, 425, 50, 30);
		gameDetails[7].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[7].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[7], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[6] = new JLabel(); //Terrorist Balken
		gameDetails[6].setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(951, 225, 129, 15)));
		gameDetails[6].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[6].setBounds(635, 425, 200, 30);
		gameDetails[6].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[6], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[8] = new JLabel(); //Uhrzeit Datum
		gameDetails[8].setText("Max. Misstrauen");
		gameDetails[8].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[8].setBounds(405, 475, 200, 30);
		gameDetails[8].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[8], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[9] = new JLabel(); //Uhrzeit Datum
		gameDetails[9].setText("121,9 %");
		gameDetails[9].setFont(new Font("Corbel",Font.BOLD,14));
		gameDetails[9].setBounds(710, 475, 50, 30);
		gameDetails[9].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[9].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[9], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[10] = new JLabel(); //Uhrzeit Datum
		gameDetails[10].setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(951, 225, 129, 15)));
		gameDetails[10].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[10].setBounds(635, 475, 200, 30);
		gameDetails[10].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[10], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[11] = new JLabel(); //Uhrzeit Datum
		gameDetails[11].setText("Wissenswert");
		gameDetails[11].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[11].setBounds(405, 525, 200, 30);
		gameDetails[11].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[11], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[12] = new JLabel(); //Uhrzeit Datum
		gameDetails[12].setText("191.7 %");
		gameDetails[12].setFont(new Font("Corbel",Font.BOLD,14));
		gameDetails[12].setBounds(710, 525, 50, 30);
		gameDetails[12].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[12].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[12], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[13] = new JLabel(); //Uhrzeit Datum
		gameDetails[13].setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(951, 225, 129, 15)));
		gameDetails[13].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[13].setBounds(635, 525, 200, 30);
		gameDetails[13].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[13], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[14] = new JLabel(); //Uhrzeit Datum
		gameDetails[14].setText("Anzahl entdeckter Events");
		gameDetails[14].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[14].setBounds(405, 575, 250, 30);
		gameDetails[14].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[14], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[15] = new JLabel(); //Uhrzeit Datum
		gameDetails[15].setText("6 / 12");
		gameDetails[15].setFont(new Font("Corbel",Font.BOLD,14));
		gameDetails[15].setBounds(710, 575, 50, 30);
		gameDetails[15].setForeground(new Color(0xf9,0xf9,0xf9));
		gameDetails[15].setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		pershighscore.add(gameDetails[15], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		gameDetails[16] = new JLabel(); //Uhrzeit Datum
		gameDetails[16].setIcon(new ImageIcon (Ressources.ingamebutton.getSubimage(951, 225, 129, 15)));
		gameDetails[16].setFont(new Font("Corbel",Font.BOLD,20));
		gameDetails[16].setBounds(635, 575, 200, 30);
		gameDetails[16].setForeground(new Color(0xf9,0xf9,0xf9));
		pershighscore.add(gameDetails[16], javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		
		
		
		
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
	 * Reset pers Highscore
	 * @author Martika
	 */
	public void resetPersHighscore(){
		gameDetails[0].setVisible(false);
		gameDetails[2].setVisible(false);
		gameDetails[3].setVisible(false);
		gameDetails[4].setVisible(false);
		gameDetails[5].setVisible(false);
		gameDetails[6].setVisible(false);
		gameDetails[7].setVisible(false);
		gameDetails[8].setVisible(false);
		gameDetails[9].setVisible(false);
		gameDetails[10].setVisible(false);
		gameDetails[11].setVisible(false);
		gameDetails[12].setVisible(false);
		gameDetails[13].setVisible(false);
		gameDetails[14].setVisible(false);
		gameDetails[15].setVisible(false);
		gameDetails[16].setVisible(false);
		gameDetails[1].setText("Wählen Sie einen Spielstand aus");
		
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
     * @author Tobias
     * 
     * kopiert einen Fortschrittsbalken aus veschiedenen grafiken zusammen
     * 
     * @param valuePercent Prozentuale anzeige des Balkens
     * @param thresholdPercent Schwellwert von rotem zu grünem Balken
     * @return Bild mit kompletter Leiste mit Hintergrung
     */
    private BufferedImage createPrivateScoreBar(double valuePercent, double thresholdPercent){
    	BufferedImage output = new BufferedImage(135,20,BufferedImage.TYPE_INT_ARGB); 
    	
    	Graphics2D g2d = output.createGraphics();
    	g2d.drawImage(Ressources.ingamebutton.getSubimage(948, 180, 135, 20), 0, 0,null);
    	if(valuePercent>1d)
    		valuePercent=1d;
    	if(valuePercent<=0.008d)
    		valuePercent=0.008d;
    	if(valuePercent>thresholdPercent)
    		g2d.drawImage(Ressources.ingamebutton.getSubimage(948, 222, (int)(valuePercent*135), 20),0,0,null);
    	else
    		g2d.drawImage(Ressources.ingamebutton.getSubimage(948, 201, (int)(valuePercent*135), 20),0,0,null);
    	return output;
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
	
	public void updatePersHighscore(){  
		int selectionId = persHighscoreList.getSelectedIndex();
		ArrayList<String> score=(ArrayList<String>) persHighscoreList.getModel().getElementAt(selectionId);
		for (int i=0; i<16; i++){
			gameDetails[i].setVisible(true);
		}
		gameDetails[0].setIcon(new ImageIcon(levelicons.get(score.get(9))));
		gameDetails[1].setText(score.get(0)+" - "+score.get(1).substring(0,score.get(1).length()-3)+" Uhr");
		gameDetails[2].setText(score.get(2));
		gameDetails[4].setText(score.get(5));
		gameDetails[6].setIcon(new ImageIcon(createPrivateScoreBar(1d, score.get(3).equals("JA")?0d:1d)));
		gameDetails[7].setText(score.get(3));
		gameDetails[10].setIcon(new ImageIcon(createPrivateScoreBar(Double.parseDouble(score.get(4))/100, 0.25d)));
		gameDetails[9].setText(score.get(4)+"%");
		gameDetails[13].setIcon(new ImageIcon(createPrivateScoreBar(Double.parseDouble(score.get(6))/100, 0.25d)));
		gameDetails[12].setText(score.get(6)+"%");
		gameDetails[16].setIcon(new ImageIcon(createPrivateScoreBar(Double.parseDouble(score.get(7))/Double.parseDouble(score.get(8)), 0.25d)));
		gameDetails[15].setText(score.get(7)+" / "+score.get(8));
	}
	
	public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}
