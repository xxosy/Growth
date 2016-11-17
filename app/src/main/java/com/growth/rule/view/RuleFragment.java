package com.growth.rule.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.growth.R;
import com.growth.rule.adapter.RuleListAdapter;
import com.growth.rule.adapter.RuleListAdapterView;
import com.growth.rule.dagger.DaggerRuleComponent;
import com.growth.rule.dagger.RuleModule;
import com.growth.rule.presenter.RulePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RuleFragment extends Fragment implements RulePresenter.View{
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private static final String TAG = RuleFragment.class.getSimpleName();
  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  View root;
  Unbinder unbinder;
  @BindView(R.id.rule_port_set)
  LinearLayout portSet;

  @BindView(R.id.rule_factor_set)
  LinearLayout factorSet;

  @BindView(R.id.rule_spinner_serials)
  Spinner spinnerSerials;

  @BindView(R.id.rule_et_value)
  EditText etValue;

  @BindView(R.id.rule_btn_condition_up)
  FrameLayout btnConditionUp;
  @BindView(R.id.condition_up_background)
  FrameLayout conditionUpBackground;
  @BindView(R.id.condition_up_text)
  TextView conditionUpText;

  @BindView(R.id.rule_btn_condition_low)
  FrameLayout btnConditionLow;
  @BindView(R.id.condition_low_background)
  FrameLayout conditionLowBackground;
  @BindView(R.id.condition_low_text)
  TextView conditionLowText;

  @BindView(R.id.rule_btn_ok)
  FrameLayout btnOK;
  @BindView(R.id.rule_btn_cancel)
  FrameLayout btnCancel;

  @BindView(R.id.rule_switch_activation)
  Switch switchActivation;
  @BindView(R.id.rule_et_actuator_serial)
  TextView etActuatorSerial;

  @BindView(R.id.rule_recycler_rule_list)
  RecyclerView ruleList;
  RuleListAdapter mRuleListAdapter;

  @BindView(R.id.rule_add_rule)
  FloatingActionButton btnAddRule;

  @BindView(R.id.rule_add_rule_window)
  FrameLayout addRuleWindow;

  @BindView(R.id.rule_btn_action_on)
  FrameLayout btnActionOn;
  @BindView(R.id.action_on_background)
  FrameLayout actionOnBackground;
  @BindView(R.id.action_on_text)
  TextView actionOnText;

  @BindView(R.id.rule_btn_action_off)
  FrameLayout btnActionOff;
  @BindView(R.id.action_off_background)
  FrameLayout actionOffBackground;
  @BindView(R.id.action_off_text)
  TextView actionOffText;

  @Inject
  RuleListAdapterView mRuleListAdapterView;

  HashMap<String,RuleButton> hashMapBtnFactor;
  HashMap<String,RuleButton> hashMapBtnPort;
  @Inject
  RulePresenter presenter;
  public RuleFragment() {
    hashMapBtnFactor = new HashMap<>();
    hashMapBtnPort = new HashMap<>();
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment RuleFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static RuleFragment newInstance(String param1, String param2) {
    RuleFragment fragment = new RuleFragment();
    Bundle args = new Bundle();

    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mRuleListAdapter = new RuleListAdapter(getActivity());
    mRuleListAdapter.setOnRecyclerSwitchChangeListener((buttonView, isChecked, id) -> presenter.onRecyclerSwitchChanged(isChecked, id));
    mRuleListAdapter.setOnRecyclerDeleteClick(id -> presenter.onRecyclerDeleteClicked(id));
    DaggerRuleComponent.builder()
        .ruleModule(new RuleModule(this,mRuleListAdapter))
        .build()
        .inject(this);

    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_rule, container, false);
    } catch (InflateException e) {

    }
    unbinder = ButterKnife.bind(this, root);

    ruleList.setAdapter(mRuleListAdapter);

    ruleList.setLayoutManager(new LinearLayoutManager(getActivity()));
    setFactor(inflater);
    setPort(inflater);
    setFactorButtonListener();
    setPortButtonListener();
    setUpAndLowButtonListener();
    etValue.addTextChangedListener(getValueTextWatcher());
    etActuatorSerial.addTextChangedListener(getActuatorSerialTextWatcher());
    initActivationSwitch();
    initActionButton();
    initOKCancelButton();
    btnAddRule.setOnClickListener(v -> presenter.onAddRuleButtonClick());

    presenter.onCreatedView();
    return root;
  }
  private void initActionButton(){
    btnActionOn.setOnClickListener(v -> {
      actionOffBackground.setBackgroundResource(R.color.colorBase);
      actionOffText.setTextColor(getResources().getColor(R.color.colorPrimary));
      actionOnBackground.setBackgroundResource(R.color.colorBase);
      actionOnText.setTextColor(getResources().getColor(R.color.colorPrimary));

      actionOnBackground.setBackgroundResource(R.color.colorPrimary);
      actionOnText.setTextColor(getResources().getColor(R.color.colorBase));
      presenter.actionOnClick();
    });
    btnActionOff.setOnClickListener(v -> {
      actionOffBackground.setBackgroundResource(R.color.colorBase);
      actionOffText.setTextColor(getResources().getColor(R.color.colorPrimary));
      actionOnBackground.setBackgroundResource(R.color.colorBase);
      actionOnText.setTextColor(getResources().getColor(R.color.colorPrimary));

      actionOffBackground.setBackgroundResource(R.color.colorPrimary);
      actionOffText.setTextColor(getResources().getColor(R.color.colorBase));
      presenter.actionOffClick();
    });
  }
  private void initOKCancelButton(){
    btnOK.setOnClickListener(v -> presenter.onOKButtonClick());
    btnCancel.setOnClickListener(v -> {
      clearAddRule();
      presenter.onCancelClick();
    });
  }
  private void initActivationSwitch(){
    presenter.activationSwitch("false");
    switchActivation.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if(isChecked)
        presenter.activationSwitch("true");
      else
        presenter.activationSwitch("false");
    });
  }
  private TextWatcher getValueTextWatcher(){
    TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.valueInputted(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
    return textWatcher;
  }
  private TextWatcher getActuatorSerialTextWatcher(){
    TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        presenter.actuatorSerialInputted(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    };
    return textWatcher;
  }

  private void setUpAndLowButtonListener(){
    btnConditionUp.setOnClickListener(v -> {
      conditionLowBackground.setBackgroundResource(R.color.colorBase);
      conditionUpBackground.setBackgroundResource(R.color.colorBase);
      conditionLowText.setTextColor(getResources().getColor(R.color.colorPrimary));
      conditionUpText.setTextColor(getResources().getColor(R.color.colorPrimary));

      conditionUpBackground.setBackgroundResource(R.color.colorPrimary);
      conditionUpText.setTextColor(getResources().getColor(R.color.colorBase));
      presenter.conditionClick("up");
    });
    btnConditionLow.setOnClickListener(v -> {
      conditionLowBackground.setBackgroundResource(R.color.colorBase);
      conditionUpBackground.setBackgroundResource(R.color.colorBase);
      conditionLowText.setTextColor(getResources().getColor(R.color.colorPrimary));
      conditionUpText.setTextColor(getResources().getColor(R.color.colorPrimary));

      conditionLowBackground.setBackgroundResource(R.color.colorPrimary);
      conditionLowText.setTextColor(getResources().getColor(R.color.colorBase));
      presenter.conditionClick("low");
    });
  }
  private void setPort(LayoutInflater inflater){
    List<String> portNums = new ArrayList<>();
    portNums.add("0");
    portNums.add("1");
    portNums.add("2");
    portNums.add("3");
    portNums.add("4");
    portNums.add("5");
    portNums.add("6");
    portNums.add("7");


    for(int i = 0;i<portNums.size();i++) {
      addPort(inflater, portNums.get(i));
    }
  }

  private void addPort(LayoutInflater inflater,String portNum){
    View btnType = inflater.inflate(R.layout.rule_button, null);
    FrameLayout background = (FrameLayout) btnType.findViewById(R.id.rule_factor_background);
    TextView tvFactorName = (TextView) btnType.findViewById(R.id.rule_tv_factor_name);
    tvFactorName.setText(portNum);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    params.leftMargin = 20;
    params.rightMargin = 20;
    params.topMargin = 5;
    params.bottomMargin = 5;
    tvFactorName.setLayoutParams(params);
    tvFactorName.setTextColor(getResources().getColor(R.color.colorPrimary));
    background.setBackgroundResource(R.color.colorBase);

    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params1.leftMargin = 10;
    params1.rightMargin = 10;
    btnType.setLayoutParams(params1);
    portSet.addView(btnType);

    RuleButton ruleButton = new RuleButton(btnType,background,tvFactorName);
    hashMapBtnPort.put(portNum,ruleButton);
  }

  private void setPortButtonListener(){
    Iterator factorIterator = hashMapBtnPort.keySet().iterator();
    while(factorIterator.hasNext()){
      String key = (String) factorIterator.next();
      RuleButton ruleButton = hashMapBtnPort.get(key);
      ruleButton.getBtnFactor().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Iterator factorIterator = hashMapBtnPort.keySet().iterator();
          while(factorIterator.hasNext()){
            RuleButton ruleButton = hashMapBtnPort.get(factorIterator.next());
            ruleButton.getBackground().setBackgroundResource(R.color.colorBase);
            ruleButton.getText().setTextColor(getResources().getColor(R.color.colorPrimary));
          }
          ruleButton.getBackground().setBackgroundResource(R.color.colorPrimary);
          ruleButton.getText().setTextColor(getResources().getColor(R.color.colorBase));
          presenter.portClick(key);
        }
      });
    }
  }
  private void setFactorButtonListener(){
    Iterator factorIterator = hashMapBtnFactor.keySet().iterator();
    while(factorIterator.hasNext()){
      String key = (String) factorIterator.next();
      RuleButton ruleButton = hashMapBtnFactor.get(key);
      ruleButton.getBtnFactor().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Iterator factorIterator = hashMapBtnFactor.keySet().iterator();
          while(factorIterator.hasNext()){
            RuleButton ruleButton = hashMapBtnFactor.get(factorIterator.next());
            ruleButton.getBackground().setBackgroundResource(R.color.colorBase);
            ruleButton.getText().setTextColor(getResources().getColor(R.color.colorPrimary));
          }
          ruleButton.getBackground().setBackgroundResource(R.color.colorPrimary);
          ruleButton.getText().setTextColor(getResources().getColor(R.color.colorBase));
          presenter.factorClick(key);
        }
      });
    }
  }
  private void setFactor(LayoutInflater inflater){
    List<String> factorNames = new ArrayList<>();
    factorNames.add("temp");
    factorNames.add("humid");
    factorNames.add("light");
    factorNames.add("ec");
    factorNames.add("ph");
    factorNames.add("co2");

    for(int i = 0;i<factorNames.size();i++) {
      addFactor(inflater, factorNames.get(i));
    }
  }
  private void clearAddRule(){
    Iterator teratorFactor = hashMapBtnFactor.keySet().iterator();
    while(teratorFactor.hasNext()){
      RuleButton ruleButton = hashMapBtnFactor.get(teratorFactor.next());
      ruleButton.getBackground().setBackgroundResource(R.color.colorBase);
      ruleButton.getText().setTextColor(getResources().getColor(R.color.colorPrimary));
    }
    Iterator iteratorPort = hashMapBtnPort.keySet().iterator();
    while(iteratorPort.hasNext()){
      RuleButton ruleButton = hashMapBtnPort.get(iteratorPort.next());
      ruleButton.getBackground().setBackgroundResource(R.color.colorBase);
      ruleButton.getText().setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    actionOffBackground.setBackgroundResource(R.color.colorBase);
    actionOffText.setTextColor(getResources().getColor(R.color.colorPrimary));
    actionOnBackground.setBackgroundResource(R.color.colorBase);
    actionOnText.setTextColor(getResources().getColor(R.color.colorPrimary));

    conditionLowBackground.setBackgroundResource(R.color.colorBase);
    conditionUpBackground.setBackgroundResource(R.color.colorBase);
    conditionLowText.setTextColor(getResources().getColor(R.color.colorPrimary));
    conditionUpText.setTextColor(getResources().getColor(R.color.colorPrimary));
  }
  private void addFactor(LayoutInflater inflater,String factorName){
    View btnType = inflater.inflate(R.layout.rule_button, null);
    FrameLayout background = (FrameLayout) btnType.findViewById(R.id.rule_factor_background);
    TextView tvFactorName = (TextView) btnType.findViewById(R.id.rule_tv_factor_name);
    tvFactorName.setText(factorName);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    params.leftMargin = 30;
    params.rightMargin = 30;
    params.topMargin = 10;
    params.bottomMargin = 10;
    tvFactorName.setLayoutParams(params);
    tvFactorName.setTextColor(getResources().getColor(R.color.colorPrimary));
    background.setBackgroundResource(R.color.colorBase);

    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params1.leftMargin = 10;
    params1.rightMargin = 10;
    btnType.setLayoutParams(params1);
    factorSet.addView(btnType);

    RuleButton ruleButton = new RuleButton(btnType,background,tvFactorName);
    hashMapBtnFactor.put(factorName,ruleButton);
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  @Override
  public void refreshSpinner(final ArrayList<String> spinnerDatas) {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
        R.layout.spinner_item,R.id.tv_spinner,spinnerDatas);
    spinnerSerials.setAdapter(adapter);

    spinnerSerials.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.sensorSerialClick(spinnerDatas.get(position));
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
  }

  @Override
  public void refreshRecycler() {
    mRuleListAdapterView.refresh();
  }

  @Override
  public void showAddRule() {
    if(addRuleWindow.getVisibility() == View.GONE) {
      Animation animation = new TranslateAnimation(
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, -1.0f,
          Animation.RELATIVE_TO_SELF, 0f);
      animation.setDuration(300);
      addRuleWindow.setAnimation(animation);
      addRuleWindow.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void hideAddRule() {
    if(addRuleWindow.getVisibility() == View.VISIBLE) {
      Animation animation = new TranslateAnimation(
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, 0f,
          Animation.RELATIVE_TO_SELF, -1.0f);
      animation.setDuration(300);
      addRuleWindow.setAnimation(animation);
      addRuleWindow.setVisibility(View.GONE);
    }
  }

  @Override
  public void showToast(String msg){
    Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
  }
}
