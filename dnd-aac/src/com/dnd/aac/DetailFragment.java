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
import android.widget.Toast;


public class DetailFragment extends android.support.v4.app.Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		   
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String projection[] = { "imageID as _id","imageUri" };
		Cursor cursor = getActivity().getContentResolver().query(aacProvider.IMAGES_URI, projection, null, null, null);
		GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(getActivity(),cursor));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
				CustomImageView a = (CustomImageView)v;
				EditText et = (EditText) getActivity().findViewById(R.id.enter);
				if (et.length() > 0) {et.append(" "+ a.getText());} else {et.append( a.getText());}

			}
		});;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details, container, false);

		    
		return view;
	}
}
