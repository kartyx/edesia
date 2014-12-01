package com.edesia;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CartAdapter extends BaseAdapter {
	 
 
    private List<CartItem> items = new ArrayList<CartItem>();

	private Context context;
 
    public CartAdapter(Context context, List<CartItem> items) {
        this.items = items;
        this.context=context;
    }
     public  CartAdapter(List<CartItem> items)
      {		
    	  this.items=items;
      }
    public int getCount() {
        return items.size();
    }
 
    public CartItem getItem(int position) {
        return items.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
        	    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        CartItem s = items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_list, null);
            holder = new ViewHolder();
            holder.Name=(TextView) convertView.findViewById(R.id.CartTitle);
            holder.Cost=(TextView) convertView.findViewById(R.id.CartPrice);
            holder.Quantity=(TextView)convertView.findViewById(R.id.CartQuantity);
            holder.Timing=(TextView)convertView.findViewById(R.id.CartTiming);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Name.setText(s.getName());
        holder.Cost.setText(s.getCost());
        holder.Quantity.setText(s.getQuantity());
        holder.Timing.setText(s.getTiming());
        return convertView;
    }
 
    static class ViewHolder {
        TextView Name,Cost,Quantity,Timing;       
    }


	}