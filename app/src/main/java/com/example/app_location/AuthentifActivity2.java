package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthentifActivity2 extends AppCompatActivity {
    private EditText emailtext, passwrd, confirmpasswrd, editNom, editPrenom;
    private Button loginButton;
    private FirebaseFirestore db; // Instance de Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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

                    Intent intent = new Intent(AuthentifActivity2.this, QuiEtes.class);
                    startActivity(intent);
                }
            }
        });

    }
}
