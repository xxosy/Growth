package com.growth.home.presenter;

import javax.inject.Inject;

/**
 * Created by SSL-D on 2016-07-20.
 */

public class HomePresenterImpl implements HomePresenter {
  private View view;

  @Inject
  public HomePresenterImpl(View view) {
    this.view = view;
  }
}
