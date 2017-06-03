package dzikiekuny.com.hekaton.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Adapter.EventAdapter;
import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.R;

public class EventsFragment extends Fragment {

    private ListView listView;

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


        listView = (ListView) view.findViewById(R.id.event_list_view);
        ArrayList<EventModel> events = new ArrayList<>(2);
//        for (int i = 0; i < 2; i++) {
//            EventModel ev = new EventModel();
//            ev.setName("Football " + i);
//            ev.setDescription();
//            ev.setLng(43);
//            ev.setLng(44);
//            ev.setDate(new Date());
//            ev.setSport(Sport.values()[i]);
//            events.add(ev);
//        }

        EventAdapter adapter = new EventAdapter(getActivity(), events);
        listView.setAdapter(adapter);

        return view;
    }

}
