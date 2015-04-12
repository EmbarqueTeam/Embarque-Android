package io.embarque.embarque;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
import io.embarque.embarque.util.DistanceComparator;

public class MainActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @InjectView(R.id.airports) RecyclerView airports;
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

        Intent intent = new Intent(this, AirportInformationActivity.class);
        startActivity(intent);
    }

    private void getAirports() {
        if (ParseData.airports != null) {
            if (location != null) {
                orderAirports();
            }
            return;
        }

        // clearAllAchedResults
        ParseQuery.getQuery("Airport").setLimit(200)
                .setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK)
                .findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e != null) {
                            // error
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
                    parseObject.add("distance", parseGeoPoint.distanceInKilometersTo(parseObject.getParseGeoPoint("location")));
                }
            }

            Collections.sort(ParseData.airports, new DistanceComparator());
        }

        adapter.setAirports(ParseData.airports);
    }
}
