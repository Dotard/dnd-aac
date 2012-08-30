package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void openReferences(View view)  
    {  
        Intent myIntent = new Intent(this, ReferenceListActivity.class );
        startActivity(myIntent);
    }
//    public void openProcedures(View view)  
//    {  
//        Intent myIntent = new Intent(this, ProceduresActivity.class );
//        startActivity(myIntent);
//    }     
    public void openQuizzes(View view)  
    {  
//        Intent myIntent = new Intent(this, QuestionListActivity.class );
//        myIntent.putExtra("chapterID", 0);
//        myIntent.putExtra("parentID", 0);
//        startActivity(myIntent);
    	
    	Intent myIntent = new Intent(this, QuizActivity.class );
        myIntent.putExtra("chapterID", 0);
        myIntent.putExtra("parentID", 0);
        startActivity(myIntent);
        
    }     
}
