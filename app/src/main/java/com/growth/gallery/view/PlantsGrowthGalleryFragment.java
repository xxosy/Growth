package com.growth.gallery.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.growth.R;
import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapter;
import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapterView;
import com.growth.gallery.dagger.DaggerPlantsGrowthGalleryComponent;
import com.growth.gallery.dagger.PlantsGrowthGalleryModule;
import com.growth.gallery.presenter.PlantsGrowthGalleryPresenter;
import com.growth.gallerypictures.view.PlantsGrowthGalleryPicturesActivity;
import com.growth.home.view.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlantsGrowthGalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlantsGrowthGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantsGrowthGalleryFragment extends Fragment implements PlantsGrowthGalleryPresenter.View {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;
  View root;
  Unbinder unbinder;
  @BindView(R.id.recycler_plants_growth_gallery_list)
  RecyclerView recyclerPlantsGrowthGalleryList;

  //PlantsGrowthGallerySensorListAdapter
  PlantsGrowthGallerySensorListAdapter mPlantsGrowthGallerySensorListAdapter;
  @Inject
  PlantsGrowthGallerySensorListAdapterView mPlantsGrowthGallerySensorListAdapterView;

  @Inject
  PlantsGrowthGalleryPresenter presenter;

  public PlantsGrowthGalleryFragment() {
    // Required empty public constructor
  }

  public static PlantsGrowthGalleryFragment newInstance(String param1, String param2) {
    PlantsGrowthGalleryFragment fragment = new PlantsGrowthGalleryFragment();
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
    ((HomeActivity) getActivity()).setOnKeyBackPressedListener(null);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mPlantsGrowthGallerySensorListAdapter = new PlantsGrowthGallerySensorListAdapter(getActivity());
    DaggerPlantsGrowthGalleryComponent.builder()
        .plantsGrowthGalleryModule(new PlantsGrowthGalleryModule(this, mPlantsGrowthGallerySensorListAdapter))
        .build()
        .inject(this);
    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_plants_growth_gallery, container, false);
    } catch (InflateException e) {

    }
    unbinder = ButterKnife.bind(this, root);
    recyclerPlantsGrowthGalleryList.setAdapter(mPlantsGrowthGallerySensorListAdapter);
    recyclerPlantsGrowthGalleryList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mPlantsGrowthGallerySensorListAdapterView.setOnRecyclerItemClickListener((adapter1, position) -> presenter.OnRecyclerPlantsGrowthGalleryItemClick(position));
    presenter.OnCreatedView();
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
  public void refreshRecyclerPlantsGrowthGalleryList() {
    mPlantsGrowthGallerySensorListAdapterView.refresh();
  }

  @Override
  public void startActivityPlantGallery(String serial) {
    Intent intent = new Intent(getActivity(), PlantsGrowthGalleryPicturesActivity.class);
    intent.putExtra("serial", serial);
    startActivity(intent);
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
