package com.example.android.dictionary.history_data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final  class history_Contract {
    private history_Contract(){}
    public static final String CONTENT_AUTHORITY="com.example.android.dictionary";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH="history";
    public static class HistoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH);
        public static final String CONTENT_LIST=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH;
        public static final String CONTENT_ITEM=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH;
        public static final String rowid=BaseColumns._ID;
        public static final String TABLE_NAME="ShowHistory";
        public static final String c1Word="WORD";

    }
}
