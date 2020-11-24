package com.nkosana.globalkineticweatherapp.model;

public class ItemLocation {

    private String id, name, code; //city attribute
    private String jsonWeather;
    private String jsonForecast;

    public ItemLocation() {
        super();
    }

    public ItemLocation(String id, String name, String code,
                        String jsonWeather, String jsonForecast) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.jsonWeather = jsonWeather;
        this.jsonForecast = jsonForecast;
    }

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

    public String getJsonWeather() {
        return jsonWeather;
    }

    public void setJsonWeather(String jsonWeather) {
        this.jsonWeather = jsonWeather;
    }

    public String getJsonForecast() {
        return jsonForecast;
    }

    public void setJsonForecast(String jsonForecast) {
        this.jsonForecast = jsonForecast;
    }
}
