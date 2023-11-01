package com.kaze2.wt.dto;

import com.google.gson.annotations.SerializedName;

public class WorldTimeResponse {
  @SerializedName("datetime") // Notice the annotation; these are used to do the deserialisation, built into the Retrofit library
  private String timestamp;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
