package com.example.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
//import androidx.loader.content.AsyncTaskLoader;
//import android.support.v4.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    String url;
    public BookLoader(Context context, String... requestUrls)
    {
        super(context);
        if(requestUrls[0] == null || requestUrls.length < 1)
        {
            Log.e("Url Error", "BookLoader: Url Error");
            return;
        }
        url = requestUrls[0];
    }
    @Nullable
    @Override
    public List<Book> loadInBackground() {
        ArrayList<Book> books = QueryUtils.fetchBookData(url);
        return books;
    }
}
