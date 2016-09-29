package com.growth.SensorDataDisplay.adapter;

import com.growth.views.OnRecyclerItemClickListener;

/**
 * Created by SSL-D on 2016-09-28.
 */

public interface HarmfulListAdapterDataView {
    void refresh();

    void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener);
}
