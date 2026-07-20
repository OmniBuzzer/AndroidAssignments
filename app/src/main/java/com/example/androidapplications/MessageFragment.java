package com.example.androidapplications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    // Reference to the chat activity: non-null on tablet, null on phone
    private final ChatWindow chatWindow;

    public MessageFragment(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_message_details, container, false);

        TextView messageText = result.findViewById(R.id.message_Text);
        TextView idText = result.findViewById(R.id.id_Text);
        Button deleteButton = result.findViewById(R.id.deleteButton);

        Bundle args = getArguments();
        if (args != null) {
            messageText.setText(args.getString("messageText"));
            idText.setText("ID = " + args.getLong("messageId"));
        }

        deleteButton.setOnClickListener(v -> {
            if (args == null) return;
            long messageId = args.getLong("messageId");

            if (chatWindow != null) {
                // Tablet: delete directly through the ChatWindow reference,
                // then remove this fragment from the FrameLayout
                chatWindow.deleteMessage(messageId);

                getParentFragmentManager()
                        .beginTransaction()
                        .remove(MessageFragment.this)
                        .commit();

            } else {
                // Phone: send the id back to ChatWindow as a result, then close
                Intent resultIntent = new Intent();
                resultIntent.putExtra("messageId", messageId);

                getActivity().setResult(Activity.RESULT_OK, resultIntent);
                getActivity().finish();
            }
        });

        return result;
    }
}