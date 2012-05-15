package com.dnd.aac;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends android.support.v4.app.ExpandableListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		ExpandableListAdapter ela = new MyExpandableListAdapter(values);
		setListAdapter(ela);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getExpandableListAdapter().getChild(1, 1).toString(); //getItem(position);
		DetailFragment fragment = (DetailFragment) getFragmentManager()
				.findFragmentById(R.id.detailFragment);
		if (fragment != null && fragment.isInLayout()) {
			//fragment.setText(item);
		} else {
			Intent intent = new Intent(getActivity().getApplicationContext(),
					DetailActivity.class);
			intent.putExtra("value", item);
			startActivity(intent);
		}
		EditText et = (EditText) getActivity().findViewById(R.id.enter);
		if (et.length() > 0) {et.append(" "+item);} else {et.append(item);}
	}
	
	private class MyExpandableListAdapter extends BaseExpandableListAdapter
    {
		public ArrayList<String> headers = new ArrayList<String>();
		public ArrayList<String> leveltwo = new ArrayList<String>();
		
		public MyExpandableListAdapter(String[] values) {
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
        	View row = ( (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.listitem2, null);
        	
            TextView textView = (TextView) row.findViewById(R.id.data_item);
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
        	
        	View row = ( (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.listitem, null);
        	
            TextView textView = (TextView) row.findViewById(R.id.data_item);
            textView.setText(getGroup(groupPosition).toString());
            
            return row;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition)
        { return true; }

        public boolean hasStableIds()
        { return true; }
        
        public boolean areAllItemsEnabled () 
        { return true; }
    }
}
