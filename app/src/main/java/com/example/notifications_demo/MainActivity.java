package com.example.notifications_demo;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My_Channel_ID";
    private static final int NOTIFICATION_ID = 100;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private NotificationManager nm;
    private Button btnShowNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowNotification = findViewById(R.id.btnShowNotification);

        // Check permission for Android 13+ (API 33+)
      btnShowNotification.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                  if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)
                          != PackageManager.PERMISSION_GRANTED) {
                      ActivityCompat.requestPermissions(MainActivity.this,
                              new String[]{Manifest.permission.POST_NOTIFICATIONS},
                              PERMISSION_REQUEST_CODE);
                  } else {
                      showNotification();
                  }
              } else {
                  showNotification();
              }
          }
      });
    }

    private void showNotification() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_notification, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "New Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);

            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("New Notification")
                    .setContentText("New Message from RAM")
                    .setSubText("SubText Example")
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("New Notification")
                    .setContentText("New Message from RAM")
                    .setSubText("SubText Example")
                    .build();
        }

        nm.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showNotification();
            }
        }
    }
}
