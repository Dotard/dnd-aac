package android.support.v4.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter
{
	public ArrayList<String> categories = new ArrayList<String>();
	public ArrayList<ArrayList<String>> subcategories = new ArrayList<ArrayList<String>>();
	
	public Map<String, List<String>> objects;
	Context context;
	int parentlayout, childlayout, flags;
	Cursor parentcursor, childcursor;
	String[] from, from2;
	String[] to, to2;
	//(this, R.layout.list_example_entry, cursor, columns, to);
	public MyExpandableListAdapter(Context context, int parentlayout, int childlayout, Cursor parentcursor, Cursor childcursor, String[] from, String[] to, String[] from2, String[] to2, int flags){
		this.context = context;
		this.parentlayout = parentlayout;
		this.childlayout = childlayout;
		this.parentcursor = parentcursor;
		this.childcursor = childcursor;
		this.from = from;
		this.from2 = from2;
		this.to = to;
		this.to2 = to2;
		this.flags = flags;
		
		if( parentcursor == null || childcursor == null) {
			Log.d("Checker", "The category or subcategory cursor(s) is/are null");
			
		}
			
		
		if (from.length != to.length || from2.length != to2.length )
		{ 
			//Error matching bind
		}
		
		/*objects = new HashMap<String, List<String>>();
		
		final int COL_CHILD_CATID = childcursor.getColumnIndex("categoryID");
		final int COL_CHILD_CATNAME = childcursor.getColumnIndex("subcategoryName");
		final int COL_PARENT_CATNAME= parentcursor.getColumnIndex("categoryName");
		
		for (int i = 1 ; i <= childcursor.getCount(); i++ ) {
			int catid = childcursor.getInt(COL_CHILD_CATID);
			List<String> newValue;
			if (moveToId(parentcursor, catid, "_id")){
				if (objects.containsKey(parentcursor.getString(COL_PARENT_CATNAME))){
					newValue = objects.get(parentcursor.getString(COL_PARENT_CATNAME));
					objects.remove(parentcursor.getString(COL_PARENT_CATNAME));
				}	 else {
					newValue = new ArrayList<String> ();
				}
				newValue.add(childcursor.getString(COL_CHILD_CATNAME));
				objects.put(parentcursor.getString(COL_PARENT_CATNAME), newValue);
			}
		}*/
		
		
		
		int COL_CHILD_CATID = childcursor.getColumnIndexOrThrow("categoryID");
		int COL_CHILD_CATNAME = childcursor.getColumnIndexOrThrow("subcategoryName");
		int COL_PARENT_CATNAME= parentcursor.getColumnIndexOrThrow("categoryName");
		int COL_PARENT_CATID = parentcursor.getColumnIndexOrThrow("categoryID");
		Log.d("Array Sizes", parentcursor.getCount()+" "+childcursor.getCount());
		
		
		
		childcursor.moveToFirst();
		for (int i = 1 ; i <= childcursor.getCount(); i++ ) {
			int catid = childcursor.getInt(COL_CHILD_CATID);

			if (moveToId(parentcursor, catid, "categoryID")){
				
				if (categories.contains(parentcursor.getString(COL_PARENT_CATNAME))){
					
					int position = categories.indexOf(parentcursor.getString(COL_PARENT_CATNAME));
			        ArrayList<String> newList = subcategories.get(position);
			        	newList.add(childcursor.getString(COL_CHILD_CATNAME));
			        //Log.d("Checker", "Subcategory added: "+ childcursor.getString(COL_CHILD_CATNAME));
			        
				}	 else {
					categories.add(parentcursor.getString(COL_PARENT_CATNAME));
					ArrayList<String> newList = new ArrayList<String> (); 
					newList.add(childcursor.getString(COL_CHILD_CATNAME));
					subcategories.add(newList);		
					//Log.d("Checker", "Category added: "+ parentcursor.getString(COL_PARENT_CATNAME));
					//Log.d("Checker", "Subcategory added: "+ childcursor.getString(COL_CHILD_CATNAME));
				}
			} else {
				
				Log.d("Checker", "Category not found: ("+ catid+")");
			}
			childcursor.moveToNext();
		}
		
		
		//Log.d("Checker", "Category Size: "+ categories.size() + " - SubCategory Size: " + subcategories.size());
		//Log.d("CategoryArray", categories.toString());
		//Log.d("SubCategoryArray", subcategories.toString());
		
	}
    public Object getChild(int groupPosition, int childPosition)
    { return subcategories.get(groupPosition).get(childPosition);}// membersGroupedByCriteria.get(groupPosition).get(childPosition); }

    public long getChildId(int groupPosition, int childPosition)
    { return childPosition; }

    public int getChildrenCount(int groupPosition)
    { return subcategories.get(groupPosition).size(); }//membersGroupedByCriteria.get(groupPosition).size(); }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,  View convertView, ViewGroup parent)
    {
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(childlayout,null);
    	
    		TextView textView = (TextView) row.findViewById( context.getResources().getIdentifier("data_item","id","com.dnd.aac"));
            textView.setText((String) getChild(groupPosition, childPosition));
            //Log.d("ChildView", getChild(groupPosition, childPosition) + " " + groupPosition + " " + childPosition);
        return row;
    }

    public Object getGroup(int groupPosition)
    { return categories.get(groupPosition);}

    public int getGroupCount()
    { return categories.size(); }

    public long getGroupId(int groupPosition)
    { return groupPosition; }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
    	//Log.d("TAG", "In getGroupView");
    	
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(parentlayout, null);
    	
//    	for ( int k = 0 ; k < to.length; k++)
//    	{
    		TextView textView = (TextView) row.findViewById(context.getResources().getIdentifier("data_item","id","com.dnd.aac"));
    		if (textView == null) {Log.d("Checker", "textView is null..");}
            textView.setText(getGroup(groupPosition).toString());
//    	}
        return row;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition)
    { return true; }

    public boolean hasStableIds()
    { return true; }
    
    public boolean areAllItemsEnabled () 
    { return true; }
    
    public boolean moveToId(Cursor c, int id, String colname) {
    	int colID = c.getColumnIndex(colname);
    	//Log.d("Checker", "moveToId: (colID,colname) - ("+colID+","+colname+")"  );
    	if (colID != -1) {
    		
    		int initPos = c.getPosition();
    		
	    	c.moveToFirst();
	    	while (c.isAfterLast() == false) {
	    	  if (c.getInt(colID) == id ) {
	    		  //Log.d("Checker", "We found it!"  );
	    	      return true;
	    	  }
	    	  c.moveToNext();
	    	}
	    	c.moveToPosition(initPos);
    	}
    	return false;
    	
    	
    }
}
