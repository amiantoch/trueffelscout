package com.trueffelscout.tsadmin.messages;

import java.util.ArrayList;

import android.os.Bundle;

import com.trueffelscout.tsadmin.TSActivity;
import com.trueffelscout.tsadmin.model.Message;
import com.trueffelscout.tsadmin.model.Trueffel;

public class MessageActivity extends TSActivity {
	
	private Message message;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		message = (Message) getIntent().getSerializableExtra("message");
	}

	@Override
	public void update(ArrayList<Trueffel> trufe) {
		// TODO Auto-generated method stub
		
	}
	
}
