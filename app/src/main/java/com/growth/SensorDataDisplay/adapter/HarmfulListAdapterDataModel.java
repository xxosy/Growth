package com.growth.SensorDataDisplay.adapter;

import com.growth.domain.harmful.HarmfulData;

/**
 * Created by SSL-D on 2016-09-28.
 */

public interface HarmfulListAdapterDataModel {
    void add(HarmfulData harmfulData);
    int getSize();
    HarmfulData getItem(int position);
    void clear();
}
