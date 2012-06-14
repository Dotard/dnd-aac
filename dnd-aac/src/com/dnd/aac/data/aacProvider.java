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

import java.io.IOException;

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

	public static final String QUERY_PARAMETER_LIMIT = "limit";

	private static final String AUTHORITY = "com.dnd.aac.data.aacProvider";
	public static final int CATEGORYS = 100;
	public static final int FAVOURITES = 111;
	public static final int CATEGORYS_ID = 110;
	public static final int IMAGES = 200;
	public static final int IMAGES_ID = 210;
	public static final int PICTOS = 300;
	public static final int PICTOS_INSUBCATEGORY = 310;
	public static final int PICTOS_INCATEGORY = 320;
	public static final int SUBCATEGORYS = 400;
	public static final int SUBCATEGORYS_ID = 410;
	public static final int TRIE_SEARCH_BY_PICTOID = 500;
	public static final int TRIE_SEARCH_BY_TRIEID = 501;
	public static final int TRIE_SUGGEST_BY_TRIEID = 510;
	public static final int TRIE_INSERT = 520;
	
	public static final int PICTO_HIT = 800;
	public static final int TRIE_HIT = 801;

	private static final String CATEGORYS_BASE_PATH = CategorysTbl.table;
	private static final String IMAGES_BASE_PATH = ImagesTbl.table;
	private static final String PICTOS_BASE_PATH = PictosTbl.table;
	private static final String SUBCATEGORYS_BASE_PATH = SubcategorysTbl.table;
	private static final String PICTOTREE_BASE_PATH = PictoTreeTbl.table;

	public static final Uri CATEGORYS_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + CATEGORYS_BASE_PATH);
	public static final Uri IMAGES_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + IMAGES_BASE_PATH);
	public static final Uri PICTOS_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + PICTOS_BASE_PATH);
	public static final Uri SUBCATEGORYS_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + SUBCATEGORYS_BASE_PATH);
	
	
	public static final Uri PICTO_SEARCH_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PICTOS_BASE_PATH + "/pictos");
	public static final Uri PICTO_HIT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + PICTOS_BASE_PATH + "/hit");
	
	
	public static final Uri TRIE_PICTOID_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PICTOTREE_BASE_PATH + "/pictoID");
	public static final Uri TRIE_TRIEID_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PICTOTREE_BASE_PATH + "/trieID");
	public static final Uri TRIE_INSERT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PICTOTREE_BASE_PATH + "/insert");
	public static final Uri TRIE_SUGGEST_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + PICTOTREE_BASE_PATH + "/suggest");
	public static final Uri TRIE_HIT_URI = Uri.parse("content://" 
			+ AUTHORITY	+ "/" + PICTOTREE_BASE_PATH + "/hit");



	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/mt-tutorial";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/mt-tutorial";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	private static final String DEBUG_TAG = "aacDatabase";
	static {
		
		sURIMatcher.addURI(AUTHORITY, PICTOS_BASE_PATH + "/hit/#", PICTO_HIT);
	
		
		sURIMatcher.addURI(AUTHORITY, CATEGORYS_BASE_PATH, CATEGORYS);
		sURIMatcher.addURI(AUTHORITY, CATEGORYS_BASE_PATH + "/#", CATEGORYS_ID);
		
		sURIMatcher.addURI(AUTHORITY, IMAGES_BASE_PATH, IMAGES);
		sURIMatcher.addURI(AUTHORITY, IMAGES_BASE_PATH + "/#", IMAGES_ID);
		
		sURIMatcher.addURI(AUTHORITY, PICTOS_BASE_PATH + "/pictos", PICTOS);
		sURIMatcher.addURI(AUTHORITY, PICTOS_BASE_PATH + "/2/1", FAVOURITES);
		sURIMatcher.addURI(AUTHORITY, PICTOS_BASE_PATH + "/1/#",PICTOS_INSUBCATEGORY);
		sURIMatcher.addURI(AUTHORITY, PICTOS_BASE_PATH + "/2/#",PICTOS_INCATEGORY);
		
		sURIMatcher.addURI(AUTHORITY, SUBCATEGORYS_BASE_PATH, SUBCATEGORYS);
		sURIMatcher.addURI(AUTHORITY, SUBCATEGORYS_BASE_PATH + "/#",SUBCATEGORYS_ID);
		
		sURIMatcher.addURI(AUTHORITY, PICTOTREE_BASE_PATH + "/pictoID/#",TRIE_SEARCH_BY_PICTOID);
		sURIMatcher.addURI(AUTHORITY, PICTOTREE_BASE_PATH + "/trieID/#",TRIE_SEARCH_BY_TRIEID);
		sURIMatcher.addURI(AUTHORITY, PICTOTREE_BASE_PATH + "/insert/#",TRIE_INSERT);
		sURIMatcher.addURI(AUTHORITY, PICTOTREE_BASE_PATH + "/suggest/#", TRIE_SUGGEST_BY_TRIEID);
		sURIMatcher.addURI(AUTHORITY, PICTOTREE_BASE_PATH + "/hit/#", TRIE_HIT);

	}

	@Override
	public boolean onCreate() {
		mDB = new aacDatabase(getContext());
		try {
			mDB.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = sURIMatcher.match(uri);

		String limit = uri.getQueryParameter(QUERY_PARAMETER_LIMIT);

		// Figure out Table
		switch (uriType) {
		case TRIE_SEARCH_BY_PICTOID:
		case TRIE_SEARCH_BY_TRIEID:
		case TRIE_SUGGEST_BY_TRIEID:
		case TRIE_HIT:
			queryBuilder.setTables(PictoTreeTbl.table);
			break;
		case PICTO_HIT:
			queryBuilder.setTables(PictosTbl.table);
			break;
		case CATEGORYS_ID:
		case CATEGORYS:
			queryBuilder.setTables(CategorysTbl.table
					+ " INNER JOIN " + ImagesTbl.table +" ON " 
						+ CategorysTbl.table + "." + CategorysTbl.imageID + 
						" = " + ImagesTbl.table + "." + ImagesTbl.imageID);
			break;
		case IMAGES:
		case IMAGES_ID:
			queryBuilder.setTables(ImagesTbl.table);
			break;		
		case FAVOURITES:
			queryBuilder.setTables(PictosTbl.table
					+ " INNER JOIN " + ImagesTbl.table + " ON " 
						+ PictosTbl.table + "." + PictosTbl.imageID + 
						" = " + ImagesTbl.table + "." + ImagesTbl.imageID);
			sortOrder = PictosTbl.table + "." + PictosTbl.playCount + " DESC";
			limit = "0 , 70";
			break;
		case PICTOS_INCATEGORY:
		case PICTOS_INSUBCATEGORY:
		case PICTOS:
			queryBuilder
					.setTables(PictosTbl.table
							+ " INNER JOIN " + ImagesTbl.table + " ON " 
								+ PictosTbl.table + "." + PictosTbl.imageID + 
								" = " + ImagesTbl.table + "." + ImagesTbl.imageID
							+ " INNER JOIN " + SubcategorysPictosTbl.table + " ON " 
								+ PictosTbl.table + "." + PictosTbl.pictoID + 
								" = " + SubcategorysPictosTbl.table + "." + SubcategorysPictosTbl.pictoID);
			break;
		case SUBCATEGORYS_ID:
		case SUBCATEGORYS:
			queryBuilder.setTables(SubcategorysTbl.table
					+ " INNER JOIN " + ImagesTbl.table + " ON " 
					+ SubcategorysTbl.table + "." + SubcategorysTbl.imageID + " = " + ImagesTbl.table + "." + ImagesTbl.imageID);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		// Figure out the filter
		switch (uriType) {
		
		case CATEGORYS:
		case IMAGES:
		case PICTOS:
		case FAVOURITES:
		case SUBCATEGORYS:
					break;
		case CATEGORYS_ID:
			queryBuilder.appendWhere(CategorysTbl.categoryID + "=" + uri.getLastPathSegment());
			break;
		case TRIE_SUGGEST_BY_TRIEID:
			queryBuilder.appendWhere(PictoTreeTbl.parentTrieID + "="+ uri.getLastPathSegment());
			break;
		case IMAGES_ID:
			queryBuilder.appendWhere(ImagesTbl.imageID + "=" + uri.getLastPathSegment());
			break;
		case TRIE_SEARCH_BY_PICTOID:
		case PICTO_HIT:
			queryBuilder.appendWhere(PictosTbl.pictoID + "=" + uri.getLastPathSegment());
			break;
		case TRIE_SEARCH_BY_TRIEID:
		case TRIE_HIT:
			queryBuilder.appendWhere(PictoTreeTbl.trieID + "=" + uri.getLastPathSegment());
			break;
		
		case PICTOS_INCATEGORY:
			queryBuilder.appendWhere(SubcategorysPictosTbl.table + "." + SubcategorysPictosTbl.subcategoryID + " IN "
					+ "(SELECT " + SubcategorysTbl.table + "." + SubcategorysTbl.subcategoryID
					+ " from " + SubcategorysTbl.table + " WHERE " + SubcategorysTbl.table + "." + SubcategorysTbl.categoryID + " = "
					+ uri.getLastPathSegment() + ")");
			break;
		case PICTOS_INSUBCATEGORY:
			queryBuilder.appendWhere(SubcategorysPictosTbl.table + "." + SubcategorysPictosTbl.subcategoryID + "=" + uri.getLastPathSegment());
			break;
		case SUBCATEGORYS_ID:
			queryBuilder.appendWhere(SubcategorysTbl.subcategoryID + " = "	+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder,
				limit);
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
			rowsAffected = sqlDB.delete(CategorysTbl.table, selection, selectionArgs);
			break;
		case CATEGORYS_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(CategorysTbl.table, CategorysTbl.categoryID + "="
						+ id, null);
			} else {
				rowsAffected = sqlDB.delete(CategorysTbl.table, selection + " and "
						+ CategorysTbl.categoryID + "=" + id, selectionArgs);
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
		switch (uriType) {
		case TRIE_INSERT:
			tableName = PictoTreeTbl.table;
			break;
		case CATEGORYS:
			tableName = CategorysTbl.table;
			break;
		case CATEGORYS_ID:
			tableName = CategorysTbl.table;
			break;
		default:
			throw new IllegalArgumentException("Invalid URI for insert");
		}

		SQLiteDatabase sqlDB = mDB.getWritableDatabase();
		try {
			long newID = sqlDB.insertOrThrow(tableName, null, values);
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
		case PICTO_HIT:
			id = uri.getLastPathSegment();
			modSelection = new StringBuilder(PictosTbl.pictoID + "=" + id);

			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}

			rowsAffected = sqlDB.update(PictosTbl.table, values,
					modSelection.toString(), null);
			break;
		case TRIE_HIT:
			id = uri.getLastPathSegment();
			modSelection = new StringBuilder(PictoTreeTbl.trieID + "=" + id);

			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}

			rowsAffected = sqlDB.update(PictoTreeTbl.table, values,
					modSelection.toString(), null);
			break;
		case CATEGORYS_ID:
			id = uri.getLastPathSegment();
			modSelection = new StringBuilder(CategorysTbl.categoryID + "=" + id);

			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}

			rowsAffected = sqlDB.update(CategorysTbl.table, values,
					modSelection.toString(), null);
			break;
		case CATEGORYS:
			rowsAffected = sqlDB.update(CategorysTbl.table, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

}
