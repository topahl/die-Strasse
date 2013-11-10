package com.stalkindustries.main.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class PersHighscoreTableRenderer extends DefaultListCellRenderer {
	  private static final long serialVersionUID = -7799441088157759804L;
	    private JLabel land;
	    private JLabel score;
	    private JLabel date;
	    private JPanel pane;
	    private Font font = new Font("Corbel",Font.BOLD,20);
	    private Color textSelectionColor =  new Color(0xf9, 0xf9, 0xf9);
	    private Color backgroundSelectionColor = new Color(0x2d,0x7c,0x9b);
	    private Color textNonSelectionColor = new Color(0xf9, 0xf9, 0xf9);
	    private Color backgroundNonSelectionColor = new Color(0,0,0,0);
	    HashMap<String,BufferedImage> icons= new HashMap<String,BufferedImage>();
	    
	    private static final int LISTELEMENTHEIGHT = 40;

	    public PersHighscoreTableRenderer() {
	 	
	    	pane= new JPanel();
	    	
	        land = new JLabel();
	        land.setOpaque(true);
	        land.setFont(font);

	        score = new JLabel();
	        score.setOpaque(true);
	        score.setFont(font);
	        date = new JLabel();
	        date.setOpaque(true);
	        date.setFont(font);
	        GroupLayout paneLayout = new GroupLayout(pane);
	        pane.setLayout(paneLayout);
	        paneLayout.setHorizontalGroup(
	        		paneLayout.createParallelGroup(Alignment.LEADING)
	        			.addGroup(paneLayout.createSequentialGroup()
	        				.addComponent(land,45,45,45)
	        				.addComponent(score,100,100,100)
	        				.addComponent(date,150,150,150))
	        );
	        paneLayout.setVerticalGroup(
	        		paneLayout.createParallelGroup(Alignment.LEADING)
	        		.addGroup(paneLayout.createParallelGroup(Alignment.BASELINE)
	        				.addComponent(land,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
	        				.addComponent(score,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT)
	        				.addComponent(date,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT,LISTELEMENTHEIGHT))
	        );
	    }

	    
	    public void addIcons(HashMap<String,BufferedImage> icons){
	    	this.icons = icons;
	    }
	    
	    @Override
	    public Component getListCellRendererComponent(
	            JList list,
	            Object value,
	            int index,
	            boolean selected,
	            boolean expanded) {
	    		
	    	ArrayList<String> values = (ArrayList<String>) value;
	        
	        land.setIcon(new ImageIcon(icons.get(values.get(9))));
	        this.setColors(land, selected);
	        
	        score.setText(values.get(2));
	        this.setColors(score, selected);
	        
	        date.setText(values.get(0));
	        this.setColors(date, selected);
	        
	        
	        
	        this.setColors(pane, selected);

	        return pane;
	    }
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
