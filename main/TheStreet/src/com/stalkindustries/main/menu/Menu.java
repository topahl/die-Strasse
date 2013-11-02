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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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

import com.stalkindustries.main.Button;
import com.stalkindustries.main.game.Ressources;

public class Menu extends JFrame implements MouseMotionListener{
	private JLayeredPane window;
	private JLayeredPane mainmenu;
	private JLayeredPane mapselect;
	private JLayeredPane profilselect;
	private JLayeredPane tutorial;
	private JLayeredPane highscore;
	private ControlMenu control;
	
	
	private JTextField username;
	
	public static final int LAYERMENU = 1;
	public static final int LAYERLEVEL = 2;
	public static final int LAYERPROFIL = 3;
	public static final int LAYERHIGHSCORE = 4;
	public static final int LAYERTUTORIAL = 5;
	
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
        
        initProfilMenu();
        
        initHighscore();
        
        initLevelSelect();
        
        initTutorial();
        
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
        
		
		
		
        showLayer(LAYERMENU);
        
        
        pack();
        
      
    }
	
	private void initTutorial(){
		tutorial = new JLayeredPane();
		
		generateStandartSubPageElements(tutorial, "Anleitung");
	}
	
	private void initProfilMenu(){
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
		label.setIcon(new ImageIcon(Ressources.menubutton.getSubimage(360, 0, 315, 66)));
		label.setBounds(450, 330, 315, 66);
		profilselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		Button button = new Button(this.control,
				Ressources.menubutton.getSubimage(0,603, 315,70),
				Ressources.menubutton.getSubimage(315, 603, 313,70),
				Ressources.menubutton.getSubimage(2 * 315, 603, 315,70),
				Ressources.menubutton.getSubimage(3 * 315, 603, 315,70),
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
        beschreibung.setText("Sie haben noch keinen Nutzernamen? Legen Sie hier einen neuen an. \n \n \nGew�nschter Nutzername");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(450,200, 350, 150);
        profilselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        //Benutzerauswahl
        JList<String> list = new JList<String>();
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setOpaque(false);
        list.setBackground(new Color(0,0,0,0));
        list.setFont(new Font("Corbel",Font.BOLD,20));
        JScrollPane scrollpane = new Scrollbar(this.control); 
        scrollpane.setViewportView(list);
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
		label.setText("Benutzer w�hlen");
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
				Ressources.menubutton.getSubimage(0,671, 315,70),
				Ressources.menubutton.getSubimage(315, 671, 313,70),
				Ressources.menubutton.getSubimage(2 * 315, 671, 315,70),
				Ressources.menubutton.getSubimage(3 * 315, 671, 315,70),
				"use", 45, 600, this);
	    profilselect.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		generateStandartSubPageElements(profilselect, "Benutzer");
        
	}
	
	private void initMainMenu(){
		mainmenu = new javax.swing.JLayeredPane();
		JLabel currentscreentext = new JLabel();
        currentscreentext.setText("Hauptmen�");
        currentscreentext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        currentscreentext.setFont(new Font("Corbel",Font.BOLD,32));
        currentscreentext.setForeground(new Color(0x1f, 0x1f, 0x1f));
        currentscreentext.setBounds(45,29, 200, 37);
        mainmenu.add(currentscreentext, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
        Button button;
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 136, 370, 80), Ressources.menubutton.getSubimage(370, 136, 370, 80), Ressources.menubutton.getSubimage(740, 136, 370, 80), Ressources.menubutton.getSubimage(1110, 136, 370, 80), "start", 70,140, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("start", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 136+80, 370, 80), Ressources.menubutton.getSubimage(370, 136+80, 370, 80), Ressources.menubutton.getSubimage(740, 136+80, 370, 80), Ressources.menubutton.getSubimage(1110, 136+80, 370, 80), "tutorial",70, 230, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("tutorial", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 136+160, 370, 80), Ressources.menubutton.getSubimage(370, 136+160, 370, 80), Ressources.menubutton.getSubimage(740, 136+160, 370, 80), Ressources.menubutton.getSubimage(1110, 136+160, 370, 80), "profil", 70, 320, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("profil", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 136+240, 370, 80), Ressources.menubutton.getSubimage(370, 136+240, 370, 80), Ressources.menubutton.getSubimage(740, 136+240, 370, 80), Ressources.menubutton.getSubimage(1110, 136+240, 370, 80), "highscore", 70, 410, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("highscore", button);
        
        button=new Button(control, Ressources.menubutton.getSubimage(0, 136+320, 370, 80), Ressources.menubutton.getSubimage(370, 136+320, 370, 80), Ressources.menubutton.getSubimage(740, 136+320, 370, 80), Ressources.menubutton.getSubimage(1110, 136+320, 370, 80), "beenden", 70, 500, this);
        mainmenu.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
        buttons.put("beenden", button);
        
        JLabel main = new JLabel();
        main.setIcon(new ImageIcon(Ressources.mainmenu));
        main.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        mainmenu.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        mainmenu.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(mainmenu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
	}
	
	private void initHighscore(){
		highscore = new JLayeredPane();
		
		generateStandartSubPageElements(highscore, "Highscore");
	}
	
	private void initLevelSelect(){
		mapselect = new JLayeredPane();
		
		
        
                
        JTextArea beschreibung = new JTextArea();
        beschreibung.setColumns(20);
        beschreibung.setLineWrap(true);
        beschreibung.setRows(5);
        beschreibung.setText("W�hlen Sie das Land, welches in diesem Spiel simuliert werden soll.");
        beschreibung.setWrapStyleWord(true);
        beschreibung.setFocusCycleRoot(true);
        beschreibung.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        beschreibung.setFocusable(false);
        beschreibung.setOpaque(false);
        beschreibung.setFont(new Font("Corbel",Font.BOLD,20));
        beschreibung.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
        beschreibung.setBounds(850,270, 250, 200);
        mapselect.add(beschreibung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        ArrayList<String> levels=readAvaidableLevel();
        for(int i=0;i<levels.size();i++){
        	if(i>3)
        		break;
        	try {
    			BufferedImage loader = ImageIO.read(new File("res\\level\\"+levels.get(i)+"\\"+levels.get(i)+"_slice_menu.png"));
    			BufferedImage iconnormal = new BufferedImage(312,134, BufferedImage.TYPE_INT_ARGB);
    			BufferedImage iconhover = new BufferedImage(312,134, BufferedImage.TYPE_INT_ARGB);
    			Graphics2D g2d = iconhover.createGraphics();
    			g2d.drawImage(loader.getSubimage(0, 0, 312, 134), 0, 0, null);
    			g2d.drawImage(Ressources.menubutton.getSubimage(0, 0, 312, 135), 0, 0, null);
    			g2d.dispose();
    			
    			g2d = iconnormal.createGraphics();
    			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    			g2d.drawImage(loader.getSubimage(0, 0, 312, 134), 0, 0, null);
    			g2d.drawImage(Ressources.menubutton.getSubimage(0, 0, 312, 135), 0, 0, null);
    			g2d.dispose();
    			
    			Button button = new Button(control,iconnormal,iconhover,iconnormal,iconnormal,"level:"+levels.get(i), 45+((i/2)*360),180+((i%2)*200), this);
    			mapselect.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
    			buttons.put(levels.get(i), button);
    			
    			JLabel label= new JLabel();
    			BufferedImage textlabel = loader.getSubimage(315, 9, loader.getWidth()-315, 26);
    			g2d = textlabel.createGraphics();
    			g2d.drawImage(Ressources.menubutton.getSubimage(315, 9, 37, 26), 0, 0, null);
    			label.setIcon(new ImageIcon(textlabel));
    			label.setBounds(45+((i/2)*360),145+((i%2)*200), loader.getWidth()-315, 25);
    			mapselect.add(label, javax.swing.JLayeredPane.DEFAULT_LAYER);	
    		} catch (IOException e) {
    			System.err.println("Could not find Image "+levels.get(i)+"_slice_menu.png");
    			e.printStackTrace();
    		}
		}
        generateStandartSubPageElements(mapselect, "Levelauswahl");
    }
	
	
	//Support Tiki
	public void mouseMoved(MouseEvent e) {	
		//mousefollower.setLocation(e.getX()-15, e.getY()-15);
	}
    public void mouseDragged(MouseEvent e) {} //do nothing, notwendig f�r implements MouseMotion
    
    
    
    /**
     * Blendet den ausgew�lten layer ein und alle anderen aus
     * @param layernummer Nummer des layers der angezeigt werden soll
     */
    public void showLayer(int layernummer){
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
    }
    
    
    /**
     * �berpr�ft ob alle daten f�r ein Level vorhanden sind
     * 
     * @param folder ein Ordner welcher ein level beinhalten soll
     * @return true = Level ist ok 
     */
    private boolean isValidLevel(File folder){
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
    
    
    private void generateStandartSubPageElements(JLayeredPane subpage, String titel){
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
        
		JLabel main = new JLabel();
		main.setIcon(new ImageIcon(Ressources.mainmenusub));
        main.setBounds(0, 0, Ressources.MAPWIDTH, Ressources.MAPHEIGHT);
        subpage.add(main, javax.swing.JLayeredPane.DEFAULT_LAYER);
        subpage.setBounds(Ressources.ZEROPOS.width,Ressources.ZEROPOS.height , Ressources.MAPWIDTH, Ressources.MAPWIDTH);
        window.add(subpage, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
    }
    
    /**
     * Lie�t alle verf�gbaren levels ein und �berpr�ft sie auf richtigkeit
     * 
     * @author Tobias
     * @return Liste aller validen Levelfiles
     */
    private ArrayList<String> readAvaidableLevel(){
    	File folder = new File("res\\level");
    	ArrayList<String> levels = new ArrayList<String>();
    	for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() && isValidLevel(fileEntry)) {
                levels.add(fileEntry.getName().trim());
                System.out.println(fileEntry.getName());
            } 
    	}
    	return levels;
    }
    
    public String getInputUsername(){
    	return username.getText();
    }
    
    
}
