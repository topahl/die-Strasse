package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.TheStreet;

/**
 * 
 * @author Alle haben mitgewirkt
 *
 */
public class GUILayer extends JFrame implements MouseMotionListener {

	private HashMap<String, Button> buttons = new HashMap<String, Button>();
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Map karte;
	private JLayeredPane baseLayer;
	private JLayeredPane fensterSpionage;
	private JLayeredPane fensterBeschwichtigen;
	private JLayeredPane fensterQuiz;
	private Simulation simulation;
	private Control control = new Control(this);
	private Haus haus;
	private ArrayList<Mensch> humans = new ArrayList<Mensch>();
	private int stepcounter = 0;

	private JLabel anzeigeZeit = new JLabel();
	private JLabel anzeigeTag = new JLabel();
	private JLabel overlayMenubar = new JLabel();
	private JLabel anzeigeStatusMisstrauen = new JLabel();
	private JLabel anzeigeStatusUeberwachung = new JLabel();
	private JLabel overlayMousefollower = new JLabel();
	private JLabel overlayNacht = new JLabel();
	private JLabel spionageBeschr = new JLabel();
	private JLabel beschwichtigenBeschr = new JLabel();

	
	
	/**
	 * Konstruktor - steuert die Initialisierung aller GUI-Elemente
	 */
	public GUILayer(String levelname) {
		
		Mensch.loadImages(levelname);
		Ressources.loadLevelInfomration(levelname);
		
		simulation = new Simulation();
		
		this.initComponents(levelname);
		this.simulation.initialize_beziehungsmatrix();
		this.setVisible(true);
		this.timer = new Timer(Ressources.GAMESPEED, new OSTimer(this));
		this.timer.setCoalesce(false);
		this.timer.start();
	}



	/**
	 * Beenden: das Spiel stoppen und Fenster schließen
	 */
	public void endGame() {
		TheStreet.loadMenu();
		this.timer.stop();
		this.dispose();
	}



