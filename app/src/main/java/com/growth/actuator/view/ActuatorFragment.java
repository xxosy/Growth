package com.growth.actuator.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growth.R;
import com.growth.actuator.dagger.ActuatorModule;
import com.growth.actuator.dagger.DaggerActuatorComponent;
import com.growth.actuator.presenter.ActuatorPresenter;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.view.HomeActivity;
import com.growth.utils.ToastControl;
import com.growth.utils.ToastControlImlp;
import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.CurveGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraph;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraphVO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActuatorFragment extends Fragment implements ActuatorPresenter.View,
    OnKeyBackPressedListener {
  final private String TAG = ActuatorFragment.class.getName();
  private static final String ARG_PARAM1 = "serial";
  private static final String ARG_PARAM2 = "param2";
  View root;
  private Unbinder unbinder;
  @BindView(R.id.container_actuator)
  LinearLayout containerActuator;
  @BindView(R.id.actuator_graph)
  ViewGroup actuatorGraph;
  @BindView(R.id.actuator_detail)
  FrameLayout actuatorDetail;
  @BindView(R.id.img_actuator_detail_on_off)
  ImageView imgActuatorDetailOnOff;
  @BindView(R.id.tv_actuator_detail_port)
  TextView tvActuatorDetailPort;
  @BindView(R.id.btn_actuator_detail_switch)
  FrameLayout btnActuatorDetailSwitch;

  @Inject
  ActuatorPresenter presenter;

  ArrayList<View> actuators;
  private String serial;
  private String mParam2;
  ToastControl mToastControl;

  //graph
  CurveGraphView cgv;
  CurveGraphView preCgv;

  private OnFragmentInteractionListener mListener;

  public ActuatorFragment() {
    actuators = new ArrayList<>();
    // Required empty public constructor
  }

  public static ActuatorFragment newInstance(String param1, String param2) {
    ActuatorFragment fragment =  new ActuatorFragment();
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
      serial = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    ((HomeActivity) getActivity()).setOnKeyBackPressedListener(this);
  }

  private void addActuator(int i) {
    View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_actuator, containerActuator, false);
    TextView tvActuatorName = (TextView) view.findViewById(R.id.tv_actuator_name);
    String name;
    if (i == 0) name = "Motor";
    else if (i == 1) name = "Sprinkler";
    else if (i == 2) name = "Light";
    else if (i == 3) name = "Actuator" + i;
    else if (i == 4) name = "Actuator" + i;
    else if (i == 5) name = "Actuator" + i;
    else if (i == 6) name = "Actuator" + i;
    else name = "Actuator" + i;
    tvActuatorName.setText(name);
    FrameLayout btnActuator = (FrameLayout) view.findViewById(R.id.btn_actuator);
    FrameLayout btnActuatorDetail = (FrameLayout) view.findViewById(R.id.btn_actuator_detail);
    btnActuatorDetail.setOnClickListener(v -> presenter.btnActuatorDetailClick(i));
    btnActuator.setOnClickListener(v -> presenter.btnActuatorClick(i));
    actuators.add(view);
    ViewGroup.LayoutParams prams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    containerActuator.addView(view, 0, prams);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    DaggerActuatorComponent.builder()
        .actuatorModule(new ActuatorModule(this))
        .build()
        .inject(this);
    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_actuator, container, false);
    } catch (InflateException e) {

    }
    unbinder = ButterKnife.bind(this, root);
    for (int i = 0; i < 8; i++) {
      addActuator(i);
    }
    mToastControl = new ToastControlImlp(getActivity());
    presenter.enter();

    btnActuatorDetailSwitch.setOnClickListener(v -> presenter.btnActuatorDetailSwitchClick());
    return root;
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

  @Override
  public void showToast(String msg) {
    mToastControl.showToast(msg);
  }

  @Override
  public void stopProgress() {

  }

  @Override
  public void startProgress() {

  }

  @Override
  public void refreshActuatorState(int[] state) {
    int i = 0;
    for (View actuator : actuators) {
      if (state[i] == 0) {
        actuator.findViewById(R.id.img_actuator_off).setVisibility(View.VISIBLE);
        actuator.findViewById(R.id.img_actuator_on).setVisibility(View.INVISIBLE);
      } else if (state[i] == 1) {
        actuator.findViewById(R.id.img_actuator_on).setVisibility(View.VISIBLE);
        actuator.findViewById(R.id.img_actuator_off).setVisibility(View.INVISIBLE);
      }
      i++;
    }
  }

  @Override
  public void showActuatorDetail(int index, int state) {
    if (actuatorDetail.getVisibility() == View.GONE) {
      actuatorDetail.setVisibility(View.VISIBLE);
      if (state == 0)
        imgActuatorDetailOnOff.setImageResource(R.drawable.ic_off);
      else if (state == 1)
        imgActuatorDetailOnOff.setImageResource(R.drawable.ic_on);
      tvActuatorDetailPort.setText("Port : " + index);
    }

    String[] legend = new String[24];
    float[] data = new float[24];
    for (int i = 0; i < 24; i++) {
      legend[i] = String.valueOf(i);
      if (i < 10) {
        data[i] = 0;
      } else if (i > 10 && i < 20)
        data[i] = 1;
      else
        data[i] = 0;
    }
    String tag = "Power";
    int color = new Color().rgb(241, 169, 78);
    int max = 2;
    int term = 1;
    setCurveGraph(actuatorGraph, legend, data, tag, color, max, term);
  }

  @Override
  public void hideActuatorDetail() {
    if (actuatorDetail.getVisibility() == View.VISIBLE)
      actuatorDetail.setVisibility(View.GONE);
  }

  @Override
  public void refreshActuatorDetailState(int state) {
    if (state == 0)
      imgActuatorDetailOnOff.setImageResource(R.drawable.ic_off);
    else if (state == 1)
      imgActuatorDetailOnOff.setImageResource(R.drawable.ic_on);
  }

  private void setCurveGraph(ViewGroup viewGroup, String[] legendArr, float[] graph, String Name, int Color, int maxValue, int increment) {
    CurveGraphVO vo = makeCurveGraphAllSetting(legendArr, graph, Name, Color, maxValue, increment);
    cgv = new CurveGraphView(getActivity(), vo);
    int height = 745;
    int width = viewGroup.getWidth() - 2;
    ViewGroup.LayoutParams prams = new ViewGroup.LayoutParams(width, height);
    viewGroup.addView(cgv, prams);
    viewGroup.removeView(preCgv);
    preCgv = cgv;
  }

  private CurveGraphVO makeCurveGraphAllSetting(String[] legendArr, float[] graph, String Name, int Color, int maxValue, int increment) {
    //padding
    int paddingBottom = CurveGraphVO.DEFAULT_PADDING;
    int paddingTop = CurveGraphVO.DEFAULT_PADDING;
    int paddingLeft = CurveGraphVO.DEFAULT_PADDING;
    int paddingRight = CurveGraphVO.DEFAULT_PADDING;

    //graph margin
    int marginTop = CurveGraphVO.DEFAULT_MARGIN_TOP;
    int marginRight = CurveGraphVO.DEFAULT_MARGIN_RIGHT;

    //max value

    //increment

    List<CurveGraph> arrGraph = new ArrayList<CurveGraph>();
    arrGraph.add(new CurveGraph(Name, Color, graph));
    CurveGraphVO vo = new CurveGraphVO(
        paddingBottom, paddingTop, paddingLeft, paddingRight,
        marginTop, marginRight, maxValue, increment, legendArr, arrGraph);
    vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, 500));
    vo.setGraphBG(R.drawable.graph_background);
    vo.setGraphNameBox(new GraphNameBox());
    return vo;
  }

  @Override
  public void onBack() {
    if (actuatorDetail.getVisibility() == View.VISIBLE) {
      hideActuatorDetail();
    } else {
      HomeActivity homeActivity = (HomeActivity) getActivity();
      homeActivity.setOnKeyBackPressedListener(null);
      homeActivity.onBackPressed();
    }
  }

  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
