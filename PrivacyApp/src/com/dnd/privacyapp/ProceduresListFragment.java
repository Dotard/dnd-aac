package com.dnd.privacyapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dnd.privacyapp.data.PrivacyAppProvider;

public class ProceduresListFragment extends ListFragment{
	private SimpleCursorAdapter adapter;
	private Cursor procedureCursor;
	private OnProcSelectedListener procSelectedListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		this.getListView().setCacheColorHint(Color.parseColor("#481a1a"));
		bindListData(); 
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			procSelectedListener = (OnProcSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnProcSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView tv = (TextView)v.findViewById(R.id.title);
	
		procSelectedListener.onProcedureSelected(id,tv.getText());
	}



	private void bindListData()
	{	
		String projection[] = {"procedureID as _id","procedureName" };
		procedureCursor = getActivity().getContentResolver().query(
				PrivacyAppProvider.PROCEDURES_URI, projection, null, null, null);

		String[] uiBindFrom = { "procedureName" }; //Add as many columns using ,
		int[] uiBindTo = { R.id.title }; //And you can bind it to as many variables using ,

		adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), R.layout.list_item2,
				procedureCursor, uiBindFrom, uiBindTo,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(adapter);
	}



	public interface OnProcSelectedListener {
		public void onProcedureSelected(long id, CharSequence title);
	}
}
