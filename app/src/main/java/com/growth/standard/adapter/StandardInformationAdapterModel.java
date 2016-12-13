package com.growth.standard.adapter;

import com.growth.domain.standard.StandardInformation;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface StandardInformationAdapterModel {
  void add(StandardInformation item);

  int getSize();

  StandardInformation getItem(int position);

  void clear();
}
