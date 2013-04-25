package com.trueffelscout.trueffelscoutapp;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.trueffelscout.trueffelscoutapp.R;

import services.MessageAsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

public class MessageActivity extends SherlockActivity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_message);
        getSupportActionBar().setIcon(R.drawable.trufa);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.titlebar);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        ImageButton send = (ImageButton)findViewById(R.id.send_message);
		send.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				
					EditText name_et = (EditText)findViewById(R.id.msg_name);
					String name = name_et.getText().toString();
					EditText phone_et = (EditText)findViewById(R.id.msg_phone);
					String phone = phone_et.getText().toString();
					EditText mail_et = (EditText)findViewById(R.id.msg_mail);
					String mail = mail_et.getText().toString();
					EditText msg_et = (EditText)findViewById(R.id.msg_msg);
					String msg = msg_et.getText().toString();
					String key = "biri";
					android.content.res.Configuration conf = getResources().getConfiguration();
					String language = conf.locale.getLanguage();
					
					new MessageAsyncTask(MessageActivity.this).execute(new String[]{key,name,phone,mail,msg, language});
			}
			
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

}
