package com.dnd.aac;

import com.dnd.aac.data.aacProvider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class DetailFragment extends android.support.v4.app.Fragment {
	
	private String projection[];
	private GridView gridview;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);		   
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		projection = new String[]{ "PICTOS.pictoID as _id","Pictos.pictoPhrase", "Images.imageUri" };;
		gridview = (GridView) getActivity().findViewById(R.id.gridview);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				TextView tv = (TextView) v.findViewById(R.id.text);
				EditText et = (EditText) getActivity().findViewById(R.id.enter);
				if (et.length() > 0) {et.append(" "+ tv.getText());} else {et.append( tv.getText());}
			}
		});;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details, container, false);
   
		return view;
	}
	
	public void setSubcategory(long subcategoryID)
	{
		Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"1/" + String.valueOf(subcategoryID)), projection, null, null, null);
		gridview.setAdapter(new MyPictoAdapter(getActivity(),R.layout.picto,cursor));
	}
	
	public void setCategory(long categoryID)
	{
		Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI,"2/" + String.valueOf(categoryID)), projection, null, null, null);
		
		gridview.setAdapter(new MyPictoAdapter(getActivity(),R.layout.picto,cursor));	
	}
}
