package com.example.app_location;

import android.os.Bundle;
import android.util.Log;

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

    private RecyclerView recyclerView;
    private proprieteAdapter propertyAdapter;
    private List<Property> propertyList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_propriete);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        propertyList = new ArrayList<>();
        getPropertyListFromFirestore();
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
                                propertyList.add(property);
                            }
                            propertyAdapter = new proprieteAdapter(propertyList, liste_propriete.this);
                            recyclerView.setAdapter(propertyAdapter);
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
