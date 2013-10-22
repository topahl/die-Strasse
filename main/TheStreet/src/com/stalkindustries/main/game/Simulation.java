package com.stalkindustries.main.game;
import java.util.ArrayList;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	private int[] location_raster;
	private ArrayList<Person> people;
	
	
	//Beschwerden an Miri
	void initialize_beziehungsmatrix(){
		int tmp;
		for(int i=0;i<this.people.size();i++)
			for(int j=0;j<this.people.size();i++)
				this.beziehungsmatrix[i][j] = 42;
			
		for(int i=0;i<this.people.size();i++){
			for(int j=i+1;j<this.people.size();i++){
				tmp = (int)Math.random()*(10);
				this.beziehungsmatrix[i][j] = tmp;
				this.beziehungsmatrix[j][i] = tmp;
				if(this.people.get(i).get_haus_id() == this.people.get(j).get_haus_id()){ //Person in einem Haushalt sind besser miteinander befreundet
					tmp = (10-tmp)/2;
					this.beziehungsmatrix[i][j] += tmp;
					this.beziehungsmatrix[j][i] += tmp;
				}
			}
		}
	}
	
	
	//Beschwerden an Miri
	void initialize_location_raster(){
		this.location_raster = new int[this.people.size()];
		for(int i=0;i<this.people.size();i++){
			this.location_raster[i] = 42;
		}
	}
	
	void update_location_raster(){
		//toDo
		//wei�t Bewohnern eine Location-ID zu
		//--> man wei� nun, wo sie sich grob befinden, d.h.
		//wenn sich Person in bestimmtem Haus befindet, dann ist Location-Id = Haus-Id
		//Schule --> Location-ID = Schule-ID
		//Park --> Location-Id = Park-ID
		//einkaufen, arbeiten, ... --> Location-Id = 42
	}
	
	
	//Beschwerden an Miri
	void calculate_misstrauen(){
		float[] misstrauen = new float[people.size()];
		float faktor = 1/100; //kommt noch darauf an, wie h�ufig die Methode aufgerufen wird
		for(int i=0;i<this.people.size();i++)
			misstrauen[i] = this.people.get(i).get_misstrauen();
		
		for(int i=0;i<this.people.size();i++){
			if(this.people.get(i).get_location_id() != 42){	//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
				for(int j=0;j<this.people.size();j++){
					if(i!=j){
						if(this.people.get(i).get_location_id() == this.people.get(j).get_location_id()){
							//zwischenspeichern in misstrauen[] erforderlich, sonst wird immer mit aktualisiertem Wert gerechnet
							if(misstrauen[i]>=misstrauen[j] && misstrauen[j]>=0)
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]>=misstrauen[j] && misstrauen[j]<0)
								misstrauen[i] = misstrauen[i] + faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]<misstrauen[j] && misstrauen[j]>=0)
								misstrauen[i] = misstrauen[i] + faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							else if(misstrauen[i]<misstrauen[j] && misstrauen[j]<0)
								misstrauen[i] = misstrauen[i] - faktor*this.beziehungsmatrix[i][j]/10*misstrauen[j];
							
							if(misstrauen[i]>100)//Rangecheck
								misstrauen[i] = 100;
							if(misstrauen[i]<-100)
								misstrauen[i] = -100;
						}
					}
				}
			}
		}
		
		//mapping der neuen Misstrauenswerte auf Person
		for(int i=0;i<this.people.size();i++){
			this.people.get(i).set_misstrauen(misstrauen[i]);
		}
	}
	
	//Support: Tobi
		public void update_position(){
			for(int i=0;i<people.size();i++){
				people.get(i).step();
			}
			
		}
	
	//Beschwerden an Miri
	float calc_misstrauen_in_street(){
		float misstrauen = 0;
		for(int i=0;i<this.people.size();i++){
			misstrauen += this.people.get(i).get_misstrauen();
		}
		misstrauen = misstrauen/this.people.size();
		
		return misstrauen;
	}
	
	//toDo
	void calc_misstrauen_after_action(){
		
	}

}
