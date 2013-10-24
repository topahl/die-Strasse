/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;


/**
 *
 * @author Tobias
 */
public class GUILayer extends javax.swing.JFrame{

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Map karte;
	private JLayeredPane layeredPane;
	private boolean b = true; //TODO please rename  if needed
	private Simulation simulation = new Simulation();
	private Haus haus;//wird sp�ter initialisiert
	private ArrayList<Mensch> humans = new ArrayList<Mensch>();
	private static int misstrauens_counter = 0;
	private static int timer_counter = 0;
	private JLabel zeit = new JLabel();
	private JLabel tag = new JLabel();
	private JLabel misstrauen_in_street = new JLabel();
	private JLabel ueberwachung_in_street = new JLabel();
	
    public GUILayer() {
        initComponents();
        this.simulation.initialize_beziehungsmatrix();
        setVisible(true);
        this.timer = new Timer(Ressources.GAMESPEED,new OSTimer(this));
        timer.start();
    }
    
    //Liebesbriefe an Tobi
    private void initComponents() {
		karte = new Map("Russland");//TODO Dynamic Map Load
        layeredPane = new javax.swing.JLayeredPane();
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        
        this.initialize_humans();
        
        //Tag malen
        String s = "Tag " + this.simulation.getSpiel_tag();
        tag.setText(s);
        tag.setBounds(920+Ressources.ZEROPOS.width, 636+Ressources.ZEROPOS.height, 183, 37);
        tag.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tag.setFont(new Font("Corbel",Font.BOLD,30));
        tag.setForeground(new java.awt.Color(255, 255, 255));
        tag.setVisible(true);
        layeredPane.add(tag, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
      	//Uhr malen
        s = this.simulation.getSpielzeit_as_string();
        zeit.setText(s);
        zeit.setBounds(920+Ressources.ZEROPOS.width, 669+Ressources.ZEROPOS.height, 183, 37);
        zeit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        zeit.setFont(new Font("Corbel",Font.BOLD,40));
        zeit.setForeground(new java.awt.Color(255, 255, 255));
        zeit.setVisible(true);
        layeredPane.add(zeit, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Debugging: Misstrauen in der Stra�e
        s = "0.0%";
        misstrauen_in_street.setText(s);
        misstrauen_in_street.setBounds(720+Ressources.ZEROPOS.width, 675+Ressources.ZEROPOS.height, 183, 37);
        misstrauen_in_street.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        misstrauen_in_street.setFont(new Font("Corbel",Font.BOLD,40));
        misstrauen_in_street.setForeground(new java.awt.Color(255, 255, 255));
        misstrauen_in_street.setVisible(true);
        layeredPane.add(misstrauen_in_street, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Debugging: �berwachung in der Stra�e
        s = "0.0%";
        ueberwachung_in_street.setText(s);
        ueberwachung_in_street.setBounds(720+Ressources.ZEROPOS.width, 637+Ressources.ZEROPOS.height, 183, 37);
        ueberwachung_in_street.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ueberwachung_in_street.setFont(new Font("Corbel",Font.BOLD,40));
        ueberwachung_in_street.setForeground(new java.awt.Color(255, 255, 255));
        ueberwachung_in_street.setVisible(true);
        layeredPane.add(ueberwachung_in_street, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        karte.setBounds(Ressources.ZEROPOS.width, Ressources.ZEROPOS.height, 1125, 720);
        layeredPane.add(karte, javax.swing.JLayeredPane.DEFAULT_LAYER);

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
		int house_of_terrorist = (int)(Math.random()*(9));
		int agent_house_nr = (int)(Math.random()*(9));
		if(agent_house_nr == house_of_terrorist){
			if(house_of_terrorist == 8)
				house_of_terrorist--;
			else
				house_of_terrorist++;
		}
			
		int people_per_house;
		int number_of_adults;
		int number_of_children;
		int spornHausX[] = new int[9];
		int spornHausY[] = new int[9];
		int spornPersonX[] = new int[4];
		int spornPersonY[] = new int[4];
		int familien_cnt = 0;
		int mensch_cnt = 0;
		Mensch mensch;

		//Spornpunkte initialisieren
		ArrayList<ArrayList<String>> location_raster = Ressources.getLocation_ids();
		for(int haus=0;haus<9;haus++){
			for(int i=0;i<location_raster.size();i++){	//y-Achse
				for(int j=0;j<location_raster.get(i).size();j++){	//x-Achse
					if(location_raster.get(i).get(j).charAt(0) ==("" + (haus+1)).charAt(0)){	
						spornHausX[haus] =  j*Ressources.RASTERHEIGHT+Ressources.ZEROPOS.width-2*Ressources.RASTERHEIGHT;
						spornHausY[haus] =  i*Ressources.RASTERHEIGHT+Ressources.ZEROPOS.height-2*Ressources.RASTERHEIGHT;
					}
						
				}
			}
		}
		
		for(int i=0;i<9;i++){ 
			familien_cnt = 0;
			//f�r ein Haus die Spornpunkte festlegen
			spornPersonX[0] = spornHausX[i];
			spornPersonY[0] = spornHausY[i];
			spornPersonX[1] = spornHausX[i] + Ressources.RASTERHEIGHT;
			spornPersonY[1] = spornHausY[i];
			spornPersonX[2] = spornHausX[i] + 2*Ressources.RASTERHEIGHT;
			spornPersonY[2] = spornHausY[i];
			spornPersonX[3] = spornHausX[i];
			spornPersonY[3] = spornHausY[i] + Ressources.RASTERHEIGHT;
			
			if(i!=agent_house_nr){
				people_per_house = (int)(Math.random()*(4))+1;
				if(i == house_of_terrorist){
					number_of_adults = 1 + (int)(Math.random()*(people_per_house));
					mensch = new Terrorist(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(spornPersonX[familien_cnt],spornPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(spornPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(spornPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;
					//this.simulation.set_people(new Terrorist(i));
					for(int j=0;j<number_of_adults-1;j++){
						//this.simulation.set_people(new Erwachsene(i));
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(spornPersonX[familien_cnt],spornPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(spornPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(spornPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				}
				else{
					number_of_adults =  (int)(Math.random()*(people_per_house))+1;
					for(int j=0;j<number_of_adults;j++){
						//this.simulation.set_people(new Erwachsene(i));
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
						this.humans.get(mensch_cnt).teleport(spornPersonX[familien_cnt],spornPersonY[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosX(spornPersonX[familien_cnt]);
						this.humans.get(mensch_cnt).setHomePosY(spornPersonY[familien_cnt]);
						mensch_cnt++;
						familien_cnt++;
					}
				}
				number_of_children = people_per_house - number_of_adults;
				for(int j=0;j<number_of_children;j++){
					//this.simulation.set_people(new Kinder(i));
					mensch = new Kinder(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.humans.get(mensch_cnt).teleport(spornPersonX[familien_cnt],spornPersonY[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosX(spornPersonX[familien_cnt]);
					this.humans.get(mensch_cnt).setHomePosY(spornPersonY[familien_cnt]);
					mensch_cnt++;
					familien_cnt++;
				}
				this.initialize_house(i,false,spornHausX[0],spornHausY[0]);
			}	
		}
		
		for(int i=0;i<this.humans.size();i++){
			this.simulation.set_people((Person)this.humans.get(i));
		}
		
		//Agent hinzuf�gen
		spornPersonX[0] = spornHausX[agent_house_nr];
		spornPersonY[0] = spornHausY[agent_house_nr];
		mensch = new Agent();
		this.humans.add(mensch);
		layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
		//this.simulation.set_agent(new Agent());
		//this.humans.add(this.simulation.get_agent());
		this.humans.get(mensch_cnt).teleport(spornPersonX[0],spornPersonY[0]);
		this.humans.get(mensch_cnt).setHomePosX(spornPersonX[0]);
		this.humans.get(mensch_cnt).setHomePosY(spornPersonY[0]);
		this.simulation.set_agent((Agent)mensch);
		this.initialize_house(agent_house_nr,true,spornPersonX[0],spornPersonY[0]);
	}
	
	
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
	
	
	public void updateTime(){
		//Tag malen
        String s = "Tag " + this.simulation.getSpiel_tag();
        tag.setText(s);
        
      	//Uhr malen
        s = this.simulation.getSpielzeit_as_string();
        zeit.setText(s);
	}
	
	public void updateMisstrauen(){
		String s = this.simulation.calc_misstrauen_in_street() + "%";
        misstrauen_in_street.setText(s);
	}
	
	public void updateUeberwachung(){
		String s = this.simulation.calc_ueberwachung_in_street() + "%";
		ueberwachung_in_street.setText(s); 
	}
	
    
	public void step(){
		this.update_location_id();
		this.updateMisstrauen();
		this.updateUeberwachung();
		if(misstrauens_counter==25)
			misstrauens_counter = 0;
		if(misstrauens_counter==0){
			simulation.calculate_misstrauen();
			simulation.calc_misstrauen_in_street();
		}
		misstrauens_counter++;
		
		if (timer_counter==4)
			timer_counter=0;
		if (timer_counter==0){
			simulation.calc_spielzeit();
			this.updateTime();
			simulation.tagesablauf();
		}
		timer_counter++;
		
		for(int i=0;i<this.humans.size();i++){
			this.humans.get(i).step();
		}
	}
}
