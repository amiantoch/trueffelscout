package com.trueffelscout.trueffelscout;

import java.util.Arrays;

import services.WidgetUpdateService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TrueffelWidgetProvider extends AppWidgetProvider{
	 public static final String DEBUG_TAG = "TrueffelWidgetProvider";

	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
	  Log.i("TrueffelscoutWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
	  Intent serviceIntent = new Intent(context, WidgetUpdateService.class);
      context.startService(serviceIntent);
     
    }
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		//Intent serviceIntent = new Intent(context, WidgetUpdateService.class);
		//context.stopService(serviceIntent);
		super.onDeleted(context, appWidgetIds);
	}
	
}
