package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Maison extends AppCompatActivity {

    private ImageView imageViewProperty;
    private TextView textViewType, textViewDescription, textViewTarif, textViewVille, textViewQuartier, textViewContact;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maison);

        imageViewProperty = findViewById(R.id.photoImageView);
        textViewType = findViewById(R.id.typeTextView);
        textViewDescription = findViewById(R.id.descriptionTextView);
        textViewTarif = findViewById(R.id.tarifTextView);
        textViewVille = findViewById(R.id.villeTextView);
        textViewQuartier = findViewById(R.id.quartierTextView);
        textViewContact = findViewById(R.id.contactTextView);

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
            } else if (itemId == R.id.favoris) {
                Intent hometIntent = new Intent(getApplicationContext(), fav.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.notif) {
                Intent hometIntent = new Intent(getApplicationContext(), notification.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilLocataire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });
        clearGlideCache();

        // Récupération des données de la propriété depuis l'intent
        Intent intent = getIntent();
        if (intent != null) {
            Property property = (Property) intent.getSerializableExtra("property");

            // Affichage des données dans les vues
            if (property != null) {
                Glide.with(this).load(property.getPhoto()).into(imageViewProperty);
                textViewType.setText(property.getType());
                textViewDescription.setText(property.getDescription());
                textViewTarif.setText(property.getTarif());
                textViewVille.setText(property.getVille());
                textViewQuartier.setText(property.getQuartier());
                textViewContact.setText(property.getContact());
            }
        }
    }
    private void clearGlideCache() {
        Glide.get(getApplicationContext()).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache();
            }
        }).start();
    }
}
