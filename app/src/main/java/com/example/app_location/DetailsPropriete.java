package com.example.app_location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DetailsPropriete extends AppCompatActivity {

    private ImageSwitcher photoImageView;
    private TextView textViewDescription, textViewTarif, textViewVille, textViewQuartier, contactTextView;
    private Button louerButton;
    private String propertyId;
    private ImageButton next, previous;
    private List<String> photoArray = new ArrayList<>();
    private int position = 0;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propriete_details);

        // Initialisation des vues
        photoImageView = findViewById(R.id.photoImageView);
        textViewDescription = findViewById(R.id.descriptionTextView);
        textViewTarif = findViewById(R.id.tarifTextView);
        textViewVille = findViewById(R.id.villeTextView);
        textViewQuartier = findViewById(R.id.quartierTextView);
        contactTextView = findViewById(R.id.contactTextView);
        louerButton = findViewById(R.id.LouerButton);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        // Configuration du listener pour le bouton Louer
        louerButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailsPropriete.this, PayementActivity.class);
            intent.putExtras(getIntent().getExtras()); // Pass propertyId to PayementActivity
            startActivity(intent);
        });

        next.setOnClickListener(v -> {
            if (position < photoArray.size() - 1) {
                position++;
                updateImage();
            } else {
                Toast.makeText(DetailsPropriete.this, "Dernière image déjà affichée", Toast.LENGTH_SHORT).show();
            }
        });

        previous.setOnClickListener(v -> {
            if (position > 0) {
                position--;
                updateImage();
            } else {
                Toast.makeText(DetailsPropriete.this, "Première image déjà affichée", Toast.LENGTH_SHORT).show();
            }
        });

        photoImageView.setFactory(() -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return imageView;
        });

        // Récupération des données passées depuis l'activité principale
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("propertyId")) {
            String id = extras.getString("propertyId");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Property").document(id);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Log.i("Property", "DocumentSnapshot data: " + document.getData());

                            photoArray = (List<String>) document.get("photo");
                            String description = document.getString("description");
                            String tarif = document.getString("tarif");
                            String ville = document.getString("ville");
                            String contact = document.getString("contact");
                            String quartier = document.getString("quartier");
                            String proId = document.getString("proId");

                            // Vérifier la récupération des images
                            if (photoArray != null) {
                                Log.i("Property", "Nombre d'images récupérées: " + photoArray.size());
                                for (String photoUrl : photoArray) {
                                    Log.i("Property", "URL de l'image: " + photoUrl);
                                }
                                Toast.makeText(DetailsPropriete.this, "Images récupérées avec succès", Toast.LENGTH_SHORT).show();
                                updateImage(); // Met à jour l'image initiale
                            } else {
                                // Afficher une image par défaut si photoArray est vide ou null
                                Toast.makeText(DetailsPropriete.this, "Aucune image disponible", Toast.LENGTH_SHORT).show();
                            }

                            textViewDescription.setText(description != null ? description : "Description non disponible");
                            textViewTarif.setText(tarif != null ? tarif + " DH / mois" : "Tarif non disponible");
                            textViewVille.setText(ville != null ? ville : "Ville non disponible");
                            textViewQuartier.setText(quartier != null ? quartier : "Quartier non disponible");
                            contactTextView.setText(contact != null ? "Contactez-nous: " + contact : "Contact non disponible");

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(proId)) {
                                louerButton.setVisibility(View.GONE);
                            }

                        } else {
                            Log.e("Property", "No such document");
                            Toast.makeText(DetailsPropriete.this, "Propriété non trouvée", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Property", "get failed with ", task.getException());
                        Toast.makeText(DetailsPropriete.this, "Échec de la récupération des données", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e("Property", "No propertyId passed in intent");
            Toast.makeText(this, "Aucun identifiant de propriété fourni", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImage() {
        if (!photoArray.isEmpty() && position < photoArray.size()) {
            Glide.with(getApplicationContext()).load(photoArray.get(position)).into((ImageView) photoImageView.getCurrentView());
        }
    }
}
