package jee.young.notepadexam;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class MainActivity extends ListActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

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
//        String noteName = "Note"+mNoteNumber++;
//        mDbAdapter.createNote(noteName, "");
//        fillData();
        Intent intent = new Intent(this,NoteEdit.class);
        startActivityForResult(intent,ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor cursor1 = cursor;
        cursor1.moveToPosition(position);

        Intent intent = new Intent(this, NoteEdit.class);
        intent.putExtra(NoteDBAdapter.KEY_ROWID, id);
        intent.putExtra(NoteDBAdapter.KEY_TITLE, cursor1.getString(cursor1.getColumnIndexOrThrow(NoteDBAdapter.KEY_TITLE)));
        intent.putExtra(NoteDBAdapter.KEY_BODY, cursor1.getString(cursor1.getColumnIndexOrThrow(NoteDBAdapter.KEY_BODY)));

        startActivityForResult(intent,ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null){
            Bundle extras = intent.getExtras();

            switch (requestCode){
                case ACTIVITY_CREATE:
                    String title = extras.getString(NoteDBAdapter.KEY_TITLE);
                    String body = extras.getString(NoteDBAdapter.KEY_BODY);
                    mDbAdapter.createNote(title, body);
                    fillData();
                    break;

                case ACTIVITY_EDIT:
                    Long rowId = extras.getLong(NoteDBAdapter.KEY_ROWID);

                    if(rowId != null) {
                        String editTitle = extras.getString(NoteDBAdapter.KEY_TITLE);
                        String editBody = extras.getString(NoteDBAdapter.KEY_BODY);
                        mDbAdapter.updateNote(rowId, editTitle, editBody);
                    }

                    fillData();
                    break;
            }
        }
    }
}
