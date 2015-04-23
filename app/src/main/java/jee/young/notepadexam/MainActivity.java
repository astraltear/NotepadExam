package jee.young.notepadexam;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class MainActivity extends ListActivity {

    private NoteDBAdapter mDbAdapter;
    private Cursor cursor;


    public static final int INSERT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;

    private int mNoteNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbAdapter = new NoteDBAdapter(this);
        try {
            mDbAdapter.open();
            fillData();
            registerForContextMenu(getListView());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fillData() {
        cursor = mDbAdapter.fetchAllNotes();
        startManagingCursor(cursor);

        String[] from = new String[]{NoteDBAdapter.KEY_TITLE};
        int[] to = new int[]{R.id.text1};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, cursor, from, to);
        setListAdapter(notes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, "Add Item");
        return result;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
       switch (item.getItemId()){
           case INSERT_ID:
               createNote();
               return true;
       }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, "Delete Note");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbAdapter.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
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
