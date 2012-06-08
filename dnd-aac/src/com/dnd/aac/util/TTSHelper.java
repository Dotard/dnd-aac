package com.dnd.aac.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.dnd.aac.MainActivity;
import com.dnd.aac.Picto;
import com.dnd.aac.R;
import com.dnd.aac.R.drawable;
import com.dnd.aac.R.id;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TTSHelper implements OnUtteranceCompletedListener, OnInitListener {

	private ArrayList<Picto> arrayOfPictos;
	private Context mContext;
	private Handler mHandler;
	private TextToSpeech myTTS;
	private HashMap<String, String> mTTSSettings = new HashMap<String, String>();
	private int MY_DATA_CHECK_CODE = 0;

	public TTSHelper (ArrayList<Picto> arrayOfPictos, Context context){
		this.arrayOfPictos = arrayOfPictos;
		mContext = context;
		
		// On Speech finish handler
		mHandler = new Handler();
		mTTSSettings.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "test");
		((MainActivity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		((MainActivity) context).startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}
	
	@Override
	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS) {
			init();
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(mContext, "Sorry! Text To Speech failed...",Toast.LENGTH_LONG).show();
		}

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				myTTS = new TextToSpeech(mContext,this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				((MainActivity) mContext).startActivity(installIntent);
		}
		}
	}

	public TextToSpeech getTTS() {
		return myTTS;
	}

	public void init() {
		myTTS.setLanguage(Locale.US);
		myTTS.setOnUtteranceCompletedListener(this);
	}
	
	public boolean isSpeaking() {return myTTS.isSpeaking();}
	
	public void saySomething(View view) {
		Button speakBtn = (Button) ((MainActivity) mContext).findViewById(R.id.button1);
		if (!myTTS.isSpeaking()) {

			String words = "";
			for (int i = 0; i < arrayOfPictos.size(); i++) {
				words += arrayOfPictos.get(i).getText() + " ";
			}

			speakBtn.setBackgroundResource(R.drawable.speech_cancel);
			myTTS.speak(words, TextToSpeech.QUEUE_FLUSH, mTTSSettings);				
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
					Button speakBtn = (Button) ((MainActivity) mContext).findViewById(R.id.button1);
					speakBtn.setBackgroundResource(R.drawable.statelist_speechbtn);
				}
			};
			mHandler.post(mUpdateResults);
		}
	}
	public void destroy() {
	// Close the Text to Speech Library
			if (myTTS != null) {

				myTTS.stop();
				myTTS.shutdown();
			}
	}
	
	
}
