package com.growth.map.view;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.growth.GpsInfo;
import com.growth.R;
import com.growth.domain.sensor.SensorItem;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.view.HomeActivity;
import com.growth.map.dagger.DaggerSensorMapComponent;
import com.growth.map.dagger.SensorMapModule;
import com.growth.map.presenter.SensorMapPresenter;
import com.growth.views.PageChange;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SensorMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SensorMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorMapFragment extends Fragment implements OnMapReadyCallback,
        SensorMapPresenter.View,
        OnKeyBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    ////zoom
    @BindView(R.id.btn_map_zoom_in)
    FrameLayout btnZoomIn;
    @BindView(R.id.btn_map_zoom_out)
    FrameLayout btnZoomOut;
    ////location
    @BindView(R.id.btn_map_location)
    FrameLayout btnLocation;

    GpsInfo gps;
    private static View root;
    //presenter
    @Inject
    SensorMapPresenter presenter;

    Marker mCurrentMaker;

    private OnFragmentInteractionListener mListener;

    public SensorMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorMapFragment newInstance(String param1, String param2) {
        SensorMapFragment fragment = new SensorMapFragment();
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
        ((HomeActivity)getActivity()).setOnKeyBackPressedListener(this);
    }


    @Override
    public void checkSerialSuccess() {
        btnAddWindowSerialCheck.setBackgroundColor(Color.GREEN);
        showToast("This serial is available");
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
    public void checkSerialFail() {
        btnAddWindowSerialCheck.setBackgroundColor(Color.RED);
        showToast("This serial is NOT available");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DaggerSensorMapComponent.builder()
                .sensorMapModule(new SensorMapModule(this))
                .build()
                .inject(this);
        if(root != null){
            ViewGroup parent = (ViewGroup) root.getParent();
            if(parent != null)
                parent.removeView(root);
        }
        try {
            root = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e){

        }
        gps = new GpsInfo(getActivity());
        unbinder = ButterKnife.bind(this,root);
        btnAddWindowGetLocation.setOnClickListener(v -> presenter.addWindowGetLocationClick());
        floatingActionButton.setOnClickListener(v -> presenter.floatingActionButtonClick());
        btnAddWindowCancel.setOnClickListener(v -> presenter.addWindowCancelClick());
        btnAddWindowSerialCheck.setOnClickListener(v -> {
            if(etAddWindowSerial.getText().toString().equals(""))
                showToast("Input your product serial.");
            else
                presenter.addWindowCheckSerialClick(etAddWindowSerial.getText().toString());

        });
        btnAddWindowOk.setOnClickListener(v -> {
            String lat = etAddWindowLat.getText().toString();
            String lng = etAddWindowLng.getText().toString();
            String serial = etAddWindowSerial.getText().toString();
            String title = etAddWindowTitle.getText().toString();
            presenter.addWindowOkClick(serial, title, lat, lng);
        });
        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnInfoWindowDetail.setOnClickListener(v -> {
            ((HomeActivity)getActivity()).setOnKeyBackPressedListener(null);
            presenter.infoWindowDetailClick();
        });
        btnInfoWindowGraph.setOnClickListener(v -> {
            ((HomeActivity)getActivity()).setOnKeyBackPressedListener(null);
            presenter.infoWindowGraphClick();
        });
        btnInfoWindowDelete.setOnClickListener(v -> {
            presenter.infoWindowDeleteSensorClick();
        });
        btnInfoWindowUpdate.setOnClickListener(v -> {
            presenter.infoWindowUpdateSensorClick();
        });
        btnZoomIn.setOnClickListener(v -> presenter.btnZoomInClick());
        btnZoomOut.setOnClickListener(v -> presenter.btnZoomOutClick());
        btnLocation.setOnClickListener(v -> presenter.btnLocationClick(gps));
        presenter.enterFragment();
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
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = googleMap.getUiSettings();
        mMap.setOnMarkerClickListener(marker -> {
            mCurrentMaker = marker;
            String title = marker.getTitle();
            presenter.markerClick(title);
            return true;
        });
        refreshMapZoom(11);
        presenter.btnLocationClick(gps);
//        presenter.enterFragment();
    }

    @Override
    public void addMarker(SensorItem sensorItem) {
        double lat = Double.parseDouble(sensorItem.getLat());
        double lng = Double.parseDouble(sensorItem.getLng());
        LatLng latLng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        //BitmapDescriptorFactory.fromResource(R.drawable.ic_sprout)
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_sprout",110,80)))
                .position(latLng)
                .title(sensorItem.getTitle()));
    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = null;
        imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    @Override
    public void refreshMarker() {

    }

    @Override
    public void showInfoWindow(String title, String serial, String humidity) {
        if(addSensorWindow.getVisibility()==View.VISIBLE){
            hideAddSensorWindow();
        }
        if(infoWindow.getVisibility()==View.GONE){
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            animation.setDuration(300);
            txtInfoWindowTitle.setText(title);
            txtInfoWindowSerial.setText(serial);
            txtInfoWindowHumidity.setText(humidity);

            infoWindow.setVisibility(View.VISIBLE);
            infoWindow.setAnimation(animation);
        }else{
            hideInfoWindow();
            showInfoWindow(title,serial,humidity);
        }
    }

    @Override
    public void hideInfoWindow() {
        if(infoWindow.getVisibility()==View.VISIBLE) {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            animation.setDuration(300);
            infoWindow.setAnimation(animation);
            infoWindow.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAddSensorWindow() {
        if(infoWindow.getVisibility()==View.VISIBLE){
            hideInfoWindow();
        }
        if(addSensorWindow.getVisibility()==View.GONE) {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            animation.setDuration(300);
            addSensorWindow.setVisibility(View.VISIBLE);
            addSensorWindow.setAnimation(animation);
        }
    }

    @Override
    public void hideAddSensorWindow() {
        if(addSensorWindow.getVisibility()==View.VISIBLE) {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            animation.setDuration(300);
            addSensorWindow.setAnimation(animation);
            addSensorWindow.setVisibility(View.GONE);
        }
    }

    @Override
    public void clearAddWindow() {
        etAddWindowSerial.setFocusableInTouchMode(true);
        etAddWindowLat.setText("");
        etAddWindowLng.setText("");
        etAddWindowSerial.setText("");
        etAddWindowTitle.setText("");
    }

    @Override
    public void fillEditLocation() {
        LatLng center = mMap.getCameraPosition().target;
        double lat = center.latitude;
        double lng = center.longitude;
        String strLat = String.valueOf(lat).substring(0,7);
        String strLng = String.valueOf(lng).substring(0,7);
        etAddWindowLat.setText(strLat);
        etAddWindowLng.setText(strLng);
    }
    @Override
    public void refreshMapZoom(int index) {
        mMap.moveCamera(CameraUpdateFactory.zoomBy(index));
    }
    @Override
    public void refreshInfoWindow() {
        mCurrentMaker.showInfoWindow();
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBack() {
        if(addSensorWindow.getVisibility()==View.VISIBLE ||infoWindow.getVisibility()==View.VISIBLE) {
            if (addSensorWindow.getVisibility() == View.VISIBLE) hideAddSensorWindow();
            if (infoWindow.getVisibility() == View.VISIBLE) hideInfoWindow();
        }else {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.setOnKeyBackPressedListener(null);
            homeActivity.onBackPressed();
        }
    }

    @Override
    public void refreshZoomButtom(int index) {
        if(index>18) {
            btnZoomIn.setClickable(false);
            btnZoomIn.setBackgroundResource(R.color.colorPrimaryDark);
        }else if(index<19 && index>0){
            btnZoomIn.setClickable(true);
            btnZoomOut.setClickable(true);
            btnZoomIn.setBackgroundResource(R.color.colorPrimary);
            btnZoomOut.setBackgroundResource(R.color.colorPrimary);
        }
        if(index<1) {
            btnZoomOut.setClickable(false);
            btnZoomOut.setBackgroundResource(R.color.colorPrimaryDark);
        }
    }

    @Override
    public void refreshMapLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat,lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
