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
import org.json.JSONObject;

import com.edesia.ItemsActivity.LoadItems;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends ActionBarActivity{
ListView CartList;
TextView noOfResults,GrandTotal,NoProduct;
LinearLayout header;
CartAdapter adapter;
Button next;
int i;
JSONArray cartArray;
JSONObject cartObject;
static JSONObject sendObject;
static String token;
public String returnString;
ConnectionDetector cd;
Boolean isInternetPresent,isServerOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_activity);
		CartList=(ListView)findViewById(R.id.cartlist);
		noOfResults=(TextView)findViewById(R.id.noOfItems);
		GrandTotal=(TextView)findViewById(R.id.totalPrice);
		header=(LinearLayout)findViewById(R.id.header);
		next=(Button)findViewById(R.id.placeorder);
		NoProduct=(TextView)findViewById(R.id.noproduct);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(null);
		getSupportActionBar().setTitle("CART- Tap And Hold on an Item to Remove");
		registerForContextMenu(CartList);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
		cartArray=new JSONArray();
		try{
		adapter=new CartAdapter(getBaseContext(),ItemsActivity.CartItems);
		adapter.notifyDataSetChanged();
		CartList.setAdapter(adapter);
		noOfResults.setText("My Cart("+adapter.getCount()+")");
		GrandTotal.setText("Rs."+ItemsActivity.total);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			Toast.makeText(getBaseContext(),"No Items added", Toast.LENGTH_SHORT).show();
		}
		if(adapter.getCount()>0)
		{
			//((ViewManager)NoProduct.getParent()).removeView(NoProduct);
			NoProduct.setVisibility(View.INVISIBLE);
			header.setVisibility(View.VISIBLE);
			CartList.setVisibility(View.VISIBLE);
		}
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0){
			try
			{
				if(CartList.getCount()>0)
				{
					for(int i=0;i<CartList.getCount();i++)
					{
					 int cost=Integer.parseInt(ItemsActivity.cartCosts.get(i).toString())*Integer.parseInt(ItemsActivity.cartQuantity.get(i).toString());
					 cartObject=new JSONObject();
					 cartObject.put("itemname",ItemsActivity.cartNames.get(i).toString());
					 cartObject.put("itemid", ItemsActivity.cartIds.get(i).toString());
					 cartObject.put("cost", String.valueOf(cost));
					 cartObject.put("quantity",ItemsActivity.cartQuantity.get(i).toString());
					 cartArray.put(cartObject);	
					}
					sendObject=new JSONObject();
					sendObject.put("cart", cartArray);	
					Log.d("send",sendObject.toString());
					ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
					  
					Boolean isInternetPresent = cd.isConnectingToInternet(); 
					if(isInternetPresent)
						new Transaction().execute(sendObject.toString());
					else
						Toast.makeText(getBaseContext(), "Cannot Connect to the Internet", Toast.LENGTH_LONG).show();
					
				}
				else
				{
					Toast.makeText(getBaseContext(), "No Items in the cart",Toast.LENGTH_SHORT).show();
				}
					}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		});
		
	}
	
class Transaction extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... arg0) {
		if(isServerOK)
		{
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/Transaction/Process");
	    try {
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("cart",arg0[0]));
	        nameValuePairs.add(new BasicNameValuePair("username",WelcomeActivity.userName));   
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        returnString=Read.readJson(response);
       
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		}
		else
		{
			Toast.makeText(getBaseContext(), "Cannot Connect to Server", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(isServerOK)
		{
		if(!returnString.trim().equalsIgnoreCase("NO")){
			if(!returnString.trim().equalsIgnoreCase("Insufficient"))
			{
		token=returnString;	
		Log.d("token",token);
		for(int i=0;i<ItemsActivity.CartItems.size();i++)
		ItemsActivity.CartItems.remove(i);
		ItemsActivity.CartItems.clear();
		adapter.notifyDataSetChanged();
		ItemsActivity.cartCosts.clear();
		ItemsActivity.cartIds.clear();
		ItemsActivity.CartItems.clear();
		ItemsActivity.cartNames.clear();
		ItemsActivity.cartQuantity.clear();
		ItemsActivity.cartTiming.clear();
		Intent tokenIntent=new Intent("com.edesia.TOKENACTIVITY");
		startActivity(tokenIntent);
		ItemsActivity.total=0;
		GrandTotal.setText("Rs."+ItemsActivity.total);
		NoProduct.setVisibility(View.VISIBLE);
		header.setVisibility(View.INVISIBLE);
		CartList.setVisibility(View.INVISIBLE);
			}
			else
			{
				Toast.makeText(getBaseContext(), "Insufficient Balance.Please Recharge!!", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(getBaseContext(), "Something Went Wrong!! please Try Again.",Toast.LENGTH_SHORT).show();
		}
		}
		else
		{
			Toast.makeText(getBaseContext(), "Cannot Connect To Server",Toast.LENGTH_SHORT).show();
		}
	}
	
}
@Override    
public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)  
{  
        super.onCreateContextMenu(menu, v, menuInfo);      
        menu.add(0, v.getId(), 0, "Remove from cart");//groupId, itemId, order, title    
}   
  
@Override    
public boolean onContextItemSelected(MenuItem item){    
	 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if(item.getTitle()=="Remove from cart"){  
            ItemsActivity.CartItems.remove(info.position);
            Log.d(ItemsActivity.cartCosts.get(info.position).toString(),ItemsActivity.cartQuantity.get(info.position).toString());
            ItemsActivity.total=ItemsActivity.total-Integer.parseInt(ItemsActivity.cartCosts.get(info.position).toString())*Integer.parseInt(ItemsActivity.cartQuantity.get(info.position).toString());
    		GrandTotal.setText("Rs."+ItemsActivity.total);
    		noOfResults.setText("My Cart("+adapter.getCount()+")");
    		ItemsActivity.cartNames.remove(info.position);
    		ItemsActivity.cartCosts.remove(info.position);
    		ItemsActivity.cartIds.remove(info.position);
    		ItemsActivity.cartQuantity.remove(info.position);
    		ItemsActivity.cartTiming.remove(info.position);
            adapter.notifyDataSetChanged();
            if(CartList.getCount()<1)
            {
            	NoProduct.setVisibility(View.VISIBLE);
        		header.setVisibility(View.INVISIBLE);
        		CartList.setVisibility(View.INVISIBLE);
            }
        }    
        else{  
           return false;  
        }    
      return true;    
  } 
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
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
