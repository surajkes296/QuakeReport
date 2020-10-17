package com.example.android.quakereport;

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
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

     private static final String LOG_TAG= QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    /**Method to tie up all the below functions*/
    public static List<Earthquake> fetchEarthquakeData(String stringurl) {
        URL url = createUrl(stringurl);
        String JSONresponse = "";
        try {
            JSONresponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in making connection", e);
        }
        List<Earthquake> list=extractFeaturesfromJson(JSONresponse);
        Log.v(LOG_TAG,"fetchEarthquakeData() exectued....");

        return list;
    }
    /*
     * @param stringUrl
     * @return
     */

    public static URL createUrl(String stringUrl){
        URL url=null;
        try{
            url=new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Url creating error",e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        String JSON="";
        if(url==null)return JSON;
        try{
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(50000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                JSON=readfromStream(inputStream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"URL Connection Error",e);
        }finally {
            if(urlConnection!=null)urlConnection.disconnect();
            if(inputStream!=null)inputStream.close();
        }

        return JSON;
    }
    private static String readfromStream(InputStream inputStream) throws IOException{
        StringBuilder output=new StringBuilder();
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
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractFeaturesfromJson(String json) {
        if(TextUtils.isEmpty(json))return null;
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            JSONObject baseJsonObj=new JSONObject(json);
            JSONArray earthquakeArray=baseJsonObj.getJSONArray("features");
            for(int i=0;i<earthquakeArray.length();i++)
            {
                JSONObject current=earthquakeArray.getJSONObject(i);
                JSONObject properties=current.getJSONObject("properties");
                Double mag=properties.getDouble("mag");
                String loc=properties.getString("place");
                String url=properties.getString("url");

                //Time converion from string->long->format in desired type
                long timeInMilliseconds=properties.getLong("time");
                Date dateObject = new Date(timeInMilliseconds);

                /*SimpleDateFormat dateFormatter = new SimpleDateFormat("DD/MMM/yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);*/

                earthquakes.add(new Earthquake(mag,loc,timeInMilliseconds,url));
            }

            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}