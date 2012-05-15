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
package com.dnd.aac.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class aacDatabase extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "aacDatabase";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "aac_data";

	//add table names column names here
	
	private static final String CREATE_TABLE_CATEGORYS = "CREATE TABLE Categorys"  + " ( "
			+ "categoryID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "categoryName TEXT, "
			+ "categoryDesc TEXT "
			+ ");";
	
	private static final String CREATE_TABLE_ITEMS = "CREATE TABLE Items" + " ( "
			+ "itemID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "itemPhrase TEXT "
			+ ");";
	
	private static final String CREATE_TABLE_RECENTITEMS = "CREATE TABLE RecentItems" + " ( "
			+ "recentItemID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "itemID INTEGER REFERENCES Items (itemID), "
			+ "recentItemPhrase TEXT, "
			+ "recentItemOutdated INTEGER "
			+ ");";
			
	private static final String CREATE_TABLE_IMAGES = "CREATE TABLE Images" + " ( "
			+ "imageID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "imageDesc TEXT, "
			+ "imageUri TEXT "
			+ ");";
	
	private static final String CREATE_TABLE_CATEGORYS_ITEMS = "CREATE TABLE Categorys_Items"  + " ( "
			+ "categorys_ItemsID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "categoryID INTEGER REFERENCES Categorys (categoryID), "
			+ "itemID INTEGER REFERENCES Items (itemID) "
			+ ");";


	public aacDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CATEGORYS);
		db.execSQL(CREATE_TABLE_ITEMS);
		db.execSQL(CREATE_TABLE_RECENTITEMS);
		db.execSQL(CREATE_TABLE_IMAGES);
		db.execSQL(CREATE_TABLE_CATEGORYS_ITEMS);
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + "Categorys");
		db.execSQL("DROP TABLE IF EXISTS " + "Items");
		db.execSQL("DROP TABLE IF EXISTS " + "RecentItems");
		db.execSQL("DROP TABLE IF EXISTS " + "Items");
		db.execSQL("DROP TABLE IF EXISTS " + "Categorys_Items");
		onCreate(db);
	}

	/**
	 * Create sample data to use
	 * 
	 * @param db
	 *            The open database
	 */
	private void seedData(SQLiteDatabase db) {
		//Categorys
		db.execSQL("insert into CATEGORYS (categoryName, categoryDesc, imageID) values"
				+"('categoryName', 'categoryDesc', 1);");
		//Items
		db.execSQL("insert into Items (itemPhrase, imageID) values"
				+"('itemPhrase', 1);");
		//Recent Items
		db.execSQL("insert into RecentItems (recentItemPhrase, recentItemOutdated, itemID, imageID) values"
				+"('recentItemPhrase', 0, 1, 1);");
		//Images
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_0.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_1.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_2.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_3.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_4.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_5.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_6.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('imageDesc', 'sample_7.jpg');");
		//Category Items
		db.execSQL("insert into Categorys_Items (categoryID, itemID) values"
				+"(1, 1);");
		
	}
}
