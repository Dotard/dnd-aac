package com.dnd.privacyapp;

import com.dnd.privacyapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class CertificateActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate);
        
        TextView tv = (TextView)this.findViewById(R.id.mainText);
        tv.setText(Html.fromHtml(
        		"PbD Tutorial <br />" +
        				"<b>Certificate of Completion</b><br />" +
        				" [Date Completed] <br />" +
        				"<a href=\"http://www.PrivacyByDesign.com\">www.PrivacyByDesign.com</a>"));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
