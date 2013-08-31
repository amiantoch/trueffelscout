package com.trueffelscout.tsadmin.trueffels;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.trueffelscout.tsadmin.R;
import com.trueffelscout.tsadmin.TSActivity;
import com.trueffelscout.tsadmin.messages.TSMessagesActivity;
import com.trueffelscout.tsadmin.model.Trueffel;
import com.trueffelscout.tsadmin.model.TrueffelsHolder;
import com.trueffelscout.tsadmin.services.TrueffelAsyncTask;
import com.trueffelscout.tsadmin.view.TrueffelItemDialog;

public class TrueffelActivity extends TSActivity{
	protected static final int editRequestCode = 1;
	private int SCREEN_ORIENTATION=0;
	
	private IntentFilter mNetworkStateChangedFilter;
    private BroadcastReceiver mNetworkStateIntentReceiver;
    private ListView truf_list;
    private ArrayAdapter<Trueffel> adapter;
    private ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_main);
        
        this.SCREEN_ORIENTATION = getScreenOrientation();
        if(savedInstanceState == null){
        	checkNetwork();
        }
        getSupportActionBar().setTitle(getString(R.string.home));
        initListView();
        pb = (ProgressBar)findViewById(R.id.progressBar);
    }
    
    private void checkNetwork(){
    	mNetworkStateChangedFilter = new IntentFilter();
		mNetworkStateChangedFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		final AlertDialog dialog = this.createDialog();
		
		mNetworkStateIntentReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
					if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
						boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
						if(noConnectivity){
							dialog.show();
						}else{
							if(dialog.isShowing()){
								dialog.dismiss();
							}
							if(adapter != null && adapter.getCount()==0){
								((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
								new TrueffelAsyncTask(TrueffelActivity.this).execute();
							}else{
								update(TrueffelsHolder.getTruffels());
							}
						}
					}
		    }
		};
    }
    
    private void initListView(){
        adapter = new TrueffelAdapter(TrueffelActivity.this, R.layout.item_trufa,getTrufe());
        this.truf_list=(ListView)findViewById(R.id.list_trufe);
        truf_list.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> adapt_view, View view, int pos,long id){
	        		Intent editIntent = new Intent(TrueffelActivity.this, TrueffelSettingsActivity.class);
	        		editIntent.putExtra("id", getTrufe().get(pos).id);
	        		editIntent.putExtra("pos", pos);
	        		startActivityForResult(editIntent,editRequestCode);
        	}
        });
        truf_list.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
				new TrueffelItemDialog(TrueffelActivity.this, adapter.getItem(pos).id).show();
				return false;
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.menu_messages).setIntent(new Intent(TrueffelActivity.this, TSMessagesActivity.class));
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_messages:
                //NavUtils.navigateUpFromSameTask(this);
            	startActivity(item.getIntent());
            	break;
        }
        return true;
    }
    
    public void setProgress(boolean show){
    	//setProgressBarIndeterminateVisibility(show);
    	if(pb.isShown()){
    		pb.setVisibility(View.GONE);
    	}else{
    		adapter.clear();
    		adapter.notifyDataSetChanged();
    		truf_list.setAdapter(adapter);
    		pb.setVisibility(View.VISIBLE);
    	}
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    }
    
    @Override
    public void onResume() {
		super.onResume();
		this.registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);
		//unregisterReceiver(mNetworkStateIntentReceiver);
		//registerReceiver(mNetworkStateIntentReceiver, mNetworkStateChangedFilter);
	}
	
	@Override
    public void onPause() {
		super.onPause();
		unregisterReceiver(mNetworkStateIntentReceiver);
		SCREEN_ORIENTATION =getScreenOrientation();
    }
	
	@Override
	public void onDestroy(){
		//unregisterReceiver(mNetworkStateIntentReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
	    finish();
	    super.onBackPressed();
	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == editRequestCode && resultCode == RESULT_OK){
			setProgress(true);
			new TrueffelAsyncTask(this).execute();
		}
	}
	
	public ArrayList<Trueffel> getTrufe(){
		return TrueffelsHolder.getTruffels();
	}
	
	public void setTrufe(ArrayList<Trueffel> trufe){
		TrueffelsHolder.setTruffles(trufe);
	}
	
	public void update(ArrayList<Trueffel> trufe){
		setTrufe(trufe);
		adapter = new TrueffelAdapter(TrueffelActivity.this, R.layout.item_trufa,trufe);
		truf_list.setAdapter(adapter);
		findViewById(R.id.trufeLayout).setVisibility(View.VISIBLE);
		pb.setVisibility(View.GONE);
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	public void addTrufa(View view){
		Intent editIntent = new Intent(TrueffelActivity.this, TrueffelSettingsActivity.class);
		editIntent.putExtra("id", -1);
		startActivityForResult(editIntent,editRequestCode);
	}
    
    public AlertDialog createDialog(){
    	AlertDialog alertDialog = new AlertDialog.Builder(TrueffelActivity.this).create();
    	alertDialog.setTitle("Nu exista conexiune de internet!");
    	 
        // Setting Dialog Message
        alertDialog.setMessage("Porniti conexiunea de internet!");
 
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);
 
        // Setting OK Button
        alertDialog.setButton("Inchide aplicatia!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	TrueffelActivity.this.finish();
                }
        });
        return alertDialog;
    }
    
    private class TrueffelAdapter extends ArrayAdapter<Trueffel>{
    	private ArrayList<Trueffel> trufead;
    	
		public TrueffelAdapter(Context context, int textViewResourceId, ArrayList<Trueffel> trufe) {
			super(context, textViewResourceId, trufe);
			this.trufead=trufe;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;

	        if (v == null) {
	            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.item_trufa, null);
	        }
	             
	        Typeface tf = Typeface.createFromAsset(TrueffelActivity.this.getAssets(),"fonts/dandelion.ttf");
	        Trueffel trufa = this.trufead.get(position);
	        
	        ImageView img = (ImageView)v.findViewById(R.id.trufa_img);
	        if(trufa.image!=null){
	        	img.setImageDrawable(trufa.image);
	        }else{
	        	img.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
	        }
	        TextView pr1 = (TextView)v.findViewById(R.id.trufa_price1);
	        pr1.setTypeface(tf);
	        TextView pr2 = (TextView)v.findViewById(R.id.trufa_price2);
	        pr2.setTypeface(tf);
	        TextView pr3 = (TextView)v.findViewById(R.id.trufa_price3);
	        pr3.setTypeface(tf);
	        TextView vis = (TextView)v.findViewById(R.id.visibility);
	        vis.setText(trufa.visibility);
	        if(trufa.types!=null){
	        if(trufa.types.size()>0 && trufa.types.size()<=3)
	        	if(trufa.types.get(0).price>=0)
	        		pr1.setText("1. "+String.valueOf(trufa.types.get(0).price)+" "+getResources().getString(R.string.unit));
	        	else
	        		pr1.setText("");
	        	if(trufa.types.size()>1)
	        		pr2.setText("2. "+String.valueOf(trufa.types.get(1).price)+" "+getResources().getString(R.string.unit));
	        	else pr2.setText("");
	        	if(trufa.types.size()==3)
	        		pr3.setText("3. "+String.valueOf(trufa.types.get(2).price)+" "+getResources().getString(R.string.unit));
	        	else pr3.setText("");
	        	TextView upt = (TextView)v.findViewById(R.id.modif_date);
	        	upt.setText(trufa.types.get(0).date);
	        }else{
	        	pr1.setText("");
	        	pr2.setText("");
	        	pr3.setText("");
	        }
	        return v;
		}
    	
    }
    
}