package com.edesia;

import android.graphics.Bitmap;

public class DashItem {
	 
    private String name;
 
    private String Quantity,Cost,Date,Id;
        
 
    private DashBoardAdapter da;
 
    public DashItem(String name,String Quantity,String Date,String Cost,String Id) {
                this.name = name;
                this.Cost= Cost;
                this.Quantity=Quantity;
                this.Date=Date;
                this.Id=Id;
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
    public void setDate(String Date)
        {
        	this.Date=Date;
        }
    public String getDate()
        {
    	 return Date;
        }
    public void setId(String Id)
    {
    	this.Id=Id;
    }
public String getId()
    {
	 return Id;
    }
    
    public DashBoardAdapter getAdapter() {
        return da;
    }
 
    public void setAdapter(DashBoardAdapter ca) {
        this.da = da;
    } 
}