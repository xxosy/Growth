package com.growth.standard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growth.R;
import com.growth.domain.standard.StandardInformation;
import com.growth.views.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SSL-D on 2016-11-21.
 */

public class StandardInformationRecyclerAdapter extends RecyclerView.Adapter<StandardInformationRecyclerAdapter.ViewHolder> implements
StandardInformationAdapterView,StandardInformationAdapterModel{
  Context context;
  List<StandardInformation> items;
  OnRecyclerItemClickListener onRecyclerItemClickListener;
  boolean foldState[];
  public StandardInformationRecyclerAdapter(Context context) {
    this.context = context;
    items = new ArrayList<>();
  }

  @Override
  public StandardInformationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_standard_information, parent, false);
    return new StandardInformationRecyclerAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    StandardInformation item = items.get(position);
    holder.tvTitle.setText(item.getTitle());
    String description = item.getDescription();
    description = description.replace("#","\r\n");
    description = description.replace("❍"," ❍");
    description = description.replace("-","  -");

    final String originalDescription = description;

    if(description.length()>50){
      description = description.substring(0,50);
    }
    final String subDescription = description;
    holder.tvTitleDescriptionSimple.setText(description);
    holder.btnItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!foldState[position]) {
          foldState[position] = !foldState[position];
          holder.tvTitleDescriptionSimple.setText(originalDescription);
        }else{
          foldState[position] = !foldState[position];
          holder.tvTitleDescriptionSimple.setText(subDescription);
        }
      }
    });
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
    this.onRecyclerItemClickListener = onRecyclerItemClickListener;
  }

  @Override
  public void add(StandardInformation item) {
    items.add(item);
    foldState = new boolean[getItemCount()];
  }

  @Override
  public int getSize() {
    return items.size();
  }

  @Override
  public StandardInformation getItem(int position) {
    return items.get(position);
  }

  @Override
  public void clear() {
    items.clear();
  }


  static class ViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.standard_tv_title)
    TextView tvTitle;
    @BindView(R.id.standard_tv_description_simple)
    TextView tvTitleDescriptionSimple;
    @BindView(R.id.standard_btn_item)
    LinearLayout btnItem;
    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
