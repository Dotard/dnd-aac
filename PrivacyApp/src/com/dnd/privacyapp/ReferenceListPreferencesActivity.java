package com.dnd.privacyapp;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.data.PrivacyAppSharedPrefs;
import com.dnd.privacyapp.receiver.AlarmReceiver;
import com.dnd.privacyapp.R;

public class ReferenceListPreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(
                PrivacyAppSharedPrefs.PREFS_NAME);
        addPreferencesFromResource(R.xml.prefs);
        
        setTheme(R.style.DarkText);

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                	ContentValues sectionValues = new ContentValues();
                	ContentValues quizValues = new ContentValues();
                	
                	sectionValues.put("secQuizComplete",false);
                	quizValues.put("quizComplete",false);

                	int updateCount = getContentResolver().update(PrivacyAppProvider.SECTION_URI
            				, sectionValues, null, null);   
                	
                	updateCount = getContentResolver().update(PrivacyAppProvider.QUIZ_URI
            				, quizValues, null, null);   
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        
        //
        Preference button = (Preference)findPreference("button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        	@Override
        	public boolean onPreferenceClick(Preference arg0){
        		builder.setMessage("This will delete all your quiz answers. Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        		return true;
        	}
        });
    }
}
