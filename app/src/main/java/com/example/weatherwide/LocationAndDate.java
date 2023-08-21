package com.example.weatherwide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//This class keeps track of current location and date
public class LocationAndDate {
    //Attributes
    private String m_location;
    private long m_date;

    //Extract Date From Unix Time stamp
    private String todayData(){
        String today = null;
        Date date = new Date(TimeUnit.SECONDS.toMillis(m_date));
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM");
        today = format.format(date);
        return today;
    }

    //Constructor
    public LocationAndDate(String location, long date){
        m_location = location;
        m_date = date;
    }

    //Getters
    public String getLocation(){ return m_location; }
    public String getDate(){ return todayData(); }
}
