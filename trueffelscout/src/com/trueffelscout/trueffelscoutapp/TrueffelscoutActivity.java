package com.trueffelscout.trueffelscoutapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import services.PhoneRequest;
import services.TrueffelAsyncTask;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.trueffelscout.trueffelscoutapp.R;

import views.KontaktDialog;
import views.SettingsDialog;
import adapters.Trueffel;
import adapters.TrueffelAdapter;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TrueffelscoutActivity extends SherlockListActivity {	
	List<Trueffel> trufe;
	private IntentFilter mNetworkStateChangedFilter;
    private BroadcastReceiver mNetworkStateIntentReceiver;
    private TrueffelAdapter adapter;
    private ActionBar ab;
    private Dialog dialogSettings;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getWindow().setBackgroundDrawable(bitmapDrawable);
        //ListView trufe_list = this.getListView();
        
        ImageView contact = (ImageView)findViewById(R.id.contact);
        contact.setImageResource(R.drawable.buton_kontakt);
        getTrufe();
        adapter = new TrueffelAdapter(this,R.layout.trufa_item,trufe); 
        setListAdapter(adapter);
       
        
        ab = this.getSupportActionBar();
        ab.setIcon(R.drawable.titlebar);
        
        
        if(!hasInternetConnection()){
			Toast.makeText(this, "Keine Netzverbindung!", Toast.LENGTH_SHORT).show();
		}
        
        mNetworkStateChangedFilter = new IntentFilter();
		mNetworkStateChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

		mNetworkStateIntentReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
					if(!noConnectivity){
						new TrueffelAsyncTask(TrueffelscoutActivity.this,adapter,trufe).execute(new String[]{});
						new PhoneRequest((TsApplication) getApplication()).execute();
					}else{
						getTrufe();
						adapter.notifyDataSetChanged();
						setListAdapter(adapter);
					}
				}
		    }
		};
		//registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);
    }
	
	@Override
    public void onResume() {
		super.onResume();
		registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);
		//unregisterReceiver(mNetworkStateIntentReceiver);
		//registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);
	}
	
	@Override
    public void onPause() {
		super.onPause();
		unregisterReceiver(mNetworkStateIntentReceiver);
    }
	
	@Override
	public void onDestroy(){
		//unregisterReceiver(mNetworkStateIntentReceiver);
		super.onDestroy();
	}
	
	public void updateTrueffel(List<Trueffel> trueffel){
		//this.adapter.notifyDataSetChanged();
		if(trueffel==null){
			adapter.notifyDataSetChanged();
		}else{
			trufe = trueffel;
			adapter = new TrueffelAdapter(this,R.layout.trufa_item,trufe); 
		}
		//.setImageResource(R.drawable.buton_kontakt);
		this.setListAdapter(adapter);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater minfl = getSupportMenuInflater();
		minfl.inflate(R.menu.trueffel_menu, menu);
		menu.findItem(R.id.uber_uns).setIntent(new Intent(this,AboutUsActivity.class));
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.uber_uns:
			startActivity(item.getIntent());
			break;
		case R.id.contact:
			KontaktDialog dialog = new KontaktDialog(this);
	    	dialog.show();
	    	break;
		case R.id.refresh:
			if(this.hasInternetConnection()){
				this.getTrufe();
				adapter = new TrueffelAdapter(this,R.layout.trufa_item,trufe); 
		        setListAdapter(adapter);
				new TrueffelAsyncTask(TrueffelscoutActivity.this,adapter,trufe).execute(new String[]{});
			}else{
				Toast.makeText(this, "Keine Netzverbindung!", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.en:
			//dialogSettings.show();
			changeLanguage("en");
			updateTrueffel(trufe);
			break;
		case R.id.de:
			//dialogSettings.show();
			changeLanguage("de");
			updateTrueffel(trufe);
			break;
		}
		return true;
	}
    
    private void changeLanguage(String language_code){
    	Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
        Drawable dt = res.getDrawable(R.drawable.buton_kontakt);
        ImageView contact = (ImageView)findViewById(R.id.contact);
		contact.setImageDrawable(dt);
    }
    /*
    public void getSettingsDialog(){
    	//if(dialogSettings==null){
	    	dialogSettings = new SettingsDialog(this);
			/*LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_language_settings, null);
			Button en = (Button) layout.findViewById(R.id.iv_en);
			en.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					changeLanguage("en");
					dialogSettings.dismiss();
				}
			});
			Button de = (Button) layout.findViewById(R.id.iv_de);
			de.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					changeLanguage("de");
					dialogSettings.dismiss();
				}
			});
			dialogSettings.setContentView(R.layout.dialog_language_settings);
    	//}
    }*/
    
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
    
    public void getTrufe(){
    	this.trufe = new ArrayList<Trueffel>();
    	Trueffel trufa1 = new Trueffel();
    	trufa1.name = getResources().getString(R.string.name_trufa_1);
    	trufa1.name_en = getResources().getString(R.string.name_trufa_1);
    	//trufa1.image = getResources().getDrawable(R.drawable.trufaneagra).toString();
    	trufe.add(trufa1);
    	
    	Trueffel trufa2 = new Trueffel();
    	trufa2.name = getResources().getString(R.string.name_trufa_2);
    	trufa2.name_en = getResources().getString(R.string.name_trufa_2);
    	//trufa2.image = getResources().getDrawable(R.drawable.trufaalba).toString();
    	trufe.add(trufa2);
    	
    }
    
    public void open_contact_dialog(View v){  
    	KontaktDialog dialog = new KontaktDialog(this);
    	dialog.show();
    }

    public void about_us(View view){
    	startActivity(new Intent(this,AboutUsActivity.class));
    }
    
}