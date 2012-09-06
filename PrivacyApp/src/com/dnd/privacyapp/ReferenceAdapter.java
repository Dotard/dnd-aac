package com.dnd.privacyapp;

import java.util.ArrayList;

import com.dnd.privacyapp.data.PrivacyAppDatabase;
import com.dnd.privacyapp.data.PrivacyAppProvider;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReferenceAdapter extends SimpleCursorAdapter implements OnClickListener{

    private Context mContext;
    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater;
    private int titleCol;
    private int quizCompCol;
    private int idCol;
   
    
    private final class ViewHolder {
        public TextView title;
        public ImageView img;
    }

    public ReferenceAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int a){
        super(context, layout, c, from, to,a);

        this.mContext = context;
        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);
        
        titleCol = mCursor.getColumnIndex("secName");
        quizCompCol = mCursor.getColumnIndex("secQuizComplete");
        idCol = mCursor.getColumnIndex("_id");


    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(mLayout, null);

                viewHolder = new ViewHolder();               
                
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.listimage);
                
                convertView.setTag(viewHolder);
                
//                viewHolder.checkBox.setOnClickListener(this);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }   
            
            String text = mCursor.getString(titleCol);
            viewHolder.title.setText( (position+1) + ") " + text);
            
            
            String[] projection = { "secQuizComplete" };
            Cursor c = mContext.getContentResolver().query( Uri.withAppendedPath(PrivacyAppProvider.SECTION_URI,mCursor.getString(idCol)), projection, null, null, null);
            c.moveToFirst();
            Boolean quizComplete = c.getInt(0) == 1 ? true : false; 
            if(quizComplete) viewHolder.img.setImageResource(R.drawable.list_item_arrow_complete);
            else viewHolder.img.setImageResource(R.drawable.list_item_arrow_incomplete);        
        }

        return convertView;
    }
    
    
    @Override
    public void onClick(View v) {
//        CheckBox cBox = (CheckBox) v;
//        
//        ViewHolder viewHolder = (ViewHolder)((View)v.getParent()).getTag();
//        int position = viewHolder.position;
// 
//        if (cBox.isChecked()) {
//        	fragment.adjustProgress(1);
//        	checkStates.set(position, true);        	
//        } else {
//        	fragment.adjustProgress(-1);
//        	checkStates.set(position, false);
//        }
    }

}
