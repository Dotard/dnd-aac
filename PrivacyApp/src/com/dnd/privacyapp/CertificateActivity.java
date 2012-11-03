package com.dnd.privacyapp;

import com.dnd.privacyapp.R;
import com.dnd.privacyapp.data.PrivacyAppSharedPrefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class CertificateActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate);
        


    		
    		SharedPreferences sp = getSharedPreferences(PrivacyAppSharedPrefs.PREFS_NAME, Context.MODE_PRIVATE);
    		String date = sp.getString("QuizCompleteDate", null);
    		String html;
 
    		if(date != null){
    	 html = "PbD Tutorial <br />" +
				"<b>Certificate of Completion</b><br />" +
				date +
				" <br />" +
				"<a href=\"http://www.PrivacyByDesign.ca\">www.PrivacyByDesign.ca</a>";}
    		else{
    			 html = "PbD Tutorial <br />" +
    					"<b>Certificate of Completion</b><br />" +
    					"<a href=\"http://www.PrivacyByDesign.ca\">www.PrivacyByDesign.ca</a>";
    		}
    	
        TextView tv = (TextView)this.findViewById(R.id.mainText);
        tv.setText(Html.fromHtml(html
        		));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
