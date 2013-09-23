package grafik;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Window extends JFrame{
	private TestMain main;
	public Window(TestMain main){
		this.main=main;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Zeichnen mit Java");
        setSize(1125, 720);
        setUndecorated(true);
        setVisible(true);
        //TP Mouse Click Listener
        Component mouseClick = new MyComponent();
        this.addMouseListener((MouseListener) mouseClick);
        
	}

	public void paint(Graphics g){
		//TP Double Frame Buffering to remove flickering
		BufferedImage bufferedImage = new BufferedImage(1125, 720, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(main.karte.getImage(), 0, 0, 1125, 720, Color.black,null);
	    for(int i=0;i<main.entitys.size();i++){
	        g2d.drawImage(main.entitys.get(i).getGraphic(), main.entitys.get(i).getX(),main.entitys.get(i).getY(),main.entitys.get(i).width,main.entitys.get(i).height,null);
	    }
	    Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.drawImage(bufferedImage, null, 0, 0); 
	  }
		
	private class MyComponent extends JComponent implements MouseListener {

	    @Override
	    public void mouseClicked(MouseEvent arg0) {
	    	if((arg0.getX()/45)*45==1080 && (arg0.getY()/45)*45 == 0){
	    		
	    		System.exit(0);
	    		
	    	}
	        //System.out.println("Clicked: "+arg0.getX()+","+arg0.getY());
	    	Person person = (Person)main.entitys.get(0);
	    	person.setWpX((arg0.getX()/45)*45);
	    	person.setWpY((arg0.getY()/45)*45);

	    }

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

	}
}
