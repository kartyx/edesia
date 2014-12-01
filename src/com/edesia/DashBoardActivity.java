package com.edesia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends ActionBarActivity{
TextView UserName,Balance;
ListView DashList;
JSONArray dashArray;
String returnString;
List<DashItem> Items;
String[] dashnames,dashcosts,dashtransids,dashitemids,dashqty,dashdates;
private String balance;
ConnectionDetector cd;
Boolean isInternetPresent,isServerOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_activity);
		UserName =(TextView)findViewById(R.id.DashUserId);
		Balance=(TextView)findViewById(R.id.DashBalance);
		DashList=(ListView)findViewById(R.id.dashlist);
		dashArray=new JSONArray();
		Log.d("username",WelcomeActivity.userName);
		UserName.setText(WelcomeActivity.userName);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(null);
		getSupportActionBar().setTitle("DASH BOARD");
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet(); 
		if(isInternetPresent)
			new PreviousTransactions().execute(WelcomeActivity.userName);
		else
			Toast.makeText(getBaseContext(), "Cannot Connect to the Internet", Toast.LENGTH_LONG).show();
	}
public class PreviousTransactions extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... arg0) {
		isServerOK=cd.isConnectingToServer();
		if(isServerOK)
		{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/DashBoard/DashBoard");
	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("dash",arg0[0]));
	    
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        returnString=Read.readJson(response);
	        Log.d("return",returnString);
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		if(isServerOK)
		{
		if(returnString!=null)
		{
		try {
	          parseData();
	    	  Items = new ArrayList<DashItem>();
			for (int i = 0; i <dashArray.length() ; i++) {
				   DashItem item = new DashItem(dashnames[i],dashqty[i],dashdates[i],"Rs."+dashcosts[i],dashtransids[i]);
				   Items.add(item);
				 }
					DashBoardAdapter adapter = new DashBoardAdapter(getBaseContext(),Items);	
			
			      DashList.setAdapter(adapter);
				  adapter.notifyDataSetChanged();
			
				 
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	}
		else{
			Toast.makeText(getBaseContext(), "Cannot Connect to Server", Toast.LENGTH_SHORT).show();
		}
	}
}

public void parseData() throws JSONException {
	try{
JSONObject jObj=new JSONObject(returnString);
	dashArray=jObj.getJSONArray("dash");
	dashnames=new String[dashArray.length()];
	dashcosts=new String[dashArray.length()];
	dashdates=new String[dashArray.length()];
	dashtransids=new String[dashArray.length()];
	dashqty=new String[dashArray.length()];
	dashitemids=new String[dashArray.length()];
for(int i=0;i<dashArray.length();i++)
{ 
	JSONObject c = dashArray.getJSONObject(i);
	dashnames[i]=c.getString("ItemName").toString();
	dashcosts[i]=c.getString("cost").toString();
	dashdates[i]=c.getString("Date").toString();
	dashtransids[i]=c.getString("TransId").toString();
	dashqty[i]=c.getString("Qty").toString();
	dashitemids[i]=c.getString("Itemid").toString();
	balance=c.getString("balance").toString();
}
Balance.setText("Rs."+balance);
}
	catch(JSONException e)
	{
e.printStackTrace();
	}
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId())
	{
	case android.R.id.home: 
		this.finish();
		break;

	default:
	return super.onOptionsItemSelected(item);
	}
	return true;

}
}
