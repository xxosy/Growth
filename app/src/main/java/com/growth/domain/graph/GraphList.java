package com.growth.domain.graph;

/**
 * Created by SSL-D on 2016-08-02.
 */

public class GraphList {
  GraphItem[] graphItems;

  public void decrypt(){
    for(GraphItem graphItem : graphItems){
      graphItem.decrypt();
    }
  }

  public GraphItem[] getGraphItems() {
    return graphItems;
  }

  public void setGraphItems(GraphItem[] graphItems) {
    this.graphItems = graphItems;
  }
}
