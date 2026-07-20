package com.example.androidapplications;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    ListView listView;
    EditText editText;
    Button sendButton;

    ArrayList<String> messages = new ArrayList<>();

    ChatAdapter messageAdapter;

    protected static final String ACTIVITY_NAME = "ChatWindow";

    private SQLiteDatabase db;

    private Cursor cursor;      // NEW: class variable so getItemId can use it

    private boolean isTablet;   // true when the sw600dp (tablet) layout is loaded

    private static final int MESSAGE_DETAILS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // frameLayout only exists in res/layout-sw600dp, so null means phone layout
        isTablet = (findViewById(R.id.frameLayout) != null);
        Log.i(ACTIVITY_NAME, "isTablet = " + isTablet);

        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.send_text);
        sendButton = findViewById(R.id.send_button);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Query with null projection returns ALL columns, including _id
        cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        Log.i(ACTIVITY_NAME, "Cursor's column count =" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name: " + cursor.getColumnName(i));
        }

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            messages.add(message);
            cursor.moveToNext();
        }

        // NOTE: cursor is no longer closed here — getItemId needs it alive

        messageAdapter.notifyDataSetChanged();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typedMessage = editText.getText().toString();

                messages.add(typedMessage);

                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, typedMessage);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

                // NEW: re-query so the cursor also contains the row we just inserted,
                // keeping cursor positions in sync with the messages list
                cursor.close();
                cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

                messageAdapter.notifyDataSetChanged();

                editText.setText("");
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 'id' is the database _id — the ListView gets it by calling
            // messageAdapter.getItemId(position) from step 6

            String message = messageAdapter.getItem(position);

            if (isTablet) {
                // Tablet: load the fragment into the FrameLayout beside the list
                MessageFragment fragment = new MessageFragment(this);

                Bundle bundle = new Bundle();
                bundle.putString("messageText", message);
                bundle.putLong("messageId", id);
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

            } else {
                // Phone: launch the MessageDetails activity instead
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtra("messageText", message);
                intent.putExtra("messageId", id);
                startActivityForResult(intent, MESSAGE_DETAILS_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MESSAGE_DETAILS_REQUEST && resultCode == RESULT_OK && data != null) {
            long idToDelete = data.getLongExtra("messageId", -1);
            Log.i(ACTIVITY_NAME, "Deleting message with id = " + idToDelete);
            deleteMessage(idToDelete);
        }
    }

    // Deletes the row from the database, then rebuilds the list and cursor
    public void deleteMessage(long id) {
        db.delete(ChatDatabaseHelper.TABLE_NAME,
                ChatDatabaseHelper.KEY_ID + "=?",
                new String[]{ Long.toString(id) });

        // Re-query and rebuild the messages list so list, cursor, and db stay in sync
        cursor.close();
        cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        messages.clear();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            messages.add(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }

        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();                          // NEW: close it here instead
        }
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0, messages);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            // NEW: return the database _id of the row at this list position
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, parent, false);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, parent, false);
            }

            TextView message = result.findViewById(R.id.textMessage);
            message.setText(getItem(position));

            return result;
        }
    }
}