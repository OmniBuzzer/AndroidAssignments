package com.example.androidapplications;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Activity is being created");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: Activity is in the foreground");

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
        Log.d(TAG, "onPause: Activity is losing focus");

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
        Log.d(TAG, "onStop: Activity is no longer visible");

        // Activity is fully hidden. Safe to do heavier cleanup:
        // - Unregister broadcast receivers
        // - Stop background services tied to the UI
        // - Commit larger data to disk or database
        // - Release resources you don't need while hidden
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Activity is being destroyed");

        // Final cleanup before the activity is gone.
        // - Cancel running threads or AsyncTasks
        // - Close database connections
        // - Release any remaining references to prevent memory leaks
        // Note: not always called if the system kills the process.
    }
}