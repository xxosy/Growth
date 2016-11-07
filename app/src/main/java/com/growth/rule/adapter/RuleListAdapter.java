package com.growth.rule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.growth.R;
import com.growth.domain.rule.Rule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SSL-D on 2016-11-01.
 */

public class RuleListAdapter extends RecyclerView.Adapter<RuleListAdapter.ViewHolder>
                              implements RuleListAdapterModel,
                                          RuleListAdapterView{
  Context context;
  List<Rule> items;
  public RuleListAdapter(Context context){
    this.context = context;
    items = new ArrayList<>();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_rule, parent, false);
    return new RuleListAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Rule rule = items.get(position);
    String condition = rule.getFactor();
    if(rule.getCondition().equals("up")) condition+=">";
    else if(rule.getCondition().equals("low")) condition+="<";
    condition+=rule.getValue();
    holder.tvAction.setText("Turn on");
    holder.tvActuator.setText(rule.getActuator_serial()+" : "+rule.getPort());
    holder.tvSensor.setText(rule.getSensor_serial());
    holder.tvCondition.setText(condition);
    if(rule.getActivation().equals("true")) holder.switchActivation.setChecked(true);
    else if(rule.getActivation().equals("false")) holder.switchActivation.setChecked(false);
    Log.i("temp",rule.getCondition());
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
  public void add(Rule rule) {
    items.add(rule);
  }

  @Override
  public int getSize() {
    return items.size();
  }

  @Override
  public Rule getItem(int position) {
    return items.get(position);
  }

  @Override
  public void clear() {
    items.clear();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rule_tv_action)
    TextView tvAction;
    @BindView(R.id.rule_tv_actuator_serial_port)
    TextView tvActuator;
    @BindView(R.id.rule_tv_condition)
    TextView tvCondition;
    @BindView(R.id.rule_tv_sensor_serial)
    TextView tvSensor;
    @BindView(R.id.item_rule_activation)
    Switch switchActivation;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
