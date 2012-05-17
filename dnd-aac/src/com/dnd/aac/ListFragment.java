package com.dnd.aac;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.MyExpandableListAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;

import com.dnd.aac.data.aacProvider;

public class ListFragment extends android.support.v4.app.ExpandableListFragment{
	
	Cursor cat;
	Cursor subcat;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Android" };
		String[] ints = new String[] { "android.R.id.text1"};
		
		String[] categoryProj = new String[] {"categoryID", "categoryName"};
		String[] subcategoryProj = new String[] {"subcategoryID","categoryID", "subcategoryName"};
		cat = getActivity().getContentResolver().query(aacProvider.CATEGORYS_URI, categoryProj, null, null, null);
		subcat = getActivity().getContentResolver().query(aacProvider.SUBCATEGORYS_URI, subcategoryProj, null, null, null);
		
		ExpandableListAdapter ela = new MyExpandableListAdapter(
				getActivity().getApplicationContext(), R.layout.listitem,R.layout.listitem2,cat,subcat,values,ints,values,ints, 0);
		setListAdapter(ela);
		
		getExpandableListView().setOnChildClickListener(new OnChildClickListener(){
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			        int childPosition, long id) {
				Log.d("Child clicked", "" + id);
				DetailFragment fragment = (DetailFragment) getFragmentManager()
						.findFragmentById(R.id.detailFragment);
				if (fragment != null && fragment.isInLayout()) {
						fragment.setSubcategory(id);
				}
				return true;
			}
		});
		
		getExpandableListView().setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Log.d("Group Clicked", "" + parent.isGroupExpanded(groupPosition));
				if(!parent.isGroupExpanded(groupPosition))
				{
					DetailFragment fragment = (DetailFragment) getFragmentManager()
							.findFragmentById(R.id.detailFragment);
					if (fragment != null && fragment.isInLayout()) {
							fragment.setCategory(id);
					}
				}
				return false;
			}
		});	
	}
}
