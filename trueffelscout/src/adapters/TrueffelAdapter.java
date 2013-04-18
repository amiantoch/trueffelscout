package adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.trueffelscout.trueffelscout.TrueffelscoutActivity;

import com.trueffelscout.trueffelscout.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TrueffelAdapter extends ArrayAdapter<Trueffel> {
	
	private List<Trueffel> items;
	private int resourceId;
	private TrueffelscoutActivity context;
	
	public TrueffelAdapter(Context context, int resourceId, List<Trueffel> objects) {
		super(context, resourceId, objects);
		this.items=objects;
		this.resourceId=resourceId;
		this.context=(TrueffelscoutActivity)context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(this.resourceId, null);
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.form_bg2);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
            bitmapDrawable.setTileModeY(Shader.TileMode.REPEAT);
            v.setBackgroundDrawable(bitmapDrawable);
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/dandelion.ttf");
        
        final Trueffel item = items.get(position);
        if (item != null) {             
            TextView name = (TextView) v.findViewById(R.id.name);
            if (name != null) {
            	if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
            		name.setText(item.name_en);
            	else
            		name.setText(item.name);
                name.setTypeface(tf);
            }
            ImageView img = (ImageView) v.findViewById(R.id.image);
            if(img!=null){
            	img.setImageDrawable(item.image);
            }
            ProgressBar pb = (ProgressBar) v.findViewById(R.id.progressBar);
            if(!context.hasInternetConnection()){
    			pb.setVisibility(View.GONE);
            }else{
            	pb.setVisibility(View.VISIBLE);
            }
            TextView cat1 = (TextView)v.findViewById(R.id.category1);
            cat1.setTypeface(tf);
			TextView pr1 = (TextView)v.findViewById(R.id.price1);
			pr1.setTypeface(tf);
			TextView cat2 = (TextView)v.findViewById(R.id.category2);
			cat2.setTypeface(tf);
			TextView pr2 = (TextView)v.findViewById(R.id.price2);
			pr2.setTypeface(tf);
			TextView cat3 = (TextView)v.findViewById(R.id.category3);
			cat3.setTypeface(tf);
			TextView pr3 = (TextView)v.findViewById(R.id.price3);
			pr3.setTypeface(tf);
            if(item.types!=null){
	            for(int i=0;i<item.types.size();i++){
	    			pb.setVisibility(View.GONE);
	    			cat3.setText("");
	    			pr3.setText("");
	            	if(i==0){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat1.setText(item.types.get(i).type_en);
	            		else
	            			cat1.setText(item.types.get(i).type);
	            		if(item.types.get(i).price==0){
	            			pr1.setTextSize(12);
	            			pr1.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr1.setText(String.valueOf(item.types.get(i).price)+"€/kg");
	            		}
	            	}else if(i==1){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat2.setText(item.types.get(i).type_en);
	            		else
	            			cat2.setText(item.types.get(i).type);
	            		if(item.types.get(i).price==0){
	            			pr2.setTextSize(12);
	            			pr2.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr2.setText(String.valueOf(item.types.get(i).price)+"€/kg");
	            		}
	            	}else if(i==2){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat3.setText(item.types.get(i).type_en);
	            		else
	            			cat3.setText(item.types.get(i).type);
	            		if(item.types.get(i).price==0){
	            			pr3.setTextSize(12);
	            			pr3.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr3.setText(String.valueOf(item.types.get(i).price)+"€/kg");
	            		}
	            	}
	            }
            }else{
            	cat1.setText("");
            	pr1.setText("");
            	cat2.setText("");
            	pr2.setText("");
            }
            
        }
		return v;
	}

}
