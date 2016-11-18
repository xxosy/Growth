package com.growth.SensorDataDisplay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.growth.R;
import com.growth.SensorDataDisplay.StateTag;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapter;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapterDataView;
import com.growth.SensorDataDisplay.dagger.DaggerSensorDataDisplayComponent;
import com.growth.SensorDataDisplay.dagger.SensorDataDisplayModule;
import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenter;
import com.growth.SensorValueGuide;
import com.growth.domain.Value;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.view.HomeActivity;
import com.growth.network.retrofit.RetrofitCreator;
import com.growth.utils.ProgressControl;
import com.growth.utils.ProgressControlImlp;
import com.growth.utils.ToastControl;
import com.growth.utils.ToastControlImlp;

import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorDataDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorDataDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorDataDisplayFragment extends Fragment implements SensorDataDisplayPresenter.View,
    OnKeyBackPressedListener {
  private static final String ARG_SERIAL = "serial";
  private String serial;

  @Inject
  SensorDataDisplayPresenter presenter;



  int harmfulImageSize;
  //Bind
  View root;
  private Unbinder unbinder;
  ////sensor_data_display
  @BindView(R.id.txt_sensor_data_display_temp)
  TextView txtTemp;
  @BindView(R.id.txt_sensor_data_display_humidity)
  TextView txtHumidity;
  @BindView(R.id.txt_sensor_data_display_light)
  TextView txtLight;
  @BindView(R.id.txt_sensor_data_display_co2)
  TextView txtCo2;
  @BindView(R.id.txt_sensor_data_display_ec)
  TextView txtEc;
  @BindView(R.id.txt_sensor_data_display_ph)
  TextView txtPh;
  ////progressbar
  @BindView(R.id.pb_temp)
  RoundCornerProgressBar pbTemp;
  @BindView(R.id.pb_humidity)
  RoundCornerProgressBar pbHumidity;
  @BindView(R.id.pb_light)
  RoundCornerProgressBar pbLight;
  @BindView(R.id.pb_ec)
  RoundCornerProgressBar pbEc;
  @BindView(R.id.pb_ph)
  RoundCornerProgressBar pbPh;
  @BindView(R.id.pb_co2)
  RoundCornerProgressBar pbCo2;
  ////sensor_value_state
  @BindView(R.id.state_temp)
  ImageView stateTemp;
  @BindView(R.id.state_humidity)
  ImageView stateHumidity;
  @BindView(R.id.state_light)
  ImageView stateLight;
  @BindView(R.id.state_co2)
  ImageView stateCo2;
  @BindView(R.id.state_ph)
  ImageView statePh;
  @BindView(R.id.state_ec)
  ImageView stateEc;
  /////change camera and state
  @BindView(R.id.frame_camera)
  FrameLayout frameCamera;
  @BindView(R.id.img_camera)
  ImageView imgCamera;
  @BindView(R.id.frame_state_view)
  FrameLayout frameStateView;
  @BindView(R.id.img_state_view)
  ImageView imgStateView;
  @BindView(R.id.btn_change)
  FloatingActionButton btnChange;
  @BindView(R.id.btn_mosquito)
  FloatingActionButton btnMosquito;
  @BindView(R.id.btn_state_view)
  FloatingActionButton btnView;
  @BindView(R.id.btn_camera)
  FloatingActionButton btnCamera;
  ////progressbar
  @BindView(R.id.progress_view)
  CircularProgressView progressView;
  @BindView(R.id.progress_layout)
  FrameLayout progressLayout;
  ////go graph
  @BindView(R.id.btn_graph_temp)
  LinearLayout btnGraphTemp;
  @BindView(R.id.btn_graph_humidity)
  LinearLayout btnGraphHumiditty;
  @BindView(R.id.btn_graph_light)
  LinearLayout btnGraphLight;
  @BindView(R.id.btn_graph_co2)
  LinearLayout btnGraphCo2;
  @BindView(R.id.btn_graph_ec)
  LinearLayout btnGraphEc;
  @BindView(R.id.btn_graph_ph)
  LinearLayout btnGraphPh;
  ////external whether
  @BindView(R.id.tv_whether)
  TextView tvWhether;
  @BindView(R.id.tv_external_temp)
  TextView tvExternalTemp;
  @BindView(R.id.tv_external_humidity)
  TextView tvExternalHumidity;
  @BindView(R.id.iv_whether)
  ImageView ivWhether;
  @BindView(R.id.swiperefresh)
  SwipeRefreshLayout mSwipeRefresh;
  @BindView(R.id.recycler_harmful_list)
  RecyclerView recyclerHarmfulList;
  private OnFragmentInteractionListener mListener;

  @Inject
  HarmfulListAdapterDataView mHarmfulListAdapterDataView;

  @BindView(R.id.harmful_list)
  FrameLayout harmfulList;
  @BindView(R.id.harmful_detail)
  FrameLayout harmfulDetail;
  @BindView(R.id.tv_harmful_title_detail)
  TextView tvHarmfulTitleDetail;
  @BindView(R.id.tv_harmful_description_detail)
  TextView tvHarmfulDescriptionDetail;
  @BindView(R.id.frame_img_harmful_detail)
  FrameLayout frameImagHarmfulDetail;
  @BindView(R.id.img_harmful_detail)
  ImageView imgHarmfulDetail;

  ProgressControl mProgressControl;
  ToastControl mToastControl;

  public SensorDataDisplayFragment() {
    // Required empty public constructor
  }

  public static SensorDataDisplayFragment newInstance(String serial) {
    SensorDataDisplayFragment fragment = new SensorDataDisplayFragment();
    Bundle args = new Bundle();
    args.putString(ARG_SERIAL, serial);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      serial = getArguments().getString(ARG_SERIAL);
    }
    ((HomeActivity) getActivity()).setOnKeyBackPressedListener(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    HarmfulListAdapter adapter = new HarmfulListAdapter(getActivity());
    DaggerSensorDataDisplayComponent.builder()
        .sensorDataDisplayModule(new SensorDataDisplayModule(this, adapter))
        .build()
        .inject(this);
    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_sensor_data_display, container, false);
    } catch (InflateException e) {
      Log.i("error", e.toString());
    }
    unbinder = ButterKnife.bind(this, root);
    recyclerHarmfulList.setAdapter(adapter);
    recyclerHarmfulList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mHarmfulListAdapterDataView.setOnRecyclerItemClickListener((adapter1, position) -> presenter.OnRecyclerItemClick(position));
    frameImagHarmfulDetail.setOnClickListener(v -> {
      if (frameImagHarmfulDetail.getLayoutParams().height != harmfulDetail.getHeight()) {
        harmfulImageSize = frameImagHarmfulDetail.getLayoutParams().height;
        frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, harmfulDetail.getHeight()));
      } else if (frameImagHarmfulDetail.getLayoutParams().height == harmfulDetail.getHeight()) {
        frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, harmfulImageSize));
      }
    });
    mProgressControl = new ProgressControlImlp(progressLayout, progressView);
    mToastControl = new ToastControlImlp(getActivity());
    presenter.onCreatedView(serial);
    btnChange.setOnClickListener(v -> presenter.btnChangeClick());
    btnMosquito.setOnClickListener(v -> presenter.btnMosquitoClick());
    btnView.setOnClickListener(v -> presenter.btnViewClick());
    btnCamera.setOnClickListener(v -> presenter.btnCameraClick());
    btnGraphTemp.setOnClickListener(v -> presenter.btnGraphTempClick());
    btnGraphHumiditty.setOnClickListener(v -> presenter.btnGraphHumidityClick());
    btnGraphCo2.setOnClickListener(v -> presenter.btnGraphCo2Click());
    btnGraphLight.setOnClickListener(v -> presenter.btnGraphLightClick());
    btnGraphEc.setOnClickListener(v -> presenter.btnGraphEcClick());
    btnGraphPh.setOnClickListener(v -> presenter.btnGraphPhClick());

    mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

    mSwipeRefresh.setOnRefreshListener(() -> presenter.swipePage(serial));
    return root;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void refreshSwipe() {
    mSwipeRefresh.setRefreshing(false);
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
    presenter.unSubscribe();
  }

  @Override
  public void refreshData(Value value) {
    String temperature=value.getTemperature();
    if(value.getTemperature().length()>5)
      temperature = value.getTemperature().substring(0, 4);
    String humidity=value.getHumidity();
    if(humidity.length()>5)
      humidity = humidity.substring(0, 4);
    String light=value.getLight();
    if(light.length()>5)
      light = light.substring(0, 4);
    String ec=String.valueOf(Float.parseFloat(value.getEc()) / 15);
    if(ec.length()>5)
      ec = ec.substring(0, 4);
    String ph=value.getPh();
    if(ph.length()>5)
      ph = ph.substring(0, 4);
    String co2=value.getCo2();
    if(co2.length()>5)
      co2 = co2.substring(0, 4);

    txtTemp.setText(String.format("%s°C", temperature));
    pbTemp.setProgress(Float.parseFloat(value.getTemperature()));
    txtHumidity.setText(String.format("%s%%", humidity));
    pbHumidity.setProgress(Float.parseFloat(value.getHumidity()));
    txtLight.setText(String.format("%slux", light));
    pbLight.setProgress(Float.parseFloat(value.getLight()));
    txtEc.setText(String.format("%s\nus/cm", ec));
    pbEc.setProgress(Float.parseFloat(value.getEc()) / 15);
    txtPh.setText(ph);
    pbPh.setProgress(Float.parseFloat(value.getPh()));
    txtCo2.setText(String.format("%sppm", co2));
    pbCo2.setProgress(Float.parseFloat(value.getCo2()));
  }

  @Override
  public void refreshState(HashMap<String, Boolean> states) {
    Set<String> tags = states.keySet();
    for (String tag : tags) {
      if (states.get(tag)) {
        if (tag.equals(StateTag.TEMPERATURE)) stateTemp.setImageResource(R.drawable.ic_smile);
        if (tag.equals(StateTag.HUMIDITY)) stateHumidity.setImageResource(R.drawable.ic_smile);
        if (tag.equals(StateTag.LIGHT)) stateLight.setImageResource(R.drawable.ic_smile);
        if (tag.equals(StateTag.CO2)) stateCo2.setImageResource(R.drawable.ic_smile);
        if (tag.equals(StateTag.EC)) stateEc.setImageResource(R.drawable.ic_smile);
        if (tag.equals(StateTag.PH)) statePh.setImageResource(R.drawable.ic_smile);
      } else {
        if (tag.equals(StateTag.TEMPERATURE)) stateTemp.setImageResource(R.drawable.ic_cry);
        if (tag.equals(StateTag.HUMIDITY)) stateHumidity.setImageResource(R.drawable.ic_cry);
        if (tag.equals(StateTag.LIGHT)) stateLight.setImageResource(R.drawable.ic_cry);
        if (tag.equals(StateTag.CO2)) stateCo2.setImageResource(R.drawable.ic_cry);
        if (tag.equals(StateTag.EC)) stateEc.setImageResource(R.drawable.ic_cry);
        if (tag.equals(StateTag.PH)) statePh.setImageResource(R.drawable.ic_cry);
      }
    }
  }

  @Override
  public void refreshCameraImage(String serial) {
    Glide.with(getActivity()).load(RetrofitCreator.getBaseUrl() + "/gallery/recent/" + serial).into(imgCamera);
  }

  @Override
  public void refreshMosquitoImage(Bitmap image) {
    if(image != null){
      image = Bitmap.createScaledBitmap(image, frameCamera.getWidth(), frameCamera.getHeight(), true);
      imgCamera.setImageBitmap(image);
    }
  }

  @Override
  public void refreshStateView(Value value) {
    if (Float.valueOf(value.getLight()) < SensorValueGuide.GUIDE_LIGHT_MIN) {
      if (Float.valueOf(value.getTemperature()) < SensorValueGuide.GUIDE_TEMP_MIN)
        imgStateView.setImageResource(R.drawable.illust_night_cold);
      else if (Float.valueOf(value.getTemperature()) > SensorValueGuide.GUIDE_TEMP_MIN)
        imgStateView.setImageResource(R.drawable.illust_night_hot);
    } else if (Float.valueOf(value.getLight()) > SensorValueGuide.GUIDE_LIGHT_MIN)
      if (Float.valueOf(value.getTemperature()) < SensorValueGuide.GUIDE_TEMP_MIN)
        imgStateView.setImageResource(R.drawable.illust_day_cold);
      else if (Float.valueOf(value.getTemperature()) > SensorValueGuide.GUIDE_TEMP_MIN)
        imgStateView.setImageResource(R.drawable.illust_day_hot);
  }

  @Override
  public void showCameraFrame() {
    frameCamera.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideCameraFrame() {
    frameCamera.setVisibility(View.GONE);
  }

  @Override
  public void changeBtn(int state) {
    if (state == 0) btnChange.setImageResource(R.drawable.ic_view);
    else if (state == 1) btnChange.setImageResource(R.drawable.ic_camera);
    else if (state == 2) btnChange.setImageResource(R.drawable.ic_mosquito);
  }

  @Override
  public void showButton() {
    setFoldingButtonShowAnimation(btnCamera);
    setFoldingButtonShowAnimation(btnView);
    setFoldingButtonShowAnimation(btnMosquito);

    btnCamera.setVisibility(View.VISIBLE);
    btnView.setVisibility(View.VISIBLE);
    btnMosquito.setVisibility(View.VISIBLE);
  }
  private void setFoldingButtonShowAnimation(View view){
    ViewGroup.MarginLayoutParams btnChangeLp = (ViewGroup.MarginLayoutParams) btnChange.getLayoutParams();
    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    Animation animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.ABSOLUTE, lp.bottomMargin - btnChangeLp.bottomMargin,
        Animation.RELATIVE_TO_SELF, 0f);
    animation.setDuration(300);
    view.setAnimation(animation);
  }
  @Override
  public void hideButton() {
    setFoldingButtonHideAnimation(btnCamera);
    setFoldingButtonHideAnimation(btnView);
    setFoldingButtonHideAnimation(btnMosquito);

    btnCamera.setVisibility(View.GONE);
    btnView.setVisibility(View.GONE);
    btnMosquito.setVisibility(View.GONE);
  }
  private void setFoldingButtonHideAnimation(View view){
    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    ViewGroup.MarginLayoutParams btnChangeLp = (ViewGroup.MarginLayoutParams) btnChange.getLayoutParams();
    Animation animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.ABSOLUTE, lp.bottomMargin - btnChangeLp.bottomMargin);
    animation.setDuration(300);
    view.setAnimation(animation);
  }
  @Override
  public void showHarmfulList() {
    Animation animation = getShowAnimation(300);
    if (harmfulList.getVisibility() == View.GONE) {
      harmfulList.setAnimation(animation);
      harmfulList.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void hideHarmfulList() {
    Animation animation = getHideAnimation(300);
    if (harmfulList.getVisibility() == View.VISIBLE) {
      harmfulList.setAnimation(animation);
      harmfulList.setVisibility(View.GONE);
    }
  }

  @Override
  public void showHarmfulDetail(String title, String description, String url) {
    Animation animation = new AlphaAnimation(0f, 1f);
    animation.setDuration(300);
    tvHarmfulTitleDetail.setText(title);
    tvHarmfulDescriptionDetail.setText(description);
    Glide.with(getActivity()).load(url).into(imgHarmfulDetail);
    if (harmfulDetail.getVisibility() == View.GONE) {
      harmfulDetail.setAnimation(animation);
      harmfulDetail.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void hideHarmfulDetail() {
    Animation animation = new AlphaAnimation(1f, 0f);
    animation.setDuration(300);
    if (harmfulDetail.getVisibility() == View.VISIBLE) {
      harmfulDetail.setAnimation(animation);
      harmfulDetail.setVisibility(View.GONE);
    }
  }

  @Override
  public void refreshHarmfulList() {
    mHarmfulListAdapterDataView.refresh();
    recyclerHarmfulList.scrollToPosition(0);
  }

  public void startProgress() {
    mProgressControl.startProgress();
  }

  public void stopProgress() {
    mProgressControl.stopProgress();
  }

  @Override
  public void refreshWhether(String whether, String externTemp, String externHumidity, String iconUrl) {
    tvWhether.setText(whether);
    tvExternalTemp.setText(String.format("%s°C", externTemp));
    tvExternalHumidity.setText(String.format("%s%%", externHumidity));
    Glide.with(getActivity()).load(iconUrl).into(ivWhether);
  }

  @Override
  public void showToast(String msg) {
    mToastControl.showToast(msg);
  }

  @Override
  public void onBack() {
    if (frameImagHarmfulDetail!=null && frameImagHarmfulDetail.getLayoutParams().height == harmfulDetail.getHeight()) {
      frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, harmfulImageSize));
    } else if (harmfulDetail!=null && harmfulDetail.getVisibility() == View.VISIBLE) {
      hideHarmfulDetail();
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
  private Animation getShowAnimation(int duration){
    Animation animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 1.0f,
        Animation.RELATIVE_TO_SELF, 0f);
    animation.setDuration(duration);

    return animation;
  }

  private Animation getHideAnimation(int duration){
    Animation animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 1.0f);
    animation.setDuration(duration);

    return animation;
  }
}
