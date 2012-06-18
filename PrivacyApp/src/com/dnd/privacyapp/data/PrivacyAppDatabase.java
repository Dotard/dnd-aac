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
package com.dnd.privacyapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PrivacyAppDatabase extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "PrivacyAppDatabase";
	private static final int DB_VERSION = 3;
	private static final String DB_NAME = "tutorial_data";

	public static final String TABLE_TUTORIALS = "tutorials";
	public static final String TABLE_CHAPTERS = "Chapters";
	public static final String TABLE_SECTIONS = "Sections";
	public static final String TABLE_QUESTIONS = "Questions";
	public static final String TABLE_PROCEDURES = "Procedures";
	public static final String TABLE_PROCEDUREITEMS = "ProcedureItems";
	public static final String ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_URL = "url";

	private static final String CREATE_TABLE_TUTORIALS = "CREATE TABLE "
			+ TABLE_TUTORIALS + " (" + ID
			+ " integer PRIMARY KEY AUTOINCREMENT, " + COL_TITLE
			+ " text NOT NULL, " + COL_URL + " text UNIQUE NOT NULL);";

	private static final String CREATE_TABLE_CHAPTERS = "CREATE TABLE Chapters (  "
			+ "chapterID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "chapterName TEXT NOT NULL, "
			+ "chapterDesc TEXT NOT NULL"
			+ ");";
	private static final String CREATE_TABLE_SECTIONS = "CREATE TABLE Sections (  "
			+ "sectionID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "sectionName TEXT NOT NULL, "
			+ "sectionDesc TEXT NOT NULL,"
			+ "quizName TEXT, "
			+ "quizComplete INTEGER DEFAULT 0 NOT NULL, "
			+ "chapterID INTEGER REFERENCES Chapters (chapterID) NOT NULL, "
			+ "parentID INTEGER REFERENCES Sections (sectionID) "
			+ ");";
	private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE Questions (  "
			+ "questionID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "questionDesc TEXT NOT NULL, "
			+ "questionNumber INTEGER, "
			+ "option1 TEXT,"
			+ "option2 TEXT,"
			+ "option3 TEXT,"
			+ "option4 TEXT,"
			+ "option5 TEXT,"
			+ "correctOption INTEGER, "
			+ "selectedOption INTEGER, "
			+ "sectionID INTEGER REFERENCES Sections (sectionID) "
			+ ");";
	private static final String CREATE_TABLE_PROCEDURES = "CREATE TABLE Procedures ( "
			+ "procedureID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "procedureName TEXT, "
			+ "procedureDesc TEXT"
			+ ");";
	
	private static final String CREATE_TABLE_PROCEDUREITEMS = "CREATE TABLE ProcedureItems ( "
			+ "procedureItemsID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "procedureID INTEGER REFERENCES Procedures (procedureID) NOT NULL,"
			+ "procedureItemsText TEXT, "
			+ "procedureItemsDesc TEXT, "
			+ " procedureItemsOrder INTEGER"
			+ ");";
	

	private static final String DB_SCHEMA = CREATE_TABLE_TUTORIALS;

	public PrivacyAppDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_SCHEMA);
		db.execSQL(CREATE_TABLE_CHAPTERS);
		db.execSQL(CREATE_TABLE_SECTIONS);
		db.execSQL(CREATE_TABLE_QUESTIONS);
		db.execSQL(CREATE_TABLE_PROCEDURES);
		db.execSQL(CREATE_TABLE_PROCEDUREITEMS);
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORIALS);
		onCreate(db);
	}

	/**
	 * Create sample data to use
	 * 
	 * @param db
	 *            The open database
	 */
	private void seedData(SQLiteDatabase db) {
		db.execSQL("insert into tutorials (title, url) values ('Local Page', 'html/DemoPage.html');");
		db.execSQL("insert into tutorials (title, url) values ('Best of Tuts+ in February 2011', 'http://mobile.tutsplus.com/articles/news/best-of-tuts-in-february-2011/');");
		db.execSQL("insert into tutorials (title, url) values ('Design & Build a 1980s iOS Phone App: Design Comp Slicing', 'http://mobile.tutsplus.com/tutorials/mobile-design-tutorials/80s-phone-app-slicing/');");
		db.execSQL("insert into tutorials (title, url) values ('Create a Brick Breaker Game with the Corona SDK: Game Controls', 'http://mobile.tutsplus.com/tutorials/corona/create-a-brick-breaker-game-with-the-corona-sdk-game-controls/');");
		db.execSQL("insert into tutorials (title, url) values ('Exporting Graphics for Mobile Apps: PNG or JPEG?', 'http://mobile.tutsplus.com/tutorials/mobile-design-tutorials/mobile-design_png-or-jpg/');");
		db.execSQL("insert into tutorials (title, url) values ('Android Tablet Design', 'http://mobile.tutsplus.com/tutorials/android/android-tablet-design/');");
		db.execSQL("insert into tutorials (title, url) values ('Build a Titanium Mobile Pizza Ordering App: Order Form Setup', 'http://mobile.tutsplus.com/tutorials/appcelerator/build-a-titanium-mobile-pizza-ordering-app-order-form-setup/');");
		db.execSQL("insert into tutorials (title, url) values ('Create a Brick Breaker Game with the Corona SDK: Application Setup', 'http://mobile.tutsplus.com/tutorials/corona/corona-sdk_brick-breaker/');");
		db.execSQL("insert into tutorials (title, url) values ('Android Tablet Virtual Device Configurations', 'http://mobile.tutsplus.com/tutorials/android/android-sdk_tablet_virtual-device-configuration/');");
		db.execSQL("insert into tutorials (title, url) values ('Build a Titanium Mobile Pizza Ordering App: Topping Selection', 'http://mobile.tutsplus.com/tutorials/appcelerator/pizza-ordering-app-part-2/');");
		db.execSQL("insert into tutorials (title, url) values ('Design & Build a 1980s iOS Phone App: Interface Builder Setup', 'http://mobile.tutsplus.com/tutorials/iphone/1980s-phone-app_interface-builder-setup/');");
		
		db.execSQL("insert into Chapters (chapterName, chapterDesc) values ('Chapter 1', 'Introduction to Privacy');");
		
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID ) values ('Section 1', 'Section 1', 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID ) values ('Section 2', 'Section 2', 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('Section 1.1', 'Section 1.1', 1, 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('Section 2.1', 'Section 2.1', 1, 2);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('Section 1.2', 'Section 1.2', 1, 1);");
		
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 1 Blah Blah?', 1, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',1, 1);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 2 Trololol?', 2, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',2, 2);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 3 Slime Time?', 3, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',3, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption,sectionID ) values"
				+"('Question 4 Too Slimey?', 4, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',4, 4);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question Where are we?', 3, 'Option 1','Option 2', 'Rye', 'Option 4', 'Option 5',3, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question Slime boss?', 3, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Fishing botter',5, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question Diablo 3 release date?', 3, 'Option 1','May 15', 'Option 3', 'Option 4', 'Option 5',2, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question Raj _____?', 3, 'Kutrapali','Option 2', 'Option 3', 'Option 4', 'Option 5',1, 3);");
		
		db.execSQL("insert into Procedures(procedureName, procedureDesc) values"
				+ "('New license','Sign up for new license');");
		db.execSQL("insert into Procedures(procedureName, procedureDesc) values"
				+ "('Renew Plates','Get new license plates');");
		db.execSQL("insert into Procedures(procedureName, procedureDesc) values"
				+ "('Renew License','Renew your license');");
		db.execSQL("insert into Procedures(procedureName, procedureDesc) values"
				+ "('New Health Card','Sign up for new health card');");
		
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(1,'Get first name','Retrieve first name from customer',1);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(1,'Get last name','Retrieve last name from customer',2);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(1,'Get middle name','Retrieve middle name from customer',3);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(1,'Get parents name','Retrieve parents name from customer',4);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(1,'Get their name','Retrieve name from customer',1);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(2,'Get their last name','Retrieve last name from customer',1);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(3,'Get their address','Retrieve address from customer',1);");
		db.execSQL("insert into ProcedureItems(procedureID,procedureItemsText,procedureItemsDesc,procedureItemsOrder) values"
				+ "(4,'Get their postal','Retrieve postal from customer',1);");
	}
}
