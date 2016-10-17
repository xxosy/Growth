package com.growth.gallerypictures.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.growth.R;
import com.growth.network.retrofit.RetrofitCreator;
import com.growth.views.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SSL-D on 2016-10-06.
 */

public class PlantsGrowthGalleryPicturesAdapter extends RecyclerView.Adapter<PlantsGrowthGalleryPicturesAdapter.ViewHolder>
    implements PlantsGrowthGalleryPicturesAdapterModel,
    PlantsGrowthGalleryPicturesAdapterView {

  Context context;
  List<String> items;
  OnRecyclerItemClickListener mOnRecyclerItemClickListener;
  String serial;

  public PlantsGrowthGalleryPicturesAdapter(Context context, String serial) {
    this.context = context;
    items = new ArrayList<>();
    this.serial = serial;
  }

  @Override
  public PlantsGrowthGalleryPicturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_plants_growth_gallery_picture, parent, false);
    return new PlantsGrowthGalleryPicturesAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    String item = items.get(items.size() - position - 1);
    String year = item.substring(0, 4);
    String month = item.substring(4, 6);
    String day = item.substring(6, 8);
    String hour = item.substring(8, 10);

    holder.tvPlantsGalleryName.setText(month + " / " + day + " / " + year + " / " + hour);
    Glide.with(context).load(RetrofitCreator.getBaseUrl() + "/gallery/" + serial + "/" + item).into(holder.imgPlantsGrowthPicture);
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
    mOnRecyclerItemClickListener = onRecyclerItemClickListener;
  }


  @Override
  public void add(String item) {
    items.add(item);
  }

  @Override
  public int getSize() {
    return items.size();
  }

  @Override
  public String getItem(int position) {
    return items.get(position);
  }

  @Override
  public void clear() {
    items.clear();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_plants_growth_picture)
    ImageView imgPlantsGrowthPicture;
    @BindView(R.id.tv_plants_gallery_name)
    TextView tvPlantsGalleryName;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
