package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.trueffelscout.trueffelscoutapp.TsApplication;

import android.app.Application;
import android.os.AsyncTask;

public class PhoneRequest extends AsyncTask<Void, Void, String> {
	private TsApplication app;
	
	public PhoneRequest(TsApplication app){
		this.app = app;
	}

	@Override
	protected String doInBackground(Void... params) {
		
		String phone="";
		String phoneUrl="http://www.trueffelscout.de/mobile/phone.php";
		URL url;
		try {
			url = new URL(phoneUrl);
		
    	
	    	URLConnection connection = url.openConnection();
	    	HttpURLConnection httpconn = (HttpURLConnection) connection;
	    	
	    	int responseCode = httpconn.getResponseCode();
	    	
	    	if(responseCode==HttpURLConnection.HTTP_OK){
	    		InputStream is = httpconn.getInputStream();
	    		BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    	    String inLine;
	    	    while ((inLine = in.readLine()) != null){
	    	    	phone += inLine;
	    	    }
	    	    in.close();
	    	    JSONObject phoneJson = new JSONObject(phone);
	    	    phone = phoneJson.getString("phone");
	    	    
	    	    return phone;
	    	}
    	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	@Override 
	public void onPostExecute(String phone){
		app.setPhone(phone);
	}
}
