package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import adapters.Trueffel;
import adapters.TrueffelType;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.renderscript.Font;
import android.renderscript.Font.Style;
import android.view.View;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trueffelscout.trueffelscoutapp.R;
import com.trueffelscout.trueffelscoutapp.TrueffelWidgetProvider;
import com.trueffelscout.trueffelscoutapp.TrueffelscoutActivity;

public class WidgetUpdateService extends Service{
	private PriceUpdateTask updater;
	private AppWidgetManager appWidgetManager;
	private RemoteViews views;
	private ComponentName trueffelWidget;
	private IntentFilter mNetworkStateChangedFilter;
    private BroadcastReceiver mNetworkStateIntentReceiver;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Context context = WidgetUpdateService.this;
		
		Intent intent_pend = new Intent(context, TrueffelscoutActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_pend, 0);

        this.views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setOnClickPendingIntent(R.id.widget_view, pendingIntent);
        PendingIntent refresh_intent = PendingIntent.getService(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_refresh, refresh_intent);
        
       // views.setImageViewBitmap(R.id.widget_trufa1, buildUpdate(context.getString(R.string.trufa1_widget),context));
       // views.setImageViewBitmap(R.id.widget_trufa2, buildUpdate(context.getString(R.string.trufa2_widget),context));
        