	/**
	 * Liebesbriefe an Tobi
	 */
	private void initComponents(String levelname) {
		
		//Einstellungen zum Basisfenster
		this.baseLayer = new JLayeredPane();
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		this.setResizable(false);
		this.setUndecorated(true);

		//MouseListener starten
		this.initMouseListener();

		//Aktionsfenster initialisieren
		this.initAktionsfenster();

		//Buttons auf Häusern erzeugen
		this.initButtonsHaeuser();

		//Buttons im Spielfenster erzeugen
		this.initButtonsGame();
		
		//Spiel-Statusanzeigen und Aktionsleisten-Grafik erzeugen
		this.initSpielanzeigen();

		//Menschen erzeugen und in Häuser beamen
		this.initHumans();

		//Levelkarte laden
		this.initMap(levelname);

		//Letzte Einstellungen zum Fenster
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				this.baseLayer, javax.swing.GroupLayout.DEFAULT_SIZE,
				Ressources.SCREEN.width, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				this.baseLayer, javax.swing.GroupLayout.DEFAULT_SIZE,
				Ressources.SCREEN.height, Short.MAX_VALUE));

		this.setBackground(Color.black);
		this.pack();
	}

	
	
	/**
	 * Erzeugen der Spielanzeigen und Aktionsleisten-Grafik
	 * @author Tobi
	 */
	private void initSpielanzeigen() {
		// Tag malen
		String s = "Tag " + this.simulation.getSpiel_tag();
		this.anzeigeTag.setText(s);
		this.anzeigeTag.setBounds(1004 + Ressources.ZEROPOS.width, 636 + Ressources.ZEROPOS.height, 100, 37);
		this.anzeigeTag.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		this.anzeigeTag.setFont(new Font("Corbel", Font.BOLD, 30));
		this.anzeigeTag.setForeground(new java.awt.Color(249, 249, 249));
		this.anzeigeTag.setVisible(true);
		this.baseLayer.add(this.anzeigeTag, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Uhr malen
		s = this.simulation.getSpielzeit_as_string();
		this.anzeigeZeit.setText(s);
		this.anzeigeZeit.setBounds(1010 + Ressources.ZEROPOS.width, 669 + Ressources.ZEROPOS.height, 100, 37);
		this.anzeigeZeit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		this.anzeigeZeit.setFont(new Font("Corbel", Font.BOLD, 40));
		this.anzeigeZeit.setForeground(new java.awt.Color(249, 249, 249));
		this.anzeigeZeit.setVisible(true);
		this.baseLayer.add(this.anzeigeZeit, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Misstrauensanzeige in der Straße
		s = "0.0%";
		this.anzeigeStatusMisstrauen.setText(s);
		this.anzeigeStatusMisstrauen.setBounds(713 + Ressources.ZEROPOS.width, 638 + Ressources.ZEROPOS.height, 183, 37);
		this.anzeigeStatusMisstrauen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		this.anzeigeStatusMisstrauen.setFont(new Font("Corbel", Font.BOLD, 16));
		this.anzeigeStatusMisstrauen.setForeground(new java.awt.Color(249, 249, 249));
		this.anzeigeStatusMisstrauen.setVisible(true);
		this.baseLayer.add(this.anzeigeStatusMisstrauen, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Überwachungsanzeige in der Straße
		this.anzeigeStatusUeberwachung.setText(s);
		this.anzeigeStatusUeberwachung.setBounds(713 + Ressources.ZEROPOS.width, 677 + Ressources.ZEROPOS.height, 183, 37);
		this.anzeigeStatusUeberwachung.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		this.anzeigeStatusUeberwachung.setFont(new Font("Corbel", Font.BOLD, 16));
		this.anzeigeStatusUeberwachung.setForeground(new java.awt.Color(249, 249, 249));
		this.anzeigeStatusUeberwachung.setVisible(true);
		this.baseLayer.add(this.anzeigeStatusUeberwachung, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Ingame Menübars
		this.overlayMenubar.setIcon(new ImageIcon(Ressources.menubars));
		this.overlayMenubar.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
		this.baseLayer.add(this.overlayMenubar, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Tag-Nacht-Modus
		this.overlayNacht.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
		this.overlayNacht.setBackground(new Color(0, 0, 1f, 0.2f));
		this.overlayNacht.setOpaque(true);
		this.overlayNacht.setVisible(false);
		this.baseLayer.add(this.overlayNacht, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	
	
	/**
	 * Levelkarte laden und einfügen
	 */
	private void initMap(String levelname) {
		// TODO Dynamic map load
	
		// Karte laden
		this.karte = new Map(levelname, this.humans.get(this.humans.size() - 1).get_haus_id());
		// Agent steht an letzter Stelle
		this.karte.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, 1125, 720);
		this.baseLayer.add(this.karte, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}

	
	
	/**
	 * Initialisierung des MouseListeners und Mousefollower-Icons
	 * @author Tiki
	 */
	private void initMouseListener() {
		// MouseMotionListener für die kleinen Icons die die Maus verfolgen
		this.addMouseMotionListener(this);

		// MousefollowerIcon wenn man eine Aktion tätigt
		this.overlayMousefollower.setBounds(15, 225, 39, 39);
		this.overlayMousefollower.setVisible(false);
		this.overlayMousefollower.setIcon(new ImageIcon(Ressources.ingamebutton.getSubimage(0, 0, 39, 39)));
		this.baseLayer.add(this.overlayMousefollower, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	

	
	/**
	 * Erzeugt die Aktionsfenster mit Inhalten
	 * @author Stephan
	 */
	private void initAktionsfenster() {

		//Dummys für Erzeugung
		JLabel label; 
		Button button;
		
		//Fenster: Quizfragen
		this.fensterQuiz = new JLayeredPane();
		this.fensterQuiz.setBounds(Ressources.ZEROPOS.width+(Ressources.MAPWIDTH-598)/2, Ressources.ZEROPOS.height+(Ressources.MAPHEIGHT-327)/2,598, 327);
		this.baseLayer.add(this.fensterQuiz, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		
		
		// Fenster: Beschwichtigen Aktionsfenster
		this.fensterBeschwichtigen = new JLayeredPane();
		this.fensterBeschwichtigen.setBounds(Ressources.ZEROPOS.width + 10, Ressources.ZEROPOS.height + 390, 248, 232);
		this.baseLayer.add(this.fensterBeschwichtigen, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Schließen-Button Beschwichtigen
		button = new Button(this.control,
				Ressources.ingamebutton.getSubimage(948, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(975, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(1002, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(1029, 90, 27, 27),
				"closeBeschwichtigen", 205, 13, this);
		this.buttons.put("closeBeschwichtigen", button);
		this.fensterBeschwichtigen.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Inhalt des Beschwichtigen Fensters
		label = new JLabel();
		label.setText("Sozialleben");
		label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		label.setFont(new Font("Corbel", Font.BOLD, 25));
		label.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
		label.setBounds(20, 12, 200, 30);
		this.fensterBeschwichtigen.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		beschwichtigenBeschr.setText("");
		beschwichtigenBeschr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		beschwichtigenBeschr.setFont(new Font("Corbel", Font.BOLD, 20));
		beschwichtigenBeschr.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
		beschwichtigenBeschr.setBounds(20, 50, 200, 30);
		this.fensterBeschwichtigen.add(beschwichtigenBeschr, javax.swing.JLayeredPane.DEFAULT_LAYER);		
		this.fensterBeschwichtigen.setEnabled(false);
		this.fensterBeschwichtigen.setVisible(false);
		
		//Titel Quizfrage
		label = new JLabel();
		label.setText("Quizfrage");
		label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		label.setFont(new Font("Corbel", Font.BOLD, 25));
		label.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
		label.setBounds(20, 12, 200, 30);
		this.fensterQuiz.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		//Inhal des Quiz Fensters
		int buttonSize = 39;
		int buttonSliceX = 0;
		int buttonSliceY = buttonSize;

		String[] buttonNamesSmall = { "QuizA", "QuizB","QuizC" };
		for (int i = 14; i < buttonNamesSmall.length+14; i++) {
			button = new Button(this.control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNamesSmall[i-14], 45, 140+(i-14)*45 , this);
			label = new JLabel();
			label.setText("Antwort");
			label.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
			label.setBounds(100, 140+(i-14)*45, 300, 39);
			label.setFont(new Font("Corbel", Font.BOLD, 20));
			
			this.fensterQuiz.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.fensterQuiz.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.buttons.put(buttonNamesSmall[i-14], button);
		}
		//Textfeld für die Frage des Quizes
		JTextArea frage = new JTextArea();
        frage.setLineWrap(true);
        frage.setText("Frage bla bla bla..");
        frage.setWrapStyleWord(true);
        frage.setFocusCycleRoot(true);
        frage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        frage.setFocusable(false);
        frage.setOpaque(false);
        frage.setFont(new Font("Corbel",Font.BOLD,20));
        frage.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
        frage.setBounds(45,60, 500, 75);
        this.fensterQuiz.add(frage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        
		
		// Fenster: Spionage Aktionsfenster
		this.fensterSpionage = new JLayeredPane();
		this.fensterSpionage.setBounds(Ressources.ZEROPOS.width + 90, Ressources.ZEROPOS.height + 390, 248, 235);
		this.baseLayer.add(this.fensterSpionage, javax.swing.JLayeredPane.DEFAULT_LAYER);

		//Schließen-Button Spionage
		button = new Button(this.control,
				Ressources.ingamebutton.getSubimage(948, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(975, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(1002, 90, 27, 27),
				Ressources.ingamebutton.getSubimage(1029, 90, 27, 27),
				"closeSpionage", 205, 13, this);
		this.buttons.put("closeSpionage", button);
		this.fensterSpionage.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);

		// Inhalt des Spionage Fensters
		label = new JLabel();
		label.setText("Spionage");
		label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		label.setFont(new Font("Corbel", Font.BOLD, 25));
		label.setForeground(new java.awt.Color(0x1f, 0x1f, 0x1f));
		label.setBounds(20, 12, 200, 30); 
		this.fensterSpionage.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		spionageBeschr.setText("");
		spionageBeschr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		spionageBeschr.setFont(new Font("Corbel", Font.BOLD, 20));
		spionageBeschr.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
		spionageBeschr.setBounds(20, 50, 200, 30);
		this.fensterSpionage.add(spionageBeschr, javax.swing.JLayeredPane.DEFAULT_LAYER);
		this.fensterSpionage.setEnabled(false);
		this.fensterSpionage.setVisible(false);

		
		//Buttons in Aktionsfenster erzeugen
		String[] buttonNamesBeschwichtigen = { "aktionKuchen",
				"aktionUnterhalten", "aktionFlirten", "aktionHand",
				"aktionParkBeschwichtigen", "aktion6Beschwichtigen" };
		this.initButtonsAktionsfenster(fensterBeschwichtigen, 156, buttonNamesBeschwichtigen);

		String[] buttonNamesSpionage = { "aktionWanze", "aktionKamera",
				"aktionHacken", "aktionFernglas", "aktionParkSpionage",
				"aktion6Spionage" };
		this.initButtonsAktionsfenster(fensterSpionage, 420, buttonNamesSpionage);
		
		
		//Hintergrundbild Spionage-Aktionsfenster
		label = new JLabel();
		label.setIcon(new ImageIcon(Ressources.ingameframe.getSubimage(0, 0, 248, 235)));
		label.setBounds(0, 0, 248, 232);
		this.fensterSpionage.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);

		
		//Hintergrundbild Beschwichtigen-Aktionsfenster
		label = new JLabel();
		label.setIcon(new ImageIcon(Ressources.ingameframe.getSubimage(0, 0, 248, 235)));
		label.setBounds(0, 0, 248, 232);
		this.fensterBeschwichtigen.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		//Hintergund Quiz Fenster
		label = new JLabel();
		label.setIcon(new ImageIcon(Ressources.ingameframe.getSubimage(765, 0, 598, 327)));
		label.setBounds(0, 0, 598, 327);
		this.fensterQuiz.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}


	/**
	 * Erzeugt alle Buttons (Aktionsbuttons, Pause/Exit) im Spielfenster
	 * @author Stephan
	 */
	private void initButtonsGame() {
		Button button;
		// Pause + Exit Button
		int buttonSize = Ressources.RASTERHEIGHT;
		int buttonSliceX = 948;
		int buttonSliceY = buttonSize;

		String[] buttonNamesPauseExit = { "pause", "close" };
		for (int i = 0; i < buttonNamesPauseExit.length; i++) {
			button = new Button(this.control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNamesPauseExit[i], Ressources.ZEROPOS.width + (23 + i) * Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height, this);
			this.baseLayer.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.buttons.put(buttonNamesPauseExit[i], button);
		}

		// Große Buttons
		buttonSize = 66;

		// Buttons Aktionsleiste
		buttonSliceX = 684;
		buttonSliceY = buttonSize;

		String[] buttonNamesAktionsleiste = { "aktionenBeschwichtigen",
				"aktionenSpionage", "aktionNachhause", "aktionRazzia" };
		for (int i = 0; i < buttonNamesAktionsleiste.length; i++) {
			button = new Button(this.control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNamesAktionsleiste[i], Ressources.ZEROPOS.width + 12 + i * 2*Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height + 642, this);
			// Nach Hause und Razzia disabled
			if (i > 1) {
				button.setEnabled(false);
			}
			this.baseLayer.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.buttons.put(buttonNamesAktionsleiste[i], button);
		}

		// Kleine Buttons TODO entfernen
		buttonSize = 39;
		buttonSliceX = 0;
		buttonSliceY = buttonSize;

		String[] buttonNamesSmall = { "smallWanze", "smallKamera",
				"smallHacken", "smallFernglas", "smallParkSpionage",
				"small6Spionage", "smallKuchen", "smallUnterhalten",
				"smallFlirten", "smallHand", "smallParkBeschwichtigen",
				"small6Beschwichtigen", "smallNachhause", "smallRazzia",
				"smallQuizA", "smallQuizB", "smallQuizC" };
		for (int i = 0; i < buttonNamesSmall.length; i++) {
			button = new Button(this.control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNamesSmall[i], Ressources.ZEROPOS.width - 39, 0 + i * buttonSize, this);
			this.baseLayer.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.buttons.put(buttonNamesSmall[i], button);
		}
	}



	/**
	 * Erzeugung der unsichtbaren Buttons über den Häusern
	 * Nummeriert von 1-9
	 * @author Sven
	 */
	private void initButtonsHaeuser() {
		Button button;
		Integer i;
		Integer j;
		Integer x;
		Integer b;
		b = 0;
		for (x = 1; x <= Ressources.NUMBERHOUSES; x++) {
			b = 0;
			if (b == 0) {
				for (j = 0; j < Ressources.getLocation_ids().size(); j++) {
					if (b == 0) {
						for (i = 0; i < Ressources.getLocation_ids().get(0)
								.size(); i++) {
							if (Ressources.getLocation_ids().get(j).get(i)
									.equals(String.valueOf(x))
									&& b == 0) {
								b = 1;
								button = new Button(this.control,
										Ressources.RASTERHEIGHT * 3,
										Ressources.RASTERHEIGHT * 3, "Haus"
												+ String.valueOf(x),
										Ressources.ZEROPOS.width + i
												* Ressources.RASTERHEIGHT,
										Ressources.ZEROPOS.height + j
												* Ressources.RASTERHEIGHT, this);
								this.baseLayer.add(button,
										javax.swing.JLayeredPane.DEFAULT_LAYER);
								this.buttons.put("Haus" + String.valueOf(x),
										button);

							}
						}
					}
				}
			}
		}
	}

	

	/**
	 * Erzeugt Aktionsfenster für Spionage und Beschwichtigen
	 * @param fenster Fenster-Objekt
	 * @param buttonSliceX Start-X-Position auf der Button Slicemap
	 * @param buttonNames
	 * @author Stephan
	 */
	private void initButtonsAktionsfenster(JLayeredPane fenster, int buttonSliceX, String[] buttonNames) {
		int buttonSize = 66;
		int buttonSliceY = buttonSize;
		int buttonPosX = 0;
		int buttonPosY = 76;

		for (int i = 0; i < buttonNames.length; i++) {
			Button button = new Button(this.control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNames[i], 15 + buttonPosX * 75, buttonPosY, this);
			fenster.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.buttons.put(buttonNames[i], button);

			//Button 1 Position weiter rechts zeichnen
			buttonPosX++;
			//nach 3 Buttons neue Zeile
			if ((i + 1) % 3 == 0) {
				buttonPosX = 0;
				buttonPosY += 75;
			}
		}
	}

	

	/**
	 * Generieren der Menschen und Teleport in die Häuser
	 * @author Miri
	 */
	private void initHumans() {
		// jeweils dem Agenten und dem Terroristen eine Hausnummer generieren
		// und dafür sorgen, dass sie ungleich sind
		int house_of_terrorist = (int) (Math.random() * Ressources.NUMBERHOUSES);
		int agent_house_nr = (int) (Math.random() * Ressources.NUMBERHOUSES);
		if (agent_house_nr == house_of_terrorist) {
			if (house_of_terrorist == 8) {
				house_of_terrorist--;
			} else {
				house_of_terrorist++;
			}
		}

		int people_per_house;
		int number_of_adults;
		int number_of_children;

		// erster Erscheinungspunkt einer Person in einem Haus --> jedes Haus
		// hat einen
		int spawnHausX[] = new int[Ressources.NUMBERHOUSES];
		int spawnHausY[] = new int[Ressources.NUMBERHOUSES];

		// alle Erscheinungspunkte der Personen in einem Haus
		int spawnPersonX[] = new int[4];
		int spawnPersonY[] = new int[4];

		int familien_cnt = 0; // wie viele Menschen in einem Haus schon
								// generiert wurden
		int mensch_cnt = 0; // wie viele Menschen insgesamt generiert wurden
		Mensch mensch; // Hilfsvariable zum Zwischenspeichern

		// Spawnpunkte initialisieren
		// immer linke obere Ecke in einem Haus
		ArrayList<ArrayList<String>> location_raster = Ressources
				.getLocation_ids();
		for (int haus = 0; haus < Ressources.NUMBERHOUSES; haus++) {
			// für jedes Haus die Location in der Map herausfinden
			for (int i = 0; i < location_raster.size(); i++) { // y-Achse
				for (int j = 0; j < location_raster.get(i).size(); j++) { // x-Achse
					// das erste 45er-Pixel, das man von dem Haus findet, wird
					// der erste Spawnpunkt
					if (location_raster.get(i).get(j).charAt(0) == ("" + (haus + 1))
							.charAt(0)) {
						spawnHausX[haus] = j * Ressources.RASTERHEIGHT
								+ Ressources.ZEROPOS.width - 2
								* Ressources.RASTERHEIGHT;
						spawnHausY[haus] = i * Ressources.RASTERHEIGHT
								+ Ressources.ZEROPOS.height - 2
								* Ressources.RASTERHEIGHT;
					}

				}
			}
		}

		for (int i = 0; i < Ressources.NUMBERHOUSES; i++) {
			// für jedes Haus die Familie erstellen
			familien_cnt = 0;
			// für ein Haus die Spawnpunkte festlegen
			spawnPersonX[0] = spawnHausX[i];
			spawnPersonY[0] = spawnHausY[i];
			spawnPersonX[1] = spawnHausX[i] + Ressources.RASTERHEIGHT;
			spawnPersonY[1] = spawnHausY[i];
			spawnPersonX[2] = spawnHausX[i] + 2 * Ressources.RASTERHEIGHT;
			spawnPersonY[2] = spawnHausY[i];
			spawnPersonX[3] = spawnHausX[i];
			spawnPersonY[3] = spawnHausY[i] + Ressources.RASTERHEIGHT;

			// Agent wird als letztes hinzugefügt
			if (i != agent_house_nr) {
				people_per_house = (int) (Math.random() * 4) + 1;
				// wie viele Menschen in einem Haus wohnen sollen
				if (i == house_of_terrorist) {
					// Terrorist muss kann kein Kind sein
					number_of_adults = 1 + (int) (Math.random() * people_per_house);
					mensch = new Terrorist(i);
					this.humans.add(mensch);
					this.baseLayer.add(mensch,
							javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(
							spawnPersonX[familien_cnt],
							spawnPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(
							spawnPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(
							spawnPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;

					// übrige Erwachsene setzen
					for (int j = 0; j < number_of_adults - 1; j++) {
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						this.baseLayer.add(mensch,
								javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(
								spawnPersonX[familien_cnt],
								spawnPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(
								spawnPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(
								spawnPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				} else {
					// in jedem Haushalt muss mindestens ein Erwachsener leben
					number_of_adults = (int) (Math.random() * people_per_house) + 1;
					for (int j = 0; j < number_of_adults; j++) {
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						this.baseLayer.add(mensch,
								javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(
								spawnPersonX[familien_cnt],
								spawnPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(
								spawnPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(
								spawnPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				}
				// Kinder berechnen
				number_of_children = people_per_house - number_of_adults;
				for (int j = 0; j < number_of_children; j++) {
					mensch = new Kinder(i);
					this.humans.add(mensch);
					this.baseLayer.add(mensch,
							javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(
							spawnPersonX[familien_cnt],
							spawnPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(
							spawnPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(
							spawnPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;
				}
				this.initHaus(i, false, spawnHausX[0], spawnHausY[0]);
			}
		}

		// Simulation benötigt die Information von allen Bewohnern (ohne Agent)
		for (int i = 0; i < this.humans.size(); i++) {
			this.simulation.set_people((Person) this.humans.get(i));
		}

		// Agent hinzufügen
		spawnPersonX[0] = spawnHausX[agent_house_nr];
		spawnPersonY[0] = spawnHausY[agent_house_nr];
		mensch = new Agent(agent_house_nr);
		this.humans.add(mensch);
		this.baseLayer.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
		this.humans.get(mensch_cnt).teleport(spawnPersonX[0], spawnPersonY[0]);
		this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[0]);
		this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[0]);
		this.simulation.set_agent((Agent) mensch);
		this.initHaus(agent_house_nr, true, spawnPersonX[0], spawnPersonY[0]);
	}

	

	/**
	 * Erzeugung eines Hauses
	 * @param hausnr Haus-ID
	 * @param agentenhaus Ist dieses Haus das Agentenhaus?
	 * @param x X-Position des Hauses
	 * @param y Y-Position des Hauses
	 * @author Miri
	 */
	private void initHaus(int hausnr, boolean agentenhaus, int x, int y) {
		this.haus = new Haus(hausnr, agentenhaus, x, y);
		this.simulation.setHouses(this.haus);
	}


	
	/**
	 * LocationID einer Person bei jedem Step updaten
	 * @author Miri
	 */
	private void updateLocationID() {
		ArrayList<ArrayList<String>> location_raster = Ressources.getLocation_ids();
		int x;
		int y;
		for (int i = 0; i < this.humans.size(); i++) {
			x = (this.humans.get(i).getPosX() + 2 * Ressources.RASTERHEIGHT - Ressources.ZEROPOS.width)
					/ Ressources.RASTERHEIGHT - 2;
			y = (this.humans.get(i).getPosY() + 2 * Ressources.RASTERHEIGHT - Ressources.ZEROPOS.height)
					/ Ressources.RASTERHEIGHT - 2;
			this.humans.get(i).set_location_id(
					location_raster.get(y).get(x).charAt(0));
		}

	}

	
	
	/**
	 * Zeitanzeige in der Spielfenster-Aktionsleiste updaten
	 * @author Miri 
	 */
	public void updateTime() {
		// Tag malen
		String s = "Tag " + this.simulation.getSpiel_tag();
		this.anzeigeTag.setText(s);

		// Uhr malen
		s = this.simulation.getSpielzeit_as_string();
		this.anzeigeZeit.setText(s);
	}

	
	
	/**
	 * Misstrauensstatus in der Spielfenster-Aktionsleiste updaten
	 * @author Miri
	 */
	public void updateMisstrauen() {
		String s = (float) Math.round(this.simulation
				.calc_misstrauen_in_street() * 100) / 100 + "%";
		this.anzeigeStatusMisstrauen.setText(s);
	}

	
	
	/**
	 * Überwachungsstatus in der Spielfenster-Aktionsleiste updaten 
	 * @author Miri
	 */
	public void updateUeberwachung() {
		String s = (float) Math.round(this.simulation
				.calc_ueberwachung_in_street() * 100) / 100 + "%";
		this.anzeigeStatusUeberwachung.setText(s);
	}

	
	
	/**
	 * Pausiert das Spiel wenn es läuft, und setzt es fort, wenn es pausiert ist
	 * @author Stephan
	 */
	public void updateTimerStatus() {
		if (this.timer.isRunning()) {
			this.timer.stop();
		} else {
			this.timer.start();
		}
	}
	
	
	
	/**
	 * Step steuert den zeitlichen Ablauf des Spieles und triggert die Simulation und Updates im Spielfenster
	 * @author Miri, Tobias, Tiki
	 */
	public void step() {
		this.updateLocationID();

		// zeichne neuen Überwachungswert
		this.updateUeberwachung();

		// Tag-Nacht-Modus
		if (this.simulation.getSpiel_stunde() == 20) {
			this.overlayNacht.setVisible(true);
		}
		if (this.simulation.getSpiel_stunde() == 6) {
			this.overlayNacht.setVisible(false);
		}

		//Misstrauen berechnen alle 25 Steps
		if (this.stepcounter % 25 == 0) {
			this.simulation.calculate_misstrauen();
			this.simulation.calc_misstrauen_in_street();
			for (int i = 0; i < this.humans.size(); i++) {
				if (this.humans.get(i) instanceof Person) {
					((Person) this.humans.get(i)).update_schatten();
				}
			}
			this.updateMisstrauen(); // Wert neu zeichnen
		}

		//Spielzeit und Tagesablauf berechnen alle 4 Steps
		if (this.stepcounter % 4 == 0) {
			this.simulation.calc_spielzeit();
			this.updateTime(); // Zeichne Spiel
			this.simulation.tagesablauf();
		}

		// Update der Personen - Step ausführen
		for (int i = 0; i < this.humans.size(); i++) {
			this.humans.get(i).step();
		}

		this.stepcounter++;
	}


	
	/**
	 * Gibt die HashMap der Buttons zurück
	 * @return Hashmap der Ingame-Buttons
	 * @author Stephan
	 */
	public HashMap<String, Button> getButtonsMap() {
		return this.buttons;
	}

	
	
	/**
	 * Gibt das passende Fenster zurück
	 * @param window Name des Fensters
	 * @return Fenster-Objekt
	 * @author Tobi
	 */
	public JLayeredPane getWindow(String window) {
		if (window.equals("spionage"))
			return this.fensterSpionage;
		if (window.equals("beschwichtigen"))
			return this.fensterBeschwichtigen;
		if (window.equals("quizfenster"))
			return this.fensterQuiz;
		return null;
	}

	/**
	 * Lifert das Jlabel für den Beschreibungstext im Aktionsfenster
	 * @param window String für das Fenster
	 * @return jLabel des Beschreibungstext
	 */
	public JLabel getBeschreibung(String window){
		if (window.equals("spionage"))
			return this.spionageBeschr;
		if (window.equals("beschwichtigen"))
			return this.beschwichtigenBeschr;
		return null;
	}

	/**
	 * Gibt Mousefollower zurück
	 * @return Mousefollower JLabel (Grafik)
	 */
	public JLabel getMousefollower() {
		return this.overlayMousefollower;
	}

	
	
	/**
	 * ActionListener für bewegte Maus
	 * @author Tiki
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		//Mausicon bewegen
		this.overlayMousefollower.setLocation(e.getXOnScreen() - 15,
				e.getYOnScreen() - 15);
	}
	
	
	/**
	 * ActionListener für Drag & Drop der Maus
	 * @author Tiki
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// do nothing; notwendig für implements MouseMotion (Interface)
	}

}
