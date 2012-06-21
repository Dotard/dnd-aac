package com.dnd.privacyapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.widgets.SaundProgressBar;

public class ProceduresViewFragment extends ListFragment{
	private ProcedureAdapter adapter;
	private SaundProgressBar mPbar;
	private long id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	 		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.proceduresview_fragment, container,true);
				
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	private void bindListData()
    {	
    	String projection[] = {"procedureItemsID as _id","procedureItemsText" };
    	String sortOrder = "procedureItemsOrder ASC";
        Cursor tutorialCursor = getActivity().getContentResolver().query(
        		Uri.withAppendedPath(PrivacyAppProvider.PROCEDUREITEMS_URI,String.valueOf(id)), projection, null, null, sortOrder);
        
        String[] uiBindFrom = { "procedureItemsText" }; //Add as many columns using ,
        int[] uiBindTo = { R.id.checkbox }; //And you can bind it to as many variables using ,
       
        mPbar.setMax(tutorialCursor.getCount());
        
        adapter = new ProcedureAdapter(
        		getActivity(), R.layout.checkbox_item,
                tutorialCursor, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,this);
     
        setListAdapter(adapter);        
    }
	
	public void setupProcedure(long id, String title){
		this.id = id;
			
		//Set up progress bar
		mPbar = (SaundProgressBar) getView().findViewById(R.id.regularprogressbar);
	    
	    Drawable indicator = getResources().getDrawable(R.drawable.progress_indicator);
	    Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 5, indicator.getIntrinsicHeight());
	    
	    indicator.setBounds(bounds);
	    
	    mPbar.setProgressIndicator(indicator);
	    mPbar.setProgress(0);
	    mPbar.setVisibility(View.VISIBLE);
	    
		TextView tv = (TextView)getView().findViewById(R.id.checkHeader);
		tv.setText(title);
		
		bindListData();
	}
	

	public void adjustProgress(int adjustment){
		int current = mPbar.getProgress() + adjustment;
		int max = mPbar.getMax();

		mPbar.setProgress(current);


		View btnBack = getView().findViewById(R.id.buttonBack);
		if(current == max && this.getActivity() instanceof ProceduresViewActivity){
			btnBack.setVisibility(View.VISIBLE);
		} else {
			btnBack.setVisibility(View.GONE);
		}      	
	}
}
