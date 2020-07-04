package com.example.ipark.Misc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {

    public static void Request_GPS(Activity act, int code){
        ActivityCompat.requestPermissions(act,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},code);
    }

    public static void Request_Coarse(Activity act, int code){
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},code);
    }

    public static boolean check_Coarse(Activity act){
        int result = ContextCompat.checkSelfPermission(act,android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean check_GPS(Activity act){
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void Request_Camera(Activity act,int code){
        ActivityCompat.requestPermissions(act,new String[]{Manifest.permission.CAMERA},code);
    }

    public static boolean check_Camera(Activity act){
        int result = ContextCompat.checkSelfPermission(act,Manifest.permission.CAMERA);
        return result==PackageManager.PERMISSION_GRANTED;
    }
}
