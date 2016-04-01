package com.example.activity.safezoneparent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tien on 24-Mar-16.
 */
public class ChildHelper {
    private DBHelpers dbHelper;

    public ChildHelper(Context context) {
        dbHelper = new DBHelpers(context);
    }

    public long insert(Child child) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Child.CHILD_FULLNAME, child.getChild_fullname());
        contentValues.put(Child.CHILD_NICKNAME, child.getChild_nickname());
        contentValues.put(Child.CHILD_AGE, child.getAge());
        contentValues.put(Child.CHILD_GRADE, child.getGrade());
        contentValues.put(Child.CHILD_GRADE, child.getGrade());
        contentValues.put(Child.IMAGE, child.getImage());
        contentValues.put(Child.GENDER, child.getGender());

        long id = sqLiteDatabase.insert(Child.TABLE_CHILD, null, contentValues);
        sqLiteDatabase.close();
        Log.d("INSERT", "insert: " + id);
        return id;
    }

    // get a note in a line database by cursor
    public Child fetch(Cursor cursor) {
        // create new note and set its value
        Child child = new Child();
        child.setChild_fullname(cursor.getString(cursor.getColumnIndex(Child.CHILD_FULLNAME)));
        child.setChild_nickname(cursor.getString(cursor.getColumnIndex(Child.CHILD_NICKNAME)));
        child.setAge(cursor.getInt(cursor.getColumnIndex(Child.CHILD_AGE)));
        child.setGrade(cursor.getInt(cursor.getColumnIndex(Child.CHILD_GRADE)));
        child.setImage(cursor.getBlob(cursor.getColumnIndex(Child.IMAGE)));
        child.setId(cursor.getInt(cursor.getColumnIndex(Child.ID)));
        child.setGender(cursor.getInt(cursor.getColumnIndex(Child.GENDER)));
        return child;
    }

    public Child getChild(String nickame) {
        Child child = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query(Child.TABLE_CHILD, null, "Nickname = ?", new String[]{nickame}, null, null, null);
        Cursor cursor = db.query(Child.TABLE_CHILD, null, "Nickname=?", new String[]{nickame}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                child = fetch(cursor);
            }
        }

//        Log.d("A", "" + child.getChild_fullname());
        return child;
    }

    // get all element note in database
    public List<Child> getAll() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Child.TABLE_CHILD, null, null, null, null, null, null);
        // create new list of Note
        List<Child> childList = new ArrayList<>();
        // check if cursor != null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // add a note get in a line into list of note
//                    notes.add(fetch(cursor));
                    childList.add(0, fetch(cursor));
                } while (cursor.moveToNext());
            }
        }
        return childList; // return list note
    }
}
