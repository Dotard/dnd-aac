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

public class aacProvider extends ContentProvider {

    private aacDatabase mDB;

    private static final String AUTHORITY = "com.dnd.aac.data.aacProvider";
    public static final int CATEGORYS = 100;
    public static final int CATEGORYS_ID = 110;
    public static final int IMAGES = 200;
    public static final int IMAGES_ID = 210;
    public static final int ITEMS = 300;
    public static final int ITEMS_INCATEGORYID = 310;

    private static final String CATEGORYS_BASE_PATH = "Categorys";
    private static final String IMAGES_BASE_PATH = "Images";
    private static final String ITEMS_BASE_PATH = "Items";
    
    public static final Uri CATEGORYS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + CATEGORYS_BASE_PATH);
    public static final Uri IMAGES_URI = Uri.parse("content://" + AUTHORITY
            + "/" + IMAGES_BASE_PATH);
    public static final Uri ITEMS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + ITEMS_BASE_PATH);
    
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mt-tutorial";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mt-tutorial";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final String DEBUG_TAG = "aacDatabase";
    static {
        sURIMatcher.addURI(AUTHORITY, CATEGORYS_BASE_PATH, CATEGORYS);
        sURIMatcher.addURI(AUTHORITY, CATEGORYS_BASE_PATH + "/#", CATEGORYS_ID);    
        sURIMatcher.addURI(AUTHORITY, IMAGES_BASE_PATH, IMAGES);
        sURIMatcher.addURI(AUTHORITY, IMAGES_BASE_PATH + "/#", IMAGES_ID);
        sURIMatcher.addURI(AUTHORITY, ITEMS_BASE_PATH + "/#", ITEMS_INCATEGORYID);  
    }

    @Override
    public boolean onCreate() {
        mDB = new aacDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        
        int uriType = sURIMatcher.match(uri);
        
        //Figure out Table
        switch (uriType) {
        case CATEGORYS_ID:
        case CATEGORYS:
        	queryBuilder.setTables("Items " +
        			" INNER JOIN Images ON ITEMS.imageID = IMAGES.imageID" +
        			" INNER JOIN Subcategorys_Items ON ITEMS.itemID = Subcategorys_Items.itemID");
			break;
        case IMAGES:
        case IMAGES_ID:
        	queryBuilder.setTables("Images");
        	break;
        case ITEMS_INCATEGORYID:
        	queryBuilder.setTables("Items " +
        			" INNER JOIN Images ON ITEMS.imageID = IMAGES.imageID" +
        			" INNER JOIN Subcategorys_Items ON ITEMS.itemID = Subcategorys_Items.itemID");
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        
        //Figure out the filter
        switch (uriType) {
        case CATEGORYS_ID:
        	queryBuilder.appendWhere("Subcategorys_Items.subcategoryID IN " +
        			"(SELECT Subcategorys.subcategoryID" +
        			" from Subcategorys WHERE Subcategorys.categoryID = " +
        			uri.getLastPathSegment() + ")" );
            break;
        case CATEGORYS:
        	//queryBuilder.appendWhere("parentID" + " IS NULL");
        	break;
        case IMAGES:
        	break;
        case IMAGES_ID:
        	queryBuilder.appendWhere("imageID" + "="
        			+ uri.getLastPathSegment());
        	break;
        case ITEMS_INCATEGORYID:
        	queryBuilder.appendWhere("Subcategorys_Items.subcategoryID" + "="
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
        case CATEGORYS:
            rowsAffected = sqlDB.delete("Categorys",
                    selection, selectionArgs);
            break;
        case CATEGORYS_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsAffected = sqlDB.delete("Categorys",
                		"categoryID" + "=" + id, null);
            } else {
                rowsAffected = sqlDB.delete("Categorys",
                        selection + " and " + "categoryID" + "=" + id,
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
        case CATEGORYS:
            return CONTENT_TYPE;
        case CATEGORYS_ID:
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
        case CATEGORYS:
        	tableName = "Categorys";
        	break;
        case CATEGORYS_ID:
        	tableName = "Categorys";
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
        case CATEGORYS_ID:
            id = uri.getLastPathSegment();
            modSelection = new StringBuilder("categoryID"
                    + "=" + id);

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }

            rowsAffected = sqlDB.update("Categorys",
                    values, modSelection.toString(), null);
            break;
        case CATEGORYS:
            rowsAffected = sqlDB.update("Categorys",
                    values, selection, selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}
