package com.trueffelscout.tsadmin.messages;

import static com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities.SETTINGS;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gcm.GCMRegistrar;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissListViewTouchListener;
import com.trueffelscout.tsadmin.R;
import com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities;
import com.trueffelscout.tsadmin.gcm.utilities.ServerUtilities;
import com.trueffelscout.tsadmin.gcm.utilities.WakeLocker;
import com.trueffelscout.tsadmin.model.Message;
import com.trueffelscout.tsadmin.model.MessageController;
import com.trueffelscout.tsadmin.services.MessagesAsynkTask;

public class TSMessagesActivity extends SherlockActivity {
	private static final int messageRequestCode = 1;
	
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private String name;
	private String email;
	private TextView lblMessage;
	private ListView msgList;
	private MessageAdapter adapter;
	private SharedPreferences sp;
	private ActionBarSherlock ab;
	private ViewFlipper flipper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        
        sp = this.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
        flipper = (ViewFlipper) findViewById(R.id.flipper_register);
         
        getSupportActionBar().setTitle(R.string.messages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setMessagesList();
        if(savedInstanceState==null)
        	initGCMRegistration();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// getSupportMenuInflater().inflate(R.menu.activity_massages, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        break; 
	        /*
	    case R.id.menu_settings:
	    	register();
	    	break;
	    	*/
	    }
	    return true;
	}
	
	public ProgressBar getProgressBar(){
		return (ProgressBar) findViewById(R.id.progressBarMsg);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == messageRequestCode && resultCode == Activity.RESULT_OK){
			update(messages);
		}
	}
	
	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        return true;  
	    }
	    return true;
	}*/
	
	/*private void initActionBar(){
    	getSupportActionBar();
        //actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_demo));
        ab.setTitle(getString(R.string.messages));

        //ab.setHomeAction(new IntentAction(this, new Intent(this, TrueffelActivity.class), R.drawable.ic_title_home_default));
        ab.setDisplayHomeAsUpEnabled(true);
        
        
        final Action settingsAction = new IntentAction(this, new Intent(this, TrueffelActivity.class), R.drawable.ic_title_export_default);
        ab.addAction(settingsAction);
        
        
    }*/
	
	private void initGCMRegistration(){
		

		GCMRegistrar.checkDevice(this);
        
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
 
        //lblMessage = (TextView) findViewById(R.id.lblMessage);
 
        registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));
 
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        
        SharedPreferences sp = this.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
		final String name = sp.getString("user", "");
		final String email = sp.getString("email", "");
		        
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
        	register();
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
            	Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            	setSupportProgressBarIndeterminateVisibility(true);
            	new MessagesAsynkTask(this).execute(new String[]{});
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, regId, name, email);
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
                setSupportProgressBarIndeterminateVisibility(true);
                new MessagesAsynkTask(TSMessagesActivity.this).execute(new String[]{});
            }
        }
	}
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver(){
		@Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            String newName = intent.getExtras().getString(CommonUtilities.EXTRA_NAME);
            String newPhone = intent.getExtras().getString(CommonUtilities.EXTRA_PHONE);
            String newMail = intent.getExtras().getString(CommonUtilities.EXTRA_MAIL);
            String newDate = intent.getExtras().getString(CommonUtilities.EXTRA_DATE);
            if(newDate==null){
            	Time now = new Time();
            	now.setToNow();
            	newDate = now.toString();
            }
            Message message = new Message(newName, newPhone, newMail, newMessage, newDate);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
 
            // Showing received message
            messages.add(0,message);
            if(adapter==null){
            	adapter = new MessageAdapter();
            }else{
            	adapter.notifyDataSetChanged();
            }
            msgList.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
 
            // Releasing wake lock
            WakeLocker.release();
        }
    };
    
    private void register(){
    	flipper.setInAnimation(this, R.anim.left_in);
    	flipper.setOutAnimation(TSMessagesActivity.this, R.anim.left_out);
    	flipper.showNext();
    	
    	final EditText user = (EditText) findViewById(R.id.etUser);
    	final EditText email = (EditText) findViewById(R.id.etEmail);
    	
    	Button btnReg = (Button) findViewById(R.id.btnRegister);
    	btnReg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				sp = TSMessagesActivity.this.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
				Editor spEdit = sp.edit();
				spEdit.putString("user", user.getText().toString());
				spEdit.putString("email", email.getText().toString());
				spEdit.commit();
				GCMRegistrar.register(TSMessagesActivity.this, CommonUtilities.SENDER_ID);
				new MessagesAsynkTask(TSMessagesActivity.this).execute(new String[]{});
				flipper.showPrevious();
			}
		});
    	
    	Button btnUnreg = (Button) findViewById(R.id.btnUnregister);
    	btnUnreg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				sp = TSMessagesActivity.this.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
				Editor spEdit = sp.edit();
				spEdit.putString("user", user.getText().toString());
				spEdit.putString("email", email.getText().toString());
				spEdit.commit();
				GCMRegistrar.unregister(TSMessagesActivity.this);
				flipper.showPrevious();
			}
		});
    	
    }
    
    private void setMessagesList(){
    	msgList = (ListView) findViewById(R.id.msg_list);
    	adapter = new MessageAdapter();
    	msgList.setAdapter(adapter);
    	msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(TSMessagesActivity.this, MessageActivity.class);
				intent.putExtra("message", messages.get(position));
				startActivityForResult(intent, messageRequestCode);
			}
		});
    	SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(msgList, new OnDismissCallback() {
			public void onDismiss(AbsListView listView,	int[] reverseSortedPositions) {
				for (int position : reverseSortedPositions) {
    				Toast.makeText(getApplicationContext(), "Trufa:"+String.valueOf(position), Toast.LENGTH_SHORT).show();
    				MessageController.getInstance(getApplicationContext()).addDeletedMsg(messages.get(position));
    				messages.remove(position);
    				update(messages);
    			}
			}
		});
	  msgList.setOnTouchListener(touchListener);
    }
 
    @Override
	public void onBackPressed() {
	   // finish();
    	if(flipper!=null && flipper.getDisplayedChild()==1){
    		flipper.showPrevious();
    	}else{
    		super.onBackPressed();
    	}
	}
    
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

	public void update(ArrayList<Message> messages) {
		setSupportProgressBarIndeterminateVisibility(false);
		ArrayList<Message> messagesFiltered = MessageController.getInstance(this).filterDeletedMessages(messages);
		this.messages = messagesFiltered;
		adapter = new MessageAdapter();
		//this.adapter.notifyDataSetChanged();
		this.msgList.setAdapter(adapter);
	}
	
	private class MessageAdapter extends ArrayAdapter<Message>{
		
		public MessageAdapter() {
			super(TSMessagesActivity.this, R.layout.tsmessages_item, messages);
		}
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if( v == null){
	            LayoutInflater inflater = TSMessagesActivity.this.getLayoutInflater();
	            v = inflater.inflate(R.layout.tsmessages_item, parent, false);
			}
			if(!MessageController.getInstance(getApplicationContext()).isMessageSeen(messages.get(position))){
				v.setBackground(getResources().getDrawable(R.drawable.background_message_read_item));
			}else{
				v.setBackground(getResources().getDrawable(R.drawable.background_message_item));
			}
            TextView name = (TextView)v.findViewById(R.id.user_name);
            String who = "";
            if(messages.get(position).getName()!=null){
	            who = messages.get(position).getName().length()>0?messages.get(position).getName():
	            	messages.get(position).getMail().length()>0?messages.get(position).getMail():
	            		messages.get(position).getPhone();
            }
            name.setText(who);
            if(messages.get(position).getMessage()!=null){
	            String msgTxt = messages.get(position).getMessage().length()>30?
	            		messages.get(position).getMessage().substring(0, 30)+"...":
	            			messages.get(position).getMessage();
	            TextView msg = (TextView)v.findViewById(R.id.user_msg);
	            msg.setText(msgTxt);
            }
            TextView date = (TextView)v.findViewById(R.id.msg_date);
            date.setText(messages.get(position).getDate());
			
		return v;
		}
	}

}