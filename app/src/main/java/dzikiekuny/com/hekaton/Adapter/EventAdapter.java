package dzikiekuny.com.hekaton.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dzikiekuny.com.hekaton.Models.Event;
import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.Models.UserModel;
import dzikiekuny.com.hekaton.R;

import static java.security.AccessController.getContext;

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
    public UserModel jsonUsersParser(JSONObject userObject) throws JSONException {

            return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View cell = mInflater.inflate(R.layout.event_cell, parent, false);
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog.Builder md = new MaterialDialog.Builder(mContext);
                LayoutInflater factory = LayoutInflater.from(mContext);
                final ArrayList<UserModel> users = new ArrayList<>();
                final View stdView = factory.inflate(R.layout.members_layout, null);
                ListView listView = (ListView) stdView.findViewById(R.id.users_list);
                final UserViewAdapter adapter = new UserViewAdapter(mContext, R.layout.user_row, users);
                listView.setAdapter(adapter);
                String users1 = mDataSource.get(position).getMembers();
                String[] splitUsers = users1.split("☺");
                DiskBasedCache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
                BasicNetwork network = new BasicNetwork(new HurlStack());
                RequestQueue mRequestQueue = new RequestQueue(cache, network);

                mRequestQueue.start();
                for (int i = 0; i < splitUsers.length; ++i) {
                    JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/users/" + splitUsers[i] + "?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                UserModel myUser = jsonUsersParser(response);
                                users.add(myUser);
                                adapter.notifyDataSetChanged();
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
                    mRequestQueue.add(getUser);
                }


                        md.customView(stdView, false);
                        String guzik;
                        if (true)
                            guzik = "Wyjdź";
                        else
                            guzik = "Dolacz";
                        md.title("Bioracy udzial")
                                .negativeText("Anuluj")
                                .positiveText(guzik)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    }
                                })
                                .build()
                                .show();
                    }
                });


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
