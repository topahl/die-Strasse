package com.stalkindustries.main.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

/** 
 * @author Tobias
 *	Spezieller Renderer um die geordnete Anzeige von Icons und diversen Textelementen in einer JList zu erm�glichen.
 *	Hier speziell f�r die Highscoretabelle. 
 */
public class HighscoreTableRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -7799441088157759804L;
    private JLabel nummer;
    private JLabel land;
    private JLabel score;
    private JLabel user;
    private JLabel date;
    private JPanel pane;
    private Font font = new Font("Corbel",Font.BOLD,20);
    private Color textSelectionColor = new Color(0xf9, 0xf9, 0xf9);
    private Color backgroundSelectionColor = new Color(0x2d,0x7c,0x9b);
    private Color textNonSelectionColor = new Color(0xf9, 0xf9, 0xf9);
    private Color backgroundNonSelectionColor = new Color(0,0,0,0);
    HashMap<String,BufferedImage> icons= new HashMap<String,BufferedImage>();
    
    private static final int LISTELEMENTHEIGHT = 40;

    public HighscoreTableRenderer() {
 	
    	pane= new JPanel();
    	
        nummer = new JLabel();
        nummer.setOpaque(true);
        nummer.setFont(font);
        land = new JLabel();
        land.setOpaque(true);
        land.setFont(font);
        score = new JLabel();
        score.setOpaque(true);
        score.setFont(font);
        user = new JLabel();
        user.setOpaque(true);
        user.setFont(font);
        date = new JLabel();
        date.setOpaque(true);
        date.setFont(font);
        GroupLayout paneLayout = new GroupLayout(pane);
        pane.setLayout(paneLayout);
        paneLayout.setHorizontalGroup(
        		paneLayout.createParallelGroup(Alignment.LEADING)
        			.addGroup(paneLayout.createSequentialGroup()
            				.addComponent(nummer,45,45,45)
            				.addComponent(land,225,225,225)
            				.addComponent(score,95,95,95)
            				.addComponent(user,180,180,180)
            				.addComponent(date,120,120,120))
        );
        paneLayout.setVerticalGroup(
        		paneLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(paneLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(nummer,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
        				.addComponent(land,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
        				.addComponent(score,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
        				.addComponent(user,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
        				.addComponent(date,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT))
        );
    }

    /**
     * Setzt die Hashmap f�r den Zugriff auf die Flaggen Icons.
     * @param icons Hashmap mit Flaggen Icons
     * @author Tobias
     */
    public void setIcons(HashMap<String,BufferedImage> icons){
    	this.icons = icons;
    }
    

    /**
     * @author Tobias
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })//Notwendig um eine Kompatibilit�t f�r JDK 1.6 & 1.7 zu erm�glichen
	@Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {
    	
    	//Daten der verschiedenen Textelemente eintragen
    	ArrayList<String> values = (ArrayList<String>) value;
        nummer.setText((index+1)+".");
        this.setColors(nummer, selected);
        
        land.setIcon(new ImageIcon(icons.get(values.get(0))));
        this.setColors(land, selected);
        
        score.setText(values.get(1));
        this.setColors(score, selected);
        
        user.setText(values.get(2));
        this.setColors(user, selected);
        
        date.setText(values.get(3));
        this.setColors(date, selected);
        
        
        this.setColors(pane, selected);
        return pane;
    }
    
    
    /**
     * Setzt die Farbe der Elemente entsprechend der Selektion.
     * @param comp Komponente die eingef�rbt werden soll.
     * @param selected <i>true</i> wenn die Komponente ausgew�hlt ist, sonst <i>false</i>
     */
    private void setColors(JComponent comp, boolean selected){
    	if(selected){
    		comp.setBackground(backgroundSelectionColor);
        	comp.setForeground(textSelectionColor);
    	}
    	else{
    		comp.setBackground(backgroundNonSelectionColor);
            comp.setForeground(textNonSelectionColor);
    	}
        	
    }
}
