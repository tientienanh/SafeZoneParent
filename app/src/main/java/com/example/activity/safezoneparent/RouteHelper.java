package com.example.activity.safezoneparent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tien on 04-Dec-15.
 */
public class RouteHelper {
    private DBHelpers dbHelpers;

    public RouteHelper(Context context) {
        dbHelpers = new DBHelpers(context);
    }

    public long insert(Route route) {
        SQLiteDatabase sqLiteDatabase = dbHelpers.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Route.TIME_FROM, route.getTimeFrom());
        contentValues.put(Route.TIME_TO, route.getTimeTo());
        contentValues.put(Route.LOCATION, route.getLocation());
        contentValues.put(Route.RADIUS, route.getRadius());
        contentValues.put(Route.D_LATITUTE, route.getdLatitute());
        contentValues.put(Route.D_LONGITUTE, route.getdLongtitute());
        contentValues.put(Route.CHILDREN_NAME, route.getChildrenName());
//        contentValues.put(Route.TIME_UPDATE,String.valueOf(route.getTimeUpdate()));

        long id = sqLiteDatabase.insert(Route.TABLE_ROUTE, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public void delete(int route_id) {
        SQLiteDatabase db = dbHelpers.getWritableDatabase();
        db.delete(Route.TABLE_ROUTE, Route.ID + "=" + route_id, null);
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbHelpers.getWritableDatabase();
//        db.execSQL("delete * from "+Route.TABLE_ROUTE);
        db.delete(Route.TABLE_ROUTE, null, null);
        db.close();
    }

    public void delete(Route route){

        SQLiteDatabase sqLiteDatabase = dbHelpers.getWritableDatabase();
        String str1 = route.getChildrenName();
        String str2 = route.getdLatitute();
        String str3 = route.getdLongtitute();
        String str4 = route.getRadius();
        String str5 = route.getLocation();
        String str6 = route.getTimeFrom();
        String str7 = route.getTimeTo();
        Cursor cursor = sqLiteDatabase.query(Route.TABLE_ROUTE, null, "children_name = ?", new String[]{route.getChildrenName()}, null, null, null);
        List<Route> routes = fetchAll(cursor);
        deleteAll();
        int id;
        for (int i = 0;i<routes.size();i++) {
            if (!routes.get(i).getChildrenName().equals(str1) ||
                !routes.get(i).getdLatitute().equals(str2) ||
                        !routes.get(i).getdLongtitute().equals(str3) ||
                        !routes.get(i).getRadius().equals(str4) ||
                        !routes.get(i).getLocation().equals(str5) ||
                        !routes.get(i).getTimeFrom().equals(str6) ||
                        !routes.get(i).getTimeTo().equals(str7) ) {
                insert(routes.get(i));
//                id = cursor.getInt(cursor.getColumnIndex(Route.ID));
//                delete(id);
            }
        }
    }

  /*  public void delete(Route route){
        SQLiteDatabase sqLiteDatabase = dbHelpers.getWritableDatabase();
        String str1 = route.getChildrenName();
        String str2 = String.valueOf(route.getdLatitute());
        String str3 = String.valueOf(route.getdLongtitute());
        String str4 = route.getRadius();
        String str5 = route.getLocation();
        String str6 = route.getTimeFrom();
        String str7 = route.getTimeTo();

//        Cursor cursor = sqLiteDatabase.query(Route.TABLE_ROUTE, null, null, null, null, null, null);
        Cursor cursor = sqLiteDatabase.query(Route.TABLE_ROUTE, null, Route.CHILDREN_NAME + "= ?" + " and " + Route.D_LATITUTE + "= ?" + " and "
                + Route.D_LONGITUTE + "= ?" + " and " + Route.RADIUS + "= ?" + " and " + Route.LOCATION + "= ?" + " and " +
                Route.TIME_FROM + "= ?" + " and " + Route.TIME_TO + "= ?"
                , new String[]{str1, str2, str3, str4, str5, str6, str7}, null, null, null);
        int numRow = cursor.getInt(cursor.getColumnIndex("RecNo"));
        delete(numRow);
    }*/

    public Route fetch(Cursor cursor) {
        Route route = new Route();
        route.setTimeFrom(cursor.getString(cursor.getColumnIndex(Route.TIME_FROM)));
        route.setTimeTo(cursor.getString(cursor.getColumnIndex(Route.TIME_TO)));
        route.setLocation(cursor.getString(cursor.getColumnIndex(Route.LOCATION)));
        route.setRadius(cursor.getString(cursor.getColumnIndex(Route.RADIUS)));
        route.setdLatitute(cursor.getString(cursor.getColumnIndex(Route.D_LATITUTE)));
        route.setdLongtitute(cursor.getString(cursor.getColumnIndex(Route.D_LONGITUTE)));
        route.setChildrenName(cursor.getString(cursor.getColumnIndex(Route.CHILDREN_NAME)));

//        String dateStr = cursor.getString(cursor.getColumnIndex(Route.TIME_UPDATE));

//        route.setTimeUpdate(Route.convertStringToDate(dateStr));
        return route;
    }

    public static void deleteRow(){

    }

    public List<Route> fetchAll(Cursor cursor) {
        List<Route> routeList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    routeList.add(fetch(cursor));
                } while (cursor.moveToNext());
            }
        }
        return routeList;
    }

    public List<Route> get(String childrenName) {
        SQLiteDatabase db = dbHelpers.getReadableDatabase();
//        Cursor cursor = db.query(Route.TABLE_ROUTE, null, null, null, null, null, null);
        Cursor cursor = db.query(Route.TABLE_ROUTE, null, "children_name = ?", new String[]{childrenName}, null,null, null);
        int rowNumber = cursor.getCount();
        return fetchAll(cursor);
    }

    public List<Route> get() {
        SQLiteDatabase db = dbHelpers.getReadableDatabase();
        Cursor cursor = db.query(Route.TABLE_ROUTE, null, null, null, null, null, null);
//        Cursor cursor = db.query(Route.TABLE_ROUTE, null, "children_name = ?", new String[]{childrenName}, null,null, null);
        int rowNumber = cursor.getCount();
        return fetchAll(cursor);
    }
}
