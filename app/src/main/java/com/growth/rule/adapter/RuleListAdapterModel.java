package com.growth.rule.adapter;

import com.growth.domain.rule.Rule;

/**
 * Created by SSL-D on 2016-11-01.
 */

public interface RuleListAdapterModel {
  void add(Rule rule);

  int getSize();

  Rule getItem(int position);

  void clear();
}
