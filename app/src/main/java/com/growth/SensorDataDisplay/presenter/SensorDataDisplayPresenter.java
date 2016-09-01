package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;

import com.growth.domain.Value;

import java.util.HashMap;

/**
 * Created by SSL-D on 2016-08-25.
 */

public interface SensorDataDisplayPresenter {
    void enterFragment(String serial);
    void btnChangeCameraViewClick();
    interface View{
        void refreshData(Value value);
        void refreshState(HashMap<String,Boolean> states);
        void refreshCameraImage(Bitmap image);
        void showCameraFrame();
        void hideCameraFrame();
        void changeBtnChageCameraView(boolean state);
    }
}
