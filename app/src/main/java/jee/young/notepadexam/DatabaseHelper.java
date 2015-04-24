package jee.young.notepadexam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "data";

    static int DATABASE_VERSION = 2;
    static String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, title text not null, body text not null); ";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(getClass().getSimpleName(), "oldVersion[" + oldVersion + "]newVersion[" + newVersion + "]");

        db.execSQL("drop table if exists notes");
        onCreate(db);
    }
}
