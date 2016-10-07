package com.growth.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.growth.R;
import com.growth.domain.sensor.SensorItem;
import com.growth.views.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SSL-D on 2016-10-06.
 */

public class PlantsGrowthGallerySensorListAdapter extends RecyclerView.Adapter<PlantsGrowthGallerySensorListAdapter.ViewHolder>
                implements PlantsGrowthGallerySensorListAdapterView,
                            PlantsGrowthGallerySensorListAdapterModel{
    Context context;
    List<SensorItem> items;
    OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    public PlantsGrowthGallerySensorListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public PlantsGrowthGallerySensorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plants_growth_gallery_list, parent, false);
        return new PlantsGrowthGallerySensorListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlantsGrowthGallerySensorListAdapter.ViewHolder holder, int position) {
        holder.tvPlantsGallerySerial.setText(items.get(position).getSerial());
        holder.tvPlantsGalleryTitle.setText(items.get(position).getTitle());
        holder.btnPlantsGrowthGallery.setOnClickListener(v -> mOnRecyclerItemClickListener.onItemClick(this,position));
    }
    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public void add(SensorItem sensorItem) {
        items.add(sensorItem);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public SensorItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public void clear() {
        items.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_plants_gallery_serial)
        TextView tvPlantsGallerySerial;
        @BindView(R.id.tv_plants_gallery_title)
        TextView tvPlantsGalleryTitle;
        @BindView(R.id.btn_plants_growth_gallery)
        FrameLayout btnPlantsGrowthGallery;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
