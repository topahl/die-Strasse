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
							buttonNamesSmall[i-14], 45, 140+(i-14)*45 , this.gui );
					label[i-14] = new JLabel();
					label[i-14].setText("Antwort");
					label[i-14].setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
					label[i-14].setBounds(100, 140+(i-14)*45, 500, 39);
					label[i-14].setFont(new Font("Corbel", Font.BOLD, 20));
					
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
		        frage.setFont(new Font("Corbel",Font.BOLD,20));
		        frage.setForeground(new java.awt.Color(0xf9, 0xf9, 0xf9));
		        frage.setBounds(45,60, 500, 75);
		        this.quizwindow.add(frage, javax.swing.JLayeredPane.DEFAULT_LAYER);
	}
	
	
	public void analyzeAntwort(String antwort){
		int antwortnr = (int)antwort.charAt(0)-64;
		//int richtigkeit = Integer.parseInt(this.quizfragen.get(this.quizstart).get(antwortnr));
		//this.beantwortet.add(richtigkeit);
		this.quizstart = (this.quizstart + this.quizstep)%this.quizfragen.size();
	}
	
	
}
