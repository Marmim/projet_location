package com.example.app_location;


import static android.content.ContentValues.TAG;

import static com.example.app_location.NotificationHelper.sendNotification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailedit,passwordedit;
    private Button connexion;
    private String email,password;
    private FirebaseAuth mAuth;
    private Button inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //récupérez l'instance partagée de l'objet FirebaseAuth :
        mAuth = FirebaseAuth.getInstance();
        //recuperer le views
        emailedit=findViewById(R.id.usernameEditText);
        passwordedit=findViewById(R.id.passwordEditText);
        connexion = findViewById(R.id.loginButton);
        inscription = findViewById(R.id.signupButton);
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AuthentifActivity2.class);
                startActivity(intent);
            }
        });


        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailedit.getText().toString();
                password=passwordedit.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    NotificationHelper.sendNotification("Nouvelle connexion", "Nous sommes heureux de vous revoir");
                                    Intent intent = new Intent(getApplicationContext(),QuiEtes.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }

        });

    }
}