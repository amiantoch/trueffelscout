package com.trueffelscout.trueffelscoutapp;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TsApplication extends Application {
	public static final String TS_PREF="prefferences";
	public static final String PHONE_PREF="phone_prefferences";
	
	private String phoneNUmber;
	private SharedPreferences sp;
	
	@Override
	public void onCreate(){
		super.onCreate();
		sp = this.getSharedPreferences(TS_PREF, Activity.MODE_PRIVATE);
	}

	public String getPhone() {
		return sp.getString(PHONE_PREF, null);
	}

	public void setPhone(String phoneNUmber) {
		if(phoneNUmber!=null || phoneNUmber.length()>0){
			Editor edit = sp.edit();
			edit.putString(PHONE_PREF, phoneNUmber);
			edit.commit();
		}
	}
	
	

}
