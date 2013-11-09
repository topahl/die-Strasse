package com.stalkindustries.main.menu;

import java.util.ArrayList;
import java.util.Comparator;

public class CompareScore implements Comparator<ArrayList<String>>{


	@Override
	public int compare(ArrayList<String> o1, ArrayList<String> o2) {
		return (int)(Double.parseDouble(o2.get(1))-Double.parseDouble(o1.get(1)));
	}

}
