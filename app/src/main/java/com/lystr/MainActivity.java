package com.lystr;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.commons.android.Logg;
import com.commons.android.vorm.ORMSupport;
import com.lystr.core.RecyclerCursorAdapter;
import com.lystr.core.SimpleViewHolder;
import com.lystr.domain.Item;
import com.lystr.domain.ItemInfo;
import com.lystr.domain.List;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.Locale;

import butterknife.Bind;

public class MainActivity extends SingletonAwareActivity implements View.OnClickListener {

  @Bind( R.id.newItemView ) View newItemView;

  @Bind( R.id.fab ) FloatingActionButton fab;

  @Bind( R.id.list ) RecyclerView recyclerView;

  @Bind( R.id.nameView ) EditText itemName;

  @Bind( R.id.amountView ) EditText amountView;

  @Bind( R.id.units ) Spinner units;

  private RecyclerCursorAdapter adapter;

  private List list;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    getSupportActionBar().setDisplayHomeAsUpEnabled( true );
    drawerHelper.drawerToggle.setDrawerIndicatorEnabled( true );
    toolbar.setNavigationOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        drawerHelper.drawerLayout.openDrawer( GravityCompat.START );
      }
    } );

    recyclerView.setLayoutManager( new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false ) );
    adapter = new RecyclerCursorAdapter( null, getLayoutInflater(), R.layout.item ){
      @Override
      public void onBindViewHolder( SimpleViewHolder holder, int position ) {
        super.onBindViewHolder( holder, position );
        Item i = ORMSupport.fromSql( Item.class, cursor );
        ItemInfo ii = ORMSupport.fromSql( ItemInfo.class, cursor, "ii_" );
        View v = holder.itemView;
        i.fillView( v );
        ii.fillView( v );
        getApp().loadImage( ii.imageUrl, v.findViewById( R.id.imageView ) );
        v.setOnClickListener( MainActivity.this );
      }
    };
    recyclerView.setAdapter( adapter );
  }

  @Override
  protected void onStart() {
    super.onStart();
    long id = getIntent().getLongExtra( "listId", -1 );
    if( -1 != id )
      list = ORMSupport.get( List.class, id );
    else
      list = ORMSupport.findBy( List.class, "name", "" );

    if( null == list ){
      list = new List();
      list.name = "";
      list.save();
    }

    getSupportActionBar().setTitle( list.name );
    adapter.changeCursor( list.getItems() );
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    getMenuInflater().inflate( R.menu.main, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    int id = item.getItemId();

    if( id == R.id.action_settings ){
      return true;
    }

    return super.onOptionsItemSelected( item );
  }

  private void saveItem( ItemInfo ii ) {
    Item item = new Item();
    try{
      item.itemInfoId = ii.id;
      item.listId = list.id;
      item.unit = (String) units.getSelectedItem();
      item.amount = Float.parseFloat( amountView.getText().toString().trim() );
      item.save();
      adapter.refreshCursor();
    }finally{
      hideLoading();
    }
  }

  private void findImage( final ItemInfo itemInfo ){
    showLoading();
    String url = getResources().getString( R.string.search_url, Locale.getDefault().getLanguage(), Uri.encode( itemInfo.name ) );
    getApp().requestQueue.add( new StringRequest( url, new Response.Listener<String>() {
      @Override
      public void onResponse( String html ) {
        Elements imgs = Jsoup.parse( html ).getElementById( "ires" ).getElementsByTag( "img" );
        Logg.i( MainActivity.this, "found [" + imgs.size() + "] images" );
        itemInfo.imageUrl = imgs.get( 0 ).absUrl( "src" );
        itemInfo.save();
        saveItem( itemInfo );
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse( VolleyError error ) {
        if( null != error.networkResponse ) Logg.e( MainActivity.this, "error " + new String( error.networkResponse.data ).trim() );
        hideLoading();
      }
    } ) );
  }

  public void save( View view ) {
    String name = itemName.getText().toString().trim();
    ItemInfo ii = ORMSupport.get( ItemInfo.class, (Long) newItemView.getTag() );
    if( null == ii ) ii = ORMSupport.findBy( ItemInfo.class, "name", name );
    if( null == ii ){
      ii = new ItemInfo();
      ii.name = name;
      findImage( ii );
    }else
      saveItem( ii );
  }

  @Override
  public void onClick( View v ) {
    ItemInfo ii = ORMSupport.get( ItemInfo.class, (Long) v.getTag() );
    if( null != ii ) ii.fillView( newItemView );
  }
}
