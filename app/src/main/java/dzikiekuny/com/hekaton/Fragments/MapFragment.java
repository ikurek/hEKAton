package dzikiekuny.com.hekaton.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Models.Event;
import dzikiekuny.com.hekaton.Models.Place;
import dzikiekuny.com.hekaton.R;

/**
 * Created by wodzu on 02.06.17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final LatLng slodowa = new LatLng(51.116162, 17.037725);

    private View rootView;

    private MapView mapView;
    private GoogleMap googleMap;

    private ArrayList<Event> events;

    private ClusterManager<Place> clusterManager;
    private SlidingUpPanelLayout slidingLayout;

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
        slidingLayout.setPanelHeight(0);
        slidingLayout.setTouchEnabled(false);

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        clusterManager = new ClusterManager<>(getContext(), googleMap);

        addItems();

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingLayout.setPanelHeight(0);
            }
        });

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Place>() {
            @Override
            public boolean onClusterClick(Cluster<Place> cluster) {
                Log.i("XHaXor","Cluster clicked");
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Place>() {
            @Override
            public boolean onClusterItemClick(Place place) {
                Log.i("XHaXor","Cluster item clicked");
                ((TextView) rootView.findViewById(R.id.nameTextView)).setText(place.getTitle());

                slidingLayout.setPanelHeight(300);
                return false;
            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder().target(slodowa).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addItems() {

        // TODO: Download events

        events = new ArrayList<Event>();
        for (int i = 0; i < 10; i++) {
            events.add(new Event());
        }

        for (Event e : events) {
            clusterManager.addItem(new Place(new LatLng(e.getLat(), e.getLng())));
        }

        // TODO: Later delete, just for tests

        // Set some lat/lng coordinates to start with.
        double lat = 51.116162;
        double lng = 17.037725;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 100d;
            lat = lat + offset;
            lng = lng + offset;
            Place offsetItem = new Place(lat, lng, "Test " + Integer.toString(i));
            clusterManager.addItem(offsetItem);
        }
    }

}
