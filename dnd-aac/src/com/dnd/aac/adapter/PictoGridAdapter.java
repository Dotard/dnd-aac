package com.dnd.aac.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnd.aac.R;
import com.dnd.aac.R.id;
import com.dnd.aac.R.string;
import com.dnd.aac.cache.ImageResizer;
import com.dnd.aac.data.ImagesTbl;
import com.dnd.aac.data.PictosTbl;
import com.dnd.aac.util.MyPreferences;

public class PictoGridAdapter extends SimpleCursorAdapter implements Filterable{
	  private Context mContext;
	  private int layout;
	  private int uriIndex;
	  private int phraseIndex;
	  
	  private int mItemHeight = 0;
      private int mNumColumns = 0;
      private int mActionBarHeight = -1;
      private GridView.LayoutParams mImageViewLayoutParams;
      private ImageResizer mImageWorker;
      private int mPictoSize;
      private GridView parentView;
      
      public PictoGridAdapter(Context context,int layout,Cursor cursor, ImageResizer mImageWorker, String[] from, int[] to ,GridView parent) {
	    	super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	    	mContext = context;
	        this.layout = layout;
	        uriIndex = cursor.getColumnIndex(ImagesTbl.imageUri);
	        phraseIndex = cursor.getColumnIndex(PictosTbl.pictoPhrase);	
	        this.mImageWorker = mImageWorker;
	        this.parentView = parent;
	        
        	refreshPictoSize();
      }

		public int getCount() {
	        return getCursor().getCount();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	    	getCursor().moveToPosition(position);
	        if (getCursor().getColumnIndex("_id") == -1) return 0;
	        return getCursor().getLong(getCursor().getColumnIndex("_id"));
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
	    	getCursor().moveToPosition(position);
	        
	        PictoViewHolder holder;     
	        if (convertView == null) {  
	        	convertView = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(layout,null);	            	 	        	    
	        	convertView.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
	        		        		        	
	        	holder = new PictoViewHolder();
	        	holder.text = (TextView)convertView.findViewById(R.id.text);
	        	holder.image = (ImageView)convertView.findViewById(R.id.image);
	        	holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        	holder.imageUri = getCursor().getString(uriIndex);
	        	convertView.setTag(holder);
	        } else {
	        	holder = (PictoViewHolder) convertView.getTag();
	        	
	        	if(convertView.getLayoutParams().height != mPictoSize){
	        		convertView.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
	        	}
	        }

	        holder.text.setText(getCursor().getString(phraseIndex));	        	
	        mImageWorker.loadImage(getCursor().getString(uriIndex), holder.image);
		        
	        return convertView;
	    }
	    
	    public void refreshPictoSize(){
	    	mPictoSize = MyPreferences.getPictoSize(mContext, mContext.getString(R.string.pref_pictosize));
	    	parentView.setColumnWidth(mPictoSize + 10);
	    }
	    
		public class PictoViewHolder{
			public TextView text;
			public ImageView image;
			public String imageUri;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			//cursor.moveToPosition(position);
			PictoViewHolder holder = (PictoViewHolder) view.getTag();
        	if(view.getLayoutParams().height != mPictoSize){
        		view.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
        	}
        	 holder.text.setText(cursor.getString(phraseIndex));	        	
 	        
 	        mImageWorker.loadImage(cursor.getString(uriIndex), holder.image);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			//mBaseCursor.moveToPosition(position);
			final View view = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(layout,null);	            	 	        	    
			view.setLayoutParams(new GridView.LayoutParams(mPictoSize,mPictoSize));
        		        		        	
			PictoViewHolder holder = new PictoViewHolder();
        	holder.text = (TextView)view.findViewById(R.id.text);
        	holder.image = (ImageView)view.findViewById(R.id.image);
        	holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        	holder.imageUri = getCursor().getString(uriIndex);
        	view.setTag(holder);
        	holder.text.setText(getCursor().getString(phraseIndex));	        	
 	        
 	        mImageWorker.loadImage(getCursor().getString(uriIndex), holder.image);
 		        
 	        return view;
		}
		
		
		
}


