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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dnd.privacyapp.data.PrivacyAppDatabase;
import com.dnd.privacyapp.data.PrivacyAppProvider;
import com.dnd.privacyapp.service.PrivacyAppDownloaderService;
import com.dnd.privacyapp.R;

public class ReferenceListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private OnTutSelectedListener tutSelectedListener;
    private static final int TUTORIAL_LIST_LOADER = 0x01;

    private SimpleCursorAdapter adapter;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { PrivacyAppDatabase.COL_URL };
        Cursor tutorialCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(PrivacyAppProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (tutorialCursor.moveToFirst()) {
            String tutorialUrl = tutorialCursor.getString(0);
                                	
                    tutSelectedListener.onTutSelected(tutorialUrl);
                
        }
        tutorialCursor.close();
        l.setItemChecked(position, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = { PrivacyAppDatabase.COL_TITLE}; //Add as many columns using ,
        int[] uiBindTo = { R.id.title}; //And you can bind it to as many variables using ,

        getLoaderManager().initLoader(TUTORIAL_LIST_LOADER, null, this);

        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.list_item,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    public interface OnTutSelectedListener {
        public void onTutSelected(String tutUrl);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tutSelectedListener = (OnTutSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTutSelectedListener");
        }
    }

    // options menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);

        // refresh menu item
        Intent refreshIntent = new Intent(
                getActivity().getApplicationContext(),
                PrivacyAppDownloaderService.class);
        refreshIntent
                .setData(Uri
                        .parse("http://feeds.feedburner.com/mobile-tuts-summary?format=xml"));

        MenuItem refresh = menu.findItem(R.id.refresh_option_item);
        refresh.setIntent(refreshIntent);

        // pref menu item
        Intent prefsIntent = new Intent(getActivity().getApplicationContext(),
                ReferenceListPreferencesActivity.class);

        MenuItem preferences = menu.findItem(R.id.settings_option_item);
        preferences.setIntent(prefsIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh_option_item:
            getActivity().startService(item.getIntent());
            break;
        case R.id.settings_option_item:
            getActivity().startActivity(item.getIntent());
            break;
        }
        return true;
    }

    // LoaderManager.LoaderCallbacks<Cursor> methods

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { PrivacyAppDatabase.ID, PrivacyAppDatabase.COL_TITLE };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                PrivacyAppProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}