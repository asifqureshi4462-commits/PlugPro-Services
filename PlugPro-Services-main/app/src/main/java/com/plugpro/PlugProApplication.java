package com.plugpro;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import com.google.firebase.FirebaseApp;

public class PlugProApplication extends Application {
    public static final String NOTIFICATION_CHANNEL_ID = "plugpro_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        createNotificationChannel();
        // NOTE: Category/provider seed data is now triggered from MainActivity /
        // ProviderMainActivity once a user is authenticated, because Firestore
        // security rules reject writes from a signed-out user (see firestore.rules).
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PlugPro Booking Alerts";
            String description = "Notifications for bookings, provider status, and chat messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
