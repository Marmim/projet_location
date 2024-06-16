package com.example.app_location;

import android.content.Context;
import android.content.Intent;
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
    private Context context;

    public proprieteAdapter(List<Property> propertyList, Context context) {
        this.propertyList = propertyList;
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
        Property property = propertyList.get(position);

        holder.type.setText(property.getType());
        holder.description.setText(property.getDescription());
        holder.tarif.setText(property.getTarif() + " dh");
        holder.ville.setText(property.getVille());
        holder.quartier.setText(property.getQuartier());

        Glide.with(context).load(property.getPhoto()).into(holder.photo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsPropriete.class);
                intent.putExtra("propertyId", property.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView type, quartier, ville, tarif, description, contact;
        ImageView photo;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.tvType);
            quartier = itemView.findViewById(R.id.tvQuartier);
            ville = itemView.findViewById(R.id.tvVille);
            tarif = itemView.findViewById(R.id.tvTarif);
            description = itemView.findViewById(R.id.tvDescription);
            photo = itemView.findViewById(R.id.image);
            contact = itemView.findViewById(R.id.contactTextView);

        }
    }
}
