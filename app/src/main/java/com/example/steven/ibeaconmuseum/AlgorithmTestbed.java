package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;

import org.altbeacon.beacon.Beacon;

import java.util.List;

/**
 * Created by Steven on 12/3/2017.
 */

public class AlgorithmTestbed {

    public double alpha;


    public AlgorithmTestbed(){}

    public GridPoint newMethod(List<Beacon> beacons){
        return new GridPoint(-1,-1);
    }

}
