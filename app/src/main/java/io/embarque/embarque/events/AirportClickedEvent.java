package io.embarque.embarque.events;

import com.parse.ParseObject;

public class AirportClickedEvent {
    public int airportPosition;

    public AirportClickedEvent(int airportPosition) {
        this.airportPosition = airportPosition;
    }
}
