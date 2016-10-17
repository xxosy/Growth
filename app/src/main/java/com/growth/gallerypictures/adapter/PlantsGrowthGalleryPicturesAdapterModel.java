package com.growth.gallerypictures.adapter;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface PlantsGrowthGalleryPicturesAdapterModel {
  void add(String item);

  int getSize();

  String getItem(int position);

  void clear();
}
