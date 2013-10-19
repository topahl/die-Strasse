package com.stalkindustries.main.game;

public class Simulation {
	
	private int[][] beziehungsmatrix;
	private int[] location_raster;
	private Person[] people;
	
	
	void initialize_beziehungsmatrix(){
		//toDo
	}
	
	void initialize_location_raster(){
		this.location_raster = new int[this.people.length];
		for(int i=0;i<this.people.length;i++){
			this.location_raster[i] = 42;
		}
	}
	
	void update_location_raster(){
		//toDo
		//weißt Bewohnern eine Location-ID zu
		//--> man weiß nun, wo sie sich grob befinden, d.h.
		//wenn sich Person in bestimmtem Haus befindet, dann ist Location-Id = Haus-Id
		//Schule --> Location-ID = Schule-ID
		//Park --> Location-Id = Park-ID
		//einkaufen, arbeiten, ... --> Location-Id = 42
	}
	
	
	void calculate_misstrauen(){
		float[] misstrauen = new float[people.length];
		float faktor = 1;
		for(int i=0;i<this.people.length;i++)
			misstrauen[i] = this.people[i].get_misstrauen();
		
		for(int i=0;i<this.people.length;i++){
			if(this.people[i].get_location_id() != 42){	//wenn sich Person dort befindet, wo sie auch beeinflusst werden kann
				for(int j=0;j<this.people.length;j++){
					if(i!=j){
						if(this.people[i].get_location_id() == this.people[j].get_location_id()){
							//zwischenspeichern in misstrauen[] erforderlich, sonst wird immer mit aktualisiertem Wert gerechnet
							misstrauen[i] = misstrauen[i] + faktor*this.beziehungsmatrix[i][j]*misstrauen[j];
							//ToDo: Rangecheck
						}
					}
				}
			}
		}
		
		//mapping der neuen Misstrauenswerte auf Person
		for(int i=0;i<this.people.length;i++){
			this.people[i].set_misstrauen(misstrauen[i]);
		}
	}

}
