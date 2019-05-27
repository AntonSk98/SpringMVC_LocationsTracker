package com.internship.models.locationModel;



import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Location implements Serializable //pojo класс для представления таблицы хранящей данные location
{
    private double lat;
    private double lng;
    private LocalDateTime time;
    public Location(){
    }
    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setNullTime(){
        this.time= null;
    }
    public void setTime(String time) {
        this.time = LocalDateTime.parse(time.replace(' ','T')); //возвращает данные с буквой T как разделитель
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDateTime getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", time=" + time +
                '}';
    }
}
