package dzikiekuny.com.hekaton.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
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

import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by wodzu on 02.06.17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    String url = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";
    private LatLng slodowa = new LatLng(51.116162, 17.037725);
    private View rootView;
    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<EventModel> events;
    private ClusterManager<EventModel> clusterManager;
    private SlidingUpPanelLayout slidingLayout;
    private View slidingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        slidingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("iosdev", "clicked");
            }
        });

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        clusterManager = new ClusterManager<EventModel>(getActivity(), googleMap);

        DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        mRequestQueue.add(getEvents());

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

    private void setSlidingView(EventModel ev) {
        ImageView iv = (ImageView) slidingLayout.findViewById(R.id.map_event_imageView);
        TextView title = (TextView) slidingLayout.findViewById(R.id.map_event_title);
        TextView subtitle = (TextView) slidingLayout.findViewById(R.id.map_event_subtitle);

        iv.setImageDrawable(Sport.valueOf(ev.getSportID()).getDrawable(getApplicationContext()));
        title.setText(ev.getName());
        subtitle.setText(ev.getDeadlineDate());

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