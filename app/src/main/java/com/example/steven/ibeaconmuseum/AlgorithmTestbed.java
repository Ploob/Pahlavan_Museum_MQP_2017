package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.special.Erf;

import java.util.List;
import java.lang.Math.*;

import static java.lang.Math.sqrt;

/**
 * Created by Steven on 12/3/2017.
 */

public class AlgorithmTestbed {

    public double alpha;
    public double sigma = 3.1;

    public AlgorithmTestbed(){}

    public GridPoint newMethod(List<Beacon> beacons){
        return new GridPoint(-1,-1);
        //return new GridPoint(sigma * sqrt(2) * Erf.erfcInv(2-2*0.9), -1);
    }




}
