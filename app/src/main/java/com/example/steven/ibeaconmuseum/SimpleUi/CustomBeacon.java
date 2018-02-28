package com.example.steven.ibeaconmuseum.SimpleUi;

import org.altbeacon.beacon.Identifier;

/**
 * Created by Steven on 2/27/2018.
 */

public class CustomBeacon {

    public Point location;
    public int id;

    public CustomBeacon(Point location, int id){
        this.location = location;
        this.id = id;
    }

}
