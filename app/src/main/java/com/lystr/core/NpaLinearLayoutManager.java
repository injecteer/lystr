package com.lystr.core;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Injecteer on 26.04.2016!
 */
public class NpaLinearLayoutManager extends LinearLayoutManager {


  public NpaLinearLayoutManager( Context context ) {
    super( context );
  }

  public NpaLinearLayoutManager( Context context, int orientation, boolean reverseLayout ) {
    super( context, orientation, reverseLayout );
  }

  public NpaLinearLayoutManager( Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ) {
    super( context, attrs, defStyleAttr, defStyleRes );
  }

  @Override
  public boolean supportsPredictiveItemAnimations() {
    return false;
  }
}
