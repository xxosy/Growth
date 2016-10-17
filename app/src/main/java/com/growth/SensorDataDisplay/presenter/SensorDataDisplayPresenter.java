package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;

import com.growth.domain.Value;
import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

import java.util.HashMap;

/**
 * Created by SSL-D on 2016-08-25.
 */

public interface SensorDataDisplayPresenter {
  void enterFragment(String serial);

  void btnChangeClick();

  void btnMosquitoClick();

  void btnViewClick();

  void btnCameraClick();

  void btnGraphTempClick();

  void btnGraphHumidityClick();

  void btnGraphLightClick();

  void btnGraphCo2Click();

  void btnGraphEcClick();

  void btnGraphPhClick();

  void swipePage(String serial);

  void OnRecyclerItemClick(int position);

  interface View extends ProgressControl, ToastControl {
    void refreshData(Value value);

    void refreshState(HashMap<String, Boolean> states);

    void refreshCameraImage(Bitmap image);

    void refreshStateView(Value value);

    void showCameraFrame();

    void hideCameraFrame();

    void changeBtn(int state);

    void startProgress();

    void stopProgress();

    void refreshWhether(String whether, String externTemp, String externHumidity, Bitmap icon);

    void refreshSwipe();

    void showButton();

    void hideButton();

    void showHarmfulList();

    void hideHarmfulList();

    void showHarmfulDetail(String title, String description, String url);

    void hideHarmfulDetail();

    void refreshHarmfulList();
  }
}
