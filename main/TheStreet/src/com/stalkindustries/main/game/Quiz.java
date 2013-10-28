package com.stalkindustries.main.game;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;

import com.stalkindustries.main.Button;

public class Quiz {
	private ArrayList<Integer> beantwortet = new ArrayList<Integer>();
	private ArrayList<ArrayList<String>> quizfragen; 
	private GUILayer gui;
	private JLayeredPane quizwindow;
	private JTextArea frage = new JTextArea();
	private int quizstart = 0;
	private int quizstep = 1;
	private JLabel label[] = new JLabel[3];

	public Quiz(GUILayer gui){
		this.gui = gui;
		this.quizfragen = Ressources.getQuiz();
		this.quizstart = (int)(Math.random()*(this.quizfragen.size()));
		this.quizstep = (int)(Math.random()*(this.quizfragen.size())+1);
	}
	
	public void starteQuiz(){
		//Frage setzen
		this.frage.setText(this.quizfragen.get(this.quizstart).get(0));
		
		//Antworten setzen
		for(int i=0;i<3;i++){
			label[i].setText(this.quizfragen.get(this.quizstart).get(i+1));
		}
		
		this.quizwindow.setVisible(true);
		this.quizwindow.setEnabled(true);
	}
	
	public void initQuizWindow(Control control){
		this.quizwindow = this.gui.getWindow("quizfenster");
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
					
					this.quizwindow.add(label[i-14], javax.swing.JLayeredPane.DEFAULT_LAYER);
					this.quizwindow.add(button, javax.swing.JLayeredPane.DEFAULT_LAYER);
				}
				//Textfeld für die Frage des Quizes
		        frage.setLineWrap(true);
		        frage.setText("Frage bla bla bla..");
		        frage.setWrapStyleWord(true);
		        frage.setFocusCycleRoot(true);
		        frage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		        frage.setFocusable(false);
		        frage.setOpaque(false);
		        frage.setFont(new Font("Corbel",Font.BOLD,18));
		        frage.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
		        frage.setBounds(30,60, 515, 75);
		        this.quizwindow.add(frage, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	
	public void analyzeAntwort(String antwort){
		int antwortnr = (int)antwort.charAt(0)-61;
		int richtigkeit = Integer.parseInt(this.quizfragen.get(this.quizstart).get(antwortnr));
		this.beantwortet.add(richtigkeit);
		if(richtigkeit == 0)
			this.quizfragen.remove(this.quizstart);
		this.quizstart = (this.quizstart + this.quizstep)%this.quizfragen.size();
	}
	
	//Beschwerden Miri
	public void calcMisstrauenAfterQuiz(){
		int misstrauen = 0;
		//nur wenn die Antwort nicht zu 100% richtig war
		if(this.beantwortet.get(this.beantwortet.size()-1) == 0){
			misstrauen = -5;
		}
		//wenn Antwort so richtig falsch war
		else if(this.beantwortet.get(this.beantwortet.size()-1) > 50){
			misstrauen = 20;	//TODO misstrauenswerte überprüfen
		}
		//wenn Antwort nur teilweise falsch war
		else{
			misstrauen = 10;
		}
		//nur bis size-1, da der Agent wieder ausgenommen ist
		for(int i=0;i<this.gui.getHumans().size()-1;i++){
			if(this.gui.getHumans().get(i) instanceof Person){
				((Person)this.gui.getHumans().get(i)).set_misstrauen(((Person)this.gui.getHumans().get(i)).get_misstrauen()+misstrauen);
				((Person)this.gui.getHumans().get(i)).update_schatten();
			}
		}
	}
	
	
}
