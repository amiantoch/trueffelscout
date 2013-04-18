package com.trueffelscout.tsadmin.gcm.utilities;

import android.content.Context;
import android.content.Intent;

public class CommonUtilities {
	// give your server registration url here
    public static final String SERVER_URL = "http://trueffelscout.de/mobile/gcm/register_gcm_device.php"; 
    public static final String SERVER_UNREGISTER = "http://trueffelscout.de/mobile/gcm/unregister_gcm_device.php";
    // Google project id
    public static final String SENDER_ID = "143536857289"; 
    //Shared pref id
    public static final String SETTINGS = "gcm_settings";
 
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "TsAdmin GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION = "com.trueffelscout.tsadmin.gcm.GCMReceiver";
 
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_MAIL = "mail";
    public static final String EXTRA_DATE = "date";
    
    public static String REG_ID;
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
    
    public static void displayMessage(Context context, String message, String name, String phone, String mail) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_PHONE, phone);
        intent.putExtra(EXTRA_MAIL, mail);
        context.sendBroadcast(intent);
    }
}
