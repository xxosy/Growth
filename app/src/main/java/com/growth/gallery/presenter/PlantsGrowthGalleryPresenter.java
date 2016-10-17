package com.growth.gallery.presenter;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface PlantsGrowthGalleryPresenter {
  void OnCreatedView();

  void OnRecyclerPlantsGrowthGalleryItemClick(int position);

  interface View {
    void refreshRecyclerPlantsGrowthGalleryList();

    void startActivityPlantGallery(String serial);
  }
}
