package com.example.androidapplications;

import android.content.ContentValues;
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

        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.send_text);
        sendButton = findViewById(R.id.send_button);

        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

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

        cursor.close();

        messageAdapter.notifyDataSetChanged();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typedMessage = editText.getText().toString();

                messages.add(typedMessage);

                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, typedMessage);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);

                messageAdapter.notifyDataSetChanged();

                editText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            return position;
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