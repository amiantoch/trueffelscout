package com.trueffelscout.tsadmin;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;

import com.actionbarsherlock.app.SherlockActivity;
import com.trueffelscout.tsadmin.model.Message;
import com.trueffelscout.tsadmin.model.Trueffel;

public abstract class TSActivity extends SherlockActivity {
	
	protected static Message message_; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	public void update(ArrayList<Trueffel> trufe){};
	public void update(){};
	
	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	public abstract void setProgress(boolean show);
	
}
