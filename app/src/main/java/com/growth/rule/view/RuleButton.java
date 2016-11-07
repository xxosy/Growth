package com.growth.rule.view;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by SSL-D on 2016-11-02.
 */

public class RuleButton {
  View btnFactor;
  FrameLayout background;
  TextView text;

  public RuleButton(View btnFactor, FrameLayout background, TextView text){
    this.btnFactor = btnFactor;
    this.background = background;
    this.text = text;
  }

  public FrameLayout getBackground() {
    return background;
  }

  public View getBtnFactor() {
    return btnFactor;
  }

  public TextView getText() {
    return text;
  }

  public void setBackground(FrameLayout background) {
    this.background = background;
  }

  public void setBtnFactor(View btnFactor) {
    this.btnFactor = btnFactor;
  }

  public void setText(TextView text) {
    this.text = text;
  }
}
