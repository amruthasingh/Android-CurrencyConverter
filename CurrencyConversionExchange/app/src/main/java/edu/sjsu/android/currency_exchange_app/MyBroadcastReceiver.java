package edu.sjsu.android.currency_exchange_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.sql.Struct;

import static android.support.v4.content.ContextCompat.startActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static double dollar_val;
    public static String convertTo;
    @Override
    public void onReceive(Context context, Intent intent){
        dollar_val = Double.parseDouble(intent.getStringExtra("Dollar_Amount"));
        convertTo = intent.getStringExtra("Convert_To");

        String log = "Action: " + intent.getAction() + "\n" +
                "Dollar Amount: " + dollar_val + "\n" +
                "Convert To: " + convertTo + "\n";

        Log.d( "MyBroadcastReceiver",log);

        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

    }

    public double getCurrency() {
        return dollar_val;
    }

    public String getConvertTo() {
        return convertTo;
    }

}
