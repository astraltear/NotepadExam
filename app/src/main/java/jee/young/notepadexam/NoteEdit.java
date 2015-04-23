package jee.young.notepadexam;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends ActionBarActivity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        setTitle("EDIT NOTE");

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmBtn = (Button) findViewById(R.id.confirm);

        mRowId = null;
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String title = extras.getString(NoteDBAdapter.KEY_TITLE);
            String body = extras.getString(NoteDBAdapter.KEY_BODY);
            mRowId = extras.getLong(NoteDBAdapter.KEY_ROWID);

            if (title !=null){
                mTitleText.setText(title);
            }

            if (body != null) {
                mBodyText.setText(body);
            }
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString(NoteDBAdapter.KEY_TITLE,mTitleText.getText().toString());
                bundle.putString(NoteDBAdapter.KEY_BODY, mBodyText.getText().toString());

                if(mRowId != null){
                    bundle.putLong(NoteDBAdapter.KEY_ROWID,mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK,mIntent);
                finish();
            }
        });


    }
}
