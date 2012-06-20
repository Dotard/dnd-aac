/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.dnd.privacyapp;

import com.dnd.privacyapp.R;
import com.dnd.privacyapp.data.PrivacyAppProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionViewFragment extends Fragment {
    private RelativeLayout viewer = null;
    
    private int qIndex;
	private int answer;
	private long sectionID;
	private Cursor questionCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        viewer = (RelativeLayout) inflater
                .inflate(R.layout.question, container, false);
        viewer.setBackgroundColor(0x00000000);
        
        qIndex = 0;
        createQuestionSet();
        loadQuestion(qIndex);
                
        return viewer;
    }

    public void loadSection(long id) {
        if (viewer != null) {
        	sectionID = id;
        	 qIndex = 0;
        	createQuestionSet();
        	loadQuestion( qIndex);
        }
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
	    		viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
	    		viewer.findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
	    		viewer.findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		break;
    	case R.id.buttonPrev:
    		if(qIndex > 0)
    		{
    			qIndex--;
    			loadQuestion(qIndex);
    			viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
    			viewer.findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
    			viewer.findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		
    		break;
    	case R.id.buttonCont:
    		viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
    		viewer.findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
    		viewer.findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		
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
        		viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
        		viewer.findViewById(R.id.buttonCheck).setVisibility(View.VISIBLE);
        		viewer.findViewById(R.id.buttonEnd).setVisibility(View.GONE);
    		}
    		
    		break;
    	case R.id.buttonEnd:
    		
    		QuestionListFragment listfragment = (QuestionListFragment) getActivity().getSupportFragmentManager()
            .findFragmentById(R.id.question_list_fragment);

		    if (listfragment == null || !listfragment.isInLayout()) {
		        getActivity().finish();
		    } else {
		    	viewer.setVisibility(View.INVISIBLE);
		    }
    		break;
    	}
    	
	}
    
    private void createQuestionSet()
    {
    	String projection[] = {"questionID as _id","questionDesc","questionNumber","option1","option2","option3","option4","option5","correctOption" };
        questionCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(PrivacyAppProvider.QUESTIONS_URI,String.valueOf(sectionID)), projection, null, null, null);
    }
    
    private void loadQuestion(int i)
    {	 	
    	if(questionCursor.getCount() == 0)
    	{
    		Toast.makeText(getActivity(), "No questions found", Toast.LENGTH_SHORT).show();
    		QuestionListFragment listfragment = (QuestionListFragment) getActivity().getSupportFragmentManager()
    	            .findFragmentById(R.id.question_list_fragment);

    			    if (listfragment == null || !listfragment.isInLayout()) {
    			        //getActivity().finish();
    			    } else {
    			    	viewer.setVisibility(View.INVISIBLE);
    			    }
    		return;
    	}
    	
    	viewer.setVisibility(View.VISIBLE);
    	
    	questionCursor.moveToPosition(qIndex);
    	
    	RadioGroup rg = (RadioGroup) viewer.findViewById(R.id.radiogroup);
    	TextView tv = (TextView) viewer.findViewById(R.id.tv1);
    	TextView tv2 = (TextView) viewer.findViewById(R.id.tvPage);
    	rg.removeAllViews();
    	
    	tv.setText(questionCursor.getString(1));
    	tv2.setText(String.format("%d of %d", qIndex+1, questionCursor.getCount()));
    	
    
    	for(int j = 0; j < 5; j++)
    	{
	    	RadioButton button1 = new RadioButton(getActivity());
	    	button1.setText(questionCursor.getString(3 + j));
	    	rg.addView(button1);
    	}
    	
    	answer = questionCursor.getInt(8);
    }
    
    private void checkAnswer()
    {
    	RadioGroup radioGroup = (RadioGroup) viewer.findViewById(R.id.radiogroup);
    	
    	int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
       	
    	int indx = radioGroup.indexOfChild(radioGroup.findViewById(checkedRadioButton));
    	
    	
    	if(indx == -1) return;
    	
 
    	if(indx + 1 == answer)
    	{
    		Toast.makeText(getActivity(), "Yousa right", Toast.LENGTH_SHORT)
			.show();

    		
    		ContentValues values = new ContentValues();
    		values.put("selectedOption", indx + 1);  		
    		getActivity().getContentResolver().update(Uri.withAppendedPath(PrivacyAppProvider.QUESTIONS_URI,String.valueOf(questionCursor.getInt(0))), values, null, null);
    		
    		if(qIndex == questionCursor.getCount() - 1)
    		{
    			viewer.findViewById(R.id.buttonEnd).setVisibility(View.VISIBLE);
    			viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
    			viewer.findViewById(R.id.buttonCheck).setVisibility(View.GONE);
    		}
    		else
    		{
    			viewer.findViewById(R.id.buttonCont).setVisibility(View.VISIBLE);
    			viewer.findViewById(R.id.buttonCheck).setVisibility(View.GONE);
    		}
    	}
    	else {
			Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
		}
    }
}
