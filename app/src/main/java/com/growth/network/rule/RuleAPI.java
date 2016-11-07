package com.growth.network.rule;

import com.growth.domain.rule.Rule;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-11-01.
 */

public interface RuleAPI {
  @POST("/rule/{usercode}")
  Observable<Void> insertRule(@Body Rule rule, @Path("usercode") String usercode);

  @GET("/rules/{usercode}")
  Observable<Rule[]> getRuleList(@Path("usercode") String usercode);

  @PUT("/rule/{usercode}")
  Observable<Void> updateRule(@Body Rule rule, @Path("usercode") String usercode);

  @DELETE("/rule/{usercode}/{id}")
  Observable<Void> deleteRule(@Path("id")int id);

  interface Service{
    Observable<Void> insertRule(Rule rule,String usercode);
    Observable<Rule[]> getRuleList(String usercode);
    Observable<Void> updateRule(Rule rule, String usercode);
    Observable<Void> deleteRule(int id);

  }
}
