package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;

import com.growth.domain.Value;
import com.growth.map.presenter.SensorMapPresenter;
import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

import java.util.HashMap;

/**
 * Created by SSL-D on 2016-08-25.
 */

public interface SensorDataDisplayPresenter {
    void enterFragment(String serial);
    void btnChangeCameraViewClick();
    void btnGraphTempClick();
    void btnGraphHumidityClick();
    void btnGraphLightClick();
    void btnGraphCo2Click();
    void btnGraphEcClick();
    void btnGraphPhClick();
    interface View extends ProgressControl,ToastControl {
        void refreshData(Value value);
        void refreshState(HashMap<String,Boolean> states);
        void refreshCameraImage(Bitmap image);
        void showCameraFrame();
        void hideCameraFrame();
        void changeBtnChageCameraView(boolean state);
        void startProgress();
        void stopProgress();
        void refreshWhether(String whether,String externTemp,String externHumidity,Bitmap icon);
    }
}
