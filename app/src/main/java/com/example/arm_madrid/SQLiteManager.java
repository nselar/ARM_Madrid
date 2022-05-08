package com.example.arm_madrid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SQLiteManager extends SQLiteOpenHelper {

    public static class LocationData implements BaseColumns{
        public static final String TABLE_NAME = "LocationData";
        public static final String COLUMN_NAME_PLACE = "Place";
        public static final String COLUMN_NAME_COMMENT = "Comment";
        public static final String COLUMN_NAME_LATITUDE = "Latitude";
        public static final String COLUMN_NAME_LONGITUDE = "Longitude";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ARMaps.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationData.TABLE_NAME + " (" +
                    LocationData._ID + " INTEGER PRIMARY KEY," +
                    LocationData.COLUMN_NAME_PLACE + " TEXT," +
                    LocationData.COLUMN_NAME_COMMENT + " TEXT," +
                    LocationData.COLUMN_NAME_LATITUDE + " DOUBLE," +
                    LocationData.COLUMN_NAME_LONGITUDE + " DOUBLE)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + LocationData.TABLE_NAME;

    private SQLiteDatabase db;

    public SQLiteManager(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void saveData(String place, String comment, double latitude, double longitude){

        ContentValues values = new ContentValues();
        values.put(LocationData.COLUMN_NAME_PLACE, place);
        values.put(LocationData.COLUMN_NAME_COMMENT, comment);
        values.put(LocationData.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationData.COLUMN_NAME_LONGITUDE, longitude);

        db.insert(LocationData.TABLE_NAME, null, values);
    }

    public Cursor getAllData(){

        String sortOrder = LocationData.COLUMN_NAME_PLACE + " ASC";

        Cursor cursor = db.query(
                LocationData.TABLE_NAME,     // The table to query
                null,                // The array of columns to return (pass null to get all)
                null,                // The columns for the WHERE clause
                null,             // The values for the WHERE clause
                null,                // don't group the rows
                null,                 // don't filter by row groups
                sortOrder                    // The sort order
        );

        return cursor;
    }

    public Cursor getPlace(long id){

        String selection = LocationData._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder = LocationData.COLUMN_NAME_PLACE + " ASC";

        Cursor cursor = db.query(
                LocationData.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        return cursor;
    }

    public void deletePlace(long id){

        String selection = LocationData._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(LocationData.TABLE_NAME, selection, selectionArgs);

    }

    public void editPlace(long id, String place, String comment, double latitude, double longitude){

        ContentValues values = new ContentValues();
        values.put(LocationData.COLUMN_NAME_PLACE, place);
        values.put(LocationData.COLUMN_NAME_COMMENT, comment);
        values.put(LocationData.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationData.COLUMN_NAME_LONGITUDE, longitude);

        String selection = LocationData._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(LocationData.TABLE_NAME,values,selection,selectionArgs);

    }
}
