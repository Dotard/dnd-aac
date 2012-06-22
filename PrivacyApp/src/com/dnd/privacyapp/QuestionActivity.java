package com.dnd.privacyapp;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	
	private int qIndex;
	private int answer;
	private long sectionID;
	private Cursor questionCursor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        sectionID = getIntent().getLongExtra("sectionID", 0);
        
        qIndex = 0;
        createQuestionSet();
        loadQuestion(qIndex);
    }
    
    public void onButtonClick(View v) {
    	int total = questionCursor.getCount();
    	switch(v.getId()){
    	case R.id.buttonCheck:
    		checkAnswer();
    		break;
    	case R.id.buttonFirst:
    		if(qIndex != 0)
    		{
    			qIndex = 0;
	    		loadQuestion(qIndex);
	    		findViewById(R.id.buttonCont).setVisibility(View.GONE);
	    		findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
	    		findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		break;
    	case R.id.buttonPrev:
    		if(qIndex > 0)
    		{
    			qIndex--;
    			loadQuestion(qIndex);
    			findViewById(R.id.buttonCont).setVisibility(View.GONE);
        		findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
        		findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		
    		break;
    	case R.id.buttonCont:
    		findViewById(R.id.buttonCont).setVisibility(View.GONE);
    		findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
    		findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		
    	case R.id.buttonNext:
    		if(qIndex < total - 1)
    		{
    			qIndex++;
    			loadQuestion(qIndex);
    		}

    		break;
    	case R.id.buttonLast:
    		if(qIndex != total -1)
    		{
    			qIndex = total - 1;
        		loadQuestion(qIndex);
        		findViewById(R.id.buttonCont).setVisibility(View.GONE);
        		findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
        		findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		
    		break;
    	case R.id.buttonEnd:
    		finish();
    		break;
    	}
    	
	}
    
    private void createQuestionSet()
    {
    	String projection[] = {"questionID as _id","questionDesc","questionNumber","option1","option2","option3","option4","option5","correctOption" };
        questionCursor = this.getContentResolver().query(
                Uri.withAppendedPath(PrivacyAppProvider.QUESTIONS_URI,String.valueOf(sectionID)), projection, null, null, null);
    }
    
    private void loadQuestion(int i)
    {	 	
    	if(questionCursor.getCount() == 0)
    	{
    		Toast.makeText(this, "No questions found", Toast.LENGTH_SHORT).show();
   			finish();
    		return;
    	}
    	
    	questionCursor.moveToPosition(qIndex);
    	
    	
    	//**TEMPORARY** FIX REMOVE LATER
    	RadioGroup rg = new RadioGroup(this);//(RadioGroup) findViewById(R.id.radiogroup);
    	TextView tv = (TextView) findViewById(R.id.tvQuestion);
    	TextView tv2 = (TextView) findViewById(R.id.tvPage);
    	rg.removeAllViews();
    	
    	tv.setText(questionCursor.getString(1));
    	tv2.setText(String.format("%d of %d", qIndex+1, questionCursor.getCount()));
    	
    
    	for(int j = 0; j < 5; j++)
    	{
	    	RadioButton button1 = new RadioButton(this);
	    	button1.setText(questionCursor.getString(3 + j));
	    	rg.addView(button1);
    	}
    	
    	answer = questionCursor.getInt(8);
    }
    
    private void checkAnswer()
    {
    	//**TEMPORARY FIX REMOVE LATER
    	RadioGroup radioGroup = new RadioGroup(this);//(RadioGroup)findViewById(R.id.radiogroup);
    	
    	int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
       	
    	int indx = radioGroup.indexOfChild(radioGroup.findViewById(checkedRadioButton));
    	
    	
    	if(indx == -1) return;
    	
 
    	if(indx + 1 == answer)
    	{
    		Toast.makeText(this, "Yousa right", Toast.LENGTH_SHORT)
			.show();

    		
    		ContentValues values = new ContentValues();
    		values.put("selectedOption", indx + 1);  		
    		getContentResolver().update(Uri.withAppendedPath(PrivacyAppProvider.QUESTIONS_URI,String.valueOf(questionCursor.getInt(0))), values, null, null);
    		
    		if(qIndex == questionCursor.getCount() - 1)
    		{
    			findViewById(R.id.buttonEnd).setVisibility(View.VISIBLE);
    			findViewById(R.id.buttonCont).setVisibility(View.GONE);
        		findViewById(R.id.buttonCheck).setVisibility(View.GONE);
    		}
    		else
    		{
    			findViewById(R.id.buttonCont).setVisibility(View.VISIBLE);
        		findViewById(R.id.buttonCheck).setVisibility(View.GONE);
    		}
    	}
    	else {
			Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
		}
    }
    

}