package com.dnd.privacyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ProceduresViewActivity extends FragmentActivity{
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.proceduresview);
	        
	        Intent i =getIntent(); //
			long id = i.getLongExtra("ID", -1);
			String title = i.getStringExtra("Title");

	        ProceduresViewFragment viewer = (ProceduresViewFragment) getSupportFragmentManager()
	                .findFragmentById(R.id.proceduresview_fragment);
	         
	        viewer.setupProcedure(id, title);
	        
	    }
	 
	 public void onBackClick(View v){
		 finish();
	 }
}
