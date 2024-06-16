package com.example.app_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        Glide.with(context).load(property.getPhoto()).into(holder.photo);

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




  /*  private boolean isPriceInRange(String propertyPrice, String selectedPriceRange) {
        // Split the selected price range into minimum and maximum values
        String[] priceRange = selectedPriceRange.split("-");
        int minPrice = Integer.parseInt(priceRange[0]);
        int maxPrice = Integer.parseInt(priceRange[1]);

        // Extract the property price
        int propertyPriceInt = Integer.parseInt(propertyPrice);

        // Check if the property price is within the selected range
        return propertyPriceInt >= minPrice && propertyPriceInt <= maxPrice;
    }*/



    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView type, quartier, ville, tarif;
        ImageView photo;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tvType);
            quartier = itemView.findViewById(R.id.tvQuartier);
            ville = itemView.findViewById(R.id.tvVille);
            tarif = itemView.findViewById(R.id.tvTarif);
            photo = itemView.findViewById(R.id.image);
        }
    }
}
