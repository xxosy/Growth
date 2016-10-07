package com.growth.SensorDataDisplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.growth.R;
import com.growth.domain.harmful.HarmfulData;
import com.growth.views.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SSL-D on 2016-09-28.
 */

public class HarmfulListAdapter extends RecyclerView.Adapter<HarmfulListAdapter.ViewHolder>
                                implements  HarmfulListAdapterDataModel,
                                            HarmfulListAdapterDataView{
    Context context;
    List<HarmfulData> items;
    OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    public HarmfulListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
        HarmfulData h = new HarmfulData();
        h.setDescription("Cercospora fragariae is a fungal plant pathogen.");
        h.setTitle("Cercospora leaf spot");
        items.add(h);
        h = new HarmfulData();
        h.setDescription("Anthrax is an infection caused by the bacterium Bacillus anthracis.");
        h.setTitle("Anthrax");
        items.add(h);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_harmful, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HarmfulData item = getItem(position);
        holder.tvHarmfulTitle.setText(item.getTitle());
        String description = item.getDescription();
        if(description.length()>60)
            description = description.substring(0,60)+" ···";
        holder.tvHarmfulDescription.setText(description);
        holder.btnHarmfulItem.setOnClickListener(v -> mOnRecyclerItemClickListener.onItemClick(this,position));
        Glide.with(context).load(item.getImgurl()).into(holder.imgHarmfulSmall);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    @Override
    public void add(HarmfulData harmfulData) {
        items.add(harmfulData);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public HarmfulData getItem(int position) {
        return items.get(position);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();

    }

    @Override
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_harmful_small)
        ImageView imgHarmfulSmall;
        @BindView(R.id.tv_harmful_title)
        TextView tvHarmfulTitle;
        @BindView(R.id.tv_harmful_description)
        TextView tvHarmfulDescription;
        @BindView(R.id.btn_harmful_item)
        FrameLayout btnHarmfulItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
