package com.example.app_location;

public class Conversation {
    private String id;
    private String name;

    // Constructeur par défaut requis pour les appels à
    // DataSnapshot.getValue(Conversation.class)
    public Conversation() {}

    public Conversation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
