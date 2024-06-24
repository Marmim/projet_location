package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConversationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewConversations;
    private ConversationsAdapter conversationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        recyclerViewConversations = findViewById(R.id.recycler_view_conversations);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(this));

        // Adapter initialization
        conversationsAdapter = new ConversationsAdapter(new ArrayList<>(), this::onConversationClicked);
        recyclerViewConversations.setAdapter(conversationsAdapter);

        // Load conversations from Firestore or Realtime Database
        loadConversations();
    }

    private void loadConversations() {
        // Load conversations from Firestore or Realtime Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("conversations")
                .get()
                .addOnCompleteListener(this::onComplete);
    }

    private void onConversationClicked(Conversation conversation) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("conversation_id", conversation.getId());
        startActivity(intent);
    }

    private void onComplete(Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            List<Conversation> conversations = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                try {
                    Conversation conversation = document.toObject(Conversation.class);
                    conversations.add(conversation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conversationsAdapter.updateConversations(conversations);
        } else {
            Toast.makeText(this, "Failed to load conversations.", Toast.LENGTH_SHORT).show();
        }
    }

}
