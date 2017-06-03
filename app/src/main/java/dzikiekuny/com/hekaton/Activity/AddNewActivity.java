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

import java.util.ArrayList;
import java.util.List;

import dzikiekuny.com.hekaton.Adapter.SportAdapter;
import dzikiekuny.com.hekaton.Models.SportModel;
import dzikiekuny.com.hekaton.R;

/**
 * Created by igor on 03.06.17.
 */

public class AddNewActivity extends AppCompatActivity {
    List<SportModel> userList = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new);
        ImageView time = (ImageView) findViewById(R.id.btn_time);
        ImageView date = (ImageView) findViewById(R.id.btn_date);
        RecyclerView sports = (RecyclerView) findViewById(R.id.sports);
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
        userList.add(new SportModel(R.id.tick, "Hitler"));
        userList.add(new SportModel(R.id.tick, "Did"));
        userList.add(new SportModel(R.id.tick, "Nothing"));
        userList.add(new SportModel(R.id.tick, "Wrong"));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
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
    }


}