package adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.trueffelscout.trueffelscoutapp.R;
import com.trueffelscout.trueffelscoutapp.TrueffelscoutActivity;


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
	private static final String HOST="http://www.trueffelscout.de";
	
	private List<Trueffel> items;
	private int resourceId;
	private TrueffelscoutActivity context;
	private ImageLoader imgLoader;
	private DisplayImageOptions options;
	
	public TrueffelAdapter(Context context, int resourceId, List<Trueffel> objects) {
		super(context, resourceId, objects);
		this.items=objects;
		this.resourceId=resourceId;
		this.context=(TrueffelscoutActivity)context;
		if(imgLoader==null){
			imgLoader = ImageLoader.getInstance();
			if(!imgLoader.isInited()){
				imgLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
			}
			options=new DisplayImageOptions.Builder()
			//.showStubImage(R.drawable.)
			//.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.trufa)
			.cacheInMemory()
			.cacheOnDisc()
			.displayer(new RoundedBitmapDisplayer(20))
			.build();

		}
	}
	
	@Override
	public int getCount(){
		return items.size();
	}
	
	@Override
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
            if(item.image!=null){
	            ImageView img = (ImageView) v.findViewById(R.id.image);
	            if(img!=null){
	            	
	            	imgLoader.displayImage(HOST+item.image,  img,options, new ImageLoadingListener() {
						
						public void onLoadingStarted(String arg0, View arg1) {
							
						}
						
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
													
						}
						
						public void onLoadingComplete(String arg0, View view, Bitmap arg2) {
							((ImageView)view).setImageBitmap(arg2);
						}
						
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
	            	});
	            	
	            }
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
            if(item.categories!=null){
	            for(int i=0;i<item.categories.size();i++){
	    			pb.setVisibility(View.GONE);
	    			cat3.setText("");
	    			pr3.setText("");
	            	if(i==0){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat1.setText(item.categories.get(i).category_en);
	            		else
	            			cat1.setText(item.categories.get(i).category);
	            		if(item.categories.get(i).price==0){
	            			pr1.setTextSize(12);
	            			pr1.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr1.setText(String.valueOf(item.categories.get(i).price)+ context.getResources().getString(R.string.unit));
	            		}
	            	}else if(i==1){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat2.setText(item.categories.get(i).category_en);
	            		else
	            			cat2.setText(item.categories.get(i).category);
	            		if(item.categories.get(i).price==0){
	            			pr2.setTextSize(12);
	            			pr2.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr2.setText(String.valueOf(item.categories.get(i).price)+ context.getResources().getString(R.string.unit));
	            		}
	            	}else if(i==2){
	            		if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase("en"))
	                		cat3.setText(item.categories.get(i).category_en);
	            		else
	            			cat3.setText(item.categories.get(i).category);
	            		if(item.categories.get(i).price==0){
	            			pr3.setTextSize(12);
	            			pr3.setText(context.getResources().getString(R.string.not_availeble));
	            		}else{
	            			pr3.setText(String.valueOf(item.categories.get(i).price)+ context.getResources().getString(R.string.unit));
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
