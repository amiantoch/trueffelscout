package com.trueffelscout.trueffelscout;

import com.trueffelscout.trueffelscout.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

public class AboutUsActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.about_us);
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
}
