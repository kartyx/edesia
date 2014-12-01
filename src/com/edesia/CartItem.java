package com.edesia;

import android.graphics.Bitmap;

public class CartItem {
	 
    private String name;
 
    private String Quantity,Cost,Timing;
        
 
    private CartAdapter ca;
 
    public CartItem(String name,String Quantity,String Timing,String Cost) {
                this.name = name;
                this.Cost= Cost;
                this.Quantity=Quantity;
                this.Timing=Timing;
    }
 
    public String getCost() {
        return Cost;
    }
 
    public void setCost(String Cost) {
        this.Cost = Cost;
    }
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(String Quantity)
    {
    	this.Quantity=Quantity;
    }
    public String getQuantity()
    {
	 return Quantity;
    }
    public void setTiming(String Timing)
        {
        	this.Timing=Timing;
        }
    public String getTiming()
        {
    	 return Timing;
        }
      
    public CartAdapter getAdapter() {
        return ca;
    }
 
    public void setAdapter(CartAdapter ca) {
        this.ca = ca;
    } 
}