package com.edesia;

import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
  
public class ConnectionDetector { 
      
    private Context _context;
      
    public ConnectionDetector(Context context){
        this._context = context;
    } 
  
   public boolean isConnectingToInternet(){ 
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          { 
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      { 
                          return true; 
                      } 
  
          } 
          return false; 
    } 
    
    public boolean isConnectingToServer()
    {
    	try
    	{
    		String url="http://"+WelcomeActivity.host+":8080";
    		URL serverURL= new URL(url);
    		URLConnection connection= serverURL.openConnection();
    		connection.setConnectTimeout(7000);
    		connection.setReadTimeout(8000);
    		connection.connect();
    		return true;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    }
} 