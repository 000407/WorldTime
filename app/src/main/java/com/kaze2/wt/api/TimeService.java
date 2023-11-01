package com.kaze2.wt.api;

import com.kaze2.wt.dto.WorldTimeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimeService {
  // Here we are only specifying WHAT we need to do. HOW it is done, is injected by Retrofit at runtime
  @GET("/api/timezone/{continent}/{city}") // Notice the annotation (i.e. @METHOD("/path/to/the/resource/{id}")
  Call<WorldTimeResponse> getTimeAtZone( // Retrofit will inject how the HTTP request is sent, and how the response is deserialised (we DON'T have do deserialise the response body manually.
      @Path("continent") String continent, @Path("city") String city); // Read about URL Path Parameters
}
