package com.dnd.privacyapp;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ProceduresActivity extends FragmentActivity implements
ProceduresListFragment.OnProcSelectedListener {

	
	
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procedures);
        
        
    }
    
    @Override
    public void onProcedureSelected(long id, CharSequence title){
    	ProceduresViewFragment viewer = (ProceduresViewFragment) getSupportFragmentManager()
    			.findFragmentById(R.id.proceduresview_fragment);

    	if (viewer == null || !viewer.isInLayout()) {
    		Intent newActivity = new Intent(this, ProceduresViewActivity.class);
    		newActivity.putExtra("ID", id);
    		newActivity.putExtra("Title", title);
    		startActivity(newActivity);
    	} else {
    		viewer.setupProcedure(id, title.toString());
    	}
    }
/*
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.proceduremenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if(item.getItemId() == R.id.exit)
    	{
    		this.finish();
    		return true;
    	}
    	if(item.getItemId() == R.id.add)
    	{
    		  //Preparing views
    	    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    	    View layout = inflater.inflate(R.layout.addproceduredialog, (ViewGroup) findViewById(R.id.layout_root));
    	//layout_root should be the name of the "top-level" layout node in the dialog_layout.xml file.
    	    final EditText nameBox = (EditText) layout.findViewById(R.id.proc_name);
    	    final EditText descBox = (EditText) layout.findViewById(R.id.proc_desc);
    	    
    	    
    	    //Building dialog
    	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	    builder.setView(layout);
    	    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	            dialog.dismiss();
    	            //save info where you want it
    	            Editable name = nameBox.getText();
    	            Editable desc = descBox.getText();
    	            
    	            ContentValues procData = new ContentValues();
    	    		procData.put("procedureName", name.toString());
    	    		procData.put("procedureDesc", desc.toString());
    	  
    	        	 getContentResolver().insert(
    	                     PrivacyAppProvider.PROCEDURES_URI,
    	                     procData);
    	        	 //tutorialCursor.requery();
    	        }
    	    });
    	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	            dialog.dismiss();
    	        }
    	    });
    	    AlertDialog dialog = builder.create();
    	    dialog.show();
        	 
    	}
    	return false;
    }*/
}
