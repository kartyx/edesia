package com.edesia;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TokenActivity extends ActionBarActivity {
TextView set;
Editor editor;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.token_activity);
	set=(TextView)findViewById(R.id.token);
	set.setText("Your Transaction ID is "+CartActivity.token);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	getSupportActionBar().setTitle("Transaction ID");
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater i=getMenuInflater();
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
	case android.R.id.home: 
		this.finish();
		break;
	default:
	return super.onOptionsItemSelected(item);
	}
	return true;
}

}
