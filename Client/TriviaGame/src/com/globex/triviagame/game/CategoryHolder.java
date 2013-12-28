package com.globex.triviagame.game;

import java.util.ArrayList;

public class CategoryHolder {

	private static ArrayList<String> catList = null;
	
	private CategoryHolder(){}

	public static ArrayList<String> getCatList(){
		return catList;
	}
	
	public static void setCatList(ArrayList<String> c) {
		catList = c;
	}

}
