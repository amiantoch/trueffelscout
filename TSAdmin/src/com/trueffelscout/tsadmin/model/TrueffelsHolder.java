package com.trueffelscout.tsadmin.model;

import java.util.ArrayList;

public class TrueffelsHolder {
	private static ArrayList<Trueffel> trufe = new ArrayList<Trueffel>();
	
	public static void setTruffles(ArrayList<Trueffel> truffels){
		trufe = truffels;
	}
	
	public static ArrayList<Trueffel> getTruffels(){
		return trufe;
	}
	
}
