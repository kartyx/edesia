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

import com.edesia.CartActivity.Transaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeActivity extends Activity{
EditText changePasstxt,ConfirmPasstxt;
Button change;
Editor editor;
String changePass,ConfirmPass;
ConnectionDetector cd;
public String returnString;
Boolean isInternetPresent,isServerOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_activity);
		changePasstxt=(EditText)findViewById(R.id.changePass);
		ConfirmPasstxt=(EditText)findViewById(R.id.confirmPass);
		change=(Button)findViewById(R.id.change);
		change.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changePass=changePasstxt.getText().toString();
				ConfirmPass=ConfirmPasstxt.getText().toString();
				if(changePass==null || ConfirmPass==null)
				{
					Toast.makeText(getBaseContext(), "Please Enter Passwords", Toast.LENGTH_LONG).show();
				}
				else if(changePass.equals(ConfirmPass))
				{
				cd = new ConnectionDetector(getApplicationContext());
				isInternetPresent = cd.isConnectingToInternet(); 
				if(isInternetPresent)
					new ChangePassword().execute();
				else
					Toast.makeText(getBaseContext(), "Cannot connect to the Internet", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Passwords Don't Match", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

public class ChangePassword extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		  isServerOK=cd.isConnectingToServer();
		  if(isServerOK)
		  {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/ChangePassword/Change");
	    try {
	        // Add your data
	    	Log.d("username", WelcomeActivity.userName);
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("user",WelcomeActivity.userName));
	        nameValuePairs.add(new BasicNameValuePair("pass",changePass));
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
		if(returnString.trim().equals("OK"))
		{
			Intent itemIntent=new Intent("com.edesia.ITEMSACTIVITY");
			startActivity(itemIntent);
			editor=WelcomeActivity.firsttime.edit();
			editor.putBoolean("FIRST",false);
			editor.commit();
			Toast.makeText(getBaseContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
		}
		else if(returnString.trim().equals("NO"))
		{
			Toast.makeText(getBaseContext(), "Something Wrong! Try Again", Toast.LENGTH_SHORT).show();
		}
		}
		else
		{
			Toast.makeText(getBaseContext(), "Cannot Connect to Server", Toast.LENGTH_SHORT).show();
		}
	}
	
}
public void skip(View v)
{
	Intent itemIntent=new Intent("com.edesia.ITEMSACTIVITY");
	startActivity(itemIntent);
	editor=WelcomeActivity.firsttime.edit();
	editor.putBoolean("FIRST",false);
	editor.commit();
	Toast.makeText(getBaseContext(), "Password Not Changed", Toast.LENGTH_SHORT).show();
}
@Override
protected void onPause() {
	//TODO Auto-generated method stub
	super.onPause();
	this.finish();
}

}
