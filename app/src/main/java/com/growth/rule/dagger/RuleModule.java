package com.growth.rule.dagger;

import com.growth.network.dagger.NetworkModule;
import com.growth.rule.adapter.RuleListAdapter;
import com.growth.rule.adapter.RuleListAdapterModel;
import com.growth.rule.adapter.RuleListAdapterView;
import com.growth.rule.presenter.RulePresenter;
import com.growth.rule.presenter.RulePresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-10-28.
 */
@Module(includes = NetworkModule.class)
public class RuleModule {
  RulePresenter.View view;
  RuleListAdapter adapter;

  public RuleModule(RulePresenter.View view, RuleListAdapter ruleListAdapter){
    this.view = view;
    adapter = ruleListAdapter;
  }

  @Provides
  public RulePresenter.View provideView(){
    return view;
  }

  @Provides
  public RulePresenter providePresenter(RulePresenterImpl rulePresenter){
    return rulePresenter;
  }

  @Provides
  public RuleListAdapterModel provideRuleListAdapterModel(){
    return adapter;
  }
  @Provides
  public RuleListAdapterView provideRuleListAdapterView(){
    return adapter;
  }
}
