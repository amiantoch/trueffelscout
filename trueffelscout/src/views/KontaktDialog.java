package views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.trueffelscout.trueffelscout.MessageActivity;
import com.trueffelscout.trueffelscout.R;

public class KontaktDialog extends Dialog implements OnClickListener {
	  ImageView call, msg, sms;
	  Activity mActivity;
	
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
		dismiss();
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+491791879581"));
	    this.mActivity.startActivity(i);
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
		  Uri uri = Uri.parse("smsto:+491791879581");   
		  Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
		  it.putExtra("sms_body", "Hallo, ich hätte Interesse an Ihren Produkten.");   
		  mActivity.startActivity(it); 
	  }

}
