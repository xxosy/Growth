package com.growth.rule.presenter;

import com.growth.domain.rule.Rule;
import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.network.SensorDataAPI;
import com.growth.network.rule.RuleDataAPI;
import com.growth.rule.adapter.RuleListAdapterModel;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-10-28.
 */

public class RulePresenterImpl implements RulePresenter{
  RulePresenter.View view;
  RuleDataAPI ruleDataAPI;
  SensorDataAPI sensorDataAPI;
  Rule insertRule;
  RuleListAdapterModel mRuleListAdapterModel;

  @Inject
  public RulePresenterImpl(RulePresenter.View view, RuleDataAPI ruleDataAPI, SensorDataAPI sensorDataAPI,RuleListAdapterModel ruleListAdapterModel){
    this.view = view;
    this.ruleDataAPI = ruleDataAPI;
    this.sensorDataAPI = sensorDataAPI;
    insertRule = new Rule();
    this.mRuleListAdapterModel = ruleListAdapterModel;
  }

  @Override
  public void onCreatedView() {
    ruleDataAPI.getRuleList(User.getInstance().getUserCode())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(result->{
          for(Rule rule:result){
            mRuleListAdapterModel.add(rule);
          }
          view.refreshRecycler();
        });
  }

  @Override
  public void factorClick(String factor) {
    insertRule.setFactor(factor);
  }

  @Override
  public void portClick(String port) {
    insertRule.setPort(port);
  }

  @Override
  public void conditionClick(String condition) {
    insertRule.setCondition(condition);
  }

  @Override
  public void sensorSerialClick(String serial) {
    insertRule.setSensor_serial(serial);
  }

  @Override
  public void actuatorSerialClick(String serial) {
    insertRule.setActuator_serial(serial);
  }

  @Override
  public void activationSwitch(String activation) {
    insertRule.setActivation(activation);
  }

  @Override
  public void valueInputted(String value) {
    insertRule.setValue(value);
  }

  @Override
  public void onOKButtonClick() {
    if(insertRule.getFactor() == null){
      view.showToast("Factor is not selected");
    }else if(insertRule.getValue() == null){
      view.showToast("Value is not inputted");
    }else if(insertRule.getCondition() == null){
      view.showToast("Condition is not selected");
    }else if(insertRule.getPort() == null){
      view.showToast("Port is not selected");
    }else{
      ruleDataAPI.insertRule(insertRule,User.getInstance().getUserCode())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(result ->{

          });
    }
    view.hideAddRule();
  }

  @Override
  public void onCancelClick() {
    view.hideAddRule();
  }

  @Override
  public void onAddRuleButtonClick() {
    Subscription subScription = sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result->{
          ArrayList<String> serials = new ArrayList<>();
          for(SensorItem sensorItem:result){
            serials.add(sensorItem.getSerial());
          }
          view.refreshSpinner(serials);
          view.showAddRule();
        });
  }
}
