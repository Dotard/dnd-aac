package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void openReferences(View view)  
    {  
        Toast.makeText(this, "Button 1 clicked!", 500).show();
        Intent myIntent = new Intent(this, ReferenceListActivity.class );
        startActivity(myIntent);
    }
    public void openProcedures(View view)  
    {  
        Toast.makeText(this, "Button 2 clicked!", 200).show();  
        Intent myIntent = new Intent(this, ProcedureListActivity.class );
        startActivity(myIntent);
    }     
    public void openQuizzes(View view)  
    {  
        Toast.makeText(this, "Button 3 clicked!", 100).show(); 
        Intent myIntent = new Intent(this, QuestionListActivity.class );
        myIntent.putExtra("chapterID", 0);
        myIntent.putExtra("parentID", 0);
        startActivity(myIntent);
    }     
}
