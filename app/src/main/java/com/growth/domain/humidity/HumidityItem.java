package com.growth.domain.humidity;

import com.growth.utils.AESDecryptor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class HumidityItem {
  private int id;
  private String humidity;
  private int sensor_id;
  private String update_time;
  public void decrypt(){
    try {
      humidity = AESDecryptor.decrypt("pais",humidity);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    }
  }
  public int getId() {
    return id;
  }

  public int getSensor_id() {
    return sensor_id;
  }

  public String getHumidity() {
    return humidity;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSensor_id(int sensor_id) {
    this.sensor_id = sensor_id;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }

  public String getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }
}
