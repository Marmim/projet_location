package com.example.app_location;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailsPropriete extends AppCompatActivity implements OnMapReadyCallback{
    BottomNavigationView bottomNavigationView;
    private GoogleMap map;
    private MapView mapView;
    private List<String> photos;
    private ImageView imageViewProperty;
    private TextView textViewType, textViewDescription, textViewTarif, textViewVille, textViewQuartier, contactTextView;

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

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        bottomNavigationView = findViewById(R.id.bot_nav);

        bottomNavigationView.setSelectedItemId(R.id.profilmenu);

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

        String id = getIntent().getStringExtra("propertyId");
        photos = getIntent().getStringArrayListExtra("propertyPhotos");

        if (photos != null && !photos.isEmpty()) {
            for (String photoUrl : photos) {
                Log.d("DetailsPropriete", "Received photo URL: " + photoUrl);
            }
            String firstPhotoUrl = photos.get(0); // Utiliser la première photo
            Log.d("DetailsPropriete", "Loading image URL: " + firstPhotoUrl);
            Glide.with(getApplicationContext()).load(firstPhotoUrl).into(imageViewProperty);
        } else {
            Log.d("DetailsPropriete", "No photos available from intent.");
            imageViewProperty.setImageResource(R.drawable.aprt); }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Property").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Document", "DocumentSnapshot data: " + document.getData());

                        // Récupération des données de Firestore
                        String description = document.getString("description");
                        String tarif = document.getString("tarif");
                        String ville = document.getString("ville");
                        String quartier = document.getString("quartier");
                        String contacte = document.getString("contact");

                        // Affichage des données dans les vues
                        textViewDescription.setText(description);
                        textViewTarif.setText(tarif + " DH /mois");
                        textViewVille.setText(ville);
                        textViewQuartier.setText(quartier);
                        contactTextView.setText("Contactez-nous : " + contacte);

                        // Mise à jour de la carte avec la localisation de la propriété
                        updateMapLocation(ville, quartier);
                    } else {
                        Log.d("Document", "No such document");
                    }
                } else {
                    Log.d("Document", "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    private void updateMapLocation(String ville, String quartier) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String locationName = quartier + ", " + ville;
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng coordinate = new LatLng(address.getLatitude(), address.getLongitude());
                map.addMarker(new MarkerOptions().position(coordinate).title("Propriété"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
            } else {
                Log.d("Geocoder", "No location found for: " + locationName);
                Toast.makeText(this, "Emplacement introuvable: " + locationName, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la récupération de l'emplacement", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}