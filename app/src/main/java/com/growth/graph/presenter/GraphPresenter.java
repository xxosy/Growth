package com.growth.graph.presenter;

import com.growth.domain.Value;
import com.growth.domain.graph.GraphList;
import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

/**
 * Created by SSL-D on 2016-08-29.
 */

public interface GraphPresenter {
  void enterFragment(String serial, int index);

  void datePreButtonClick(int index);

  void dateNextButtonClick(int index);

  void tabClick(int index);

  void unSubscribe();
  interface View extends ProgressControl, ToastControl {
    void refreshPage(Value value);

    void refreshUpdateTime(String time);

    void refreshToday(String today);

    void refreshSensorData(Value value);

    void refreshMaxSensorData();

    void refreshMinSensorData();

    void refreshGraphDate(String date);

    void refreshChart(GraphList items);

    void displayToast(String msg);

    void refreshTab();
  }
}
