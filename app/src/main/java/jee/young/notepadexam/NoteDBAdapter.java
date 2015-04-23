package jee.young.notepadexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class NoteDBAdapter {
    public static final String KEY_TITLE="title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    String DATABASE_TABLE = "notes";

    private final String TAG = this.getClass().getSimpleName();
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private Context mCtx;

    public NoteDBAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public NoteDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE,title);
        initialValues.put(KEY_BODY,body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteNote(long rowid) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowid, null) > 0;
    }

    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_BODY}, null, null, null, null, null);
    }

    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null, null, null, null,null);

        if (mCursor !=null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowid,String title,String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE,title);
        args.put(KEY_BODY,body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowid, null) > 0;
    }
}
