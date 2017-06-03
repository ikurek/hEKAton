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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dzikiekuny.com.hekaton.Models.Event;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.R;
import dzikiekuny.com.hekaton.Models.Place;

/**
 * Created by wodzu on 02.06.17.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private LatLng slodowa = new LatLng(51.116162, 17.037725);

    private View rootView;

    private MapView mapView;
    private GoogleMap googleMap;

    private ArrayList<Event> events;

    private ClusterManager<Place> clusterManager;
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

        clusterManager = new ClusterManager<Place>(getActivity(), googleMap);

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
                //((TextView) rootView.findViewById(R.id.nameTextView)).setText(place.getTitle());

                slidingLayout.setPanelHeight(300);
                setSlidingView(place.getEvent());
                return false;
            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder().target(slodowa).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setSlidingView(Event ev) {
        ImageView iv = (ImageView) slidingLayout.findViewById(R.id.map_event_imageView);
        TextView title = (TextView) slidingLayout.findViewById(R.id.map_event_title);
        TextView subtitle = (TextView) slidingLayout.findViewById(R.id.map_event_subtitle);

        iv.setImageDrawable(ev.getSport().getDrawable(getActivity().getApplicationContext()));
        title.setText(ev.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM hh:mm");
        subtitle.setText(formatter.format(ev.getDate()));

    }

    private void addItems() {

        // TODO: Download events

        events = new ArrayList<Event>();
        double lat = 51.116162;
        double lng = 17.037725;

        for (int i=0; i<10; i++){
            Event ev = new Event("Name " + i, "Desc", new Date(), lat, lng, Sport.Football);
            double offset = i / 100d;
            ev.setLat(lat+offset);
            ev.setLng(lng+offset);
            events.add(ev);
            clusterManager.addItem(new Place(ev));
        }


    }

}