package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/* Do not import this statement
import android.support.v4.content.AsyncTaskLoader;
*/

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String LOG_TAG=EarthquakeLoader.class.getName();
    private String mUrl;
    public EarthquakeLoader(Context context,String url) {
        super(context);
        mUrl=url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG,"onStartLoading() exectued....");

    }
    /**
     * This is on a background thread.
     */
    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        Log.v(LOG_TAG,"loadInBackground() exectued....");
        return earthquakes;
    }
}