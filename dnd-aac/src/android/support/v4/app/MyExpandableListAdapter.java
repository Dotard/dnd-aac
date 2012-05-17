package android.support.v4.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter
{
	public ArrayList<HashMap<String,String>> categories = new ArrayList<HashMap<String,String>>();
	public ArrayList<ArrayList<HashMap<String,String>>> subcategories = new ArrayList<ArrayList<HashMap<String,String>>>();
	
	public Map<String, List<String>> objects;
	
	Context context;
	int parentlayout, childlayout, flags;
	Cursor parentcursor, childcursor;
	String[] from, from2;
	String[] to, to2;

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
			
		
		int COL_CHILD_SUBID = childcursor.getColumnIndexOrThrow("subcategoryID");
		int COL_CHILD_CATID = childcursor.getColumnIndexOrThrow("categoryID");
		int COL_CHILD_CATNAME = childcursor.getColumnIndexOrThrow("subcategoryName");
		int COL_PARENT_CATNAME= parentcursor.getColumnIndexOrThrow("categoryName");
		int COL_PARENT_CATID = parentcursor.getColumnIndexOrThrow("categoryID");
		Log.d("Array Sizes", parentcursor.getCount()+" "+childcursor.getCount());
		HashMap<String,String> childMap;    
		HashMap<String,String> parentMap;
		HashMap<Long,Integer> lookupMap = new HashMap<Long,Integer>();//child looks up correct position of parent
		ArrayList<HashMap<String,String>> childList;
		
		if(parentcursor.moveToFirst()) {
			for(int i = 0; i < parentcursor.getCount(); i++){
				String catID = parentcursor.getString(COL_PARENT_CATID);
				parentMap = new HashMap<String,String>();
				parentMap.put("categoryID", catID);
				parentMap.put("categoryName", parentcursor.getString(COL_PARENT_CATNAME));
				categories.add(parentMap); //add Category
				
				childList = new ArrayList<HashMap<String,String>> ();
				subcategories.add(childList);
				
				lookupMap.put(Long.parseLong(catID), i);
				
				parentcursor.moveToNext();
			}
		}
		
		if(childcursor.moveToFirst()){
			for (int i = 0 ; i < childcursor.getCount(); i++ ){
				Long parentID = Long.parseLong(childcursor.getString(COL_CHILD_CATID));
				childList = subcategories.get(lookupMap.get(parentID));
				
				childMap = new HashMap<String,String>();
		        childMap.put("subcategoryID", childcursor.getString(COL_CHILD_SUBID));
		        childMap.put("subcategoryName", childcursor.getString(COL_CHILD_CATNAME));
		        childList.add(childMap);
		        
		        childcursor.moveToNext();
			}
		}	
	}
    public Object getChild(int groupPosition, int childPosition)
    { return subcategories.get(groupPosition).get(childPosition);}// membersGroupedByCriteria.get(groupPosition).get(childPosition); }

    public long getChildId(int groupPosition, int childPosition)
    { 
    	HashMap<String,String> m = (HashMap<String,String>) getChild(groupPosition, childPosition);
    	return Long.parseLong(m.get("subcategoryID")); 
    }

    public int getChildrenCount(int groupPosition)
    { return subcategories.get(groupPosition).size(); }//membersGroupedByCriteria.get(groupPosition).size(); }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,  View convertView, ViewGroup parent)
    {
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(childlayout,null);
    		TextView textView = (TextView) row.findViewById( context.getResources().getIdentifier("data_item","id","com.dnd.aac"));
    		HashMap<String,String> m = (HashMap<String,String>) getChild(groupPosition, childPosition);
            textView.setText(m.get("subcategoryName"));
            Log.d("ChildView", getChild(groupPosition, childPosition) + " " + groupPosition + " " + childPosition);
        return row;
    }

    public Object getGroup(int groupPosition)
    { return categories.get(groupPosition);}

    public int getGroupCount()
    { return categories.size(); }

    public long getGroupId(int groupPosition)
    { 
    	@SuppressWarnings("unchecked")
		HashMap<String,String> m = (HashMap<String,String>) getGroup(groupPosition);
    	return Long.parseLong(m.get("categoryID"));
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {   	
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(parentlayout, null);
    	
    		TextView textView = (TextView) row.findViewById(context.getResources().getIdentifier("data_item","id","com.dnd.aac"));
    		if (textView == null) {Log.d("Checker", "textView is null..");}
    		@SuppressWarnings("unchecked")
			HashMap<String,String> m = (HashMap<String,String>) getGroup(groupPosition);
            textView.setText(m.get("categoryName"));

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
    	Log.d("Checker", "moveToId: (colID,colname) - ("+colID+","+colname+")"  );
    	if (colID != -1) {
    		
    		int initPos = c.getPosition();
    		
	    	c.moveToFirst();
	    	while (c.isAfterLast() == false) {
	    	  if (c.getInt(colID) == id ) {
	    		  Log.d("Checker", "We found it!"  );
	    	      return true;
	    	  }
	    	  c.moveToNext();
	    	}
	    	c.moveToPosition(initPos);
    	}
    	return false;
    	
    	
    }
}
