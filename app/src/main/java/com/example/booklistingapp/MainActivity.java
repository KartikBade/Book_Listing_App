package com.example.booklistingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static EditText searchTextView;
    private static ImageView searchButton;
    private static String query;
    private static String temp;
    private static String url;
    private BookListAdapter bookListAdapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTextView = findViewById(R.id.search_text_view);
        searchButton = findViewById(R.id.search_button);
        emptyTextView = findViewById(R.id.empty_text_view);
        progressBar = findViewById(R.id.progress_bar);
        final ListView mainList = findViewById(R.id.main_list);
        url = null;
        query = null;
        temp = searchTextView.getText().toString();

        progressBar.setVisibility(View.GONE);
        mainList.setEmptyView(emptyTextView);
        emptyTextView.setText("Get a variety of books!");

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        if(connectivityManager.getActiveNetworkInfo() == null)
        {
            progressBar.setVisibility(View.GONE);
            mainList.setEmptyView(emptyTextView);
            emptyTextView.setText("No Internet Connection!");
            return;
        }

        bookListAdapter = new BookListAdapter(this, new ArrayList<Book>());
        mainList.setAdapter(bookListAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query = searchTextView.getText().toString();
                if(!query.equals(temp) && query != null)
                {
                    bookListAdapter.clear();
                    emptyTextView.setText(null);
                    progressBar.setVisibility(View.VISIBLE);
                    temp = query;
                    url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
                    getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
                }
            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView titleTextView = view.findViewById(R.id.title_text_view);
                String query = (String) titleTextView.getText();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        emptyTextView.setText("No Books Found!");
        bookListAdapter.clear();
        if(books != null && !books.isEmpty())
        {
            bookListAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookListAdapter.clear();
    }
}