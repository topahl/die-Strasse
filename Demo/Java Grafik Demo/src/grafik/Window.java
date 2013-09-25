package grafik;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Window extends JFrame{
	private TestMain main;
	private Dimension dim; //represents Game 0 Position
	private Dimension screen; // represents the Screen
	public Window(TestMain main){
		this.main=main;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Zeichnen mit Java");
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        //setSize(1125, 720);
        setSize((int)screen.getWidth(),(int)screen.getHeight());
        dim = new Dimension();
        dim.setSize((screen.getWidth()/2)-562,(screen.getHeight()/2)-360);
        setBackground(Color.black);
        setUndecorated(true);
        setVisible(true);
        //TP Mouse Click Listener
        Component mouseClick = new MyComponent();
        this.addMouseListener((MouseListener) mouseClick);
        
	}

	public void paint(Graphics g){
		//TP Double Frame Buffering to remove flickering
		BufferedImage bufferedImage = new BufferedImage((int)screen.getWidth(), (int)screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0,(int)screen.getWidth(), (int)screen.getHeight());
		g2d.drawImage(main.karte.getImage(),(int)dim.getWidth(), (int)dim.getHeight(), 1125, 720, Color.black,null);
	    for(int i=0;i<main.entitys.size();i++){
	        g2d.drawImage(main.entitys.get(i).getGraphic(), main.entitys.get(i).getX(),main.entitys.get(i).getY(),main.entitys.get(i).width,main.entitys.get(i).height,null);
	    }
	    Graphics2D g2dComponent = (Graphics2D) g;
	    g2dComponent.drawImage(bufferedImage, null, 0, 0); 
	  }
		
	private class MyComponent extends JComponent implements MouseListener {

	    @Override
	    public void mouseClicked(MouseEvent arg0) {
	    	if((arg0.getX()-dim.getWidth()-1080)<45 && ((arg0.getY()-dim.getHeight())) < 45 && (arg0.getX()-dim.getWidth()-1080)>0 && ((arg0.getY()-dim.getHeight())) > 0){
	    		
	    		System.exit(0);
	    		
	    	}
	        System.out.println("Clicked: "+arg0.getX()+","+arg0.getY());
	    	Person person = (Person)main.entitys.get(0);
	    	person.setWpX(((arg0.getX()/45)*45)+((int)dim.getWidth()%45));
	    	person.setWpY(((arg0.getY()/45)*45)+((int)dim.getHeight()%45));

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
