package dzikiekuny.com.hekaton.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.R;

/**
 * Created by kacperraczy on 03.06.2017.
 */

public class EventAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<EventModel> mDataSource;
    private final LayoutInflater mInflater;


    public EventAdapter(Context context, ArrayList<EventModel> events) {
        mContext = context;
        mDataSource = events;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell = mInflater.inflate(R.layout.event_cell, parent, false);
        TextView title = (TextView) cell.findViewById(R.id.event_title);
        TextView subTitle = (TextView) cell.findViewById(R.id.event_subtitle);
        TextView detail = (TextView) cell.findViewById(R.id.event_detail);
        ImageView imageView = (ImageView) cell.findViewById(R.id.event_imageView);

        EventModel event = mDataSource.get(position);
        title.setText(event.getName());
        subTitle.setText(event.getDeadlineDate());

        imageView.setImageDrawable(Sport.valueOf(event.getSportID()).getDrawable(mContext));
        // detail.setText("XD");

        return cell;
    }
}
