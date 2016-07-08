package com.lystr.domain;

import com.commons.android.vorm.DomainClass;
import com.commons.android.vorm.annotation.NotBlank;

public class ItemInfo extends DomainClass {

  @NotBlank
  public String name;

  public String imageUrl;

  @Override
  public String getCreateSql() {
    return "create table if not exists iteminfo ( "
        + "name text not null, "
        + "imageUrl text not null, "
        + super.getCreateSql();
  }

}
