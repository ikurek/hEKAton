package dzikiekuny.com.hekaton.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


/**
 * Created by XHaXor on 02.06.2017.
 */

public class Place implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;

    public Place(Double lat, Double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }

    public Place(Double lat, Double lng){
        mPosition = new LatLng(lat, lng);
        mTitle = "";
    }

    public Place(LatLng latlng){
        mPosition = latlng;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }
}
