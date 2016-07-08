package com.lystr.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.commons.android.Logg;
import com.commons.android.vorm.DomainClass;
import com.commons.android.vorm.ORMSupport;

public class DatabaseHelper extends SQLiteOpenHelper {

  public DatabaseHelper( Context ctx ) {
    super( ctx, "lystr.db", null, 1 );
  }

  @Override
  public void onCreate( SQLiteDatabase db ) {
    Logg.i( this, "onCreated entered" );
    for( Class<? extends DomainClass> dc : ORMSupport.VIEW_MAP.keySet() ){
      Logg.i( this, "class " + dc.getSimpleName() );
      try{
        String createSql = dc.newInstance().getCreateSql();
        if( null != createSql ) db.execSQL( createSql );
      }catch( Exception e ){
        Logg.e( this, "", e );
      }
    }
  }

  @Override
  public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
  }
}
