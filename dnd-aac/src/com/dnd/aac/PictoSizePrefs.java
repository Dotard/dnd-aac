package com.dnd.aac;

import java.util.ArrayList;

import com.dnd.aac.util.MyPreferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class PictoSizePrefs extends ListPreference {
	
	public PictoSizePrefs(Context context, AttributeSet attrs){
		super(context,attrs);

		ArrayList<Integer> prefs = MyPreferences.getPictoSizePrefs(context);
		String[] sizeNames = MyPreferences.getPictoSizeNames(context);
		
		CharSequence[] entries = new CharSequence[sizeNames.length];
		CharSequence[] entryValues = new CharSequence[prefs.size()-1];
		
		for(int i = 1; i < prefs.size(); i++){
			entries[i - 1] = prefs.get(i).toString();
			entryValues[i - 1] = prefs.get(i).toString();
		}
		
		setEntries(sizeNames);
		setEntryValues(entryValues);
		
		setDefaultValue(prefs.get(0).toString()); //always set default programatically	
	}
	
	public PictoSizePrefs(Context context) {
		this(context, null);
	}

}
