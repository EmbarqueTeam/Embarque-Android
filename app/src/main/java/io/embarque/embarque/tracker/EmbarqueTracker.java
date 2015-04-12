package io.embarque.embarque.tracker;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
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
}
