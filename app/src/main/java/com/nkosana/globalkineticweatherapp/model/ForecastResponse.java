package com.nkosana.globalkineticweatherapp.model;

import java.util.ArrayList;

public class ForecastResponse {

    City city;

    private class City {
        public Integer id;
        public String name;
        public Coordinates coord;

        private class Coordinates {
            public Double lon, lat;
        }

        public String country;
        public Double population;
//		public Sys sys;
//
//		public class Sys{
//			Double population;
//		}
    }

    String cod;
    Double message;
    Integer cnt;

    public ArrayList<Daily> list;

    public class Daily {
        public Long dt;
        public Temp temp;

        public class Temp {
            public Double day;
            public Double min;
            public Double max;
            public Double night;
            public Double eve;
            public Double morn;
        }

        public Double pressure;
        public Double humidity;
        // Weather weather;
        public ArrayList<Weather> weather;

        public class Weather {
            public Integer id;
            public String main;
            public String description;
            public String icon;
        }

        public Double speed;
        public Double deg;
        public Double clouds;
    }
}
