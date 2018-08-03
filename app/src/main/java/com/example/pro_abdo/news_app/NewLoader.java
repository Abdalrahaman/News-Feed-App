package com.example.pro_abdo.news_app;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewLoader extends AsyncTaskLoader<List<New>> {

    private static final String LOG_TAG = NewLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;


    public NewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG, "onStartLoading");

        forceLoad();
    }

    @Override
    public List<New> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of News.
        List<New> news = QueryUtils.fetchNewData(mUrl);
        return news;
    }
}
