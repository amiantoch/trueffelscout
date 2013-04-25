package views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.trueffelscout.trueffelscoutapp.MessageActivity;
import com.trueffelscout.trueffelscoutapp.R;
import com.trueffelscout.trueffelscoutapp.TsApplication;

public class KontaktDialog extends Dialog implements OnClickListener {
	  private ImageView call, msg, sms;
	  private Activity mActivity;
	  private String phone;
	
	  public KontaktDialog(Activity activity) {      
	    super(activity);
	    mActivity = activity;
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_contact);
		call = (ImageView)findViewById(R.id.call);
		call.setOnClickListener(this);
		msg = (ImageView)findViewById(R.id.send_message);
		msg.setOnClickListener(this);
		sms = (ImageView)findViewById(R.id.send_sms);
		sms.setOnClickListener(this);
		phone = ((TsApplication)activity.getApplication()).getPhone();
	  }
	
	  public void onClick(View v) {       
	    if (v == call)
	        call_biri();
	    else if(v==msg) {
	        message_biri();
	    }else if(v==sms){
	    	sms_biri();
	    }
	  }
	  
	  public void call_biri(){
		  if(phone==null){
			  Toast.makeText(this.mActivity, mActivity.getResources().getString(R.string.phone_not_availible), Toast.LENGTH_SHORT).show();
		  }else{
				dismiss();
				Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
			    this.mActivity.startActivity(i);
		  }
	  }
	  public void message_biri(){
		dismiss();
		Intent i = new Intent(mActivity,MessageActivity.class);
		mActivity.startActivity(i);
		/*
		MessageDialog msg_dialog = new MessageDialog(mActivity);
		msg_dialog.show();*/
	  }
	  public void sms_biri(){
		  if(phone!=null){
			  Uri uri = Uri.parse("smsto:"+phone);   
			  Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
			  it.putExtra("sms_body", this.mActivity.getResources().getString(R.string.sms_body));   
			  mActivity.startActivity(it); 
		  }else{
			  Toast.makeText(this.mActivity, mActivity.getResources().getString(R.string.phone_not_availible), Toast.LENGTH_SHORT).show();
		  }
	  }

}
