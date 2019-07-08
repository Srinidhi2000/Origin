package com.example.android.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.dictionary.history_data.history_Contract;

public class history_adapter extends CursorAdapter {
    public history_adapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_listitem,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView word=(TextView)view.findViewById(R.id.wordSearched);
        int wordIndex=cursor.getColumnIndex(history_Contract.HistoryEntry.c1Word);
        String wordsearched=cursor.getString(wordIndex);
        word.setText(wordsearched);

    }
}
