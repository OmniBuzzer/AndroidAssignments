package com.example.androidapplications;

import android.os.Bundle;
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

    // Class variables for the views
    ListView listView;
    EditText editText;
    Button sendButton;

    // ArrayList to store chat messages
    ArrayList<String> messages = new ArrayList<>();

    // Adapter reference so we can notify it when data changes
    ChatAdapter messageAdapter;

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

        // Initialize views using findViewById (IDs match the updated XML)
        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.send_text);
        sendButton = findViewById(R.id.send_button);

        // Set up the adapter and attach it to the ListView
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        // onClick for Send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String typedMessage = editText.getText().toString();

                // Add it to the ArrayList
                messages.add(typedMessage);

                // Tell the adapter the data changed so the ListView refreshes
                messageAdapter.notifyDataSetChanged();

                // Clear the EditText so user can type a new message
                editText.setText("");
            }
        });
    }

    // Inner class — ChatAdapter
    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
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
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Temporary stub until step 9 — just show the message text in a basic TextView.
            TextView tv = new TextView(ChatWindow.this);
            tv.setText(getItem(position));
            tv.setTextSize(18);
            tv.setPadding(24, 24, 24, 24);
            return tv;
        }
    }
}