package com.growth.intro.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.growth.R;
import com.growth.domain.database.DBManager;
import com.growth.domain.user.User;
import com.growth.home.view.HomeActivity;
import com.growth.intro.dagger.DaggerIntroComponent;
import com.growth.intro.dagger.IntroModule;
import com.growth.intro.presenter.IntroPresenter;

import javax.inject.Inject;

public class IntroActivity extends AppCompatActivity implements IntroPresenter.View {

  @Inject
  IntroPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_intro);
    DaggerIntroComponent.builder()
        .introModule(new IntroModule(this))
        .build()
        .inject(this);
    new Thread(new Runnable() {
      @Override
      public void run() {
        DBManager dbManager = new DBManager(IntroActivity.this, "user.db", null, 1);
        presenter.setDBManager(dbManager);
        presenter.setUserCode();
        Log.i("userCode", User.getInstance().getUserCode());
      }
    }).start();

    Handler handler = new Handler();

    handler.postDelayed(() -> {
      Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
      startActivity(intent);
      overridePendingTransition(android.R.anim.fade_in,
          android.R.anim.fade_out);
      finish();
    }, 1000);
  }
}
