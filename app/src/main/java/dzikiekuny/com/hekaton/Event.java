package dzikiekuny.com.hekaton;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kacperraczy on 03.06.2017.
 */

enum Sport {
    Football, Basketball, Volleyball, TableTennis, Tennis, Cycling, Running;

    Drawable getDrawable(Context context) {
        Drawable result = null;
        switch (this) {
            case  Football:
                result = context.getResources().getDrawable(R.drawable.football);
                break;
            case  Basketball:
                result = context.getResources().getDrawable(R.drawable.basketball);
                break;
            case Volleyball:
                result = context.getResources().getDrawable(R.drawable.volleyball);
                break;
            case TableTennis:
                result = context.getResources().getDrawable(R.drawable.tabletennis);
                break;
            case Tennis:
                result = context.getResources().getDrawable(R.drawable.tennis);
                break;
            case Cycling:
                result = context.getResources().getDrawable(R.drawable.cycling);
                break;
            case Running:
                result = context.getResources().getDrawable(R.drawable.running);
                break;
        }
        return result;
    }
}

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
