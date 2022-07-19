package com.example.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UTFDataFormatException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public static ArrayList<Book> fetchBookData(String url) {

        URL requestUrl = createUrl(url);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e("Error", "fetchBookData: Error making http request");
        }

        ArrayList<Book> books = extractFeaturesFromJson(jsonResponse);

        return books;
    }

    static URL createUrl(String requestUrl)
    {
        URL url= null;
        try
        {
            url = new URL(requestUrl);
        }
        catch(MalformedURLException e)
        {
            Log.e("URL Error", "Corrupted URL");
        }
        return url;
    }

    static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e("HTTPConnectionError", "makeHttpRequest: " + urlConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e("HTTPConnectionError" , "makeHttpRequest: IOException");;
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<Book> extractFeaturesFromJson(String bookJson)
    {
        ArrayList<Book> books = new ArrayList<Book>();

        try {
            JSONObject jsonObject = new JSONObject(bookJson);
            JSONArray items = jsonObject.getJSONArray("items");

            for(int i=0; i<items.length(); i++)
            {
                JSONObject currentObject = items.getJSONObject(i);
                JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageLink = imageLinks.getString("smallThumbnail");

                books.add(new Book(author, title, imageLink));
            }

        } catch (JSONException e) {
            Log.e("JSON Error", "extractFeaturesFromJson: Error parsing json");
        }

        return books;
    }

    static Bitmap convertToBitmap(String imageLink)
    {
        Bitmap image = null;
        URL url = createUrl(imageLink);
        try {
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e("Convert to Bitmap", "convertToBitmap: Error converting url to bitmap " + e);
        }
        return image;
    }
}