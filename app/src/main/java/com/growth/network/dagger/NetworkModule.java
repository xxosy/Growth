package com.growth.network.dagger;

import com.growth.network.SensorDataAPI;
import com.growth.network.retrofit.RetrofitCreator;
import com.growth.network.rule.RuleDataAPI;
import com.growth.network.user.UserDataAPI;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by SSL-D on 2016-07-21.
 */
@Module
public class NetworkModule {
  @Provides
  public Retrofit provideRetrofit() {
    return RetrofitCreator.createRetrofit();
  }

  @Provides
  public SensorDataAPI provideSensorDataAPI(Retrofit retrofit) {
    return new SensorDataAPI(retrofit);
  }

  @Provides
  public RuleDataAPI provideRuleDataAPI(Retrofit retrofit){
    return new RuleDataAPI(retrofit);
  }

  @Provides
  public UserDataAPI provideUserDataAPI(Retrofit retrofit){
    return new UserDataAPI(retrofit);
  }
}
