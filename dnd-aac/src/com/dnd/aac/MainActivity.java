package com.dnd.aac;

import java.io.IOException;
import java.util.ArrayList;
import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.dnd.aac.util.EditHelper;
import com.dnd.aac.util.MyPreferences;
import com.dnd.aac.util.SuggestHelper;
import com.dnd.aac.util.TTSHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements
OnSharedPreferenceChangeListener{
	static public ZipResourceFile mExpansionFile;
	private int MY_DATA_CHECK_CODE = 0;

	private ArrayList<Picto> arrayOfPictos = new ArrayList<Picto>();
	public SuggestHelper suggestHelper;
	public TTSHelper ttsHelper;
	public EditHelper editHelper;
	private Menu optMenu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
		suggestHelper = new SuggestHelper(arrayOfPictos, this);
		ttsHelper = new TTSHelper(arrayOfPictos, this);
		editHelper = new EditHelper(arrayOfPictos, this);
		
		
		//Load defaults; first time ONLY
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		MyPreferences.registerOnSharedPreferenceChangeListener(this, this);
		
        try {
			mExpansionFile = APKExpansionSupport.getAPKExpansionZipFile(this, 1, 0);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        
        Menu sizesMenu = menu.findItem(R.id.menu_size).getSubMenu(); //get menu for picto sizes 
        //SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        this.optMenu = menu;
        
        ArrayList<Integer> sizePrefs = MyPreferences.getPictoSizePrefs(this);
        sizePrefs.remove(0);//remove default entry
        int selectedSize = MyPreferences.getPictoSize(this, getString(R.string.pref_pictosize));
            
        for(int i = 0; i < sizePrefs.size(); i++){
        	if(selectedSize == sizePrefs.get(i)){
        		sizesMenu.getItem(i).setChecked(true); 
        		break;
        	}
        }
        
        //do stuff for searchView
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		DetailFragment detailsFragment;
		int dimRes = -1;
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
	        		startActivity(new Intent(this,MyPreferenceActivity.class));
	        	}else{
	        		startActivity(new Intent(this,MyPreferenceHCActivity.class));
	        	}	        		
	        	return true;
	        case R.id.menu_size_xs:
	        	dimRes =  R.dimen.picto_xs; break;
	        case R.id.menu_size_s:
	        	dimRes =  R.dimen.picto_s; break;	        	
	        case R.id.menu_size_m:
	        	dimRes =  R.dimen.picto_m; break;
	        case R.id.menu_size_l:
	        	dimRes =  R.dimen.picto_l; break;
	        case R.id.menu_size_xl:
	        	dimRes =  R.dimen.picto_xl; break;
	        case R.id.menu_size_xxl:
	        	dimRes =  R.dimen.picto_xxl; break;
	        case R.id.menu_exit:
	        	finish();
	        	return true;       
	    }
	    
	    if (dimRes != -1) {
	    	item.setChecked(true);	      
	    	MyPreferences.saveString(this, getString(R.string.pref_pictosize), String.valueOf(getResources().getDimension(dimRes)));
	    	detailsFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
	    	detailsFragment.refreshGridView();
	    	return true;
	    }
	    
	    return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			ttsHelper.onActivityResult(requestCode,resultCode,data);
		}
	}

		
	public void saySomething(View view) {
		if (!ttsHelper.isSpeaking()) suggestHelper.hitTriePath();
		ttsHelper.saySomething(view);
	}
	
	public void deletePictos(View view) {
		editHelper.deletePictos(view);
	}
		
	@Override
	protected void onDestroy() {
		ttsHelper.destroy();
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		if(key.equals(getString(R.string.pref_pictosize))){
			Menu sizesMenu = optMenu.findItem(R.id.menu_size).getSubMenu(); //get menu for picto sizes 
	       	               
	        ArrayList<Integer> sizePrefs = MyPreferences.getPictoSizePrefs(this);
	        sizePrefs.remove(0);//remove default entry
	        int selectedSize = MyPreferences.getPictoSize(this, getString(R.string.pref_pictosize));
	            
	        for(int i = 0; i < sizePrefs.size(); i++){
	        	if(selectedSize == sizePrefs.get(i)){
	        		sizesMenu.getItem(i).setChecked(true); 
	        		DetailFragment detailsFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
	        		if(detailsFragment != null){
	        			detailsFragment.refreshGridView();
	        		}
	        		break;
	        	}
	        }
		}	
	}

}