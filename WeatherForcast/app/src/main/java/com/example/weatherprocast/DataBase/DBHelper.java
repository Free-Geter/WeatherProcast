package com.example.weatherprocast.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "city_list";

    public static final String CREATE_TABLE = "create table " + dbContract.ContactEntry.TABLE_NAME + "("
            + dbContract.ContactEntry.CONTACT_ID + " integer primary key autoincrement,"
            + dbContract.ContactEntry.NAME + " varchar(20) unique not null,"
            + dbContract.ContactEntry.CONTENT + " text not null)";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        this(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    Log.d("city message", "onCreate: " + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
