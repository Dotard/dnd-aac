package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CheckBoxAdapter extends SimpleCursorAdapter implements OnClickListener {

    private Context mContext;
    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater; 
    private final class ViewHolder {
        public CheckBox checkBox;
    }

    public CheckBoxAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int a) {
        super(context, layout, c, from, to,a);

        this.mContext = context;
        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);        
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(mLayout, null);

                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setOnClickListener(this);


                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }   
            
            String text = mCursor.getString(1);
            viewHolder.checkBox.setText(text);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {

        CheckBox cBox = (CheckBox) v;
        
        /* 
            How can I access all the checkboxes here?
        */
        
        CheckListActivity cla = (CheckListActivity) mContext;
        ProgressBar pb = (ProgressBar) cla.findViewById(R.id.progess_bar);
        TextView tv = (TextView) cla.findViewById(R.id.progresstext);
 
        if (cBox.isChecked()) {
        	pb.setProgress(pb.getProgress() + 1);
        	
        } else {
        	pb.setProgress(pb.getProgress() - 1);
        }
        
        if(pb.getProgress() == pb.getMax())
        {
        	tv.setVisibility(View.VISIBLE); 
        }
        else
        {
        	tv.setVisibility(View.GONE); 
        }

    }
}
