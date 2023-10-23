package com.kaze2.wt.dto;

public class WorldTimeResponse {
    private String datetime;
    private Long unixtime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(Long unixtime) {
        this.unixtime = unixtime;
    }
}
