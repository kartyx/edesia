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

public class DashBoardAdapter extends BaseAdapter {
    private List<DashItem> items = new ArrayList<DashItem>();

	private Context context;
 
    public DashBoardAdapter(Context context, List<DashItem> items) {
        this.items = items;
        this.context=context;
    }
     public  DashBoardAdapter(List<DashItem> items)
      {		
    	  this.items=items;
      }
    public int getCount() {
        return items.size();
    }
 
    public DashItem getItem(int position) {
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
        DashItem s = items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dash_list, null);
            holder = new ViewHolder();
            holder.Name=(TextView) convertView.findViewById(R.id.DashItem);
            holder.Date=(TextView) convertView.findViewById(R.id.DashDate);
            holder.Cost=(TextView) convertView.findViewById(R.id.DashCost);
            holder.Quantity=(TextView)convertView.findViewById(R.id.DashQuantity);
            holder.TransId=(TextView)convertView.findViewById(R.id.DashTransId);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Name.setText(s.getName());
        holder.Cost.setText(s.getCost());
        holder.Quantity.setText(s.getQuantity());
        holder.TransId.setText(s.getId());
        holder.Date.setText(s.getDate());
        return convertView;
    }
 
    static class ViewHolder {
        TextView Name,Cost,Quantity,TransId,Date;       
    }


	}