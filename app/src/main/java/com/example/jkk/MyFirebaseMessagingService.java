package com.example.jkk;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Directly access data from RemoteMessage
            String title = remoteMessage.getData().get("title");
            String text = remoteMessage.getData().get("text");
            String key1 = remoteMessage.getData().get("key1");
            String key2 = remoteMessage.getData().get("key2");

            // Show notification at a specific time (e.g., 8:00 AM)
            scheduleNotification(title, text, key1, key2);

            // Save notification data to Firestore
            saveNotificationToFirestore(title, text, key1, key2);
        }
    }

    private void scheduleNotification(String title, String text, String key1, String key2) {
        createNotificationChannel();

        Intent intent = new Intent(this, popupAdminactivity.class);
        Toast.makeText(this, "ADDING", Toast.LENGTH_SHORT).show();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the time to show the notification (e.g., 8:00 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_none_24)
                .setContentTitle(title)
                .setContentText(text + " - " + key1 + " - " + key2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void saveNotificationToFirestore(String title, String text, String key1, String key2) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("title", title);
        notificationData.put("text", text);
        notificationData.put("key1", key1);
        notificationData.put("key2", key2);

        // Save notification data to Firestore
        db.collection("notifications")
                .add(notificationData)
                .addOnSuccessListener(documentReference -> {
                    // Notification data saved successfully to Firestore
                    Toast.makeText(getApplicationContext(), "Notification data saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save notification data to Firestore
                    Toast.makeText(getApplicationContext(), "Failed to save notification data to Firestore", Toast.LENGTH_SHORT).show();
                });
    }
}
