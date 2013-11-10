package com.stalkindustries.main.game;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;

import com.stalkindustries.main.Button;

public class Quiz {
	private ArrayList<Integer> beantwortet = new ArrayList<Integer>();
	private ArrayList<ArrayList<String>> quizFragen; 
	private GUILayer gui;
	private JLayeredPane quizWindow;
	private JTextArea frage = new JTextArea();
	private JLabel zeit = new JLabel();
	private int quizStart = 0;
	private int quizStep = 1;
	private JLabel label[] = new JLabel[3];
	private int timeLeft=0;
	private static final int QUIZTIME = 100;
	private boolean running = false;

	public Quiz(GUILayer gui){
		this.gui = gui;
		this.quizFragen = Ressources.getQuiz();
		this.quizStart = (int)(Math.random()*(this.quizFragen.size()));
		this.quizStep = (int)(Math.random()*(this.quizFragen.size()-2)+1);
	}
	

	public void starteQuiz(){
		//Frage setzen
		this.frage.setText(this.quizFragen.get(this.quizStart).get(0));
		
		//Antworten setzen
		for(int i=0;i<3;i++){
			label[i].setText(this.quizFragen.get(this.quizStart).get(i+1));
		}
		this.timeLeft = this.QUIZTIME;
		
		this.quizWindow.setVisible(true);
		this.quizWindow.setEnabled(true);
		this.running=true;
		this.timeLeft = this.QUIZTIME;
	}
	
	public void initQuizWindow(Control control){
		this.quizWindow = this.gui.getWindow("quizfenster");
		//Inhalt des Quiz Fensters
		Button button;
		int buttonSize = 39;
		int buttonSliceX = 0;
		int buttonSliceY = buttonSize;

		String[] buttonNamesSmall = { "QuizA", "QuizB","QuizC" };
		for (int i = 14; i < buttonNamesSmall.length+14; i++) {
			button = new Button(control,
					Ressources.ingamebutton.getSubimage(buttonSliceX, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 2 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					Ressources.ingamebutton.getSubimage(buttonSliceX + 3 * buttonSize, i * buttonSliceY, buttonSize, buttonSize),
					buttonNamesSmall[i-14], 30, 140+(i-14)*45 , this.gui );
			label[i-14] = new JLabel();
			label[i-14].setText("Antwort");
			label[i-14].setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
			label[i-14].setBounds(85, 140+(i-14)*45, 500, 39);
			label[i-14].setFont(new Font("Corbel", Font.BOLD, 18));
					
			this.quizWindow.add(label[i-14], javax.swing.JLayeredPane.DEFAULT_LAYER);
			this.quizWindow.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
		}
				//Textfeld für die Frage des Quizes
		frage.setLineWrap(true);
		frage.setText("");
		frage.setWrapStyleWord(true);
		frage.setFocusCycleRoot(true);
		frage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		frage.setFocusable(false);
		frage.setOpaque(false);
		frage.setFont(new Font("Corbel",Font.BOLD,18));
		frage.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
		frage.setBounds(30,60, 515, 75);
		this.quizWindow.add(frage, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		this.zeit=new JLabel();
		this.zeit.setIcon(new ImageIcon(Ressources.ingamebutton.getSubimage(948, 138, 179, 20)));
		this.zeit.setBounds(412,19, 167, 19);
		this.quizWindow.add(this.zeit, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	//Beschwerden Miri
	public void analyzeAntwort(String antwort){
		int antwortnr = (int)antwort.charAt(0)-61;
		int richtigkeit = Integer.parseInt(this.quizFragen.get(this.quizStart).get(antwortnr));
		this.beantwortet.add(richtigkeit);
		if(richtigkeit == 100)
			this.quizFragen.remove(this.quizStart);
		this.quizStart = (this.quizStart + this.quizStep)%this.quizFragen.size();
		this.running = false;
	}
	
	//Beschwerden Miri
	public void calcMisstrauenAfterQuiz(){
		
		gui.getButtonsMap().get("pause").setEnabled(true);
		
		int misstrauen = 0;
		//nur wenn die Antwort nicht zu 100% richtig war
		if(this.beantwortet.get(this.beantwortet.size()-1) == 100){
			misstrauen = -5;
		}
		//wenn Antwort so richtig falsch war
		else if(this.beantwortet.get(this.beantwortet.size()-1) < 50){
			misstrauen = 20;	//TODO misstrauenswerte überprüfen
		}
		//wenn Antwort nur teilweise falsch war
		else{
			misstrauen = 10;
		}
		//nur bis size-1, da der Agent wieder ausgenommen ist
		for(int i=0;i<this.gui.getHumans().size()-1;i++){
			if(this.gui.getHumans().get(i) instanceof Person){
				((Person)this.gui.getHumans().get(i)).setMisstrauen(((Person)this.gui.getHumans().get(i)).getMisstrauen()+misstrauen);
			}
			//sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
			if(this.gui.getHumans().get(i) instanceof Person){
				if(((Person)this.gui.getHumans().get(i)).getMisstrauen() > 100)
					((Person)this.gui.getHumans().get(i)).setMisstrauen(100);
				if(((Person)this.gui.getHumans().get(i)).getMisstrauen() < -100)
					((Person)this.gui.getHumans().get(i)).setMisstrauen(-100);
			}
			((Person)this.gui.getHumans().get(i)).updateSchatten();
		}
	}
	/**
	 * @author Tobias
	 */
	public void step(){
		this.timeLeft--;
		this.zeit.setSize((int)((timeLeft*166)/QUIZTIME),19);
		if(timeLeft<=0){
			this.running=false;
			this.beantwortet.add(0);
			this.quizWindow.setEnabled(false);
			this.quizWindow.setVisible(false);
			this.calcMisstrauenAfterQuiz();
			this.quizStart = (this.quizStart + this.quizStep)%this.quizFragen.size();
		}
	}

	public int getTimeleft() {
		return timeLeft;
	}

	public void setTimeleft(int timeleft) {
		this.timeLeft = timeleft;
	}

	public boolean isRunning() {
		return running;
	}
	
	public ArrayList<Integer> getBeantwortete(){
		return this.beantwortet;
	}
	
}
