package com.dnd.aac.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.devsmart.android.ui.HorizontalListView;
import com.dnd.aac.MainActivity;
import com.dnd.aac.Picto;
import com.dnd.aac.R;
import com.dnd.aac.R.id;
import com.dnd.aac.adapter.PictoBarAdapter;

public class BarHelper {
	
	private Context mContext;
	private ArrayList<Picto> arrayOfPictos;
	
	public BarHelper (ArrayList<Picto> arrayOfPictos,Context context) {
		this.arrayOfPictos = arrayOfPictos;
		mContext = context;	
		
		// pictoView Setup
		HorizontalListView pictoView = (HorizontalListView) ((MainActivity) mContext).findViewById(R.id.listview);
		pictoView.setOnItemClickListener(pictoClicked);
		pictoView.setAdapter(new PictoBarAdapter (arrayOfPictos, mContext));
	}
	

	public void addPicto(Picto picto) {
		
		((LinearLayout) ((MainActivity) mContext).findViewById(R.id.suggestbox)).setVisibility(View.INVISIBLE);
		arrayOfPictos.add(picto);	
		
		((MainActivity) mContext).suggestHelper.updateTrieValues(arrayOfPictos.size());
		
		((BaseAdapter) ((HorizontalListView) ((MainActivity) mContext).findViewById(R.id.listview)).getAdapter()).notifyDataSetChanged();
		((MainActivity) mContext).suggestHelper.getSuggestion(); 
	
	}
	
	public void deletePictos(View view) {
	
		LinearLayout suggestbox = (LinearLayout) ((MainActivity) mContext).findViewById(R.id.suggestbox);
		suggestbox.setVisibility(View.INVISIBLE);
		
		
		arrayOfPictos.clear();
		HorizontalListView listview = (HorizontalListView) ((MainActivity) mContext).findViewById(R.id.listview);
		((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
		listview.scrollTo(0);
		ImageView deleteBtn = (ImageView) ((MainActivity) mContext).findViewById(R.id.imageView1);
		deleteBtn.setVisibility(View.INVISIBLE);
	}
	
	private OnItemClickListener pictoClicked = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			LinearLayout suggestbox = (LinearLayout) ((MainActivity) mContext).findViewById(R.id.suggestbox);
			suggestbox.setVisibility(View.INVISIBLE);
			
			arrayOfPictos.remove(position);
			if (position != arrayOfPictos.size()-1)
			((MainActivity) mContext).suggestHelper.updateTrieValues(position);
			((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
			
			((MainActivity) mContext).suggestHelper.getSuggestion();
		}
	};
	
}
