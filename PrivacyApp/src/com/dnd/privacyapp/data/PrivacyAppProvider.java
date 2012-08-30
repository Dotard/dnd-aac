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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PrivacyAppProvider extends ContentProvider {

    private PrivacyAppDatabase mDB;

    private static final String AUTHORITY = "com.dnd.privacyapp.data.PrivacyAppProvider";
    public static final int TUTORIALS = 100;
    public static final int TUTORIAL_ID = 110;
    public static final int CHAPTERS = 200;
    public static final int CHAPTER_ID = 210;
    public static final int SECTIONS 	= 300;
    public static final int SECTIONS_ID 	= 310;
    public static final int QUESTIONS 	= 400;
    public static final int QUESTION_ID = 410;
    public static final int PROCEDURES = 500;
    public static final int PROCEDURES_ID =510;
    public static final int PROCEDUREITEMS = 600;
    public static final int SECTION = 700;
    public static final int SECTION_ID = 710;
    public static final int QUIZ = 800;
    public static final int QUIZ_ID = 820;
    public static final int QUIZ_SECTION = 810;

    private static final String TUTORIALS_BASE_PATH = "tutorials";
    private static final String CHAPTERS_BASE_PATH = "Chapters";
    private static final String SECTIONS_BASE_PATH 	= "Sections";
    private static final String QUESTIONS_BASE_PATH	= "Questions";
    private static final String PROCEDURES_BASE_PATH = "Procedures";
    private static final String PROCEDUREITEMS_BASE_PATH = "ProcedureItems";
    private static final String SECTION_BASE_PATH = "Section";
    private static final String QUIZ_BASE_PATH = "Quiz";
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TUTORIALS_BASE_PATH);
    public static final Uri CHAPTERS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + CHAPTERS_BASE_PATH);
             public static final Uri SECTIONS_URI 	= Uri.parse("content://" + AUTHORITY + "/" + SECTIONS_BASE_PATH);
    public static final Uri QUESTIONS_URI 	= Uri.parse("content://" + AUTHORITY + "/" + QUESTIONS_BASE_PATH);
    public static final Uri PROCEDURES_URI = Uri.parse("content://" + AUTHORITY
    		+ "/" + PROCEDURES_BASE_PATH);
    public static final Uri PROCEDUREITEMS_URI = Uri.parse("content://" + AUTHORITY
    		+ "/" + PROCEDUREITEMS_BASE_PATH);
    public static final Uri SECTION_URI = Uri.parse("content://" + AUTHORITY
    		+ "/" + SECTION_BASE_PATH);
    public static final Uri QUIZ_URI = Uri.parse("content://" + AUTHORITY
    		+ "/" + QUIZ_BASE_PATH);
    
    
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mt-tutorial";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mt-tutorial";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final String DEBUG_TAG = "PrivacyAppProvider";
    static {
        sURIMatcher.addURI(AUTHORITY, TUTORIALS_BASE_PATH, TUTORIALS);
        sURIMatcher.addURI(AUTHORITY, TUTORIALS_BASE_PATH + "/#", TUTORIAL_ID);
        sURIMatcher.addURI(AUTHORITY, CHAPTERS_BASE_PATH, CHAPTERS);
        sURIMatcher.addURI(AUTHORITY, CHAPTERS_BASE_PATH + "/#", CHAPTER_ID);
        sURIMatcher.addURI(AUTHORITY, SECTIONS_BASE_PATH, SECTIONS);
        sURIMatcher.addURI(AUTHORITY, SECTIONS_BASE_PATH + "/#", SECTIONS_ID);
        sURIMatcher.addURI(AUTHORITY, QUESTIONS_BASE_PATH, QUESTIONS);
        sURIMatcher.addURI(AUTHORITY, QUESTIONS_BASE_PATH + "/#", QUESTION_ID);
        sURIMatcher.addURI(AUTHORITY, PROCEDURES_BASE_PATH, PROCEDURES);
        sURIMatcher.addURI(AUTHORITY, PROCEDURES_BASE_PATH + "/#", PROCEDURES_ID);
        sURIMatcher.addURI(AUTHORITY, PROCEDUREITEMS_BASE_PATH, PROCEDUREITEMS);
        sURIMatcher.addURI(AUTHORITY, PROCEDUREITEMS_BASE_PATH + "/#", PROCEDUREITEMS);
        sURIMatcher.addURI(AUTHORITY, SECTION_BASE_PATH, SECTION);
        sURIMatcher.addURI(AUTHORITY, SECTION_BASE_PATH + "/#", SECTION_ID);
        sURIMatcher.addURI(AUTHORITY, QUIZ_BASE_PATH, QUIZ);
        sURIMatcher.addURI(AUTHORITY, QUIZ_BASE_PATH + "/#", QUIZ_ID);
        sURIMatcher.addURI(AUTHORITY, QUIZ_BASE_PATH + "/SEC/#", QUIZ_SECTION);
        
    }

    @Override
    public boolean onCreate() {
        mDB = new PrivacyAppDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        
        int uriType = sURIMatcher.match(uri);
        
        //Figure out Table
        switch (uriType) {
        case TUTORIAL_ID:
        case TUTORIALS:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_TUTORIALS);
			break;
        case CHAPTER_ID:
        case CHAPTERS:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_CHAPTERS);
        	break;
        case SECTIONS_ID:
        case SECTIONS:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_SECTIONS);
        	break;
        case QUESTION_ID:
        case QUESTIONS:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_QUESTIONS);
        	break;
        case PROCEDURES:
        case PROCEDURES_ID:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_PROCEDURES);
        	break;
        case PROCEDUREITEMS:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_PROCEDUREITEMS);
        	break;
        case SECTION:
        case SECTION_ID:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_SECTION);
        	break;
        case QUIZ:
        case QUIZ_SECTION:
        	queryBuilder.setTables(PrivacyAppDatabase.TABLE_QUIZ);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        
        //Figure out the filter
        switch (uriType) {
        case TUTORIAL_ID:
        	queryBuilder.appendWhere(PrivacyAppDatabase.ID + "="
                    + uri.getLastPathSegment());
            break;
        case TUTORIALS:
            break;
        case CHAPTER_ID:
        	queryBuilder.appendWhere("chapterID" + "="
                    + uri.getLastPathSegment());
            break;
        case CHAPTERS:
            break;
        case SECTIONS_ID:
        	queryBuilder.appendWhere("parentID" + "="
                    + uri.getLastPathSegment());
        	break;
        case SECTIONS:
        	//queryBuilder.appendWhere("parentID" + " IS NULL");
        	break;
        case QUESTION_ID:
        	queryBuilder.appendWhere("sectionID" + "="
        			+ uri.getLastPathSegment());
        	break;
        case QUESTIONS:
        	break;
        case PROCEDURES:
        case PROCEDURES_ID:
        	break;
        case PROCEDUREITEMS:
        	queryBuilder.appendWhere("procedureID" + "=" + uri.getLastPathSegment());
        	break;
        case SECTION:
        	break;
        case SECTION_ID:
        	queryBuilder.appendWhere(PrivacyAppDatabase.COL_SEC_ID + "="
                    + uri.getLastPathSegment());
        	break;
        case QUIZ:
        	break;
        case QUIZ_SECTION:
        	queryBuilder.appendWhere("secID" + "="
        			+ uri.getLastPathSegment());
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }

     
        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsAffected = 0;
        switch (uriType) {
        case TUTORIALS:
            rowsAffected = sqlDB.delete(PrivacyAppDatabase.TABLE_TUTORIALS,
                    selection, selectionArgs);
            break;
        case TUTORIAL_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsAffected = sqlDB.delete(PrivacyAppDatabase.TABLE_TUTORIALS,
                        PrivacyAppDatabase.ID + "=" + id, null);
            } else {
                rowsAffected = sqlDB.delete(PrivacyAppDatabase.TABLE_TUTORIALS,
                        selection + " and " + PrivacyAppDatabase.ID + "=" + id,
                        selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case TUTORIALS:
            return CONTENT_TYPE;
        case TUTORIAL_ID:
            return CONTENT_ITEM_TYPE;
        default:
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        String tableName = "";
        switch(uriType)
        {
        case TUTORIALS:
        	tableName = PrivacyAppDatabase.TABLE_TUTORIALS;
        	break;
        case PROCEDURES:
        	tableName = PrivacyAppDatabase.TABLE_PROCEDURES;
        	break;
        default:
        	throw new IllegalArgumentException("Invalid URI for insert");
        }

        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        try {
            long newID = sqlDB.insertOrThrow(tableName,
                    null, values);
            if (newID > 0) {
                Uri newUri = ContentUris.withAppendedId(uri, newID);
                getContext().getContentResolver().notifyChange(uri, null);
                return newUri;
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
        } catch (SQLiteConstraintException e) {
            Log.i(DEBUG_TAG, "Ignoring constraint failure.");
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsAffected;
        String id;
        StringBuilder modSelection;

        switch (uriType) {
        case TUTORIAL_ID:
            id = uri.getLastPathSegment();
            modSelection = new StringBuilder(PrivacyAppDatabase.ID
                    + "=" + id);

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }

            rowsAffected = sqlDB.update(PrivacyAppDatabase.TABLE_TUTORIALS,
                    values, modSelection.toString(), null);
            break;
        case TUTORIALS:
            rowsAffected = sqlDB.update(PrivacyAppDatabase.TABLE_TUTORIALS,
                    values, selection, selectionArgs);
            break;
        case QUESTION_ID:
        	id = uri.getLastPathSegment();
             modSelection = new StringBuilder("questionID"
                    + "=" + id);

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }
            rowsAffected = sqlDB.update(PrivacyAppDatabase.TABLE_QUESTIONS, values, modSelection.toString(), selectionArgs);
        	break;
        case SECTION_ID:
        	id = uri.getLastPathSegment();
        	modSelection = new StringBuilder("secID=" + id);
        	
        	if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }
        	
        	rowsAffected = sqlDB.update(PrivacyAppDatabase.TABLE_SECTION, values, modSelection.toString(), selectionArgs);
        	break;
        case QUIZ_ID:
        	id = uri.getLastPathSegment();
        	modSelection = new StringBuilder("quizID" + "=" + id);
        	
        	if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }
        	
        	rowsAffected = sqlDB.update(PrivacyAppDatabase.TABLE_QUIZ, values, modSelection.toString(), selectionArgs);
        	break;
        	
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}
