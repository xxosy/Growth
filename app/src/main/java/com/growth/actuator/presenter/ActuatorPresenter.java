package com.growth.actuator.presenter;

import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

/**
 * Created by SSL-D on 2016-09-20.
 */

public interface ActuatorPresenter {
  void enter();

  void btnActuatorClick(int index);

  void btnActuatorDetailClick(int index);

  void btnActuatorDetailSwitchClick();

  interface View extends ProgressControl, ToastControl {
    void refreshActuatorState(int[] state);

    void showActuatorDetail(int index, int state);

    void hideActuatorDetail();

    void refreshActuatorDetailState(int state);
  }
}
