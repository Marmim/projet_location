package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class locataire  extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private Button suivantButton,RetourButton;
    private Spinner Ville_spinner,Quartier_spinner,prix_spinner,Type;
    private ImageButton backButton;

    FirebaseFirestore db;
    proprieteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locataire);


        suivantButton = findViewById(R.id.suivantButton);
        RetourButton=findViewById(R.id.RetourButton);
        Ville_spinner=findViewById(R.id.Ville_spinner);
        Quartier_spinner=findViewById(R.id.Quartier_spinner);
        backButton = findViewById(R.id.backButton);
        Type=findViewById(R.id.Type);
        prix_spinner=findViewById(R.id.prix_spinner);


        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  String selectedVille = Ville_spinner.getSelectedItem().toString();
                String selectedQuartier = Quartier_spinner.getSelectedItem().toString();
                String selectedType = Type.getSelectedItem().toString();
                String selectedPrix = prix_spinner.getSelectedItem().toString();


                // Filtrer les propriétés dans l'adaptateur
                adapter.FilterListe(selectedType, selectedVille, selectedQuartier, selectedPrix);*/
                Intent intent = new Intent(locataire.this, liste_propriete.class);
                startActivity(intent);
            }
        });


        RetourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(locataire.this, liste_propriete.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(locataire.this, QuiEtes.class);
                startActivity(intent);
            }
        });
         db = FirebaseFirestore.getInstance();



       CollectionReference citiesRef = db.collection("Ville");
        ArrayList<String> villes = new ArrayList<String>();

        citiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                villes.add(document.getId());
                            }
                            // Create adapter and set it to the spinner here
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(locataire.this, android.R.layout.simple_spinner_item, villes.toArray(new String[villes.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Ville_spinner.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        Ville_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.i("selected", Ville_spinner.getSelectedItem().toString());
                getQuartiers(Ville_spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        CollectionReference typeRef = db.collection("Type_logement");
        ArrayList<String> type = new ArrayList<String>();

        typeRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                type.add(document.getId());
                            }
                            // Create adapter and set it to the spinner here
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(locataire.this, android.R.layout.simple_spinner_item, type.toArray(new String[type.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Type.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        //get prix
        CollectionReference prixRef = db.collection("Prix");
        ArrayList<String> prix = new ArrayList<String>();

        prixRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                prix.add(document.getId());
                            }
                            // Create adapter and set it to the spinner here
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(locataire.this, android.R.layout.simple_spinner_item, prix.toArray(new String[prix.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            prix_spinner.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


/*

        // Initialisez l'API Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDHynPsigaOqir66zhLd102X2ugoZdZFEU");
        }

        // Votre code existant...

        Ville_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
                loadDistricts(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
*/



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

            }
            else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilLocataire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });

    }

    public void getQuartiers(String city)
    {
        DocumentReference docRef = db.collection("Ville").document(city);

        // Fetch the document using get()
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> quartiers = ((List<Object>) document.getData().get("quartiers")).stream().map(object -> Objects.toString(object, null))
                                .collect(Collectors.toList());
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(locataire.this, android.R.layout.simple_spinner_item, quartiers.toArray(new String[quartiers.size()]));
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        Quartier_spinner.setAdapter(dataAdapter);
                    } else {
                        Log.d("Document data:", "No such document!");
                    }
                } else {
                    Log.d("Error getting document:", task.getException().toString());
                }
            }
        });
    }

   /* private void loadDistricts(String cityName) {
        // Créez une requête pour rechercher les lieux correspondant au nom de la ville
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(cityName + " neighborhoods")
                .setCountries("MA") // Remplacez "MA" par le code du pays approprié si nécessaire
                .build();

        // Initialisez le client Places
        PlacesClient placesClient = Places.createClient(this);

        // Exécutez la requête
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<String> districts = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                districts.add(prediction.getPrimaryText(null).toString());
            }

            // Mettez à jour le Spinner des quartiers
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districts);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Quartier_spinner.setAdapter(adapter);

            Log.d("API_SUCCESS", "Requête API réussie. Quartiers trouvés : " + districts.size());
        }).addOnFailureListener((exception) -> {
            Log.e("API_FAILURE", "Erreur lors de la recherche des quartiers : ", exception);
        });
    }
*/
}



