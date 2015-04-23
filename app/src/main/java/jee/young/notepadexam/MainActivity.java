package jee.young.notepadexam;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class MainActivity extends ListActivity {

    private NoteDBAdapter mDbAdapter;
    public static final int INSERT_ID = Menu.FIRST;
    private int mNoteNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbAdapter = new NoteDBAdapter(this);
        try {
            mDbAdapter.open();
            fillData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fillData() {
        Cursor cursor = mDbAdapter.fetchAllNotes();
        startManagingCursor(cursor);

        String[] from = new String[]{NoteDBAdapter.KEY_TITLE};
        int[] to = new int[]{R.id.text1};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, cursor, from, to);
        setListAdapter(notes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, "Add Item");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == INSERT_ID) {
            createNote();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        String noteName = "Note"+mNoteNumber++;
        mDbAdapter.createNote(noteName, "");
        fillData();
    }
}
