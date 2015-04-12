package io.embarque.embarque.tracker;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.embarque.embarque.R;

public class EmbarqueTracker {
    private static Tracker tracker;

    public static void init(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        tracker = analytics.newTracker(R.xml.global_tracker);
    }

    public static Tracker getTracker() {
        return tracker;
    }

    public static void trackScreen(String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void trackEvent(String category, String action, String label) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label);
        tracker.send(builder.build());
    }

    public static void trackEvent(String category, String action) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);
        tracker.send(builder.build());
    }
}
