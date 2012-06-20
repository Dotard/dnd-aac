package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class QuestionListActivity extends FragmentActivity implements
	QuestionListFragment.OnSectionSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list_fragment);
    }

    @Override
    public void onSectionSelected(long id) {
    	
    	QuestionViewFragment viewer = (QuestionViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_view_fragment);
    	
    	Log.d("Log", "Section selected " + id);

        if (viewer == null || !viewer.isInLayout()) {
            Intent showContent = new Intent(getApplicationContext(),
                    QuestionViewActivity.class);
            showContent.putExtra("SectionID", id);
            startActivity(showContent);
        } else {
        	
            viewer.loadSection(id);
        }
    }
    
    public void onButtonClick(View v)
    {
    	QuestionViewFragment viewer = (QuestionViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.question_view_fragment);
    	viewer.onButtonClick(v);
    }
}