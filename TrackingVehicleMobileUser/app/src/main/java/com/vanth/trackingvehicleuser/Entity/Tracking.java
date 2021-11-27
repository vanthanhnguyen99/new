package com.vanth.trackingvehicleuser.Entity;

import java.time.LocalDateTime;

public class Tracking {
    String id_vehicle;
    LocalDateTime tracktime;
    float x;
    float y;

    public String getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(String id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public LocalDateTime getTracktime() {
        return tracktime;
    }

    public void setTracktime(LocalDateTime tracktime) {
        this.tracktime = tracktime;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
