package com.dnd.aac;

import android.content.Context;
import android.widget.ImageView;

public class CustomImageView extends ImageView{

	private String text;
	public CustomImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomImageView(Context context,String text) {
		super(context);
		// TODO Auto-generated constructor stub
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
}
