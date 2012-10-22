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
	public static final String TABLE_SECTION = "Section";
	public static final String TABLE_QUIZ = "Quiz";
	public static final String ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_URL = "url";
	public static final String COL_SEC_ID = "secID";
	public static final String COL_SEC_NAME = "secName";	
	public static final String COL_SEC_URI = "secURI";

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
	
	private static final String CREATE_TABLE_SECTION = "CREATE TABLE Section ( "
			+ "secID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "secParentID INTEGER REFERENCES Section (secID), "
			+ "secName TEXT NOT NULL, "
			+ "secQuizComplete INTEGER DEFAULT 0 NOT NULL, "
			+ "secURI TEXT NOT NULL"
			+ ");";
	
	private static final String CREATE_TABLE_QUIZ = "CREATE TABLE QUIZ ( "
			+ "quizID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "secID INTEGER REFERENCES Section (secID), "
			+ "quizText TEXT NOT NULL, "
			+ "quizOption1 TEXT, "
			+ "quizOption2 TEXT, "
			+ "quizOption3 TEXT, "
			+ "quizOption4 TEXT, "
			+ "quizOption5 TEXT, "
			+ "quizCorrectOption INTEGER NOT NULL, "
			+ "quizComplete INTEGER DEFAULT 0"
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
		db.execSQL(CREATE_TABLE_SECTION);
		db.execSQL(CREATE_TABLE_QUIZ);
		seedData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DEBUG_TAG,
				"Upgrading database. Existing contents will be lost. ["
						+ oldVersion + "]->[" + newVersion + "]");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORIALS);		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROCEDURES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROCEDUREITEMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);

		onCreate(db);
	}

	/**
	 * Create sample data to use
	 * 
	 * @param db
	 *            The open database
	 */
	private void seedData(SQLiteDatabase db) {
		db.execSQL("insert into tutorials (title, url) values ('Section 1', 'html/sec1.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 2', 'html/sec2.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 3', 'html/sec3.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 4', 'html/sec4.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 5', 'html/sec5.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 6', 'html/sec6.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 7', 'html/sec7.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 8', 'html/sec8.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 9', 'html/sec9.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 10', 'html/sec10.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 11', 'html/sec11.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 12', 'html/sec12.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 13', 'html/sec13.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 14', 'html/sec14.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 15', 'html/sec15.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 16', 'html/sec16.html');");
		db.execSQL("insert into tutorials (title, url) values ('Section 17', 'html/sec17.html');");
		
		db.execSQL("insert into Chapters (chapterName, chapterDesc) values ('Ch. 1', 'Introduction to Privacy');");
		
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID ) values ('¤ 1', 'Section 1', 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID ) values ('¤ 2', 'Section 2', 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('¤ 1.1', 'Section 1.1', 1, 1);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('¤ 2.1', 'Section 2.1', 1, 2);");
		db.execSQL("insert into Sections (sectionName, sectionDesc, chapterID, parentID ) values ('¤ 1.2', 'Section 1.2', 1, 1);");
		
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 1 What is the first option?', 1, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',1, 1);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 2 What comes after 1?', 2, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',2, 2);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 3 What button is in the middle?', 3, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',3, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption,sectionID ) values"
				+"('Question 4 What question number is this?', 4, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Option 5',4, 4);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 5 What Do you think about this app?', 3, 'Option 1','Option 2', 'Its good', 'Option 4', 'Option 5',3, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 6 Which is the correct answer?', 3, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Correct Answer',5, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 7 What Do you think about this app?', 3, 'Option 1','Option 2', 'Its good', 'Option 4', 'Option 5',3, 3);");
		db.execSQL("insert into Questions (questionDesc, questionNumber, option1, option2, option3, option4, option5, correctOption, sectionID ) values"
				+"('Question 8 Which is the correct answer?', 3, 'Option 1','Option 2', 'Option 3', 'Option 4', 'Correct Answer',5, 3);");

		
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
		
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('Privacy Basics: what is privacy and why does it matter?', 'html/sec1.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('How is information privacy expressed?', 'html/sec2.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('Principles of Fair Information Practices (FIPs)', 'html/sec3.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('Privacy Under attack', 'html/sec4.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('Needed: More Robust FIPs ', 'html/sec5.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('Emergence of Privacy by Design (PbD)', 'html/sec6.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('7 Foundational Principles of Privacy by Design', 'html/sec7.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Proactive not Reactive; Preventative not Remedial', 'html/sec8.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Privacy as the Default Setting', 'html/sec9.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Privacy Embedded into Design', 'html/sec10.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Full Functionality - Positive-Sum, not Zero-Sum', 'html/sec11.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: End-to-End Security - Full Lifecycle Protection', 'html/sec12.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Visibility and Transparency - Keep it Open', 'html/sec13.html');");
		db.execSQL("INSERT into Section(secName, secURI) values"
				+ "('PbD Foundational Principle: Respect for User Privacy - Keep it User-Centric', 'html/sec14.html');");

		
//		db.execSQL("INSERT into Section(secName, secParentID,secURI) values"
//				+ "();");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(1,'In 1928, U.S. Justice Louis D. Brandeis famously defined privacy by as \"the right to be let alone -- the most comprehensive of rights and the right most valued by civilized men.\" Do you think that this is still an accurate description of privacy today?'" +
						",'No, it confuses 4th amendment (rights against search and seizure) with privacy'" +
						",'No, it does not capture the idea of individual control'" +
						",'No, because women have privacy rights, too'" +
						",'All of the above'" +
						",'None of the above',4);");
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(1,'Information asymmetries confer power and advantage to parties with more or better knowledge than the others in a given transaction scenario or context. In the context of privacy, this can result in:'" +
				",'Adverse selection, i.e., discrimination against disadvantaged individuals','Moral hazard, i.e., misuse or abuse of information about disadvantaged individuals'" +
				",'Information monopoly, i.e., individuals cannot find out what is known about them','All of the above','None of the above',4);");
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(1,'Studies have shown that people who are continuously watched and recorded behave differently. Which of the following is the least-privacy-invasive impact?'" +
				",'Conform to the expectations of others (as the individual understands this)','Disguise self, or develop different personas or identities.'" +
				",'Wash hands more frequently in washrooms','All of the above','None of the above',3);");
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(1,'Germany enshrined the individual right to privacy into their constitution, mainly because:','war victims needed protection ','Germany wanted to prevent future abuses of secret dossiers on citizens by the state'" +
				",'the Allied forces made them do it','All of the above','None of the above',2);");

		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(2,'\"Privacy by Obscurity means\"','Hiding','Covering Up','No record made','All of the above','None of the above',3);");
		
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(3,'Digitization of data heralded the dawn of the modern privacy because:','Made possible the large-scale collection, use and transfer of personal data','Privacy laws potentially created new trade barriers'" +
				",'Allowed unknown and unaccountable entities to collect information about individuals.','All of the above','None of the above',4);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(4,'Which of the following forms of identity presentation allow individuals to be tracked and monitored automatically?','Browser cookies','Cell phone numbers'" +
				",'Use of payment cards','All of the above','None of the above',4);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(5,'User-generated content is not always fully covered by privacy laws because individuals are presumed to have consented to putting their own personal information into the public domain. Which of the following online activities might fall into this category?','Social media profiles'" +
				",'Blog or twitter feeds','Participation in wikis','All of the above','None of the above',4);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(6,'Privacy by Design Principles have special relevance for large-scale, complex architectures and ecosystems, where multiple participants must collaborate and coordinate to ensure appropriate privacy protections. Which of the following domains would benefit from applying PbD principles?'" +
				",'Interoperable electronic health records','social networking platforms','cloud computing','mobile ecosystem','All of the above',5);");
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(7,'Okay this is an easy question: how many Privacy by Design Foundational Principles are there??','Zero','One','Four ','Seven','Ten',4);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(8,'An ounce of prevention is worth a pound of cure. If privacy breaches aren’t prevented, then what is typically the \"cure\"?','negative publicity and brand damage','costs of remediation ','customer churn','fines and lawsuits','All of the above',5);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(9,'Which of the following examples illustrates Privacy as the Default Setting??','Wi-fi routers require first time users to create a secure key upon installation','Online services require users to agree to lengthy privacy policies before allowing to proceed'" +
				",'Users must opt out of automatic online tracking and monitoring','All of the above','None of the above',1);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(10,'The best privacy protections should be systematically integrated into the information technologies, systems and architectures and be automatic.  Which of the following scenarios best illustrates this concept?'" +
				",'Browsers ship with native ability to automatically encrypt online sessions (SSL/TLS)','Devices installed in vehicles can automatically monitor vehicle location, activity, and time for the purposes of calculating rental, insurance and road toll costs and invoices.','An internet single sign-on scheme allows users to reuse their credentials to access other websites, allowing centralized monitoring and control of user activities.'" +
				",'All of the above','None of the above',1);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(11,'The hallmark of positive-sum results is achieving apparently irreconcilable objectives in an optimized way. Which of the following examples best illustrates this from a privacy perspective?'" +
				",'Body screening machines that quickly and accurately detect aviation threats without any need to identity the passenger or store images.','An interoperable electronic health record that is kept accurate and secure and made available to health care practitioners across the country.'" +
				",'Online tracking, profiling and advertising that is signaled by a small icon a web page.','All of the above','None of the above',1);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(12,'Which of the following operational requirements describe this principle?','encrypt by default','securely destroy devices and media containing personal data','log, monitor and audit activity','All of the above','None of the above',1);");
		
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(13,'Being open and transparent involves more than just appointing a privacy officer and posting a privacy policy. What else could it require?','documenting data holdings, processes and outcomes','submitting to independent reviews, assessments and audits','posting results for others to emulate as best practices'" +
				",'All of the above','None of the above',4);");
		db.execSQL("INSERT into Quiz(secID, quizText, quizOption1, quizOption2, quizOption3, quizOption4, quizOption5, quizCorrectOption) values" 
				+ "(14,'Which of the following organizational activities helps advance user-centricity?','Providing timely and clear privacy information and notices','providing users with intuitive and effective ways to exercise preferences','provide users with direct access to data holdings and an account of all uses'" +
				",'All of the above','None of the above',4);");
				
	}
}
