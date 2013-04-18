package com.trueffelscout.tsadmin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trueffelscout.tsadmin.messages.TSMessagesActivity;
import com.trueffelscout.tsadmin.model.Message;

public class MessagesAsynkTask extends AsyncTask<String, Void, ArrayList<Message>> {
	TSMessagesActivity activity;
	
	public MessagesAsynkTask(TSMessagesActivity activity){
		this.activity = activity;
	}
	
	@Override
	protected ArrayList<Message> doInBackground(String... params) {
		ArrayList<Message> messages = new ArrayList<Message>();
		
		HttpURLConnection httpcon;
		try {
			URL url = new URL("http://www.trueffelscout.de/mobile/gcm/messages.php");
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoOutput(true);
			httpcon.setRequestProperty("Content-Type", "application/json");
			httpcon.setRequestProperty("Accept", "application/json");
			httpcon.setRequestMethod("POST");
			httpcon.connect();
	
			InputStream response = httpcon.getInputStream();
	        BufferedReader br = new BufferedReader(new     
			InputStreamReader(response));
			String str="", temp;
			while (null != ((temp = br.readLine())))
			{
			    System.out.println (str);
			    str=str + temp;             
			}       
			Gson gson = new Gson();
			JSONObject msgJson = new JSONObject(str);
			messages = gson.fromJson(msgJson.getJSONArray("messages").toString(), new TypeToken<ArrayList<Message>>(){}.getType());
			return messages;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(activity, "Could not read Json object", Toast.LENGTH_LONG).show();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Message> result){
		if(result!=null){
			activity.update(result);
		}else{
			Toast.makeText(activity, "Error reading messages", Toast.LENGTH_SHORT).show();
		}
	}

}
