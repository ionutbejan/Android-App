package com.example.john.sscores.api.data;

public class League {
    private int id;
    private int legacy_id;
    private int country_id;
    private String name;
    private boolean is_cup;
    private int current_season_id;
    private int current_round_id;
    private int current_stage_id;
    private boolean live_standings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLegacy_id() {
        return legacy_id;
    }

    public void setLegacy_id(int legacy_id) {
        this.legacy_id = legacy_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_cup() {
        return is_cup;
    }

    public void setIs_cup(boolean is_cup) {
        this.is_cup = is_cup;
    }

    public int getCurrent_season_id() {
        return current_season_id;
    }

    public void setCurrent_season_id(int current_season_id) {
        this.current_season_id = current_season_id;
    }

    public int getCurrent_round_id() {
        return current_round_id;
    }

    public void setCurrent_round_id(int current_round_id) {
        this.current_round_id = current_round_id;
    }

    public int getCurrent_stage_id() {
        return current_stage_id;
    }

    public void setCurrent_stage_id(int current_stage_id) {
        this.current_stage_id = current_stage_id;
    }

    public boolean isLive_standings() {
        return live_standings;
    }

    public void setLive_standings(boolean live_standings) {
        this.live_standings = live_standings;
    }
}
