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
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class QuestionListActivity extends ListActivity {

	private SimpleCursorAdapter adapter;
	private long chapterID;
	private long parentID;
	
	  /** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist);
        
     
        Bundle b = getIntent().getExtras();
        chapterID =  b.getLong("chapterID");
        parentID =  b.getLong("parentID");
        if (chapterID == 0){
        	queryChapters();
        } 
        else {
        	Log.d("putExtra", "chapterID = "+ chapterID + ", parentID = " + parentID);
        	querySections(chapterID,parentID);
        }
       
    }


    
    private void queryChapters()
    {
    	String projection[] = {"chapterID as _id","chapterName","chapterDesc" };
        Cursor tutorialCursor = this.getContentResolver().query(
                PrivacyAppProvider.CHAPTERS_URI, projection, null, null, null);
        
        String[] uiBindFrom = { "chapterName", "chapterDesc" };
        int[] uiBindTo = { R.id.title, R.id.desc }; 

        adapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                tutorialCursor, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        
        setListAdapter(adapter);
        
    }
    
    private void querySections(long chapterID, long parentID)
    {
    	String projection[] = {"sectionID as _id","sectionName","sectionDesc"};
    	Uri uri = PrivacyAppProvider.SECTIONS_URI;
    			if (parentID > 0) { uri = Uri.withAppendedPath(uri, String.valueOf(parentID)); };
        Cursor tutorialCursor = this.getContentResolver().query(
        		uri, projection, "chapterID = " + chapterID, null, null);
        
        if (tutorialCursor.getCount() > 0) {
        
        String[] uiBindFrom = { "sectionName", "sectionDesc" }; 
        int[] uiBindTo = { R.id.title, R.id.desc }; 
        adapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                tutorialCursor, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
        }
        else //No subsections found so look for quizzes.
        {
//        	String projection2[] = {"questionID as _id", "sectionID"};
//        	Uri uri2 = TutListProvider.QUESTIONS_URI;
//            Cursor tutorialCursor2 = this.getContentResolver().query(
//            		uri2, projection2, "sectionID = " + parentID, null, null);
            
//            if (tutorialCursor2.getCount() > 0) {
        	//If we found questions display them.    
            	//setContentView(R.layout.question);
        	
            	Intent newActivity = new Intent(this, QuestionActivity.class);
            	newActivity.putExtra("sectionID", parentID);
            	startActivity(newActivity);
            	finish();
//        	}

        }
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
    	super.onListItemClick(l, v, position, id);  	
      	//Intent newActivity = new Intent(this, QuestionActivity.class);
    	Intent newActivity = new Intent(this, QuestionListActivity.class);
    	if (chapterID == 0){
    	newActivity.putExtra("chapterID", id);
    	newActivity.putExtra("parentID", 0);
    	} else {
    	newActivity.putExtra("chapterID", chapterID);
    	newActivity.putExtra("parentID", id);
    	}
    	startActivity(newActivity);
    }
}
