package dzikiekuny.com.hekaton.Models;

import java.util.Date;
import java.util.Map;

public class Event {
    private String name;
    private String description;
    private Date date;
    private double lat;
    private double lng;
    private Sport sport;

    public Event(String name, String description, Date date, Double lat, Double lng, Sport sport) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.sport = sport;
    }

    public Event() {

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

    public void setDescription() {
        this.description = "GRAMY W GAŁE";
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
