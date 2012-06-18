package com.dnd.privacyapp;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ProgressBar;

public class CheckListActivity extends ListActivity{
	
	private SimpleCursorAdapter adapter;
	private Cursor tutorialCursor;
	private long id;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		
		Intent i = getIntent();
		id = i.getLongExtra("ID", -1);
		
		collectListData();
	}
	
	private void collectListData()
    {	
    	String projection[] = {"procedureItemsID as _id","procedureItemsText" };
        tutorialCursor = this.getContentResolver().query(
        		Uri.withAppendedPath(PrivacyAppProvider.PROCEDUREITEMS_URI,String.valueOf(id)), projection, null, null, null);
        
        String[] uiBindFrom = { "procedureItemsText" }; //Add as many columns using ,
        int[] uiBindTo = { R.id.checkbox }; //And you can bind it to as many variables using ,

        ProgressBar pb = (ProgressBar) findViewById(R.id.progess_bar);
        pb.setMax(tutorialCursor.getCount());
        
        adapter = new CheckBoxAdapter(
                this, R.layout.checkbox_item,
                tutorialCursor, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
     
        setListAdapter(adapter);
    }
}
