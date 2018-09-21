package com.example.john.sscores.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RestClient {
    private static final String BASE_URL = "https://soccer.sportmonks.com/api/v2.0/";
    private static final String API_KEY = "?3mPOuFJzGnI28IaJj2iHeW23k0iMooSJoPfjD00vsU8oP6RoKQv2RTa0IAQL";

    public static final String HIGHLIGHTS = BASE_URL + Endpoints.highlights + API_KEY;
    public static final String LEAGUES = BASE_URL + Endpoints.leagues + API_KEY;

    private static RestClient instance = null;
    private Api api;

    private RestClient() {

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        OkHttpClient client = okHttpBuilder.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .build();

        api = retrofit.create(Api.class);
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }

        return instance;
    }

    public Api getApi() {
        return api;
    }
}
