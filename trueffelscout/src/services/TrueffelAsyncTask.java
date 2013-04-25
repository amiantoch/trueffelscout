package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import com.trueffelscout.trueffelscoutapp.R;
import com.trueffelscout.trueffelscoutapp.TrueffelscoutActivity;

import adapters.Trueffel;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrueffelAsyncTask extends AsyncTask<String,Void,List<Trueffel>>{

	private TrueffelscoutActivity activity;
	List<Trueffel> trufe;
	ArrayAdapter<Trueffel> adapter;
	
	public TrueffelAsyncTask(TrueffelscoutActivity activity, ArrayAdapter<Trueffel> adapter, List<Trueffel> trufe){
		this.activity = activity;
		this.adapter = adapter;
		this.trufe = trufe;
	}
	
	@Override 
	protected void onPreExecute(){
		this.adapter.notifyDataSetChanged();
		this.activity.setListAdapter(adapter);
	}
	
	@Override
	protected List<Trueffel> doInBackground(String... params) {
		// TODO Auto-generated method stub

		URL url;		
		try{
	    	String feed = "http://www.trueffelscout.de/mobile/trueffels.php";
	    	url = new URL(feed);
	    	
	    	URLConnection connection = url.openConnection();
	    	HttpURLConnection httpconn = (HttpURLConnection) connection;
	    	
	    	int responseCode = httpconn.getResponseCode();
	    	
	    	if(responseCode==HttpURLConnection.HTTP_OK){
	    		InputStream is = httpconn.getInputStream();
	    		BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    		String page= "";
	    	    String inLine;
	    	    while ((inLine = in.readLine()) != null){
	    	     page += inLine;
	    	    }
	    	    in.close();
	    		

                JSONObject json = new JSONObject(page);
                JSONArray jArray = json.getJSONArray("trueffels");
                Gson gson = new Gson();
                List<Trueffel> trufe_loc = gson.fromJson(jArray.toString(), new TypeToken<List<Trueffel>>(){}.getType());
	    		System.out.println(trufe_loc.toString());
                return trufe_loc;
                /*
	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder db = dbf.newDocumentBuilder();
	    		
	    		Document dom = db.parse(is_xml);
	    		Element doc_elem = dom.getDocumentElement();
	    		NodeList nl = doc_elem.getElementsByTagName("trueffel");
	    		if(nl.getLength()!=0){
		    		for(int i=0;i<nl.getLength();i++){
		    			Element cat_node = (Element) nl.item(i);
		    			Element category = (Element) cat_node.getElementsByTagName("categories").item(0);
		    			NodeList categories = (NodeList) category.getChildNodes();
		    			Trueffel trufa;
		    			if(this.trufe.size()>i){
		    				trufa = this.trufe.get(i);
		    			}else{
		    				trufa = new Trueffel();
		    				Element name_node = (Element) cat_node.getElementsByTagName("name").item(0);
		    				trufa.name = name_node.getTextContent();
		    				Element nameen_node = (Element) cat_node.getElementsByTagName("name_en").item(0);
		    				trufa.name_en = nameen_node.getTextContent();
		    				Element img_node = (Element) cat_node.getElementsByTagName("image").item(0);
		    				trufa.image = getImage(img_node.getTextContent(),"") ; 
		    				
		    			}
		    			ArrayList<TrueffelType> types = new ArrayList<TrueffelType>();
		    			String cat_str="";
	    				int pr_int=0;
	    				String caten_str="";
		    			for(int j=0;j<categories.getLength();j++){
		    				Element elem = (Element) categories.item(j);
		    				TrueffelType type = new TrueffelType();
		    				if(elem.getTagName().equalsIgnoreCase("category")){
		    					cat_str = elem.getTextContent();
		    					type.type=cat_str;
		    				}
		    				if(elem.getTagName().equalsIgnoreCase("price")){
		    					pr_int = Integer.parseInt(elem.getTextContent());
		    					type.price = pr_int;
		    				}
		    				if(elem.getTagName().equalsIgnoreCase("category_en")){
		    					caten_str = elem.getTextContent();
		    					type.type_en=caten_str;
		    				}
		    				if((j%3==1)||(j==categories.getLength()-1)){
		    					types.add(type);
		    				}
		    			}
		    			trufa.types = types;
		    			
		    			if(this.trufe.size()>i){
		    				trufe.set(i, trufa);
		    			}else{
		    				trufe.add(trufa);
		    		}
		    		}  
	    		}  */
	    		
	    	}
		}catch (SocketException e){
			e.printStackTrace();
			return null;
		}catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(JsonSyntaxException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(final List<Trueffel> result){
		//Button topo_btn = (Button) activity.findViewById(R.id.tras_topo_btn);
		if(result==null){
			//Toast.makeText(activity, "No prices availible!", Toast.LENGTH_SHORT).show();
		}else{
			//this.adapter.notifyDataSetChanged();
			this.activity.updateTrueffel(result);//.setListAdapter(adapter);
			TextView upd_txt = (TextView) activity.findViewById(R.id.main_update);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Date date = new Date();
			upd_txt.setText(activity.getResources().getString(R.string.updated)+dateFormat.format(date));
		}
	}
	
	public Drawable getImage(String url, String src_name) throws java.net.MalformedURLException, java.io.IOException {
		Drawable abc = Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), src_name);
        return abc;
    }  

}
