package com.example.android.dictionary;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.dictionary.history_data.history_Contract;
//To display the history of words seen
public class history extends AppCompatActivity implements LoaderCallbacks<Cursor> {
private static final int WORD_LOADER=1;
history_adapter adapter;
ListView listView;
TextView emptystate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showhistory);
        listView=(ListView) findViewById(R.id.list);
        emptystate=(TextView)findViewById(R.id.emptystate);
        listView.setEmptyView(emptystate);
adapter=new history_adapter(this,null);
listView.setAdapter(adapter);
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int wordIndex=adapter.getCursor().getColumnIndex(history_Contract.HistoryEntry.c1Word);
        String word=adapter.getCursor().getString(wordIndex);
        Intent intent=new Intent(history.this,MainActivity.class);
        intent.putExtra("history_word",word);
        Uri currenturi=ContentUris.withAppendedId(history_Contract.HistoryEntry.CONTENT_URI,id);
        intent.setData(currenturi);
        startActivity(intent);
    }
});
getLoaderManager().initLoader(WORD_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={
                history_Contract.HistoryEntry.rowid,
                history_Contract.HistoryEntry.c1Word
        };
        return new CursorLoader(this,history_Contract.HistoryEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
adapter.swapCursor(null);
    }

  }
