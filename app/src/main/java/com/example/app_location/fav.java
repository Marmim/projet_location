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
        recyclerView.setHasFixedSize(true);

        db = FirebaseFirestore.getInstance();
        FavorisDB favdb = new FavorisDB(this);
        ArrayList<String> favoris = favdb.readFavoris();

        favoriteProperties = new ArrayList<>();
        propertyAdapter = new proprieteAdapter(favoriteProperties, this);
        recyclerView.setAdapter(propertyAdapter);

        // Parcourir les favoris et récupérer les propriétés Firestore de manière asynchrone
        for (String f : favoris) {
            Log.d("favoris", f);
            db.collection("Property")
                    .document(f)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                Property property = document.toObject(Property.class);
                                if (property != null) {
                                    property.setId(document.getId());
                                    favoriteProperties.add(property);
                                    // Notifier l'adaptateur lorsqu'une nouvelle propriété est ajoutée
                                    propertyAdapter.notifyItemInserted(favoriteProperties.size() - 1);
                                    Log.d("favpro", property.getId());
                                }
                                Log.d("Firestore", "Nombre de propriétés favorites: " + favoriteProperties.size());
                            } else {
                                Log.w("Firestore", "Erreur lors de la récupération des favoris.", task.getException());
                            }
                        }
                    });
        }


        favoriteProperties = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        propertyAdapter = new proprieteAdapter(favoriteProperties, this);
        recyclerView.setAdapter(propertyAdapter);




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
            } else if (itemId == R.id.profilmenu) {
                Intent profilIntent = new Intent(getApplicationContext(), ProfilLocataire.class);
                startActivity(profilIntent);
                return true;
            }
            return false;
        });
    }

}
