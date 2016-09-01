package com.growth.network;

import com.growth.domain.temperature.TemperatureItem;
import com.growth.domain.temperature.TemperatureList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-07-22.
 */

public interface TemperatureAPI {
    @GET("/temperature/list/{serial}")
    Observable<TemperatureList> getTemperatureList(@Path("serial") String serial);
    @GET("/temperature/recent")
    Observable<TemperatureItem> getTemperature();

    interface Service{
        Observable<TemperatureItem> getTemperature();
        Observable<TemperatureList> getTemperatureList(String serial);
    }
}
