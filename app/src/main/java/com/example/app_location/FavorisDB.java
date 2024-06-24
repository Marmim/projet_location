package com.example.app_location;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class FavorisDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "favorisDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "monFavoris";

    private static final String ProId = "proID";

    public FavorisDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ProId + " TEXT)";
        db.execSQL(query);
    }

    public void addFavoris(String propertyID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProId, propertyID);
        Log.d("rowid", String.valueOf(db.insert(TABLE_NAME, null, values)));
        db.close();
    }

    public void removeFavoris(String propertyID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "proID=?", new String[]{propertyID});
        db.close();
    }

    public ArrayList<String> readFavoris()
    {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        @SuppressLint("Recycle") Cursor cursorFavoris
                = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<String> proIDs
                = new ArrayList<String>();
        int i=0;
        // moving our cursor to first position.
        if (cursorFavoris.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                Log.i("favorisdb",(i++)+"found");
                Log.i("favorisdbname",cursorFavoris.getString(0));
                proIDs.add(
                        cursorFavoris.getString(0)
                );
            } while (cursorFavoris.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorFavoris.close();
        return proIDs;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
