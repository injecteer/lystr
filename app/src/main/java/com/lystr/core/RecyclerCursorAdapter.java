package com.lystr.core;

import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Injecteer on 07.12.2015.
 */
public class RecyclerCursorAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

  protected Cursor cursor;

  protected LayoutInflater inflater;

  protected boolean firstExtraItem = false;

  @LayoutRes
  protected int itemId;

  public Map<String,Object> binding = new HashMap<>();

  public RecyclerCursorAdapter( Cursor cursor, LayoutInflater inflater, int itemId ) {
    super();
    this.cursor = cursor;
    this.inflater = inflater;
    this.itemId = itemId;
  }

  public RecyclerCursorAdapter( Cursor cursor, LayoutInflater inflater, int itemId, boolean firstExtraItem ) {
    this( cursor, inflater, itemId );
    this.firstExtraItem = firstExtraItem;
  }

  @Override
  public SimpleViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
    return new SimpleViewHolder( inflater.inflate( itemId, parent, false ) );
  }

  @Override
  public void onBindViewHolder( SimpleViewHolder holder, int position ) {
    if( firstExtraItem ){
      if( 0 != position ) cursor.moveToPosition( position - 1 );
    }else
      cursor.moveToPosition( position );
  }

  @Override
  public int getItemCount() {
    int c = null == cursor ? 0 : cursor.getCount();
    return c + ( firstExtraItem ? 1 : 0 );
  }

  public void changeCursor( Cursor newCursor ){
    if( newCursor == cursor ) return;
    if( null != cursor && !cursor.isClosed() ) cursor.close();
    cursor = newCursor;
    if( cursor != null ) notifyDataSetChanged();
  }

  public void refreshCursor() {
    cursor.requery();
    notifyDataSetChanged();
  }

  public Cursor getCursor() { return cursor; }

  public void close() {
    if( null != cursor && !cursor.isClosed() ) cursor.close();
  }
}
