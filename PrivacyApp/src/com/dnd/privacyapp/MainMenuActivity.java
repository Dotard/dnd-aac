package com.dnd.privacyapp;

import com.dnd.privacyapp.R;
import com.dnd.privacyapp.data.PrivacyAppProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	String projection[] = {"secID"};
		String selectionArgs[] = {"0"};
		Cursor c = this.getContentResolver().query(PrivacyAppProvider.SECTION_URI,
				projection, "secQuizComplete=?", selectionArgs, null);
		if(c.getCount() > 0){ 
			Button b = (Button)this.findViewById(R.id.btnCertificate);
			b.setVisibility(View.GONE);
			
		}
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
    
    public void openCertificate(View view)
    {
    	Intent myIntent = new Intent(this, CertificateActivity.class );
        startActivity(myIntent);
    }
}
