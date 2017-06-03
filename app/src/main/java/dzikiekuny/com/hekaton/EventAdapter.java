package dzikiekuny.com.hekaton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dzikiekuny.com.hekaton.Models.Event;

/**
 * Created by kacperraczy on 03.06.2017.
 */

public class EventAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Event> mDataSource;
    private LayoutInflater mInflater;


    public EventAdapter(Context context, ArrayList<Event> events) {
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
        View cell = (View) mInflater.inflate(R.layout.event_cell,parent,false);
        TextView title = (TextView) cell.findViewById(R.id.event_title);
        TextView subTitle = (TextView) cell.findViewById(R.id.event_subtitle);
        TextView detail = (TextView) cell.findViewById(R.id.event_detail);
        ImageView imageView = (ImageView) cell.findViewById(R.id.event_imageView);

        Event event = mDataSource.get(position);
        title.setText(event.getName());

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM hh:mm");
        subTitle.setText(formatter.format(event.getDate()));
        imageView.setImageDrawable(event.getSport().getDrawable(mContext));
        cell.setBackground(mContext.getDrawable(R.drawable.customshape));

        return cell;
    }
}
