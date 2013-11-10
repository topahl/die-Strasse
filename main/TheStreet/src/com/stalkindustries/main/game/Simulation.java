package com.stalkindustries.main.game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.BorderFactory;

public class Simulation {

	private int[][] beziehungsmatrix;
	// private int[] location_raster; nicht mehr benötigt
	private ArrayList<Person> people = new ArrayList<Person>();
	private Agent agent = new Agent(0, "");
	private int spielTag = 1;
	private int spielStunde = 7;
	private int spielMinute = 0;
	private ArrayList<Haus> houses = new ArrayList<Haus>();
	private boolean wieschteAktion = true; // wieeeeescht = boese
	private double misstrauenMax = -100;

	public Simulation() {

	}

	// Beschwerden an Miri
	void initialisiereBeziehungsmatrix() {
		int tmp;
		this.beziehungsmatrix = new int[this.people.size()][this.people.size()];
		for (int i = 0; i < this.people.size(); i++) {
			for (int j = 0; j < this.people.size(); j++) {
				this.beziehungsmatrix[i][j] = 0;
			}
		}

		// asymmetrisch
		for (int i = 0; i < this.people.size(); i++) {
			for (int j = 0; j < this.people.size(); j++) {
				if (i != j) {
					tmp = (int) (Math.random() * 10) + 1;
					this.beziehungsmatrix[i][j] = tmp;
					if (this.people.get(i).getHausId() == this.people.get(j).getHausId()) { // Person in einem Haushalt sind
																							// besser miteinander befreundet
						tmp = (10 - tmp) / 2;
						this.beziehungsmatrix[i][j] += tmp;
						this.beziehungsmatrix[j][i] += tmp;
					}
				}
			}
		}

		// //symmetrische Matrix
		// for(int i=0;i<this.people.size();i++){
		// for(int j=i+1;j<this.people.size();j++){
		// tmp = (int)(Math.random()*(10))+1;
		// this.beziehungsmatrix[i][j] = tmp;
		// this.beziehungsmatrix[j][i] = tmp;
		// if(this.people.get(i).get_haus_id() ==
		// this.people.get(j).get_haus_id()){ //Person in einem Haushalt sind
		// besser miteinander befreundet
		// tmp = (10-tmp)/2;
		// this.beziehungsmatrix[i][j] += tmp;
		// this.beziehungsmatrix[j][i] += tmp;
		// }
		// }
		// }
	}

	// Beschwerden an Sven und Miri
	void calcMisstrauen() {
		if (this.spielStunde > 5 || this.spielStunde < 2) {
			// misstrauen[] ist eine Hilfvariable, die später die später die
			// neuen Werte enthält und sie wird benötigt, dass nicht die echten
			// Werte verändert werden, bevor alle berechnet wurden
			double[] misstrauen = new double[this.people.size()];

			double faktor = 0.01275; // Faktor, der regelt, wie stark sich
										// Personen bei einem Methodenaufruf
										// beeinflussen --> abhängig von
										// Häufigkeit des Methodenaufrufs

			// misstrauen[] mit den alten Misstrauenswerten initialisieren
			for (int i = 0; i < this.people.size(); i++) {
				misstrauen[i] = this.people.get(i).get_misstrauen();
			}

			// jede Person kann theoretisch wieder von jeder anderen beeinflusst
			// werden
			for (int i = 0; i < this.people.size(); i++) {
				// wenn sich Person dort befindet, wo sie auch beeinflusst
				// werden kann
				if (this.people.get(i).getLocationId() - 48 + 1 != 0
						&& this.people.get(i).getLocationId() - 48 + 1 != 'X'
						&& this.people.get(i).getLocationId() - 48 + 1 != 'E') {
					for (int j = 0; j < this.people.size(); j++) {
						// eine Person kann sich nicht selbst beeinflussen
						if (i != j) {
							// wenn sich die beiden Personen am selben Ort
							// befinden
							if (this.people.get(i).getLocationId() - 48 + 1 == this.people
									.get(j).getLocationId() - 48 + 1) {
								// Ausnahme in der Berechnung: Kinder
								// beeinflussen Erwachsene weniger
								if (this.people.get(i) instanceof Erwachsene
										&& this.people.get(j) instanceof Kinder) { // Kind beeinflusst Erwachsenen weniger
									misstrauen[i] = misstrauen[i] - faktor / 2 * this.beziehungsmatrix[i][j] * (this.people.get(i)
													.get_misstrauen() - this.people
													.get(j).get_misstrauen());
								} else {
									misstrauen[i] = misstrauen[i] - faktor * this.beziehungsmatrix[i][j] * (this.people.get(i)
													.get_misstrauen() - this.people
													.get(j).get_misstrauen());
								}

								// sorgt dafür, dass sich das Misstrauen
								// zwischen -100 und 100 bewegt
								if (misstrauen[i] > 100) {
									misstrauen[i] = 100;
								}
								if (misstrauen[i] < -100) {
									misstrauen[i] = -100;
								}
							}
						}
					}
				}
			}

			// Mapping der neuen Misstrauenswerte auf Person
			for (int i = 0; i < this.people.size(); i++) {
				this.people.get(i).set_misstrauen(misstrauen[i]);
			}
		}
	}

	// Support: Tobi
	public void updatePosition() {
		for (int i = 0; i < this.people.size(); i++) {
			this.people.get(i).step();
		}
	}

	// Beschwerden an Miri
	// Mittelwert des Misstrauens der einzelnen Personen
	public double calcMisstrauenInStreet() {
		float misstrauen = 0;
		for (int i = 0; i < this.people.size(); i++) {
			misstrauen += this.people.get(i).get_misstrauen();
		}
		misstrauen = misstrauen / this.people.size();

		return misstrauen;
	}

	// Beschwerden Miri
	public void calcMisstrauenNachBeschwichtigenInPark() {
		for (int i = 0; i < this.people.size(); i++) {
			// Checken, ob sich noch jemand im Park befindet
			if (this.people.get(i).getLocationId() == 'P') {
				this.people.get(i).set_misstrauen(
						this.people.get(i).get_misstrauen() - 1);
			}

			// sorgt dafür, dass sich das Misstrauen zwischen -100 und 100
			// bewegt
			if (this.people.get(i).get_misstrauen() > 100) {
				this.people.get(i).set_misstrauen(100);
			}
			if (this.people.get(i).get_misstrauen() < -100) {
				this.people.get(i).set_misstrauen(-100);
			}
		}
	}

