package com.growth.domain.network;

/**
 * Created by SSL-D on 2016-08-10.
 */

public class NetworkState {
  public static String STATE_OK = "200";
  public static String STATE_FAIL = "400";
  public static String STATE_NOT_FOUND = "404";
  public static String STATE_NO_ACCOUNT = "401";
  String stateCode;
  String stateTip;
}
