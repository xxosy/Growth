package com.growth.domain.weather;

/**
 * Created by SSL-D on 2016-10-24.
 */

public class WeatherItem {
  Coord coord;
  Weather[] weather;
  String base;
  Main main;
  Wind wind;
  Clouds clouds;
  int dt;
  Sys sys;
  int id;
  String name;
  int cod;

  public Clouds getClouds() {
    return clouds;
  }

  public Coord getCoord() {
    return coord;
  }

  public int getCod() {
    return cod;
  }

  public int getDt() {
    return dt;
  }

  public int getId() {
    return id;
  }

  public Main getMain() {
    return main;
  }

  public String getBase() {
    return base;
  }

  public String getName() {
    return name;
  }

  public Sys getSys() {
    return sys;
  }

  public Weather[] getWeather() {
    return weather;
  }

  public Wind getWind() {
    return wind;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public void setClouds(Clouds clouds) {
    this.clouds = clouds;
  }

  public void setCod(int cod) {
    this.cod = cod;
  }

  public void setCoord(Coord coord) {
    this.coord = coord;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }

  public void setWeather(Weather[] weather) {
    this.weather = weather;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }
}
