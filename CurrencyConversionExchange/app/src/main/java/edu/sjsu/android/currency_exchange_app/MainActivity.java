package edu.sjsu.android.currency_exchange_app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    public static double gbp;
    public static double eur;
    public static double inr;
    public static double currency;

    public static String currencyConvertTo;
    public static double convertedCurrency;

    private static Button button;
    public static TextView tv_dollar, tv_convertTo;

    public static String APP_NAME = "CurrencyExchange";

    MyBroadcastReceiver br = new MyBroadcastReceiver();

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_dollar = (TextView)findViewById(R.id.dollarAmount);
        tv_convertTo = (TextView)findViewById(R.id.convertTo);
        button = (Button)findViewById(R.id.apply);

        IntentFilter filter = new IntentFilter("android.intent.action.MAIN");
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);

        this.registerReceiver(br, new IntentFilter("MyBroadcastReceiver"));

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                currency = br.getCurrency();
                currencyConvertTo = br.getConvertTo();
                String api_response = GetAPIResponse();
                Log.v(APP_NAME,api_response);
                Log.i(APP_NAME, currency + " -- " + currencyConvertTo);
                String convertedCurrency = String.valueOf(currencyConversion(currency, currencyConvertTo, api_response));
                Log.v(APP_NAME,convertedCurrency);


                tv_dollar.setText("Dollar Amount: $"+currency);
                tv_convertTo.setText("Convert to: "+currencyConvertTo);

                Intent intent = new Intent();
                intent.setAction("edu.sjsu.android.currency_exchange_app");
                intent.putExtra("Converted_Value", convertedCurrency);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setComponent(new ComponentName("edu.sjsu.android.currency_converter_app",
                        "edu.sjsu.android.currency_converter_app.ReturnReceiver"));
                Log.i("Intent is sent back", String.valueOf(intent.getExtras()));
                sendBroadcast(intent);

            }
        });


    }

    public double currencyConversion(double amount, String convertTo, String apiResponse){
        JSONParser myparser = new JSONParser();
        Object s = null;
        Log.v("Entering  method",apiResponse );
        try
        {
            JSONObject myobj = (JSONObject)myparser.parse(apiResponse);
            JSONObject myRateObj = (JSONObject)myobj.get("rates");
            s = myRateObj.get("GBP");
            String gbpCurrency = s.toString();
            Log.v("converted",gbpCurrency);
            gbp = Double.parseDouble(gbpCurrency);
            s = myRateObj.get("EUR");
            String eurCurrency = s.toString();
            eur = Double.parseDouble(eurCurrency);
            s = myRateObj.get("INR");
            String inrCurrency = s.toString();
            inr = Double.parseDouble(inrCurrency);

        }
        catch (org.json.simple.parser.ParseException e)
        {
            e.printStackTrace();
        }

        Log.i(APP_NAME, "ConvertTo: " + convertTo);
        convertedCurrency = 0.0;
        switch(convertTo){
            case "British Pound":
                convertedCurrency = amount * gbp;
                break;
            case "Indian Rupee":
                convertedCurrency = amount * inr;
                break;
            case "Euro":
                convertedCurrency = amount * eur;
                break;
            default:
                break;

        }

        return convertedCurrency;
    }


    public String GetAPIResponse()
    {
        enableStrictMode();

        URL urlForGetRequest = null;
        String apiResponse = null;
        try {
            urlForGetRequest = new URL("https://api.openrates.io/latest?base=USD");
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                apiResponse = response.toString();
                Log.v(APP_NAME,apiResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse;

    }

    public void finishMainActivity(View v) {
        MainActivity.this.finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(this.br);

    }

}