package com.example.activity.safezoneparent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tien on 01-Dec-15.
 */
public class Route {

    public static final String TIME_FROM = "time_from";
    public static final String TIME_TO = "time_to";
    public static final String ADDRESS = "address";  //name
    public static final String RADIUS = "radius";
    public static final String TABLE_ROUTE = "Route";
    public static final String ID = "id";
    public static final String LATITUTE = "latitude";
    public static final String LONGITUTE = "longitude";
    public static final String CHILDREN_USER = "children_user";

    String timeFrom;
    String timeTo;
    String address; //name
    String radius;
    String dLatitute;
    String dLongtitute;

    public Route(String timeFrom, String timeTo, String address, String radius, String dLatitute, String dLongtitute, String childrenName, int id) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.address = address;
        this.radius = radius;
        this.dLatitute = dLatitute;
        this.dLongtitute = dLongtitute;
        this.childrenName = childrenName;
        this.id = id;
    }

    public Route() {
    }

    public String getChildrenName() {
        return childrenName;
    }

    public void setChildrenName(String childrenName) {
        this.childrenName = childrenName;
    }

    String childrenName;

    public String getdLatitute() {
        return dLatitute;
    }

    public void setdLatitute(String dLatitute) {
        this.dLatitute = dLatitute;
    }

    public String getdLongtitute() {
        return dLongtitute;
    }

    public void setdLongtitute(String dLongtitute) {
        this.dLongtitute = dLongtitute;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;




    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public static final Date convertStringToDate(String dateStr){
        Date d  = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MM-yyyy hh:mm");
        try {
            d = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }
}
