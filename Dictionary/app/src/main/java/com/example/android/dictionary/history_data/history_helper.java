package com.example.android.dictionary.history_data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class history_helper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="ShowHistory.db";
    public static final int DATABASE_VERSION=1;

    public history_helper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE " + history_Contract.HistoryEntry.TABLE_NAME + " ("
                +history_Contract.HistoryEntry.rowid + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +history_Contract.HistoryEntry.c1Word + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS " + history_Contract.HistoryEntry.TABLE_NAME);
    }
}
