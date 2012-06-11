package com.dnd.aac;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class GeneralPreferenceFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		int res = getActivity().getResources()
				.getIdentifier(getArguments().getString("resource"), "xml", getActivity().getPackageName());
		
		addPreferencesFromResource(res);
		
		ListPreference lp = (ListPreference)findPreference(getString(R.string.pref_pictosize));
		if(lp != null){			
			lp.setValue(String.valueOf(
					MyPreferences.getPictoSize(getActivity(), getActivity().getString(R.string.pref_pictosize)))
					);
		}
	}	
	
}
