package com.example.app_location;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class proprietaire extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String CHANNEL_ID = "my_channel_01";
    private EditText photos, description, contacte, tarif;
    private Button LancerButton, backButton, BSelectImage;
    private ImageButton previous, next;
    ImageSwitcher IVPreviewImage;

    private Spinner Ville_spinner, Type, Quartier_spinner;

    int SELECT_PICTURE = 200;
    private Uri selectedImageUri;
    ArrayList<Uri> mArrayUri;

    List<String> imagesEncodedList;
    int position = 0;


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
        mArrayUri = new ArrayList<Uri>();
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        // click here to select next image
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    IVPreviewImage.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(proprietaire.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }
        });


// click here to view previous image

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    IVPreviewImage.setImageURI(mArrayUri.get(position));
                }
            }
        });


        // showing all images in imageswitcher
        IVPreviewImage.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });



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

            } else if (itemId == R.id.profilmenu) {
                Intent hometIntent = new Intent(getApplicationContext(), ProfilProprietaire.class);
                startActivity(hometIntent);
                return true;
            }
            return false;
        });


        // selectionner l image dans le gallerie
    BSelectImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
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
        // Ensure that at least one image has been selected
        if (mArrayUri.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner au moins une image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get other property data
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

        // Utiliser un compteur pour savoir quand toutes les images ont été téléchargées
        AtomicInteger uploadCount = new AtomicInteger(0);
        List<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : mArrayUri) {
            StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());

            imagesRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        imageUrls.add(imageUrl);

                        // Vérifier si toutes les images ont été téléchargées
                        if (uploadCount.incrementAndGet() == mArrayUri.size()) {
                            // Enregistrer toutes les URLs d'images dans Firestore avec les autres données de la propriété
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("photo", imageUrls);
                            userData.put("description", descriptionText);
                            userData.put("contact", contact);
                            userData.put("tarif", tarifText);
                            userData.put("ville", ville);
                            userData.put("quartier", quartier);
                            userData.put("type", types);
                            userData.put("isPaid", false);
                            userData.put("proId", FirebaseAuth.getInstance().getCurrentUser().getUid());

                            db.collection("Property")
                                    .add(userData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(proprietaire.this, "Votre propriété est publiée", Toast.LENGTH_SHORT).show();
                                        sendNotification("Nouvelle publication", "Votre propriété a été ajoutée avec succès.");
                                        Intent intent = new Intent(proprietaire.this, liste_propriete.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(proprietaire.this, "Erreur lors de l'enregistrement des données.", Toast.LENGTH_SHORT).show());
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(proprietaire.this, "Erreur lors de l'envoi de l'image.", Toast.LENGTH_SHORT).show());
        }
    }




                              /*  // selectionner l image dans le gallerie
                                void imageChooser () {

                                    // create an instance of the
                                    // intent of the type image
                                    Intent i = new Intent();
                                    i.setType("image/*");
                                    i.setAction(Intent.ACTION_GET_CONTENT);

                                    // pass the constant to compare it
                                    // with the returned requestCode
                                    startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
                                }*/

    // this function is triggered when user
// selects the image from the imageChooser
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                IVPreviewImage.setImageURI(selectedImageUri);
            }

        }

    }*/








    // this function is triggered when user
// selects the image from the imageChooser
                        @Override
                        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                            super.onActivityResult(requestCode, resultCode, data);
                            // When an Image is picked
                            if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
                                // Get the Image from data
                                if (data.getClipData() != null) {
                                    ClipData mClipData = data.getClipData();
                                    int cout = data.getClipData().getItemCount();
                                    for (int i = 0; i < cout; i++) {
                                        // adding imageuri in array
                                        Uri imageurl = data.getClipData().getItemAt(i).getUri();
                                        mArrayUri.add(imageurl);
                                    }
                                    // setting 1st selected image into image switcher
                                    IVPreviewImage.setImageURI(mArrayUri.get(0));
                                    position = 0;
                                } else {
                                    Uri imageurl = data.getData();
                                    mArrayUri.add(imageurl);
                                    IVPreviewImage.setImageURI(mArrayUri.get(0));
                                    position = 0;
                                }
                            } else {
                                // show this if no image is selected
                                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                            }
                        }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.drawablelogo);
        builder.setContentTitle(title);  // Utilise le titre passé en paramètre
        builder.setContentText(message); // Utilise le message passé en paramètre
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}





