package com.dnd.privacyapp;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProcedureAdapter extends SimpleCursorAdapter implements OnClickListener{

    private Context mContext;
    private int mLayout;
    private Cursor mCursor;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Boolean> checkStates;
    private int textCol;
    private ProceduresViewFragment fragment;
    
    private final class ViewHolder {
        public CheckBox checkBox;
        public int position;
    }

    public ProcedureAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int a,ProceduresViewFragment fragment){
        super(context, layout, c, from, to,a);

        this.mContext = context;
        this.mLayout = layout;
        this.mCursor = c;
        this.mLayoutInflater = LayoutInflater.from(context);        
        checkStates = new ArrayList<Boolean>();
        for(int i = 0; i < mCursor.getCount(); i++){
        	checkStates.add(false);
        }
        
        textCol = mCursor.getColumnIndexOrThrow("procedureItemsText");
        
        this.fragment = fragment;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (mCursor.moveToPosition(position)) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(mLayout, null);

                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                
                convertView.setTag(viewHolder);
                
                viewHolder.checkBox.setOnClickListener(this);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }   
            
            String text = mCursor.getString(textCol);
            viewHolder.checkBox.setText( (position+1) + ". " + text);
            viewHolder.checkBox.setChecked(checkStates.get(position));
            viewHolder.position = position;
        }

        return convertView;
    }
    
    
    @Override
    public void onClick(View v) {
        CheckBox cBox = (CheckBox) v;
        
        ViewHolder viewHolder = (ViewHolder)((View)v.getParent()).getTag();
        int position = viewHolder.position;
 
        if (cBox.isChecked()) {
        	fragment.adjustProgress(1);
        	checkStates.set(position, true);        	
        } else {
        	fragment.adjustProgress(-1);
        	checkStates.set(position, false);
        }
    }

}
