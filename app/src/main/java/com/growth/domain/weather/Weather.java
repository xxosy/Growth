package com.growth.domain.weather;

/**
 * Created by SSL-D on 2016-10-24.
 */

public class Weather {
  int id;
  String main;
  String description;
  String icon;

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getIcon() {
    return icon;
  }

  public String getMain() {
    return main;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public void setMain(String main) {
    this.main = main;
  }
}
