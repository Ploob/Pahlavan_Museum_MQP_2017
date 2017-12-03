package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Identifier;

/**
 * Created by Steven on 11/29/2017.
 */

public class LocationRoom {

    public LocationRoomMap roomMap;
    public Identifier majorId;

    public double roomAlpha;

    public LocationRoom(LocationRoomMap roomMap, Identifier majorId, double roomAlpha){
        this.roomMap = roomMap;
        this.roomAlpha = roomAlpha;
        this.majorId = majorId;
    }

}
