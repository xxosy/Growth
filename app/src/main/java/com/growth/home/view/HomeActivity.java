package com.growth.home.view;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.growth.R;
import com.growth.SensorDataDisplay.view.SensorDataDisplayFragment;
import com.growth.actuator.view.ActuatorFragment;
import com.growth.gallery.view.PlantsGrowthGalleryFragment;
import com.growth.graph.view.GraphFragment;
import com.growth.home.OnKeyBackPressedListener;
import com.growth.home.PageChangeUtil;
import com.growth.home.dagger.DaggerHomeComponent;
import com.growth.home.dagger.HomeModule;
import com.growth.home.presenter.HomePresenter;
import com.growth.map.view.SensorMapFragment;
import com.growth.monitor.view.MonitorFragment;
import com.growth.rule.view.RuleFragment;
import com.growth.standard.view.StandardInformationFragment;
import com.growth.views.PageChange;

import javax.inject.Inject;


public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    HomePresenter.View,
    PageChange,
    SensorMapFragment.OnFragmentInteractionListener,
    GraphFragment.OnFragmentInteractionListener,
    SensorDataDisplayFragment.OnFragmentInteractionListener,
    ActuatorFragment.OnFragmentInteractionListener,
    PlantsGrowthGalleryFragment.OnFragmentInteractionListener,
    RuleFragment.OnFragmentInteractionListener,
    StandardInformationFragment.OnFragmentInteractionListener,
    MonitorFragment.OnFragmentInteractionListener{

  private final int container = R.id.container;
  private long backKeyPressedTime = 0;
  private Toast toast;
  @Inject
  HomePresenter homePresenter;

  private OnKeyBackPressedListener onKeyBackPressedListener;

  private FragmentTransaction mFragmentTransaction;

  private boolean backState = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setContentView(R.layout.activity_home);
    DaggerHomeComponent.builder()
        .homeModule(new HomeModule(this))
        .build()
        .inject(this);
    setCustomActionbar();
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    PageChangeUtil.newInstance().setPageChange(this);
    mFragmentTransaction = getSupportFragmentManager().beginTransaction();
    //TODO: Use this for Test
//    mFragmentTransaction.replace(container, StandardInformationFragment.newInstance("", ""));
    mFragmentTransaction.replace(container, SensorMapFragment.newInstance());
    mFragmentTransaction.commit();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  private void setCustomActionbar() {
    ActionBar actionBar = getSupportActionBar();

    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowTitleEnabled(false);

    View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_main, null);
    actionBar.setCustomView(mCustomView);

    Toolbar parent = (Toolbar) mCustomView.getParent();
    parent.setContentInsetsAbsolute(0, 0);

    actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));

    ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT);
    actionBar.setCustomView(mCustomView, params);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, parent, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (onKeyBackPressedListener != null) onKeyBackPressedListener.onBack();
    else if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
    else if (backState) super.onBackPressed();
    else {
      if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
        backKeyPressedTime = System.currentTimeMillis();
        showGuide();
        return;
      }
      if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
        this.finish();
        toast.cancel();
      }
      super.onBackPressed();
    }
  }
  public void showGuide() {
    toast = Toast.makeText(this,
        R.string.toast_message_back_press, Toast.LENGTH_SHORT);
    toast.show();
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_map) {
      pageChange4NotStack(new SensorMapFragment().newInstance());
    } else if (id == R.id.nav_setting) {

    } else if (id == R.id.nav_actuator) {
      pageChange4NotStack(new ActuatorFragment().newInstance("", ""));
    } else if (id == R.id.nav_plants_gallery) {
      pageChange4NotStack(new PlantsGrowthGalleryFragment().newInstance("", ""));
    } else if(id == R.id.nav_rule){
      pageChange4NotStack(new RuleFragment().newInstance("",""));
    }else if(id == R.id.nav_standard){
      pageChange4NotStack(new StandardInformationFragment().newInstance("",""));
    }else if(id == R.id.nav_monitor){
      pageChange4NotStack(new MonitorFragment().newInstance("",""));
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  public void setOnKeyBackPressedListener(OnKeyBackPressedListener onKeyBackPressedListener) {
    this.onKeyBackPressedListener = onKeyBackPressedListener;
  }

  public void setBackState(boolean state){
    backState = state;
  }

  @Override
  public void pageChange(Fragment fragment) {
    mFragmentTransaction = getSupportFragmentManager().beginTransaction();
    mFragmentTransaction.replace(container, fragment);
    mFragmentTransaction.addToBackStack(null);
    mFragmentTransaction.commit();
  }

  public void pageChange4NotStack(Fragment fragment) {
    FragmentManager fm = getSupportFragmentManager();
    mFragmentTransaction = fm.beginTransaction();
    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    mFragmentTransaction.add(container, fragment);
    mFragmentTransaction.commit();
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }
}

