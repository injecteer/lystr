package com.lystr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lystr.core.DrawerHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class SingletonAwareActivity extends AppCompatActivity {

  public DrawerHelper drawerHelper = new DrawerHelper();

  protected ProgressDialog progressDialog;

  @Nullable @Bind( R.id.toolbar ) protected Toolbar toolbar;

  protected boolean wasInBackground = false;

  @Override
  public void setContentView( int layoutResID ) {
    try{
      super.setContentView( layoutResID );
      ButterKnife.bind( this );
    }catch( OutOfMemoryError oom ){
      oom.printStackTrace();
    }

    if( null != toolbar ){
      setSupportActionBar( toolbar );
      getSupportActionBar().setDisplayHomeAsUpEnabled( true );
      getSupportActionBar().setHomeButtonEnabled( true );
      toolbar.setNavigationOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View v ) {
          onBackPressed();
        }
      } );
    }

    progressDialog = new ProgressDialog( this );
    progressDialog.setMessage( getResources().getString( R.string.loading ) );
    progressDialog.setCancelable( false );
    progressDialog.setIndeterminate( true );

    drawerHelper.init( this );
  }

  @Override
  protected void onStart() {
    super.onStart();
    SingletonApplication a = getApp();
    
    wasInBackground = a.isInBackground;
    a.isInBackground = false;
    a.lastForegroundTransition = System.currentTimeMillis();
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    SingletonApplication a = getApp();
    if( 1500 < System.currentTimeMillis() - a.lastForegroundTransition ){
      a.isInBackground = true;
    }
  }
  
  @Override
  protected void onNewIntent( Intent intent ) {
    super.onNewIntent( intent );
    setIntent( intent );
  }

  public SingletonApplication getApp() {
    return (SingletonApplication)getApplication();
  }

  public void backToMain( View v ) {
  }

  @Override
  public void onBackPressed() {
    if( null != drawerHelper.drawerLayout && drawerHelper.drawerLayout.isDrawerOpen( GravityCompat.START ) ) drawerHelper.close();
    else super.onBackPressed();
  }
  
  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    switch( item.getItemId() ){
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return false;
  }

  @Override
  protected void onPostCreate( Bundle savedInstanceState ) {
    super.onPostCreate( savedInstanceState );
    drawerHelper.syncState();
  }

  public void showLoading( int... timeout ) {
    if( null == progressDialog || progressDialog.isShowing() ) return;
    progressDialog.show();
    if( 1 == timeout.length )
      new Handler(){
        @Override public void handleMessage( Message msg ) {
          if( progressDialog.isShowing() ) progressDialog.dismiss();
        }
      }.sendEmptyMessageDelayed( 0, timeout[ 0 ] );
  }

  public void hideLoading() {
    try{
      if( null != progressDialog && progressDialog.isShowing() ) progressDialog.dismiss();
    }catch( Exception ignored ){}
  }

}
