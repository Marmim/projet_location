package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class proprietaire extends AppCompatActivity {
    private Button suivantButton, RetourButton;
    private final int MENU_PROFIL = R.id.profilmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proprietaire);

        suivantButton = findViewById(R.id.suivantButton);
        RetourButton = findViewById(R.id.RetourButton);

        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proprietaire.this, liste_propriete.class);
                startActivity(intent);
            }
        });
        RetourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proprietaire.this, QuiEtes.class);
                startActivity(intent);
            }
        });
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
