package com.lystr.domain;

import android.database.Cursor;

import com.commons.android.vorm.DomainClass;
import com.commons.android.vorm.ORMSupport;

public class List extends DomainClass {

  public String name;

  public Cursor  getItems() {
    return ORMSupport.db().rawQuery( "select i._id, i.unit, i.amount, ii._id as 'ii_id', ii.name as 'ii_name', ii.imageUrl as 'ii_imageUrl' from item i join iteminfo ii on i.itemInfoId=ii._id where i.listId=? order by i.timestamp asc", new String[]{ NF.format( id ) } );
  }

  @Override
  public String getCreateSql() {
    return "create table if not exists list ( "
        + "name text unique not null, "
        + super.getCreateSql();
  }

}
