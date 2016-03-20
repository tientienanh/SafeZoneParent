package com.example.activity.safezoneparent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tien on 22-Oct-15.
 */
public class DBHelpers extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "route.db";
    public DBHelpers(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ROUTE = "CREATE TABLE " + Route.TABLE_ROUTE + "( " +
                Route.ID + " integer primary key,"+
                Route.CHILDREN_NAME + " text," +
                Route.TIME_FROM + " text," +
                Route.TIME_TO + " text," +
                Route.LOCATION + " text," +
                Route.RADIUS + " text," +
                Route.D_LATITUTE + " text," +
                Route.D_LONGITUTE + " text " + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
