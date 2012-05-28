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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class aacDatabase extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "aacDatabase";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "aac_data";
	private static final String DB_PATH = "/data/data/com.dnd.aac/databases/";
	private Context mContext;

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
		mContext = context;
	}

	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{ 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		Log.d("tag","Attemping upgrade from " + oldVersion + "to " + newVersion);
//		if( mContext.deleteDatabase(DB_NAME)){
//			try {
//				createDataBase();
//			} catch (IOException e) {
//				throw new Error("Error creating database");
//			}
//		} else {
//			throw new Error("onUpgrade: Error deleting database");
//		}		
	}

	/**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    	}
 
    	if(checkDB != null) checkDB.close();
    	
    	return checkDB != null ? true : false;
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = mContext.getAssets().open("db/" + DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }

}
