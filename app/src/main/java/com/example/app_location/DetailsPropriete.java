package com.example.app_location;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailsPropriete extends AppCompatActivity {

    private ImageView imageViewProperty;
    private TextView textViewType, textViewDescription, textViewTarif, textViewVille, textViewQuartier, textViewContact;

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
        textViewContact = findViewById(R.id.contactTextView);

        // Récupération des données passées depuis l'activité principale
        Property property = getIntent().getParcelableExtra("property");

        // Assure-toi que property n'est pas null avant de l'utiliser
        if (property != null) {
            Glide.with(this).load(property.getPhoto()).into(imageViewProperty);
            textViewType.setText("Type: " + property.getType());
            textViewDescription.setText("Description: " + property.getDescription());
            textViewTarif.setText("Tarif: " + property.getTarif());
            textViewVille.setText("Ville: " + property.getVille());
            textViewQuartier.setText("Quartier: " + property.getQuartier());
            textViewContact.setText("Contact: " + property.getContact());
        }
    }
}
