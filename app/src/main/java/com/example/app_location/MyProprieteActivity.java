package com.example.app_location;


import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

    public class MyProprieteActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private MyproprieteAdpter myPropertyAdapter;
        private List<Property> propertyList;

        private FirebaseFirestore db;
        private FirebaseAuth mAuth;

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

            myPropertyAdapter = new MyproprieteAdpter(propertyList, this);
            recyclerView.setAdapter(myPropertyAdapter);

            getPropertyListFromFirestore();


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myPropertyAdapter.filterList(newText);
                    return true;
                }
            });


        }

        private void getPropertyListFromFirestore() {
            db.collection("Property")
                    .whereEqualTo("proId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                propertyList.clear();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Property property = document.toObject(Property.class);
                                    assert property != null;
                                    property.setId(document.getId());
                                    propertyList.add(property);
                                }
                                myPropertyAdapter.filterList("");
                            } else {
                                Log.w("Firestore", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }


    }


