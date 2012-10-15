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

import java.io.IOException;
import java.io.InputStream;

import com.dnd.privacyapp.data.PrivacyAppDatabase;
import com.dnd.privacyapp.data.PrivacyAppProvider;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReferenceViewFragment extends Fragment {
	private WebView webView = null;
	private TextView tutorialContent = null;
	private ScrollView scrollView = null;
	private long sectionID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.web_view, container, false);
		scrollView = (ScrollView)v;
		webView = (WebView)v.findViewById(R.id.mwebview);

//		webView.setBackgroundColor(0x00000000);
		webView.setVisibility(View.GONE);
		sectionID = -1;
		
		tutorialContent = (TextView)v.findViewById(R.id.tutorialContent);
		
		return v;
	}

	public void loadArticle(long id) {		  
//		if (webView != null) {
//			sectionID = id;
//			String uri;
//			String projection[] = { PrivacyAppDatabase.COL_SEC_URI };
//	        Cursor sections = getActivity().getContentResolver().query(
//	                Uri.withAppendedPath(PrivacyAppProvider.SECTION_URI,
//	                        String.valueOf(id)), projection, null, null, null);
//	        sections.moveToFirst();
//	        uri = sections.getString(0);	                                		                
//	        
//	        sections.close();
//	        
//			if (uri.startsWith("http"))
//				webView.loadUrl(uri);
//			else
//				webView.loadUrl("file:///android_asset/" + uri);
//
//			layoutView.findViewById(R.id.sectionlayout).setVisibility(View.VISIBLE);
//			
//		}
		
		if(tutorialContent != null) {
			sectionID = id;
			String uri;
			String projection[] = { PrivacyAppDatabase.COL_SEC_URI };
	        Cursor sections = getActivity().getContentResolver().query(
	                Uri.withAppendedPath(PrivacyAppProvider.SECTION_URI,
	                        String.valueOf(id)), projection, null, null, null);
	        sections.moveToFirst();
	        uri = sections.getString(0);	  
	        
	        sections.close();
	        
	        AssetManager am = this.getActivity().getAssets();
			
			
			try{
				InputStream is = am.open(uri);
				
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				
				//byte buffer into a string
				String content = new String(buffer);
				
				tutorialContent.setText(Html.fromHtml(content), TextView.BufferType.SPANNABLE);
				
				scrollView.findViewById(R.id.sectionlayout).setVisibility(View.VISIBLE);
			}catch(IOException e){
				e.printStackTrace();
			}
			
	        
		}
	}

	public void onButtonClick(View v)
	{
		Intent myIntent = new Intent(this.getActivity(), QuizViewActivity.class );
		if(sectionID > 0)
		{
			myIntent.putExtra("SectionID", sectionID);        
			startActivity(myIntent);
		}
	}

}
