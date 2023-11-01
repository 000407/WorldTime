package com.kaze2.wt.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  // Read about singleton pattern
  private static final String BASE_URL = "https://worldtimeapi.org";
  private static Retrofit retrofit;

  public static Retrofit getRetrofitInstance() {
    if (retrofit == null) {
      retrofit =
          new retrofit2.Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }

    return retrofit;
  }
}
