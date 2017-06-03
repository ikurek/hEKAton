package dzikiekuny.com.hekaton.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.Date;
import java.util.Map;

import dzikiekuny.com.hekaton.R;

public class Event {
    private String name;
    private String description;
    private Date date;
    private double lat;
    private double lng;
    private Sport sport;

    public Event(Map<String, Object> dict) {
        name = (String) dict.get("name");
        description = (String) dict.get("desc");
        //...

    }

    public Event() {
        this.name = "Besztanie androida";
        this.description = "wspolne besztanie chujowego systemy";
        this.date = new Date();
        this.lat= 54.3;
        this.lng= 54.3;
        this.sport = Sport.Running;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
