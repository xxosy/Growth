package com.growth.graph.dagger;

import com.growth.graph.presenter.GraphPresenter;
import com.growth.graph.presenter.GraphPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-08-29.
 */
@Module(includes = NetworkModule.class)
public class GraphModule {
    GraphPresenter.View view;

    public GraphModule(GraphPresenter.View view){
        this.view = view;
    }
    @Provides
    GraphPresenter providePresenter(GraphPresenterImpl graphPresenter){
        return graphPresenter;
    }
    @Provides
    GraphPresenter.View provideView(){
        return view;
    }
}
