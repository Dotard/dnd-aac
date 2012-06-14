package com.dnd.aac;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dnd.aac.data.ImagesTbl;
import com.dnd.aac.data.PictosTbl;
import com.dnd.aac.data.aacProvider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Picto {

	private int id = 0;
	private View pictoView; 
	public int trieID = 0;
	public String name;
	
	public Picto (int id, String name, Context context){
		this.id = id;
		this.name= name;
		
		pictoView = ( (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ).inflate(R.layout.picto_bar,null);
		
		String [] projection = new String[]{ PictosTbl.table + "." + PictosTbl.pictoID + " as _id",
				PictosTbl.table + "." + PictosTbl.pictoPhrase, ImagesTbl.table + "." + ImagesTbl.imageUri};
		Cursor pictoCursor = context.getContentResolver().query(Uri.parse(aacProvider.PICTOS_URI + "/pictos"), 
				projection,PictosTbl.table + "." + PictosTbl.pictoID + " = " + id, null, null);
    	
		if(pictoCursor.moveToFirst()){
		
    	ViewHolder holder= new ViewHolder();
		
    	holder.text = (TextView)pictoView.findViewById(R.id.text);
    	holder.image = (ImageView)pictoView.findViewById(R.id.image);
    	//holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    	holder.imageUri = pictoCursor.getString(pictoCursor.getColumnIndex(ImagesTbl.imageUri));
    	pictoView.setTag(holder);
    	
    	holder.text.setText(name);
    	
    	
        try {
    	InputStream fileStream = MainActivity.mExpansionFile.getInputStream("picto/" 
    			+ pictoCursor.getString(pictoCursor.getColumnIndex(ImagesTbl.imageUri)));
    	
    	BufferedInputStream buf = new BufferedInputStream(fileStream);
    	Bitmap bitmap = BitmapFactory.decodeStream(buf);
    	holder.image.setImageBitmap(bitmap);
    	buf.close();
    } catch (IOException e) {
    	e.printStackTrace();
    }
		
    	//DetailFragment.mImageWorker.loadImage(pictoCursor.getString(pictoCursor.getColumnIndex("imageUri")), holder.image);
		}
		pictoCursor.close();
	}
	
	public Picto (int id, String name){
		this.id = id;
		this.name= name;
		
	}
	
	public int getDrawableId(Context context) {
		return 0;//return context.getResources().getIdentifier(imagestr, "drawable", "com.dnd.aac");			
	}
	
	public View getPictoView() {
		return pictoView;			
	}
	
	
	public String getText() {
		return name;			
	}
	
	public int getId() {
		return id;			
	}
	class ViewHolder{
		public TextView text;
		public ImageView image;
		public String imageUri;
	}		
}
