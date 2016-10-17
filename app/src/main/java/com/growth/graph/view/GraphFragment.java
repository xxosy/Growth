package com.growth.graph.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.growth.R;
import com.growth.SensorValueGuide;
import com.growth.domain.StandardGrowthData;
import com.growth.domain.Value;
import com.growth.domain.graph.GraphItem;
import com.growth.domain.graph.GraphList;
import com.growth.graph.dagger.DaggerGraphComponent;
import com.growth.graph.dagger.GraphModule;
import com.growth.graph.presenter.GraphPresenter;
import com.growth.utils.ProgressControl;
import com.growth.utils.ProgressControlImlp;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment implements GraphPresenter.View,
    View.OnClickListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private int mainValueType = ValueTpye.TEMPERATURE;
  // TODO: Rename and change types of parameters
  private String serial;
  private int initValueType;
  //Bind
  View root;
  private Unbinder unbinder;
  ////change date
  @BindView(R.id.btn_date_pre)
  FrameLayout btnDatePre;
  @BindView(R.id.btn_date_next)
  FrameLayout btnDateNext;
  ////display data
  @BindView(R.id.tv_sensor_data)
  TextView tvSensorData;
  @BindView(R.id.tv_max_sensor_data)
  TextView tvMaxSensorData;
  @BindView(R.id.tv_min_sensor_data)
  TextView tvMinSensorData;
  @BindView(R.id.tv_today)
  TextView tvToday;
  @BindView(R.id.tv_update_time)
  TextView tvUpdateTime;
  @BindView(R.id.tv_graph_date)
  TextView tvGraphDate;
  @BindView(R.id.graphview)
  ViewGroup mChart;
  ////tab
  @BindView(R.id.tab_temp)
  FrameLayout tabTemp;
  @BindView(R.id.tab_humidity)
  FrameLayout tabHumidity;
  @BindView(R.id.tab_light)
  FrameLayout tabLight;
  @BindView(R.id.tab_co2)
  FrameLayout tabCo2;
  @BindView(R.id.tab_ec)
  FrameLayout tabEc;
  @BindView(R.id.tab_ph)
  FrameLayout tabPh;
  @BindView(R.id.tab_soil_moisture)
  FrameLayout tabSoil_Moisture;
  @BindView(R.id.tab_marker_temp)
  View tabMarkerTemp;
  @BindView(R.id.tab_marker_humidity)
  View tabMarkerHumidity;
  @BindView(R.id.tab_marker_light)
  View tabMarkerLight;
  @BindView(R.id.tab_marker_co2)
  View tabMarkerCo2;
  @BindView(R.id.tab_marker_ec)
  View tabMarkerEc;
  @BindView(R.id.tab_marker_ph)
  View tabMarkerPh;
  @BindView(R.id.tab_marker_soil_moisture)
  View tabMarkerSoilMoisture;
  ////progressbar
  @BindView(R.id.progress_view)
  CircularProgressView progressView;
  @BindView(R.id.progress_layout)
  FrameLayout progressLayout;

  ProgressControl mProgressControl;
  ToastControl mToastControl;
  //graph
  CurveGraphView cgv;
  CurveGraphView preCgv;

  private OnFragmentInteractionListener mListener;
  //presenter
  @Inject
  GraphPresenter presenter;

  public GraphFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment GraphFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GraphFragment newInstance(String param1, int param2) {
    GraphFragment fragment = new GraphFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putInt(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      serial = getArguments().getString(ARG_PARAM1);
      initValueType = getArguments().getInt(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    DaggerGraphComponent.builder()
        .graphModule(new GraphModule(this))
        .build()
        .inject(this);
    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_graph, container, false);
    } catch (InflateException e) {

    }
    unbinder = ButterKnife.bind(this, root);
    mProgressControl = new ProgressControlImlp(progressLayout, progressView);
    mToastControl = new ToastControlImlp(getActivity());
    btnDatePre.setOnClickListener(this);
    btnDateNext.setOnClickListener(this);

    tabTemp.setOnClickListener(this);
    tabHumidity.setOnClickListener(this);
    tabLight.setOnClickListener(this);
    tabCo2.setOnClickListener(this);
    tabPh.setOnClickListener(this);
    tabEc.setOnClickListener(this);
    tabSoil_Moisture.setOnClickListener(this);
    mainValueType = initValueType;
    presenter.enterFragment(serial, initValueType);
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
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void refreshPage(Value value) {
    refreshUpdateTime(value.getUpdate_time());
    refreshToday(value.getUpdate_date());
    refreshSensorData(value);
    refreshMaxSensorData();
    refreshMinSensorData();
    refreshGraphDate(value.getUpdate_date());
  }

  @Override
  public void refreshUpdateTime(String time) {
    tvUpdateTime.setText(time);
  }

  @Override
  public void refreshToday(String today) {
    tvToday.setText(today);
  }

  @Override
  public void refreshSensorData(Value value) {
    String main = value.getTemperature();
    if (mainValueType == ValueTpye.TEMPERATURE) {
      main = value.getTemperature();
      if (main.length() > 4)
        main = main.substring(0, 4);
      main = main.concat("°C");
    } else if (mainValueType == ValueTpye.CO2) {
      main = value.getCo2();
      main = main.concat("ppm");
    } else if (mainValueType == ValueTpye.HUMIDITY) {
      main = value.getHumidity();
      if (main.length() > 4) {
        main = main.substring(0, 4);
      }
      main = String.valueOf(Float.valueOf(main) + 30);
      main = main.concat("%");
    } else if (mainValueType == ValueTpye.LIGHT) {
      main = value.getLight();
      main = main.concat("lux");
    } else if (mainValueType == ValueTpye.PH) {
      main = value.getPh();
      if (main.length() > 4)
        main = main.substring(0, 4);
    } else if (mainValueType == ValueTpye.EC) {
      main = String.valueOf(Float.parseFloat(value.getEc()) / 15);
      main = main.substring(0, 4);
      main = main.concat("us/cm");
    } else if (mainValueType == ValueTpye.SOIL_MOISTURE) {
      main = value.getSoil_moisture();
      main = main.substring(0, 4);
      main = main.concat("mm");
    }
    tvSensorData.setText(main);
  }

  @Override
  public void refreshMaxSensorData() {
    float maxSensorData = SensorValueGuide.GUIDE_TEMP_MAX;
    if (mainValueType == ValueTpye.TEMPERATURE) {
      maxSensorData = SensorValueGuide.GUIDE_TEMP_MAX;
    } else if (mainValueType == ValueTpye.HUMIDITY) {
      maxSensorData = SensorValueGuide.GUIDE_HUMIDITY_MAX;
    } else if (mainValueType == ValueTpye.CO2) {
      maxSensorData = SensorValueGuide.GUIDE_CO2_MAX;
    } else if (mainValueType == ValueTpye.LIGHT) {
      maxSensorData = SensorValueGuide.GUIDE_LIGHT_MAX;
    } else if (mainValueType == ValueTpye.PH) {
      maxSensorData = SensorValueGuide.GUIDE_PH_MAX;
    } else if (mainValueType == ValueTpye.EC) {
      maxSensorData = SensorValueGuide.GUIDE_EC_MAX;
    }
    tvMaxSensorData.setText(String.valueOf(maxSensorData));
  }

  @Override
  public void refreshMinSensorData() {
    float minSensorData = SensorValueGuide.GUIDE_TEMP_MIN;
    if (mainValueType == ValueTpye.TEMPERATURE) {
      minSensorData = SensorValueGuide.GUIDE_TEMP_MIN;
    } else if (mainValueType == ValueTpye.HUMIDITY) {
      minSensorData = SensorValueGuide.GUIDE_HUMIDITY_MIN;
    } else if (mainValueType == ValueTpye.CO2) {
      minSensorData = SensorValueGuide.GUIDE_CO2_MIN;
    } else if (mainValueType == ValueTpye.LIGHT) {
      minSensorData = SensorValueGuide.GUIDE_LIGHT_MIN;
    } else if (mainValueType == ValueTpye.PH) {
      minSensorData = SensorValueGuide.GUIDE_PH_MIN;
    } else if (mainValueType == ValueTpye.EC) {
      minSensorData = SensorValueGuide.GUIDE_EC_MIN;
    }
    tvMinSensorData.setText(String.valueOf(minSensorData));
  }

  @Override
  public void refreshGraphDate(String date) {
    tvGraphDate.setText(date);
  }

  private void setCurveGraph(ViewGroup viewGroup, String[] legendArr, float[] graph, String Name, int Color, int maxValue, int increment, CurveGraph standard) {
    CurveGraphVO vo = makeCurveGraphAllSetting(legendArr, graph, Name, Color, maxValue, increment, standard);
    cgv = new CurveGraphView(getActivity(), vo);
    int height = viewGroup.getHeight() - 2;
    ViewGroup.LayoutParams prams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
    viewGroup.addView(cgv, prams);
    viewGroup.removeView(preCgv);
    preCgv = cgv;
  }

  private CurveGraphVO makeCurveGraphAllSetting(String[] legendArr, float[] graph, String Name, int Color, int maxValue, int increment, CurveGraph standard) {
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
    arrGraph.add(standard);
    CurveGraphVO vo = new CurveGraphVO(
        paddingBottom, paddingTop, paddingLeft, paddingRight,
        marginTop, marginRight, maxValue, increment, legendArr, arrGraph);
    vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, 1000));
    vo.setGraphBG(R.drawable.graph_background);
    vo.setGraphNameBox(new GraphNameBox());
    return vo;
  }

  @Override
  public void refreshChart(GraphList items) {
    int length = 24;
    int k = 0;
    if (items.getGraphItems().length > 24) {
      length = 24;
      k = items.getGraphItems().length - 24;
    } else if (items.getGraphItems().length <= 24) length = items.getGraphItems().length;
    String[] legend = new String[length];
    float[] data = new float[length];
    float[] standard = new float[length];
    int i = 0;
    float fmax = 0;
    String tag = "Temperature";
    int term = 10;
    int color = new Color().rgb(0, 153, 255);
    for (GraphItem item : items.getGraphItems()) {
      if (i - k >= 0) {
        legend[i - k] = item.getUpdate_time().substring(0, 2);
        if (item.getTemperature() != null) {
          standard[i - k] = StandardGrowthData.temperature[i];
          if(item.getTemperature().equals("")){
            Log.i("update time",item.getUpdate_time());
            data[i - k] = 0;
          }else{
            data[i - k] = Float.parseFloat(item.getTemperature());
          }

          tag = "Temperature";
          color = new Color().rgb(0, 153, 255);
        } else if (item.getHumidity() != null) {
          standard[i - k] = StandardGrowthData.humidity[i];
          data[i - k] = Float.parseFloat(item.getHumidity()) + 30;
          tag = "Humidity";
          color = new Color().rgb(0, 204, 255);
        } else if (item.getCo2() != null) {
          standard[i - k] = StandardGrowthData.co2[i];
          data[i - k] = Float.parseFloat(item.getCo2());
          tag = "CO2";
          color = new Color().rgb(0, 128, 128);
        } else if (item.getLight() != null) {
          standard[i - k] = StandardGrowthData.light[i];
          data[i - k] = Float.parseFloat(item.getLight());
          tag = "Light";
          color = new Color().rgb(255, 204, 0);
        } else if (item.getPh() != null) {
          standard[i - k] = StandardGrowthData.ph[i];
          data[i - k] = Float.parseFloat(item.getPh());
          tag = "PH";
          color = new Color().rgb(0, 255, 0);
        } else if (item.getEc() != null) {
          standard[i - k] = StandardGrowthData.ec[i];
          data[i - k] = Float.parseFloat(item.getEc()) / 15;
          tag = "EC";
          color = new Color().rgb(255, 140, 20);
        } else if (item.getSoil_moisture() != null) {
          data[i - k] = Float.parseFloat(item.getSoil_moisture());
          tag = "Soil Moisture";
          color = new Color().rgb(93, 76, 70);
        }
        if (fmax < data[i - k])
          fmax = data[i - k];
        if (fmax < standard[i - k])
          fmax = standard[i - k];
      }
      i++;
    }
    int max = (int) fmax;
    max += 10;
    max = max / 10 * 10;
    term = max / 4;
    CurveGraph standardGraph = new CurveGraph("standard", new Color().rgb(128, 128, 128), standard);
    if (length > 2)
      setCurveGraph(mChart, legend, data, tag, color, max, term, standardGraph);
    else
      displayToast("Data is not enough (The number MIN:2)");
  }

  @Override
  public void displayToast(String msg) {
    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void refreshTab() {
    if (mainValueType == ValueTpye.TEMPERATURE) {
      refreshTabMarker(tabMarkerTemp);
    } else if (mainValueType == ValueTpye.HUMIDITY) {
      refreshTabMarker(tabMarkerHumidity);
    } else if (mainValueType == ValueTpye.CO2) {
      refreshTabMarker(tabMarkerCo2);
    } else if (mainValueType == ValueTpye.LIGHT) {
      refreshTabMarker(tabMarkerLight);
    } else if (mainValueType == ValueTpye.PH) {
      refreshTabMarker(tabMarkerPh);
    } else if (mainValueType == ValueTpye.EC) {
      refreshTabMarker(tabMarkerEc);
    } else if (mainValueType == ValueTpye.SOIL_MOISTURE) {
      refreshTabMarker(tabMarkerSoilMoisture);
    }
  }

  private void refreshTabMarker(View view) {
    tabMarkerTemp.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerHumidity.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerLight.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerCo2.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerPh.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerEc.setBackgroundResource(R.color.colorPrimaryDark);
    tabMarkerSoilMoisture.setBackgroundResource(R.color.colorPrimaryDark);
    view.setBackgroundResource(R.color.colorAccent);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.tab_temp) {
      if (mainValueType != ValueTpye.TEMPERATURE) {
        mainValueType = ValueTpye.TEMPERATURE;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_humidity) {
      if (mainValueType != ValueTpye.HUMIDITY) {
        mainValueType = ValueTpye.HUMIDITY;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_light) {
      if (mainValueType != ValueTpye.LIGHT) {
        mainValueType = ValueTpye.LIGHT;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_co2) {
      if (mainValueType != ValueTpye.CO2) {
        mainValueType = ValueTpye.CO2;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_ec) {
      if (mainValueType != ValueTpye.EC) {
        mainValueType = ValueTpye.EC;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_ph) {
      if (mainValueType != ValueTpye.PH) {
        mainValueType = ValueTpye.PH;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.tab_soil_moisture) {
      if (mainValueType != ValueTpye.SOIL_MOISTURE) {
        mainValueType = ValueTpye.SOIL_MOISTURE;
        presenter.tabClick(mainValueType);
      }
    } else if (v.getId() == R.id.btn_date_pre) {
      presenter.datePreButtonClick(mainValueType);
    } else if (v.getId() == R.id.btn_date_next) {
      presenter.dateNextButtonClick(mainValueType);
    }
  }

  @Override
  public void startProgress() {
    mProgressControl.startProgress();
  }

  @Override
  public void stopProgress() {
    mProgressControl.stopProgress();
  }

  @Override
  public void showToast(String msg) {
    mToastControl.showToast(msg);
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
}
