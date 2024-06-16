package com.example.app_location;
 import static android.content.ContentValues.TAG;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.View;
import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.android.gms.tasks.Task;
 import com.google.firebase.auth.AuthCredential;
 import com.google.firebase.auth.EmailAuthProvider;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

    public class info_perso extends AppCompatActivity {
        TextView fname,lname,email,phone,adress;
        FirebaseAuth mauth;
        FirebaseFirestore fstore;
        String userID;
        ImageButton backButton;
        private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.info_perso);
            fname = findViewById(R.id.fnamtxt);
            lname = findViewById(R.id.lnametxt);
            email= findViewById(R.id.emailtxt);
            backButton=findViewById(R.id.backButton);
            mauth=FirebaseAuth.getInstance();
            fstore=FirebaseFirestore.getInstance();



            // Récupérer le rôle de l'utilisateur
            Intent intent = getIntent();
            String role = intent.getStringExtra("role");

           // Gestion du clic sur le bouton de retour
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirection en fonction du rôle
                    if ("locataire".equals(role)) {
                        Intent intent = new Intent(info_perso.this, ProfilLocataire.class);
                        startActivity(intent);
                    } else if ("proprietaire".equals(role)) {
                        Intent intent = new Intent(info_perso.this, ProfilProprietaire.class);
                        startActivity(intent);
                    }
                }
            });


            userID = mauth.getCurrentUser().getEmail();
            DocumentReference documentReference = fstore.collection("utilisateurs").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    fname.setText(documentSnapshot.getString("nom"));
                    lname.setText(documentSnapshot.getString("prenom"));
                    email.setText(documentSnapshot.getString("email"));
                }
            });



            oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
            newPasswordEditText = findViewById(R.id.newPasswordEditText);
            confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

            Button changePasswordButton = findViewById(R.id.changePasswordButton);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String oldPassword = oldPasswordEditText.getText().toString();
                    String newPassword = newPasswordEditText.getText().toString();
                    String confirmPassword = confirmPasswordEditText.getText().toString();

                    if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                        Toast.makeText(info_perso.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isPasswordValid(newPassword, confirmPassword)) {
                        Toast.makeText(info_perso.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        changePassword(user, oldPassword, newPassword);
                    } else {
                        Toast.makeText(info_perso.this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void changePassword(FirebaseUser user, String oldPassword, String newPassword) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(info_perso.this, "Mot de passe changé avec succès", Toast.LENGTH_SHORT).show();
                                    // Mettre à jour le mot de passe dans Firestore
                                    updatePasswordInFirestore(user.getEmail(), newPassword);
                                    // Effacer les champs de mot de passe
                                    oldPasswordEditText.setText("");
                                    newPasswordEditText.setText("");
                                    confirmPasswordEditText.setText("");
                                } else {
                                    Toast.makeText(info_perso.this, "Échec du changement de mot de passe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(info_perso.this, "Votre mot de passe est incorrecte", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private boolean isPasswordValid(String newPassword, String confirmPassword) {
            return newPassword.equals(confirmPassword);
        }

        private void updatePasswordInFirestore(String email, String newPassword) {
            DocumentReference documentReference = fstore.collection("utilisateurs").document(email);
            documentReference.update("password", newPassword)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Mot de passe mis à jour dans Firestore");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Erreur lors de la mise à jour du mot de passe dans Firestore", e);
                        }
                    });
        }
        }


