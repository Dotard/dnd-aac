package com.dnd.aac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.dnd.aac.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter
{
	public ArrayList<HashMap<String,String>> categories = new ArrayList<HashMap<String,String>>();
	public ArrayList<ArrayList<HashMap<String,String>>> subcategories = new ArrayList<ArrayList<HashMap<String,String>>>();
	
	public Map<String, List<String>> objects;
	
	private int lastSelectedChild = -1,lastSelectedGroup = 0;
	
	Context context;
	int parentlayout, childlayout, flags;
	Cursor parentcursor, childcursor;
	String[] from, from2;
	String[] to, to2;
	ColorStateList cl = null, cl2=null;
	AnimationSet c;
	
	Bitmap ninePatchExpanded, ninePatchCollapsed; 
	
	// Setup fadein/out animations
	   AnimationSet fadeIn, fadeOut;
	   
		private ZipResourceFile mExpansionFile;
	   

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
		
		ninePatchExpanded = get_ninepatch(R.raw.expander_ic_maximized, 33, 35, context);
		ninePatchCollapsed = get_ninepatch(R.raw.expander_ic_minimized, 33, 35, context);
		
		int COL_CHILD_SUBID = childcursor.getColumnIndexOrThrow("subcategoryID");
		int COL_CHILD_CATID = childcursor.getColumnIndexOrThrow("categoryID");
		int COL_CHILD_CATNAME = childcursor.getColumnIndexOrThrow("subcategoryName");
		int COL_CHILD_IMG = childcursor.getColumnIndexOrThrow("imageUri");
		
		int COL_PARENT_CATNAME= parentcursor.getColumnIndexOrThrow("categoryName");
		int COL_PARENT_CATID = parentcursor.getColumnIndexOrThrow("categoryID");
		int COL_PARENT_IMG = parentcursor.getColumnIndexOrThrow("imageUri");
		
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
				parentMap.put("imageUri", parentcursor.getString(COL_PARENT_IMG));
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
		        childMap.put("imageUri", childcursor.getString(COL_CHILD_IMG));
		        childList.add(childMap);
		        
		        childcursor.moveToNext();
			}
		}	
		
		try {
     	   XmlResourceParser xpp = context.getResources().getXml(R.drawable.selector_listitem_background);
     	   cl = ColorStateList.createFromXml(context.getResources(), xpp);
     	   xpp = context.getResources().getXml(R.drawable.selector_listitem_textcolor);
     	   cl2 = ColorStateList.createFromXml(context.getResources(), xpp);
     	} catch (Exception e) {}

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

    @SuppressWarnings("unchecked")
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,  View convertView, ViewGroup parent)
    {
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(childlayout,null);
    		TextView textView = (TextView) row.findViewById( R.id.data_item);
            textView.setText(((HashMap<String,String>) getChild(groupPosition, childPosition)).get("subcategoryName"));
            
            if (lastSelectedGroup == groupPosition && lastSelectedChild == childPosition) 
            {	
            	row.setBackgroundResource(R.color.gray_40);
            	//row.setBackgroundResource(cl);
            	textView.setTextColor(cl2);
            }
            
        return row;
    }

    public Object getGroup(int groupPosition)
    { return categories.get(groupPosition);}

    public int getGroupCount()
    { return categories.size(); }
    
	@SuppressWarnings("unchecked")
    public long getGroupId(int groupPosition)
    { 

    	return Long.parseLong(((HashMap<String,String>) getGroup(groupPosition)).get("categoryID"));
    }

    
    @SuppressWarnings("unchecked")
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {   	
    	View row = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(parentlayout, null);
    	
    		TextView textView = (TextView) row.findViewById(R.id.data_item);
    		ImageView iv = (ImageView) row.findViewById(R.id.groupIndicator);
    		
    		if (textView == null) {Log.d("Checker", "textView is null..");}
            textView.setText(((HashMap<String,String>) getGroup(groupPosition)).get("categoryName"));

            if (lastSelectedGroup == groupPosition && lastSelectedChild == -1 ) 
            {
            	row.setBackgroundResource(R.color.gray_40);
            	textView.setTextColor(cl);
            }
                       
            if (groupPosition == 0) {    	
            	iv.setImageResource(R.drawable.btn_rating_star_off_normal);
            } else if (isExpanded){           
            	iv.setImageBitmap(ninePatchExpanded);
            } else {
            	iv.setImageBitmap(ninePatchCollapsed);
            }		   
            
            
            /*
             * TODO: Implement category pictures
             */
            //ImageView groupIcon = (ImageView) row.findViewById(R.id.groupIcon);

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
    	if (colID != -1) {
    		
    		int initPos = c.getPosition();
    		
	    	c.moveToFirst();
	    	while (c.isAfterLast() == false) {
	    	  if (c.getInt(colID) == id ) {
	    	      return true;
	    	  }
	    	  c.moveToNext();
	    	}
	    	c.moveToPosition(initPos);
    	}
    	return false;
    	
    	
    }
    
    public boolean lastSelectedItem (int groupPosition, int childPosition)
    {
    	if (lastSelectedItem(groupPosition) && lastSelectedChild == childPosition) return true;
    	return false;
    }
    public boolean lastSelectedItem (int groupPosition)
    {
    	if (lastSelectedGroup == groupPosition) return true;
    	return true;
    }
    
    public void setSelectedItem (int groupPosition, int childPosition)
    {
    	lastSelectedChild = childPosition;
    	lastSelectedGroup = groupPosition;
    }
    public void  setSelectedItem (int groupPosition)
    {
    	lastSelectedChild = -1;
    	lastSelectedGroup = groupPosition;
    }
    
    public static Bitmap get_ninepatch(int id,int x, int y, Context context){
        // id is a resource id for a valid ninepatch

        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), id);

        byte[] chunk = bitmap.getNinePatchChunk();
        boolean result = NinePatch.isNinePatchChunk(chunk);
        if (!result) { 
        	Log.d("get_ninepatch", "Chunk BAD!");
        	return null;
        }
        NinePatchDrawable np_drawable = new NinePatchDrawable(context.getResources(), bitmap,
                chunk, new Rect(), null);
        np_drawable.setBounds(0, 0,x, y);
        
        Bitmap output_bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output_bitmap);
        np_drawable.draw(canvas);
        
        return output_bitmap;
    }
}
