package com.trueffelscout.tsadmin.gcm;

import static com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities.SENDER_ID;
import static com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities.SETTINGS;
import static com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities.displayMessage;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.trueffelscout.tsadmin.R;
import com.trueffelscout.tsadmin.gcm.utilities.CommonUtilities;
import com.trueffelscout.tsadmin.gcm.utilities.ServerUtilities;
import com.trueffelscout.tsadmin.messages.TSMessagesActivity;

public class GCMIntentService extends GCMBaseIntentService {
	
	private final static String TAG="GCMIntentService";
	
	public GCMIntentService(){
		super(SENDER_ID);
	}
	
	@Override
	protected void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		 Log.i(TAG, "Received message");
	     String message = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
	     String name = intent.getExtras().getString(CommonUtilities.EXTRA_NAME);
         String phone = intent.getExtras().getString(CommonUtilities.EXTRA_PHONE);
         String mail = intent.getExtras().getString(CommonUtilities.EXTRA_MAIL);
         displayMessage(context, message, name, phone, mail);
        // notifies user
         generateNotification(context, message, name, phone, mail);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.i(TAG,"Device registered to gcm, REGID: "+regId);
		CommonUtilities.REG_ID = regId;
		SharedPreferences sp = context.getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
		String user = sp.getString("user", "");
		String email = sp.getString("email", "");
		sp.edit().putString("REG_ID", regId).commit();
		CommonUtilities.REG_ID=regId;
		//displayMessage(context, );
		Toast.makeText(context, getString(R.string.server_register), Toast.LENGTH_LONG);
		ServerUtilities.register(context, regId, user, email);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.v(TAG,"device unregistered");
		Toast.makeText(context, getString(R.string.server_unregister), Toast.LENGTH_LONG);
		//displayMessage(context, getString(R.string.server_unregister));
        ServerUtilities.unregister(context, regId, "ami");
	}
	
	@Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message, "Deleted message", "", "");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }
    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String name, String phone, String mail) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
       
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, name, when);
 
        String title = context.getString(R.string.app_name);
 
        Intent notificationIntent = new Intent(context, TSMessagesActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        notification.setLatestEventInfo(context, name, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
 
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
 
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
 
    }

}
