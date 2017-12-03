package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Identifier;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 11/29/2017.
 */

/*
  Class that manages one buildings layout
    Contains the building in which the application is working,
    and contains LocationRooms per room in the building.
    Each room is also paired with its Major ID used for lookup
*/
public class Location {

    public String name;
    public HashMap<Identifier, LocationRoom> locationRoomHashMap = new HashMap<>();

    public Location(String name, List<LocationRoom> locationRooms){
        this.name = name;

        // Populate the hashmap with the given rooms
        for(LocationRoom room : locationRooms){
            locationRoomHashMap.put(room.majorId, room);
        }

    }

}
