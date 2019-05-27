package com.internship.models.locationModel;

import java.util.List;

public class LocationsAndDefaultKey {
    private List<Location> locations;
    private int defaultkey;

    public List<Location> getLocations() {
        return locations;
    }

    public int getDefaultkey() {
        return defaultkey;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setDefaultkey(int defaultkey) {
        this.defaultkey = defaultkey;
    }

}
