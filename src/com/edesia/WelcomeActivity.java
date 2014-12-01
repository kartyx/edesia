package com.edesia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
static String host="192.168.43.60";
static SharedPreferences firsttime;
Editor editor;
static boolean loggedin=false;
static boolean first=true;
static String userName;
String Password;
Boolean isInternetPresent,isServerRunning;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		firsttime=getSharedPreferences("LOGINPREFERS",Context.MODE_PRIVATE);
		final ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();  
		Thread t=new Thread()
		{
			public void run()
			{
				try{
					sleep(2000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
				  if(isInternetPresent)
				  {
					if(!firsttime.contains("FIRST"))
					{
						Intent loginIntent=new Intent("com.edesia.LOGINACTIVITY");
						startActivity(loginIntent);
					}
					else
					{
						first=firsttime.getBoolean("FIRST",false);
						userName=firsttime.getString("user", null);
						Password=firsttime.getString("pass", null);
						loggedin=firsttime.getBoolean("loggedin",false);
						if(loggedin)
						{
							Intent itemIntent=new Intent("com.edesia.ITEMSACTIVITY");
							startActivity(itemIntent);
						}
						else
						{
							Intent loginIntent=new Intent("com.edesia.LOGINACTIVITY");
							startActivity(loginIntent);
						}
					}
				  }
				  else
				  {
					  kill();
				  }				
			}
			}
		};
	t.start();
		if(!isInternetPresent)
		{
		Toast.makeText(getBaseContext(), "No Internet Connection",Toast.LENGTH_SHORT).show();
		}
	}
		
	protected void kill() {
		// TODO Auto-generated method stub
		this.finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}
}
