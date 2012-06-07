package com.dnd.aac;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private static final int SPLASH_DISPLAY_TIME = 0;  /*set to 0 while testing ;2 seconds */
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            
            setContentView(R.layout.splash);
          
           
             /* Create a new handler with which to start the main activity
                and close this splash activity after SPLASH_DISPLAY_TIME has
                elapsed. */
            new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NewApi")
					@Override
                    public void run() {
                           
                            /* Create an intent that will start the main activity. */
                            Intent mainIntent = new Intent(SplashScreen.this,
                                    DownloaderActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                           
                            /* Finish splash activity so user cant go back to it. */
                            SplashScreen.this.finish();
                           
                            /* Apply our splash exit (fade out) and main
                               entry (fade in) animation transitions. */
                            overridePendingTransition(R.animator.mainfadein,
                                    R.animator.splashfadeout);
                    }
            }, SPLASH_DISPLAY_TIME);
    }
}
