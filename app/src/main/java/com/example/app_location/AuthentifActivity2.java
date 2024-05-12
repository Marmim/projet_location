package com.example.app_location;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthentifActivity2 extends AppCompatActivity {
    private EditText emailtext, passwrd, confirmpasswrd, editNom, editPrenom;
    private Button loginButton;
    private FirebaseFirestore db; // Instance de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentif2);

        // Initialiser Firestore
        db = FirebaseFirestore.getInstance();

        // Récupérer les vues à partir du layout XML
        emailtext = findViewById(R.id.emailtext);
        passwrd = findViewById(R.id.passwrd);
        confirmpasswrd = findViewById(R.id.confirmpasswrd);
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        loginButton = findViewById(R.id.loginButton);

        // Ajouter un OnClickListener au bouton de connexion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailtext.getText().toString();
                String password = passwrd.getText().toString();
                String confirmPassword = confirmpasswrd.getText().toString();
                String nom = editNom.getText().toString();
                String prenom = editPrenom.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                    Toast.makeText(AuthentifActivity2.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(AuthentifActivity2.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                } else {
                    // Créer un utilisateur avec Firebase Auth
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> {
                                // L'utilisateur a été créé avec succès
                                // Enregistrer les informations de l'utilisateur dans Firestore
                                Map<String, Object> user = new HashMap<>();
                                user.put("nom", nom);
                                user.put("prenom", prenom);

                                db.collection("utilisateurs").add(user)
                                        .addOnSuccessListener(documentReference -> {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            // Rediriger vers l'activité suivante
                                            Intent intent = new Intent(AuthentifActivity2.this, QuiEtes.class);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w(TAG, "Error adding document", e);
                                            Toast.makeText(AuthentifActivity2.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Une erreur s'est produite lors de la création de l'utilisateur
                                Toast.makeText(AuthentifActivity2.this, "Erreur lors de la création de l'utilisateur", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error creating user", e);
                            });
                }
            }
        });

    }
}
