package com.example.android.dictionary;

import android.content.AsyncTaskLoader;
import android.content.Context;


public class word_loader extends AsyncTaskLoader<Dic_parameters> {
    private String url;

    public word_loader(Context context,String givenUrl) {
        super(context);
        url=givenUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Dic_parameters loadInBackground() {
        if(url==null)
            return null;
        Dic_parameters dic_parameters=Connect.Fetch_word(url);
        return dic_parameters;
    }

}
