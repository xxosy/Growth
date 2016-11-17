package com.growth.graph.dagger;

import com.growth.graph.view.GraphFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-08-29.
 */
@Component(modules = GraphModule.class)
public interface GraphComponent {
  void inject(GraphFragment graphFragment);
}
