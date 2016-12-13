package com.growth.standard.presenter;

/**
 * Created by SSL-D on 2016-11-21.
 */

public interface StandardInformationPresenter {
  void OnCreatedView();
  interface View{
    void refreshRecyclerView();
  }
}
