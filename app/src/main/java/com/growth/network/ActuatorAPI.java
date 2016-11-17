package com.growth.network;

import com.growth.domain.actuator.ActuatorState;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-09-20.
 */

public interface ActuatorAPI {
  @GET("/actuator/states")
  Observable<ActuatorState> getActuatorState();

  @PUT("/actuator/states/{port}/{action}")
  Observable<Void> putActuatorState(@Body ActuatorState actuatorState, @Path("port") String port, @Path("action")String action);

  interface Service {
    Observable<ActuatorState> getActuatorState();

    Observable<Void> putActuatorState(ActuatorState actuatorState,String port, String action);
  }
}
