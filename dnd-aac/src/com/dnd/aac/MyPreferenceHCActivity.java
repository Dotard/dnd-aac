package com.dnd.aac;

import java.util.List;

import android.preference.PreferenceActivity;

public class MyPreferenceHCActivity extends PreferenceActivity{
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers,target);
	}

}
