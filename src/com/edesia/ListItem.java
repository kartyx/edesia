package com.edesia;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class ListItem {
	 
    private String name;
 
    private String imgUrl,Cost;
    
    private Bitmap image;
    
 
    private ItemsAdapter ia;
 
    public ListItem(String name, String imgUrl,String Cost) {
                this.name = name;
                this.imgUrl = imgUrl;
                this.Cost= Cost;
                this.image = null;
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
    public String getImgUrl() {
        return imgUrl;
    }
 
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
 
        public Bitmap getImage() {
        return image;
    }
 
    public ItemsAdapter getAdapter() {
        return ia;
    }
 
    public void setAdapter(ItemsAdapter ia) {
        this.ia = ia;
    }
 
    public void loadImage(ItemsAdapter ia) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.ia = ia;
        if (imgUrl != null && !imgUrl.equals("")) {
            new ImageLoadTask().execute(imgUrl);
        }
    }
 
    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {
 
        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }
 
        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = ImageService.getBitmapFromURL(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        protected void onProgressUpdate(String... progress) {
            // NO OP
        }
 
        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
                image = ret;
                if (ia != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    ia.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + name + " image");
            }
        }
    }
 
}