package com.growth.network;

import com.growth.domain.harmful.HarmfulData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-09-28.
 */

public interface HarmfulAPI {
    @GET("/harmful/list/{division}")
    Observable<HarmfulData[]> getHarmfulData(@Path("division") String division);

    interface Service{
        Observable<HarmfulData[]> getHarmfulData(String division);
    }
}
