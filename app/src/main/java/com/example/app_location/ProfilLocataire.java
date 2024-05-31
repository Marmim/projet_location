package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilLocataire extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    TextView info , notif , payment , fav , logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil_locataire);

        info = findViewById(R.id.editTextText5);
        notif = findViewById(R.id.editTextText7);
        payment = findViewById(R.id.editTextText6);
        fav = findViewById(R.id.editTextText8);
        logout = findViewById(R.id.editTextText9);


        bottomNavigationView = findViewById(R.id.bot_nav);

        bottomNavigationView.setSelectedItemId(R.id.profilmenu);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent hometIntent = new Intent(getApplicationContext(), liste_propriete.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.search) {
                Intent hometIntent = new Intent(getApplicationContext(), locataire.class);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilLocataire.this, MainActivity.class);
                startActivity(intent);
            }
        });
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilLocataire.this, notification.class);
                startActivity(intent);
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilLocataire.this, fav.class);
                startActivity(intent);
            }
        });
    }
}