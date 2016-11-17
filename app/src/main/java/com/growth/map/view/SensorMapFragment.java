package com.growth.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.growth.R;
import com.growth.domain.sensor.SensorItem;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.view.HomeActivity;
import com.growth.map.dagger.DaggerSensorMapComponent;
import com.growth.map.dagger.SensorMapModule;
import com.growth.map.presenter.SensorMapPresenter;
import com.growth.utils.GpsInfo;
import com.growth.utils.ProgressControl;
import com.growth.utils.ProgressControlImlp;
import com.growth.utils.ToastControl;
import com.growth.utils.ToastControlImlp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SensorMapFragment extends Fragment implements OnMapReadyCallback,
    SensorMapPresenter.View,
    OnKeyBackPressedListener {
  private final int ANIMATION_DURAITON = 300;

  //Google Map
  private GoogleMap mMap;

  //Bind
  private Unbinder unbinder;
  @BindView(R.id.btn_addwindow_ok)
  FrameLayout btnAddWindowOk;
  @BindView(R.id.btn_addwindow_cancel)
  FrameLayout btnAddWindowCancel;
  @BindView(R.id.btn_get_location)
  FrameLayout btnAddWindowGetLocation;
  @BindView(R.id.btn_serial_check)
  FrameLayout btnAddWindowSerialCheck;
  @BindView(R.id.et_lat)
  EditText etAddWindowLat;
  @BindView(R.id.et_lng)
  EditText etAddWindowLng;
  @BindView(R.id.et_serial)
  EditText etAddWindowSerial;
  @BindView(R.id.et_title)
  EditText etAddWindowTitle;
  @BindView(R.id.add_sensor_window)
  LinearLayout addSensorWindow;
  @BindView(R.id.floatingActionButton)
  FloatingActionButton floatingActionButton;
  //info window
  @BindView(R.id.info_window)
  LinearLayout infoWindow;
  @BindView(R.id.txt_infowindow_title)
  TextView txtInfoWindowTitle;
  @BindView(R.id.txt_infowindow_humidity)
  TextView txtInfoWindowHumidity;
  @BindView(R.id.txt_infowindow_serial)
  TextView txtInfoWindowSerial;
  @BindView(R.id.btn_info_window_detail)
  FrameLayout btnInfoWindowDetail;
  @BindView(R.id.btn_info_window_graph)
  FrameLayout btnInfoWindowGraph;
  @BindView(R.id.btn_info_window_delete)
  FrameLayout btnInfoWindowDelete;
  @BindView(R.id.btn_info_window_update)
  FrameLayout btnInfoWindowUpdate;
  @BindView(R.id.btn_info_window_actuator)
  FrameLayout btnInfoWindowActuator;
  ////zoom
  @BindView(R.id.btn_map_zoom_in)
  FrameLayout btnZoomIn;
  @BindView(R.id.btn_map_zoom_out)
  FrameLayout btnZoomOut;
  ////location
  @BindView(R.id.btn_map_location)
  FrameLayout btnLocation;
  ////progressbar
  @BindView(R.id.progress_view)
  CircularProgressView progressView;
  @BindView(R.id.progress_layout)
  FrameLayout progressLayout;
  GpsInfo gps;
  private static View root;
  //presenter
  @Inject
  SensorMapPresenter presenter;
  ProgressControl mProgressControl;
  ToastControl mToastControl;

  Marker mCurrentMaker;

  private OnFragmentInteractionListener mListener;

  public static SensorMapFragment newInstance() {
    SensorMapFragment fragment = new SensorMapFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((HomeActivity) getActivity()).setOnKeyBackPressedListener(this);
  }

  @Override
  public void clearMap() {
    mMap.clear();
  }

  @Override
  public void refreshAddWindowUpdateSensor(String serial, String title, String lat, String lng) {
    etAddWindowSerial.setFocusableInTouchMode(false);
    etAddWindowSerial.setText(serial);
    etAddWindowTitle.setText(title);
    etAddWindowLat.setText(lat);
    etAddWindowLng.setText(lng);
  }

  @Override
  public void checkSerialSuccess() {
    btnAddWindowSerialCheck.setBackgroundResource(R.color.colorPrimary);
    btnAddWindowSerialCheck.setClickable(false);
    showToast(getString(R.string.toast_message_available_serial));
  }

  @Override
  public void checkSerialFail() {
    btnAddWindowSerialCheck.setBackgroundResource(R.drawable.selector_reverse);
    showToast(getString(R.string.toast_message_disavailable_serial));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    DaggerSensorMapComponent.builder()
        .sensorMapModule(new SensorMapModule(this))
        .build()
        .inject(this);

    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_map, container, false);
    } catch (InflateException e) {

    }
    gps = new GpsInfo(getActivity());
    unbinder = ButterKnife.bind(this, root);
    mProgressControl = new ProgressControlImlp(progressLayout, progressView);
    mToastControl = new ToastControlImlp(getActivity());
    floatingActionButton.setOnClickListener(v -> presenter.floatingActionButtonClick());

    btnAddWindowGetLocation.setOnClickListener(v -> presenter.addWindowGetLocationClick());
    btnAddWindowCancel.setOnClickListener(v -> presenter.addWindowCancelClick());
    btnAddWindowSerialCheck.setOnClickListener(v -> {
      if (etAddWindowSerial.getText().toString().equals("")) {
        showToast(getString(R.string.toast_message_empty_serial));
      } else {
        String serial = etAddWindowSerial.getText().toString();
        presenter.addWindowCheckSerialClick(serial);
      }
    });
    btnAddWindowOk.setOnClickListener(v -> {
      String lat = etAddWindowLat.getText().toString();
      String lng = etAddWindowLng.getText().toString();
      String serial = etAddWindowSerial.getText().toString();
      String title = etAddWindowTitle.getText().toString();
      presenter.addWindowOkClick(serial, title, lat, lng);
    });
    SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    btnInfoWindowDetail.setOnClickListener(v -> {
      ((HomeActivity) getActivity()).setOnKeyBackPressedListener(null);
      presenter.infoWindowDetailClick();
    });
    btnInfoWindowGraph.setOnClickListener(v -> {
      ((HomeActivity) getActivity()).setOnKeyBackPressedListener(null);
      presenter.infoWindowGraphClick();
    });

    btnInfoWindowDelete.setOnClickListener(v -> presenter.infoWindowDeleteSensorClick());
    btnInfoWindowUpdate.setOnClickListener(v -> presenter.infoWindowUpdateSensorClick());
    btnInfoWindowActuator.setOnClickListener(v -> presenter.infoWindowActuatorClick());

    btnZoomIn.setOnClickListener(v -> presenter.btnZoomInClick());
    btnZoomOut.setOnClickListener(v -> presenter.btnZoomOutClick());
    btnLocation.setOnClickListener(v -> presenter.btnLocationClick(gps));

    presenter.onCreatedView();

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
    presenter.unSubscribe();
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnMarkerClickListener(marker -> {
      mCurrentMaker = marker;
      String title = marker.getTitle();
      presenter.markerClick(title);
      return true;
    });
  }

  @Override
  public void addMarker(SensorItem sensorItem, boolean isUpdating) {
    double lat = Double.parseDouble(sensorItem.getLat());
    double lng = Double.parseDouble(sensorItem.getLng());
    LatLng latLng = new LatLng(lat, lng);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
    Bitmap icon;
    if (isUpdating)
      icon = resizeMapIcons("ic_sprout", 110, 80);
    else
      icon = resizeMapIcons("ic_sprout_wilt", 110, 80);
    MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon))
        .position(latLng)
        .title(sensorItem.getTitle());
    mMap.addMarker(markerOptions);
  }

  public Bitmap resizeMapIcons(String iconName, int width, int height) {
    Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
    Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    return resizedBitmap;
  }

  @Override
  public void showInfoWindow(String title, String serial, String humidity) {
    if (addSensorWindow.getVisibility() == View.VISIBLE) {
      hideAddSensorWindow();
    }
    if (infoWindow.getVisibility() == View.GONE) {
      Animation animation = getShowAnimation(ANIMATION_DURAITON);

      txtInfoWindowTitle.setText(title);
      txtInfoWindowSerial.setText(serial);
      txtInfoWindowHumidity.setText(humidity);

      infoWindow.setVisibility(View.VISIBLE);
      infoWindow.setAnimation(animation);
    } else {
      hideInfoWindow();
      showInfoWindow(title, serial, humidity);
    }
  }

  @Override
  public void hideInfoWindow() {
    if (infoWindow.getVisibility() == View.VISIBLE) {
      Animation animation = getHideAnimation(ANIMATION_DURAITON);
      infoWindow.setAnimation(animation);
      infoWindow.setVisibility(View.GONE);
    }
  }

  @Override
  public void showAddSensorWindow() {
    if (infoWindow.getVisibility() == View.VISIBLE) {
      hideInfoWindow();
    }
    if (addSensorWindow.getVisibility() == View.GONE) {
      Animation animation = getShowAnimation(ANIMATION_DURAITON);
      addSensorWindow.setVisibility(View.VISIBLE);
      addSensorWindow.setAnimation(animation);
    }
  }

  @Override
  public void hideAddSensorWindow() {
    if (addSensorWindow.getVisibility() == View.VISIBLE) {
      Animation animation = getHideAnimation(ANIMATION_DURAITON);
      addSensorWindow.setAnimation(animation);
      addSensorWindow.setVisibility(View.GONE);
    }
  }

  @Override
  public void clearAddWindow() {
    etAddWindowSerial.setFocusableInTouchMode(true);
    btnAddWindowSerialCheck.setClickable(true);
    btnAddWindowSerialCheck.setBackgroundResource(R.drawable.selector_reverse);
    etAddWindowLat.getText().clear();
    etAddWindowLng.getText().clear();
    etAddWindowSerial.getText().clear();
    etAddWindowTitle.getText().clear();
  }

  @Override
  public void fillEditLocation() {
    LatLng center = mMap.getCameraPosition().target;
    double lat = center.latitude;
    double lng = center.longitude;
    String strLat = String.valueOf(lat).substring(0, 7);
    String strLng = String.valueOf(lng).substring(0, 7);
    etAddWindowLat.setText(strLat);
    etAddWindowLng.setText(strLng);
  }

  @Override
  public void refreshMapZoom(int index) {
    mMap.moveCamera(CameraUpdateFactory.zoomBy(index));
  }

  @Override
  public void showToast(String msg) {
    mToastControl.showToast(msg);
  }

  @Override
  public void onBack() {
    if (addSensorWindow.getVisibility() == View.VISIBLE || infoWindow.getVisibility() == View.VISIBLE) {
      if (addSensorWindow.getVisibility() == View.VISIBLE) hideAddSensorWindow();
      if (infoWindow.getVisibility() == View.VISIBLE) hideInfoWindow();
    } else {
      HomeActivity homeActivity = (HomeActivity) getActivity();
      homeActivity.setOnKeyBackPressedListener(null);
      homeActivity.onBackPressed();
    }
  }

  @Override
  public void refreshZoomButton(int index) {
    if (index > 18) {
      btnZoomIn.setClickable(false);
      btnZoomIn.setBackgroundResource(R.color.colorPrimaryDark);
    } else if (index < 19 && index > 0) {
      btnZoomIn.setClickable(true);
      btnZoomOut.setClickable(true);
      btnZoomIn.setBackgroundResource(R.drawable.selector_base);
      btnZoomOut.setBackgroundResource(R.drawable.selector_base);
    }
    if (index < 1) {
      btnZoomOut.setClickable(false);
      btnZoomOut.setBackgroundResource(R.color.colorPrimaryDark);
    }
  }

  private Animation getShowAnimation(int duration){
    Animation animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, -1.0f,
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

  @Override
  public void refreshMapLocation(double lat, double lng, int index) {
    LatLng latLng = new LatLng(lat, lng);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, index));
  }

  @Override
  public void startProgress() {
    mProgressControl.startProgress();
  }

  @Override
  public void stopProgress() {
    mProgressControl.stopProgress();
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
