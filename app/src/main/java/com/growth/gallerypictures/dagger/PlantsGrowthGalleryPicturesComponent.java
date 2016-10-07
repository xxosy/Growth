package com.growth.gallerypictures.dagger;

import com.growth.gallerypictures.view.PlantsGrowthGalleryPicturesActivity;

import dagger.Component;

/**
 * Created by SSL-D on 2016-10-06.
 */
@Component(modules = PlantsGrowthGalleryPicturesModule.class)
public interface PlantsGrowthGalleryPicturesComponent {
    void inject(PlantsGrowthGalleryPicturesActivity plantsGrowthGalleryPicturesActivity);
}
