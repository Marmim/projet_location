package com.example.app_location;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class recycleView extends AppCompatActivity {
    private RecyclerView recyclerview;
    private proprieteAdapter propertyAdapter;
    private ArrayList<Property> PropretyArrayList;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    private Button LancerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_propriete);

        recyclerview = findViewById(R.id.recyclerView);
        LancerButton = findViewById(R.id.LancerButton);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        PropretyArrayList = new ArrayList<Property>();
        db.collection("your_collection_name")
                .get() // Get all documents in the collection
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                PropretyArrayList.add(new Property(document.getString("photo")
                                        ,document.getString("description"),
                                        document.getString("contact")
                                        ,document.getString("tarif")
                                        ,document.getString("ville")
                                        ,document.getString("quartier")
                                        ,document.getString("type")));
                            }
                        } else {
                            // Handle errors
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
        propertyAdapter = new proprieteAdapter(PropretyArrayList.toArray(new Property[PropretyArrayList.size()]), this);
        recyclerview.setAdapter(propertyAdapter);

        LancerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(recycleView.this, liste_propriete.class));
            }
        });

        EventChangeListener();

        bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setSelectedItemId(R.id.profilmenu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(getApplicationContext(), Maison.class);
            } else if (itemId == R.id.search) {
                intent = new Intent(getApplicationContext(), liste_propriete.class);
            } else if (itemId == R.id.favoris) {
                intent = new Intent(getApplicationContext(), fav.class);
            } else if (itemId == R.id.notif) {
                intent = new Intent(getApplicationContext(), notification.class);
            } else if (itemId == R.id.profilmenu) {
                intent = new Intent(getApplicationContext(), ProfilLocataire.class);
            }
            if (intent != null) {
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void EventChangeListener() {
        db.collection("Property")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e(TAG, "firestore error: " + error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Property property = dc.getDocument().toObject(Property.class);
                                PropretyArrayList.add(property);
                            }
                        }
                        propertyAdapter.notifyDataSetChanged();
                    }
                });
    }
}
