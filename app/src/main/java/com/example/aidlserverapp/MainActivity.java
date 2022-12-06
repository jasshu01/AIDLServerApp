package com.example.aidlserverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static TextView receivedMessage = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receivedMessage = findViewById(R.id.receivedMessage);

        Log.d("myserver", "oncreate");
//        receivedMessage.setText(getSharedPreferences("AIDL_Server", MODE_PRIVATE).getString("AIDL_Client_Server", "Nothing to Show"));

    }

    @Override
    protected void onStart() {
        super.onStart();


        receivedMessage.setText(getSharedPreferences("AIDL_Server", MODE_PRIVATE).getString("AIDL_Client_Server", "Nothing to Show"));


        Log.d("myserver", "onstart" + getSharedPreferences("AIDL_Server", MODE_PRIVATE).getString("AIDL_Client_Server", "Nothing to Show"));
    }
}