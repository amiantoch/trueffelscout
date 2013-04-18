package com.trueffelscout.trueffelscout;

import services.MessageAsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

public class MessageActivity extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_message);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
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
					new MessageAsyncTask(MessageActivity.this).execute(new String[]{key,name,phone,mail,msg});
			}
			
		});
	}

}
