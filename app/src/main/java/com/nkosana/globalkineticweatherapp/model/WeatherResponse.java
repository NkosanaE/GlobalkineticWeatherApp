package com.nkosana.globalkineticweatherapp.model;

import java.util.ArrayList;

public class WeatherResponse {

    Coordinates coord;

    private class Coordinates {
        public Double lon, lat;
    }

    // Weather weather;
    public ArrayList<Weather> weather;

    public class Weather {
        public Double id;
        public String main;
        public String description;
        public String icon;
    }

    String base;

    public Main main;

    public class Main {
        public Double temp;
        public Double pressure;
        public Double humidity;
        public Double temp_min, temp_max;
    }

    //Double visibility;
    public Wind wind;

    public class Wind {
        public Double speed;
        public Double deg;
    }

    //	public Rain rain;
    public Clouds clouds;

    public class Clouds {
        public Double all;
    }

    public Long dt;

    public Sys sys;

    public class Sys {
        public Double message;
        public String country;
        public Long sunrise;
        public Long sunset;
    }

    public Long id;
    public String name;
    public Double cod;

}
