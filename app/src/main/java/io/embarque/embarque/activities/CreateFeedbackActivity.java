package io.embarque.embarque.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.embarque.embarque.R;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.tracker.EmbarqueTracker;
import io.embarque.embarque.util.SeekBarStagedControl;

public class CreateFeedbackActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.cover) ImageView cover;
    @InjectView(R.id.layer) View layer;
    @InjectView(R.id.feedback_content) LinearLayout feedbackContent;

    SeekBarStagedControl[] seekBarStagedControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ParseData.selectedAirport.getString("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ParseData.selectedAirport.getParseFile("cover") != null) {
            Picasso.with(this).load(ParseData.selectedAirport.getParseFile("cover").getUrl())
                    .fit().centerCrop().into(cover);
            toolbar.setBackgroundResource(android.R.color.transparent);
        } else {
            cover.setVisibility(View.GONE);
            layer.setVisibility(View.GONE);
        }

        setUpView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        EmbarqueTracker.trackEvent("Feedback Main", "Cancelled");
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        EmbarqueTracker.trackEvent("Feedback Main", "Cancelled");
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EmbarqueTracker.trackScreen("Feedback Main Screen");
    }

    private void setUpView() {
        // I'm sorry
        int[] titles = {
                R.string.pontualidade,
                R.string.informacoes,
                R.string.wifi,
                R.string.alimentacao,
                R.string.conservacao,
                R.string.seguranca
        };

        int[] icons = {
                R.drawable.clock,
                R.drawable.question,
                R.drawable.wifi,
                R.drawable.food,
                R.drawable.trashcan,
                R.drawable.security
        };

        int totalItems = titles.length;

        seekBarStagedControls = new SeekBarStagedControl[totalItems];

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < totalItems; i++) {
            View view = inflater.inflate(R.layout.view_feedback_item, feedbackContent, false);

            ((ImageView) view.findViewById(R.id.icon)).setImageResource(icons[i]);
            ((TextView) view.findViewById(R.id.label)).setText(titles[i]);

            seekBarStagedControls[i] = new SeekBarStagedControl((SeekBar) view.findViewById(R.id.seek), 7);

            feedbackContent.addView(view);
        }

        View view = inflater.inflate(R.layout.view_send_button, feedbackContent, false);

        view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        feedbackContent.addView(view);
    }

    private void sendFeedback() {
        ParseObject feedback = new ParseObject("Feedback");

        feedback.put("punctuality", seekBarStagedControls[0].getSelectedPos() - 3);
        feedback.put("information", seekBarStagedControls[1].getSelectedPos() - 3);
        feedback.put("wifi", seekBarStagedControls[2].getSelectedPos() - 3);
        feedback.put("food", seekBarStagedControls[3].getSelectedPos() - 3);
        feedback.put("conservation", seekBarStagedControls[4].getSelectedPos() - 3);
        feedback.put("security", seekBarStagedControls[5].getSelectedPos() - 3);
        feedback.put("airport", ParseData.selectedAirport);

        ParseData.currentFeedback = feedback;

        feedback.saveEventually();

        EmbarqueTracker.trackEvent("Feedback Main", "Sent");

        Intent intent = new Intent(this, AccurateFeedback.class);
        startActivity(intent);
    }
}
