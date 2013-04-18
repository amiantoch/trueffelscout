package services;

import java.io.IOException;
import java.io.InputStream;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.trueffelscout.trueffelscout.TrueffelscoutActivity;

import com.trueffelscout.trueffelscout.R;
import adapters.Trueffel;
import adapters.TrueffelType;
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
	    		InputStream is_xml = httpconn.getInputStream();
	    		
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
	    		}
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
		} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return null;
		} catch (SAXException e) {
				e.printStackTrace();
				return null;
		}
		
		return trufe;
	}
	
	@Override
	protected void onPostExecute(final List<Trueffel> result){
		//Button topo_btn = (Button) activity.findViewById(R.id.tras_topo_btn);
		if(result==null){
			//Toast.makeText(activity, "No prices availible!", Toast.LENGTH_SHORT).show();
		}else{
			this.adapter.notifyDataSetChanged();
			this.activity.setListAdapter(adapter);
			TextView upd_txt = (TextView) activity.findViewById(R.id.main_update);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Date date = new Date();
			upd_txt.setText("Stand: "+dateFormat.format(date));
		}
	}
	
	public Drawable getImage(String url, String src_name) throws java.net.MalformedURLException, java.io.IOException {
		Drawable abc = Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), src_name);
        return abc;
    }  

}
