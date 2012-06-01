package com.dnd.aac;

import android.content.Context;
import android.graphics.Bitmap;

public class Picto {

	private int id = 0;
	private String name = "test" ;
	private Bitmap bitmap = null;
	public int trieID = 0;
	
	public Picto (int id, String name){
		this.id = id;
		this.name= name;
	}
	
	public Picto (int id, String name, Bitmap bitmap){
		this.id = id;
		this.name= name;
		this.bitmap = bitmap;
	}
	
	public int getDrawableId(Context context) {
		return 0;//return context.getResources().getIdentifier(imagestr, "drawable", "com.dnd.aac");			
	}
	
	public Bitmap getBitmap() {
		return bitmap;			
	}
	
	
	public String getText() {
		return name;			
	}
	
	public int getId() {
		return id;			
	}
	
}
