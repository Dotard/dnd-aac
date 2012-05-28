package com.dnd.aac;

import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Need to check if Activity has been switched to landscape mode
		// If yes, finished and go back to the start Activity
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		setContentView(R.layout.details_activity_layout);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String s = extras.getString("value");
		}
	}
}

