package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class fav extends AppCompatActivity {
    private RecyclerView recyclerView;
    private proprieteAdapter propertyAdapter;
    private List<Property> favoriteProperties;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoriteProperties = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        propertyAdapter = new proprieteAdapter(favoriteProperties, this);
        recyclerView.setAdapter(propertyAdapter);

        loadFavoritePropertiesFromFirestore();

        bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setSelectedItemId(R.id.profilmenu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent homeIntent = new Intent(getApplicationContext(), liste_propriete.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.search) {
                Intent searchIntent = new Intent(getApplicationContext(), liste_propriete.class);
                startActivity(searchIntent);
                return true;
            } else if (itemId == R.id.favoris) {
                Intent favIntent = new Intent(getApplicationContext(), fav.class);
                startActivity(favIntent);
                return true;
            } else if (itemId == R.id.notif) {
                Intent notifIntent = new Intent(getApplicationContext(), notification.class);
                startActivity(notifIntent);
                return true;
            } else if (itemId == R.id.profilmenu) {
                Intent profilIntent = new Intent(getApplicationContext(), ProfilLocataire.class);
                startActivity(profilIntent);
                return true;
            }
            return false;
        });
    }

    private void loadFavoritePropertiesFromFirestore() {
        db.collection("Property")
                .whereEqualTo("favorite", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            favoriteProperties.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Property property = document.toObject(Property.class);
                                if (property != null) {
                                    property.setId(document.getId());
                                    favoriteProperties.add(property);
                                }
                            }
                            propertyAdapter.notifyDataSetChanged();
                            Log.d("Firestore", "Nombre de propriétés favorites: " + favoriteProperties.size());
                        } else {
                            Log.w("Firestore", "Erreur lors de la récupération des favoris.", task.getException());
                        }
                    }
                });
    }

}