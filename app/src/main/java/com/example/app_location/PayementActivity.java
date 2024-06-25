package com.example.app_location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PayementActivity extends AppCompatActivity {

    private EditText contactEditText;
    private EditText cardNumberEditText;
    private Button payButton , contrat;
    private FirebaseFirestore db;
    private ImageButton backButton;
    private String propertyId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payement);

        contactEditText = findViewById(R.id.edittext_contact);
        cardNumberEditText = findViewById(R.id.edittext_card_number);
        payButton = findViewById(R.id.button_pay);
        backButton = findViewById(R.id.backButton);
        contrat = findViewById(R.id.button_contrat);


        contrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayementActivity.this, Contrat.class);
                startActivity(intent);
            }
         });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayementActivity.this, DetailsPropriete.class);
                intent.putExtra("propertyId", propertyId);
                startActivity(intent);
            }
        });

        // Récupération du propertyId passé depuis DetailsPropriete
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("propertyId")) {
            propertyId = extras.getString("propertyId");
        }

        db = FirebaseFirestore.getInstance();

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("utilisateurs").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String fullName = "";
                            if (document != null && document.exists())
                                fullName = document.getString("nom") + " " + document.getString("prenom");

                            String contact = contactEditText.getText().toString();
                            String cardNumber = cardNumberEditText.getText().toString();

                            if (contact.isEmpty() || cardNumber.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final String[] amount = {""};
                            String pid = getIntent().getExtras().getString("propertyId");
                            db.collection("Property").document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null && document.exists())
                                            amount[0] = document.getString("tarif");
                                    } else {
                                        Log.i("payement tarif", "failure tarif");
                                    }
                                }
                            });

                            // Créer un objet Map pour les données à enregistrer
                            Map<String, Object> payment = new HashMap<>();
                            payment.put("fullname", fullName);
                            payment.put("contact", contact);
                            payment.put("amount", amount[0]);
                            payment.put("card_number", cardNumber);
                            payment.put("proId", pid);


                            // Ajouter les données à la collection "payments" dans Firestore
                            db.collection("payments")
                                    .add(payment)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Enregistrement réussi
                                            Toast.makeText(getApplicationContext(), "Paiement enregistré avec succès", Toast.LENGTH_SHORT).show();
                                            updatePropertyAsPaid(pid); // Mettre à jour la propriété comme payée
                                            Intent intent = new Intent(PayementActivity.this, tableau_bord.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Enregistrement échoué
                                            Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement du paiement", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    private void updatePropertyAsPaid(String propertyId) {
        db.collection("Property").document(propertyId)
                .update("isPaid", true)
                .addOnSuccessListener(aVoid -> Log.d("PayementActivity", "Property marked as paid"))
                .addOnFailureListener(e -> Log.w("PayementActivity", "Error updating property", e));
    }
}