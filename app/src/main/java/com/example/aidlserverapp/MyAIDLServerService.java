package com.example.aidlserverapp;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyAIDLServerService extends Service {


    private static int connectionCount;

    public MyAIDLServerService() {
        connectionCount = 0;
        Log.d("myserver", " " + connectionCount);
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.d("myserver", " " + intent.getPackage());
        Log.d("myserver", " " + connectionCount);
        MyAIDLServerService.connectionCount++;
        Log.d("myserver", " " + connectionCount);
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public boolean onUnbind(Intent intent) {
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

            return MyAIDLServerService.connectionCount;
        }

        @Override
        public String setDisplayData(String packageName, int clientPID, String messageFromClient) throws RemoteException {
            Log.d("myserver", "package name " + packageName);
            Log.d("myserver", "Client id  = " + clientPID);
            Log.d("myserver", "Server id  = " + getPID());
//            Log.d("myserver", "Server Connections  = " + getConnectionCount());
            Log.d("myserver", "message = " + messageFromClient);
            Log.d("myserver", "-----------");


            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    int i = 0;
                    while (i < 50) {
                        Log.d("myserver", packageName + " " + i++);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
            SharedPreferences sp = getSharedPreferences("AIDL_Server", getApplicationContext().MODE_MULTI_PROCESS);
            SharedPreferences.Editor ed = sp.edit();
            String txt = "last Message \n" + "ClientPackageName : " + packageName + "\n" + "Client PID : " + clientPID + "\n Message From Client : " + messageFromClient;

            ed.putString("AIDL_Client_Server", txt);
            ed.apply();


            String sendMessage = "";
            sendMessage += "Package Name : " + getPackageName() + "\n";
            sendMessage += "Package PID : " + getPID() + "\n";
            sendMessage += "Message Received" + "\n";


            return sendMessage;

        }
    };
}