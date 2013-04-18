package com.trueffelscout.tsadmin.gcm;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;
import com.trueffelscout.tsadmin.R;

public class GCMReceiver extends GCMBroadcastReceiver {
	
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return context.getPackageName()+".gcm.GCMIntentService";
	}
	
}
