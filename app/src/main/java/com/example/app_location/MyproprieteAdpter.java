package com.example.app_location;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyproprieteAdpter extends RecyclerView.Adapter<MyproprieteAdpter.PropertyViewHolder> {

    private static List<Property> propertyList;
    private List<Property> filteredPropertyList;
    private Context context;
    private FirebaseFirestore db;

    public MyproprieteAdpter(List<Property> propertyList, Context context) {
        this.propertyList = propertyList;
        this.filteredPropertyList = new ArrayList<>(propertyList);
        this.context = context;
        this.db = FirebaseFirestore.getInstance(); // Initialiser Firebase Firestore
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.modelmypropriete, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Property property = filteredPropertyList.get(position);

        holder.type.setText(property.getType());
        holder.tarif.setText(property.getTarif() + " DH");
        holder.quartier.setText(property.getQuartier());
        holder.disponibilite.setText(property.isPaid() ? "Non disponible" : "Disponible");

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(position, property.getId());
            }
        });


        if (property.getPhoto() != null && !property.getPhoto().isEmpty()) {
            String firstImageUrl = property.getPhoto().get(0);
            // Utilisez une bibliothèque d'image comme Glide ou Picasso pour charger l'image
            Glide.with(context).load(firstImageUrl).into(holder.photo);
            String imageUrl = property.getPhoto().get(0); // Première URL de la liste
            Log.d("PropertyAdapter", "Loading image URL: " + imageUrl); // Ajout de log pour vérifier l'URL
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.photo);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsPropriete.class);
                Bundle b = new Bundle();
                b.putString("propertyId", property.getId());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredPropertyList.size();
    }

    public void filterList(String text) {
        filteredPropertyList.clear();
        if (text.isEmpty()) {
            filteredPropertyList.addAll(new HashSet<>(propertyList));
        } else {
            Set<Property> uniqueProperties = new HashSet<>();
            for (Property property : propertyList) {
                if (property.getType().toLowerCase().contains(text.toLowerCase()) ||
                        property.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        property.getQuartier().toLowerCase().contains(text.toLowerCase()) ||
                        property.getTarif().toLowerCase().contains(text.toLowerCase())) {
                    uniqueProperties.add(property);
                }
            }
            filteredPropertyList.addAll(uniqueProperties);
        }
        notifyDataSetChanged();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView type, quartier, ville, disponibilite, tarif;
        ImageView photo;
        ImageButton btnDelete;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tvType);
            quartier = itemView.findViewById(R.id.tvQuartier);
            disponibilite = itemView.findViewById(R.id.tvDisponibilite);
            tarif = itemView.findViewById(R.id.tvTarif);
            photo = itemView.findViewById(R.id.image);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showDeleteConfirmationDialog(int position, String propertyId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Voulez-vous vraiment supprimer cette propriété ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Supprimer la propriété de la liste et de Firestore
                removeProperty(position, propertyId);
            }
        });
        builder.setNegativeButton("Non", null);
        builder.show();
    }

    public void removeProperty(int position, String propertyId) {
        // Supprimer de Firestore
        db.collection("Property").document(propertyId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Supprimer de la liste principale et de la liste filtrée
                    Property propertyToRemove = filteredPropertyList.get(position);
                    propertyList.remove(propertyToRemove);
                    filteredPropertyList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    // Gérer l'échec de la suppression
                    // Par exemple, afficher un message à l'utilisateur
                });
    }

    public void updatePropertyStatus(String propertyId, boolean isPaid) {
        for (Property property : propertyList) {
            if (property.getId().equals(propertyId)) {
                property.setPaid(isPaid);
                notifyDataSetChanged();
                break;
            }
        }
    }

}
