package com.lystr.domain;

import android.view.View;
import android.widget.TextView;

import com.commons.android.vorm.DomainClass;
import com.lystr.R;

public class Item extends DomainClass {

  public Long itemInfoId;

  public Long listId;

  public float amount = 1;

  public String unit = "piece";

  @Override
  public void fillView( View v ) {
    super.fillView( v );
    try{
      int uid = R.string.class.getField( "unit_" + unit ).getInt( null );
      String text = v.getResources().getString( uid, amount );
      ((TextView) v.findViewById( R.id.amountView )).setText( text );
    }catch( Exception ignored ){
      ignored.printStackTrace();
    }
  }

  @Override
  public String getCreateSql() {
    return "create table if not exists item ( "
        + "itemInfoId integer not null references iteminfo( _id ), "
        + "listId integer not null references list( _id ), "
        + "amount real not null, "
        + "unit text not null, "
        + super.getCreateSql();
  }

}
