/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import com.stalkindustries.main.Button;
import com.stalkindustries.main.TheStreet;


/**
 *
 * @author Tobias
 */
public class GUILayer extends JFrame{

	
	private HashMap<String,Button> buttons = new HashMap<String,Button>();
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Map karte;
	private JLayeredPane layeredPane;
	private boolean b = true; //TODO please rename  if needed
	private Simulation simulation = new Simulation();
	private Control control = new Control(this);
	private Haus haus;//wird sp�ter initialisiert
	private ArrayList<Mensch> humans = new ArrayList<Mensch>();
	private int stepcounter=0;
	
	private JLabel zeit = new JLabel();
	private JLabel tag = new JLabel();
	private JLabel menubar = new JLabel();
	private JLabel misstrauen_in_street = new JLabel();
	private JLabel ueberwachung_in_street = new JLabel();
	
    public GUILayer() {
        initComponents();
        this.simulation.initialize_beziehungsmatrix();
        setVisible(true);
        this.timer = new Timer(Ressources.GAMESPEED,new OSTimer(this));
        timer.setCoalesce(false);
        timer.start();
    }
    
    public void endGame(){
    	TheStreet.loadMenu();
    	timer.stop();
    	this.dispose();
    }
    
    //Liebesbriefe an Tobi
    private void initComponents() {
        layeredPane = new javax.swing.JLayeredPane();
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        
        
        // --------
        // alle Buttons erzeugen und hinzuf�gen
        // Gr��e von Stephan
        // --------
        Button button;
        
        //Exit
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(948, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+2*Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+3*Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		"close", Ressources.ZEROPOS.width+24*Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("close", button);
        
        //Pause
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(948, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+2*Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		Ressources.ingamebutton.getSubimage(948+3*Ressources.RASTERHEIGHT, 0, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT),
        		"pause", Ressources.ZEROPOS.width+23*Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("pause", button);

        //Gro�e Buttons
        int buttonSize = 66;
        
        //Buttons Beschwichtigen
        //Hauptbutton unten
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(156, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(156+buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(156+2*buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(156+3*buttonSize, 0, buttonSize, buttonSize),
        		"aktionenBeschwichtigen", Ressources.ZEROPOS.width+12, Ressources.ZEROPOS.height+642);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("aktionenBeschwichtigen", button);
        
        //Aktionsbuttons
        String[] buttonNamesBeschwichtigen = { "aktionKuchen", "aktionUnterhalten", "aktionFlirten", "aktionHand", "aktionParkBeschwichtigen" };
        for(int i = 0; i < buttonNamesBeschwichtigen.length; i++) {
	        button = new Button(control,
	        		Ressources.ingamebutton.getSubimage(156, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(156+buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(156+2*buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(156+3*buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		buttonNamesBeschwichtigen[i], Ressources.ZEROPOS.width+12+i*90, Ressources.ZEROPOS.height-66);
	        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
	        buttons.put(buttonNamesBeschwichtigen[i], button);
        }
        
        //Buttons Spionage
        //Hauptbutton unten
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(420, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(420+buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(420+2*buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(420+3*buttonSize, 0, buttonSize, buttonSize),
        		"aktionenSpionage", Ressources.ZEROPOS.width+102, Ressources.ZEROPOS.height+642);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("aktionenSpionage", button);
        
        //Aktionsbuttons
        String[] buttonNamesSpionage = { "aktionWanze", "aktionKamera", "aktionHacken", "aktionFernglas", "aktionParkSpionage" };
        for(int i = 0; i < buttonNamesSpionage.length; i++) {
	        button = new Button(control,
	        		Ressources.ingamebutton.getSubimage(420, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(420+buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(420+2*buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		Ressources.ingamebutton.getSubimage(420+3*buttonSize, (i+1)*buttonSize, buttonSize, buttonSize),
	        		buttonNamesSpionage[i], Ressources.ZEROPOS.width+12+i*90, Ressources.ZEROPOS.height+720);
	        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
	        buttons.put(buttonNamesSpionage[i], button);
        }
        
        //Nach Hause Button
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(684, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+2*buttonSize, 0, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+3*buttonSize, 0, buttonSize, buttonSize),
        		"aktionNachhause", Ressources.ZEROPOS.width+192, Ressources.ZEROPOS.height+642);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("aktionNachhause", button);
        
        //Nach Razzia Button
        button = new Button(control,
        		Ressources.ingamebutton.getSubimage(684, buttonSize, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+buttonSize, buttonSize, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+2*buttonSize, buttonSize, buttonSize, buttonSize),
        		Ressources.ingamebutton.getSubimage(684+3*buttonSize,  buttonSize, buttonSize, buttonSize),
        		"aktionRazzia", Ressources.ZEROPOS.width+282, Ressources.ZEROPOS.height+642);
        layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("aktionRazzia", button);
        
        // --------
        //Buttons Ende
        // --------
        
        
      //Tag malen
        String s = "Tag " + this.simulation.getSpiel_tag();
        tag.setText(s);
        tag.setBounds(1004+Ressources.ZEROPOS.width, 636+Ressources.ZEROPOS.height, 100, 37);
        tag.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tag.setFont(new Font("Corbel",Font.BOLD,30));
        tag.setForeground(new java.awt.Color(249, 249, 249));
        tag.setVisible(true);
        layeredPane.add(tag, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
      	//Uhr malen
        s = this.simulation.getSpielzeit_as_string();
        zeit.setText(s);
        zeit.setBounds(1010+Ressources.ZEROPOS.width, 669+Ressources.ZEROPOS.height, 100, 37);
        zeit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        zeit.setFont(new Font("Corbel",Font.BOLD,40));
        zeit.setForeground(new java.awt.Color(249, 249, 249));
        zeit.setVisible(true);
        layeredPane.add(zeit, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
      //Debugging: Misstrauen in der Stra�e
        s = "46.7%";
        misstrauen_in_street.setText(s);
        misstrauen_in_street.setBounds(713+Ressources.ZEROPOS.width, 638+Ressources.ZEROPOS.height, 183, 37);
        misstrauen_in_street.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        misstrauen_in_street.setFont(new Font("Corbel",Font.BOLD,16));
        misstrauen_in_street.setForeground(new java.awt.Color(249, 249, 249));
        misstrauen_in_street.setVisible(true);
        layeredPane.add(misstrauen_in_street, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Debugging: �berwachung in der Stra�e
        s = "84.6%";
        ueberwachung_in_street.setText(s);
        ueberwachung_in_street.setBounds(713+Ressources.ZEROPOS.width, 677+Ressources.ZEROPOS.height, 183, 37);
        ueberwachung_in_street.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ueberwachung_in_street.setFont(new Font("Corbel",Font.BOLD,16));
        ueberwachung_in_street.setForeground(new java.awt.Color(249, 249, 249));
        ueberwachung_in_street.setVisible(true);
        layeredPane.add(ueberwachung_in_street, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Ingame Men�bars
        menubar.setIcon(new ImageIcon(Ressources.menubars));
        menubar.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height,Ressources.MAPWIDTH,Ressources.MAPHEIGHT);
        layeredPane.add(menubar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        
        
        
        this.initialize_humans();
        
        
        
        //TODO Dynamic map load
        
        //Karte laden
        karte = new Map("russland",this.humans.get(this.humans.size()-1).get_haus_id());//Agent steht an letzter Stelle
        karte.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, 1125, 720);
        layeredPane.add(karte, javax.swing.JLayeredPane.DEFAULT_LAYER);

        
        
        //Sven
        InitHousebuttons();        
        
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.width, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, Ressources.SCREEN.height, Short.MAX_VALUE)
        );
        
        setUndecorated(true);
        setBackground(Color.black);
        pack();
    }
   
    
	//Beschwerden an Miri
	private void initialize_humans(){  
		//jeweils dem Agenten und dem Terroristen eine Hausnummer generieren
		//und daf�r sorgen, dass sie ungleich sind
		int house_of_terrorist = (int)(Math.random()*(Ressources.NUMBERHOUSES));
		int agent_house_nr = (int)(Math.random()*(Ressources.NUMBERHOUSES));
		if(agent_house_nr == house_of_terrorist){
			if(house_of_terrorist == 8)
				house_of_terrorist--;
			else
				house_of_terrorist++;
		}
			
		int people_per_house; 
		int number_of_adults;
		int number_of_children;
		
		//erster Erscheinungspunkt einer Person in einem Haus --> jedes Haus hat einen
		int spawnHausX[] = new int[Ressources.NUMBERHOUSES];
		int spawnHausY[] = new int[Ressources.NUMBERHOUSES];
		
		//alle Erscheinungspunkte der Personen in einem Haus
		int spawnPersonX[] = new int[4];
		int spawnPersonY[] = new int[4];
		
		int familien_cnt = 0;	//wie viele Menschen in einem Haus schon generiert wurden
		int mensch_cnt = 0;		//wie viele Menschen insgesamt generiert wurden
		Mensch mensch;			//Hilfsvariable zum Zwischenspeichern

		//Spawnpunkte initialisieren
		//immer linke obere Ecke in einem Haus
		ArrayList<ArrayList<String>> location_raster = Ressources.getLocation_ids();
		for(int haus=0;haus<Ressources.NUMBERHOUSES;haus++){	//f�r jedes Haus die Location in der Map herausfinden
			for(int i=0;i<location_raster.size();i++){	//y-Achse
				for(int j=0;j<location_raster.get(i).size();j++){	//x-Achse
					//das erste 45er-Pixel, das man von dem Haus findet, wird der erste Spawnpunkt
					if(location_raster.get(i).get(j).charAt(0) ==("" + (haus+1)).charAt(0)){	
						spawnHausX[haus] =  j*Ressources.RASTERHEIGHT+Ressources.ZEROPOS.width-2*Ressources.RASTERHEIGHT;
						spawnHausY[haus] =  i*Ressources.RASTERHEIGHT+Ressources.ZEROPOS.height-2*Ressources.RASTERHEIGHT;
					}
						
				}
			}
		}
		
		for(int i=0;i<Ressources.NUMBERHOUSES;i++){ //f�r jedes Haus die Familie erstellen
			familien_cnt = 0;
			//f�r ein Haus die Spawnpunkte festlegen
			spawnPersonX[0] = spawnHausX[i];
			spawnPersonY[0] = spawnHausY[i];
			spawnPersonX[1] = spawnHausX[i] + Ressources.RASTERHEIGHT;
			spawnPersonY[1] = spawnHausY[i];
			spawnPersonX[2] = spawnHausX[i] + 2*Ressources.RASTERHEIGHT;
			spawnPersonY[2] = spawnHausY[i];
			spawnPersonX[3] = spawnHausX[i];
			spawnPersonY[3] = spawnHausY[i] + Ressources.RASTERHEIGHT;
			
			//Agent wird als letztes hinzugef�gt
			if(i!=agent_house_nr){
				people_per_house = (int)(Math.random()*(4))+1; //wie viele Menschen in einem Haus wohnen sollen
				if(i == house_of_terrorist){
					//Terrorist muss kann kein Kind sein
					number_of_adults = 1 + (int)(Math.random()*(people_per_house));
					mensch = new Terrorist(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(spawnPersonX[familien_cnt],spawnPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;
					
					//�brige Erwachsene setzen
					for(int j=0;j<number_of_adults-1;j++){
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(spawnPersonX[familien_cnt],spawnPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				}
				else{
					//in jedem Haushalt muss mindestens ein Erwachsener leben
					number_of_adults =  (int)(Math.random()*(people_per_house))+1;
					for(int j=0;j<number_of_adults;j++){
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(spawnPersonX[familien_cnt],spawnPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				}
				//Kinder berechnen
				number_of_children = people_per_house - number_of_adults;
				for(int j=0;j<number_of_children;j++){
					mensch = new Kinder(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(spawnPersonX[familien_cnt],spawnPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;
				}
				this.initialize_house(i,false,spawnHausX[0],spawnHausY[0]);
			}	
		}
		
		//Simulation ben�tigt die Information von allen Bewohnern (ohne Agent)
		for(int i=0;i<this.humans.size();i++){
			this.simulation.set_people((Person)this.humans.get(i));
		}
		
		//Agent hinzuf�gen
		spawnPersonX[0] = spawnHausX[agent_house_nr];
		spawnPersonY[0] = spawnHausY[agent_house_nr];
		mensch = new Agent(agent_house_nr);
		this.humans.add(mensch);
		layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
		this.humans.get(mensch_cnt).teleport(spawnPersonX[0],spawnPersonY[0]);
		this.humans.get(mensch_cnt).setHomePosX(spawnPersonX[0]);
		this.humans.get(mensch_cnt).setHomePosY(spawnPersonY[0]);
		this.simulation.set_agent((Agent)mensch);
		this.initialize_house(agent_house_nr,true,spawnPersonX[0],spawnPersonY[0]);
	}
	
	//Beschwerden Miri
	private void initialize_house(int hausnr, boolean agentenhaus, int x, int y){
		haus = new Haus(hausnr,agentenhaus,x,y);
		this.simulation.setHouses(haus);
	}
    
    //Beschwerden an Miri
    //"MAIN"
   /* public void start(){
    	 int delay = 40;
    	  Timer t = new Timer(delay, new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	      //GUILayer.actionPerformed(e);
    	    }
    	  });
    	  t.start();
    }*/
	
	
	//Beschwerden an Miri
	void update_location_id(){
		ArrayList<ArrayList<String>> location_raster = Ressources.getLocation_ids();
		int x;
		int y;
		for(int i=0;i<this.humans.size();i++){
			x = (this.humans.get(i).getPosX()+2*Ressources.RASTERHEIGHT-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT - 2;
			y = (this.humans.get(i).getPosY()+2*Ressources.RASTERHEIGHT-Ressources.ZEROPOS.height)/Ressources.RASTERHEIGHT - 2;
			this.humans.get(i).set_location_id(location_raster.get(y).get(x).charAt(0));
		}	
		
	}
	
	//Beschwerden Miri
	public void updateTime(){
		//Tag malen
        String s = "Tag " + this.simulation.getSpiel_tag();
        tag.setText(s);
        
      	//Uhr malen
        s = this.simulation.getSpielzeit_as_string();
        zeit.setText(s);
	}
	
	//Beschwerden Miri
	public void updateMisstrauen(){
		String s = (float)Math.round(this.simulation.calc_misstrauen_in_street()*100)/100 + "%";
        misstrauen_in_street.setText(s);
	}
	
	//Beschwerden Miri
	public void updateUeberwachung(){
		String s = (float)Math.round(this.simulation.calc_ueberwachung_in_street()*100)/100 + "%";
		ueberwachung_in_street.setText(s);
	}
	
    //Beschwerden an Miri
	//adopted by Tobias
	public void step(){
		this.update_location_id();
		

		// zeichne neuen �berwachungswert
		this.updateUeberwachung();

		if(stepcounter%25==0){ //Aufruf alle 25 steps
			simulation.calculate_misstrauen();
			simulation.calc_misstrauen_in_street();
			for(int i=0;i<this.humans.size();i++){
				if(this.humans.get(i) instanceof Person){
					((Person)this.humans.get(i)).update_schatten();
				}
			}
			this.updateMisstrauen(); //Wert neu zeichnen
		}
		
		if (stepcounter%10==0){ // Aufruf alle 4 steps
			simulation.calc_spielzeit();
			this.updateTime(); //Zeiche Spiel
			simulation.tagesablauf();
		}
		
		//Update Persons
		for(int i=0;i<this.humans.size();i++){
			this.humans.get(i).step();
		}
		
		stepcounter++;
	}
	
	//kreiert vom unglaublichen Stephan
	//Pausiert das Spiel oder setzt es fort
	public void updateTimerStatus() {
		if(timer.isRunning())
			timer.stop();
		else
			timer.start();
	}
	private void InitHousebuttons() { //Sven
		Button button;
		Integer i;
		Integer j;
		Integer x;
		Integer b;
		b=0;
		for (x=1; x <= 9;x++ ){
			b=0;
			if (b==0){
			for (j=0; j<Ressources.getLocation_ids().size();j++){
				if(b==0){
				for (i=0; i<Ressources.getLocation_ids().get(0).size();i++){
					if ((Ressources.getLocation_ids().get(j).get(i).equals(String.valueOf(x))&&(b==0))){
						b=1;
						button = new Button(control , Ressources.RASTERHEIGHT*3, Ressources.RASTERHEIGHT*3, "Haus"+String.valueOf(x), Ressources.ZEROPOS.width+i*Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height+j*Ressources.RASTERHEIGHT);
						layeredPane.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
						buttons.put("Haus"+String.valueOf(x), button);
			        
					}
				}
				
			}
			}
			}
		}
		//for (i=0, i< Ressources.getLocation.)
		//Ressources.getLocation_ids().get(i).get(j);
		//invis Buttons
	//	button = new Button(control , Ressources.RASTERHEIGHT*3, Ressources.RASTERHEIGHT*3, "Haus1", Ressources.ZEROPOS.width+Ressources.RASTERHEIGHT, Ressources.ZEROPOS.height+Ressources.RASTERHEIGHT);
		
		
	
	}
	
}
