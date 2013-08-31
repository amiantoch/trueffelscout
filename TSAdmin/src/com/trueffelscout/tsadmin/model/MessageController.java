package com.trueffelscout.tsadmin.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MessageController {
	private static final String SHARED_PREF = "preferances_message";
	private static final String DELETED_MSG = "deleted";
	private static final String SEEN_MSG = "seen";
	
	private static MessageController _instance;
	private Context _context;
	private SharedPreferences _sp;
	
	private MessageController(Context context){
		_context = context;
		_sp = _context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
	}
	
	public static MessageController getInstance(Context context){
		if(_instance == null){
			_instance = new MessageController(context);
		}
		return _instance;
	}
	
	public boolean addDeletedMsg(Message msg){
		try {
			String delMsgStr = _sp.getString(DELETED_MSG, "");
			if(delMsgStr.length()>0){
				JSONArray delMsgs = new JSONArray(delMsgStr);
				delMsgs.put(msg.getId());
				_sp.edit().putString(DELETED_MSG, delMsgs.toString()).commit();
			}else{
				JSONArray delMsgs = new JSONArray();
				delMsgs.put(msg.getId());
				_sp.edit().putString(DELETED_MSG, delMsgs.toString()).commit();
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addSeenMessage(Message msg){
		try {
			String seenMsgStr = _sp.getString(SEEN_MSG, "");
			if(seenMsgStr.length()>0){
				JSONArray seenMsgs = new JSONArray(seenMsgStr);
				seenMsgs.put(msg.getId());
				_sp.edit().putString(SEEN_MSG, seenMsgs.toString()).commit();
			}else{
				JSONArray seenMsgs = new JSONArray();
				seenMsgs.put(msg.getId());
				_sp.edit().putString(SEEN_MSG, seenMsgs.toString()).commit();
			}
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isMessageDeleted(Message msg){
		String delMsgStr = _sp.getString(DELETED_MSG, "");
		if(delMsgStr.length()>0){
			try {
				JSONArray delArray = new JSONArray(delMsgStr);
				for(int i=0;i<delArray.length();i++){
					if(delArray.getInt(i) == msg.getId()){
						return true;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	public boolean isMessageSeen(Message msg){
		boolean seen = false;
		String seenMsgStr = _sp.getString(SEEN_MSG, "");
		if(seenMsgStr.length()>0){
			try {
				JSONArray seenArray = new JSONArray(seenMsgStr);
				for(int i=0;i<seenArray.length();i++){
					if(seenArray.getInt(i) == msg.getId()){
						seen = true;
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return seen;
	}
	
	public ArrayList<Message> filterDeletedMessages(ArrayList<Message> messages){
		ArrayList<Message> filtered = new ArrayList<Message>();
		for(Message msg:messages){
			if(!isMessageDeleted(msg)){
				filtered.add(msg);
			}
		}
		return filtered;
	}

}
