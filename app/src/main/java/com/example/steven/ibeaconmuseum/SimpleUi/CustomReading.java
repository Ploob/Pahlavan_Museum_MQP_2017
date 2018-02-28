package com.example.steven.ibeaconmuseum.SimpleUi;

import org.altbeacon.beacon.Identifier;

/**
 * Created by Steven on 2/28/2018.
 */

public class CustomReading {

    public double value;
    public Identifier id;

    public CustomReading(Identifier id, Double value){
        this.id = id;
        this.value = value;
    }

}
