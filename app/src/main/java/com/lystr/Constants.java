package com.lystr;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Injecteer on 23.03.2016.
 */
public class Constants {

//  public static final String HOST_NAME = "ec2-52-28-83-247.eu-central-1.compute.amazonaws.com:"; // test
  public static final String HOST_NAME = "ec2-52-29-136-229.eu-central-1.compute.amazonaws.com:1"; // prod

  public static final String URL_BASE = "http://" + HOST_NAME; // test

  public static final SimpleDateFormat SDF_FB = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault() );

  public static final SimpleDateFormat SDF_GOOGLE = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );

  public static final String EMAIL_REGEXP = "^[\\w\\._%+-]+@[\\w\\.-]+\\.[a-zA-Z]{2,4}$";

  public static final String S3_URL = "https://_BUCKET_.s3.amazonaws.com/";

  public static final long[] VIBRATE_PATTERN = { 0, 300, 260, 800 };

}
