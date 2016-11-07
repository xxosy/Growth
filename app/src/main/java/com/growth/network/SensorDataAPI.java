package com.growth.network;


import com.growth.domain.UpdateSensorData;
import com.growth.domain.Value;
import com.growth.domain.actuator.ActuatorState;
import com.growth.domain.graph.GraphList;
import com.growth.domain.harmful.HarmfulData;
import com.growth.domain.humidity.HumidityItem;
import com.growth.domain.humidity.HumidityList;
import com.growth.domain.sensor.SensorItem;
import com.growth.domain.sensor.SensorList;
import com.growth.domain.temperature.TemperatureList;
import com.growth.domain.user.User;
import com.growth.domain.weather.WeatherItem;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class SensorDataAPI implements SensorAPI.Service
    , HumidityAPI.Service
    , ValueAPI.Service
    , GraphDataAPI.Service
    , UserAPI.Service
    , ActuatorAPI.Service
    , HarmfulAPI.Service,
    PlantsGalleryAPI.Service,
    WeatherAPI.Service{
  private Retrofit retrofit;

  @Inject
  public SensorDataAPI(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  @Override
  public Observable<HumidityItem> getHumidity() {
    return retrofit.create(HumidityAPI.class)
        .getHumidity();
  }

  @Override
  public Observable<HumidityItem> getHumidity(String serial) {
    return retrofit.create(HumidityAPI.class)
        .getHumidity(serial);
  }

  @Override
  public Observable<HumidityList> getHumidityList() {
    return retrofit.create(HumidityAPI.class)
        .getHumidityList();
  }

  @Override
  public Observable<SensorItem> getSensor(String serial) {
    return retrofit.create(SensorAPI.class)
        .getSensor(serial);
  }

  @Override
  public Observable<SensorList> updateSensorName(String serial, String name) {
    return retrofit.create(SensorAPI.class)
        .updateSensorName(serial, name);
  }

  @Override
  public Observable<SensorItem[]> updateSensorData(String serial, UpdateSensorData data) {
    return retrofit.create(SensorAPI.class)
        .updateSensorData(serial, data);
  }

  @Override
  public Observable<SensorList> deleteSensor(String serial) {
    return retrofit.create(SensorAPI.class)
        .deleteSensor(serial);
  }

  @Override
  public Observable<Void> insertMapSensor(String serial, String usercode, UpdateSensorData data) {
    return retrofit.create(SensorAPI.class)
        .insertMapSensor(serial, usercode, data);
  }

  @Override
  public Observable<SensorItem[]> getMapSensors(String usercode) {
    return retrofit.create(SensorAPI.class)
        .getMapSensors(usercode);
  }

  @Override
  public Observable<Void> deleteMapSensor(String serial, String usercode) {
    return retrofit.create(SensorAPI.class)
        .deleteMapSensor(serial, usercode);
  }

  @Override
  public Observable<Void> updateMapSensor(String serial, String usercode, UpdateSensorData data) {
    return retrofit.create(SensorAPI.class)
        .updateMapSensor(serial, usercode, data);
  }

  @Override
  public Observable<SensorItem[]> getSensorList() {
    return retrofit.create(SensorAPI.class)
        .getSensorList();
  }

  @Override
  public Observable<GraphList> getTemperatureList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getTemperatureList(serial, date);
  }

  @Override
  public Observable<GraphList> getHumidityList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getHumidityList(serial, date);
  }

  @Override
  public Observable<GraphList> getTemperature2List(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getTemperature2List(serial, date);
  }

  @Override
  public Observable<GraphList> getEcList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getEcList(serial, date);
  }

  @Override
  public Observable<GraphList> getPhList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getPhList(serial, date);
  }

  @Override
  public Observable<GraphList> getLightList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getLightList(serial, date);
  }

  @Override
  public Observable<GraphList> getCo2List(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getCo2List(serial, date);
  }

  @Override
  public Observable<GraphList> getSoilMoistureList(String serial, String date) {
    return retrofit.create(GraphDataAPI.class)
        .getSoilMoistureList(serial, date);
  }

  @Override
  public Observable<Value> getValue(String serial) {
    return retrofit.create(ValueAPI.class)
        .getValue(serial);
  }

  @Override
  public Observable<Value> getValue() {
    return retrofit.create(ValueAPI.class)
        .getValue();
  }

  @Override
  public Observable<TemperatureList> getValueList() {
    return retrofit.create(ValueAPI.class)
        .getValueList();
  }

  @Override
  public Observable<User> getUserCode(String usercode) {
    return retrofit.create(UserAPI.class)
        .getUserCode(usercode);
  }

  @Override
  public Observable<Void> insertUserCode(String usercode) {
    return retrofit.create(UserAPI.class)
        .insertUserCode(usercode);
  }

  @Override
  public Observable<ActuatorState> getActuatorState() {
    return retrofit.create(ActuatorAPI.class)
        .getActuatorState();
  }

  @Override
  public Observable<Void> putActuatorState(ActuatorState actuatorState) {
    return retrofit.create(ActuatorAPI.class)
        .putActuatorState(actuatorState);
  }

  @Override
  public Observable<HarmfulData[]> getHarmfulData(String division) {
    return retrofit.create(HarmfulAPI.class)
        .getHarmfulData(division);
  }

  @Override
  public Observable<String[]> getPlantsPictureList(String serial) {
    return retrofit.create(PlantsGalleryAPI.class)
        .getPlantsPictureList(serial);
  }

  @Override
  public Observable<WeatherItem> getWeather() {
    return retrofit.create(WeatherAPI.class)
        .getWeather();
  }
}
