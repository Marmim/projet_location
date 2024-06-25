package com.example.app_location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class liste_propriete extends AppCompatActivity {

    Context context;

    private RecyclerView recyclerView;
    private proprieteAdapter propertyAdapter;
    private List<Property> propertyList;
    private FirebaseFirestore db;
    private ArrayList<Uri> mArrayUri;
    private int position = 0;
    CheckBox checkbox;
    private static final int PICK_IMAGE_REQUEST = 1;
    List<String> favoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_propriete);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.searchView);

        db = FirebaseFirestore.getInstance();
        propertyList = new ArrayList<>();
        FavorisDB favdb = new FavorisDB(this);
        favoris = favdb.readFavoris();
        /*for(String f:favoris)
            Log.d("liste profav", String.valueOf(favoris.contains("6YUyTmePE1Ue4D0zgUXD")));*/

        propertyAdapter = new proprieteAdapter(propertyList, this);
        recyclerView.setAdapter(propertyAdapter);

        getPropertyListFromFirestore();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                propertyAdapter.filterList(newText);
                return true;
            }
        });


    }

    private void getPropertyListFromFirestore() {
        db.collection("Property")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            propertyList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Property property = document.toObject(Property.class);
                                assert property != null;
                                property.setId(document.getId());
                                if (favoris.contains(property.getId())) {
                                    property.setFavorite(true);
                                }
                                // Récupérer les images du champ 'photo'
                                Log.d("liste profav", String.valueOf(property.isFavorite()));
                                List<String> photoArray = (List<String>) document.get("photo");
                                property.setPhoto(photoArray);
                                propertyList.add(property);
                                if (property != null) {
                                    property.setId(document.getId());
                                    propertyList.add(property);
                                }
                            }
                            propertyAdapter.filterList("");
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    }
