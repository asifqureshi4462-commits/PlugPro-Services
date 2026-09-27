package com.plugpro.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * On Android 13 (API 33) and above, POST_NOTIFICATIONS is a dangerous
 * permission that must be requested at runtime, or push notifications
 * (booking alerts, chat messages, FCM) will silently never appear even
 * though the permission is declared in the manifest.
 */
public class NotificationPermissionHelper {

    private static final int REQUEST_CODE_NOTIFICATIONS = 1001;

    public static void requestIfNeeded(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE_NOTIFICATIONS
                );
            }
        }
    }
}
