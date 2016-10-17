package com.growth.gallery.dagger;

import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapter;
import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapterModel;
import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapterView;
import com.growth.gallery.presenter.PlantsGrowthGalleryPresenter;
import com.growth.gallery.presenter.PlantsGrowthGalleryPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-10-06.
 */
@Module(includes = NetworkModule.class)
public class PlantsGrowthGalleryModule {
  PlantsGrowthGalleryPresenter.View view;
  PlantsGrowthGallerySensorListAdapter mPlantsGrowthGallerySensorListAdapter;

  public PlantsGrowthGalleryModule(PlantsGrowthGalleryPresenter.View view, PlantsGrowthGallerySensorListAdapter plantsGrowthGallerySensorListAdapter) {
    this.view = view;
    mPlantsGrowthGallerySensorListAdapter = plantsGrowthGallerySensorListAdapter;
  }

  @Provides
  public PlantsGrowthGalleryPresenter providePresenter(PlantsGrowthGalleryPresenterImpl plantsGrowthGalleryPresenter) {
    return plantsGrowthGalleryPresenter;
  }

  @Provides
  public PlantsGrowthGalleryPresenter.View provideView() {
    return this.view;
  }

  @Provides
  public PlantsGrowthGallerySensorListAdapterModel providePlantsGrowthGallerySensorListAdapterModel() {
    return mPlantsGrowthGallerySensorListAdapter;
  }

  @Provides
  public PlantsGrowthGallerySensorListAdapterView providePlantsGrowthGallerySensorListAdapterView() {
    return mPlantsGrowthGallerySensorListAdapter;
  }
}
