package com.example.shivani.bookshelf;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by shivani on 7/2/2017.
 */

public class QueryUtils {

    private static String jsonResponse;

    public static ArrayList<Book> fetchBookData(URL url) {
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("fetchBookData", "Problem making the HTTP request.", e);
        }
        return (extractFeatures(jsonResponse));

    }

    private static String makeHttpRequest(URL url) throws IOException {
        String json = "";
        if (url == null) {
            return json;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                json = readFromStream(inputStream);
            } else {
                Log.e("makeHttpRequest", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("makeHttpRequest", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return json;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder str = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                str.append(line);
                line = reader.readLine();
            }
        }
        return str.toString();
    }

    private static ArrayList<Book> extractFeatures(String output) {
        if (TextUtils.isEmpty(output)) {
            return null;
        }
        ArrayList<Book> answer1 = new ArrayList<>();

        try {
            JSONObject base = new JSONObject(output);
            JSONArray list1 = base.getJSONArray("items");
            for (int i = 0; i < list1.length(); i++) {
                JSONObject current = list1.getJSONObject(i);
                JSONObject info = current.getJSONObject("volumeInfo");
                JSONArray authorInfo = info.getJSONArray("authors");
                 String authorArray[] = new String[authorInfo.length()];
                for (int j = 0; j < authorInfo.length(); j++) {
                    authorArray[j] = authorInfo.getString(j);
                }
                answer1.add(new Book(info.getString("title"), authorArray));


            }
        } catch (JSONException e) {
            Log.e("extractFeatures", "Problem in parsing JSON", e);
        }
        return answer1;
    }
}

