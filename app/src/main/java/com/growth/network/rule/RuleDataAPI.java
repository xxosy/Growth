package com.growth.network.rule;

import com.growth.domain.rule.Rule;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by SSL-D on 2016-11-01.
 */

public class RuleDataAPI implements RuleAPI.Service{
  Retrofit retrofit;

  @Inject
  public RuleDataAPI(Retrofit retrofit){
    this.retrofit = retrofit;
  }

  @Override
  public Observable<Void> insertRule(Rule rule, String usercode) {
    return retrofit.create(RuleAPI.class)
        .insertRule(rule,usercode);
  }

  @Override
  public Observable<Rule[]> getRuleList(String usercode) {
    return retrofit.create(RuleAPI.class)
        .getRuleList(usercode);
  }

  @Override
  public Observable<Void> updateRule(Rule rule, String usercode) {
    return retrofit.create(RuleAPI.class)
        .updateRule(rule, usercode);
  }

  @Override
  public Observable<Void> deleteRule(int id) {
    return retrofit.create(RuleAPI.class)
        .deleteRule(id);
  }
}
