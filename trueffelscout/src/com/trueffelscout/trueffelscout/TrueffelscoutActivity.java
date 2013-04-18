package com.trueffelscout.trueffelscout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import services.TrueffelAsyncTask;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.trueffelscout.trueffelscout.R;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
        getTrufe();
        adapter = new TrueffelAdapter(this,R.layout.trufa_item,trufe); 
        setListAdapter(adapter);
        
        ab = this.getSupportActionBar();
        getSettingsDialog();
        //ab.setIcon(bitmapDrawable);
        
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
	
	public void updateTrueffel(){
		this.adapter.notifyDataSetChanged();
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
		case R.id.settings:
			dialogSettings.show();
		}
		return true;
	}
    
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
			dialogSettings.setContentView(R.layout.dialog_language_settings);*/
    	//}
    }
    /*
    private void changeLanguage(String language_code){
    	Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
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
    	trufa1.name = getString(R.string.name_trufa_1);
    	trufa1.image = getResources().getDrawable(R.drawable.trufaneagra);
    	trufe.add(trufa1);
    	
    	Trueffel trufa2 = new Trueffel();
    	trufa2.name = getString(R.string.name_trufa_2);
    	trufa2.image = getResources().getDrawable(R.drawable.trufaalba);
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