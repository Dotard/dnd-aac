package com.dnd.aac;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class EditPictoAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<Picto> arrayOfPictos;

	public EditPictoAdapter (ArrayList<Picto> arrayOfPictos,Context context) {
		this.arrayOfPictos = arrayOfPictos;
		mContext = context;		
	}
	@Override
	public void notifyDataSetChanged() {
		if (getCount() > 0) {
			ImageView image = (ImageView) ((MainActivity) mContext).findViewById(R.id.imageView1);
			image.setVisibility(View.VISIBLE);
			((MainActivity) mContext).findViewById(R.id.emptyTextView).setVisibility(View.INVISIBLE);
		} else {
			ImageView image = (ImageView) ((MainActivity) mContext).findViewById(R.id.imageView1);
			image.setVisibility(View.INVISIBLE);
			((MainActivity) mContext).findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
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
		return arrayOfPictos.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return arrayOfPictos.get(position).getPictoView();
	}
	
	

}
