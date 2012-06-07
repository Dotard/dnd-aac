package com.dnd.aac;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
      private int mPictoSize;
      
	    public MyPictoAdapter(Context c,int layout,Cursor cursor, ImageResizer mImageWorker) {
	    	 mContext = c;
		        this.layout = layout;
		        mCursor = cursor;
		        uriIndex = mCursor.getColumnIndex("imageUri");
		        phraseIndex = mCursor.getColumnIndex("pictoPhrase");	
		        this.mImageWorker = mImageWorker;
		        
		        
	        	refreshPictoSize();
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
	        	convertView.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
	        		        		        	
	        	holder = new ViewHolder();
	        	holder.text = (TextView)convertView.findViewById(R.id.text);
	        	holder.image = (ImageView)convertView.findViewById(R.id.image);
	        	holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        	holder.imageUri = mCursor.getString(mCursor.getColumnIndex("imageUri"));
	        	convertView.setTag(holder);
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        	
	        	if(convertView.getLayoutParams().height != mPictoSize){
	        		convertView.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
	        	}
	        }
	        	
	        holder.text.setText(mCursor.getString(phraseIndex));	        	
	        
	        mImageWorker.loadImage(mCursor.getString(mCursor.getColumnIndex("imageUri")), holder.image);
		        
	        return convertView;
	    }
	    
	    public void refreshPictoSize(){
	    	mPictoSize = MyPreferences.getPictoSize(mContext, mContext.getString(R.string.pref_pictosize));	    			
	    }
	    
		class ViewHolder{
			public TextView text;
			public ImageView image;
			public String imageUri;
		}		
		
}


