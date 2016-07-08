package com.lystr;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.util.LruCache;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.commons.android.Logg;
import com.commons.android.SingletonApplicationBase;
import com.commons.android.vorm.ORMSupport;
import com.lystr.core.DatabaseHelper;
import com.lystr.core.Utils;
import com.lystr.domain.Item;
import com.lystr.domain.ItemInfo;
import com.lystr.domain.List;

/**
 * Created by Injecteer on 23.03.2016.
 */
public class SingletonApplication extends SingletonApplicationBase {

  public SharedPreferences prefs;

  public RequestQueue requestQueue;

  public ImageLoader imageLoader;

  public Cache cache;

  public boolean isInBackground = false;

  public long lastForegroundTransition = System.currentTimeMillis();

  @Override
  public void onCreate() {
    super.onCreate();
    Logg.DEBUG = BuildConfig.DEBUG;

    try{ Class.forName( "android.os.AsyncTask" ); }catch( ClassNotFoundException ignored ){}
    prefs = PreferenceManager.getDefaultSharedPreferences( this );

    ORMSupport.fillMappings( R.id.class, List.class, ItemInfo.class, Item.class );
    openHelper = new DatabaseHelper( this );
    ORMSupport.init( this );

    cache = new DiskBasedCache( getCacheDir(), 1 << 23 );
    Network network = new BasicNetwork( new HurlStack() );
    requestQueue = new RequestQueue( cache, network );
    requestQueue.start();

    imageLoader = new ImageLoader( requestQueue, new ImageLoader.ImageCache() {
      private final LruCache<String, Bitmap> bmCache = new LruCache<>( 80 ), iconCache = new LruCache<>( 80 );
      @Override public void putBitmap( String url, Bitmap bitmap ) {
        int size = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? bitmapSize( bitmap ) : bitmapSizeOld( bitmap );
        if( ( 1 << 16 ) > size ) iconCache.put( url, bitmap );
        else if( ( 1 << 20 ) > size ) bmCache.put( url, bitmap );
//        Logg.i( "CacheLoader", "size = " + ( size >> 10 ) + " kB " + bitmap.getWidth() + ":" + bitmap.getHeight() + " / lru = " + bmCache.size() + ", ic = " + iconCache.size() );
      }

      @TargetApi( Build.VERSION_CODES.KITKAT )
      private int bitmapSize( Bitmap bm ){
        return bm.getAllocationByteCount();
      }

      @TargetApi( Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 )
      private int bitmapSizeOld( Bitmap bm ){
        return bm.getByteCount();
      }

      @Override public Bitmap getBitmap( String url ) {
        Bitmap bm = bmCache.get( url );
        if( null == bm ) bm = iconCache.get( url );
        return bm;
      }
    } );
  }

  public void loadImage( String url, View target ) {
    loadImage( url, target, R.drawable.ic_menu_camera );
  }

  public void loadAvatar( String url, View target ) {
    loadImage( url, target, R.drawable.ic_menu_camera );
  }

  public void loadImage( String url, View target, @DrawableRes int errorImageId ) {
    if( null == target || !( target instanceof NetworkImageView ) ) return;
    NetworkImageView t = (NetworkImageView)target;
    t.setDefaultImageResId( errorImageId );
    t.setErrorImageResId( errorImageId );

    if( Utils.isEmpty( url ) )
      t.setImageDrawable( getResources().getDrawable( errorImageId ) );
    else
      t.setImageUrl( url, imageLoader );
  }

  public boolean isAuthenticated() {
    return !Utils.isEmpty( prefs.getString( "token", null ) );
  }

}
