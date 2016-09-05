package com.growth.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by SSL-D on 2016-09-05.
 */

public class ToastControlImlp implements ToastControl{
    Context context;

    public ToastControlImlp(Context context){
        this.context = context;
    }

    public void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
