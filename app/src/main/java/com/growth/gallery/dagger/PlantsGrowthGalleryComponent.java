package com.growth.gallery.dagger;

import com.growth.gallery.view.PlantsGrowthGalleryFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-10-06.
 */

@Component(modules = PlantsGrowthGalleryModule.class)
public interface PlantsGrowthGalleryComponent {
    void inject(PlantsGrowthGalleryFragment plantsGrowthGalleryFragment);
}
