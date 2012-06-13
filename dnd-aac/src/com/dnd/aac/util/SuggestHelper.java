package com.dnd.aac.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dnd.aac.MainActivity;
import com.dnd.aac.Picto;
import com.dnd.aac.R;
import com.dnd.aac.R.id;
import com.dnd.aac.R.layout;
import com.dnd.aac.data.aacProvider;

public class SuggestHelper {

	private ArrayList<Picto> arrayOfPictos;
	private Context mContext;
	private ContentResolver mCR;
	private final int SUGGESTION_BOX_START_X = 20;
	
	public SuggestHelper (ArrayList<Picto> arrayOfPictos, Context context){
		this.arrayOfPictos = arrayOfPictos;
		mContext = context;
		mCR = mContext.getContentResolver();
	}
	
	//Update trie values in arrayOfPictos from picto@position and onwards
	public void updateTrieValues(int position) {
			String[] trieProj = new String[] {"trieID as _id","pictoID", "parentTrieID" , "hits"};
			Cursor node = null;
			
			int parentTrieID = 0;
			if (position > 1) {	parentTrieID = arrayOfPictos.get(position-2).trieID; } 
			
			if (arrayOfPictos.size() != 0 ) { 
				int i = position - 1;
				for (; i < arrayOfPictos.size(); i++)
				{
					node = mCR.query(Uri.parse(aacProvider.TRIE_PICTOID_URI + "/" + arrayOfPictos.get(i).getId()), trieProj,
							"parentTrieID = " + parentTrieID, null, null);
					if (!node.moveToFirst())
					{
						//Path doesn't fully exists add next node and go again.
	
							ContentValues values = new ContentValues (3);
							values.put("pictoID", arrayOfPictos.get(i).getId());
							int parent = (i == 0) ? 0 : arrayOfPictos.get(i-1).trieID;
							values.put("parentTrieID", parent);
							values.put("hits", 0 );
							mCR.insert(Uri.parse(aacProvider.TRIE_INSERT_URI + "/" + arrayOfPictos.get(i).getId()) , values);
	
							node = mCR.query(Uri.parse(aacProvider.TRIE_PICTOID_URI + "/" + arrayOfPictos.get(i).getId()), trieProj,
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
		
		if (arrayOfPictos.size() != 0) {
							
			int i = 0;
			for (; i < arrayOfPictos.size(); i++)
			{
				triehit = mCR.query(Uri.parse(aacProvider.TRIE_HIT_URI + "/" + arrayOfPictos.get(i).trieID), trieProj, null, null, null);
				if (!triehit.moveToFirst()) return false; 
				values = new ContentValues (1);
				values.put("hits", Integer.parseInt(triehit.getString(triehit.getColumnIndex("hits"))) + 1);
				mCR.update(Uri.parse(aacProvider.TRIE_HIT_URI + "/" + arrayOfPictos.get(i).trieID) , values, null, null);
				
				pictohit = mCR.query(Uri.parse(aacProvider.PICTO_HIT_URI + "/" + arrayOfPictos.get(i).getId()), favProj, null, null, null);
				if (!pictohit.moveToFirst()) return false; 
				values = new ContentValues (1);
				values.put("playCount", Integer.parseInt(pictohit.getString(pictohit.getColumnIndex("playCount"))) + 1);
				mCR.update(Uri.parse(aacProvider.PICTO_HIT_URI + "/" + arrayOfPictos.get(i).getId()) , values, null, null);
				
			}

			triehit.close();
			pictohit.close();
		}
		
		return true;
			
		
		}

		public int[] getSuggestion() {
						
			int [] pictoIDs = new int[3];
			if (arrayOfPictos.size() == 0)  {
				Log.d("Suggestions", "Exit early at 1");
				return pictoIDs;
			}
			String [] projection = new String[]{ "PICTOS.pictoID as _id","Pictos.pictoPhrase", "Images.imageUri" };
			String[] trieProj = new String[] {"trieID as _id","pictoID", "parentTrieID" , "hits"};
			LinearLayout suggestbox = (LinearLayout) ((MainActivity) mContext).findViewById(R.id.suggestbox);
			suggestbox.setVisibility(View.INVISIBLE);
			suggestbox.setX(SUGGESTION_BOX_START_X + arrayOfPictos.size() * 125);
						
			Cursor suggestions = mCR.query(Uri.parse(aacProvider.TRIE_SUGGEST_URI + "/" 
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
				
				Cursor pictoCursor = mCR.query(aacProvider.PICTO_SEARCH_URI, projection,
						searchString, null, null);
				if (!pictoCursor.moveToFirst()) {
					Log.d("Suggestions", "Exit early at 2");
					return pictoIDs;
				}
							
			View suggestpicto1 = ( (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.picto_suggest,null);
			ImageView imageView1 = (ImageView) suggestpicto1.findViewById( R.id.image);
			TextView textView1  = (TextView) suggestpicto1.findViewById( R.id.text);
						
	        try {
				
				InputStream fileStream = MainActivity.mExpansionFile.getInputStream("picto/" + pictoCursor.getString(pictoCursor.getColumnIndex("imageUri")));
				
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

			return pictoIDs;
		}
		
		private OnClickListener suggestionClicked = new OnClickListener() {

			public void onClick(View view) {
				TextView tv = (TextView) view.findViewById(R.id.text);
				int id = (Integer) ((LinearLayout) view).getTag();
				
				((MainActivity) mContext).editHelper.addPicto(new Picto((int) id, tv.getText()+"", mContext));
			}
		};
}
