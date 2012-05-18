package com.dnd.aac;

import java.io.BufferedInputStream;
import java.io.IOException;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPictoAdapter extends BaseAdapter{
	  private Context mContext;
	  private int layout;
	  private Cursor mCursor;
	  
	    public MyPictoAdapter(Context c,int layout,Cursor cursor) {
	        mContext = c;
	        this.layout = layout;
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
	        return mCursor.getLong(mCursor.getColumnIndexOrThrow("_id"));
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View picto;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	mCursor.moveToPosition(position);
	        	
	        	picto = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(layout,null);	        	
	        	picto.setLayoutParams(new GridView.LayoutParams(115,115));	
	        	
	        	ImageView imageView = (ImageView)picto.findViewById(R.id.image);
	            AssetManager am = mContext.getAssets();
		        try {
					BufferedInputStream buf = new BufferedInputStream(am.open("images/" + mCursor.getString(mCursor.getColumnIndex("imageUri"))));
					Bitmap bitmap = BitmapFactory.decodeStream(buf);
					imageView.setImageBitmap(bitmap);
					buf.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        TextView tv = (TextView)picto.findViewById(R.id.text);
		        tv.setText(mCursor.getString(mCursor.getColumnIndex("pictoPhrase")));
		             
	        } else {
	        	picto = convertView;	        	
	        }	   
    
	        return picto;
	    }
}


