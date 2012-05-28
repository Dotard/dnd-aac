package com.dnd.aac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.devsmart.android.ui.HorizontalListView;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

public class MainActivity extends SherlockFragmentActivity implements
		OnItemClickListener, OnInitListener,
		TextToSpeech.OnUtteranceCompletedListener {
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech myTTS;
	private HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	private GestureDetector mGestureDetector;
	private Handler mHandler;
	private ArrayList<Picto> arrayOfPictos = new ArrayList<Picto>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

		// On Speech finish handler
		mHandler = new Handler();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "test");
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// pictoView Setup
		HorizontalListView pictoView = (HorizontalListView) findViewById(R.id.listview);
		pictoView.setOnItemClickListener(pictoClicked);
		pictoView.setAdapter(editPictoAdapter);

		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs",
				MODE_WORLD_READABLE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		// prefsEditor.putString(MY_NAME, "Sai");
		// prefsEditor.putString(MY_WALLPAPER, "f664.PNG");
		// prefsEditor.commit();

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case 0:
	            Log.d("ActionBar", "Settings button clicked");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public boolean onCreateOptionsMenu(Menu menu) {


        menu.add(Menu.NONE, 0 ,Menu.NONE, R.string.settingsmenustring)
            .setIcon(android.R.drawable.ic_menu_agenda)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        /*menu.add("Search")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("Refresh")
            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
*/
        return true;
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
	}
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				myTTS = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS) {
			myTTS.setLanguage(Locale.US);
			myTTS.setOnUtteranceCompletedListener(this);
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(this, "Sorry! Text To Speech failed...",
					Toast.LENGTH_LONG).show();
		}

	}

	public void expandAll(View view) {
		ListFragment listfragment = (ListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.listFragment);
		listfragment.expandAll();
	}

	public void collapseAll(View view) {
		ListFragment listfragment = (ListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.listFragment);
		listfragment.collapseAll();
	}
	
	
	public void saySomething(View view) {
		Button speakBtn = (Button) findViewById(R.id.button1);
		if (!myTTS.isSpeaking()) {

			String words = "";
			for (int i = 0; i < arrayOfPictos.size(); i++) {
				words += arrayOfPictos.get(i).getText() + " ";
			}

			speakBtn.setBackgroundResource(R.drawable.speech_cancel);
			myTTS.speak(words, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
		} else {
			speakBtn.setBackgroundResource(R.drawable.statelist_speechbtn);
			myTTS.stop();
		}

	}

	public void onUtteranceCompleted(String uttId) {
		if (uttId.equalsIgnoreCase("test")) {
			// Create runnable for posting
			Runnable mUpdateResults = new Runnable() {
				public void run() {
					Button speakBtn = (Button) findViewById(R.id.button1);
					speakBtn.setBackgroundResource(R.drawable.statelist_speechbtn);
				}
			};
			mHandler.post(mUpdateResults);
		}
	}
	
	

	public void addPicto(Picto picto) {
		arrayOfPictos.add(picto);
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();

	}

	public void deletePictos(View view) {
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		arrayOfPictos.clear();
		((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
		ImageView deleteBtn = (ImageView) findViewById(R.id.imageView1);
		deleteBtn.setVisibility(View.INVISIBLE);
	}
	
	private OnItemClickListener pictoClicked = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			arrayOfPictos.remove(position);
			((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
		}
	};

	private BaseAdapter editPictoAdapter = new BaseAdapter() {

		@Override
		public void notifyDataSetChanged() {
			if (getCount() > 0) {
				ImageView image = (ImageView) findViewById(R.id.imageView1);
				image.setVisibility(View.VISIBLE);
				findViewById(R.id.emptyTextView).setVisibility(View.INVISIBLE);
			} else {
				ImageView image = (ImageView) findViewById(R.id.imageView1);
				image.setVisibility(View.INVISIBLE);
				findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
			}
			super.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return arrayOfPictos.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayOfPictos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.editpicto, null);
			TextView title = (TextView) retval.findViewById(R.id.text);
			ImageView image = (ImageView) retval.findViewById(R.id.image);
			LinearLayout ll = (LinearLayout) retval.findViewById(R.id.picto);
			title.setText(arrayOfPictos.get(position).getText());
			// Drawable resImg =
			// getApplicationContext().getResources().getDrawable(arrayOfPictos.get(position).getDrawableId(getApplicationContext()));
			image.setImageBitmap(arrayOfPictos.get(position).getBitmap());

			// image.setOnClickListener((OnClickListener) mOnButtonClicked);
			// ll.setOnClickListener((OnClickListener) mOnButtonClicked);

			return retval;
		}

	};

	@Override
	protected void onDestroy() {

		// Close the Text to Speech Library
		if (myTTS != null) {

			myTTS.stop();
			myTTS.shutdown();
		}
		super.onDestroy();
	}

}