package com.example.app_location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class proprieteAdapter extends RecyclerView.Adapter<proprieteAdapter.PropertyViewHolder> {


    Context context;
    ArrayList<Property> PropretyArrayList;

    public proprieteAdapter(Context context, ArrayList<Property> PropretyArrayList) {
        this.context = context;
        this.PropretyArrayList = PropretyArrayList;
    }

    @NonNull
    @Override
    public proprieteAdapter.PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(context).inflate(R.layout.liste_propriete,parent,false);
        return new PropertyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull proprieteAdapter.PropertyViewHolder holder, int position) {

        Property property=PropretyArrayList.get(position);

        // Utilisez Glide pour charger l'image
        Glide.with(context)
                .load(property.getPhoto())
                .into(holder.photo);
        holder.type.setText(property.getType());
        holder.description.setText(property.getDescription());
        holder.tarif.setText(property.getTarif());
        holder.ville.setText(property.getVille());
        holder.quartier.setText(property.getQuartier());



    }

    @Override
    public int getItemCount() {
        return PropretyArrayList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder{

        TextView type,quartier,ville,tarif,description;
        ImageView photo;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.tvType);
            quartier=itemView.findViewById(R.id.tvQuartier);
            ville=itemView.findViewById(R.id.tvVille);
            tarif=itemView.findViewById(R.id.tvTarif);
            description=itemView.findViewById(R.id.tvDescription);
            photo=itemView.findViewById(R.id.image);

        }
    }
}