	// Beschwerden an Miri
	// Beschwerden an Miri
	public void calcMisstrauenNachBeschwichtigen(int actionId, Person person) {
		int zufall = 0;
		int risiko;
		double misstrauen = person.get_misstrauen();

		// Fehlschlagen der Aktionen ist unter anderem abhängig vom
		// Misstrauensstatus
		if (misstrauen >= 0) {
			zufall = (int) (Math.random() * (misstrauen / 10));
			if (zufall == 0) {
				zufall = 1;
			}
		}
		// Personen sind weniger misstrauisch
		else {
			if (misstrauen / 10 < 0) {
				zufall = -(int) (Math.random() * -(misstrauen / 10));
				if (zufall == 0) {
					zufall = -1;
				}
			}
			// else
			// zufall = (int)(Math.random()*(misstrauen/10));
		}

		// Risiko einer Beschwichtigenaktion steigt, je häufiger man sie
		// ausführt
		risiko = person.get_durchgefuehrteBeschwichtigungen(actionId);
		if (actionId == 0 || actionId == 2 || actionId == 3) { // Kuchen,
																// Flirten,
																// Helfen
			if (risiko == 0) {
				if (zufall > 5) {
					zufall *= -1;
				} else if (zufall >= 0) {
					zufall *= -10;
				} else {
					zufall *= 10;
				}
			} else {
				if (zufall > 5) {
					zufall *= 6;
				} else if (zufall >= 0) {
					zufall *= 6;
				} else {
					zufall *= -3;
				}
			}
		} else if (actionId == 1) { // bei Haus vorbeigehen, um zu reden
			if (risiko <= 3)
				if (zufall >= 0) {
					zufall *= -3;
				} else {
					zufall *= 4;
				}
			else if (zufall >= 0) {
				zufall *= 3;
			} else {
				zufall *= -1;
			}
		} else { // Im Park unterhalten
			if (zufall >= 0) {
				zufall *= -2;
			} else {
				zufall *= 3;
			}
		}

		misstrauen += zufall;

		// sorgt dafür, dass sich das Misstrauen zwischen -100 und 100 bewegt
		if (misstrauen > 100) {
			misstrauen = 100;
		}
		if (misstrauen < -100) {
			misstrauen = -100;
		}

		// Misstrauen der Person setzen
		person.set_misstrauen(misstrauen);
	}

	// TODO
	// Beschwerden an Miri
	public void calcMisstrauenNachUeberwachung(int hausId) {
		// Risiko berechnen
		// Risiko ist abhängig von der Uhrzeit, d.h.tagsüber ist das Risiko
		// höher als nachts
		int risiko = 0;
		if (this.spielStunde > 1 && this.spielStunde < 6) { // Nachtmodus
			risiko = (int) (Math.random() * 3);
		} else { // Tagmodus
			risiko = (int) (Math.random() * 15);
		}

		int mittelpunktX = this.houses.get(hausId - 1).getPosX() + 3
				* Ressources.RASTERHEIGHT / 2;
		int mittelpunktY = this.houses.get(hausId - 1).getPosY() + 3
				* Ressources.RASTERHEIGHT / 2;
		int epsilon = 200;

		for (int i = 0; i < this.people.size(); i++) {
			// Checken, ob sich noch jemand in dem Haus befindet
			// für alle Personen, die noch im Haus sind, das Misstrauen neu
			// berechnen
			if (this.people.get(i).getLocationId() - 48 == hausId) {
				if (risiko > 2) {
					this.people.get(i).set_misstrauen(
							this.people.get(i).get_misstrauen() + 6); // TODO:  den Wert 50 testen  ... eventuell erhöhen
				}
			}
			// Checken, ob sich jemand in einer epsilon-Umgebung um das Haus
			// befindet, in das eingebrochen werden soll
			// --> 1. Epsilon-Umgebung aufspannen (ist eine relative eckige :-D)
			// -->Mittelpunkt vom Haus bestimmen
			else {
				// wenn sich eine Person in der Epsilon-Umgebung befindet
				if (this.people.get(i).getPosX() >= mittelpunktX - epsilon
						&& this.people.get(i).getPosX() <= mittelpunktX
								+ epsilon
						&& this.people.get(i).getPosY() >= mittelpunktY
								- epsilon
						&& this.people.get(i).getPosY() <= mittelpunktY
								+ epsilon
						&& this.people.get(i).getLocationId() != 'E') {
					// wenn das per Zufall eine Person ist, die in dem Haus
					// wohnt und z.B. auf dem Heimweg ist
					// hier ist das Misstrauen natürlich größer
					if (this.people.get(i).getHausId() + 1 == hausId) {
						if (risiko > 2) {
							this.people.get(i).set_misstrauen(
									this.people.get(i).get_misstrauen() + 2);
						}
					} else {
						if (risiko > 2) {
							this.people.get(i).set_misstrauen(
									this.people.get(i).get_misstrauen() + 1);
						}
					}
				}
			}
			// sorgt dafür, dass sich das Misstrauen zwischen -100 und 100
			// bewegt
			if (this.people.get(i).get_misstrauen() > 100) {
				this.people.get(i).set_misstrauen(100);
			}
			if (this.people.get(i).get_misstrauen() < -100) {
				this.people.get(i).set_misstrauen(-100);
			}
		}

	}

	// Beschwerden Miri
	public void agentBetrittFremdesHaus() {
		// wenn sich der Agent in irgendeinem Haus befindet
		if (this.getAgent().getLocationId() - 48 >= 1
				&& this.getAgent().getLocationId() - 48 <= 9) {
			// wenn sich der Agent in dem Haus befindet, das ausspioniert werden
			// soll
			if (this.getAgent().getLocationId() != (char) this.getAgent()
					.getHausId() + 48 + 1) {
				if (this.wieschteAktion) {
					this.calcMisstrauenNachUeberwachung(this.getAgent()
							.getLocationId() - 48);
				}

			}
		}
	}

	// Beschwerden Miri
	// während ein Haus überwacht wird, macht sich bei den Bewohnern ein
	// Unwohlsein breit und sie werden mehr misstrauisch
	public void calcMisstrauenDuringUeberwachung() {
		for (int i = 0; i < this.people.size(); i++) {
			// wenn Überwachungsmodule in dem Haus, in dem die Person lebt,
			// installiert wurden
			if (this.houses.get(this.people.get(i).getHausId())
					.getUeberwachungsmodule().size() > 0) {
				this.people.get(i).set_misstrauen(
						this.people.get(i).get_misstrauen() + 2);
			}
			// sorgt dafür, dass sich das Misstrauen zwischen -100 und 100
			// bewegt
			if (this.people.get(i).get_misstrauen() > 100) {
				this.people.get(i).set_misstrauen(100);
			}
			if (this.people.get(i).get_misstrauen() < -100) {
				this.people.get(i).set_misstrauen(-100);
			}
		}
	}

