package com.example.android.dictionary;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.dictionary.history_data.history_Contract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
//To display the etymology of a word searched
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Dic_parameters>  {
 ProgressBar loading;
 EditText search;
 int cnt=0;
    String checkvalue;
 Boolean isconnected=false;
 TextView search_result,display_etymology,remove;
 LinearLayout etymology_layout;
 private static final int WORD_LOADER_ID=1;
 private  String WORD_URL="null";
 private Uri currenturi=null;
 public static final String WORDID="wordId";
 public ArrayList<String> wordsadded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = findViewById(R.id.loading);
        etymology_layout=findViewById(R.id.etymology_layout);
        search = findViewById(R.id.search_edittext);
        display_etymology=findViewById(R.id.display_etymology);
        search_result = findViewById(R.id.search_result);
        loading.setVisibility(View.GONE);
        etymology_layout.setVisibility(View.GONE);
        remove=findViewById(R.id.remove);
        remove.setVisibility(View.GONE);
        wordsadded=new ArrayList<>();
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        if (active != null && active.isConnected())
        {isconnected=true;
          Intent intent=getIntent();
           checkvalue=intent.getStringExtra("history_word");
            currenturi = intent.getData();
            loadData();
            if(checkvalue!=null)
          {
              search.setText(checkvalue);
              getword();
              }
        }
        if(!isconnected)
        {
            loading.setVisibility(View.GONE);
            search_result.setText("NO INTERNET CONNECTION");
        }
search.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        }


    @Override
    public void afterTextChanged(Editable s)
    {
        etymology_layout.setVisibility(View.GONE);
        search_result.setVisibility(View.VISIBLE);
        search_result.setText(R.string.intro);
        remove.setVisibility(View.GONE);
    }
});

    }

    public void findword(View view)
    {
        getword();
    }
    private void getword()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        etymology_layout.setVisibility(View.GONE);
        if (active != null && active.isConnected())
        {
            loading.setVisibility(View.VISIBLE);
            createstring();
            LoaderManager loaderManager=getLoaderManager();
            loaderManager.restartLoader(WORD_LOADER_ID,null, this );

        }else {
            loading.setVisibility(View.GONE);
            search_result.setText("NO INTERNET CONNECTION");

        }

    }
private void createstring()
{
    final String language="en-gb";
    final String word=search.getText().toString();
    final String fields="etymologies";
    final String strictMatch="false";
    final String word_id=word.toLowerCase();
    WORD_URL="https://od-api.oxforddictionaries.com/api/v2/entries/"+language + "/" + word_id
            + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
}
public void removeWord(View view)
{  if(currenturi!=null)
{int rowsdeleted=getContentResolver().delete(currenturi,null,null);
for(int i=0;i<wordsadded.size();i++)
{
   if(wordsadded.get(i).equals(search.getText().toString()))
    {
        wordsadded.remove(i);
    }
}
saveData();
finish();
}

}
    @Override
    public Loader<Dic_parameters> onCreateLoader(int id, Bundle args) {
        return new word_loader(getApplicationContext(),WORD_URL);

    }

    @Override
    public void onLoadFinished(Loader<Dic_parameters> loader, Dic_parameters data) {
        loading.setVisibility(View.GONE);
        etymology_layout.setVisibility(View.VISIBLE);
        search_result.setText("");
        display_etymology.setText("No info found");
        if(data!=null)
        {  if(checkvalue!=null)
            remove.setVisibility(View.VISIBLE);
            display_etymology.setText(data.getEtimology());
           for(int i=0;i<wordsadded.size();i++) {
               if (wordsadded.get(i).equals(search.getText().toString())) {
                   cnt++;
               }
           }
           if(cnt==0)
           { ContentValues values=new ContentValues();
               values.put(history_Contract.HistoryEntry.c1Word,search.getText().toString());
               Uri newuri=getContentResolver().insert(history_Contract.HistoryEntry.CONTENT_URI,values);
               wordsadded.add(search.getText().toString());
                saveData();}
        }

    }

    @Override
    public void onLoaderReset(Loader<Dic_parameters> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.view_history)
        {
            Intent intent=new Intent(this,history.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
public void saveData()
{
    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    Gson gson=new Gson();
    String json=gson.toJson(wordsadded);
    editor.putString(WORDID,json);
    editor.apply();
}
public void loadData()
{
    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    Gson gson=new Gson();
    String json=sharedPreferences.getString(WORDID,null);
    Type type=new TypeToken<ArrayList<String>>(){}.getType();
    wordsadded=gson.fromJson(json,type);
    if(wordsadded==null)
    {
        wordsadded=new ArrayList<>();
    }
}
}
