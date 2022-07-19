package com.example.booklistingapp;

import android.graphics.Bitmap;

public class Book {

    private String author;
    private String title;
    private Bitmap image;

    Book(String authorT, String titleT, String imageT)
    {
        author = authorT;
        title = titleT;
        image = QueryUtils.convertToBitmap(imageT);
    }

    String getAuthor()
    {
        return author;
    }

    String getTitle()
    {
        return title;
    }

    Bitmap getImage()
    {
        return image;
    }

}
