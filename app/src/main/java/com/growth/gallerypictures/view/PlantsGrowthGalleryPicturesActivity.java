package com.growth.gallerypictures.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.growth.R;
import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapter;
import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapterView;
import com.growth.gallerypictures.dagger.DaggerPlantsGrowthGalleryPicturesComponent;
import com.growth.gallerypictures.dagger.PlantsGrowthGalleryPicturesModule;
import com.growth.gallerypictures.presenter.PlantsGrowthGalleryPicturesPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlantsGrowthGalleryPicturesActivity extends AppCompatActivity
    implements PlantsGrowthGalleryPicturesPresenter.View {
  @Inject
  PlantsGrowthGalleryPicturesPresenter presenter;
  PlantsGrowthGalleryPicturesAdapter adapter;
  @Inject
  PlantsGrowthGalleryPicturesAdapterView mPlantsGrowthGalleryPicturesAdapterView;
  @BindView(R.id.recycler_plants_growth_gallery_pictures)
  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.activity_plants_growth_gallery_pictures);
    Intent intent = new Intent(this.getIntent());
    String serial = intent.getStringExtra("serial");
    adapter = new PlantsGrowthGalleryPicturesAdapter(this, serial);
    DaggerPlantsGrowthGalleryPicturesComponent.builder()
        .plantsGrowthGalleryPicturesModule(new PlantsGrowthGalleryPicturesModule(this, adapter))
        .build()
        .inject(this);
    setCustomActionbar();
    ButterKnife.bind(this);

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    mPlantsGrowthGalleryPicturesAdapterView.setOnRecyclerItemClickListener((adapter1, position) -> presenter.OnRecyclerPlantsGrowthGalleryItemClick(position));
    presenter.onCreated(serial);
  }

  private void setCustomActionbar() {
    ActionBar actionBar = getSupportActionBar();

    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowTitleEnabled(false);

    View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_gallery_pictures, null);
    actionBar.setCustomView(mCustomView);
    FrameLayout btnBack = (FrameLayout) mCustomView.findViewById(R.id.btn_plants_gallery_back);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    Toolbar parent = (Toolbar) mCustomView.getParent();
    parent.setContentInsetsAbsolute(0, 0);

    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));

    ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT);
    actionBar.setCustomView(mCustomView, params);

  }

  @Override
  public void refreshRecycler() {
    mPlantsGrowthGalleryPicturesAdapterView.refresh();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}
