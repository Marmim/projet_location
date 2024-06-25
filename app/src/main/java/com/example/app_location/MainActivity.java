package com.example.app_location;


import static android.content.ContentValues.TAG;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
    public static final String CHANNEL_ID = "my_channel_01";


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

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),QuiEtes.class);
            startActivity(intent);
            finish();
        }
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
                                    sendNotification("Nouvelle connexion", "Nous sommes heureux de vous revoir");
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
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.drawablelogo);
        builder.setContentTitle("Nouvelle Connexion");
        builder.setContentText("Nous sommes heureux de vous revoir.");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);// Replace with your own icon
// Changed to HIGH

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}