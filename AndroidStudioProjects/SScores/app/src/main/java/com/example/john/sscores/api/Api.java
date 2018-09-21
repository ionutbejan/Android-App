package com.example.john.sscores.api;

import com.example.john.sscores.api.apiresponse.LeaguesResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

interface Api {
    @GET(RestClient.HIGHLIGHTS)
    Call<Response> getHighlights();

    @GET(RestClient.LEAGUES)
    Call<LeaguesResponse> getLeagues();
}
