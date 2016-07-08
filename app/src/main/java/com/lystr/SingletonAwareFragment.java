package com.lystr;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.volley.Response;
import com.lystr.core.NpaLinearLayoutManager;

import org.json.JSONObject;

public class SingletonAwareFragment extends DialogFragment {
  
  protected SwipeRefreshLayout swipeRefreshLayout;

  public View v;

  public SingletonApplication getApp() {
    return (SingletonApplication)getActivity().getApplication();
  }

  public void refresh(){}

  public boolean onSave( SingletonAwareActivity activity, JSONObject json ){
    return false;
  }

  public boolean onDelete( SingletonAwareActivity activity ){
    return false;
  }

  public void onReset( SingletonAwareActivity activity ){
  }

  public void showLoading( int... timeout ) {
    FragmentActivity a = getActivity();
    if( null != a ) ((SingletonAwareActivity)a).showLoading( timeout );
  }

  public void hideLoading() {
    FragmentActivity a = getActivity();
    if( null != a ) ((SingletonAwareActivity)a).hideLoading();
  }

  @NonNull
  protected LinearLayoutManager newLayoutManager() {
    return new NpaLinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false );
  }

  @NonNull
  protected Response.Listener<JSONObject> getOnResultListener( final SingletonAwareActivity activity ) {
    return new Response.Listener<JSONObject>() {
        @Override
        public void onResponse( JSONObject response ) {
          hideLoading();
          Intent i = new Intent();
          i.putExtra( "response", response.toString() );
          activity.setResult( Activity.RESULT_OK, i );
          activity.finish();
        }
      };
  }
}