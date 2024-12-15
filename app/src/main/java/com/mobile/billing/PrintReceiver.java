package com.mobile.billing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class PrintReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("data","intent");
        Uri dataUri = intent.getData();
        if (dataUri != null) {
            String jsonData = dataUri.getQueryParameter("data");
            Log.d("Service","data");
            Intent serviceIntent = new Intent(context, PrintService.class);
            serviceIntent.putExtra("data", jsonData);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent); // Required for Android 8+
            }
        }
    }
}