        // Tell the AppWidgetManager to perform an update on the current app widget
        this.trueffelWidget = new ComponentName(context, TrueffelWidgetProvider.class);
        this.appWidgetManager = AppWidgetManager.getInstance(context);
		if(hasInternetConnection()){
			updater = new PriceUpdateTask();
			updater.execute();
		}else{
			views.setTextViewText(R.id.widget_pr1, "-");
			views.setTextViewText(R.id.widget_pr2, "");
			views.setTextViewText(R.id.widget_pr12, "-");
			views.setTextViewText(R.id.widget_pr22, "-");
			views.setTextViewText(R.id.widget_pr32, "-");
			views.setTextViewText(R.id.widget_last_update, getResources().getString(R.string.no_internet_connection));
		}
		appWidgetManager.updateAppWidget(this.trueffelWidget, views);
		if(!hasInternetConnection()){
			this.stopSelf();
		}
		return START_REDELIVER_INTENT;
	}
	
	public boolean hasInternetConnection(){
		ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
	    if (i == null)
		    return false;
		if (!i.isConnected())
		    return false;
		if (!i.isAvailable())
		    return false;
	    return true;
	}
	
	@Override
	public void onDestroy() {
		if(updater!=null){
			updater.cancel(true);
		}
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public Bitmap buildUpdate(String time, Context context) {
	    Bitmap myBitmap = Bitmap.createBitmap(160, 30, Bitmap.Config.ARGB_4444);
	    Canvas myCanvas = new Canvas(myBitmap);
	    Paint paint = new Paint();
	    Typeface clock = Typeface.createFromAsset(context.getAssets(),"fonts/dandelion.ttf");
	    paint.setAntiAlias(true);
	    paint.setSubpixelText(true);
	    paint.setTypeface(clock);
	    paint.setStyle(Paint.Style.FILL);
	    paint.setColor(context.getResources().getColor(R.color.title_color));
	    paint.setTextSize(20);
	  //  paint.setTextAlign(Align.CENTER);
	    myCanvas.drawText(time, 10, 20, paint);
	    return myBitmap;
    }

	
	private class PriceUpdateTask extends AsyncTask<String,Void,List<Trueffel>>{
		@Override
		protected void onPreExecute(){
			views.setViewVisibility(R.id.widget_refresh, View.GONE);
			views.setViewVisibility(R.id.widget_progress, View.VISIBLE);
		}
		
		@Override
		protected List<Trueffel> doInBackground(String... params) {
			List<Trueffel> trufe = new ArrayList<Trueffel>();
			URL url;		
			try{
		    	String feed = "http://www.trueffelscout.de/mobile/trueffels.php";
		    	url = new URL(feed);
		    	
		    	URLConnection connection = url.openConnection();
		    	HttpURLConnection httpconn = (HttpURLConnection) connection;
		    	
		    	int responseCode = httpconn.getResponseCode();
		    	
		    	if(responseCode==HttpURLConnection.HTTP_OK){
		    		InputStream is = httpconn.getInputStream();
		    		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		    		String page= "";
		    	    String inLine;
		    	    while ((inLine = in.readLine()) != null){
		    	     page += inLine;
		    	    }
		    	    in.close();

	                JSONObject json = new JSONObject(page);
	                JSONArray jArray = json.getJSONArray("trueffels");
	                Gson gson = new Gson();
	                List<Trueffel> trufe_loc = gson.fromJson(jArray.toString(), new TypeToken<List<Trueffel>>(){}.getType());
		    		System.out.println(trufe_loc.toString());
	                return trufe_loc;
		    		
		    	}
			}catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			return trufe;
		}
		 
	
		@Override
		protected void onPostExecute(List<Trueffel> result){
			if(result!=null&&result.size()>0){
				
				if(result.size()>2){
					views.setViewVisibility(R.id.widget_special, View.VISIBLE);
				}else{
					views.setViewVisibility(R.id.widget_special, View.GONE);
					views.setViewVisibility(R.id.widget_info, View.VISIBLE);
				}
				
				if(result.get(0).categories.get(0).price==0 && result.get(0).categories.get(1).price==0){
					views.setViewVisibility(R.id.not_availible_1, View.VISIBLE);
					views.setTextViewText(R.id.not_availible_1, getResources().getString(R.string.not_availeble));
					views.setTextViewText(R.id.widget_pr1, "");
					views.setTextViewText(R.id.widget_pr2, "");
				}else{
					views.setViewVisibility(R.id.not_availible_1, View.GONE);
					if(result.get(0).categories.get(0).price!=0){
						views.setTextViewText(R.id.widget_pr1, "Kat.1: "+String.valueOf(result.get(0).categories.get(0).price)+ getResources().getString(R.string.unit_eur));
					}else{
						views.setTextViewText(R.id.widget_pr1, "");
					}
					if(result.get(0).categories.get(1).price!=0){
						//views.setFloat(R.id.widget_pr2, "setTextSize", 12);
						views.setTextViewText(R.id.widget_pr2, "Kat.2: "+String.valueOf(result.get(0).categories.get(1).price)+ getResources().getString(R.string.unit_eur));
					}else{
						views.setTextViewText(R.id.widget_pr2, "");
					}
				}
				if(result.get(1).categories.get(0).price==0 && result.get(1).categories.get(1).price==0 && result.get(1).categories.get(2).price==0){
					views.setViewVisibility(R.id.not_availible_2, View.VISIBLE);
					views.setTextViewText(R.id.not_availible_2, getResources().getString(R.string.not_availeble));
					views.setTextViewText(R.id.widget_pr12, "");
					views.setTextViewText(R.id.widget_pr22, "");
					views.setTextViewText(R.id.widget_pr32, "");
				}else{
					views.setViewVisibility(R.id.not_availible_2, View.GONE);
					if(result.get(1).categories.get(0).price!=0){
						views.setTextViewText(R.id.widget_pr12, "Kat.1: "+String.valueOf(result.get(1).categories.get(0).price)+getResources().getString(R.string.unit_eur));
					}else{
						views.setTextViewText(R.id.widget_pr12, "");
					}
					if(result.get(1).categories.get(1).price!=0){
						views.setFloat(R.id.widget_pr22, "setTextSize", 12);
						views.setTextViewText(R.id.widget_pr22, "Kat.2: "+String.valueOf(result.get(1).categories.get(1).price)+getResources().getString(R.string.unit_eur));
					}else{
						views.setTextViewText(R.id.widget_pr22, "");
					}
					if(result.get(1).categories.get(2).price!=0){
						//views.setFloat(R.id.widget_pr32, "setTextSize", 12);
						views.setTextViewText(R.id.widget_pr32, "Kat.3: "+String.valueOf(result.get(1).categories.get(2).price)+getResources().getString(R.string.unit_eur));
					}else{
						views.setTextViewText(R.id.widget_pr32,"");
					}
				}
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				Date date = new Date();
				views.setTextViewText(R.id.widget_last_update, "Update:"+dateFormat.format(date));
			}else{
				views.setTextViewText(R.id.widget_pr1, "-");
				views.setTextViewText(R.id.widget_pr12, "-");
			}
			views.setViewVisibility(R.id.widget_progress, View.GONE);
			views.setViewVisibility(R.id.widget_refresh, View.VISIBLE);
			appWidgetManager.updateAppWidget(trueffelWidget, views);
			WidgetUpdateService.this.stopSelf();
		}
	}
}