	// Beschwerden Miri
	// Allen Häusern den Überwachungsstatus updaten
	public void updateUeberwachungsstatus() {
		float ueberwachungswert = 0;

		for (int i = 0; i < this.getHouses().size(); i++) {
			this.getHouses().get(i).setUeberwachungsstatus(0);
			for (int j = 0; j < this.getHouses().get(i)
					.getUeberwachungsmodule().size(); j++) {
				if (this.getHouses().get(i).getUeberwachungsmodule().get(j)
						.equals("Wanze")) {
					ueberwachungswert = ueberwachungswert
							+ this.getHouses().get(i).getUeberwachungsWert(0);
				}
				if (this.getHouses().get(i).getUeberwachungsmodule().get(j)
						.equals("Kamera")) {
					ueberwachungswert = ueberwachungswert
							+ this.getHouses().get(i).getUeberwachungsWert(1);
				}
				if (this.getHouses().get(i).getUeberwachungsmodule().get(j)
						.equals("Hacken")) {
					ueberwachungswert = ueberwachungswert
							+ this.getHouses().get(i).getUeberwachungsWert(2);
				}
				if (this.getHouses().get(i).getUeberwachungsmodule().get(j)
						.equals("Fernglas")) {
					ueberwachungswert = ueberwachungswert
							+ this.getHouses().get(i).getUeberwachungsWert(3);
				}
				if (ueberwachungswert > 100) {
					ueberwachungswert = 100;
				}
				this.getHouses().get(i)
						.setUeberwachungsstatus(ueberwachungswert);
				if (ueberwachungswert >= 0) {
					this.calcColouredPeople(i);
				}
			}
			ueberwachungswert = 0;
		}
	}

	// Beschwerden Miri
	// Mittelwert der Überwachungsstati der einzelnen Häuser
	public float calcUeberwachungsstatusInStreet() {
		float ueberwachung = 0;
		for (int i = 0; i < this.houses.size(); i++) {
			ueberwachung += this.houses.get(i).getUeberwachungsstatus();
		}
		ueberwachung = ueberwachung / (this.houses.size() - 1);

		return ueberwachung;
	}

	// Support Tiki
	void calcSpielzeit() {
		this.spielMinute++;
		if (this.spielMinute == 60) {
			this.spielMinute = 0;
			this.spielStunde++;
		}
		if (this.spielStunde == 24) {
			this.spielStunde = 0;
			this.spielTag++;
		}
	}

	// Support Tiki
	public void tagesablauf() {
		char locationId = '0';
		int hausId = 0;
		for (int i = 0; i < this.people.size(); i++) {
			locationId = this.people.get(i).getLocationId();
			hausId = this.people.get(i).getHausId() + 1;

			// Es bekommen nur die Menschen einen neuen Weg zugewiesen, wenn sie
			// sich gerade nicht bewegen
			if (this.people.get(i).getCurrentMove() == 'n') {

				// Zuerst wird der Tagesablauf der Kinder überprüft, da dieser
				// von den Erwachsenen unterschiedlich ist
				if (this.people.get(i) instanceof Kinder) {
					if (this.people.get(i).getZeitverzogerung()
							+ this.spielMinute == 60) {

						if (this.spielStunde == 7) { // zur Schule gehen
							this.calcWeg(this.people.get(i), 'E');
						}

						// Nur wenn der Mensch nicht zuHause ist, kann er nach
						// Hause gehen
						if (this.people.get(i).getHomePosX() != this.people
								.get(i).getPosX()
								|| this.people.get(i).getHomePosY() != this.people
										.get(i).getPosY()) {

							if (this.spielStunde == 14) { // nach Hause gehen
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							}
							if (this.spielStunde == 20) { // nach Hause gehen
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							}
						}
					} else {

						// nach Hause gehen, notwendig, falls die Kinder noch
						// eine runde im Park drehen & 20 Uhr überschritten wird
						if (this.spielStunde >= 20
								&& (this.people.get(i).getHomePosX() != this.people
										.get(i).getPosX() || this.people.get(i)
										.getHomePosY() != this.people.get(i)
										.getPosY())) {
							this.calcWeg(this.people.get(i),
									String.valueOf(hausId).charAt(0));
						}

						// randomisiert in den Park gehen
						if (this.spielStunde >= 15 && this.spielStunde <= 19
								&& locationId != 'P') {
							if ((int) (Math.random() * 200) == 3) {
								this.calcWeg(this.people.get(i), 'P');
							}
						}

						// randomisiert den Park verlassen oder noch eine Runde
						// drehen
						if (locationId == 'P' && this.spielStunde <= 19) {
							if ((int) (Math.random() * 5) == 3) {
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							} else {
								if (this.people.get(i).getCurrentMove() == 'n') {
									this.calcWeg(this.people.get(i), 'P');
								}
							}
						}
					}

				} else {
					// Nun wir der Tagesablauf der Erwachsenen untersucht
					if (this.people.get(i) instanceof Erwachsene) {

						// Zuerst werden die Erwachsenen untersucht, die Arbeit
						// haben
						if (((Erwachsene) this.people.get(i)).isHat_arbeit()) {
							if (this.spielStunde == 8
									&& this.people.get(i).getZeitverzogerung()
											+ this.spielMinute == 60) { // Zur
																		// Arbeit
																		// gehen
								this.calcWeg(this.people.get(i), 'E');
							}
							if (this.spielStunde == 16
									&& this.people.get(i).getZeitverzogerung()
											+ this.spielMinute >= 60
									&& (this.people.get(i).getHomePosX() != this.people
											.get(i).getPosX() || this.people
											.get(i).getHomePosY() != this.people
											.get(i).getPosY())) { // nach Hause
																	// gehen
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							}
							if (this.spielStunde >= 17 || this.spielStunde <= 0) { // in
																					// den
																					// Park
																					// gehen
								if ((int) (Math.random() * 300) == 3) {
									this.calcWeg(this.people.get(i), 'P');
								}
							}
						} else {
							if (this.spielStunde == 9
									&& this.people.get(i).getZeitverzogerung()
											+ this.spielMinute == 60) { // zum
																		// Einkaufen
																		// gehen
								if ((int) (Math.random() * 3) + 1 == 1) {
									this.calcWeg(this.people.get(i), 'E');
								}
							}
							if (this.spielStunde >= 9 && this.spielStunde <= 14
									&& hausId != 'E') { // zum Einkaufen gehen
								if ((int) (Math.random() * 300) == 3) {
									this.calcWeg(this.people.get(i), 'P');
								}
							}
							if (this.spielStunde == 12
									&& this.people.get(i).getZeitverzogerung()
											+ this.spielMinute >= 60
									&& (this.people.get(i).getHomePosX() != this.people
											.get(i).getPosX() || this.people
											.get(i).getHomePosY() != this.people
											.get(i).getPosY())) { // nach Hause
																	// gehen
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							}
							if (this.spielStunde >= 14 || this.spielStunde <= 1) { // in
																					// den
																					// Park
																					// gehen
								if ((int) (Math.random() * 300) == 3) {
									this.calcWeg(this.people.get(i), 'P');
								}
							}
						}
						if (this.spielStunde == 1
								&& this.people.get(i).getZeitverzogerung()
										+ this.spielMinute >= 60
								&& (this.people.get(i).getHomePosX() != this.people
										.get(i).getPosX() || this.people.get(i)
										.getHomePosY() != this.people.get(i)
										.getPosY())) { // nach Hause gehen
							this.calcWeg(this.people.get(i),
									String.valueOf(hausId).charAt(0));
						}
						if (this.spielStunde >= 2
								&& this.spielStunde < 4
								&& (this.people.get(i).getHomePosX() != this.people
										.get(i).getPosX() || this.people.get(i)
										.getHomePosY() != this.people.get(i)
										.getPosY())) { // nach Hause gehen
							this.calcWeg(this.people.get(i),
									String.valueOf(hausId).charAt(0));
						}
						if (locationId == 'P'
								&& (this.spielStunde < 2 || this.spielStunde >= 9)) { // nach
																						// Hause
																						// gehen
							if ((int) (Math.random() * 5) == 3) {
								this.calcWeg(this.people.get(i), String
										.valueOf(hausId).charAt(0));
							} else {
								if (this.people.get(i).getCurrentMove() == 'n') {
									this.calcWeg(this.people.get(i), 'P');
								}
							}
						}
					}
				}
			}
		}
		if (this.getAgent().getLocationId() != (char) (this.getAgent()
				.getHausId() + 48 + 1)
				&& (this.spielStunde >= 21 || this.spielStunde < 6)
				&& !this.wieschteAktion
				&& this.getAgent().getCurrentMove() == 'n'
				&& this.getAgent().getLocationId() != 'P') {
			this.calcWeg(this.getAgent(),
					(char) (this.getAgent().getHausId() + 48 + 1));
		}
		if (this.getAgent().getLocationId() != (char) (this.getAgent()
				.getHausId() + 48 + 1)
				&& this.spielStunde >= 2
				&& this.spielStunde < 6
				&& !this.wieschteAktion
				&& this.getAgent().getCurrentMove() == 'n') {
			this.calcWeg(this.getAgent(),
					(char) (this.getAgent().getHausId() + 48 + 1));
		}
	}

