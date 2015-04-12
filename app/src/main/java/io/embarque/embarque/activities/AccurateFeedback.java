package io.embarque.embarque.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.embarque.embarque.R;
import io.embarque.embarque.data.ParseData;

public class AccurateFeedback extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.cover) ImageView cover;
    @InjectView(R.id.company) EditText company;
    @InjectView(R.id.flight) EditText flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accurate_feedback);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ParseData.selectedAirport.getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ParseData.selectedAirport.getParseFile("cover") != null) {
            Picasso.with(this).load(ParseData.selectedAirport.getParseFile("cover").getUrl())
                    .fit().centerCrop().into(cover);
            toolbar.setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @OnClick(R.id.send)
    public void onSend() {
        ParseData.currentFeedback.put("company", company.getText().toString());
        ParseData.currentFeedback.put("flight", flight.getText().toString());

        ParseData.currentFeedback.saveEventually();

        close();
    }

    @OnClick(R.id.skip)
    public void onSkip() {
        close();
    }

    private void close() {
        finish();
    }
}
