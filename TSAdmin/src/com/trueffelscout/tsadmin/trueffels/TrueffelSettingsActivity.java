package com.trueffelscout.tsadmin.trueffels;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trueffelscout.tsadmin.R;
import com.trueffelscout.tsadmin.TSActivity;
import com.trueffelscout.tsadmin.model.Trueffel;
import com.trueffelscout.tsadmin.model.TrueffelType;
import com.trueffelscout.tsadmin.model.TrueffelsHolder;
import com.trueffelscout.tsadmin.services.TrueffelAsyncTask;

public class TrueffelSettingsActivity extends TSActivity {
	private static final int CAM_REQUEST = 1; 
	private static final int ADD_TRUFA=-1;
	private int id_trufa;
	//ArrayList<TrueffelType> trufa_types;
	Trueffel trufa;
	private ImageView img;
	private Bitmap newImgBmp;
	private JSONArray type_deleted;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trufa_settings);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getWindow().setBackgroundDrawable(bitmapDrawable);
		
        
        this.id_trufa = this.getIntent().getIntExtra("id",ADD_TRUFA);
        
		this.img = (ImageView) findViewById(R.id.edit_img_trufa);
		if(this.id_trufa!=-1){
			//new TrueffelAsyncTask(this).execute(new String[]{"http://www.trueffelscout.de/mobile/TSadmin.php?id="+String.valueOf(this.id_trufa)});
			int pos = this.getIntent().getIntExtra("pos",-1);
			if(pos!=-1){
				this.trufa = TrueffelsHolder.getTruffels().get(pos);
				update();
			}
		}else{
			ProgressBar pb = (ProgressBar)findViewById(R.id.edit_trufa_pb);
			pb.setVisibility(View.GONE);
			RelativeLayout tr_sett = (RelativeLayout)findViewById(R.id.trufa_settings);
			tr_sett.setVisibility(View.VISIBLE);
			ImageButton cam = (ImageButton) findViewById(R.id.camButton);
			cam.setOnClickListener(new OnClickListener(){
				public void onClick(View arg0) {
					Intent cam_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(cam_intent,CAM_REQUEST);
				}
			});
		}
		EditText name = (EditText) findViewById(R.id.edit_name_trufa);
		name.clearFocus();
		
		//this.trufa_types = new ArrayList<TrueffelType>();
		this.type_deleted = new JSONArray();
	}
	
	public void update(){
		
		
		ProgressBar pb = (ProgressBar)findViewById(R.id.edit_trufa_pb);
		pb.setVisibility(View.GONE);
		RelativeLayout tr_sett = (RelativeLayout)findViewById(R.id.trufa_settings);
		tr_sett.setVisibility(View.VISIBLE);
		
		//RadioGroup vis = (RadioGroup) findViewById(R.id.edi);
		 
		//this.trufa_types = trufa.types;
		
			if(trufa.name!=null){
				EditText name = (EditText) findViewById(R.id.edit_name_trufa);
				name.setText(trufa.name);
			}
			if(trufa.image!=null){
				img.setImageDrawable(trufa.image);
			}
			if(trufa.visibility!=null){
				//RadioGroup vis = (RadioGroup) findViewById(R.id.radioGroup1);
				if(trufa.visibility.equalsIgnoreCase("Public")){
					RadioButton publ = (RadioButton)findViewById(R.id.edit_public);
					publ.setChecked(true);
				}else if(trufa.visibility.equalsIgnoreCase("Private")){
					RadioButton priv = (RadioButton)findViewById(R.id.edit_private);
					priv.setChecked(true);
				}
			}
			if(trufa.types!=null){
				LinearLayout upt_price_layout = (LinearLayout)findViewById(R.id.update_prices_layout);
				if(trufa.types.size()>0){
					int idx = 0; 
					while(idx<trufa.types.size()){
						LinearLayout edit_price_layout = getNewLinearLayout(idx,trufa.types.get(idx).type, trufa.types.get(idx).price);
						upt_price_layout.addView(edit_price_layout);
						idx++;
					}
					LinearLayout add_price_layout = (LinearLayout)findViewById(R.id.add_prices_layout);
					add_price_layout.removeViewAt(0);
				}
			}
			ImageButton cam = (ImageButton) findViewById(R.id.camButton);
			if(this.id_trufa>2){
				cam.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						Intent cam_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cam_intent,CAM_REQUEST);
					}
				});
			}else{
				//img.setVisibility(View.GONE);
				cam.setVisibility(View.GONE);
			}
			if(trufa.description!=null){
				EditText descr = (EditText) findViewById(R.id.edit_descr);
				descr.setText(trufa.description);
			}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == CAM_REQUEST && resultCode == RESULT_OK){
			this.newImgBmp = (Bitmap) data.getExtras().get("data");
			this.img.setImageBitmap(newImgBmp);
			CheckBox img_upload = (CheckBox)findViewById(R.id.edit_img_confirm);
			img_upload.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		this.id_trufa = this.getIntent().getIntExtra("id",-1);
	}
	
	public void saveTrufa(View view){
		new SaveTrueffelAsyncTask().execute(new String[]{});
	}
	public void cancelTrufa(View view){
		this.finish();
	}
	
	public void deletePriceChar(View view){
		LinearLayout add_layout = (LinearLayout)findViewById(R.id.add_prices_layout);
		int nr_pr = add_layout.getChildCount();
		LinearLayout upt_layout = (LinearLayout)findViewById(R.id.update_prices_layout);
		int nr_upt = upt_layout.getChildCount();
		RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1); 
		int selected = g.getCheckedRadioButtonId();
		RadioButton b = (RadioButton)findViewById(selected);
		if(nr_pr>0){
			if(b.getText().equals("Public")&&nr_upt==0){
				if(nr_pr>1){
					add_layout.removeViewAt(nr_pr-1);
				}
			}else{
				add_layout.removeViewAt(nr_pr-1);
			}
		}else{
			if(b.getText().equals("Public")){
				if(nr_upt>1){
					upt_layout.removeViewAt(nr_upt-1);
					this.type_deleted.put(this.trufa.types.get(nr_upt-1).id);
				}
			}else{
				upt_layout.removeViewAt(nr_upt-1);
				this.type_deleted.put(this.trufa.types.get(nr_upt-1).id);
			}
		}
	}
	
	public void addNewPriceText(View view){
		LinearLayout add_layout = (LinearLayout)findViewById(R.id.add_prices_layout);
		LinearLayout new_add_layout = getNewLinearLayout(this.id_trufa,"",0);
		add_layout.addView(new_add_layout);
	}
	
	public LinearLayout getNewLinearLayout(int id, String car, int price){
		LinearLayout price_layout = new LinearLayout(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		price_layout.setLayoutParams(params);
		price_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		EditText char_et = new EditText(this);
		char_et.setWidth(300);
		char_et.setHeight(LayoutParams.WRAP_CONTENT);
		char_et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		char_et.setText(car);
		
		EditText pr_et = new EditText(this);
		pr_et.setWidth(100);
		pr_et.setHeight(LayoutParams.WRAP_CONTENT);
		pr_et.setInputType(InputType.TYPE_CLASS_NUMBER);
		pr_et.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		pr_et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		pr_et.setText(String.valueOf(price));
		
		switch(id){
			case -1:char_et.setId(R.id.add_char);
					pr_et.setId(R.id.add_price); 
					break;
			case 0: char_et.setId(R.id.edit_char_0);
					pr_et.setId(R.id.edit_price_0);
					break;
			case 1: char_et.setId(R.id.edit_char_1);
					pr_et.setId(R.id.edit_price_1);
					break;
			case 2: char_et.setId(R.id.edit_char_2);
					pr_et.setId(R.id.edit_price_2);
					break;
			case 3: char_et.setId(R.id.edit_char_3);
					pr_et.setId(R.id.edit_price_3);
					break;
			case 4: char_et.setId(R.id.edit_char_4);
					pr_et.setId(R.id.edit_price_4);
					break;
			case 5: char_et.setId(R.id.edit_char_5);
					pr_et.setId(R.id.edit_price_5);
					break;
			default: break;		
		}
		
		TextView txt_eur = new TextView(this);
		txt_eur.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		txt_eur.setText(getResources().getString(R.string.euro));
		
		price_layout.addView(char_et);
		price_layout.addView(pr_et);
		price_layout.addView(txt_eur);
		
		return price_layout;
	}
	
	private class SaveTrueffelAsyncTask extends AsyncTask<String, Void, String> {
		private JSONObject dataJson;
		private ProgressDialog pd;
		
		@Override 
		protected void onPreExecute(){
			pd = new ProgressDialog(TrueffelSettingsActivity.this);
			pd.show();
			pd.setMessage("Updating truffel");
			readActivityInJSON();
		}
			
		@Override
		protected String doInBackground(String... params) {
			
	        try{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://www.trueffelscout.de/mobile/edit_trueffel.php");
				httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json;charset=ISO-8859-1"); 
			    StringEntity entity = new StringEntity(dataJson.toString(), HTTP.UTF_8);
				httpPost.setEntity(entity);
		        HttpResponse response = httpClient.execute(httpPost);
		        HttpEntity entityResponse = response.getEntity();
		        // If the response does not enclose an entity, there is no need
		        if (entityResponse != null) {
					InputStream instream = entity.getContent();
					Header contentEncoding = response.getFirstHeader("Content-Encoding");
					BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
					StringBuilder sb = new StringBuilder();

					String line = null;
					try {
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							instream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return sb.toString();
		        }
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public void onPostExecute(String result){
			//pd.setMessage("Done!");
			if(result!=null) Toast.makeText(TrueffelSettingsActivity.this, result, Toast.LENGTH_LONG).show();
			pd.setMessage(result);
			pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			//pd.dismiss();
		}
		
		private void readActivityInJSON(){
			
			dataJson = new JSONObject();
			
			JSONObject trufaJson = new JSONObject();
			try {
				trufaJson.put("id_trueffel", id_trufa);
				EditText name_et = (EditText)findViewById(R.id.edit_name_trufa);
				trufaJson.put("name", name_et.getText());
				EditText descr_et = (EditText)findViewById(R.id.edit_descr);
				trufaJson.put("description", descr_et.getText());
				CheckBox loadImg = (CheckBox)findViewById(R.id.edit_img_confirm);
				if(loadImg.isChecked()){
					trufaJson.put("image", getStringFromBitmap(newImgBmp));
				}
				RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1); 
				int selected = g.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(selected);
				trufaJson.put("visibility", b.getText());
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("readActivityInJSON","Json error");
			}
			
			JSONArray updateTypesJson = new JSONArray();
			LinearLayout upt_price_layout = (LinearLayout)findViewById(R.id.update_prices_layout);
			for(int i=0;i<upt_price_layout.getChildCount();i++){
				JSONObject typeUptJson = new JSONObject();
				try {
					typeUptJson.put("id_category", trufa.types.get(i).id);
					typeUptJson.put("id_trueffel", id_trufa);
					LinearLayout ll = (LinearLayout)upt_price_layout.getChildAt(i);
					EditText et_cat = (EditText)ll.getChildAt(0);
					typeUptJson.put("category", et_cat.getText());
					EditText et_pr = (EditText)ll.getChildAt(1);
					typeUptJson.put("price", et_pr.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("readActivityInJSON","Json error");
				}
				updateTypesJson.put(typeUptJson);
			}
			
			JSONArray addTypesJson = new JSONArray();
			LinearLayout add_price_layout = (LinearLayout)findViewById(R.id.add_prices_layout);
			for(int i=0;i<add_price_layout.getChildCount();i++){
				JSONObject typeAddJson = new JSONObject();
				try {
					typeAddJson.put("id_trueffel", id_trufa);
					LinearLayout ll = (LinearLayout)add_price_layout.getChildAt(i);
					EditText et_cat = (EditText)ll.getChildAt(0);
					typeAddJson.put("category", et_cat.getText());
					EditText et_pr = (EditText)ll.getChildAt(1);
					typeAddJson.put("price", et_pr.getText());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("readActivityInJSON","Json error");
				}
				addTypesJson.put(typeAddJson);
			}
			
			JSONObject typesJson = new JSONObject();
			try {
				typesJson.put("updateCategories", updateTypesJson);
				typesJson.put("addCategories", addTypesJson);
				if(type_deleted.length()>0){
					typesJson.put("deleteCategories", type_deleted);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				dataJson.put("trueffel", trufaJson);
				dataJson.put("trueffelTypes", typesJson);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private String getStringFromBitmap(Bitmap bitmapPicture) {
			 final int COMPRESSION_QUALITY = 100;
			 String encodedImage;
			 ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
			 bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
			 byteArrayBitmapStream);
			 byte[] b = byteArrayBitmapStream.toByteArray();
			 encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
			 return encodedImage;
		 }

	}
	

}
