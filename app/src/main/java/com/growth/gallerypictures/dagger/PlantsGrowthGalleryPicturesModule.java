package com.growth.gallerypictures.dagger;

import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapter;
import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapterModel;
import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapterView;
import com.growth.gallerypictures.presenter.PlantsGrowthGalleryPicturesPresenter;
import com.growth.gallerypictures.presenter.PlantsGrowthGalleryPicturesPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-10-06.
 */
@Module(includes = NetworkModule.class)
public class PlantsGrowthGalleryPicturesModule {
    PlantsGrowthGalleryPicturesPresenter.View view;
    PlantsGrowthGalleryPicturesAdapter adapter;
    public PlantsGrowthGalleryPicturesModule(PlantsGrowthGalleryPicturesPresenter.View view, PlantsGrowthGalleryPicturesAdapter plantsGrowthGalleryPicturesAdapter){
        this.view = view;
        adapter = plantsGrowthGalleryPicturesAdapter;
    }

    @Provides
    public PlantsGrowthGalleryPicturesPresenter.View provideView(){
        return view;
    }
    @Provides
    public PlantsGrowthGalleryPicturesPresenter providePresenter(PlantsGrowthGalleryPicturesPresenterImpl plantsGrowthGalleryPicturesPresenter){
        return plantsGrowthGalleryPicturesPresenter;
    }
    @Provides
    public PlantsGrowthGalleryPicturesAdapterModel providePlantsGrowthGalleryPicturesAdapterModel(){
        return adapter;
    }
    @Provides
    public PlantsGrowthGalleryPicturesAdapterView providePlantsGrowthGalleryPicturesAdapterView(){
        return adapter;
    }
}
