package com.example.android.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUri;

    public Earthquake(double magnitude,String location,long timiInMilliSeconds,String uri){
        mMagnitude=magnitude;
        mLocation=location;
        mTimeInMilliseconds=timiInMilliSeconds;
        mUri =uri;
    }
    public double getMagnitude(){
        return mMagnitude;
    }
    public String getLocation(){
        return mLocation;
    }
    public long getTimeInMilliSeconds(){
        return mTimeInMilliseconds;
    }
    public String getUri(){return mUri;}
}
