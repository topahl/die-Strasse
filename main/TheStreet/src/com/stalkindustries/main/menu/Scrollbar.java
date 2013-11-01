package com.stalkindustries.main.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;

import com.stalkindustries.main.IControl;
import com.stalkindustries.main.game.Ressources;

public class Scrollbar extends JScrollPane {

public Scrollbar(IControl control){
    JScrollBar sb = this.getVerticalScrollBar();
    sb.setUI(new ScrollbarUI(control));
}



	static class ScrollbarUI extends MetalScrollBarUI {
    private Image imageThumb, imageTrack;
    private JButton up,down;
    ScrollbarUI(IControl control) {
        imageThumb = Ressources.menubutton.getSubimage(675, 62, 30, 56);
		up = new JButton();
		up.setIcon(new ImageIcon(Ressources.menubutton.getSubimage(675, 30, 30, 30)));
		down = new JButton();
		down.setIcon(new ImageIcon(Ressources.menubutton.getSubimage(675, 0, 30, 30)));
		
    }


    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {        
        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor( Color.blue );
        g.drawRect( 0, 0, thumbBounds.width - 2, thumbBounds.height - 1 );
        AffineTransform transform = AffineTransform.getScaleInstance((double)thumbBounds.width/imageThumb.getWidth(null),(double)thumbBounds.height/imageThumb.getHeight(null));
        ((Graphics2D)g).drawImage(imageThumb, transform, null);
        g.drawImage(Ressources.menubutton.getSubimage(675, 60, 30, 2), 0, 0, null);
        g.drawImage(Ressources.menubutton.getSubimage(675, 118, 30, 2), 0, thumbBounds.height-2, null);
        g.translate( -thumbBounds.x, -thumbBounds.y );
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {        
        
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return down;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return up;
    }

  }

	static class ScrollbarArrowUpUI extends MetalScrollButton {
		private static Image arrow;
		public ScrollbarArrowUpUI() {
			super(0, 30, true);
			arrow = Ressources.ingamebutton.getSubimage(948, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT, Ressources.RASTERHEIGHT);
		}
		
		@Override
		public void paint(Graphics g){
				g.drawImage(arrow, 0, 0, null);
		}
		
		@Override
		public Dimension getMaximumSize(){
			return new Dimension(30,30);
		}
		public int getButtonWidth(){
			return 30;
		}
		
		public Dimension getPreferredSize(JComponent c){
			return new Dimension(30,30);
		}
	}
	

	
}

