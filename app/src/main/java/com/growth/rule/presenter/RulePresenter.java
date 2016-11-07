package com.growth.rule.presenter;

import java.util.ArrayList;

/**
 * Created by SSL-D on 2016-10-28.
 */

public interface RulePresenter {
  void onCreatedView();
  void factorClick(String factor);
  void portClick(String port);
  void conditionClick(String condition);
  void sensorSerialClick(String serial);
  void actuatorSerialClick(String serial);
  void activationSwitch(String activation);
  void valueInputted(String value);
  void onOKButtonClick();
  void onAddRuleButtonClick();
  void onCancelClick();
  interface View{
    void refreshSpinner(final ArrayList<String> spinnerDatas);
    void refreshRecycler();
    void showAddRule();
    void hideAddRule();
    void showToast(String msg);
  }
}
