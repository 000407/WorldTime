package com.kaze2.wt.api.impl;

import com.kaze2.wt.api.TimeApi;

import java.io.IOException;
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
    public String getTimeAtZone(String timeZone) throws IOException {
        final Request request = new Request.Builder()
                .url("https://worldtimeapi.org/api/timezone/" + timeZone)
                .build();

        final Call call = client.newCall(request);
        final Response response = call.execute();
        return response.body().string();
    }
}
