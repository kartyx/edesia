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



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
EditText userName,Password;
String pass;
Button login;
Editor editor;
String returnString;
ConnectionDetector cd;
Boolean isInternetPresent,isServerOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		userName=(EditText)findViewById(R.id.userName);
		Password=(EditText)findViewById(R.id.Password);
		login=(Button)findViewById(R.id.login);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

	}

public void login(View v)
{
	WelcomeActivity.userName=userName.getText().toString();
	pass=Password.getText().toString();
	if(WelcomeActivity.userName!=null&&pass!=null)
	{
	Toast.makeText(getBaseContext(), "Logging in", Toast.LENGTH_SHORT).show();
	if(isInternetPresent)
		new Authenticate().execute();
	else
		Toast.makeText(getBaseContext(), "Cannot Connect to the Internet", Toast.LENGTH_LONG).show();
	}
	else
	{
		Toast.makeText(getBaseContext(), "Please Enter Data", Toast.LENGTH_LONG).show();
	}
	

}
public class Authenticate extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		isServerOK=cd.isConnectingToServer();
		if(isServerOK)
		{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/EdesiaLogin/Authenticate");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("user",WelcomeActivity.userName));
	        nameValuePairs.add(new BasicNameValuePair("pass",pass));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        returnString=Read.readJson(response);
       
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
		// TODO Auto-generated method stub
		if(isServerOK)
		{
		if(returnString.trim().equalsIgnoreCase("Yes"))
		{
			Editor editor=WelcomeActivity.firsttime.edit();
			editor.putBoolean("loggedin", true);
			WelcomeActivity.loggedin=true;
			editor.putString("user",WelcomeActivity.userName);
			editor.putString("pass",pass);
			editor.commit();
			if(!WelcomeActivity.first)
			{
				WelcomeActivity.first=false;
				editor.putBoolean("FIRST",WelcomeActivity.first);
				Intent itemIntent=new Intent("com.edesia.ITEMSACTIVITY");
				startActivity(itemIntent);
			}
			else
			{
				WelcomeActivity.first=false;
				editor.putBoolean("FIRST", WelcomeActivity.first);
				Intent changeIntent=new Intent("com.edesia.CHANGEACTIVITY");
				startActivity(changeIntent);
			}
		}
		else if(returnString.trim().equalsIgnoreCase("Wrong"))
		{
	 		Toast.makeText(getBaseContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
		}
		else if(returnString.trim().equalsIgnoreCase("No"))
		{
			Toast.makeText(getBaseContext(), "Id Does Not Exists", Toast.LENGTH_SHORT).show();
		}
	}
		else
		{
			Toast.makeText(getBaseContext(), "Cannot Connect To Server", Toast.LENGTH_SHORT).show();
		}
	}
	
}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	this.finish();
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	this.finish();
}

}
