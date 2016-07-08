package com.lystr.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lystr.MainActivity;
import com.lystr.R;
import com.lystr.SingletonApplication;
import com.lystr.SingletonAwareActivity;

public class DrawerHelper {

  public DrawerLayout drawerLayout;

  public ActionBarDrawerToggle drawerToggle;

  public NavigationView drawer;

  public boolean init( final SingletonAwareActivity thiz ) {
    drawerLayout = (DrawerLayout) thiz.findViewById( R.id.drawer_layout );
    if( null == drawerLayout ) return false;

    drawerToggle = new ActionBarDrawerToggle( thiz, drawerLayout, R.string.app_name, R.string.app_name ) {
      @Override
      public void onDrawerOpened( View drawerView ) {
        super.onDrawerOpened( drawerView );
        thiz.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING );
        Utils.hideKeyboard( thiz );
      }

      @Override
      public void onDrawerClosed( View drawerView ) {
        Utils.hideKeyboard( thiz );
        thiz.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );
        super.onDrawerClosed( drawerView );
      }
    };
    drawerToggle.setDrawerIndicatorEnabled( false );
    drawerLayout.addDrawerListener( drawerToggle );

    ((NavigationView) thiz.findViewById( R.id.left_drawer )).setItemIconTintList( null );

    drawer = (NavigationView) thiz.findViewById( R.id.left_drawer );
    drawer.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected( MenuItem menuItem ) {
        drawerLayout.closeDrawer( GravityCompat.START );
        Intent i;
        switch( menuItem.getItemId() ){
          default:
            i = new Intent( thiz, MainActivity.class );
        }
        i.addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );
        thiz.startActivity( i );
        return false;
      }
    } );

    refreshCounter( thiz.getApp() );

    initHeader( thiz );

    return true;
  }

  public void refreshCounter( SingletonApplication app ) {
//    SharedPreferences p = app.prefs;
//    int count = Math.min( 99, p.getInt( "newSocialSinceLastSeen", 0 ) + p.getInt( "newChatsSinceLastSeen", 0 ) );
//    TextView view = (TextView) drawer.getMenu().findItem( R.id.messages_item ).getActionView().findViewById( R.id.counterView );
//    if( 0 == count )
//      view.setVisibility( View.GONE );
//    else
//      view.setText( "" + count );
  }

  public void initHeader( final SingletonAwareActivity activity ) {
    SingletonApplication app = activity.getApp();
    if( !app.isAuthenticated() ) return;

    showAvatar( app );
    View headerView = drawer.getHeaderView( 0 );
    SharedPreferences p = app.prefs;
    ( (TextView) headerView.findViewById( R.id.infoUserView ) ).setText( p.getString( "userName", "" ) );
    ( (TextView) headerView.findViewById( R.id.emailView ) ).setText( p.getString( "email", "" ) );
    headerView.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        drawerLayout.closeDrawer( GravityCompat.START );
//        Intent i = new Intent( activity, ProfileActivity.class );
//        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
//        activity.startActivity( i );
      }
      } );
  }

  @NonNull
  public void showAvatar( SingletonApplication app ) {
    View headerView = drawer.getHeaderView( 0 );
    SharedPreferences p = app.prefs;
    String profileImgUrl = p.getString( "profileImgUrl", null );
    app.loadAvatar( profileImgUrl, headerView.findViewById( R.id.avatarView ) );
  }

  public void syncState() {
    if( null != drawerToggle ) drawerToggle.syncState();
  }

  public void close() {
    drawerLayout.closeDrawer( GravityCompat.START );
  }

}