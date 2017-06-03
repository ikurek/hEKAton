package dzikiekuny.com.hekaton.DatabaseConnection;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dzikiekuny.com.hekaton.Models.UserModel;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by igor on 03.06.17.
 */

public class DatabaseSupport {

    //DATA
    private final DiskBasedCache cache;
    private final BasicNetwork network;
    //URLs
    private final String usersUrl = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";
    private RequestQueue mRequestQueue;
    private String facebookID;
    private UserModel myUser;
    private final JsonArrayRequest getUserAsRequest = new JsonArrayRequest
            (Request.Method.GET, usersUrl, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        myUser = jsonUsersParser(response, facebookID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });

    public DatabaseSupport() {

        cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);

    }

    public void insertUserAsModel(UserModel userToInsert) {
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(insertUserAsRequest(userToInsert));
    }

    public UserModel getUserAsModel(String facebookid) {
        this.facebookID = facebookid;
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(getUserAsRequest);
        return myUser;
    }

    public UserModel jsonUsersParser(JSONArray users, String fbID) throws JSONException {
        for (int i = 0; i < users.length(); ++i) {
            if (users.getJSONObject(i).getString("fbid").equals(fbID)) {
                JSONObject userObject = users.getJSONObject(i);
                return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));
            }
        }
        return null;

    }


    private JsonObjectRequest insertUserAsRequest(UserModel user) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", user.getName());
            params.put("fbid", user.getFbid());
            params.put("joined", user.getJoined());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.POST, usersUrl, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Działa", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
            }
        });
    }

}
