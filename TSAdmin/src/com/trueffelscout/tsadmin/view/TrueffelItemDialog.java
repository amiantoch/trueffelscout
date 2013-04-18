package com.trueffelscout.tsadmin.view;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.trueffelscout.tsadmin.R;
import com.trueffelscout.tsadmin.services.EditTrueffelAsyncTask;
import com.trueffelscout.tsadmin.trueffels.TrueffelActivity;
import com.trueffelscout.tsadmin.trueffels.TrueffelSettingsActivity;


public class TrueffelItemDialog extends Dialog implements OnClickListener{
	private TrueffelActivity context;
	private int idTrufa;
	private Button delTrufa;
	private Button editTrufa;
	
	public TrueffelItemDialog(TrueffelActivity context, int id) {
		super(context);
		this.context = context;
		this.idTrufa = id;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.trueffe_item_dialog);
		delTrufa = (Button)findViewById(R.id.button_delete_trueffel);
		delTrufa.setOnClickListener(this);
		editTrufa = (Button)findViewById(R.id.button_edit_trueffel);
		editTrufa.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v==this.delTrufa){
			JSONObject json = new JSONObject();
			try {
				json.put("delete_trueffel", this.idTrufa);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new EditTrueffelAsyncTask(this.context).execute(new String[]{json.toString()});
			this.dismiss();
			
		}else if(v==this.editTrufa){
			Intent editIntent = new Intent(this.context, TrueffelSettingsActivity.class);
			editIntent.putExtra("id", this.idTrufa);
			this.context.startActivity(editIntent);
			this.dismiss();
		}
	}

}
