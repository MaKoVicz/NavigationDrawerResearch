package com.example.navigationdrawerresearch.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.navigationdrawerresearch.R;
import com.example.navigationdrawerresearch.fragment.ContactFragment;
import com.example.navigationdrawerresearch.fragment.HomeFragment;
import com.example.navigationdrawerresearch.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private DrawerLayout mDrawer;
  private NavigationView nvView;
  private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawerToggle = setupDrawerToggle();
    mDrawer.addDrawerListener(drawerToggle);

    nvView = (NavigationView) findViewById(R.id.nvView);
    setupDrawerContent(nvView);

    setNavDownloadSwitchItemClick();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (drawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawer.openDrawer(GravityCompat.START);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setupDrawerContent(NavigationView navigationView) {
    //Go to HomeFragment at first launch
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.flContent, new HomeFragment())
        .commit();
    navigationView.getMenu().getItem(0).setChecked(true);
    setTitle(navigationView.getMenu().getItem(0).getTitle());

    //Set ClickListener for Fragment item in Navigation Drawer
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectDrawerItem(item);
        return true;
      }
    });
  }

  private void selectDrawerItem(MenuItem menuItem) {
    Fragment navFragmentItem = null;
    Class fragmentClass;

    switch (menuItem.getItemId()) {
      case R.id.nav_home:
        fragmentClass = HomeFragment.class;
        break;
      case R.id.nav_contact:
        fragmentClass = ContactFragment.class;
        break;
      case R.id.nav_setting:
        fragmentClass = SettingFragment.class;
        break;
      default:
        fragmentClass = HomeFragment.class;
    }

    try {
      navFragmentItem = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }

    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction().replace(R.id.flContent, navFragmentItem).commit();

    menuItem.setChecked(true);
    setTitle(menuItem.getTitle());
    mDrawer.closeDrawers();
  }

  private ActionBarDrawerToggle setupDrawerToggle() {
    return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
  }

  private void setNavDownloadSwitchItemClick() {
    Menu menu = nvView.getMenu();
    MenuItem menuItem = menu.findItem(R.id.nav_switch);
    View actionView = MenuItemCompat.getActionView(menuItem);
    final SwitchCompat mDownloadSwitch = (SwitchCompat) actionView.findViewById(R.id.download_switch);

    menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem menuItem) {
        if (mDownloadSwitch.isChecked()) {
          mDownloadSwitch.setChecked(false);
        } else {
          mDownloadSwitch.setChecked(true);
        }
        mDrawer.closeDrawers();
        return true;
      }
    });
  }
}