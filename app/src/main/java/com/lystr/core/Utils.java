package com.lystr.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.commons.android.BaseUtils;
import com.commons.android.TrayAttr;
import com.lystr.Constants;

import java.text.DecimalFormat;

@SuppressLint("DefaultLocale")
public class Utils extends BaseUtils {

  static{
    NOTIFICATION_LIGHTS = 0xFFFFCC00;
  }
  
  static DecimalFormat etaDF = new DecimalFormat( "00" );
  
  public static void showNotification( Context ctx, Intent i, TrayAttr trayAttr ) {
    showNotification( ctx, i, trayAttr, 233, "", Constants.VIBRATE_PATTERN );
  }

  public static Drawable DEFAULT_AVATAR;  
  
//  public static void setAvatar( Resources res, View v, String avatar, boolean... notRound ) {
//    ImageView imageView = v instanceof ImageView ? (ImageView)v : (ImageView)v.findViewById( R.id.avatarImage );
//    Drawable d;
//
//    int color = res.getColor( android.R.color.white );
//    float border = res.getDimension( R.dimen.avatar_border_width );
//
//    if( null != avatar && !"null".equals( avatar ) && 0 < avatar.length() ){
//      if( 0 == notRound.length || !notRound[ 0 ] )
//        d = new RoundedAvatarDrawable( base64toBitmap( avatar ), color, border );
//      else
//        d = new BitmapDrawable( res, base64toBitmap( avatar ) );
//    }else if( null == imageView.getDrawable() ){
//      if( null == DEFAULT_AVATAR )
//        DEFAULT_AVATAR = new RoundedAvatarDrawable( BitmapFactory.decodeResource( res, R.drawable.com_facebook_profile_default_icon ), color, border );
//      d = DEFAULT_AVATAR;
//    }else
//      return;
//
//    imageView.setImageDrawable( d );
//  }

  public static Bitmap replaceColor( Bitmap bm, int oldColor, int newColor, boolean b2w ) {
    float[] hsv = { 0f, 0f, 0f };
    Color.colorToHSV( oldColor, hsv );
    float oldHue = hsv[ 0 ];
    Color.colorToHSV( newColor, hsv );
    float newHue = hsv[ 0 ];
    
    int width = bm.getWidth(), height = bm.getHeight();
    Bitmap dst = Bitmap.createBitmap( width, height, Config.ARGB_8888 );
    for( int y = 0; y < height; y++ ){
      for( int x = 0; x < width; x++ ){
        int color = bm.getPixel( x, y );
        Color.colorToHSV( color, hsv );
        float hueDelta = oldHue - hsv[ 0 ];
        if( Math.abs( hueDelta ) < 5 ){
          hsv[ 0 ] = newHue - hueDelta;
          color = Color.HSVToColor( hsv );
        }
        dst.setPixel( x, y, color );
      }
    }
    return dst;
  }
}