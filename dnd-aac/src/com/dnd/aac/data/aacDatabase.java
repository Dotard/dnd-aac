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
	
	private static final String CREATE_TABLE_SUBCATEGORYS = "CREATE TABLE Subcategorys" + " ( "
			+ "subcategoryID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "categoryID INTEGER REFERENCES Categorys (categoryID), "
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "subcategoryName TEXT, "
			+ "subcategoryDesc TEXT "
			+ ");";
	
	private static final String CREATE_TABLE_PICTOS = "CREATE TABLE Pictos" + " ( "
			+ "pictoID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "pictoPhrase TEXT, "
			+ "playCount INTEGER"
			+ ");";
	
	private static final String CREATE_TABLE_RECENTPICTOS = "CREATE TABLE RecentPictos" + " ( "
			+ "recentPictoID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID), "
			+ "pictoID INTEGER REFERENCES Pictos (pictoID), "
			+ "recentPictoPhrase TEXT, "
			+ "recentPictoOutdated INTEGER "
			+ ");";
			
	private static final String CREATE_TABLE_IMAGES = "CREATE TABLE Images" + " ( "
			+ "imageID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "imageDesc TEXT, "
			+ "imageUri TEXT "
			+ ");";
	
	private static final String CREATE_TABLE_SUBCATEGORYS_PICTOS = "CREATE TABLE Subcategorys_Pictos"  + " ( "
			+ "subcategorys_PictosID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "subcategoryID INTEGER REFERENCES Subcategorys (subcategoryID), "
			+ "pictoID INTEGER REFERENCES Pictos (pictoID) "
			+ ");";


	public aacDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CATEGORYS);
		db.execSQL(CREATE_TABLE_SUBCATEGORYS);
		db.execSQL(CREATE_TABLE_PICTOS);
		db.execSQL(CREATE_TABLE_RECENTPICTOS);
		db.execSQL(CREATE_TABLE_IMAGES);
		db.execSQL(CREATE_TABLE_SUBCATEGORYS_PICTOS);
		
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + "Categorys");
		db.execSQL("DROP TABLE IF EXISTS " + "Pictos");
		db.execSQL("DROP TABLE IF EXISTS " + "RecentPictos");
		db.execSQL("DROP TABLE IF EXISTS " + "Pictos");
		db.execSQL("DROP TABLE IF EXISTS " + "Categorys_Pictos");
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
		db.execSQL("insert into Categorys (categoryName, categoryDesc, imageID) values"
				+"('Favourites', 'Favourites', 0);");
		db.execSQL("insert into Categorys (categoryName, categoryDesc, imageID) values"
				+"('Companys', 'Companies', 1);");
		db.execSQL("insert into Categorys (categoryName, categoryDesc, imageID) values"
				+"('Schools', 'Schools', 2);");
		
		//Subcategorys
		db.execSQL("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
				+"('Google', 'categoryDesc', 2, 10);");
		db.execSQL("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
				+"('Microsoft', 'categoryDesc', 2, 10);");
		db.execSQL("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
				+"('Apple', 'categoryDesc', 2, 9);");
		db.execSQL("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
				+"('Ryerson', 'categoryDesc', 3, 12);");
		db.execSQL("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
				+"('Waterloo', 'categoryDesc', 3, 13);");
		//Pictos
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('GoogleAds', 	10,		24);");
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('BillGates', 	11, 	1);");
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('Jobs', 			9, 		190);");
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('Panar', 		12, 	74);");
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('June Lowe', 	13, 	73);");
		db.execSQL("insert into Pictos (pictoPhrase, imageID, playCount) values"
				+"('Sadeg', 		12, 	200);");
		//Recent Pictos
		db.execSQL("insert into RecentPictos (recentPictoPhrase, recentPictoOutdated, pictoID, imageID) values"
				+"('recentPictoPhrase', 0, 1, 1);");
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
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('Apple Logo', 'apple.png');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('Google Logo', 'google.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('MS Logo', 'microsoft.jpeg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('Ryerson', 'poring.jpg');");
		db.execSQL("insert into Images (imageDesc, imageUri) values"
				+"('Waterloo', 'uw.jpg');");
		//Subcategory Pictos
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(1, 1);");
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(2, 2);");
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(3, 3);");
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(4, 4);");
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(5, 5);");
		db.execSQL("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
				+"(4, 6);");
	}
}
