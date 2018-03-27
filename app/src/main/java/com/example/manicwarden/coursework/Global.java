package com.example.manicwarden.coursework;
/***
 Frazer J Johnston. Matriculation Number: S1623945
 ***/
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;



public class Global {

    private static Global instance;
    private Global(){}

    public ArrayList<HashMap<String, String>> PlanneditemList = new ArrayList<>();
    public ArrayList<HashMap<String, String>> CurrentitemList = new ArrayList<>();

    public void setPlanneditemList(ArrayList<HashMap<String, String>> list)
    {
        this.PlanneditemList = list;
    }

    public void setCurrentitemList(ArrayList<HashMap<String, String>> list)
    {
        this.CurrentitemList = list;
    }
    public static synchronized Global getInstance(){
        if(instance==null){
            instance=new Global();
        }
        return instance;
    }



    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public static boolean isConnectedtoInternet(Context context) {
        if (isOnline())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}



