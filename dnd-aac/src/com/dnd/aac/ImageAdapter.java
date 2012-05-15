package com.dnd.aac;

import java.io.BufferedInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	  private Context mContext;
	  private Cursor mCursor;

	    public ImageAdapter(Context c,Cursor cursor) {
	        mContext = c;
	        mCursor = cursor;
	    }

	    public int getCount() {
	        return mCursor.getCount();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        mCursor.moveToPosition(position);
	        return mCursor.getLong(0);
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new CustomImageView(mContext,"hi im a dog");
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	            
	            mCursor.moveToPosition(position);
	            AssetManager am = mContext.getAssets();
		        try {
					BufferedInputStream buf = new BufferedInputStream(am.open("images/" + mCursor.getString(1)));
					Bitmap bitmap = BitmapFactory.decodeStream(buf);
					imageView.setImageBitmap(bitmap);
					buf.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
	        } else {
	            imageView = (ImageView) convertView;
	        }	   

	        
	       
	        return imageView;
	    }
}

