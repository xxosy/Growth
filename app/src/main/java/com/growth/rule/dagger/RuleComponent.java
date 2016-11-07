package com.growth.rule.dagger;

import com.growth.rule.view.RuleFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-10-28.
 */
@Component(modules = RuleModule.class)
public interface RuleComponent {
  void inject(RuleFragment ruleFragment);
}
