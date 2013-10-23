/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	private Person testperson;
	private JLayeredPane layeredPane;
	private boolean b = true; //TODO please rename  if needed
	private Simulation simulation = new Simulation();
	private Dimension screen;  //Screen resolution
	private Dimension zeropos; //Koordinatenverschiebung auf Bildschirms
	private Haus haus;
	private ArrayList<Mensch> humans = new ArrayList<Mensch>();
	
    public GUILayer() {
        initComponents();
        setVisible(true);
        this.timer = new Timer(40,new OSTimer(this));
        timer.start();
    }
    
    //Liebesbriefe an Tobi
    private void initComponents() {
    	screen = Toolkit.getDefaultToolkit().getScreenSize();//Screen resolution
		karte = new Map("Russland");//TODO Dynamic Map Load
		
		zeropos = new Dimension();	//zeropos berechnen -> Koordinatenverschiebung
		zeropos.setSize((screen.getWidth()/2)-(karte.getWidth()/2),(screen.getHeight()/2)-(karte.getHeight()/2));
    	
        layeredPane = new javax.swing.JLayeredPane();
        testperson = new Erwachsene(1);
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        
        this.initialize_humans();
        
        testperson.setLocation(90, 90);
        layeredPane.add(testperson, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        karte.setBounds(zeropos.width, zeropos.height, 1125, 720);
        layeredPane.add(karte, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, screen.width, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, screen.height, Short.MAX_VALUE)
        );
        
        setUndecorated(true);
        setBackground(Color.black);
        pack();
    }
   
    
	//Beschwerden an Miri
	private void initialize_humans(){  
		int house_of_terrorist = (int)(Math.random()*(9));
		int people_per_house;
		int number_of_adults;
		int number_of_children;
		int agent_house_nr = (int)(Math.random()*(9));
		Mensch mensch;
		
		for(int i=0;i<9;i++){ 
			if(i!=agent_house_nr){
				people_per_house = (int)(Math.random()*(4))+1;
				if(i == house_of_terrorist){
					number_of_adults = 1 + (int)(Math.random()*(people_per_house));
					mensch = new Terrorist(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					//this.simulation.set_people(new Terrorist(i));
					for(int j=0;j<number_of_adults-1;j++){
						//this.simulation.set_people(new Erwachsene(i));
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					}
				}
				else{
					number_of_adults =  (int)(Math.random()*(people_per_house))+1;
					for(int j=0;j<number_of_adults;j++){
						//this.simulation.set_people(new Erwachsene(i));
						mensch = new Erwachsene(i);
						this.humans.add(mensch);
						layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
					}
				}
				number_of_children = people_per_house - number_of_adults;
				for(int j=0;j<number_of_children;j++){
					//this.simulation.set_people(new Kinder(i));
					mensch = new Kinder(i);
					this.humans.add(mensch);
					layeredPane.add(mensch, javax.swing.JLayeredPane.DEFAULT_LAYER);
				}
				this.initialize_house(i,false);
			}	
		}
		/*//humans befüllen mit Menschen, Kindern und Agenten
		for(int i=0;i<this.simulation.get_people().size();i++){
			this.humans.add(this.simulation.get_people().get(i));
		}*/
		
		for(int i=0;i<this.humans.size();i++){
			this.simulation.set_people((Person)this.humans.get(i));
		}
		
		//Agent hinzufügen
		this.simulation.set_agent(new Agent());
		this.humans.add(this.simulation.get_agent());
		this.initialize_house(agent_house_nr,true);
	}
	
	
	private void initialize_house(int hausnr, boolean agentenhaus){
		int x=0;
		int y=0;
		haus = new Haus(hausnr,agentenhaus,x,y);
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
    
	public void step(){
		this.simulation.calculate_misstrauen();
		this.simulation.calc_misstrauen_in_street();
		System.out.println("pupsi");
	}
}
