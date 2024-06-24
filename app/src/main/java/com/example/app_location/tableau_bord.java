package com.example.app_location;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class tableau_bord extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
        setContentView(R.layout.tableau_bord);

            bottomNavigationView = findViewById(R.id.bot_nav);
            db = FirebaseFirestore.getInstance();

            bottomNavigationView.setSelectedItemId(R.id.nav_home);

            // Observer les changements de la base de données Firestore pour les paiements
            db.collection("payments")
                    .whereEqualTo("propertyId", "propertyId")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        LinearLayout paymentListLayout = findViewById(R.id.paymentListLayout);
                        paymentListLayout.removeAllViews();

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String tenantName = document.getString("full_name");

                            TextView tenantNameTextView = new TextView(this); // Utilisez "this" pour référencer l'activité actuelle
                            tenantNameTextView.setText(tenantName);
                            tenantNameTextView.setTextSize(18);
                            tenantNameTextView.setPadding(10, 5, 10, 5);

                            paymentListLayout.addView(tenantNameTextView);
                        }
                    });

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent hometIntent = new Intent(getApplicationContext(), liste_propriete.class);
                    startActivity(hometIntent);
                    return true;
                } else if (itemId == R.id.search) {
                    Intent hometIntent = new Intent(getApplicationContext(), liste_propriete.class);
                    startActivity(hometIntent);
                    return true;
                } else  if (itemId == R.id.favoris) {
                Intent hometIntent = new Intent(getApplicationContext(), fav.class);
                startActivity(hometIntent);
                return true;
                }
                else if (itemId == R.id.notif) {
                    Intent hometIntent = new Intent(getApplicationContext(), notification.class);
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

        }

}
