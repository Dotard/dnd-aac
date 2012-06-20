package com.dnd.privacyapp;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.TextView;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.R;
import com.dnd.privacyapp.widgets.SaundProgressBar;

public class CheckListActivity extends ListActivity{
	
	private CheckBoxAdapter adapter;
	private SaundProgressBar mPbar;
	private long id;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		
		//Set up progress bar
		mPbar = (SaundProgressBar) findViewById(R.id.regularprogressbar);
	    
	    Drawable indicator = getResources().getDrawable(R.drawable.progress_indicator);
	    Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 5, indicator.getIntrinsicHeight());
	    
	    indicator.setBounds(bounds);
	    
	    mPbar.setProgressIndicator(indicator);
	    
		Intent i = getIntent();
		id = i.getLongExtra("ID", -1);
		String title = i.getStringExtra("Title");
		TextView tv = (TextView)findViewById(R.id.checkHeader);
		tv.setText(title);
		
		collectListData();		
		
		
	}
	
	private void collectListData()
    {	
    	String projection[] = {"procedureItemsID as _id","procedureItemsText" };
    	String sortOrder = "procedureItemsOrder ASC";
        Cursor tutorialCursor = this.getContentResolver().query(
        		Uri.withAppendedPath(PrivacyAppProvider.PROCEDUREITEMS_URI,String.valueOf(id)), projection, null, null, sortOrder);
        
        String[] uiBindFrom = { "procedureItemsText" }; //Add as many columns using ,
        int[] uiBindTo = { R.id.checkbox }; //And you can bind it to as many variables using ,
       
        mPbar.setMax(tutorialCursor.getCount());
        
        adapter = new CheckBoxAdapter(
                this, R.layout.checkbox_item,
                tutorialCursor, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
     
        setListAdapter(adapter);
        
    }
	
	public void onBackClick(View v){
		finish();
	}

	public void adjustProgress(int adjustment){
		int current = mPbar.getProgress() + adjustment;
		int max = mPbar.getMax();

		mPbar.setProgress(current);


		View btnBack = findViewById(R.id.buttonBack);
		if(current == max){
			btnBack.setVisibility(View.VISIBLE);
		} else {
			btnBack.setVisibility(View.GONE);
		}      	
	}
}
