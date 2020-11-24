package com.nkosana.globalkineticweatherapp.model;

public class City {

    String id, name, code;
    Double lat, lng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = Double.parseDouble(lat);
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = Double.parseDouble(lng);
    }
}
