package com.example.app_location;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DetailsPropriete extends AppCompatActivity {

    private ImageView imageViewProperty;
    private TextView textViewType, textViewDescription, textViewTarif, textViewVille, textViewQuartier,contactTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propriete_details);

        // Initialisation des vues
        imageViewProperty = findViewById(R.id.photoImageView);
        textViewDescription = findViewById(R.id.descriptionTextView);
        textViewTarif = findViewById(R.id.tarifTextView);
        textViewVille = findViewById(R.id.villeTextView);
        textViewQuartier = findViewById(R.id.quartierTextView);
        contactTextView = findViewById(R.id.contactTextView);


        // Récupération des données passées depuis l'activité principale
        String id = getIntent().getExtras().getString("propertyId");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Property").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i("Property", id);
                        String photo = document.getString("photo");
                        String type = document.getString("type");
                        String description = document.getString("description");
                        String tarif = document.getString("tarif");
                        String ville = document.getString("ville");
                        String contacte = document.getString("contact");
                        String quartier = document.getString("quartier");

                        // Affichage des données dans les vues
                        Glide.with(getApplicationContext()).load(photo).into(imageViewProperty);
                        textViewDescription.setText( description);
                        textViewTarif.setText( tarif +" DH  /mois");
                        textViewVille.setText(ville);
                        textViewQuartier.setText(quartier);
                        contactTextView.setText("Contactez-nous: " + contacte);

                    }
                }
            }
        });

    }
}
