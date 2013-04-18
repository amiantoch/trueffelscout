package views;

import java.util.Locale;

import com.trueffelscout.trueffelscout.R;
import com.trueffelscout.trueffelscout.TrueffelscoutActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SettingsDialog extends Dialog {
	private Activity activity;
	
	public SettingsDialog(Activity context) {
		super(context);
		this.activity = context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_language_settings);
		Button en = (Button) findViewById(R.id.iv_en);
		en.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				changeLanguage("en");
				if(activity instanceof TrueffelscoutActivity){
					((TrueffelscoutActivity) activity).updateTrueffel();
				}
				dismiss();
			}
		});
		Button de = (Button) findViewById(R.id.iv_de);
		de.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				changeLanguage("de");
				if(activity instanceof TrueffelscoutActivity){
					((TrueffelscoutActivity) activity).updateTrueffel();
				}
				dismiss();
			}
		});
	}

	private void changeLanguage(String language_code){
    	Resources res = activity.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);
    }
}
