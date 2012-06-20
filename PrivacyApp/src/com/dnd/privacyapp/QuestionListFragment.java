/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.dnd.privacyapp;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ExpandableListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorTreeAdapter;

import com.dnd.privacyapp.data.PrivacyAppDatabase;
import com.dnd.privacyapp.data.PrivacyAppProvider;

public class QuestionListFragment extends android.support.v4.app.ExpandableListFragment implements
LoaderManager.LoaderCallbacks<Cursor>  {
	
    private OnSectionSelectedListener sectionSelectedListener;
    private static final int TUTORIAL_LIST_LOADER = 0x01;
    
	@Override
	public boolean onChildClick(ExpandableListView parent, View view, int groupPos,
			int childPos, long id) {

        	
        sectionSelectedListener.onSectionSelected(id);

        parent.setItemChecked(childPos, true);	
		
		return super.onChildClick(parent, view, groupPos, childPos, id);
	}

	private static final String[] CHAPTER_PROJECTION = new String[] {
        "chapterID as _id",
        "chapterName"
    };

    private static final String[] SECTION_PROJECTION = new String[] {
    	"sectionID as _id",
        "sectionName"
    };
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(TUTORIAL_LIST_LOADER, null, this);

        
     // Set up our adapter
        mAdapter = new QuestionListAdapter(
                getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "chapterName" }, // Name for group layouts
                new int[] { android.R.id.text1 },
                new String[] { "sectionName" }, // Number for child layouts
                new int[] { android.R.id.text1 });

        setListAdapter(mAdapter);

        mQueryHandler = new QueryHandler(getActivity(), mAdapter);

        // Query for people
        mQueryHandler.startQuery(TOKEN_GROUP, null, PrivacyAppProvider.CHAPTERS_URI, CHAPTER_PROJECTION, null ,null, null);

        
    }
    
    
    
    private static final int TOKEN_GROUP = 0;
    private static final int TOKEN_CHILD = 1;
    
    private QueryHandler mQueryHandler;
    private CursorTreeAdapter mAdapter;
    
    private static final class QueryHandler extends AsyncQueryHandler {
        private CursorTreeAdapter mAdapter;

        public QueryHandler(Context context, CursorTreeAdapter adapter) {
            super(context.getContentResolver());
            this.mAdapter = adapter;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            switch (token) {
            case TOKEN_GROUP:
            	if (cursor != null && cursor.moveToFirst()) Log.d("QueryHandler", "Set Group cursor, # of results:" + cursor.getCount());
                mAdapter.setGroupCursor(cursor);
                break;

            case TOKEN_CHILD:
            	if (cursor != null && cursor.moveToFirst()) Log.d("QueryHandler", "Set Child cursor, # of results:" + cursor.getCount());
                int groupPosition = (Integer) cookie;
                mAdapter.setChildrenCursor(groupPosition, cursor);
                break;
            }
        }
    }

    
    
    public class QuestionListAdapter extends SimpleCursorTreeAdapter {

        // Note that the constructor does not take a Cursor. This is done to avoid querying the 
        // database on the main thread.
        public QuestionListAdapter(Context context, int groupLayout,
                int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                int[] childrenTo) {

            super(context, null, groupLayout, groupFrom, groupTo, childLayout, childrenFrom,
                    childrenTo);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            // Given the group, we return a cursor for all the children within that group 

            mQueryHandler.startQuery(TOKEN_CHILD, groupCursor.getPosition(), PrivacyAppProvider.SECTIONS_URI, 
                    SECTION_PROJECTION, null, 
                    null, "chapterID, parentID");

            return null;
        }
        
        
    }

    public interface OnSectionSelectedListener {
        public void onSectionSelected(long id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getExpandableListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            sectionSelectedListener = (OnSectionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSectionSelectedListener");
        }
    }
    
    @Override
	public void onDestroy() {
        super.onDestroy();

        // Null out the group cursor. This will cause the group cursor and all of the child cursors
        // to be closed.
        mAdapter.changeCursor(null);
        mAdapter = null;
    }
    
    // LoaderManager.LoaderCallbacks<Cursor> methods

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                PrivacyAppProvider.CHAPTERS_URI, CHAPTER_PROJECTION, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}





















