package dzikiekuny.com.hekaton.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Adapter.EventAdapter;
import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.UserModel;
import dzikiekuny.com.hekaton.R;

public class EventsFragment extends Fragment {
    String url = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";
    String url1 = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";
    EventAdapter adapter;
    ArrayList<EventModel> events = new ArrayList<>();
    private ListView listView;
    private UserModel myUser;
    private RequestQueue mRequestQueue;
    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        DiskBasedCache cache = new DiskBasedCache(getContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(getUserFacebook(Profile.getCurrentProfile().getId()));
        listView = (ListView) view.findViewById(R.id.event_list_view);



        adapter = new EventAdapter(getActivity(), events);
        listView.setAdapter(adapter);

        return view;
    }
    private JsonArrayRequest getUserFacebook(final String facebookID) {
        return new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            myUser = jsonUsersParser(response, facebookID);
                            events.clear();
                            String[] eventsID = myUser.getJoined().split("☺");
                            for(int i = 0; i<eventsID.length; ++i){
                                mRequestQueue.add(getEvent(eventsID[i]));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }
    public UserModel jsonUsersParser(JSONArray users, String fbID) throws JSONException {
        for(int i = 0; i<users.length(); ++i){
            if(users.getJSONObject(i).getString("fbid").equals(fbID)) {
                JSONObject userObject = users.getJSONObject(i);
                return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));
            }
        }
        return null;

    }
    public EventModel jsonEventParser(JSONObject eventObject) throws JSONException {

            return new EventModel(eventObject.getString("name"), eventObject.getString("deadline_date"), eventObject.getString("user_id"), eventObject.getString("description"), eventObject.getString("members"), eventObject.getString("lat"), eventObject.getString("lng"), eventObject.getString("sport_id"), eventObject.getString("id"));

    }
    private JsonObjectRequest getEvent(String eventID){
        Log.i("Event link", "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0");
        return new JsonObjectRequest
                (Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            EventModel myEvent =  jsonEventParser(response);
                            events.add(myEvent);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
    }

}
