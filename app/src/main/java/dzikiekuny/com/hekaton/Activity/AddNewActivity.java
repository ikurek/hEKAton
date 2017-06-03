package dzikiekuny.com.hekaton.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import dzikiekuny.com.hekaton.Models.SportModel;
import dzikiekuny.com.hekaton.R;

<<<<<<<Updated upstream
        =======
        >>>>>>>Stashed changes

/**
 * Created by igor on 03.06.17.
 */

public class AddNewActivity extends AppCompatActivity {
<<<<<<<Updated upstream
    private final List<SportModel> userList = new ArrayList<>();
    RecyclerView sports;
=======
    UserModel myUser;
    List<Sport> userList = new ArrayList<>();
    String url = "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0";
    String url1 = "http://dzikiekuny.azurewebsites.net/tables/events?ZUMO-API-VERSION=2.0.0";
    int PLACE_PICKER_REQUEST = 1;
    Stashed changes
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SportAdapter adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView location;
>>>>>>>
    private Place myPlace;

    {
        @Override
        public void onClick (View v){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(AddNewActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    }

=======
        chooseLocation.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        if (myUser != null) {
            EventModel eventModel = new EventModel(name.getText().toString(),
                    c.getTime().toString(), myUser.getId(), description.getText().toString(), "",
                    String.valueOf(myPlace.getLatLng().latitude), String.valueOf(myPlace.getLatLng().longitude),
                    Sport.values()[adapter.currentSelected()].toString());
            mRequestQueue.add(insertEvent(eventModel));

        }
    }
    })
            save.setOnClickListener(new View.OnClickListener()
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new);
        ImageView time = (ImageView) findViewById(R.id.btn_time);
        ImageView date = (ImageView) findViewById(R.id.btn_date);
<<<<<<<Updated upstream
        RecyclerView sports = (RecyclerView) findViewById(R.id.sports);
=======
        final EditText name = (EditText) findViewById(R.id.name);
        Button save = (Button) findViewById(R.id.save);
        ImageView chooseLocation = (ImageView) findViewById(R.id.choose_location);
        sports = (RecyclerView) findViewById(R.id.sports);

>>>>>>>Stashed changes
        final TextView txtTime = (TextView) findViewById(R.id.in_time);
        final TextView txtDate = (TextView) findViewById(R.id.in_date);
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        txtTime.setText(mHour + ":" + mMinute);
        txtDate.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
        userList.add(new SportModel("Hitler"));
        userList.add(new SportModel("Did"));
        userList.add(new SportModel("Nothing"));
        userList.add(new SportModel("Wrong"));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        sports.setLayoutManager(llm);
        sports.setAdapter(new SportAdapter(userList, getApplicationContext()));
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
<<<<<<<Updated upstream
    })
}

    public void refresh() {
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
        for (int i = 0; i < users.length(); ++i) {
            if (users.getJSONObject(i).getString("fbid").equals(fbID)) {
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

    private JsonObjectRequest insertEvent(EventModel event) {
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
>>>>>>>Stashed changes

}