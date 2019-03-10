package edu.sjsu.android.currency_converter_app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String APP_NAME = "CurrencyConverterApp";
    public static EditText enterDollarAmount;
    public static Spinner spinner;
    public static BroadcastReceiver br;
    public static TextView tv_currency;
    public static Button  b_convert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_currency = (TextView) findViewById(R.id.convertedvalue);
        b_convert = (Button) findViewById(R.id.convert);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(APP_NAME, "Broadcast receiver received");
                Bundle b = intent.getExtras();
                if(b != null){
                    String output = "Dollar amount $" + enterDollarAmount.getText().toString() +
                            " converted to " + b.getString("Converted_Value") + " " +
                            spinner.getSelectedItem().toString();
                    tv_currency.setText(output);
                   Log.i(APP_NAME, output);
                }
            }
        };


        b_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(APP_NAME, "Convert button pressed");
                enterDollarAmount = (EditText)findViewById(R.id.DollarAmount);
                spinner = (Spinner)findViewById(R.id.spinner);
                Intent intent = new Intent();
                intent.setAction("edu.sjsu.android.currency_converter_app");
                intent.putExtra("Dollar_Amount", enterDollarAmount.getText().toString());
                intent.putExtra("Convert_To", spinner.getSelectedItem().toString());
                Log.i(APP_NAME, spinner.getSelectedItem().toString());
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setComponent(new ComponentName("edu.sjsu.android.currency_exchange_app",
                        "edu.sjsu.android.currency_exchange_app.MyBroadcastReceiver"));
                sendBroadcast(intent);
            }
        });

        this.registerReceiver(br, new IntentFilter("ReturnReceiver"));
    }

    public void finishMainActivity(View v) {
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(this.br);
    }
}