	// Support Tiki
	private Point calcParkeingang(ArrayList<ArrayList<String>> location_ids) {
		int parkCounter = 0;

		for (int i = 1; i < 15; i++) {
			for (int j = 1; j < 24; j++) {
				if (location_ids.get(i).get(j).equals("P")) {
					if (this.valuesInRange(j, i + 1)) {
						if (location_ids.get(i + 1).get(j).equals("P")
								|| location_ids.get(i + 1).get(j).equals("X")) {
							parkCounter++;
						}
					}
					if (this.valuesInRange(j, i - 1)) {
						if (location_ids.get(i - 1).get(j).equals("P")
								|| location_ids.get(i - 1).get(j).equals("X")) {
							parkCounter++;
						}
					}
					if (this.valuesInRange(j + 1, i)) {
						if (location_ids.get(i).get(j + 1).equals("P")
								|| location_ids.get(i).get(j + 1).equals("X")) {
							parkCounter++;
						}
					}
					if (this.valuesInRange(j - 1, i)) {
						if (location_ids.get(i).get(j - 1).equals("P")
								|| location_ids.get(i).get(j - 1).equals("X")) {
							parkCounter++;
						}
					}

				}
				if (parkCounter == 3)
					return new Point(i, j);
				else {
					parkCounter = 0;
				}
			}
		}
		return null;
	}

	// Support Tiki
	public void calcWeg(Mensch mensch, char zielLoc) {

		char locationId;
		ArrayList<ArrayList<String>> locationIds;
		Stack<Character> neuerWeg = new Stack<Character>();
		new Point();

		locationId = mensch.getLocationId();
		locationIds = Ressources.getLocation_ids();

		if (zielLoc == 'P') {
			this.calcParkeingang(locationIds);
		}

		// Aktuelle Position des Männchens wird auf 0 gesetzt
		if (zielLoc != locationId || zielLoc != 'P') {
			locationIds = this.rasterkarteInitialisieren(locationIds,
					String.valueOf(zielLoc), locationId);
		} else {
			locationIds = this.rasterkarteInitialisieren(locationIds, "P",
					locationId);
		}

		// if ((mensch.getPosY()-Ressources.ZEROPOS.height)%45!=0 &&
		// ((mensch.getPosX()-Ressources.ZEROPOS.width)/Ressources.RASTERHEIGHT)%45!=0){
		switch (mensch.getCurrentMove()) {
		case 'r':
			locationIds.get(
					(mensch.getPosY() - Ressources.ZEROPOS.height)
							/ Ressources.RASTERHEIGHT).set(
					(mensch.getPosX() - Ressources.ZEROPOS.width)
							/ Ressources.RASTERHEIGHT + 1, "0");
			break;
		case 'u':
			locationIds.get(
					(mensch.getPosY() - Ressources.ZEROPOS.height)
							/ Ressources.RASTERHEIGHT + 1).set(
					(mensch.getPosX() - Ressources.ZEROPOS.width)
							/ Ressources.RASTERHEIGHT, "0");
			break;
		case 'o':
		case 'l':
		default:
			locationIds.get(
					(mensch.getPosY() - Ressources.ZEROPOS.height)
							/ Ressources.RASTERHEIGHT).set(
					(mensch.getPosX() - Ressources.ZEROPOS.width)
							/ Ressources.RASTERHEIGHT, "0");
		}

		// Bewegungsstack nach und nach füllen
		neuerWeg = this.calcWegInRasterkarte(locationIds, mensch, zielLoc);

		// Stack zur Bewegung freigeben
		mensch.setMoves(neuerWeg);
	}

