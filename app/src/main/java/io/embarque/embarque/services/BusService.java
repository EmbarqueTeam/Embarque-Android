package io.embarque.embarque.services;

import com.squareup.otto.Bus;

public class BusService {
    private static Bus INSTANCE;

    private BusService() {}

    public static Bus getBus() {
        if (INSTANCE == null) {
            INSTANCE = new Bus();
        }

        return INSTANCE;
    }
}
