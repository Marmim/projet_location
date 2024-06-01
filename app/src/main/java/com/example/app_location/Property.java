package com.example.app_location;


    public class Property {
        private String photo;
        private String description;
        private String contact;
        private String tarif;
        private String ville;
        private String quartier;
        private String type;

        public Property() {
            // Nécessaire pour la sérialisation/désérialisation de Firestore
        }

        // Getters et Setters
        public String getPhoto() { return photo; }
        public void setPhoto(String photo) { this.photo = photo; }
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
    }


