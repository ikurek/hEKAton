package dzikiekuny.com.hekaton.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Adapter.UserViewAdapter;
import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.Models.UserModel;
import dzikiekuny.com.hekaton.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by wodzu on 02.06.17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    String url = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";
    String url1 = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";
    private LatLng slodowa = new LatLng(51.116162, 17.037725);
    private View rootView;
    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<EventModel> events;
    private ClusterManager<EventModel> clusterManager;
    private SlidingUpPanelLayout slidingLayout;
    RequestQueue mRequestQueue;
    private View slidingView;
    private UserModel myUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public UserModel jsonUsersParser(JSONObject userObject) throws JSONException {

        return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));


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
    private JsonArrayRequest getUserFacebook(final String facebookID) {
        return new JsonArrayRequest
                (Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            myUser = jsonUsersParser(response, facebookID);
                            if(myUser==null)
                                Log.i("CHuj", ":(((");
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map_fragment);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        slidingLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        slidingView = rootView.findViewById(R.id.event_contentView);
        slidingLayout.setPanelHeight(0);
        slidingLayout.setTouchEnabled(false);

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        clusterManager = new ClusterManager<EventModel>(getActivity(), googleMap);

        DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(getEvents());
        mRequestQueue.add(getUserFacebook(Profile.getCurrentProfile().getId()));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingLayout.setPanelHeight(0);
            }
        });

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<EventModel>() {
            @Override
            public boolean onClusterClick(Cluster<EventModel> cluster) {
                Log.i("XHaXor","Cluster clicked");
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<EventModel>() {
            @Override
            public boolean onClusterItemClick(EventModel event) {
                Log.i("XHaXor","Cluster item clicked");
                //((TextView) rootView.findViewById(R.id.nameTextView)).setText(place.getTitle());

                slidingLayout.setPanelHeight(300);
                setSlidingView(event);
                return false;
            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder().target(slodowa).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setSlidingView(final EventModel ev) {
        if(myUser!=null)
        slidingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder md = new MaterialDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final ArrayList<UserModel> users = new ArrayList<>();
                final View stdView = factory.inflate(R.layout.members_layout, null);
                ListView listView = (ListView) stdView.findViewById(R.id.users_list);
                final UserViewAdapter adapter = new UserViewAdapter(getContext(), R.layout.user_row, users);
                listView.setAdapter(adapter);
                String[] splitUsers = ev.getMembers().split("☺");
                for (int i = 0; i < splitUsers.length; ++i) {
                    JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/users/" + splitUsers[i] + "?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                UserModel myUser = jsonUsersParser(response);
                                users.add(myUser);
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
                    mRequestQueue.add(getUser);
                }


                md.customView(stdView, false);
                String guzik;

                if (false)
                    guzik = "Wyjdź";
                else
                    guzik = "Dolacz";
                md.title("Bioracy udzial")
                        .negativeText("Anuluj")
                        .positiveText(guzik)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                UserModel newUser = new UserModel(myUser.getName(), myUser.getFbid(), myUser.getJoined(), myUser.getId());
                                if(newUser.getJoined().length()<3)
                                    newUser.setJoined(ev.getId());
                                else
                                    newUser.setJoined(newUser.getJoined()+"☺"+ev.getId());
                                mRequestQueue.add(updateUser(newUser, newUser.getId()));
                                mRequestQueue.add(getEvent(ev.getId()));
                            }
                        })
                        .build()
                        .show();
            }
        });
        ImageView iv = (ImageView) slidingLayout.findViewById(R.id.map_event_imageView);
        TextView title = (TextView) slidingLayout.findViewById(R.id.map_event_title);
        TextView subtitle = (TextView) slidingLayout.findViewById(R.id.map_event_subtitle);

        iv.setImageDrawable(Sport.valueOf(ev.getSportID()).getDrawable(getApplicationContext()));
        title.setText(ev.getName());
        subtitle.setText(ev.getDeadlineDate());

    }
    public EventModel jsonEventParser(JSONObject eventObject) throws JSONException {

        return new EventModel(eventObject.getString("name"), eventObject.getString("deadline_date"), eventObject.getString("user_id"), eventObject.getString("description"), eventObject.getString("members"), eventObject.getString("lat"), eventObject.getString("lng"), eventObject.getString("sport_id"), eventObject.getString("id"));

    }
    private JsonObjectRequest updateEvent(EventModel event, String eventID){
        JSONObject params = new JSONObject();
        try {
            params.put("name", event.getName());
            params.put("deadline_date", event.getDeadlineDate());
            params.put("user_id", event.getUserID());
            params.put("description", event.getDescription());
            params.put("members", event.getMembers());
            params.put("lat", event.getLat());
            params.put("lng", event.getLng());
            params.put("sport_id", event.getSportID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.PATCH,  "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0", params,  new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Działa", response.toString());
                // pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }
    private JsonObjectRequest getEvent(String eventID){
        Log.i("Event link", "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0");
        return new JsonObjectRequest
                (Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/events/"+eventID+"?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            EventModel myEvent =  jsonEventParser(response);
                            myEvent.setMembers(myEvent.getMembers()+"☺"+myUser.getId());
                            mRequestQueue.add(updateEvent(myEvent, myEvent.getId()));

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
    private JsonObjectRequest updateUser(UserModel user, String userID){
        JSONObject params = new JSONObject();
        try {
            params.put("name", user.getName());
            params.put("fbid", user.getFbid());
            params.put("joined", user.getJoined());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.PATCH,  "http://dzikiekuny.azurewebsites.net/tables/users/"+userID+"?ZUMO-API-VERSION=2.0.0", params,  new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Działa", response.toString());
                // pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }
    public ArrayList<EventModel> jsonEventsParser(JSONArray eventsArray) throws JSONException {
        ArrayList<EventModel> events = new ArrayList<>();
        for (int i = 0; i < eventsArray.length(); ++i) {
            JSONObject eventObject = eventsArray.getJSONObject(i);
            events.add(new EventModel(eventObject.getString("name"), eventObject.getString("deadline_date"), eventObject.getString("user_id"), eventObject.getString("description"), eventObject.getString("members"), eventObject.getString("lat"), eventObject.getString("lng"), eventObject.getString("sport_id"), eventObject.getString("id")));
        }
        return events;

    }

    private JsonArrayRequest getEvents() {
        return new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            events = jsonEventsParser(response);
                            Log.i("Events size", String.valueOf(events.size()));

                            for (EventModel eventModel : events) {
                                clusterManager.addItem(eventModel);
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


}