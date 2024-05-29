package com.example.app_location;

import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class proprieteAdapter extends RecyclerView.Adapter<proprieteAdapter.PropertyViewHolder> {

        private List<Property> propertyList;

        public proprieteAdapter(List<Property> propertyList) {
            this.propertyList = propertyList;
        }

        @NonNull
        @Override
        public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liste_propriete, parent, false);
            return new PropertyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
            Property property = propertyList.get(position);

            holder.villeTextView.setText(property.getVille() + ", " + property.getQuartier());
            holder.typeTextView.setText(property.getType());
            holder.tarifTextView.setText(property.getTarif());
            // Charger l'image depuis l'URL (utilisez une bibliothèque comme Picasso ou Glide)
            Glide.with(holder.itemView.getContext()).load(property.getPhoto()).into(holder.imageView);
        }
        @Override
        public int getItemCount() {
            return propertyList.size();
        }

        public static class PropertyViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView villeTextView;
            public TextView typeTextView ;

            public TextView tarifTextView;

            public PropertyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView3);
                villeTextView = itemView.findViewById(R.id.property_ville);
                typeTextView = itemView.findViewById(R.id.property_type);
                tarifTextView = itemView.findViewById(R.id.property_tarif);
            }
        }
    }


