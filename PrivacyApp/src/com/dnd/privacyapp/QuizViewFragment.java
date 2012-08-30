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

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuizViewFragment extends Fragment implements OnClickListener{
    private RelativeLayout viewer = null;
    
    private int qIndex;
	private int answer;
	private long quizID;
	private long sectionID;
	private Cursor questionCursor;
	private TextView label;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        viewer = (RelativeLayout) inflater
                .inflate(R.layout.quizquestion, container, false);
        viewer.setBackgroundColor(0x00000000);
        
        qIndex = 0;

        label = (TextView)viewer.findViewById(R.id.label1);
        
        viewer.setVisibility(View.INVISIBLE);
        
        return viewer;
    }

    public void loadSection(long id) {
        if (viewer != null) {
        	sectionID = id;
        	 qIndex = 0;
        	createQuestionSet();
        	loadQuestion( qIndex);
        	resetLayout();
        }
    }
        
    public void onButtonClick(View v) {
    	int total = questionCursor.getCount();
    	switch(v.getId()){
    	case R.id.buttonFirst:
    		if(qIndex != 0) {
    			qIndex = 0;
	    		loadQuestion(qIndex);
	    		resetLayout();
    		}
    		break;
    	case R.id.buttonPrev:
    		if(qIndex > 0)
    		{
    			qIndex--;
    			loadQuestion(qIndex);
    			resetLayout();
    		}
    		
    		break;
    	case R.id.buttonCont:	
    	case R.id.buttonNext:
    		if(qIndex < total - 1)
    		{
    			qIndex++;
    			loadQuestion(qIndex);
    			resetLayout();
    		}

    		break;
    	case R.id.buttonLast:
    		if(qIndex != total -1)
    		{
    			qIndex = total - 1;
        		loadQuestion(qIndex);
        		resetLayout();
    		}
    		
    		break;
    	case R.id.buttonEnd:
    		
    		QuizListFragment listfragment = (QuizListFragment) getActivity().getSupportFragmentManager()
            .findFragmentById(R.id.quizlistfragment);

		    if (listfragment == null || !listfragment.isInLayout()) {
		        getActivity().finish();
		    } else {
		    	viewer.setVisibility(View.INVISIBLE);
		    }
		    v.setVisibility(View.GONE);
    		break;
    	}
    	
	}
    
    private void createQuestionSet()
    {
    	String projection[] = {"quizID as _id","quizText","quizOption1","quizOption2","quizOption3","quizOption4","quizOption5","quizCorrectOption","quizComplete" };
        questionCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(PrivacyAppProvider.QUIZ_URI,"SEC/" + String.valueOf(sectionID)), projection, null, null, null);
    }
    
    private void loadQuestion(int i)
    {	 	
    	if(questionCursor.getCount() == 0)
    	{
    		Toast.makeText(getActivity(), "No questions found", Toast.LENGTH_SHORT).show();
    		QuizListFragment listfragment = (QuizListFragment) getActivity().getSupportFragmentManager()
    	            .findFragmentById(R.id.quizlistfragment);

    			    if (listfragment == null || !listfragment.isInLayout()) {
    			        if(getActivity() instanceof QuizViewActivity){
    			        	getActivity().finish();
    			        }
    			    } else {
    			    	viewer.setVisibility(View.INVISIBLE);
    			    }
    		return;
    	}
    	
    	viewer.setVisibility(View.VISIBLE);
    	
    	questionCursor.moveToPosition(qIndex);
    	
    	quizID = questionCursor.getLong(questionCursor.getColumnIndexOrThrow("_id"));
    			
    	TextView tv = (TextView) viewer.findViewById(R.id.tvQuestion);
    	TextView tv2 = (TextView) viewer.findViewById(R.id.tvPage);
    	
    	int textIndex = questionCursor.getColumnIndexOrThrow("quizText");
    	tv.setText(questionCursor.getString(textIndex));
    	tv2.setText(String.format("%d of %d", qIndex+1, questionCursor.getCount()));
    	
    	int ansIndex = questionCursor.getColumnIndexOrThrow("quizCorrectOption");
    	answer = questionCursor.getInt(ansIndex);
    	
    	int quizComplete = questionCursor.getInt(questionCursor.getColumnIndexOrThrow("quizComplete"));
    	
    	LinearLayout questionlayout = (LinearLayout) viewer.findViewById(R.id.optionsLayout);
    	questionlayout.setEnabled(true);
    	questionlayout.removeAllViews();
    	
    	int firstOption = questionCursor.getColumnIndexOrThrow("quizOption1");
    	for(int j = 0; j < 5; j++)
    	{
	    	Button button1 = new Button(getActivity());
	    	button1.setText(questionCursor.getString(firstOption + j));
	    	button1.setOnClickListener(this);
	    	button1.setTag(j+1);
	    	questionlayout.addView(button1);
    	}
    	
    	if(quizComplete == 1){
    		disableQuestions();
    		View v = questionlayout.findViewWithTag(answer);
    		v.setBackgroundResource(R.drawable.btn_quiz_question_correct);
    	}
    }
    
	@Override
	public void onClick(View v) {
		int index = (Integer)(v.getTag());
		
		if(index == answer){
			v.setBackgroundResource(R.drawable.btn_quiz_question_correct);
			label.setBackgroundColor(Color.GREEN);
			label.setTextColor(Color.WHITE);
			
			disableQuestions();
			
			//Mark Quiz complete in database
			ContentValues quizValues = new ContentValues();
			quizValues.put("quizComplete", true);
			
			int updated = getActivity().getContentResolver().update(Uri.withAppendedPath(PrivacyAppProvider.QUIZ_URI, String.valueOf(quizID))
					, quizValues, null, null);

			if(updated == 1) Toast.makeText(getActivity(), "1 Row updated",Toast.LENGTH_SHORT).show();
			
			//Check if all questions in Quiz completed
			String projection[] = {"quizID as _id"};
			String selectionArgs[] = {"0"};
			Cursor c = getActivity().getContentResolver().query(Uri.withAppendedPath(PrivacyAppProvider.QUIZ_URI,"SEC/" + String.valueOf(sectionID)),
					projection, "quizComplete=?", selectionArgs, null);
			if(c.getCount() == 0){ 
				Toast.makeText(getActivity(), "All questions in quiz completed",Toast.LENGTH_SHORT).show();
				//Mark section complete in database
				ContentValues sectionValues = new ContentValues();
				sectionValues.put("secQuizComplete", true);
				
				int updateCount = getActivity().getContentResolver().update(Uri.withAppendedPath(PrivacyAppProvider.SECTION_URI, String.valueOf(sectionID))
						, sectionValues, null, null);

				if(updateCount == 1) Toast.makeText(getActivity(), "Section complete!",Toast.LENGTH_SHORT).show();
			}
			
//			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//			builder.setMessage("You finished all quizes! Would you like to go to certificate page?")
//			       .setCancelable(false)
//			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   Intent myIntent = new Intent(getActivity(), CertificateActivity.class );
//			               startActivity(myIntent);
//			           }
//			       })
//			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			                dialog.cancel();
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			getActivity().showDialog(id);
			
			FragmentManager fm = this.getFragmentManager();
			ViewCertificateDialog vcd = new ViewCertificateDialog();
			vcd.show(fm, "fragment");
			
			//Check if every Quiz is complete in app
			projection = new String[]{"secID"};
			selectionArgs = new String[]{"0"};
			c = getActivity().getContentResolver().query(PrivacyAppProvider.SECTION_URI,
					projection, "secQuizComplete=?", selectionArgs, null);
			if(c.getCount() == 0){ 
				Toast.makeText(getActivity(), "You completed every single quiz!",Toast.LENGTH_SHORT).show();
				
				
			}
			
			if(qIndex == questionCursor.getCount() - 1)
    		{
    			viewer.findViewById(R.id.buttonEnd).setVisibility(View.VISIBLE);
    			viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
    			label.setText("Thats correct! No more questions");
    		}
    		else
    		{
    			viewer.findViewById(R.id.buttonCont).setVisibility(View.VISIBLE);
    			label.setText("Thats correct! Press continue");
    		}
		}
		else
		{
			v.setBackgroundResource(R.drawable.btn_quiz_question_incorrect);
			label.setBackgroundColor(Color.RED);
			label.setTextColor(Color.WHITE);
			label.setText("Oops, that was not the correct answer");
			
		}
	}
	
	private void disableQuestions(){
		LinearLayout questionlayout = (LinearLayout) viewer.findViewById(R.id.optionsLayout);
		for(int i = 0; i < questionlayout.getChildCount(); i++){
			questionlayout.getChildAt(i).setEnabled(false);
		}
	}
	
	private void resetLayout(){
		viewer.findViewById(R.id.buttonCont).setVisibility(View.GONE);
		viewer.findViewById(R.id.buttonEnd).setVisibility(View.GONE);
		label.setText("");		
		label.setBackgroundColor(getResources().getColor(android.R.color.transparent));
	}
}
