package com.growth.utils;

import android.view.View;
import android.widget.FrameLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

/**
 * Created by SSL-D on 2016-09-05.
 */

public class ProgressControlImlp implements ProgressControl {
  FrameLayout progressLayout;
  CircularProgressView progressView;

  public ProgressControlImlp(FrameLayout progressLayout, CircularProgressView progressView) {
    this.progressLayout = progressLayout;
    this.progressView = progressView;
  }

  @Override
  public void startProgress() {
    if (progressLayout.getVisibility() == View.GONE) {
      progressLayout.setVisibility(View.VISIBLE);
      progressView.startAnimation();
    }
  }

  @Override
  public void stopProgress() {
    if (progressLayout.getVisibility() == View.VISIBLE) {
      progressLayout.setVisibility(View.GONE);
      progressView.stopAnimation();
    }
  }
}