	// Support Tiki
	private Stack<Character> calcWegInRasterkarte(
			ArrayList<ArrayList<String>> location_ids, Mensch mensch,
			char zielloc) {

		Stack<Character> neuer_weg = new Stack<Character>();
		int counter = 1;
		int xPos_current = 0;
		int yPos_current = 0;
		boolean firstStep = true;
		String ziellocation = "Z";

		goal: for (int i = 0; i < 100; i++) {
			for (int j = 0; j < Ressources.MAPHEIGHT / Ressources.RASTERHEIGHT; j++) { // J entspricht y-wert, K  entspricht x-wert
				for (int k = 0; k < Ressources.MAPWIDTH
						/ Ressources.RASTERHEIGHT; k++) {
					// Es werden Zahlen auf der Map gesucht
					if (location_ids.get(j).get(k).equals(String.valueOf(i))) {
						// Es wird überprüft, ob das Ziel in direkter Nähe liegt
						if (this.valuesInRange(k, j + 1)) { // 15 -> Rasterhöhe
							if (location_ids.get(j + 1).get(k)
									.equals(ziellocation)) {
								location_ids.get(j + 1).set(k,
										String.valueOf(i + 1));
								counter = i;
								yPos_current = j + 1;
								xPos_current = k;
								break goal;
							}
						}
						if (this.valuesInRange(k + 1, j)) { // 24 ->
															// Rasterbreite
							if (location_ids.get(j).get(k + 1)
									.equals(ziellocation)) {
								location_ids.get(j).set(k + 1,
										String.valueOf(i + 1));
								counter = i;
								yPos_current = j;
								xPos_current = k + 1;
								break goal;
							}
						}
						if (this.valuesInRange(k, j - 1)) {
							if (location_ids.get(j - 1).get(k)
									.equals(ziellocation)) {
								location_ids.get(j - 1).set(k,
										String.valueOf(i + 1));
								counter = i;
								yPos_current = j - 1;
								xPos_current = k;
								break goal;
							}
						}
						if (this.valuesInRange(k - 1, j)) {
							if (location_ids.get(j).get(k - 1)
									.equals(ziellocation)) {
								location_ids.get(j).set(k - 1,
										String.valueOf(i + 1));
								counter = i;
								yPos_current = j;
								xPos_current = k - 1;
								break goal;
							}
						}

						if (i > 0 && zielloc == mensch.getLocationId()
								|| zielloc != mensch.getLocationId()) {
							// Es wird überprüft, ob ein Feld
							// drüber/drunter/links oder rechts ebenfalls
							// begehbar ist -> das wird markiert
							if (this.valuesInRange(k, j + 1)) {
								if (location_ids.get(j + 1).get(k).equals("X")) { // Weg nach unten ist begehbar
									location_ids.get(j + 1).set(k,
											String.valueOf(i + 1));
								}
							}
							if (this.valuesInRange(k + 1, j)) {
								if (location_ids.get(j).get(k + 1).equals("X")) { // Weg nach rechts ist begehbar
									location_ids.get(j).set(k + 1,
											String.valueOf(i + 1));
								}
							}
							if (this.valuesInRange(k, j - 1)) {
								if (location_ids.get(j - 1).get(k).equals("X")) { // Weg nach oben ist begehbar
									location_ids.get(j - 1).set(k,
											String.valueOf(i + 1));
								}
							}
							if (this.valuesInRange(k - 1, j)) {
								if (location_ids.get(j).get(k - 1).equals("X")) { // Weg nach links ist begehbar
									location_ids.get(j).set(k - 1,
											String.valueOf(i + 1));
								}
							}
						} else { // TODO anpassen!!!
							if (this.valuesInRange(k, j)) {
								if (location_ids.get(j - 1).get(k).equals("X")
										&& firstStep) { // Weg nach oben ist
														// begehbar
									location_ids.get(j - 1).set(k,
											String.valueOf(i + 1));
									firstStep = false;
								}
							}
							if (this.valuesInRange(k, j)) {
								if (location_ids.get(j).get(k - 1).equals("X")
										&& firstStep) { // Weg nach links ist
														// begehbar
									location_ids.get(j).set(k - 1,
											String.valueOf(i + 1));
									firstStep = false;
								}
							}
							if (this.valuesInRange(k + 1, j)) {
								if (location_ids.get(j).get(k + 1).equals("X")
										&& firstStep) { // Weg nach rechts ist
														// begehbar
									location_ids.get(j).set(k + 1,
											String.valueOf(i + 1));
									firstStep = false;
								}
							}
							if (this.valuesInRange(k, j + 1)) {
								if (location_ids.get(j + 1).get(k).equals("X")
										&& firstStep) { // Weg nach unten ist
														// begehbar
									location_ids.get(j + 1).set(k,
											String.valueOf(i + 1));
									firstStep = false;
								}
							}
						}

					}
				}
			}
			// Wenn eine Person in den Park gehen möchte und schon im Park ist,
			// braucht man eine andere Ziellocation
			if (i == 2 && zielloc == 'P' && mensch.getLocationId() == 'P') {
				ziellocation = "0";
			}
		}

		// Der Stack für die Bewegung wird mit den richtigen Werten gefüllt.
		// Dafür hangelt man sich absteigend an der zahlenreihe entlang
		neuer_weg = this.fuelleStackWeg(zielloc, mensch, location_ids, counter,
				xPos_current, yPos_current);

		return neuer_weg;
	}

	// Support Tiki
	private Stack<Character> fuelleStackWeg(Character zielLoc, Mensch mensch,
			ArrayList<ArrayList<String>> locationIds, int counter,
			int xPosCurrent, int yPosCurrent) {
		Stack<Character> neuerWeg = new Stack<Character>();

		// Falls das Ziel ein Haus ist, soll die Person auf ihren startpunkt
		// laufen.
		if (mensch.getHausId() == zielLoc - 48 - 1) {
			if ((int) zielLoc - 48 <= 9 && (int) zielLoc - 48 > 0) { // -48 für char umwandlung zu int
				neuerWeg = this.fuelleStackFuerHomeposition(mensch,
						xPosCurrent, yPosCurrent);
			}
		}

		if (mensch.getLocationId() != zielLoc || (int) (Math.random() * 2) == 1) {
			for (int i = counter; i >= 0; i--) {
				if (mensch.getLocationId() == zielLoc && i == 0
						&& zielLoc == 'P') {
					if (this.valuesInRange(xPosCurrent, yPosCurrent + 1)) {
						if (locationIds.get(yPosCurrent + 1).get(xPosCurrent)
								.equals(String.valueOf(counter + 1))) { // unten gehts weiter
							yPosCurrent++;
							neuerWeg.push('o');
						}
					}
					if (this.valuesInRange(xPosCurrent + 1, yPosCurrent)) {
						if (locationIds.get(yPosCurrent).get(xPosCurrent + 1)
								.equals(String.valueOf(counter + 1))) { // rechts gehts weiter
							xPosCurrent++;
							neuerWeg.push('l');
						}
					}
					if (this.valuesInRange(xPosCurrent, yPosCurrent - 1)) {
						if (locationIds.get(yPosCurrent - 1).get(xPosCurrent)
								.equals(String.valueOf(counter + 1))) { // oben gehts weiter
							yPosCurrent--;
							neuerWeg.push('u');
						}
					}
					if (this.valuesInRange(xPosCurrent - 1, yPosCurrent)) {
						if (locationIds.get(yPosCurrent).get(xPosCurrent - 1)
								.equals(String.valueOf(counter + 1))) { // links gehts weiter
							xPosCurrent--;
							neuerWeg.push('r');
						}
					}
				} else {
					if (this.valuesInRange(xPosCurrent, yPosCurrent + 1)) {
						if (locationIds.get(yPosCurrent + 1).get(xPosCurrent)
								.equals(String.valueOf(i))) { // unten gehts  weiter
							yPosCurrent++;
							neuerWeg.push('o');
						}
					}
					if (this.valuesInRange(xPosCurrent + 1, yPosCurrent)) {
						if (locationIds.get(yPosCurrent).get(xPosCurrent + 1)
								.equals(String.valueOf(i))) { // rechts gehts weiter
							xPosCurrent++;
							neuerWeg.push('l');
						}
					}
					if (this.valuesInRange(xPosCurrent, yPosCurrent - 1)) {
						if (locationIds.get(yPosCurrent - 1).get(xPosCurrent)
								.equals(String.valueOf(i))) { // oben gehts weiter
							yPosCurrent--;
							neuerWeg.push('u');
						}
					}
					if (this.valuesInRange(xPosCurrent - 1, yPosCurrent)) {
						if (locationIds.get(yPosCurrent).get(xPosCurrent - 1)
								.equals(String.valueOf(i))) { // links gehts weiter
							xPosCurrent--;
							neuerWeg.push('r');
						}
					}
				}
			}
		} else {
			for (int i = 1; i <= counter + 1; i++) {
				if (zielLoc != 'P') {
					i--;
				}
				if (this.valuesInRange(xPosCurrent, yPosCurrent + 1)) {
					if (locationIds.get(yPosCurrent + 1).get(xPosCurrent)
							.equals(String.valueOf(i))) { // unten gehts weiter
						yPosCurrent++;
						neuerWeg.push('o');
					}
				}
				if (this.valuesInRange(xPosCurrent + 1, yPosCurrent)) {
					if (locationIds.get(yPosCurrent).get(xPosCurrent + 1)
							.equals(String.valueOf(i))) { // rechts gehts weiter
						xPosCurrent++;
						neuerWeg.push('l');
					}
				}
				if (this.valuesInRange(xPosCurrent, yPosCurrent - 1)) {
					if (locationIds.get(yPosCurrent - 1).get(xPosCurrent)
							.equals(String.valueOf(i))) { // oben gehts weiter
						yPosCurrent--;
						neuerWeg.push('u');
					}
				}
				if (this.valuesInRange(xPosCurrent - 1, yPosCurrent)) {
					if (locationIds.get(yPosCurrent).get(xPosCurrent - 1)
							.equals(String.valueOf(i))) { // links gehts weiter
						xPosCurrent--;
						neuerWeg.push('r');
					}
				}
				if (zielLoc != 'P') {
					i++;
				}
			}
		}

		return neuerWeg;
	}

