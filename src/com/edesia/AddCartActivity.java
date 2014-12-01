package com.edesia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddCartActivity extends Activity{
TextView Title;
static String name,breakfast,lunch,firstbreak,id,cost,Quantity,Timing;
RadioButton breakfastradio,firstbreakradio,lunchradio,selected;
RadioGroup timinggroup;
Spinner Quants;

Button AddToCart,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Au to-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_cart_dialog);
		Title=(TextView)findViewById(R.id.ItemTitle);
		timinggroup=(RadioGroup)findViewById(R.id.TimingGroup);
		Quants=(Spinner)findViewById(R.id.QtySpinner);
		breakfastradio=(RadioButton)findViewById(R.id.breakfastradio);
		firstbreakradio=(RadioButton)findViewById(R.id.firstbreakradio);
		lunchradio=(RadioButton)findViewById(R.id.lunchradio);
		AddToCart=(Button)findViewById(R.id.CartButton);
		cancel=(Button)findViewById(R.id.CancelBtn);
		Intent newIntent = getIntent();
		setTitle("Select Timing and Quantity");
		name=newIntent.getStringExtra("Name");
		breakfast=newIntent.getStringExtra("BreakFast");
		firstbreak=newIntent.getStringExtra("FirstBreak");
		lunch=newIntent.getStringExtra("Lunch");
		id=newIntent.getStringExtra("ID");
		cost=newIntent.getStringExtra("Cost");
		Title.setText(name);
		if(breakfast.equalsIgnoreCase("no"))
		{
			timinggroup.removeViewAt(0);
		}
		if(firstbreak.equalsIgnoreCase("no"))
		{
			timinggroup.removeViewAt(1);
		}
		if(lunch.equalsIgnoreCase("no"))
		{
			timinggroup.removeViewAt(2);
		}
	AddToCart.setOnClickListener(new OnClickListener(){
		CartItem item;
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try
			{
			  Quantity= Quants.getSelectedItem().toString();
			  int pos=timinggroup.getCheckedRadioButtonId();
			  selected=(RadioButton)findViewById(pos);
			  Timing=selected.getText().toString();
			  ItemsActivity.cartNames.add(name);
			  ItemsActivity.cartCosts.add(cost);
			  ItemsActivity.cartIds.add(id);
			  ItemsActivity.cartQuantity.add(Quantity);
			  ItemsActivity.cartTiming.add(Timing);
			  ItemsActivity.i++;
			  if(Quantity!=null && selected!=null)
			  {				  
				  Log.d(cost,Quantity);
			  int price=Integer.parseInt(Quantity)*Integer.parseInt(cost);
			  ItemsActivity.total+=price;
			  Log.d("total",String.valueOf(ItemsActivity.total));
			   item = new CartItem(name,Quantity,Timing,"Rs."+String.valueOf(price));
			  ItemsActivity.CartItems.add(item);
			  ItemsActivity.menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.cartavail));
			  Toast.makeText(getBaseContext(), "Added To Cart",Toast.LENGTH_SHORT).show();
			  finish();					  
			  }
			}
			catch(NullPointerException e)
			{
				  Toast.makeText(getBaseContext(), "Please select the timing", Toast.LENGTH_SHORT).show();
			}
				      				
		}
		
	});
	cancel.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	});
	}

}
