package io.embarque.embarque.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.embarque.embarque.R;
import io.embarque.embarque.adapters.AirportDetailsAdapter;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.events.FeedbackCreatedEvent;
import io.embarque.embarque.services.BusService;
import io.embarque.embarque.tracker.EmbarqueTracker;
import io.embarque.embarque.widgets.SlidingTabLayout;

public class AirportInformationActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.cover) ImageView cover;
    @InjectView(R.id.layer) View layer;
    @InjectView(R.id.tabs) SlidingTabLayout tabs;
    @InjectView(R.id.view_pager) ViewPager viewPager;

    int pageSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ParseData.selectedAirport.getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AirportDetailsAdapter adapter = new AirportDetailsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);

        tabs.setContentDescription(0, getString(R.string.details));
        tabs.setContentDescription(1, getString(R.string.history));

        tabs.setDistributeEvenly(true);

        tabs.setViewPager(viewPager);

        if (ParseData.selectedAirport.getParseFile("cover") != null) {
            Picasso.with(this).load(ParseData.selectedAirport.getParseFile("cover").getUrl())
                    .fit().centerCrop().into(cover);
            toolbar.setBackgroundResource(android.R.color.transparent);
        } else {
            cover.setVisibility(View.GONE);
            layer.setVisibility(View.GONE);
        }

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageSelected = position;
            }

            @Override
            public void onPageSelected(int position) {
                //
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (pageSelected == 0) {
                        EmbarqueTracker.trackScreen("Detail Screen");
                    } else {
                        EmbarqueTracker.trackScreen("History Screen");
                    }
                }
            }
        });

        // default
        EmbarqueTracker.trackScreen("Detail Screen");

        BusService.getBus().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create) {
            if (pageSelected == 0) {
                EmbarqueTracker.trackEvent("Detail", "Plus Selected");
            } else {
                EmbarqueTracker.trackEvent("History", "Plus Selected");
            }
            Intent intent = new Intent(this, CreateFeedbackActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onPause() {

        if (isFinishing()) {
            BusService.getBus().unregister(this);
        }

        super.onPause();
    }

    @Subscribe
    public void onFeedbackCreated(FeedbackCreatedEvent event) {
        viewPager.setCurrentItem(1);
    }
}
