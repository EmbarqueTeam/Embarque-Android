package io.embarque.embarque;

import android.app.Application;

import com.parse.Parse;

import io.embarque.embarque.tracker.EmbarqueTracker;

public class EmbarqueApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "KEnFNcnLArxlllOus00LNLjM6KosLi11tTJn7Aes", "ZefV9QEgxnAnsHS8im6sg0R49evcyYOKVTWWsFDb");
        EmbarqueTracker.init(this);
    }
}
