package com.dnd.aac;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;

public class MyPictoAdapter extends BaseAdapter{
	  private Context mContext;
	  private int layout;
	  private Cursor mCursor;
	  private ZipResourceFile mExpansionFile;
	  private int uriIndex;
	  private int phraseIndex;
	  
	    public MyPictoAdapter(Context c,int layout,Cursor cursor) {
	        mContext = c;
	        this.layout = layout;
	        mCursor = cursor;
	        uriIndex = mCursor.getColumnIndex("imageUri");
	        phraseIndex = mCursor.getColumnIndex("pictoPhrase");
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
	    @Override
		public View getView(int position, View convertView, ViewGroup parent) {
	        mCursor.moveToPosition(position);
	        ViewHolder holder;
	        if (convertView == null) {  
	        	convertView = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(layout,null);
	        	convertView.setLayoutParams(new GridView.LayoutParams(115,115));
	        	
	        	holder = new ViewHolder();
	        	holder.text = (TextView)convertView.findViewById(R.id.text);
	        	holder.image = (ImageView)convertView.findViewById(R.id.image);
	        	convertView.setTag(holder);
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        }
	        	
	        holder.text.setText(mCursor.getString(phraseIndex));	        	
	        	
	        try {
	        	InputStream fileStream = mExpansionFile.getInputStream("picto/" + mCursor.getString(mCursor.getColumnIndex("imageUri")));
//	        	BufferedInputStream buf = new BufferedInputStream(new FileInputStream("/mnt/sdcard/Android/data/com.dnd.aac/main.1.com.dnd.aac/picto/" + mCursor.getString(uriIndex)));
	        	BufferedInputStream buf = new BufferedInputStream(fileStream);
	        	Bitmap bitmap = BitmapFactory.decodeStream(buf);
	        	holder.image.setImageBitmap(bitmap);
	        	buf.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		        
	        return convertView;
	    }

		class ViewHolder{
			public TextView text;
			public ImageView image;
		}
}


