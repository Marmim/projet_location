package com.example.app_location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class locataire  extends AppCompatActivity {
    private Button suivantButton,RetourButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locataire);
        suivantButton = findViewById(R.id.suivantButton);
        RetourButton=findViewById(R.id.RetourButton);
        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(locataire.this, Maison.class);
                startActivity(intent);
            }
        });
        RetourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(locataire.this, QuiEtes.class);
                startActivity(intent);
            }
        });
    }
    }

