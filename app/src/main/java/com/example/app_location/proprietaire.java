package com.example.app_location;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class proprietaire extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private EditText photos, description, contacte, tarif;
    private Button LancerButton, backButton, BSelectImage;

    // One Preview Imageprivate final int MENU_PROFIL = R.id.profilmenu;
    private Spinner Ville_spinner, Type, Quartier_spinner;
    private ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
    private Uri selectedImageUri;

    private ActivityResultLauncher<Intent> imageChooserLauncher;

    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.proprietaire);
        bottomNavigationView = findViewById(R.id.bot_nav);
        LancerButton = findViewById(R.id.LancerButton);
        backButton = findViewById(R.id.backButton);
        description = findViewById(R.id.description);
        contacte = findViewById(R.id.contacte);
        tarif = findViewById(R.id.tarif);
        Type = findViewById(R.id.Type);
        Ville_spinner = findViewById(R.id.Ville_spinner);
        Quartier_spinner = findViewById(R.id.Quartier_spinner);
        BSelectImage = findViewById(R.id.BSelectImage);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        // Set up image chooser launcher
        imageChooserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                IVPreviewImage.setImageURI(selectedImageUri);
                            }
                        }
                    }
                }
        );


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
            } else if (itemId == R.id.favoris) {
                Intent hometIntent = new Intent(getApplicationContext(), fav.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.notif) {
                Intent hometIntent = new Intent(getApplicationContext(), notification.class);
                startActivity(hometIntent);
                return true;
            } else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilProprietaire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });


        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
// quand je clique sur lancer les donnees saisi seront enregistré
        LancerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirestore();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proprietaire.this, QuiEtes.class);
                startActivity(intent);
            }
        });


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        //get villes
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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(proprietaire.this, android.R.layout.simple_spinner_item, villes.toArray(new String[villes.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Ville_spinner.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        //get type

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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(proprietaire.this, android.R.layout.simple_spinner_item, type.toArray(new String[type.size()]));
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Type.setAdapter(dataAdapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    // get quartiers de chacun de ville
    public void getQuartiers(String city) {
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(proprietaire.this, android.R.layout.simple_spinner_item, quartiers.toArray(new String[quartiers.size()]));
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

// save data in firestore

    private void saveDataToFirestore() {
        // Ensure that an image has been selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Veuillez sélectionner une image.", Toast.LENGTH_SHORT).show();
            return;
        }

        String descriptionText = description.getText().toString();
        String contact = contacte.getText().toString();
        String tarifText = tarif.getText().toString();
        String ville = Ville_spinner.getSelectedItem().toString();
        String types = Type.getSelectedItem().toString();
        String quartier = Quartier_spinner.getSelectedItem().toString();


        if (descriptionText.isEmpty() || contact.isEmpty() || tarifText.isEmpty() || ville.isEmpty() || types.isEmpty() || quartier.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + selectedImageUri.getLastPathSegment());

        // Upload the image to Firebase Storage
        imagesRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Save the property data along with the image URL to Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("photo", imageUrl);
                    userData.put("description", descriptionText);
                    userData.put("contact", contact);
                    userData.put("tarif", tarifText);
                    userData.put("ville", ville);
                    userData.put("quartier", quartier);
                    userData.put("type", types);


                    db.collection("Property")
                            .add(userData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(proprietaire.this, "Votre propriété est publiée", Toast.LENGTH_SHORT).show();
                                // Rediriger vers la page suivante après l'enregistrement des données
                                sendNotification("Nouvelle publication", "Votre propriété a été ajoutée avec succès.");

                                Intent intent = new Intent(proprietaire.this, liste_propriete.class);

                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> Toast.makeText(proprietaire.this, "Erreur lors de l'enregistrement des données.", Toast.LENGTH_SHORT).show());

                }))
                .addOnFailureListener(e -> Toast.makeText(proprietaire.this, "Erreur lors de l'enregistrement de l'image.", Toast.LENGTH_SHORT).show());
    }


    // selectionner l image dans le gallerie
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
// selects the image from the imageChooser
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                IVPreviewImage.setImageURI(selectedImageUri);
            }
        }

    }
    private void sendNotification(String title, String message) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/all_notifications");
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);
            JSONObject data = new JSONObject();
            data.put("message", message);
            json.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .header("Authorization", "AIzaSyDHynPsigaOqir66zhLd102X2ugoZdZFEU") // Remplacez YOUR_SERVER_KEY par votre clé d'API
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "Failed to send notification", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Notification sent successfully");
            }
        });
    }
}