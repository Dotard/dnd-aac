package com.dnd.aac;

import java.util.ArrayList;
import java.util.List;

import com.dnd.aac.data.aacProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.MyExpandableListAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		
		String[] categoryProj = new String[] {"categoryID as _id","categoryID", "categoryName"};
		String[] subcategoryProj = new String[] {"subcategoryID as _id","categoryID", "subcategoryName"};
		cat = getActivity().getContentResolver().query(aacProvider.CATEGORYS_URI, categoryProj, null, null, null);
		subcat = getActivity().getContentResolver().query(aacProvider.SUBCATEGORYS_URI, subcategoryProj, null, null, null);
		
		ExpandableListAdapter ela = new MyExpandableListAdapter(
				getActivity().getApplicationContext(), R.layout.listitem,R.layout.listitem2,cat,subcat,values,ints,values,ints, 0);
		setListAdapter(ela);
		
		getExpandableListView().setOnChildClickListener(new OnChildClickListener(){
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			        int childPosition, long id) {
				Log.d("Child clicked", "IncorrectID" + id);
				DetailFragment fragment = (DetailFragment) getFragmentManager()
						.findFragmentById(R.id.detailFragment);
				if (fragment != null && fragment.isInLayout()) {
						fragment.setSubcategory((int)id);
				}
				return false;
			}
		});
		
		getExpandableListView().setOnGroupClickListener(new OnGroupClickListener(){

			@Override
			public boolean onGroupClick(ExpandableListView el, View v,
					int groupPosition, long id) {
				Log.d("Group Clicked", "IncorrectID" + id);
				DetailFragment fragment = (DetailFragment) getFragmentManager()
						.findFragmentById(R.id.detailFragment);
				if (fragment != null && fragment.isInLayout()) {
						fragment.setCategory( (int)id);
				}
				return false;
			}

		});
		
	}

	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("It worked", "booya1");
		Log.d("ItemClicked", v.getTag()+"");
		String item = (String) getExpandableListAdapter().getChild(1, 1).toString(); //getItem(position);
		DetailFragment fragment = (DetailFragment) getFragmentManager()
				.findFragmentById(R.id.detailFragment);
		if (fragment != null && fragment.isInLayout()) {
			//fragment.setText(item);
			Log.d("ItemClicked", v.getTag()+"");
			if (v.getTag() == "Category") {
				fragment.setCategory((int) id);
			} else {			
				fragment.setSubcategory((int) id);
			}
		} else {
			Intent intent = new Intent(getActivity().getApplicationContext(),
					DetailActivity.class);
			intent.putExtra("value", item);
			startActivity(intent);
		}
		EditText et = (EditText) getActivity().findViewById(R.id.enter);
		if (et.length() > 0) {et.append(" "+item);} else {et.append(item);}
	}
	
	/*
	private class MyExpandableListAdapter extends BaseExpandableListAdapter
    {
		public ArrayList<String> headers = new ArrayList<String>();
		public ArrayList<String> leveltwo = new ArrayList<String>();
		Context context;
		int parentlayout, childlayout, flags;
		Cursor c;
		String[] from;
		int[] to;
		//(this, R.layout.list_example_entry, cursor, columns, to);
		public MyExpandableListAdapter(Context context, int parentlayout, int childlayout, Cursor c, String[] from, int[] to, int flags){
			this.context = context;
			this.parentlayout = parentlayout;
			this.childlayout = childlayout;
			this.c = c;
			this.from = from;
			this.to = to;
			this.flags = flags;
		headers.add("A");
		headers.add("B");
		headers.add("C");
		}
        public Object getChild(int groupPosition, int childPosition)
        { return headers.get(childPosition);}// membersGroupedByCriteria.get(groupPosition).get(childPosition); }

        public long getChildId(int groupPosition, int childPosition)
        { return childPosition; }

        public int getChildrenCount(int groupPosition)
        { return headers.size(); }//membersGroupedByCriteria.get(groupPosition).size(); }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,  View convertView, ViewGroup parent)
        {
        	View row = ( (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(android.R.layout.simple_expandable_list_item_1, null);
        	
            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return row;
        }

        public Object getGroup(int groupPosition)
        { return headers.get(groupPosition); }

        public int getGroupCount()
        { return headers.size(); }

        public long getGroupId(int groupPosition)
        { return groupPosition; }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
        	Log.d("TAG", "In getGroupView");
        	
        	View row = ( (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(android.R.layout.simple_expandable_list_item_1, null);
        	
            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setText(getGroup(groupPosition).toString());
            
            return row;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition)
        { return true; }

        public boolean hasStableIds()
        { return true; }
        
        public boolean areAllItemsEnabled () 
        { return true; }
    }*/


}
