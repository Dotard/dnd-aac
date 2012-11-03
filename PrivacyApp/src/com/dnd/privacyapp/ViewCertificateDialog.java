package com.dnd.privacyapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ViewCertificateDialog extends DialogFragment{
	
	public ViewCertificateDialog(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.certificatedialog,container);
		final Dialog d = this.getDialog();
		
		TextView link = (TextView)view.findViewById(R.id.tvLink);
		link.setText(Html.fromHtml("Learn more about PbD at <a href=\"http://www.PrivacyByDesign.ca\">www.PrivacyByDesign.ca</a>"));
		link.setMovementMethod(LinkMovementMethod.getInstance());
		
		Button button = (Button)view.findViewById(R.id.btnMainMenu);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
//				Intent myIntent = new Intent(getActivity(), CertificateActivity.class );
//	            startActivity(myIntent);				
	            d.dismiss();
	            getActivity().finish();
			}
		});

		button = (Button)view.findViewById(R.id.btnResetQuizQuestions);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				Intent myIntent = new Intent(getActivity(), ReferenceListPreferencesActivity.class );
	            startActivity(myIntent);			
				d.dismiss();
			}
		});
		
		return view;
		            
	}
}
