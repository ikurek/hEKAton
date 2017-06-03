package dzikiekuny.com.hekaton.Models;

/**
 * Created by wodzu on 03.06.17.
 */

public class UserModel {
    private String name;
    private String fbid;
    private String joined;
    private String id;

    public UserModel(String name, String fbid, String joined, String id) {
        this.name = name;
        this.fbid = fbid;
        this.id = id;
        this.joined = joined;
    }

    public UserModel(String name, String fbid) {
        this.name = name;
        this.fbid = fbid;
        this.joined = "XD";
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

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }
}
