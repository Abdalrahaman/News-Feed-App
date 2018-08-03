package com.example.pro_abdo.news_app;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getName();


    public QueryUtils() {

    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link New} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<New> extractFeatureFromJson(String NewJSON) {

        String[] author_name = null;

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(NewJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<New> News = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(NewJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or news).
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");

            JSONArray NewArray = jsonResponse.getJSONArray("results");

            // For each new in the newArray, create an {@link New} object
            for (int i = 0; i < NewArray.length(); i++) {

                // Get a single new at position i within the list of news
                JSONObject currentNew = NewArray.getJSONObject(i);


                // Extract the value for the key called "sectionName"
                String section_name = currentNew.getString("sectionName");

                // Extract the value for the key called "webTitle"
                String title = currentNew.getString("webTitle");

                // Extract the value for the key called "PublicationDate"
                String DateAndtime = currentNew.getString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentNew.getString("webUrl");

                JSONArray authorArray = currentNew.getJSONArray("tags");
                if (authorArray.length() > 0) {

                    author_name = new String[authorArray.length()];

                    for (int j = 0; j < authorArray.length(); j++) {

                        JSONObject currentAuthor = authorArray.getJSONObject(j);

                        author_name[j] = currentAuthor.getString("webTitle");

                    }

                }

                New aNew = new New(section_name, title, DateAndtime, url, author_name);

                // Add the new {@link New} to the list of News.
                News.add(aNew);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return News;
    }

    /**
     * Query the USGS dataset and return a list of {@link New} objects.
     */
    public static List<New> fetchNewData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "fetchEarthquakeData");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link New}s
        List<New> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link New}s
        return earthquakes;
    }
}
