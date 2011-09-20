package com.caug.failblog.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.caug.failblog.other.ImageCache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class FailblogSQL extends BaseSQL 
{
	public FailblogSQL(SQLiteOpenHelper openHelper) 
	{
		super(openHelper);
	}

	public ImageCache getImageCache(int id)
	{
		ImageCache imageCache = null;

		String[] projection = null; // Get all columns
		String selection = ImageCache.Columns._ID + " = ?";
		String[] selectionArgs = { Integer.toString(id) };
		String sortOrder = null;

		SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
		SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
		sqLiteQueryBuilder.setTables(SQLHelper.TABLE_NAME_IMAGE_CACHE);
		
		Cursor cursor = null;
		try
		{
			cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
	
			if(cursor != null && cursor.moveToFirst())
			{
				imageCache = mapImageCache(cursor);
			}
		}finally{
			if(cursor != null)
			{
				cursor.close();
			}
		}
		
		return imageCache;
	}

	public List<ImageCache> getImageCacheList(int pageNumber, int recordsPerPage)
	{
		List<ImageCache> imageCacheList = new ArrayList<ImageCache>();

		String[] projection = null; // Get all columns
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = ImageCache.Columns._ID;

		SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
		SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
		sqLiteQueryBuilder.setTables(SQLHelper.TABLE_NAME_IMAGE_CACHE);
		
		Cursor cursor = null;
		try
		{
			cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
	
			if(cursor != null && cursor.moveToPosition((pageNumber - 1) * recordsPerPage))
			{
				imageCacheList.add(mapImageCache(cursor));
				while(cursor.moveToNext() && (recordsPerPage == 0 || imageCacheList.size() < recordsPerPage))
				{
					imageCacheList.add(mapImageCache(cursor));
				}
			}
		}finally{
			if(cursor != null)
			{
				cursor.close();
			}
		}
		
		return imageCacheList;
	}

	public void saveImageCache(int id, String name, String localImageUri, String remoteImageUri, String remoteEntryUri)
	{
		SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(ImageCache.Columns.NAME, name);
		contentValues.put(ImageCache.Columns.LOCAL_IMAGE_URI, localImageUri);
		contentValues.put(ImageCache.Columns.REMOTE_IMAGE_URI, remoteImageUri);
		contentValues.put(ImageCache.Columns.REMOTE_ENTRY_URI, remoteEntryUri);

		int rowCount = sqLiteDatabase.update(SQLHelper.TABLE_NAME_IMAGE_CACHE, contentValues, ImageCache.Columns._ID + " = ?", new String[]{ Integer.toString(id) });
		if(rowCount == 0)
		{
			contentValues.put(ImageCache.Columns.ENTERED_DATE, SQL_DATE.format(new Date(System.currentTimeMillis())));

			sqLiteDatabase.insert(SQLHelper.TABLE_NAME_IMAGE_CACHE, null, contentValues);
		}
	}

	public void deleteImageCache(int id)
	{
		SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

		String whereClause = ImageCache.Columns._ID + " = ? ";
		String[] whereArgs = new String[]{ Integer.toString(id) };
		
		sqLiteDatabase.delete(SQLHelper.TABLE_NAME_IMAGE_CACHE, whereClause, whereArgs);
	}

	private ImageCache mapImageCache(Cursor cursor)
	{
		ImageCache imageCache = new ImageCache();

		imageCache.setId(cursor.getInt(cursor.getColumnIndex(ImageCache.Columns._ID)));
		imageCache.setName(cursor.getString(cursor.getColumnIndex(ImageCache.Columns.NAME)));
		imageCache.setLocalImageUri(cursor.getString(cursor.getColumnIndex(ImageCache.Columns.LOCAL_IMAGE_URI)));
		imageCache.setRemoteImageUri(cursor.getString(cursor.getColumnIndex(ImageCache.Columns.REMOTE_IMAGE_URI)));
		imageCache.setRemoteEntryUri(cursor.getString(cursor.getColumnIndex(ImageCache.Columns.REMOTE_ENTRY_URI)));
		try{ imageCache.setEnteredDate(SQL_DATE.parse(cursor.getString(cursor.getColumnIndex(ImageCache.Columns.ENTERED_DATE)))); }catch(Exception e){}

		return imageCache;
	}
}
