package com.example.android.dictionary.history_data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class history_provider extends ContentProvider {
    private history_helper dbhelper;
    private static final int WORD=1;
    private static final int WORD_ITEM=2;
    private static final UriMatcher urimatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        urimatcher.addURI(history_Contract.CONTENT_AUTHORITY,history_Contract.PATH,WORD);
        urimatcher.addURI(history_Contract.CONTENT_AUTHORITY,history_Contract.PATH+"/#",WORD_ITEM);
    }
    @Override
    public boolean onCreate() {
        dbhelper=new history_helper(getContext());
        return true;
    }
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase database=dbhelper.getReadableDatabase();
        Cursor cursor;
        int match=urimatcher.match(uri);
        switch(match)
        {
            case WORD:cursor=database.query(history_Contract.HistoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WORD_ITEM:
                selection=history_Contract.HistoryEntry.rowid+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(history_Contract.HistoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:throw new IllegalArgumentException("Cannot query"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    
    @Override
    public String getType( @NonNull Uri uri) {
        final int match=urimatcher.match(uri);
        switch(match)
        {   case WORD:
                return history_Contract.HistoryEntry.CONTENT_LIST;
            case WORD_ITEM:
                return history_Contract.HistoryEntry.CONTENT_ITEM;
            default:
                throw new IllegalStateException("Unknown URI");
        }
    }

    
    @Override
    public Uri insert( @NonNull Uri uri,  ContentValues values)
    {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        long id=database.insert(history_Contract.HistoryEntry.TABLE_NAME,null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
     }

    @Override
    public int delete( @NonNull Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase database=dbhelper.getWritableDatabase();
        selection=history_Contract.HistoryEntry.rowid+"=?";
        selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
        int rows=database.delete(history_Contract.HistoryEntry.TABLE_NAME,selection,selectionArgs);
        if(rows!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rows;
    }

    @Override
    public int update( @NonNull Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