	// Support Tiki
	private Stack<Character> fuelleStackFuerHomeposition(Mensch mensch,
			int xPosCurrent, int yPosCurrent) {
		Stack<Character> neuerWeg = new Stack<Character>();

		if ((mensch.getHomePosX() - Ressources.ZEROPOS.width)
				/ Ressources.RASTERHEIGHT + 1 == xPosCurrent) {
			neuerWeg.push('l');
		}
		if ((mensch.getHomePosX() - Ressources.ZEROPOS.width)
				/ Ressources.RASTERHEIGHT + 2 == xPosCurrent) {
			neuerWeg.push('l');
			neuerWeg.push('l');
		}
		if ((mensch.getHomePosX() - Ressources.ZEROPOS.width)
				/ Ressources.RASTERHEIGHT - 1 == xPosCurrent) {
			neuerWeg.push('r');
		}
		if ((mensch.getHomePosX() - Ressources.ZEROPOS.width)
				/ Ressources.RASTERHEIGHT - 2 == xPosCurrent) {
			neuerWeg.push('r');
			neuerWeg.push('r');
		}
		if ((mensch.getHomePosY() - Ressources.ZEROPOS.height)
				/ Ressources.RASTERHEIGHT - 1 == yPosCurrent) {
			neuerWeg.push('u');
		}
		if ((mensch.getHomePosY() - Ressources.ZEROPOS.height)
				/ Ressources.RASTERHEIGHT - 2 == yPosCurrent) {
			neuerWeg.push('u');
			neuerWeg.push('u');
		}
		if ((mensch.getHomePosY() - Ressources.ZEROPOS.height)
				/ Ressources.RASTERHEIGHT + 1 == yPosCurrent) {
			neuerWeg.push('o');
		}
		if ((mensch.getHomePosY() - Ressources.ZEROPOS.height)
				/ Ressources.RASTERHEIGHT + 2 == yPosCurrent) {
			neuerWeg.push('o');
			neuerWeg.push('o');
		}

		return neuerWeg;
	}

	// Support Tiki
	private ArrayList<ArrayList<String>> rasterkarteInitialisieren(
			ArrayList<ArrayList<String>> locationIds, String zielLocation,
			char locationId) {
		for (int i = 0; i < locationIds.size(); i++) {
			for (int j = 0; j < locationIds.get(i).size(); j++) {
				if (locationIds.get(i).get(j).charAt(0) != 'X'
						&& locationIds.get(i).get(j).charAt(0) != zielLocation
								.charAt(0)
						&& locationIds.get(i).get(j).charAt(0) != 'P'
						&& locationIds.get(i).get(j).charAt(0) != locationId) {
					locationIds.get(i).set(j, "You shall not pass!");
				}
				if (!(zielLocation.equals("P") && locationId == 'P')) {
					if (locationIds.get(i).get(j).charAt(0) == zielLocation
							.charAt(0)) {
						locationIds.get(i).set(j, "Z");
					}
					if (locationIds.get(i).get(j).charAt(0) == locationId) {
						locationIds.get(i).set(j, "X");
					}
				} else {
					if (locationIds.get(i).get(j).charAt(0) == 'X') {
						locationIds.get(i).set(j, "You shall not pass!");
					}
				}
				if (locationIds.get(i).get(j).charAt(0) == 'P') {
					locationIds.get(i).set(j, "X");
				}

			}
		}
		return locationIds;
	}

