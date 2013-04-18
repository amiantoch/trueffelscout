package com.trueffelscout.tsadmin.model;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class Trueffel {
	public int id;
	public String name;
	public String description;
	public Drawable image;
	public String visibility;
	public ArrayList<TrueffelType> types;
	
	public Trueffel(){
		
	}
	public Trueffel(int id,String name, String descr, Drawable img, String visibility){
		this.id=id;
		this.name=name;
		this.description=descr;
		this.image=img;
		this.visibility=visibility;
	}

}
