package com.example.app_location;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class proprietaire extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private EditText photos,description,contacte,tarif;
    private Button LancerButton, AnnulerButton;
    private ImageButton backButton ,imageViewSecondary,imageViewMain;
    private final int MENU_PROFIL = R.id.profilmenu;
    private Spinner Ville_spinner,Type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.proprietaire);
        bottomNavigationView = findViewById(R.id.bot_nav);
        LancerButton = findViewById(R.id.LancerButton);
        AnnulerButton = findViewById(R.id.AnnulerButton);
        backButton = findViewById(R.id.backButton);
        photos = findViewById(R.id.photos);
        description = findViewById(R.id.description);
        contacte = findViewById(R.id.contacte);
        tarif = findViewById(R.id.tarif);

        Type=findViewById(R.id.Type);



        Ville_spinner = findViewById(R.id.Ville_spinner);

        bottomNavigationView.setSelectedItemId(R.id.profilmenu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent hometIntent = new Intent(getApplicationContext(), Maison.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.search) {
                Intent hometIntent = new Intent(getApplicationContext(),liste_propriete.class);
                startActivity(hometIntent);
                return true;
            } else  if (itemId == R.id.favoris) {
                Intent hometIntent = new Intent(getApplicationContext(), favoris.class);
                startActivity(hometIntent);
                return true;
            }
            else if (itemId == R.id.notif) {
                Intent hometIntent = new Intent(getApplicationContext(), Maison.class);
                startActivity(hometIntent);
                return true;
            }
            else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilLocataire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });




// quand je clique sur lancer les donnees saisi seront enregistré
        LancerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirestore();

            }
        });
        AnnulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proprietaire.this, liste_propriete.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proprietaire.this, QuiEtes.class);
                startActivity(intent);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //get villes
        CollectionReference citiesRef = db.collection("Ville");
        ArrayList<String> villes = new ArrayList<String>();

        citiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                villes.add(document.getId());
                            }
                            // Create adapter and set it to the spinner here
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(proprietaire.this, android.R.layout.simple_spinner_item, villes.toArray(new String[villes.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Ville_spinner.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        //get type

        CollectionReference typeRef = db.collection("Type_logement");
        ArrayList<String> type = new ArrayList<String>();

        typeRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                type.add(document.getId());
                            }
                            // Create adapter and set it to the spinner here
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(proprietaire.this, android.R.layout.simple_spinner_item, type.toArray(new String[type.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Type.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

        private void saveDataToFirestore() {
            String photo = photos.getText().toString();
            String descriptionText = description.getText().toString();
            String contact = contacte.getText().toString();
            String tarifText = tarif.getText().toString();
            String ville = Ville_spinner.getSelectedItem().toString();
            String types = Type.getSelectedItem().toString();


            if (photo.isEmpty() || descriptionText.isEmpty() || contact.isEmpty() || tarifText.isEmpty() || ville.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userData = new HashMap<>();
            userData.put("photo", photo);
            userData.put("description", descriptionText);
            userData.put("contact", contact);
            userData.put("tarif", tarifText);
            userData.put("ville", ville);
            userData.put("type", types);

            db.collection("users")
                    .add(userData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(proprietaire.this, "Votre propriété est publiée", Toast.LENGTH_SHORT).show();
                        // Rediriger vers la page suivante après l'enregistrement des données
                        Intent intent = new Intent(proprietaire.this, tableau_bord.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(proprietaire.this, "Erreur lors de l'enregistrement des données.", Toast.LENGTH_SHORT).show());
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case 1:
                // L'utilisateur a cliqué sur l'élément de menu "Profil"
                Intent intent = new Intent(this, ProfilProprietaire.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
