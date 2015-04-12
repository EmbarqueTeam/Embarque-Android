package io.embarque.embarque.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.embarque.embarque.R;
import io.embarque.embarque.data.ParseData;
import io.embarque.embarque.events.FeedbackCreatedEvent;
import io.embarque.embarque.services.BusService;

public class AccurateFeedback extends ActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.cover) ImageView cover;
    @InjectView(R.id.layer) View layer;
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
        } else {
            cover.setVisibility(View.GONE);
            layer.setVisibility(View.GONE);
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
        BusService.getBus().post(new FeedbackCreatedEvent());

        Toast.makeText(this, R.string.feedback_saved, Toast.LENGTH_LONG).show();

        finish();
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        // all touch events close the keyboard before they are processed except EditText instances.
        // if focus is an EditText we need to check, if the touchevent was inside the focus editTexts
        final View currentFocus = getCurrentFocus();
        if (!(currentFocus instanceof EditText) || !isTouchInsideView(ev, currentFocus)) {
            try {
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (NullPointerException ignored) {}
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * determine if the given motionevent is inside the given view.
     *
     * @param ev
     *            the given view
     * @param currentFocus
     *            the motion event.
     * @return if the given motionevent is inside the given view
     */
    private boolean isTouchInsideView(final MotionEvent ev, final View currentFocus) {
        final int[] loc = new int[2];
        currentFocus.getLocationOnScreen(loc);
        return ev.getRawX() > loc[0] && ev.getRawY() > loc[1] && ev.getRawX() < (loc[0] + currentFocus.getWidth())
                && ev.getRawY() < (loc[1] + currentFocus.getHeight());
    }
}
