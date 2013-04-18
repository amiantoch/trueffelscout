package com.trueffelscout.tsadmin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.trueffelscout.tsadmin.trueffels.TrueffelActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class EditTrueffelAsyncTask extends AsyncTask<String, Void, String> {
	
	private TrueffelActivity activity;
	private ProgressDialog pd;
	
	public EditTrueffelAsyncTask(TrueffelActivity activity){
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... params) {
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.trueffelscout.de/mobile/edit_trueffel.php");
			httpPost.setHeader("Accept", "application/json");
		    httpPost.setHeader("Content-type", "application/json;charset=ISO-8859-1"); 
		    StringEntity entity = new StringEntity(params[0], HTTP.UTF_8);
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
	protected void onPostExecute(String result){
		new TrueffelAsyncTask(this.activity).execute(new String[]{"http://www.trueffelscout.de/mobile/TSadmin.php"});
		Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
	}

}
