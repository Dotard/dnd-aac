package com.dnd.aac.util;

import java.util.ArrayList;

import com.dnd.aac.R;
import com.dnd.aac.R.array;
import com.dnd.aac.R.dimen;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class MyPreferences{

	public static void registerOnSharedPreferenceChangeListener(Context c,OnSharedPreferenceChangeListener listener){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(c);
		sPref.registerOnSharedPreferenceChangeListener(listener);
	}
	
	public static void saveString(Context c, String key, String value){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(c);
		SharedPreferences.Editor editor = sPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/*
	 * Returns an array of picto sizes with the default value stored at index 0. 
	 */
	public static ArrayList<Integer> getPictoSizePrefs(Context c){
		ArrayList<Integer> prefs = new ArrayList<Integer>();
		int defaultValue = (int)c.getResources().getDimension(R.dimen.picto_m);
		
		prefs.add(defaultValue);
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_xs));
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_s));
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_m));
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_l));
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_xl));
		prefs.add((int)c.getResources().getDimension(R.dimen.picto_xxl));
					
		return prefs;
	}
	
	public static String[] getPictoSizeNames(Context c){
		String[] sizeNames = c.getResources().getStringArray(R.array.pictosize_array);
		
		return sizeNames;
	}
	
	public static String getString(Context c, String key){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(c);
		return sPref.getString(key, null);
	}
	
	public static int getInt(Context c,String key){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(c);
		return sPref.getInt(key, -1);
	}
	
	public static int getPictoSize(Context c, String key){
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(c);
		try {
			return parseIntFromString(sPref.getString(key, null));
		} catch (Exception e) {
			return -1;
		}
	}
	
    /*
     * Formats string if in a float format then parses string to return an int
     */
    private static int parseIntFromString(String number) throws Exception{
    	if(number == null) throw new Exception();
    	
    	if(number.matches("\\d*\\.\\d*")){
    		String s = number.substring(0,number.lastIndexOf('.'));
    		return Integer.parseInt(s);	    		
    	} else if(number.matches("\\d+")){
    		return Integer.parseInt(number);
    	} else{
    		throw new NumberFormatException("Invalid int: " + number);
    	}
    }

}
