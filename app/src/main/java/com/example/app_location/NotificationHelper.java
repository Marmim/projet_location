package com.example.app_location;

// NotificationHelper.java

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AIzaSyDHynPsigaOqir66zhLd102X2ugoZdZFEU"; // Remplacez par votre clé d'API Firebase

    public static void sendNotification(String title, String message) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/all_notifications");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", message);
            json.put("notification", notificationObj);
            JSONObject dataObj = new JSONObject();
            dataObj.put("message", message);
            json.put("data", dataObj);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .header("Authorization", "key=" + SERVER_KEY)
                .url(FCM_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to send notification", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Notification sent successfully");
                } else {
                    Log.e(TAG, "Failed to send notification. Response: " + response.body().string());
                }
            }
        });
    }
}
