package com.stalkindustries.main.menu;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Comparator Klasse zum vergleichen von 2 Scores.
 * Hie sind schnell Anpassungen bezüglich der Sortierreihenfolge zu machen 
 * 
 * @author Tobias
 *
 */
public class CompareScore implements Comparator<ArrayList<String>>{

	/**
	 * @author Tobias
	 * @see Comparator#compare(Object, Object)
	 */
	@Override
	public int compare(ArrayList<String> o1, ArrayList<String> o2) {
		return (int)(Double.parseDouble(o2.get(1))-Double.parseDouble(o1.get(1)));
	}

}
