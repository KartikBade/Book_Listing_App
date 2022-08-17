package com.example.booklistingapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<Book> {

    public BookListAdapter(@NonNull Context context, @NonNull ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView authorTextView = listItemView.findViewById(R.id.author_text_view);
        TextView titleTextView = listItemView.findViewById(R.id.title_text_view);
        ImageView imageView = listItemView.findViewById(R.id.imageView);

        authorTextView.setText(currentBook.getAuthor());
        titleTextView.setText(currentBook.getTitle());
        imageView.setImageBitmap(currentBook.getImage());

        return listItemView;
    }
}
