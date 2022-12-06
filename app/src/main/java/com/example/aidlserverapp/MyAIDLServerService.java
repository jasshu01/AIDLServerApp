package com.example.aidlserverapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyAIDLServerService extends Service {

    int connectionCount;

    public MyAIDLServerService() {
        connectionCount = 0;

    }

    @Override
    public IBinder onBind(Intent intent) {
        connectionCount++;

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        connectionCount--;
        return super.onUnbind(intent);
    }

    private final AIDLServer.Stub binder = new AIDLServer.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPID() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public int getConnectionCount() throws RemoteException {
            return connectionCount;
        }

        @Override
        public String setDisplayData(String packageName, int clientPID, String messageFromClient) throws RemoteException {
            Log.d("myserver", "package name " + packageName);
            Log.d("myserver", "Client id  = " + clientPID);
            Log.d("myserver", "Server id  = " + getPID());
            Log.d("myserver", "Server Connections  = " + getConnectionCount());
            Log.d("myserver", "message = " + messageFromClient);
            Log.d("myserver", "-----------");


            SharedPreferences sp = getSharedPreferences("AIDL_Server", getApplicationContext().MODE_MULTI_PROCESS);
            SharedPreferences.Editor ed = sp.edit();
            String txt = "last Message \n" + "ClientPackageName : " + packageName + "\n" + "Client PID : " + clientPID + "\n Message From Client : " + messageFromClient;

            ed.putString("AIDL_Client_Server", txt);
            ed.apply();



            String sendMessage = "";
            sendMessage += "Package Name : " + getPackageName() + "\n";
            sendMessage += "Package PID : " + getPID() + "\n";
            sendMessage += "Message Received" + "\n";


            if (MainActivity.receivedMessage != null) {
                MainActivity.receivedMessage.setText(txt);
            }


            return sendMessage;

        }
    };
}