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
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnd.aac.cache.DiskLruCache;
import com.dnd.aac.cache.ImageResizer;

public class MyPictoAdapter extends BaseAdapter{
	  private Context mContext;
	  private int layout;
	  private Cursor mCursor;
	  private int uriIndex;
	  private int phraseIndex;
	  
	  private int mItemHeight = 0;
      private int mNumColumns = 0;
      private int mActionBarHeight = -1;
      private GridView.LayoutParams mImageViewLayoutParams;
      private ImageResizer mImageWorker;
      
      
	  
	  private int uriIndex;
	  private int phraseIndex;
	  
	    public MyPictoAdapter(Context c,int layout,Cursor cursor, ImageResizer mImageWorker) {
	    	 mContext = c;
		        this.layout = layout;
		        mCursor = cursor;
		        uriIndex = mCursor.getColumnIndex("imageUri");
		        phraseIndex = mCursor.getColumnIndex("pictoPhrase");	
		        this.mImageWorker = mImageWorker;
		    }

	    public int getCount() {
	        return mCursor.getCount();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        mCursor.moveToPosition(position);
	        if (mCursor.getColumnIndex("_id") == -1) return 0;
	        return mCursor.getLong(mCursor.getColumnIndex("_id"));
	    }
	    
	    public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }
        
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
            mImageWorker.setImageSize(height);
            notifyDataSetChanged();
        }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        mCursor.moveToPosition(position);
	        ViewHolder holder;
	        	        
	        if (convertView == null) {  
	        	convertView = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(layout,null);
	        	convertView.setLayoutParams(new GridView.LayoutParams(115,115));
	        		        	
	        	holder = new ViewHolder();
	        	holder.text = (TextView)convertView.findViewById(R.id.text);
	        	holder.image = (ImageView)convertView.findViewById(R.id.image);
	        	holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        	convertView.setTag(holder);
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        }
	        	
	        holder.text.setText(mCursor.getString(phraseIndex));	        	
	        
//	        try {
//	        	InputStream fileStream = MainActivity.mExpansionFile.getInputStream("picto/" + mCursor.getString(mCursor.getColumnIndex("imageUri")));
//	        	
////	        	BufferedInputStream buf = new BufferedInputStream(new FileInputStream("/mnt/sdcard/Android/data/com.dnd.aac/main.1.com.dnd.aac/picto/" + mCursor.getString(uriIndex)));
//	        	BufferedInputStream buf = new BufferedInputStream(fileStream);
//	        	Bitmap bitmap = BitmapFactory.decodeStream(buf);
//	        	holder.image.setImageBitmap(bitmap);
//	        	buf.close();
//	        } catch (IOException e) {
//	        	e.printStackTrace();
//	        }
	        
	        mImageWorker.loadImage(mCursor.getString(mCursor.getColumnIndex("imageUri")), holder.image);
		        
	        return convertView;
	    }

		class ViewHolder{
			public TextView text;
			public ImageView image;
		}		
		
}


