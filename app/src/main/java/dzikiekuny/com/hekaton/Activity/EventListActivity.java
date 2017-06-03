package dzikiekuny.com.hekaton.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import dzikiekuny.com.hekaton.Adapter.EventAdapter;
import dzikiekuny.com.hekaton.Models.Event;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.R;

/**
 * Created by kacperraczy on 03.06.2017.
 */

public class EventListActivity extends Activity {
    private ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);


        listView = (ListView) findViewById(R.id.event_list_view);
        ArrayList<Event> events = new ArrayList<Event>(2);
        for(int i = 0; i<2; i++) {
            Event ev = new Event();
            ev.setName("Football " + i);
            ev.setDescription();
            ev.setLng(43);
            ev.setLng(44);
            ev.setDate(new Date());
            ev.setSport(Sport.values()[i]);
            events.add(ev);
        }

        EventAdapter adapter = new EventAdapter(this, events);
        listView.setAdapter(adapter);
    }
}
