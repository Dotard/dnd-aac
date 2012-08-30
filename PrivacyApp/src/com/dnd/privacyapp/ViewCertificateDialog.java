package com.dnd.privacyapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ViewCertificateDialog extends DialogFragment{
	
	public ViewCertificateDialog(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.certificatedialog,container);
		
		Button button = (Button)view.findViewById(R.id.viewVerticiateButton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				Intent myIntent = new Intent(getActivity(), CertificateActivity.class );
	            startActivity(myIntent);
			}
		});
		
		final Dialog d = this.getDialog();
		button = (Button)view.findViewById(R.id.dismissDialogButton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				d.dismiss();
			}
		});
		
		return view;               
	}
}
