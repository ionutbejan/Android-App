package com.example.john.sscores.api.apiresponse;

import com.example.john.sscores.api.data.League;

import java.util.ArrayList;

public class LeaguesResponse {
    ArrayList<League> data;

    public ArrayList<League> getData() {
        return data;
    }

    public void setData(ArrayList<League> data) {
        this.data = data;
    }
}
