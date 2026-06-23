package com.example.androidapplications;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidapplications.databinding.ActivityTestToolBarBinding;
import com.google.android.material.snackbar.Snackbar;

public class TestToolBar extends AppCompatActivity {

    private ActivityTestToolBarBinding binding;
    private String dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityTestToolBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMessage = getString(R.string.item_one_text);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, getString(R.string.snackbar_mail_text), Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        if (id == R.id.action_one) {
            Log.d("Toolbar", "Option 1 selected");
            Snackbar.make(binding.getRoot(), dialogMessage, Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.action_two) {
            Log.d("Toolbar", "Option 2 selected");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_back_title_page);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.action_three) {
            Log.d("Toolbar", "Option 3 selected");

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_new_message, null);
            EditText newMessageInput = dialogView.findViewById(R.id.dialog_edit_message);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_new_msg_title);
            builder.setView(dialogView);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialogMessage = newMessageInput.getText().toString();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (id == R.id.action_about) {
            Toast.makeText(this, "Version 1.0, by Jagnoor Hayer", Toast.LENGTH_LONG).show();
        } else {
            return super.onOptionsItemSelected(mi);
        }
        return true;
    }
}