package com.example.app_location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfilProprietaire extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private ImageButton backButton;
    private TextView info, notifs, fav, logout, pub, dash, listloc, pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);



        setContentView(R.layout.activity_profil_proprietaire);

        bottomNavigationView = findViewById(R.id.bot_nav);
        bottomNavigationView.setSelectedItemId(R.id.profilmenu);
        backButton = findViewById(R.id.backButton);
        info = findViewById(R.id.editTextText5);
        pay = findViewById(R.id.editTextText6);
        pub = findViewById(R.id.editTextText9);
        logout = findViewById(R.id.editTextText12);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, liste_propriete.class);
                startActivity(intent);
            }
        });

        // se deriger vers info.person
        TextView personalInfoTextView = findViewById(R.id.editTextText5);
        personalInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, info_perso.class);
                Bundle b = new Bundle();
                b.putString("role","proprietaire");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        TextView logoutTextView = findViewById(R.id.editTextText12);
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, MainActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        TextView listeProprieteTextView = findViewById(R.id.listePropText1);
        listeProprieteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, MyProprieteActivity.class);
                startActivity(intent);
            }
        });


        // Récupérer les informations de l'utilisateur à partir de Firestore et les afficher dans l'EditText
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        if (user != null) {
            String email = user.getEmail();
            db.collection("utilisateurs")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nom = document.getString("nom");
                                String prenom = document.getString("prenom");
                                String fullName = nom + " " + prenom;
                                EditText editTextText3 = findViewById(R.id.editTextText3);
                                editTextText3.setText(fullName);
                            }
                        } else {
                            //lerreur
                        }
                    });
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent hometIntent = new Intent(getApplicationContext(), liste_propriete.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.search) {
                Intent hometIntent = new Intent(getApplicationContext(),liste_propriete.class);
                startActivity(hometIntent);
            }
            else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilProprietaire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });


        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, proprietaire.class);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilProprietaire.this, info_perso.class);
                startActivity(intent);
            }
         });


    }
}