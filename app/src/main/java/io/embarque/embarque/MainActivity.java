package io.embarque.embarque;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.activities.AirportInformationActivity;
import io.embarque.embarque.adapters.AirportListAdapter;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.events.AirportClickedEvent;
import io.embarque.embarque.services.BusService;
import io.embarque.embarque.tracker.EmbarqueTracker;
import io.embarque.embarque.util.DistanceComparator;
import io.embarque.embarque.widgets.FixedRecyclerView;

public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @InjectView(R.id.airports) FixedRecyclerView airports;
    @InjectView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;

    @InjectView(R.id.toolbar) Toolbar toolbar;

    private GoogleApiClient googleApiClient;
    private Location location = null;
    private AirportListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        airports.setLayoutManager(layoutManager);

        adapter = new AirportListAdapter();
        airports.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAirports();
            }
        });

        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusService.getBus().register(this);

        // track view
        EmbarqueTracker.trackScreen("Airports Screen");
    }

    @Override
    protected void onPause() {
        BusService.getBus().unregister(this);
        super.onPause();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        getAirports();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        getAirports();
    }

    @Subscribe
    public void onAirportClicked(AirportClickedEvent event) {
        ParseData.selectedAirport = ParseData.airports.get(event.airportPosition);

        EmbarqueTracker.trackEvent("Airports", "Airport Selected", ParseData.selectedAirport.getString("name"));

        Intent intent = new Intent(this, AirportInformationActivity.class);
        startActivity(intent);
    }

    private void getAirports() {
        ParseQuery.getQuery("Airport").setLimit(200)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK)
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        swipeRefresh.setRefreshing(false);
                        if (e != null && e.getCode() != ParseException.CACHE_MISS) {
                            showErrorMessage();
                            return;
                        }

                        // data
                        ParseData.airports = parseObjects;
                        orderAirports();
                    }
                });
    }

    private void orderAirports() {
        if (location != null) {
            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

            for (ParseObject parseObject : ParseData.airports) {
                if (parseObject.getParseGeoPoint("location") != null) {
                    parseObject.put("distance", parseGeoPoint.distanceInKilometersTo(parseObject.getParseGeoPoint("location")));
                }
            }

            Collections.sort(ParseData.airports, new DistanceComparator());
        }

        adapter.setAirports(ParseData.airports);
    }

    private void showErrorMessage() {
        Toast.makeText(
                this,
                R.string.parse_error,
                Toast.LENGTH_LONG
        ).show();
    }
}
