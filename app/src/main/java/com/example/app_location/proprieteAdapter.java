package com.example.app_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class proprieteAdapter extends RecyclerView.Adapter<proprieteAdapter.PropertyViewHolder> {

    private List<Property> propertyList;
    private List<Property> filteredPropertyList;
    private Context context;


    public proprieteAdapter(List<Property> propertyList, Context context) {
        this.propertyList = propertyList;
        this.filteredPropertyList = new ArrayList<>(propertyList);
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.model, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = filteredPropertyList.get(position);


        holder.type.setText(property.getType());
        holder.tarif.setText(property.getTarif() + " DH");
        holder.ville.setText(property.getVille());
        holder.quartier.setText(property.getQuartier());
        holder.disponibilite.setText(property.isPaid() ? "Non disponible" : "Disponible");

        if (property.getPhoto() != null && !property.getPhoto().isEmpty()) {
            String firstImageUrl = property.getPhoto().get(0);
            // Utilisez une bibliothèque d'image comme Glide ou Picasso pour charger l'image
            Glide.with(context).load(firstImageUrl).into(holder.photo);
        }

        if (property.isFavorite()) {
            holder.imageHeart.setImageResource(R.drawable.orangeheart); // Utiliser une icône de favori remplie
        } else {
            holder.imageHeart.setImageResource(R.drawable.baseline_favorite_24); // Utiliser une icône de favori non remplie
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

        holder.imageHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FavorisDB favdb = new FavorisDB(v.getContext());
                if(property.isFavorite())
                    favdb.removeFavoris(property.getId());
                else
                    favdb.addFavoris(property.getId());
                toggleFavoriteStatus(property);

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
                        property.getVille().toLowerCase().contains(text.toLowerCase()) ||
                        property.getQuartier().toLowerCase().contains(text.toLowerCase()) ||

                property.getTarif().toLowerCase().contains(text.toLowerCase())) {
                    uniqueProperties.add(property);
                }
            }
            filteredPropertyList.addAll(uniqueProperties);
        }
        notifyDataSetChanged();
    }



    private void toggleFavoriteStatus(Property property) {

        boolean newFavoriteStatus = !property.isFavorite(); // Inverse le statut favori actuel
        Log.d("toggleFavoriteStatus", "Mise à jour du favori pour la propriété: " + property.getId() + " à " + newFavoriteStatus);
        property.setFavorite(newFavoriteStatus);
        notifyDataSetChanged();
        Toast.makeText(context, "Favori mis à jour", Toast.LENGTH_SHORT).show();
        Log.d("Firestore", "Favori mis à jour avec succès pour la propriété: " + property.getId());

    }



    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView type, quartier, ville, tarif,disponibilite;
        ImageView photo,imageHeart;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tvType);
            quartier = itemView.findViewById(R.id.tvQuartier);
            ville = itemView.findViewById(R.id.tvVille);
            tarif = itemView.findViewById(R.id.tvTarif);
            photo = itemView.findViewById(R.id.image);
            disponibilite=itemView.findViewById(R.id.tvDisponibilite);
            imageHeart = itemView.findViewById(R.id.imageHeart);

        }


    }
}
