package com.growth.intro.presenter;

import com.growth.domain.database.DBManager;
import com.growth.domain.user.User;
import com.growth.network.user.UserDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-08-31.
 */

public class IntroPresenterImpl implements IntroPresenter {
  private final int USER_CODE_LENGTH = 8;
  private IntroPresenter.View view;
  private UserDataAPI userDataAPI;
  private DBManager mDBManager;

  @Inject
  public IntroPresenterImpl(IntroPresenter.View view, UserDataAPI userDataAPI) {
    this.view = view;
    this.userDataAPI = userDataAPI;
  }

  @Override
  public void setDBManager(DBManager mDBManager) {
    this.mDBManager = mDBManager;
  }

  @Override
  public void setUserCode() {
    if (mDBManager.getCount() == 0) {
      String userCode = createUserCode();
      insertUserCode(userCode);
      mDBManager.createUserCode(userCode);
    }
    User.getInstance().setUserCode(mDBManager.getUserCode());
  }

  private String createUserCode() {
    String tempUserCode = "";
    for (int i = 0; i < USER_CODE_LENGTH; i++) {
      int rndVal = (int) (Math.random() * 62);
      if (rndVal < 10) {
        tempUserCode += rndVal; //ASCII NUMBER
      } else if (rndVal > 35) {
        tempUserCode += (char) (rndVal + 61); //ASCII LOWER CASE
      } else {
        tempUserCode += (char) (rndVal + 55); //ASCII UPPER CASE
      }
    }
    return tempUserCode;
  }

  private void insertUserCode(String userCode) {
    userDataAPI.insertUserCode(userCode)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }
}
