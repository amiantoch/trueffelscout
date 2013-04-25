package com.trueffelscout.tsadmin.messages;

import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;
import com.trueffelscout.tsadmin.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trueffelscout.tsadmin.TSActivity;
import com.trueffelscout.tsadmin.model.Message;
import com.trueffelscout.tsadmin.model.Trueffel;
import com.trueffelscout.tsadmin.trueffels.TrueffelActivity;

public class MessageActivity extends TSActivity {
	
	private Message message;
	private TextView phone;
	private TextView mail;
	private ImageView callBtn;
	private ImageView smsBtn;
	private ImageView mailBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		message = (Message) getIntent().getSerializableExtra("message");
		
		initActionBar();
		
		TextView name = (TextView)findViewById(R.id.msg_name);
		if(message.getName()!=null){
			name.setText(message.getName());
		}
		phone = (TextView)findViewById(R.id.msg_phone);
		if(message.getPhone()!=null){
			phone.setText(message.getPhone());
		}
		mail = (TextView)findViewById(R.id.msg_mail);
		if(message.getMail()!=null){
			mail.setText(message.getMail());
		}
		TextView date = (TextView)findViewById(R.id.msg_date);
		date.setText(message.getDate());
		TextView msg = (TextView)findViewById(R.id.msg_msg);
		if(message.getMessage()!=null){
			msg.setText(message.getMessage());
		}
		
		getCallButton();
		getSmsButton();
		getMailButton();
		
	}
	
	private void initActionBar(){
        getSupportActionBar().setTitle(getString(R.string.messages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

	@Override
	public void update(ArrayList<Trueffel> trufe) {
		// TODO Auto-generated method stub
		
	}
	
	public ImageView getCallButton(){
		if(callBtn==null){
			callBtn = (ImageView) findViewById(R.id.msg_call1);
			callBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if(phone.getText().length()>0){
						Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone.getText().toString()));
					    startActivity(i);
					}else{
						Toast.makeText(MessageActivity.this, "Phone number missing!", Toast.LENGTH_SHORT);
					}
				}
			});
		}
		return callBtn;
	}
	
	public ImageView getSmsButton(){
		if(smsBtn==null){
			smsBtn = (ImageView) findViewById(R.id.msg_sms);
			smsBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if(phone.getText().length()>0){
						Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone.getText().toString()));
					    startActivity(i);
					}else{
						Toast.makeText(MessageActivity.this, "Phone number missing!", Toast.LENGTH_SHORT);
					}
				}
			});
		}
		return smsBtn;
	}
	
	public ImageView getMailButton(){
		if(mailBtn==null){
			mailBtn = (ImageView) findViewById(R.id.msg_mail_btn);
			mailBtn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					if(mail.getText().length()>0){
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",mail.getText().toString(), null));
					//emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trueffelscout");
					startActivity(Intent.createChooser(emailIntent, "Send email..."));
					}else{
						Toast.makeText(MessageActivity.this, "Email address missing!", Toast.LENGTH_SHORT);
					}
				}
			});
		}
		return smsBtn;
	}
	
}
