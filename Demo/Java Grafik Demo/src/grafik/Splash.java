package grafik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
	
public class Splash extends JFrame{
	private Dimension screen; // represents the Screen
	private Dimension dim; //represents Game 0 Position
	private BufferedImage logo;
	private int pos = 10;
	private int inc = 1;
	
	public Splash(){
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screen.getWidth(),(int)screen.getHeight());
        setBackground(Color.black);
        setUndecorated(true);
        setVisible(true);
        try {
			logo = ImageIO.read(new File("src\\grafik\\data\\stalk.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dim = new Dimension();
        dim.setSize((screen.getWidth()/2)-215,(screen.getHeight()/2)-50);
	}
	
	public void paint(Graphics g){
		BufferedImage bufferedImage = new BufferedImage((int)screen.getWidth(), (int)screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0,(int)screen.getWidth(), (int)screen.getHeight());
	    g2d.drawImage(logo, null, dim.width, dim.height);
	    g2d.setColor(Color.white);
	    g2d.fillOval(dim.width + pos + 25, dim.height+65, 8, 8);
	    g2d.fillOval(dim.width + pos + 120, dim.height+65, 8, 8);
		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);
		
	    if(pos <1 || pos > 15){
	    	inc*=-1;
	    }
	    else{
	    	double i = Math.random();
	    	if(i>0.9d)
	    		inc*=-1;
	    }
	    pos+=inc;
	}
}


