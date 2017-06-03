package dzikiekuny.com.hekaton.Models;

/**
 * Created by wodzu on 03.06.17.
 */

public class EventModel {
    String name;
    String deadlineDate;
    String userID;
    String description;
    String members;
    String lat;
    String lng;
    String sportID;
    String id;
    public EventModel(String name, String deadlineDate, String userID, String description, String members, String lat, String lng, String sportID) {
        this.name = name;
        this.deadlineDate = deadlineDate;
        this.userID = userID;
        this.description = description;
        this.members = members;
        this.lat = lat;
        this.lng = lng;
        this.sportID = sportID;
    }

    public EventModel(String name, String deadlineDate, String userID, String description, String members, String lat, String lng, String sportID, String id) {
        this.name = name;
        this.deadlineDate = deadlineDate;
        this.userID = userID;
        this.description = description;
        this.members = members;
        this.lat = lat;
        this.lng = lng;
        this.sportID = sportID;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
