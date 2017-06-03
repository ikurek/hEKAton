package dzikiekuny.com.hekaton.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import dzikiekuny.com.hekaton.Models.Event;


/**
 * Created by XHaXor on 02.06.2017.
 */

public class Place implements ClusterItem {

    private Event event;
    private final LatLng mPosition;

    public Place(Event ev) {
        event = ev;
        mPosition = new LatLng(ev.getLat(), ev.getLng());
    }


    public Place(LatLng latlng){
        mPosition = latlng;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Event getEvent() {
        return event;
    }
}
