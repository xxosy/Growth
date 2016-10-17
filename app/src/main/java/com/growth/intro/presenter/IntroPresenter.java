package com.growth.intro.presenter;

import com.growth.domain.database.DBManager;

/**
 * Created by SSL-D on 2016-08-31.
 */

public interface IntroPresenter {
  void setDBManager(DBManager mDBManager);

  void setUserCode();

  interface View {

  }
}
