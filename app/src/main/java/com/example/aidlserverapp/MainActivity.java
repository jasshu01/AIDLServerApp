package com.example.aidlserverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aidlclientapp.AIDLClient;

public class MainActivity extends AppCompatActivity {
    TextView receivedMessage = null;
    EditText sendMessage;
    Button sendbutton;

    AIDLClient aidlClient = null;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            aidlClient = AIDLClient.Stub.asInterface(iBinder);
            Log.d("myserver", "Server -> client");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            aidlClient = null;
        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receivedMessage = findViewById(R.id.receivedMessage);
        sendMessage = findViewById(R.id.sendMessage);
        sendbutton = findViewById(R.id.sendbutton);

        SharedPreferences sp = getSharedPreferences("AIDLServer", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();


        Intent intent = new Intent("com.jasshugarg.AIDLClient");
        intent.setComponent(new ComponentName("com.example.aidlclientapp1", "com.example.aidlclientapp1.MyAIDLClientService"));
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aidlClient != null) {
                    try {
                        String rcvdMsg = aidlClient.setDisplayData(getPackageName(), getTaskId(), sendMessage.getText().toString());
                        Toast.makeText(MainActivity.this, "Message Sent To Server", Toast.LENGTH_SHORT).show();
                        sendMessage.setText("");
                        receivedMessage.setText(rcvdMsg);

                        ed.putString("AIDL_Client_Server", rcvdMsg);
                        ed.apply();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        receivedMessage.setText(getSharedPreferences("AIDL_Server", MODE_PRIVATE).getString("AIDL_Client_Server", "Nothing to Show"));


//        Log.d("myserver", "onstart" + getSharedPreferences("AIDL_Server", MODE_PRIVATE).getString("AIDL_Client_Server", "Nothing to Show"));
    }


}