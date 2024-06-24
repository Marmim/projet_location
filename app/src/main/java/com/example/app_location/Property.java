package com.example.app_location;

import java.io.Serializable;
import java.util.List;


public class Property implements Serializable{
        private String id;
        private List<String> photo;

        public String getId() {
            return id;
        }


        public void setId(String id) {
            this.id = id;
        }

        private String description;
        private String contact;
        private String tarif;
        private String ville;
        private String quartier;
        private String type;
         private boolean favorite;


        public Property() {
            // Nécessaire pour la sérialisation/désérialisation de Firestore
        }

        public Property(List<String> photo, String description, String contact, String tarif, String ville, String quartier, String type) {
            this.photo = photo;
            this.description = description;
            this.contact = contact;
            this.tarif = tarif;
            this.ville = ville;
            this.quartier = quartier;
            this.type = type;
            this.favorite = false;
        }

        // Getters et Setters
        public List<String> getPhoto() { return photo; }
        public void setPhoto(List<String> photo) { this.photo = photo; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContact() { return contact; }
        public void setContact(String contact) { this.contact = contact; }
        public String getTarif() { return tarif; }
        public void setTarif(String tarif) { this.tarif = tarif; }
        public String getVille() { return ville; }
        public void setVille(String ville) { this.ville = ville; }
        public String getQuartier() { return quartier; }
        public void setQuartier(String quartier) { this.quartier = quartier; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    }


