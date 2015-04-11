package io.embarque.embarque.util;

import com.parse.ParseObject;

import java.util.Comparator;

public class DistanceComparator implements Comparator<ParseObject> {
    @Override
    public int compare(ParseObject lhs, ParseObject rhs) {
        return (int) (lhs.getDouble("distance") - rhs.getDouble("distance"));
    }
}
