package com.growth.network;

import com.growth.domain.weather.WeatherItem;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by SSL-D on 2016-10-24.
 */

public interface WeatherAPI {
  @GET("/weather")
  Observable<WeatherItem> getWeather();

  interface Service{
    Observable<WeatherItem> getWeather();
  }
}
