package com.example.app_location;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuiEtes extends AppCompatActivity {
 private Button buttonPropr,buttonLoca ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qui_etes);
        // Récupérer les vues à partir du layout XML
        buttonPropr = findViewById(R.id.buttonPropr);
        buttonLoca = findViewById(R.id.buttonLoca);
        buttonLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuiEtes.this, locataire.class);
                intent.putExtra("role", "locataire");
                startActivity(intent);
            }
        });

        buttonPropr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuiEtes.this, proprietaire.class);
                intent.putExtra("role", "proprietaire");
                startActivity(intent);
            }
        });


    }
}