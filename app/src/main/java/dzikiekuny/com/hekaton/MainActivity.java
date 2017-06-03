package dzikiekuny.com.hekaton;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.Profile;

import de.hdodenhof.circleimageview.CircleImageView;
import dzikiekuny.com.hekaton.Activity.AddNewActivity;
import dzikiekuny.com.hekaton.Fragments.EventsFragment;
import dzikiekuny.com.hekaton.Fragments.MapFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String MAP_STRING = "mapFragment";
    private static final String EVENTS_STRING = "eventsFragment";
    private final MapFragment mapFragment = new MapFragment();
    private final EventsFragment eventsFragment = new EventsFragment();
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Start","App");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNewActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);

        CircleImageView profilePicture = (CircleImageView) headerView.findViewById(R.id.imageView);
        TextView userTextView = (TextView) headerView.findViewById(R.id.nameLabel);
        Glide.with(MainActivity.this).load("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large").into(profilePicture);
        userTextView.setText(Profile.getCurrentProfile().getName());

        fragment = mapFragment;
        fragmentManager = getSupportFragmentManager();
        currentFragmentTag = MAP_STRING;
        fragmentManager.beginTransaction().replace(R.id.frameLayoutForFragments, fragment, currentFragmentTag).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if(id == R.id.nav_list) {
            startEventsFragment();
        } else if (id == R.id.nav_home) {
            startMapFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startMapFragment(){
        if (fragment != null) {
            fragment = mapFragment;
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            currentFragmentTag=MAP_STRING;
            fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment, currentFragmentTag).commit();
        }

    }

    private void startEventsFragment() {
        if (fragment != null) {
            fragment = eventsFragment;
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            currentFragmentTag = EVENTS_STRING;
            fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment, currentFragmentTag).commit();
        }

    }
}
