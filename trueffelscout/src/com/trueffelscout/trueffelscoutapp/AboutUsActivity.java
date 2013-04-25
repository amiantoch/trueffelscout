package com.trueffelscout.trueffelscoutapp;

import java.util.Locale;

import services.TrueffelAsyncTask;
import views.KontaktDialog;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.trueffelscout.trueffelscoutapp.R;

import adapters.TrueffelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUsActivity extends SherlockActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.about_us);
        getSupportActionBar().setIcon(R.drawable.trufa);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.titlebar);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getWindow().setBackgroundDrawable(bitmapDrawable);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.about_us_layout);
        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.form_bg2);
        BitmapDrawable bitmapDrawable1 = new BitmapDrawable(bmp1);
        bitmapDrawable1.setTileModeY(Shader.TileMode.REPEAT);
        
        layout.setBackgroundDrawable(bitmapDrawable1);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater minfl = getSupportMenuInflater();
		minfl.inflate(R.menu.trueffel_menu, menu);
		//menu.findItem(R.id.uber_uns).setIcon(R.drawable.);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.uber_uns:
			finish();
			break;
		case android.R.id.home:
			finish();
			break;
		case R.id.contact:
			KontaktDialog dialog = new KontaktDialog(this);
	    	dialog.show();
	    	break;
		case R.id.refresh:
			Intent intent = new Intent(this, TrueffelscoutActivity.class);
			startActivity(intent);
			break;
		case R.id.en:
			//dialogSettings.show();
			changeLanguage("en");
			break;
		case R.id.de:
			//dialogSettings.show();
			changeLanguage("de");
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
	        String text = res.getString(R.string.uber_uns_text);
	        TextView txt = (TextView)findViewById(R.id.about_us_tv);
			txt.setText(text);
			Drawable dt = res.getDrawable(R.drawable.uberuns_act);
		    ImageView contact = (ImageView)findViewById(R.id.uber_uns_iv);
			contact.setImageDrawable(dt);
	    }
}
