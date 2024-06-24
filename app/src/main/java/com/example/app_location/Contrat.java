package com.example.app_location;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class Contrat extends AppCompatActivity {

    EditText urlField;
    Button downloadButton;
    Button renderContratButton;
    Button confirmButton;

    private static final int PICK_PDF_REQUEST = 1;
    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrat);

        urlField = findViewById(R.id.btn_url);
        downloadButton = findViewById(R.id.btn_download_pdf);
        renderContratButton = findViewById(R.id.contrat);
        confirmButton = findViewById(R.id.ConfirmContrat);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlField.getText().toString().trim();
                if (!url.isEmpty()) {
                    Download_PDF_View_Intent(url);
                } else {
                    Toast.makeText(Contrat.this, "Veuillez entrer une URL valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        renderContratButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
                    openPDFFile(selectedFilePath);
                } else {
                    Toast.makeText(Contrat.this, "Aucun fichier PDF sélectionné", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Download_PDF_View_Intent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Aucune application disponible pour ouvrir ce fichier", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Sélectionner un fichier PDF"), PICK_PDF_REQUEST);
    }

    private void openPDFFile(String filePath) {
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Aucune application disponible pour ouvrir ce fichier", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedFileUri = data.getData();
            selectedFilePath = selectedFileUri.getPath();
            // Ici, vous pouvez traiter le chemin du fichier sélectionné (par exemple, l'afficher dans un TextView)
            Toast.makeText(this, "Fichier sélectionné : " + selectedFilePath, Toast.LENGTH_SHORT).show();
        }
    }
}