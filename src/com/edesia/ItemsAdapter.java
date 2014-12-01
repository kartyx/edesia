package com.edesia;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemsAdapter extends BaseAdapter {
	 
    private static LayoutInflater mInflater;
 
    private List<ListItem> items = new ArrayList<ListItem>();

	private Context context;
 
    public ItemsAdapter(Context context, List<ListItem> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.context=context;
    }
     public  ItemsAdapter(List<ListItem> items)
      {		
    	  this.items=items;
      }
    public int getCount() {
        return items.size();
    }
 
    public ListItem getItem(int position) {
        return items.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ListItem s = items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.items_list, null);
            holder = new ViewHolder();
            holder.Name=(TextView) convertView.findViewById(R.id.ItemName);
            holder.Cost=(TextView) convertView.findViewById(R.id.ItemPrice);
            holder.image = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Name.setText(s.getName());
        holder.Cost.setText(s.getCost());
        if (s.getImage() != null) {
            holder.image.setImageBitmap(s.getImage());
        } else {
                // MY DEFAULT IMAGE
            holder.image.setImageResource(R.drawable.ic_launcher);
        }

        return convertView;
    }
 
    static class ViewHolder {
        TextView Name,Cost;
        ImageView image;
        
        
    }

	public void setContext(FragmentActivity activity) {
		// TODO Auto-generated method stub
        mInflater = LayoutInflater.from(activity);

	}

	}