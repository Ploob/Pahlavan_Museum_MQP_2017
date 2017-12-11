package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 11/29/2017.
 */
/*
    RoomGrid contains the gridpoints to show if the room exists in the given location
    HashMap.get(

 */
public class LocationRoomMap {

    public GridPoint[][] roomGrid;
    public HashMap<GridPoint, PointOfInterest> gridToPoi = new HashMap<>();
    public HashMap<GridPoint, Identifier> gridToBeacon = new HashMap<>(); // Beacon ID from location
    public HashMap<Identifier, GridPoint> beaconToGrid = new HashMap<>(); // Location of beacon from ID


    public LocationRoomMap(GridPoint[][] roomGrid, GridPoint[] poiLoc, PointOfInterest[] pois, GridPoint[] beaconLoc, Identifier[] beacons){
        this.roomGrid = roomGrid;
//TODO remake
        // Place each poi in the hashmap
        for(int i=0; i < poiLoc.length; i++){
            //gridToPoi.put(roomGrid[poiLoc[i].x][poiLoc[i].y], pois[i]);
        }

        // Place each beacon in the hashmap
        for(int i=0; i<beaconLoc.length; i++){
            //gridToBeacon.put(roomGrid[beaconLoc[i].x][beaconLoc[i].y], beacons[i]);
            //beaconToGrid.put(beacons[i], roomGrid[beaconLoc[i].x][beaconLoc[i].y]);
        }
    }

}
