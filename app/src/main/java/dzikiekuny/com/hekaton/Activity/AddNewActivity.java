package dzikiekuny.com.hekaton.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.Profile;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dzikiekuny.com.hekaton.Adapter.SportAdapter;
import dzikiekuny.com.hekaton.Models.EventModel;
import dzikiekuny.com.hekaton.Models.Sport;
import dzikiekuny.com.hekaton.Models.UserModel;
import dzikiekuny.com.hekaton.R;

/**
 * Created by igor on 03.06.17.
 */

public class AddNewActivity extends AppCompatActivity {

    RecyclerView sports;
    UserModel myUser;
    private SportAdapter adapter;
    List<Sport> userList = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    RequestQueue mRequestQueue;
    private TextView location;
    String url = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";
    String url1 = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";
    private Place myPlace;
    int PLACE_PICKER_REQUEST = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new);
        this.setTitle("");
        DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
        BasicNetwork network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);

        mRequestQueue.start();
        mRequestQueue.add(getUserFacebook(Profile.getCurrentProfile().getId()));
        ImageView time = (ImageView) findViewById(R.id.btn_time);
        final EditText description = (EditText) findViewById(R.id.description);
        ImageView date = (ImageView) findViewById(R.id.btn_date);
        final EditText name = (EditText)findViewById(R.id.name);
        Button save = (Button) findViewById(R.id.save);
        ImageView chooseLocation = (ImageView) findViewById(R.id.choose_location);
        sports = (RecyclerView) findViewById(R.id.sports);

        final TextView txtTime = (TextView) findViewById(R.id.in_time);
        final TextView txtDate = (TextView) findViewById(R.id.in_date);
        location = (TextView) findViewById(R.id.location);
        final Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txtTime.setText(mHour + ":" + mMinute);
        txtDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);

        for (Sport sp : Sport.values()) {
            userList.add(sp);
            Log.i("Wartosc sp", sp.toString());
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        sports.setLayoutManager(llm);
        this.adapter = new SportAdapter(userList, getApplicationContext(), this);
        sports.setAdapter(adapter);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AddNewActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myUser!=null){
                    EventModel eventModel = new EventModel(name.getText().toString(),
                            c.getTime().toString(), myUser.getId(), description.getText().toString(), myUser.getId(),
                            String.valueOf(myPlace.getLatLng().latitude), String.valueOf(myPlace.getLatLng().longitude),
                            Sport.values()[adapter.currentSelected()].toString());
                    mRequestQueue.add(insertEvent(eventModel));

                }
            }
        });
    }
    public void refresh(){
        adapter.notifyDataSetChanged();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                myPlace = place;
                location.setText(place.getName());
            }
            Log.i("Result code", String.valueOf(resultCode));
        }
    }
    public UserModel jsonUsersParser(JSONArray users, String fbID) throws JSONException {
        for(int i = 0; i<users.length(); ++i){
            if(users.getJSONObject(i).getString("fbid").equals(fbID)) {
                JSONObject userObject = users.getJSONObject(i);
                return new UserModel(userObject.getString("name"), userObject.getString("fbid"), userObject.getString("joined"), userObject.getString("id"));
            }
        }
        return null;

    }
    private JsonArrayRequest getUserFacebook(final String facebookID) {
        return new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            myUser = jsonUsersParser(response, facebookID);
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
    }
    private JsonObjectRequest updateUser(UserModel user, String userID){
        JSONObject params = new JSONObject();
        try {
            params.put("name", user.getName());
            params.put("fbid", user.getFbid());
            params.put("joined", user.getJoined());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.PATCH,  "http://dzikiekuny.azurewebsites.net/tables/users/"+userID+"?ZUMO-API-VERSION=2.0.0", params,  new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Działa", response.toString());
                // pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }
    private JsonObjectRequest insertEvent(EventModel event){
        JSONObject params = new JSONObject();
        try {
            params.put("name", event.getName());
            params.put("deadline_date", event.getDeadlineDate());
            params.put("user_id", event.getUserID());
            params.put("description", event.getDescription());
            params.put("members", event.getMembers());
            params.put("lat", event.getLat());
            params.put("lng", event.getLng());
            params.put("sport_id", event.getSportID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.POST,
                url1, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Działa", response.toString());
                        UserModel newUser = new UserModel(myUser.getName(), myUser.getFbid(), myUser.getJoined(), myUser.getId());
                            try {
                                Log.i("ID", response.getString("id"));
                                if(myUser.getJoined().length()<3)
                                newUser.setJoined(response.getString("id"));
                                else
                                    newUser.setJoined(newUser.getJoined()+"☺"+response.getString("id"));
                                mRequestQueue.add(updateUser(newUser, newUser.getId()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        finish();
                        // pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error volley", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });
    }


}