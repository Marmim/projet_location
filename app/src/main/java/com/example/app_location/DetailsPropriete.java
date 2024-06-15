package com.example.app_location;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailsPropriete extends AppCompatActivity {

    private ImageView imageViewProperty;
    private TextView textViewType, textViewDescription, textViewTarif, textViewVille, textViewQuartier;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propriete_details);

        // Initialisation des vues
        imageViewProperty = findViewById(R.id.photoImageView);
        textViewType = findViewById(R.id.typeTextView);
        textViewDescription = findViewById(R.id.descriptionTextView);
        textViewTarif = findViewById(R.id.tarifTextView);
        textViewVille = findViewById(R.id.villeTextView);
        textViewQuartier = findViewById(R.id.quartierTextView);

        // Récupération des données passées depuis l'activité principale
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String photo = extras.getString("photo");
            String type = extras.getString("type");
            String description = extras.getString("description");
            String tarif = extras.getString("tarif");
            String ville = extras.getString("ville");
            String quartier = extras.getString("quartier");

            // Affichage des données dans les vues
            Glide.with(this).load(photo).into(imageViewProperty);
            textViewType.setText("Type: " + type);
            textViewDescription.setText("Description: " + description);
            textViewTarif.setText("Tarif: " + tarif);
            textViewVille.setText("Ville: " + ville);
            textViewQuartier.setText("Quartier: " + quartier);
        }
    }
}
