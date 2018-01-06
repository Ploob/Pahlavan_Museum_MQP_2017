package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Identifier;

import java.util.HashMap;

/**
 * Created by Steven on 1/6/2018.
 */

public class ReadingBeaconPair {

    public HashMap<Identifier, Double> readingMap;

    public ReadingBeaconPair(){
        readingMap = new HashMap<>();
    }



}
