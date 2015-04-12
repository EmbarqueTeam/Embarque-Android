package io.embarque.embarque.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.embarque.embarque.R;
import io.embarque.embarque.adapters.AirportDetailsAdapter;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.widgets.SlidingTabLayout;

public class AirportInformationActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.tabs) SlidingTabLayout tabs;
    @InjectView(R.id.view_pager) ViewPager viewPager;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.add_report)
    public void onAddReport() {
        Intent intent = new Intent(this, CreateFeedbackActivity.class);
        startActivity(intent);
    }
}
