package com.example.androidapplications;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    private static final String TAG = "ListItemsActivity";

    private ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ListItemsActivity is being created");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgButton = findViewById(R.id.imageButton);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchButton = findViewById(R.id.switchButton);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // MediaStore.ACTION_IMAGE_CAPTURE is a standard Intent action
                // that any installed camera app can respond to.
                // We don't name a specific Activity class — Android picks
                // the user's default camera app.
                Intent cameraIntent =
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, 20);
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CharSequence text;
                int duration;

                if (isChecked) {
                    text = "Switch is On";
                    duration = Toast.LENGTH_SHORT;
                } else {
                    text = "Switch is Off";
                    duration = Toast.LENGTH_LONG;
                }

                Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                toast.show();
            }
        });
    }

    public void print(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        print("Returned To ListItemsActivity");

        // Make sure this result came from our camera request
        if (requestCode == 20 && resultCode == RESULT_OK) {

            Log.i(TAG, "Photo captured, updating ImageButton");

            // Extract the thumbnail Bitmap from the returned Intent's extras
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap photo = (Bitmap) extras.get("data");

            // Replace the button's image with the photo we just took
            imgButton.setImageBitmap(photo);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ListItemsActivity is in the foreground");

        // Activity is interactive. Start things that should only run
        // while the user is actively using the screen:
        // - Camera preview
        // - Sensor listeners (accelerometer, GPS)
        // - Animations
        // - Resume video/audio playback

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ListItemsActivity is losing focus");

        // Called when another activity comes in front (could be partial,
        // like a dialog). Keep this method FAST — heavy work delays the
        // next activity from appearing.
        // - Pause animations or video playback
        // - Unregister sensor listeners
        // - Persist small bits of user data quickly
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ListItemsActivity is no longer visible");

        // Activity is fully hidden. Safe to do heavier cleanup:
        // - Unregister broadcast receivers
        // - Stop background services tied to the UI
        // - Commit larger data to disk or database
        // - Release resources you don't need while hidden
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ListItemsActivity is being destroyed");

        // Final cleanup before the activity is gone.
        // - Cancel running threads or AsyncTasks
        // - Close database connections
        // - Release any remaining references to prevent memory leaks
        // Note: not always called if the system kills the process.
    }
}