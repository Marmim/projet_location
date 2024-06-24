package com.example.app_location;

public class NotificationItem {
    private String title;
    private String body;

    public NotificationItem() {
        // Default constructor required for calls to DataSnapshot.getValue(NotificationItem.class)
    }

    public NotificationItem(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
