package dzikiekuny.com.hekaton;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
            ev.setDescription("GRAMY W GAŁE");
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
