package com.growth.gallerypictures.presenter;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface PlantsGrowthGalleryPicturesPresenter {
  void onCreated(String serial);

  void OnRecyclerPlantsGrowthGalleryItemClick(int position);

  interface View {
    void refreshRecycler();
  }
}
