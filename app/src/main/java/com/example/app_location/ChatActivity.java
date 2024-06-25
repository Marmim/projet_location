package com.example.app_location;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize RecyclerView and its adapter
        recyclerViewChat = findViewById(R.id.recycler_view_chat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        recyclerViewChat.setAdapter(chatAdapter);

        // Initialize Views
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        // Button click listener to send message
        buttonSend.setOnClickListener(v -> sendMessage());
    }
    String messageText = editTextMessage.getText().toString().trim();
    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();

        if (!messageText.isEmpty()) {
            // Créer un objet Message avec le texte du message
//            Message message = new Message(messageText);

            // Ajouter le message à la liste
//            messages.add(message);

            // Notifier l'adaptateur du changement
            chatAdapter.notifyDataSetChanged();

            // Effacer le champ de texte après l'envoi
            editTextMessage.setText("");
        }
    }





}