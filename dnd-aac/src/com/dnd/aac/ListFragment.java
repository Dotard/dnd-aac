package com.dnd.aac;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;

import com.dnd.aac.adapter.PictoCatagoryAdapter;
import com.dnd.aac.data.aacProvider;

public class ListFragment extends android.support.v4.app.ExpandableListFragment {
	
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
		
		String[] categoryProj = new String[] {"categoryID", "categoryName" , "imageUri"};
		String[] subcategoryProj = new String[] {"subcategoryID","categoryID", "subcategoryName", "imageUri"};
		cat = getActivity().getContentResolver().query(aacProvider.CATEGORYS_URI, categoryProj, null, null, null);
		subcat = getActivity().getContentResolver().query(aacProvider.SUBCATEGORYS_URI, subcategoryProj, null, null, null);

		setListAdapter(new PictoCatagoryAdapter(getActivity().getApplicationContext(), R.layout.listitem_group,R.layout.listitem_child,
				cat,subcat,values,ints,values,ints, 0));
				
	    // Remove group indicators.
	    getExpandableListView().setGroupIndicator(null);

		getExpandableListView().setOnChildClickListener(new OnChildClickListener(){
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			        int childPosition, long id) {
				DetailFragment fragment = (DetailFragment) getFragmentManager()
						.findFragmentById(R.id.detailFragment);
				if (fragment != null && fragment.isInLayout()) {
						fragment.setSubcategory(id);
						
				}
				PictoCatagoryAdapter adapter = (PictoCatagoryAdapter) getExpandableListAdapter();
				adapter.setSelectedItem( groupPosition, childPosition);
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		
		getExpandableListView().setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

					DetailFragment fragment = (DetailFragment) getFragmentManager()
							.findFragmentById(R.id.detailFragment);
					if (fragment != null && fragment.isInLayout()) {
							fragment.setCategory(id);
					}
					PictoCatagoryAdapter adapter = (PictoCatagoryAdapter) getExpandableListAdapter();
					adapter.setSelectedItem( groupPosition);
					adapter.notifyDataSetChanged();
					
					//Returns true to prevent collapse
					return 	parent.isGroupExpanded(groupPosition);				
			}	
			
		});
	}
	

	public void collapseAll() {
        int count = this.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i <count ; i++)
        	this.getExpandableListView().collapseGroup(i);  
	}
	

	public void expandAll() {
	     int count = this.getExpandableListAdapter().getGroupCount();
	     for (int i = 0; i <count ; i++)
	    	 this.getExpandableListView().expandGroup(i);
	}
	
	public void toggleExpandCollapse(ImageView view) {
		int i = (Integer) view.getTag();
		if (getExpandableListView().isGroupExpanded(i)){
			this.getExpandableListView().collapseGroup(i);  
		} else {
			this.getExpandableListView().expandGroup(i);  
		}
		
	};
}
