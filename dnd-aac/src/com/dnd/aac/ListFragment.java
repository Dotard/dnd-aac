package com.dnd.aac;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.dnd.aac.data.aacProvider;

public class ListFragment extends android.support.v4.app.ExpandableListFragment{
	
	Cursor cat;
	Cursor subcat;
	boolean isGlowing = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*try {
		XmlResourceParser xpp=Resources.getSystem().getXml(R.drawable.listitem_background); 
		ColorStateList csl = ColorStateList.createFromXml(getResources(), 
				xpp);
		} catch (Exception e) {}
		*/
		
		
	}
	
	public void startAnimation() {

        final View brackets =  getActivity().findViewById(R.id.listFragment);
        brackets.setVisibility(View.VISIBLE);

        Animation listfade = AnimationUtils.loadAnimation(getActivity(), R.animator.glowing_set);
        
        listfade.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.animator.glowing_set);
                anim.setAnimationListener(this);
                brackets.startAnimation(anim);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });


        brackets.startAnimation(listfade);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Android" };
		String[] ints = new String[] { "android.R.id.text1"};
		
		String[] categoryProj = new String[] {"categoryID", "categoryName" , "imageUri"};
		String[] subcategoryProj = new String[] {"subcategoryID","categoryID", "subcategoryName", "imageUri"};
		cat = getActivity().getContentResolver().query(aacProvider.CATEGORYS_URI, categoryProj, null, null, null);
		subcat = getActivity().getContentResolver().query(aacProvider.SUBCATEGORYS_URI, subcategoryProj, null, null, null);
		
		ExpandableListAdapter ela = new MyExpandableListAdapter(
				getActivity().getApplicationContext(), R.layout.listitem_group_glow,R.layout.listitem_child,cat,subcat,values,ints,values,ints, 0);
		setListAdapter(ela);
				
		// Create a Drawable object with states
	    //Drawable icon =	this.getResources().getDrawable(R.drawable.selector_listitem_icon);
	    // Set the newly created Drawable object as group indicator.
	    // Now you should be seeing your icons as group indicators.
	    getExpandableListView().setGroupIndicator(null);
		
		//getExpandableListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//getExpandableListView().setSelector(R.drawable.listitem_background);
		getExpandableListView().setOnChildClickListener(new OnChildClickListener(){
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			        int childPosition, long id) {
				DetailFragment fragment = (DetailFragment) getFragmentManager()
						.findFragmentById(R.id.detailFragment);
				if (fragment != null && fragment.isInLayout()) {
						fragment.setSubcategory(id);
						
				}
				MyExpandableListAdapter adapter = (MyExpandableListAdapter) getExpandableListAdapter();
				adapter.setSelectedItem( groupPosition, childPosition);
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		
		getExpandableListView().setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if(!parent.isGroupExpanded(groupPosition))
				{
					
					DetailFragment fragment = (DetailFragment) getFragmentManager()
							.findFragmentById(R.id.detailFragment);
					if (fragment != null && fragment.isInLayout()) {
							fragment.setCategory(id);
					}
					MyExpandableListAdapter adapter = (MyExpandableListAdapter) getExpandableListAdapter();
					adapter.setSelectedItem( groupPosition);
					adapter.notifyDataSetChanged();
				}
				
				if (isGlowing){
					isGlowing = false;
				View brackets =  getActivity().findViewById(R.id.listFragment);
				brackets.clearAnimation();
				MyExpandableListAdapter adapter = (MyExpandableListAdapter) getExpandableListAdapter();
				adapter.parentlayout = R.layout.listitem_group;

				}
				return false;
			}	
			
		});	
		
		startAnimation();
	}
}
