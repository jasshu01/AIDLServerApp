package com.example.aidlserverapp;

import android.app.Service;
import android.content.Intent;
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
        public void setDisplayData(String packageName, int clientPID, String messageFromClient) throws RemoteException {
            Log.d("myserver", "package name " + packageName);
            Log.d("myserver", "Client id  = " + clientPID);
            Log.d("myserver", "Server id  = " + getPID());
            Log.d("myserver", "Server Connections  = " + getConnectionCount());
            Log.d("myserver", "message = " + messageFromClient);
            Log.d("myserver", "-----------");

//            if (MainActivity.receivedMessage != null) {
//
//                String txt = "ClientPackageName : " + packageName + "\n" + "Client PID : " + clientPID + "\n Message From Client : " + messageFromClient;
//                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
//                MainActivity.receivedMessage.setText("ClientPackageName : " + packageName + "\n" + "Client PID : " + clientPID + "\n Message From Client : " + messageFromClient);
//            }
        }
    };
}