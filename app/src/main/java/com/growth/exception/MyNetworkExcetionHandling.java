package com.growth.exception;

import android.util.Log;

import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by SSL-D on 2016-09-05.
 */

public class MyNetworkExcetionHandling {
    public static void excute(Throwable error, ProgressControl progress, ToastControl toast){
        if(error instanceof ConnectException){
            toast.showToast("Failed to connect, Please check your network");
            progress.stopProgress();
        }else if(error instanceof SocketTimeoutException){
            toast.showToast("Timeout to connect, Please check your network");
            progress.stopProgress();
        }else{
            Log.i("error",error.toString());
        }
    }
}
