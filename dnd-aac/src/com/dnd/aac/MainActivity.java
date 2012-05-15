package com.dnd.aac;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnItemClickListener, OnInitListener {
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech myTTS;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);
        
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
    }    
    public void saySomething(View view)  
    {  
    	 EditText enteredText = (EditText)findViewById(R.id.enter);
         String words = enteredText.getText().toString();
         myTTS.speak(words, TextToSpeech.QUEUE_FLUSH, null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
            	myTTS = new TextToSpeech(this, this);
            	

            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
            
            
        }

        
    }

	@Override
	public void onInit(int initStatus) {
	    if (initStatus == TextToSpeech.SUCCESS) {
	        myTTS.setLanguage(Locale.US);
	    }
	    else if (initStatus == TextToSpeech.ERROR) {
	        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
	    }

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//
	}
	
	/*public void onDestroy(){
		myTTS.shutdown();		
	}*/
	
}