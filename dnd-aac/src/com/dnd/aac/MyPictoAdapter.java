package com.dnd.aac;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;

import android.content.Context;
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
	  private ZipResourceFile mExpansionFile;
	  
	    public MyPictoAdapter(Context c,int layout,Cursor cursor) {
	        mContext = c;
	        this.layout = layout;
	        mCursor = cursor;
	        try {
				mExpansionFile = APKExpansionSupport.getAPKExpansionZipFile(mContext,
				        1, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
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
		        try {
					
					InputStream fileStream = mExpansionFile.getInputStream("picto/" + mCursor.getString(mCursor.getColumnIndex("imageUri")));
					
					BufferedInputStream buf = new BufferedInputStream(fileStream);
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


