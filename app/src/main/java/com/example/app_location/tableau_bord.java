package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class tableau_bord extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
        setContentView(R.layout.tableau_bord);

            bottomNavigationView = findViewById(R.id.bot_nav);

            bottomNavigationView.setSelectedItemId(R.id.nav_home);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent hometIntent = new Intent(getApplicationContext(), Maison.class);
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

        }

}
