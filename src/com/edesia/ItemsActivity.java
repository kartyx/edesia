package com.edesia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edesia.R.drawable;

import android.support.v7.app.ActionBarActivity;

import android.content.ClipData.Item;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemsActivity extends ActionBarActivity implements OnItemClickListener{
ListView itemslist;
List<ListItem> Items;
public String jsonString;
public JSONArray jArray; 
static List<CartItem> CartItems;
TextView Results;
Editor editor;
static Menu menu;
static int total=0,i=0;
String[] names,costs,imageurls,hosts,paths,breakfasts,firstbreaks,lunches,ids;
ConnectionDetector cd;
Boolean isInternetPresent,isServerOK;
static List<String> cartNames,cartCosts,cartIds,cartQuantity,cartTiming;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.items_activity);
		Results=(TextView)findViewById(R.id.noOfResults);
		itemslist=(ListView)findViewById(R.id.Itemslist);
		cd = new ConnectionDetector(getApplicationContext());
		  
		isInternetPresent = cd.isConnectingToInternet(); 
		if(isInternetPresent)
			new LoadItems().execute();
		else
			Toast.makeText(getBaseContext(), "Cannot Connect to thhe Internet", Toast.LENGTH_LONG).show();
		CartItems=new ArrayList<CartItem>();
		itemslist.setOnItemClickListener(this);
		cartNames=new ArrayList<String>();
		cartCosts=new ArrayList<String>();
		cartQuantity=new ArrayList<String>();
		cartIds=new ArrayList<String>();
		cartTiming=new ArrayList<String>();

	}
	 @Override
	 public void onItemClick(AdapterView<?> parent, View view, int position,
	   long id) {
			Intent addIntent=new Intent("com.edesia.ADDCARTACTIVITY");
			addIntent.putExtra("ID", ids[position]);
			addIntent.putExtra("Name", names[position]);
			addIntent.putExtra("BreakFast",breakfasts[position]);
			addIntent.putExtra("FirstBreak",firstbreaks[position]);
			addIntent.putExtra("Lunch", lunches[position]);
			addIntent.putExtra("Cost", costs[position]);
			startActivity(addIntent);

	 
	 }
	 
@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
		if(CartItems.size()>0)
		{
			menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.cartavail));
		}
		else
		{
			menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.cart));
		}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
	}

class LoadItems extends AsyncTask<String, String, String> {
		


		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }

	    @Override
	    protected String doInBackground(String... f_url) {
	    	isServerOK=cd.isConnectingToServer();
	    	if(isServerOK)
	    	{
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/LoadItems/LoadItems");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("",""));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response=httpclient.execute(httppost);
		        jsonString=Read.readJson(response);
		    } catch (ClientProtocolException e) {

		    } catch (IOException e) {

		    }
			  catch(RuntimeException e)
			  {
				  Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
			  }
	       
	    	}
	        return null;
	    }
	    

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(isServerOK)
			{
			if(jsonString!=null)
			{
			try {
		          parseData();
		    	  Items = new ArrayList<ListItem>();
				for (int i = 0; i <jArray.length() ; i++) {
					   ListItem item = new ListItem(names[i],imageurls[i],"Rs."+costs[i]);
					   Items.add(item);
					 }
						ItemsAdapter adapter = new ItemsAdapter(getBaseContext(),Items);	
				
				      itemslist.setAdapter(adapter);
					  adapter.notifyDataSetChanged();
					  for(ListItem item: Items)
					  {
						  item.loadImage(adapter);
					  }	
				
					 
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		}
			else{
				Toast.makeText(getBaseContext(), "Cannot Connect To Server", Toast.LENGTH_SHORT).show();
			}
		}
	}

public void parseData() throws JSONException {
	try{
JSONObject jObj=new JSONObject(jsonString);
	jArray=jObj.getJSONArray("Items");
	names=new String[jArray.length()];
	costs=new String[jArray.length()];
	hosts=new String[jArray.length()];
	paths=new String[jArray.length()];
	breakfasts=new String[jArray.length()];
	lunches=new String[jArray.length()];
	firstbreaks=new String[jArray.length()];
	imageurls=new String[jArray.length()];
	ids=new String[jArray.length()];
	Results.setText("Items("+jArray.length()+" Results)");
for(int i=0;i<jArray.length();i++)
{ 
	JSONObject c = jArray.getJSONObject(i);
	names[i]=c.getString("Name").toString();
	costs[i]=c.getString("Cost").toString();
	hosts[i]=c.getString("host").toString();
	paths[i]=c.getString("image").toString();
	breakfasts[i]=c.getString("BreakFast").toString();
	lunches[i]=c.getString("Lunch").toString();
	firstbreaks[i]=c.getString("FirstBreak").toString();
	ids[i]=c.getString("ID").toString();
	imageurls[i]=hosts[i]+paths[i];
}
	}
	catch(JSONException e)
	{
    	//Toast.makeText(getBaseContext(), "No Result Found", Toast.LENGTH_SHORT).show();
	}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater i=getMenuInflater();
	this.menu=menu;
	i.inflate(R.menu.menu, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch(item.getItemId())
	{
	case R.id.cartmenu: 
		Intent cartIntent=new Intent("com.edesia.CARTACTIVITY");
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(cartIntent);
		break;
	case R.id.Changemenu:
		Intent helpIntent=new Intent("com.edesia.CHANGEACTIVITY");
	      helpIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	      startActivity(helpIntent);
	      break;
	case R.id.logoutmenu:
		Intent loginIntent=new Intent("com.edesia.LOGINACTIVITY");
		editor=WelcomeActivity.firsttime.edit();
		editor.putBoolean("loggedin",false);
		editor.commit();
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		this.finish();
		startActivity(loginIntent);
		break;
	case R.id.dashboard:
		Intent dashIntent=new Intent("com.edesia.DASHBOARDACTIVITY");
	    dashIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(dashIntent);
		break;
	default:
	return super.onOptionsItemSelected(item);
	}
	return true;
}

}
