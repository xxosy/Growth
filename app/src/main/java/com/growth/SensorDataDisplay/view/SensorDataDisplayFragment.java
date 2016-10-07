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
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapter;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapterDataView;
import com.growth.SensorDataDisplay.dagger.DaggerSensorDataDisplayComponent;
import com.growth.SensorDataDisplay.dagger.SensorDataDisplayModule;
import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenter;
import com.growth.SensorValueGuide;
import com.growth.domain.Value;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.view.HomeActivity;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Inject
    SensorDataDisplayPresenter presenter;
    // TODO: Rename and change types of parameters
    private String serial;
    private String mParam2;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorDataDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorDataDisplayFragment newInstance(String param1, String param2) {
        SensorDataDisplayFragment fragment = new SensorDataDisplayFragment();
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
        ((HomeActivity)getActivity()).setOnKeyBackPressedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HarmfulListAdapter adapter = new HarmfulListAdapter(getActivity());
        DaggerSensorDataDisplayComponent.builder()
                .sensorDataDisplayModule(new SensorDataDisplayModule(this,adapter))
                .build()
                .inject(this);
        if(root != null){
            ViewGroup parent = (ViewGroup) root.getParent();
            if(parent != null)
                parent.removeView(root);
        }
        try {
            root = inflater.inflate(R.layout.fragment_sensor_data_display, container, false);
        } catch (InflateException e){
            Log.i("error",e.toString());
        }
        unbinder = ButterKnife.bind(this,root);
        recyclerHarmfulList.setAdapter(adapter);
        recyclerHarmfulList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHarmfulListAdapterDataView.setOnRecyclerItemClickListener((adapter1,position)->presenter.OnRecyclerItemClick(position));
        frameImagHarmfulDetail.setOnClickListener(v -> {
            if(frameImagHarmfulDetail.getLayoutParams().height!= harmfulDetail.getHeight()){
                harmfulImageSize=frameImagHarmfulDetail.getLayoutParams().height;
                frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,harmfulDetail.getHeight()));
            }else if(frameImagHarmfulDetail.getLayoutParams().height== harmfulDetail.getHeight()){
                frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,harmfulImageSize));
            }
        });
        mProgressControl = new ProgressControlImlp(progressLayout, progressView);
        mToastControl = new ToastControlImlp(getActivity());
        presenter.enterFragment(serial);
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
    public void refreshSwipe(){
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
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void refreshData(Value value) {
        txtTemp.setText(value.getTemperature().substring(0,4)+"°C");
        pbTemp.setProgress(Float.parseFloat(value.getTemperature()));
        txtHumidity.setText(value.getHumidity().substring(0,4)+"%");
        pbHumidity.setProgress(Float.parseFloat(value.getHumidity()));
        txtLight.setText(value.getLight()+"lux");
        pbLight.setProgress(Float.parseFloat(value.getLight()));
        txtEc.setText(String.valueOf(Float.parseFloat(value.getEc())/15).substring(0,4)+"\nus/cm");
        pbEc.setProgress(Float.parseFloat(value.getEc())/15);
        txtPh.setText(String.valueOf(Float.parseFloat(value.getPh())).substring(0,4));
        pbPh.setProgress(Float.parseFloat(value.getPh()));
        txtCo2.setText(value.getCo2()+"ppm");
        pbCo2.setProgress(Float.parseFloat(value.getCo2()));
    }
    @Override
    public void refreshState(HashMap<String,Boolean> states) {
        Set<String> tags = states.keySet();
        for(String tag:tags) {
            if (states.get(tag)) {
                if(tag.equals("temp")) stateTemp.setImageResource(R.drawable.ic_smile);
                if(tag.equals("humidity")) stateHumidity.setImageResource(R.drawable.ic_smile);
                if(tag.equals("light")) stateLight.setImageResource(R.drawable.ic_smile);
                if(tag.equals("co2")) stateCo2.setImageResource(R.drawable.ic_smile);
                if(tag.equals("ec")) stateEc.setImageResource(R.drawable.ic_smile);
                if(tag.equals("ph")) statePh.setImageResource(R.drawable.ic_smile);
            } else {
                if(tag.equals("temp")) stateTemp.setImageResource(R.drawable.ic_cry);
                if(tag.equals("humidity")) stateHumidity.setImageResource(R.drawable.ic_cry);
                if(tag.equals("light")) stateLight.setImageResource(R.drawable.ic_cry);
                if(tag.equals("co2")) stateCo2.setImageResource(R.drawable.ic_cry);
                if(tag.equals("ec")) stateEc.setImageResource(R.drawable.ic_cry);
                if(tag.equals("ph")) statePh.setImageResource(R.drawable.ic_cry);
            }
        }
    }

    @Override
    public void refreshCameraImage(Bitmap image) {
        image = Bitmap.createScaledBitmap(image,frameCamera.getWidth(),frameCamera.getHeight(),true);
        imgCamera.setImageBitmap(image);
    }

    @Override
    public void refreshStateView(Value value) {
        if(Float.valueOf(value.getLight()) < SensorValueGuide.GUIDE_LIGHT_MIN) {
            if(Float.valueOf(value.getTemperature()) < SensorValueGuide.GUIDE_TEMP_MIN)
                imgStateView.setImageResource(R.drawable.illust_night_cold);
            else if(Float.valueOf(value.getTemperature()) > SensorValueGuide.GUIDE_TEMP_MIN)
                imgStateView.setImageResource(R.drawable.illust_night_hot);
        }
        else if(Float.valueOf(value.getLight()) > SensorValueGuide.GUIDE_LIGHT_MIN)
            if(Float.valueOf(value.getTemperature()) < SensorValueGuide.GUIDE_TEMP_MIN)
                imgStateView.setImageResource(R.drawable.illust_day_cold);
            else if(Float.valueOf(value.getTemperature()) > SensorValueGuide.GUIDE_TEMP_MIN)
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
        if(state == 0) btnChange.setImageResource(R.drawable.ic_view);
        else if(state==1)btnChange.setImageResource(R.drawable.ic_camera);
        else if(state==2)btnChange.setImageResource(R.drawable.ic_mosquito);
    }
    @Override
    public void showButton(){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)btnCamera.getLayoutParams();
        ViewGroup.MarginLayoutParams btnChangeLp = (ViewGroup.MarginLayoutParams)btnChange.getLayoutParams();
        Animation cameraAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, lp.bottomMargin-btnChangeLp.bottomMargin,
                Animation.RELATIVE_TO_SELF, 0f);
        lp = (ViewGroup.MarginLayoutParams)btnView.getLayoutParams();
        Animation viewAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, lp.bottomMargin-btnChangeLp.bottomMargin,
                Animation.RELATIVE_TO_SELF, 0f);
        lp = (ViewGroup.MarginLayoutParams)btnMosquito.getLayoutParams();
        Animation mosquitoAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, lp.bottomMargin-btnChangeLp.bottomMargin,
                Animation.RELATIVE_TO_SELF, 0f);
        cameraAnimation.setDuration(300);
        viewAnimation.setDuration(300);
        mosquitoAnimation.setDuration(300);

        btnCamera.setAnimation(cameraAnimation);
        btnCamera.setVisibility(View.VISIBLE);
        btnView.setAnimation(viewAnimation);
        btnView.setVisibility(View.VISIBLE);
        btnMosquito.setAnimation(mosquitoAnimation);
        btnMosquito.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideButton(){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)btnCamera.getLayoutParams();
        ViewGroup.MarginLayoutParams btnChangeLp = (ViewGroup.MarginLayoutParams)btnChange.getLayoutParams();
        Animation cameraAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, lp.bottomMargin-btnChangeLp.bottomMargin);
        lp = (ViewGroup.MarginLayoutParams)btnView.getLayoutParams();
        Animation viewAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, lp.bottomMargin-btnChangeLp.bottomMargin);
        lp = (ViewGroup.MarginLayoutParams)btnMosquito.getLayoutParams();
        Animation mosquitoAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE,lp.bottomMargin-btnChangeLp.bottomMargin);
        cameraAnimation.setDuration(300);
        viewAnimation.setDuration(300);
        mosquitoAnimation.setDuration(300);
        btnCamera.setAnimation(cameraAnimation);
        btnCamera.setVisibility(View.GONE);
        btnView.setAnimation(viewAnimation);
        btnView.setVisibility(View.GONE);
        btnMosquito.setAnimation(mosquitoAnimation);
        btnMosquito.setVisibility(View.GONE);
    }

    @Override
    public void showHarmfulList() {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(300);
        if(harmfulList.getVisibility()==View.GONE) {
            harmfulList.setAnimation(animation);
            harmfulList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideHarmfulList() {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        animation.setDuration(300);
        if(harmfulList.getVisibility()==View.VISIBLE) {
            harmfulList.setAnimation(animation);
            harmfulList.setVisibility(View.GONE);
        }
    }

    @Override
    public void showHarmfulDetail(String title, String description,String url) {
        Animation animation = new AlphaAnimation(0f,1f);
        animation.setDuration(300);
        tvHarmfulTitleDetail.setText(title);
        tvHarmfulDescriptionDetail.setText(description);
        Glide.with(getActivity()).load(url).into(imgHarmfulDetail);
        if(harmfulDetail.getVisibility()==View.GONE) {
            harmfulDetail.setAnimation(animation);
            harmfulDetail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideHarmfulDetail() {
        Animation animation = new AlphaAnimation(1f,0f);
        animation.setDuration(300);
        if(harmfulDetail.getVisibility()==View.VISIBLE) {
            harmfulDetail.setAnimation(animation);
            harmfulDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshHarmfulList() {
        mHarmfulListAdapterDataView.refresh();
        recyclerHarmfulList.scrollToPosition(0);
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
    public void startProgress(){
        mProgressControl.startProgress();
    }
    public void stopProgress(){
        mProgressControl.stopProgress();
    }

    @Override
    public void refreshWhether(String whether, String externTemp, String externHumidity,Bitmap icon) {
        tvWhether.setText(whether);
        tvExternalTemp.setText(externTemp+"°C");
        tvExternalHumidity.setText(externHumidity+"%");
        ivWhether.setImageBitmap(icon);
    }

    @Override
    public void showToast(String msg) {
        mToastControl.showToast(msg);
    }

    @Override
    public void onBack() {
        if(frameImagHarmfulDetail.getLayoutParams().height== harmfulDetail.getHeight()){
            frameImagHarmfulDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,harmfulImageSize));
        }else if(harmfulDetail.getVisibility()==View.VISIBLE ) {
            hideHarmfulDetail();
        }else {
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
