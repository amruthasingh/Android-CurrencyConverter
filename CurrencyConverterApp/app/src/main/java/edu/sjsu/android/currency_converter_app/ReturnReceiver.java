package edu.sjsu.android.currency_converter_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ReturnReceiver extends BroadcastReceiver {

    public static String convertedCurrency = "";
    public static String APP_NAME = "ReturnReceiver";
    @Override
    public void onReceive(Context context, Intent intent){
        convertedCurrency = intent.getStringExtra("Converted_Value");

        String log = "Action: " + intent.getAction() + "\n" +
                "Converted Currency: " + convertedCurrency + "\n";
        Log.d( APP_NAME, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

        Intent i = new Intent("ReturnReceiver");
        i.putExtra("Converted_Value", convertedCurrency);
        context.sendBroadcast(i);

    }

}
