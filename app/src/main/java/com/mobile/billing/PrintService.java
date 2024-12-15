package com.mobile.billing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class PrintService extends Service {

    private static final String CHANNEL_ID = "print_service_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the foreground notification
        startForegroundNotification();

        String jsonData = intent.getStringExtra("data");
        Log.d("data","check");
        if (jsonData != null) {
            try {
                Log.d("Service",jsonData);
                JSONObject invoiceData = new JSONObject(jsonData);
                processPrintJob(invoiceData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return START_STICKY;
    }

    private void processPrintJob(JSONObject invoiceData) {
        // Add your printer integration logic here
        PrinterManager printerManager = new PrinterManager(getApplicationContext());
        printerManager.printInvoice(invoiceData);

        // Stop the service after the print job is done
        stopForeground(true);
        stopSelf();
    }

    private void startForegroundNotification() {
        String channelName = "Print Service";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Printing")
                .setContentText("Processing your print job...")
                .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's icon
                .build();

        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not bound
    }
}