	// Support Tiki
	private void fuelleStackRumwuseln(int doTimes) {
		Stack<Character> neuerWeg = new Stack<Character>();
		ArrayList<ArrayList<String>> locationIds;
		locationIds = Ressources.getLocation_ids();

		locationIds.get(
				(this.agent.getPosY() - Ressources.ZEROPOS.height)
						/ Ressources.RASTERHEIGHT).set(
				(this.agent.getPosX() - Ressources.ZEROPOS.width)
						/ Ressources.RASTERHEIGHT, "0");

		locationIds = this.rasterkarteInitialisieren(locationIds, String
				.valueOf(this.getAgent().getLocationId()), this.getAgent()
				.getLocationId());

		locationIds.get(
				(this.agent.getPosY() - Ressources.ZEROPOS.height)
						/ Ressources.RASTERHEIGHT).set(
				(this.agent.getPosX() - Ressources.ZEROPOS.width)
						/ Ressources.RASTERHEIGHT, "Z");

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 24; j++) {
				if ((locationIds.get(i).get(j) == "Z" || locationIds.get(i)
						.get(j) == "0")
						&& locationIds.get(i + 1).get(j) == "Z"
						&& locationIds.get(i - 1).get(j) == "Z"
						&& locationIds.get(i).get(j + 1) == "Z"
						&& locationIds.get(i).get(j - 1) == "Z") {

					// Nun läuft der Agent lustig hin und her
					for (int k = 0; k < doTimes; k++) {
						neuerWeg.push('r');
						neuerWeg.push('o');
						neuerWeg.push('l');
						neuerWeg.push('u');
						neuerWeg.push('r');
						neuerWeg.push('u');
						neuerWeg.push('l');
						neuerWeg.push('o');
						neuerWeg.push('o');
						neuerWeg.push('l');
						neuerWeg.push('u');
						neuerWeg.push('u');
						neuerWeg.push('r');
						neuerWeg.push('o');
					}

					if (this.agent.getPosX() == this.agent.getHomePosX()
							&& this.agent.getPosY() == this.agent.getHomePosY()) {
						neuerWeg.push('u');
						neuerWeg.push('r');
					} else {
						if ((this.agent.getPosX() - Ressources.ZEROPOS.width)
								/ Ressources.RASTERHEIGHT == j
								&& (this.agent.getPosY() - Ressources.ZEROPOS.height)
										/ Ressources.RASTERHEIGHT == i + 1) {
							neuerWeg.push('o');
						} else {
							if ((this.agent.getPosX() - Ressources.ZEROPOS.width)
									/ Ressources.RASTERHEIGHT != j
									|| (this.agent.getPosY() - Ressources.ZEROPOS.height)
											/ Ressources.RASTERHEIGHT != i) {
								// Der Agent wird zum Mittelpunkt des Hauses
								// geleitet
								if (locationIds.get(i - 2).get(j - 1)
										.equals("X")
										|| locationIds.get(i - 1).get(j - 2)
												.equals("X")) {
									neuerWeg.push('u');
									neuerWeg.push('r');
								}
								if (locationIds.get(i - 2).get(j).equals("X")) {
									neuerWeg.push('u');
								}
								if (locationIds.get(i - 2).get(j + 1)
										.equals("X")
										|| locationIds.get(i - 1).get(j + 2)
												.equals("X")) {
									neuerWeg.push('u');
									neuerWeg.push('l');
								}
								if (locationIds.get(i).get(j + 2).equals("X")) {
									neuerWeg.push('l');
								}
								if (locationIds.get(i + 1).get(j + 2)
										.equals("X")
										|| locationIds.get(i + 2).get(j + 1)
												.equals("X")) {
									neuerWeg.push('o');
									neuerWeg.push('l');
								}
								if (locationIds.get(i + 2).get(j).equals("X")) {
									neuerWeg.push('o');
								}
								if (locationIds.get(i + 2).get(j - 1)
										.equals("X")
										|| locationIds.get(i + 1).get(j - 2)
												.equals("X")) {
									neuerWeg.push('o');
									neuerWeg.push('r');
								}
								if (locationIds.get(i).get(j - 2).equals("X")) {
									neuerWeg.push('r');
								}
							}
						}
					}
				}
			}
		}
		this.getAgent().setMoves(neuerWeg);
	}

	// Support Tiki
	public void doSomethingAfterAgentAction() {
		int personId = 0;

		if (!this.wieschteAktion && this.getAgent().getMussWuseln() != "Park"
				&& this.getAgent().getMussWuseln() != "Park+") {
			if (this.getAgent().getMussWuseln().charAt(1) <= '9'
					&& this.getAgent().getMussWuseln().charAt(1) >= '0') {
				personId = Integer.parseInt(this.getAgent().getMussWuseln()
						.substring(0, 2));
			} else {
				personId = Integer.parseInt(this.getAgent().getMussWuseln()
						.substring(0, 1));
			}
		}

		// Spionage
		if (this.getAgent().getMussWuseln().equals("Wanze+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().add("Wanze");
			this.getHouses()
					.get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(
							(float) (Math.random() * 17.5f + 1) + 22.5f, 0);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Wanze")) {
			this.fuelleStackRumwuseln(2);
			this.getAgent().setMussWuseln("Wanze+");
		}

		if (this.getAgent().getMussWuseln().equals("Kamera+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().add("Kamera");
			this.getHouses()
					.get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(
							(float) (Math.random() * 10 + 1) + 35, 1);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Kamera")) {
			this.fuelleStackRumwuseln(3);
			this.getAgent().setMussWuseln("Kamera+");
		}

		if (this.getAgent().getMussWuseln().equals("Hacken+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().add("Hacken");
			this.getHouses()
					.get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(
							(float) (Math.random() * 20 + 1) + 15, 2);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Hacken")) {
			this.fuelleStackRumwuseln(2);
			this.getAgent().setMussWuseln("Hacken+");
		}

		if (this.getAgent().getMussWuseln().length() == 10
				&& this.getAgent().getMussWuseln().substring(1, 10)
						.equals("Fernglas+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses()
					.get(this.getAgent().getMussWuseln().charAt(0) - 48 - 1)
					.getUeberwachungsmodule().add("Fernglas");
			this.getHouses()
					.get(this.getAgent().getMussWuseln().charAt(0) - 48 - 1)
					.setUeberwachungsWert((float) (Math.random() * 7 + 1) + 8,
							3);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() == 9
				&& this.getAgent().getMussWuseln().substring(1, 9)
						.equals("Fernglas")) {
			this.fuelleStackRumwuseln(1);
			this.getAgent()
					.setMussWuseln(this.getAgent().getMussWuseln() + "+");
		}

		// if(get_agent().getMussWuseln().equals("Park+") &&
		// get_agent().getCurrentMove()=='n'){
		// berechne_weg(null, agent, 'P');
		// calcMisstrauenAfterBeschwichtigenInPark();
		// }
		if (this.getAgent().getMussWuseln().equals("Park")) {
			this.calcWeg(this.agent, 'P');
			this.calcMisstrauenNachBeschwichtigenInPark();
		}

		// Spionage entfernen

		if (this.getAgent().getMussWuseln().equals("Wanzer+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().remove("Wanze");
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(0, 0);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Wanzer")) {
			this.fuelleStackRumwuseln(2);
			this.getAgent().setMussWuseln("Wanzer+");
		}

		if (this.getAgent().getMussWuseln().equals("Kamerar+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().remove("Kamera");
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(0, 1);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Kamerar")) {
			this.fuelleStackRumwuseln(3);
			this.getAgent().setMussWuseln("Kamerar+");
		}

		if (this.getAgent().getMussWuseln().equals("Hackenr+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.getUeberwachungsmodule().remove("Hacken");
			this.getHouses().get(this.getAgent().getLocationId() - 48 - 1)
					.setUeberwachungsWert(0, 2);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().equals("Hackenr")) {
			this.fuelleStackRumwuseln(2);
			this.getAgent().setMussWuseln("Hackenr+");
		}

		if (this.getAgent().getMussWuseln().length() == 11
				&& this.getAgent().getMussWuseln().substring(1, 11)
						.equals("Fernglasr+")
				&& this.getAgent().getCurrentMove() == 'n') {
			this.getHouses()
					.get(this.getAgent().getMussWuseln().charAt(0) - 48)
					.getUeberwachungsmodule().remove("Fernglas");
			this.getHouses()
					.get(this.getAgent().getMussWuseln().charAt(0) - 48)
					.setUeberwachungsWert(0, 3);
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() == 10
				&& this.getAgent().getMussWuseln().substring(1, 10)
						.equals("Fernglasr")) {
			this.fuelleStackRumwuseln(1);
			this.getAgent()
					.setMussWuseln(this.getAgent().getMussWuseln() + "+");
		}

		// Soziales
		if (this.getAgent().getMussWuseln().length() >= 8
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 8)
						.equals("Kuchen+")
				|| this.getAgent().getMussWuseln().length() >= 9
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 9)
						.equals("Kuchen+")) {
			this.calcMisstrauenNachBeschwichtigen(0,
					this.getPeople().get(personId));
			this.getPeople().get(personId)
					.erhoehe_durchgefuehrteBeschwichtigungen(0);
			this.getPeople().get(personId).setMoves(new Stack());
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() >= 7
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 7)
						.equals("Kuchen")
				|| this.getAgent().getMussWuseln().length() >= 8
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 8)
						.equals("Kuchen")) {
			this.getAgent().setMussWuseln(personId + "Kuchen+");
			// wuseln
		}

		if (this.getAgent().getMussWuseln().length() >= 13
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 13)
						.equals("Unterhalten+")
				|| this.getAgent().getMussWuseln().length() >= 14
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 14)
						.equals("Unterhalten+")) {
			this.calcMisstrauenNachBeschwichtigen(1,
					this.getPeople().get(personId));
			this.getPeople().get(personId)
					.erhoehe_durchgefuehrteBeschwichtigungen(1);
			this.getPeople().get(personId).setMoves(new Stack());
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() >= 12
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 12)
						.equals("Unterhalten")
				|| this.getAgent().getMussWuseln().length() >= 13
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 13)
						.equals("Unterhalten")) {
			this.getAgent().setMussWuseln(personId + "Unterhalten+");
			// wuseln
		}

		if (this.getAgent().getMussWuseln().length() >= 9
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 9)
						.equals("Flirten+")
				|| this.getAgent().getMussWuseln().length() >= 10
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 10)
						.equals("Flirten+")) {
			this.calcMisstrauenNachBeschwichtigen(2,
					this.getPeople().get(personId));
			this.getPeople().get(personId)
					.erhoehe_durchgefuehrteBeschwichtigungen(2);
			this.getPeople().get(personId).setMoves(new Stack());
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() >= 8
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 8)
						.equals("Flirten")
				|| this.getAgent().getMussWuseln().length() >= 9
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 9)
						.equals("Flirten")) {
			this.getAgent().setMussWuseln(personId + "Flirten+");
			// wuseln
		}

		if (this.getAgent().getMussWuseln().length() >= 6
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 6)
						.equals("Hand+")
				|| this.getAgent().getMussWuseln().length() >= 7
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 7)
						.equals("Hand+")) {
			this.calcMisstrauenNachBeschwichtigen(3,
					this.getPeople().get(personId));
			this.getPeople().get(personId)
					.erhoehe_durchgefuehrteBeschwichtigungen(3);
			this.getPeople().get(personId).setMoves(new Stack());
			this.getAgent().setMussWuseln("");
		}
		if (this.getAgent().getMussWuseln().length() >= 5
				&& personId <= 9
				&& this.getAgent().getMussWuseln().substring(1, 5)
						.equals("Hand")
				|| this.getAgent().getMussWuseln().length() >= 6
				&& personId > 9
				&& this.getAgent().getMussWuseln().substring(2, 6)
						.equals("Hand")) {
			this.getAgent().setMussWuseln(personId + "Hand+");
			// wuseln
		}

	}

	// Support Tiki
	private void calcColouredPeople(int hausid) {
		for (int i = 0; i < this.getPeople().size(); i++) {
			if (this.getPeople().get(i).getHausId() == hausid
					&& !this.getPeople().get(i).getIstFarbig()) {
				this.getPeople().get(i).setIstFarbig(true);
				this.getPeople().get(i).farbeZeigen(true);
			}
		}
	}

	// Support Tiki
	private boolean valuesInRange(int x, int y) {
		if (x >= 0 && x < Ressources.MAPWIDTH / Ressources.RASTERHEIGHT
				&& y >= 0 && y < Ressources.MAPHEIGHT / Ressources.RASTERHEIGHT)
			return true;
		else
			return false;
	}

	// Support Tiki
	public boolean calcGameOver() {
		if (this.calcMisstrauenInStreet() >= 90.0)
			return true;
		for (int i = 0; i < this.people.size(); i++) {
			if (this.people.get(i).getBorder() != null
					&& this.people.get(i).getCurrentMove() == 'n'
					&& this.people.get(i).getLocationId() == 'E')
				return true;
			if (this.people.get(i) instanceof Terrorist
					&& this.people.get(i).get_misstrauen() >= 85.00) {
				if (this.people.get(i).getCurrentMove() == 'n') {
					this.calcWeg(this.people.get(i), 'E');
					this.people.get(i).setBorder(
							BorderFactory.createLineBorder(Color.red));
					return false;
				}
			}

		}
		return false;
	}

	public ArrayList<Person> getPeople() {
		return this.people;
	}

	public void setPeople(Person p) {
		this.people.add(p);
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public int getSpielTag() {
		return this.spielTag;
	}

	public int getSpielStunde() {
		return this.spielStunde;
	}

	public int getSpielMinute() {
		return this.spielMinute;
	}

	public String getSpielzeitAsString() {
		String zeit = "";
		zeit = String.valueOf(this.spielStunde);
		if (this.spielStunde <= 9) {
			zeit = "0" + zeit;
		}
		zeit = zeit + ":";
		if (this.spielMinute <= 9) {
			zeit = zeit + "0";
		}
		zeit = zeit + String.valueOf(this.spielMinute);

		return zeit;
	}

	public ArrayList<Haus> getHouses() {
		return this.houses;
	}

	public void setHouses(Haus haus) {
		this.houses.add(haus);
	}

	// public boolean isWieeeeschteAktion() {
	// return wieeeeschteAktion;
	// }

	public void setWieschteAktion(boolean wieeeeschteAktion) {
		this.wieschteAktion = wieeeeschteAktion;
	}

	public boolean isWieschteAktion() {
		return this.wieschteAktion;
	}

	public void calcMisstrauenMax() {
		double misstrauen = this.calcMisstrauenInStreet();
		if (this.misstrauenMax < misstrauen) {
			this.misstrauenMax = misstrauen;
		}
	}

	public double getMisstrauenMax() {
		return this.misstrauenMax;
	}
}
