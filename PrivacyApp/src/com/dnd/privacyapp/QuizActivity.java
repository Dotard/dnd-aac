package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class QuizActivity extends FragmentActivity implements
	QuizListFragment.OnSectSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        
        Intent i = getIntent(); //
		long id = i.getLongExtra("sectionID", -1);
		if(id > -1){
			onSectSelected(id);
		}        
    }

    @Override
    public void onSectSelected(long id) {
    	
    	QuizViewFragment viewer = (QuizViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.quizviewfragment);
    	
    	Log.d("Log", "Section selected " + id);

        if (viewer == null || !viewer.isInLayout()) {
            Intent showContent = new Intent(getApplicationContext(),
                    QuizViewActivity.class);
            showContent.putExtra("SectionID", id);
            startActivity(showContent);
        } else {
        	
            viewer.loadSection(id);
        }
    }
    
    public void onButtonClick(View v)
    {
    	QuizViewFragment viewer = (QuizViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.quizviewfragment);
    	viewer.onButtonClick(v);
    }
}