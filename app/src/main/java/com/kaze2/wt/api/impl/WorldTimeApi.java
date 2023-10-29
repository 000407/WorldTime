package com.kaze2.wt.api.impl;

import com.google.gson.Gson;
import com.kaze2.wt.api.TimeApi;
import com.kaze2.wt.dto.WorldTimeResponse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorldTimeApi implements TimeApi {
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    @Override
    public ZonedDateTime getTimeAtZone(String timeZone) throws IOException {
        final Request request = new Request.Builder()
                .url("https://worldtimeapi.org/api/timezone/" + timeZone)
                .build();

        final Call call = client.newCall(request);
        final Response response = call.execute();
        final String body = response.body().string();

        //Deserialization using GSON

        //1. Create GSON object
        final Gson gson = new Gson();

        //2. Deserialize the JSON string using gson.fromJson(String, Class<T>) method
        final WorldTimeResponse time = gson.fromJson(body, WorldTimeResponse.class);

        //3. Parse the datetime string as a ZonedDateTime instance and return.
        return ZonedDateTime.parse(time.getDatetime());
    }
}
