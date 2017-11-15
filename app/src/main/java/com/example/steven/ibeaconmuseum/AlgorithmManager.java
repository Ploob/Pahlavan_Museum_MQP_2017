package com.example.steven.ibeaconmuseum;

/**
 * Created by Steven on 11/15/2017.
 */

import static java.lang.Math.*;

public class AlgorithmManager {

    public double pathLossDistance(int rssi, double alpha, int rssiOneMeter){
        return pow(10, ((double)rssiOneMeter - (double)rssi)/(10 * alpha));
    }

}
