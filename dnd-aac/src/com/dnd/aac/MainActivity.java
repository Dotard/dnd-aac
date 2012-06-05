package com.dnd.aac;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.devsmart.android.ui.HorizontalListView;
import com.dnd.aac.data.aacProvider;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;

import android.R.menu;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
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
import android.view.View.OnClickListener;
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
	static public ZipResourceFile mExpansionFile;
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech myTTS;
	private HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	private GestureDetector mGestureDetector;
	private Handler mHandler;
	private ArrayList<Picto> arrayOfPictos = new ArrayList<Picto>();
	private int SUGGESTION_BOX_START_X = 20; 

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
		
		
        try {
			mExpansionFile = APKExpansionSupport.getAPKExpansionZipFile(this,
			        1, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

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
	MenuInflater menuInflater = getSupportMenuInflater();
        //menuInflater.inflate(R.menu.settings_menu, menu);
        
        final EditText et = (EditText)((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.collapsible_edittext,null);
        
		et.addTextChangedListener(new TextWatcher(){
			
			@Override
		    public void afterTextChanged(Editable s) {
		        // TODO Auto-generated method stub
				Log.d("ActionBar", et.getText()+"");
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		        // TODO Auto-generated method stub
		    }

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        // TODO Auto-generated method stub
		    }
	
		});
        
       /* menu.add("Search")
    	.setIcon(R.drawable.ic_search)
    	.setActionView(et)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
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
			hitTriePath();
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
	
	
	private ArrayList<Integer> triePath = new ArrayList<Integer>();

	public void addPicto(Picto picto) {
		
		LinearLayout suggestbox = (LinearLayout) findViewById(R.id.suggestbox);
		suggestbox.setVisibility(View.INVISIBLE);
		
		arrayOfPictos.add(picto);
		
		HorizontalListView listview = ((HorizontalListView) findViewById(R.id.listview));
		((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
				
		getSuggestion(); 
	
	}
	
	public void deletePictos(View view) {
	
		LinearLayout suggestbox = (LinearLayout) findViewById(R.id.suggestbox);
		suggestbox.setVisibility(View.INVISIBLE);
		
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
		arrayOfPictos.clear();
		((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
		ImageView deleteBtn = (ImageView) findViewById(R.id.imageView1);
		deleteBtn.setVisibility(View.INVISIBLE);
	}

	public void updateTrieValues() {
		int parentTrieID = 0;
		String[] trieProj = new String[] {"trieID as _id","pictoID", "parentTrieID" , "hits"};
		Cursor node = null;
		if (arrayOfPictos.size() != 0) {
							
			int i = 0;
			for (; i < arrayOfPictos.size(); i++)
			{
				node = getContentResolver().query(Uri.parse(aacProvider.TRIE_PICTOID_URI + "/" + arrayOfPictos.get(i).getId()), trieProj,
						"parentTrieID = " + parentTrieID, null, null);
				if (!node.moveToFirst())
				{
					//Path doesn't fully exists add next node and go again.

						ContentValues values = new ContentValues (3);
						values.put("pictoID", arrayOfPictos.get(i).getId());
						int parent = (i == 0) ? 0 : arrayOfPictos.get(i-1).trieID;
						values.put("parentTrieID", parent);
						values.put("hits", 0 );
						Log.d("Node Created",parent+","+arrayOfPictos.get(i).getId() );
						getContentResolver().insert(Uri.parse(aacProvider.TRIE_INSERT_URI + "/" + arrayOfPictos.get(i).getId()) , values);

						node = getContentResolver().query(Uri.parse(aacProvider.TRIE_PICTOID_URI + "/" + arrayOfPictos.get(i).getId()), trieProj,
								"parentTrieID = " + parentTrieID, null, null);
						node.moveToFirst();
						
				} 
				
				arrayOfPictos.get(i).trieID = node.getInt(node.getColumnIndex("_id"));
				
				parentTrieID = node.getInt(node.getColumnIndex("_id"));
			}
			
		
			node.close();
		}
		
	}
	
	public boolean hitTriePath() {
		String[] trieProj = new String[] {"trieID as _id", "hits"};
		String[] favProj = new String[] {"pictoID as _id","playCount"};
		Cursor triehit = null;
		Cursor pictohit = null;
		ContentValues values;
		
		updateTrieValues();
		
		if (arrayOfPictos.size() != 0) {
							
			int i = 0;
			for (; i < arrayOfPictos.size(); i++)
			{
				triehit = getContentResolver().query(Uri.parse(aacProvider.TRIE_HIT_URI + "/" + arrayOfPictos.get(i).trieID), trieProj, null, null, null);
				if (!triehit.moveToFirst()) return false; 
				values = new ContentValues (1);
				values.put("hits", Integer.parseInt(triehit.getString(triehit.getColumnIndex("hits"))) + 1);
				getContentResolver().update(Uri.parse(aacProvider.TRIE_HIT_URI + "/" + arrayOfPictos.get(i).trieID) , values, null, null);
				
				pictohit = getContentResolver().query(Uri.parse(aacProvider.PICTO_HIT_URI + "/" + arrayOfPictos.get(i).getId()), favProj, null, null, null);
				if (!pictohit.moveToFirst()) return false; 
				values = new ContentValues (1);
				values.put("playCount", Integer.parseInt(pictohit.getString(pictohit.getColumnIndex("playCount"))) + 1);
				getContentResolver().update(Uri.parse(aacProvider.PICTO_HIT_URI + "/" + arrayOfPictos.get(i).getId()) , values, null, null);
				
			}

			triehit.close();
			pictohit.close();
		}
		
		return true;
			
		
		}

		public int[] getSuggestion() {
			
			updateTrieValues();
			
			int [] pictoIDs = new int[3];
			if (arrayOfPictos.size() == 0)  {
				Log.d("Suggestions", "Exit early at 1");
				return pictoIDs;
			}
			String [] projection = new String[]{ "PICTOS.pictoID as _id","Pictos.pictoPhrase", "Images.imageUri" };
			String[] trieProj = new String[] {"trieID as _id","pictoID", "parentTrieID" , "hits"};
			LinearLayout suggestbox = (LinearLayout) findViewById(R.id.suggestbox);
			suggestbox.setVisibility(View.INVISIBLE);
			suggestbox.setX(SUGGESTION_BOX_START_X + arrayOfPictos.size() * 125);
						
			Cursor suggestions = getApplicationContext().getContentResolver().query(Uri.parse(aacProvider.TRIE_SUGGEST_URI + "/" 
			+ arrayOfPictos.get(arrayOfPictos.size()-1).trieID).buildUpon().appendQueryParameter(
			        aacProvider.QUERY_PARAMETER_LIMIT,
			        "3").build(), trieProj,
					"hits > 0", null, "hits DESC");
			int i = 0;
			if (suggestions.moveToFirst()){	do { pictoIDs [i++] = Integer.parseInt(suggestions.getString(suggestions.getColumnIndex("pictoID"))); } while (suggestions.moveToNext());}

			int numFound = i;
			while (i < 3) {	pictoIDs [i++] = 0;	};
			//return an array of int of PictoIDs
			
			suggestbox.removeAllViews();
			
			//intercept number to show if there are too many words in the list
			numFound = ( arrayOfPictos.size() > 7) ? 10 - arrayOfPictos.size() : numFound;
			
			
			for (int j = 0 ; j < numFound ; j++) { 
				
				String searchString = "PICTOS.pictoID = " + pictoIDs[j]; 
				
				Cursor pictoCursor = getApplicationContext().getContentResolver().query(aacProvider.PICTO_SEARCH_URI, projection,
						searchString, null, null);
				if (!pictoCursor.moveToFirst()) {
					Log.d("Suggestions", "Exit early at 2");
					return pictoIDs;
				}
							
			View suggestpicto1 = ( (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.picto_suggest,null);
			ImageView imageView1 = (ImageView) suggestpicto1.findViewById( R.id.image);
			TextView textView1  = (TextView) suggestpicto1.findViewById( R.id.text);
						
	        try {
				
				InputStream fileStream = mExpansionFile.getInputStream("picto/" + pictoCursor.getString(pictoCursor.getColumnIndex("imageUri")));
				
				BufferedInputStream buf = new BufferedInputStream(fileStream);
				Bitmap bitmap = BitmapFactory.decodeStream(buf);
				imageView1.setImageBitmap(bitmap);
				buf.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			suggestpicto1.setTag(pictoIDs[j]);
			textView1.setText(pictoCursor.getString(pictoCursor.getColumnIndex("pictoPhrase")));			
			suggestpicto1.setOnClickListener(suggestionClicked);
			suggestbox.addView(suggestpicto1);
			
			pictoCursor.close();
			
			}
			
			if (numFound > 0) suggestbox.setVisibility(View.VISIBLE);
			
			/*for (int k = 0; k < arrayOfPictos.size(); k++){
			Log.d("Suggestion ["+k+"] trieID, pictoID", arrayOfPictos.get(k).trieID + ", " + arrayOfPictos.get(k).getId() );
			}*/
			return pictoIDs;
		}
	

	private OnClickListener suggestionClicked = new OnClickListener() {

		public void onClick(View view) {
			ImageView image = (ImageView) view.findViewById(R.id.image);
			TextView tv = (TextView) view.findViewById(R.id.text);
			int id = (Integer) ((LinearLayout) view).getTag();
			
			addPicto(new Picto((int) id, tv.getText()+"", ((BitmapDrawable)image.getDrawable()).getBitmap()));
		}
	};
	
	private OnItemClickListener pictoClicked = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			LinearLayout suggestbox = (LinearLayout) findViewById(R.id.suggestbox);
			suggestbox.setVisibility(View.INVISIBLE);
			
			arrayOfPictos.remove(position);
			((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
			
			getSuggestion();
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