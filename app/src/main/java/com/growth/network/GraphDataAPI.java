package com.growth.network;


import com.growth.domain.graph.GraphList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-08-02.
 */

public interface GraphDataAPI {
    @GET("/temperature/list/{serial}/{date}")
    Observable<GraphList> getTemperatureList(@Path("serial") String serial,@Path("date")String date);
    @GET("/humidity/list/{serial}/{date}")
    Observable<GraphList> getHumidityList(@Path("serial") String serial,@Path("date")String date);
    @GET("/temperature2/list/{serial}/{date}")
    Observable<GraphList> getTemperature2List(@Path("serial") String serial,@Path("date")String date);
    @GET("/ec/list/{serial}/{date}")
    Observable<GraphList> getEcList(@Path("serial") String serial,@Path("date")String date);
    @GET("/ph/list/{serial}/{date}")
    Observable<GraphList> getPhList(@Path("serial") String serial,@Path("date")String date);
    @GET("/light/list/{serial}/{date}")
    Observable<GraphList> getLightList(@Path("serial") String serial,@Path("date")String date);
    @GET("/co2/list/{serial}/{date}")
    Observable<GraphList> getCo2List(@Path("serial") String serial,@Path("date")String date);
    @GET("/soil_moisture/list/{serial}/{date}")
    Observable<GraphList> getSoilMoistureList(@Path("serial") String serial,@Path("date")String date);
    interface Service{
        Observable<GraphList> getTemperatureList(String serial,@Path("date")String date);
        Observable<GraphList> getHumidityList(String serial,@Path("date")String date);
        Observable<GraphList> getTemperature2List(String serial,@Path("date")String date);
        Observable<GraphList> getEcList(String serial,@Path("date")String date);
        Observable<GraphList> getPhList(String serial,@Path("date")String date);
        Observable<GraphList> getLightList(String serial,@Path("date")String date);
        Observable<GraphList> getCo2List(String serial,@Path("date")String date);
        Observable<GraphList> getSoilMoistureList(String serial,String date);
    }
}
