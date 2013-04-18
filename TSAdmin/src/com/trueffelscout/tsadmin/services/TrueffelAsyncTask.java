package com.trueffelscout.tsadmin.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

import com.trueffelscout.tsadmin.TSActivity;
import com.trueffelscout.tsadmin.model.Trueffel;
import com.trueffelscout.tsadmin.model.TrueffelType;
import com.trueffelscout.tsadmin.trueffels.TrueffelActivity;

public class TrueffelAsyncTask extends AsyncTask<String,Void,ArrayList<Trueffel>>{

	private TSActivity activity;
	private ProgressDialog pd;
	
	public TrueffelAsyncTask(TSActivity activity){
		this.activity = activity;
	}
	
	@Override 
	protected void onPreExecute(){
		//ProgressBar pb = (ProgressBar)activity.findViewById(R.id.progressBar);
		//pb.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected ArrayList<Trueffel> doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<Trueffel> trufe_loc;
		trufe_loc = new ArrayList<Trueffel>();
		StringBuilder builder = new StringBuilder();
		HttpClient httpcl = new DefaultHttpClient();
    	HttpGet httpget = new HttpGet();
    	try{
    		//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    		//nameValuePairs.add(new BasicNameValuePair("user", "1"));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		httpget.setURI(new URI(params[0]));
    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
    		String response = httpcl.execute(httpget,responseHandler);

            JSONObject json = new JSONObject(response);
            JSONArray jArray = json.getJSONArray("trueffels");
            for(int i=0;i<jArray.length();i++){
            	JSONObject jsonobj  = jArray.getJSONObject(i); 
            	Drawable img = this.getImage("http://www.trueffelscout.de/"+jsonobj.getString("image"), "picture");
            	Trueffel trufa = new Trueffel(jsonobj.getInt("id_trueffel"),jsonobj.getString("name"),jsonobj.getString("description"),img,jsonobj.getString("visibility"));
            	ArrayList<TrueffelType> ttypes = new ArrayList<TrueffelType>();
            	JSONArray jsoncat = jsonobj.getJSONArray("categories");
            	for(int j=0;j<jsoncat.length();j++){
            		JSONObject jsontype=jsoncat.getJSONObject(j);
            		TrueffelType tt = new TrueffelType();
            		tt.id = jsontype.getInt("id_category");
            		tt.type = jsontype.getString("category");
            		tt.price = jsontype.getInt("price");
            		tt.date = jsontype.getString("date");
            		ttypes.add(tt);
            	}
            	if(jsoncat.length()>0) trufa.types = ttypes;
            	trufe_loc.add(trufa);
            }
            
    	}catch(ClientProtocolException ex){
    		ex.printStackTrace();
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trufe_loc;
	}
	
	@Override
	protected void onPostExecute(final ArrayList<Trueffel> result){
		//pd.dismiss();
		if(result==null){
			Toast.makeText(activity, "No truffles availible!", Toast.LENGTH_SHORT).show();
		}else{
			activity.update(result);
		}
	}
	
	public Drawable getImage(String url, String src_name) throws java.net.MalformedURLException, java.io.IOException {
		Drawable abc = Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), src_name);
        return abc;
    }  

}
