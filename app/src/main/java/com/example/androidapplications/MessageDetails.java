package com.example.androidapplications;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MessageFragment fragment = new MessageFragment(null);

        Bundle bundle = new Bundle();

        bundle.putString("messageText", getIntent().getStringExtra("messageText"));
        bundle.putLong("messageId", getIntent().getLongExtra("messageId", -1));

        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment).commit();
    }
}