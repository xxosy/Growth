package com.growth.network;

import com.growth.domain.UpdateSensorData;
import com.growth.domain.sensor.SensorItem;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-07-22.
 */

public interface SensorAPI {
  @GET("/sensor/{serial}")
  Observable<SensorItem> getSensor(@Path("serial") String serial);

  @GET("/sensor/list")
  Observable<SensorItem[]> getSensorList();

  @POST("/map/sensor/{serial}/{usercode}")
  Observable<Void> insertMapSensor(@Path("serial") String serial, @Path("usercode") String usercode, @Body UpdateSensorData data);

  @GET("/map/sensor/{usercode}")
  Observable<SensorItem[]> getMapSensors(@Path("usercode") String usercode);

  @DELETE("/map/sensor/{serial}/{usercode}")
  Observable<Void> deleteMapSensor(@Path("serial") String serial, @Path("usercode") String usercode);

  @PUT("/map/sensor/{serial}/{usercode}")
  Observable<Void> updateMapSensor(@Path("serial") String serial, @Path("usercode") String usercode, @Body UpdateSensorData data);

  interface Service {

    Observable<SensorItem> getSensor(String serial);


    Observable<Void> insertMapSensor(String serial, String usercode, UpdateSensorData data);

    Observable<SensorItem[]> getMapSensors(String usercode);

    Observable<Void> deleteMapSensor(String serial, String usercode);

    Observable<Void> updateMapSensor(String serial, String usercode, UpdateSensorData data);
  }
}
