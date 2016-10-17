package com.growth.home;

import com.growth.views.PageChange;

/**
 * Created by SSL-D on 2016-08-25.
 */

public class PageChangeUtil {
  PageChange pageChange;
  public static PageChangeUtil instance;

  public static PageChangeUtil newInstance() {
    if (instance == null)
      instance = new PageChangeUtil();
    return instance;
  }

  public PageChange getPageChange() {
    return pageChange;
  }

  public void setPageChange(PageChange pageChange) {
    this.pageChange = pageChange;
  }
}
