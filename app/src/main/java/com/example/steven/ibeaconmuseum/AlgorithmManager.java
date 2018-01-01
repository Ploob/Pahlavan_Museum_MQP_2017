package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoomMap;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.special.Erf;

import java.util.List;

import static java.lang.Math.*;

public class AlgorithmManager {

    private double sigma; // Sigma of the readings
    private double certainty; // % certainty of a signal
    private double dbTolerance; // DB tolerance to be within based on the sigma and certainty
    private int granularity; // X by X points of measurement per square meter

    public AlgorithmManager(double sigma, double certainty, int granularity){
        this.sigma = sigma;
        this.certainty = certainty;
        this.granularity = granularity;
        this.dbTolerance = this.sigma * sqrt(2) * Erf.erfcInv(2 - 2 * this.certainty);
    }

    public GridPoint MaximumLikelihoodRoomLocation(LocationRoomMap roomMap, List<Beacon> readBeacons){
        int numBeacons = readBeacons.size(); // Total number of beacons seen

        /*
            Begin by calculating the theoretical RSSI values for each point
            The RSSI values are calculated based on the beacons seen
         */

        int[][] theoreticalReadingGrid;
        for(int i=0; i<roomMap.roomGrid.length; i++){
            for(int j=0; j<roomMap.roomGrid[i].length; j++){

            }
        }

        int[][] scoreGrid; // Score for the room
        for(int i=0; i<roomMap.roomGrid.length; i++){
            for(int j=0; j<roomMap.roomGrid[i].length; j++){

            }
        }
    }

    public double getDbTolerance(){ return this.dbTolerance; }
    public double getCertainty(){ return this.certainty; }
    public double getSigma(){ return this.sigma; }

}
