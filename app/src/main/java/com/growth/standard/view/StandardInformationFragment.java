package com.growth.standard.view;

import android.content.Context;
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
import com.growth.standard.adapter.StandardInformationAdapterView;
import com.growth.standard.adapter.StandardInformationRecyclerAdapter;
import com.growth.standard.dagger.DaggerStandardInformationComponent;
import com.growth.standard.dagger.StandardInformationModule;
import com.growth.standard.presenter.StandardInformationPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StandardInformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StandardInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardInformationFragment extends Fragment implements StandardInformationPresenter.View{
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  @Inject
  StandardInformationPresenter presenter;
  @Inject
  StandardInformationAdapterView mStandardInformationAdapterView;

  private Unbinder unbinder;
  private View root;

  @BindView(R.id.standard_information_recycler)
  RecyclerView recycler;

  public StandardInformationFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment StandardInformationFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static StandardInformationFragment newInstance(String param1, String param2) {
    StandardInformationFragment fragment = new StandardInformationFragment();
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
    StandardInformationRecyclerAdapter adapter = new StandardInformationRecyclerAdapter(getActivity());
    DaggerStandardInformationComponent.builder()
        .standardInformationModule(new StandardInformationModule(this,adapter))
        .build()
        .inject(this);
    if (root != null) {
      ViewGroup parent = (ViewGroup) root.getParent();
      if (parent != null)
        parent.removeView(root);
    }
    try {
      root = inflater.inflate(R.layout.fragment_standard_information, container, false);
    } catch (InflateException e) {

    }
    unbinder = ButterKnife.bind(this, root);
    recycler.setAdapter(adapter);
    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
//    presenter.unSubscribe();
  }

  @Override
  public void refreshRecyclerView() {
    mStandardInformationAdapterView.refresh();
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
